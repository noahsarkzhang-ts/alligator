package org.noahsark.biz.online.listener;

import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.rocketmq.RocketmqProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * 服务停止事件监听器
 *
 * @author zhangxt
 * @date 2020/3/13
 */
@Component
public class ApplicationStopListener implements ApplicationListener<ContextClosedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStopListener.class);


    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        RocketmqProxy mqProxy = ServerContext.mqProxy;

        if (mqProxy != null) {
            mqProxy.shutdown();
        }

        RegistrationClient regClient = ServerContext.regClient;
        if (regClient != null) {
            regClient.shutdown();
        }

        logger.info("Application stoped!");
    }
}
