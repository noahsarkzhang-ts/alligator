package org.noahsark.rocketmq;

import java.util.ArrayList;
import java.util.List;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Request;

/**
 * Created by hadoop on 2021/5/4.
 */
public class RocketmqProxy {

    private String producerGroup;

    private String consumerGroup;

    private String namesrvAddr;

    private List<RocketmqTopic> topics = new ArrayList<>();

    private RocketmqConsumer consumer;

    private RocketmqProducer producer;

    private RocketmqPromiseHolder promiseHolder;

    private RocketmqChannelHolder channelHolder;

    public RocketmqProxy() {
    }

    public RocketmqProxy(Builder builder) {
        this.producerGroup = builder.producerGroup;
        this.consumerGroup = builder.consumerGroup;
        this.namesrvAddr = builder.namesrvAddr;
        this.topics = builder.topics;

        init();
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    private void init() {
        producer = new RocketmqProducer(this.producerGroup, this.namesrvAddr);

        consumer = new RocketmqConsumer(this.consumerGroup, this.namesrvAddr);
        this.topics.stream().forEach(topic -> consumer.subscribe(topic));
        consumer.setProxy(this);

        promiseHolder = new RocketmqPromiseHolder(producer);
        channelHolder = new RocketmqChannelHolder(producer, promiseHolder);

    }

    public RpcPromise sendAsync(RocketmqTopic topic, Request request,
                                CommandCallback commandCallback,
                                int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);
        promise.invoke(this.promiseHolder, request, commandCallback, timeoutMillis);

        return promise;
    }

    public Object sendSync(RocketmqTopic topic, Request request, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);
        Object result = promise.invokeSync(this.promiseHolder, request, timeoutMillis);

        return result;
    }

    public void sendOneway(RocketmqTopic topic, Request request) {
        request.setRequestId(promiseHolder.nextId());

        RocketmqMessage msg = new RocketmqMessage();

        byte[] body = null;

        if (request instanceof MultiRequest) {
            body = MultiRequest.encode((MultiRequest) request);
        }
        msg.setContent(body);

        msg.setTopic(topic.getTopic());
        msg.setTag(topic.getTag());
        msg.setKey(topic.getKey());

        producer.sendOneway(msg);
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

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public List<RocketmqTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<RocketmqTopic> topics) {
        this.topics = topics;
    }

    public RocketmqConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(RocketmqConsumer consumer) {
        this.consumer = consumer;
    }

    public RocketmqProducer getProducer() {
        return producer;
    }

    public void setProducer(RocketmqProducer producer) {
        this.producer = producer;
    }

    public RocketmqPromiseHolder getPromiseHolder() {
        return promiseHolder;
    }

    public void setPromiseHolder(RocketmqPromiseHolder promiseHolder) {
        this.promiseHolder = promiseHolder;
    }

    public RocketmqChannelHolder getChannelHolder() {
        return channelHolder;
    }

    public void setChannelHolder(RocketmqChannelHolder channelHolder) {
        this.channelHolder = channelHolder;
    }

}
