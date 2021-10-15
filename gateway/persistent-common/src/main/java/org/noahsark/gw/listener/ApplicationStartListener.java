package org.noahsark.gw.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.noahsark.gw.config.CommonConfig;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.mq.MqProxy;
import org.noahsark.mq.MqProxyFactory;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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

        /*List<RocketmqTopic> topics = new ArrayList<>();
        RocketmqTopic gwTopic = new RocketmqTopic();
        gwTopic.setTopic(config.getMqProxy().getTopic());
        topics.add(gwTopic);

        String nameSrv = config.getMqProxy().getNameSrv();
        String consumerGroup = config.getMqProxy().getConsumerGroup();
        String producerGroup = config.getMqProxy().getProducerGroup();

        RocketmqProxy rocketmqProxy = new RocketmqProxy.Builder()
                .topics(topics)
                .consumerGroup(consumerGroup)
                .producerGroup(producerGroup)
                .namesrvAddr(nameSrv)
                .build();

        rocketmqProxy.start();
        */

        String dialect = config.getMqProxy().getDialect();

        if ("rocketmq".equals(dialect)) {
            List<String> mqTopic = new ArrayList<>();
            mqTopic.add(config.getMqProxy().getTopic());

            Map<String,String> mqConfig = new HashMap<>();
            mqConfig.put("dialect", config.getMqProxy().getDialect());
            mqConfig.put("nameSrv", config.getMqProxy().getRocketMq().getNameSrv());
            mqConfig.put("consumerGroup", config.getMqProxy().getRocketMq().getConsumerGroup());
            mqConfig.put("producerGroup", config.getMqProxy().getRocketMq().getProducerGroup());

            MqProxy mqProxy = MqProxyFactory.createMqProxy(mqConfig, mqTopic);

            mqProxy.start();

            ServerContext.mqProxy = mqProxy;

        }

        logger.info("Application start!");
    }
}
