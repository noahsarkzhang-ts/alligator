package org.noahsark.gw.busevent;

import org.noahsark.gw.manager.UserEventEmitter;
import org.noahsark.gw.user.UserManager;
import org.noahsark.gw.user.UserSubject;
import org.noahsark.server.event.ClientDisconnectEvent;
import org.noahsark.server.eventbus.ApplicationListener;
import org.noahsark.server.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/6/30
 */
@Component
public class ClientDisconnectListener extends ApplicationListener<ClientDisconnectEvent> {

    private Logger logger = LoggerFactory.getLogger(ClientDisconnectListener.class);

    @Autowired
    private UserEventEmitter userEventEmitter;

    @Override
    public void onApplicationEvent(ClientDisconnectEvent event) {

        UserSubject subject = (UserSubject) event.getSource();

        logger.info("client connection disconnect:{}",subject);

        UserManager.getInstance().removeSession(subject.getId());

        // 广播用户下线事件
        userEventEmitter.eimit(subject, false);
    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }


}
