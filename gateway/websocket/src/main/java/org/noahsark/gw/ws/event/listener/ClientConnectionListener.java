package org.noahsark.gw.ws.event.listener;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.gw.ws.config.CommonConfig;
import org.noahsark.gw.ws.context.ServerContext;
import org.noahsark.gw.ws.ping.WebsocketPingPayloadGenerator;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.registration.domain.Service;
import org.noahsark.server.constant.BizServiceType;
import org.noahsark.server.event.ClientConnectionEvent;
import org.noahsark.server.eventbus.ApplicationListener;
import org.noahsark.server.eventbus.EventBus;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
@Component
public class ClientConnectionListener extends ApplicationListener<ClientConnectionEvent> {

    private Logger logger = LoggerFactory.getLogger(ClientConnectionListener.class);

    @Autowired
    private CommonConfig config;

    @Override
    public void onApplicationEvent(ClientConnectionEvent event) {

        Service service = new Service();
        service.setLoad(0);
        service.setId("gw-ws-01");
        service.setBiz(BizServiceType.BIZ_GW_WS);
        service.setZone(1);
        service.setName("gw-ws-01");

        RegistrationClient regClient = ServerContext.regClient;
        regClient.registerPingPayloadGenerator(new WebsocketPingPayloadGenerator());

        regClient.registerServiceAsync(service, new CommandCallback() {
            @Override
            public void callback(Object result) {
                Result<Void> response = JsonUtils.fromCommonObject((byte[]) result);

                logger.info("result: {}", response);
            }

            @Override
            public void failure(Throwable cause) {
                logger.warn("registerService catch an exception!", cause);
            }
        });


    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }
}
