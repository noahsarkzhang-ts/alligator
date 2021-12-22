package org.noahsark.registration.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 服务启动监听器
 *
 * @author zhangxt
 * @date 2020/3/13
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ApplicationStartedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        logger.info("Application start!");
    }
}
