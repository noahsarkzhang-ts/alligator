package org.noahsark.gw.ws.listener;

import org.noahsark.server.queue.WorkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/3/13
 */
@Component
public class ApplicationStopListener implements ApplicationListener<ContextClosedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationStopListener.class);


    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        logger.info("Application stoped!");
    }
}
