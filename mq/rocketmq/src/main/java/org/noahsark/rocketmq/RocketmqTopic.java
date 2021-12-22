package org.noahsark.rocketmq;

import org.noahsark.mq.Topic;

/**
 * RocketMQ TOPIC
 *
 * @author zhangxt
 * @date 2021/5/2
 */
public class RocketmqTopic implements Topic {

    private String topic;

    private String tag;

    private String key;

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

    @Override
    public String toString() {
        return "RocketmqTopic{" +
                "topic='" + topic + '\'' +
                ", tag='" + tag + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
