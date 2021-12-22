package org.noahsark.gw.busevent;

import org.noahsark.gw.config.CommonConfig;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.server.event.ServerStartupEvent;
import org.noahsark.server.eventbus.ApplicationListener;
import org.noahsark.server.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 服务器启动事件监听器
 *
 * @author zhangxt
 * @date 2021/4/12
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
