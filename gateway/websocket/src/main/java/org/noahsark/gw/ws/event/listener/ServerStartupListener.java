package org.noahsark.gw.ws.event.listener;

import org.noahsark.gw.ws.config.CommonConfig;
import org.noahsark.gw.ws.context.ServerContext;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.server.event.ServerStartupEvent;
import org.noahsark.server.eventbus.ApplicationListener;
import org.noahsark.server.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
@Component
public class ServerStartupListener extends ApplicationListener<ServerStartupEvent> {

    @Autowired
    private CommonConfig config;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {

        RegistrationClient regClient = new RegistrationClient(config.getRegServer().getHost(),
                config.getRegServer().getPort());

        regClient.connect();

        ServerContext.regClient = regClient;

    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }
}
