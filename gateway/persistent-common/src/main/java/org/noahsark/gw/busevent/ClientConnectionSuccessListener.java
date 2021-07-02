package org.noahsark.gw.busevent;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.gw.config.CommonConfig;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.gw.ping.PersistentPingPayloadGenerator;
import org.noahsark.registration.BizServiceCache;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.registration.domain.Service;
import org.noahsark.server.constant.BizServiceType;
import org.noahsark.server.event.ClientConnectionSuccessEvent;
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
public class ClientConnectionSuccessListener extends ApplicationListener<ClientConnectionSuccessEvent> {

    private Logger logger = LoggerFactory.getLogger(ClientConnectionSuccessListener.class);

    @Autowired
    private CommonConfig config;

    @Override
    public void onApplicationEvent(ClientConnectionSuccessEvent event) {

        Service service = new Service();
        service.setLoad(0);
        service.setId(config.getServerConfig().getId());
        service.setBiz(BizServiceType.BIZ_GW_WS);
        service.setZone(config.getServerConfig().getZone());
        service.setName(config.getServerConfig().getName());
        service.setTopic(config.getMqProxy().getTopic());

        RegistrationClient regClient = ServerContext.regClient;
        regClient.registerPingPayloadGenerator(new PersistentPingPayloadGenerator());

        regClient.registerServiceAsync(service, new CommandCallback() {
            @Override
            public void callback(Object result, int currentFanout, int fanout) {
                Result<Void> response = JsonUtils.fromCommonObject((byte[]) result);

                logger.info("result: {}", response);
            }

            @Override
            public void failure(Throwable cause, int currentFanout, int fanout) {
                logger.warn("registerService catch an exception!", cause);
            }
        });

        BizServiceCache bizServiceCache = new BizServiceCache(regClient);
        ServerContext.bizServiceCache = bizServiceCache;
    }

    @PostConstruct
    public void register() {
        EventBus.getInstance().register(this);
    }
}
