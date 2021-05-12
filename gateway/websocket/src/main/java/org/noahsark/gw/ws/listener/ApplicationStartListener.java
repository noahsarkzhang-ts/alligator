package org.noahsark.gw.ws.listener;

import org.noahsark.gw.ws.context.ServerContext;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/3/13
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic("TopicTest-1");

        String nameSrv = "120.79.235.83:9876";
        String producerGroup = "gw-ws-produce1";
        String consumerGroup = "gw-ws-cousumer1";

        RocketmqProxy rocketmqProxy = new RocketmqProxy.Builder()
                .topic(topic)
                .consumerGroup(consumerGroup)
                .producerGroup(producerGroup)
                .namesrvAddr(nameSrv)
                .build();

        ServerContext.mqProxy = rocketmqProxy;

        logger.info("Application start!");
    }
}
