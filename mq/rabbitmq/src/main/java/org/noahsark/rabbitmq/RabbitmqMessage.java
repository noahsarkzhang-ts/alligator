package org.noahsark.rabbitmq;

import org.noahsark.mq.Message;

/**
 * RabbitMQ 消息
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class RabbitmqMessage implements Message {

    private RabbitmqTopic topic;

    private byte[] content;

    public RabbitmqTopic getTopic() {
        return topic;
    }

    public void setTopic(RabbitmqTopic topic) {
        this.topic = topic;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
