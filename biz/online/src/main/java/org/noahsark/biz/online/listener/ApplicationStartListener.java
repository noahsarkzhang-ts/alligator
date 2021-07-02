package org.noahsark.biz.online.listener;

import org.noahsark.biz.online.config.CommonConfig;
import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/3/13
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);

    @Autowired
    private CommonConfig config;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        String nameSrv = config.getMqProxy().getNameSrv();
        String consumerGroup = config.getMqProxy().getConsumerGroup();
        String producerGroup = config.getMqProxy().getProducerGroup();

        List<RocketmqTopic> topics = new ArrayList<>();

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(config.getMqProxy().getTopic());
        topics.add(topic);

        RocketmqTopic userEventTopic = new RocketmqTopic();
        userEventTopic.setTopic(config.getSysEvent().getUserTopic());
        topics.add(userEventTopic);

        RocketmqTopic serviceEventTopic = new RocketmqTopic();
        serviceEventTopic.setTopic(config.getSysEvent().getServiceTopic());
        topics.add(serviceEventTopic);

        RocketmqProxy rocketmqProxy = new RocketmqProxy.Builder()
                .topics(topics)
                .consumerGroup(consumerGroup)
                .producerGroup(producerGroup)
                .namesrvAddr(nameSrv)
                .build();

        rocketmqProxy.start();

        ServerContext.mqProxy = rocketmqProxy;

        logger.info("Application start!");
    }
}
