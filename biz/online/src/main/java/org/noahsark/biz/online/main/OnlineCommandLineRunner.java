package org.noahsark.biz.online.main;

import org.noahsark.biz.online.config.CommonConfig;
import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/11
 */
@Component
public class OnlineCommandLineRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(OnlineCommandLineRunner.class);

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public void run(String... args) throws Exception {
        RocketmqTopic topic = new RocketmqTopic();

        topic.setTopic(commonConfig.getMqProxy().getTopic());

        RocketmqProxy rocketmqProxy = new RocketmqProxy.Builder()
                .topic(topic)
                .consumerGroup(commonConfig.getMqProxy().getConsumerGroup())
                .producerGroup(commonConfig.getMqProxy().getProducerGroup())
                .namesrvAddr(commonConfig.getMqProxy().getNameSrv())
                .build();

        ServerContext.mqProxy = rocketmqProxy;

        log.info("RocketmqProxy start!");
    }
}
