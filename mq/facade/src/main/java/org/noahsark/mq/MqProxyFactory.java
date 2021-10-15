package org.noahsark.mq;

import org.noahsark.rabbitmq.RabbitmqProxy;
import org.noahsark.rabbitmq.RabbitmqTopic;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/10/11
 */
public class MqProxyFactory {

    public static MqProxy createMqProxy(Map<String, String> config, List<String> topics) {
        MqProxy mqProxy = null;

        String dialect = config.get("dialect");
        if ("rabbitmq".equals(dialect)) {

            RabbitmqProxy.Builder builder = new RabbitmqProxy.Builder();
            builder.vhost(config.get("vhost"));
            builder.urls(config.get("urls"));
            builder.username(config.get("username"));
            builder.password(config.get("password"));

            // TODO 构建 topics
            List<RabbitmqTopic> rabbitmqTopics = new ArrayList<>();
            topics.stream().forEach(topic -> {
                RabbitmqTopic rabbitmqTopic = new RabbitmqTopic();
                rabbitmqTopic.setQueueName(topic);

                rabbitmqTopics.add(rabbitmqTopic);
            });
            builder.topics(rabbitmqTopics);

            return builder.build();
        } else if ("rocketmq".equals(dialect)) {
            RocketmqProxy.Builder builder = new RocketmqProxy.Builder();
            builder.consumerGroup(config.get("consumerGroup"));
            builder.producerGroup(config.get("producerGroup"));
            builder.namesrvAddr(config.get("nameSrv"));

            // TODO 构建 topics
            List<RocketmqTopic> rocketmqTopics = new ArrayList<>();
            topics.stream().forEach(topic -> {
                RocketmqTopic rocketmqTopic = new RocketmqTopic();
                rocketmqTopic.setTopic(topic);

                rocketmqTopics.add(rocketmqTopic);
            });
            builder.topics(rocketmqTopics);

            return builder.build();
        }

        return mqProxy;
    }
}
