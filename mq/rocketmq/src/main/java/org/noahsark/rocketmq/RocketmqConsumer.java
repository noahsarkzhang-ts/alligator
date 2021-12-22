package org.noahsark.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.noahsark.mq.Consumer;
import org.noahsark.mq.MessageListener;
import org.noahsark.mq.exception.MQOprationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * RocketMQ 消费者
 *
 * @author zhangxt
 * @date 2021/5/1
 */
public class RocketmqConsumer implements Consumer<RocketmqTopic> {

    private static Logger logger = LoggerFactory.getLogger(RocketmqConsumer.class);

    private static final String GROUP_NAME = UUID.randomUUID().toString();

    private List<RocketmqTopic> topics = new ArrayList<>();

    private MessageListener listener;

    private DefaultMQPushConsumer consumer;

    public RocketmqConsumer(String namesrvAddr) {
        consumer = new DefaultMQPushConsumer(GROUP_NAME);
        try {
            consumer.setNamesrvAddr(namesrvAddr);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    public RocketmqConsumer(String groupName, String namesrvAddr) {
        try {
            consumer = new DefaultMQPushConsumer(groupName);
            consumer.setNamesrvAddr(namesrvAddr);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }

    }

    @Override
    public void registerMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void subscribe(RocketmqTopic topic) {
        topics.add(topic);
    }

    @Override
    public void subscribe(List<RocketmqTopic> topics) {
        this.topics = topics;
    }

    @Override
    public void start() {

        try {
            topics.stream().forEach(topic -> {
                try {
                    consumer.subscribe(topic.getTopic(), topic.getTag());
                } catch (Exception ex) {
                    logger.error("catch an excepion.", ex);
                }
            });

            consumer.registerMessageListener(
                    (MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {

                        list.stream().forEach(messageExt -> {
                            try {
                                byte[] body = messageExt.getBody();
                                listener.consumeMessage(body);

                            } catch (Exception ex) {
                                logger.error("catch an exception.", ex);
                            }
                        });

                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    });

            consumer.start();

        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    @Override
    public void shutdown() {

        if (consumer != null) {
            consumer.shutdown();
        }
    }

}
