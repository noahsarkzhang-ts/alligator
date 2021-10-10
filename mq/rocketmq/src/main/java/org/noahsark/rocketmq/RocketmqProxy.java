package org.noahsark.rocketmq;

import org.noahsark.client.future.PromisHolder;
import org.noahsark.mq.*;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.session.ChannelHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2021/5/4.
 */
public class RocketmqProxy extends AbstractMqProxy {

    private String producerGroup;

    private String consumerGroup;

    private String namesrvAddr;

    private RocketmqConsumer consumer;

    private RocketmqProducer producer;

    private PromisHolder promiseHolder;

    private ChannelHolder channelHolder;

    private List<RocketmqTopic> topics = new ArrayList<>();

    public RocketmqProxy() {
    }

    public RocketmqProxy(Builder builder) {
        this.producerGroup = builder.producerGroup;
        this.consumerGroup = builder.consumerGroup;
        this.namesrvAddr = builder.namesrvAddr;
        this.topics = builder.topics;

        init();
    }

    private void init() {
        producer = new RocketmqProducer(this.producerGroup, this.namesrvAddr);

        consumer = new RocketmqConsumer(this.consumerGroup, this.namesrvAddr);
        this.topics.stream().forEach(topic -> consumer.subscribe(topic));
        consumer.registerMessageListener(new DefaultmqMessageListener(this));

        initHolder();
    }

    @Override
    protected void initHolder() {
        promiseHolder = new MqPromiseHolder(producer);
        channelHolder = new MqChannelHolder(producer, promiseHolder);
    }

    @Override
    public void sendOneway(Topic topic, Request request) {
        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);

        producer.sendOneway(producer.buildMessage(request));
    }

    @Override
    public void start() {
        producer.start();
        consumer.start();
    }

    @Override
    public void shutdown() {
        producer.shutdown();
        consumer.shutdown();
    }

    public static class Builder {
        private String producerGroup;

        private String consumerGroup;

        private String namesrvAddr;

        private List<RocketmqTopic> topics = new ArrayList<>();

        public Builder producerGroup(String producerGroup) {
            this.producerGroup = producerGroup;

            return this;
        }

        public Builder consumerGroup(String consumerGroup) {
            this.consumerGroup = consumerGroup;

            return this;
        }

        public Builder namesrvAddr(String namesrvAddr) {
            this.namesrvAddr = namesrvAddr;

            return this;
        }

        public Builder topics(List<RocketmqTopic> topics) {
            this.topics = topics;

            return this;
        }

        public Builder topic(RocketmqTopic topic) {
            this.topics.add(topic);

            return this;
        }

        public RocketmqProxy build() {
            return new RocketmqProxy(this);
        }
    }



    public RocketmqConsumer getConsumer() {
        return consumer;
    }

    public RocketmqProducer getProducer() {
        return producer;
    }

}
