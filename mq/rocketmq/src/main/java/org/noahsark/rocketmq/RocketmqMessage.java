package org.noahsark.rocketmq;

import org.noahsark.mq.Message;

/**
 * RocketMQ 消息
 *
 * @author zhangxt
 * @date 2021/5/2
 */
public class RocketmqMessage implements Message {

    private String topic;

    private String tag;

    private String key;

    private byte[] content;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RocketmqMessage{" +
                "topic='" + topic + '\'' +
                ", tag='" + tag + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
