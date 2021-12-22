package org.noahsark.biz.online.manager;

import org.noahsark.biz.online.config.CommonConfig;
import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.common.dto.UserInfo;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.registration.domain.ServiceQuery;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.constant.BizServiceType;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户事件广播器
 *
 * @author zhangxt
 * @date 2021/6/30
 */
@Component
public class UserEventBroadcaster {

    private Logger logger = LoggerFactory.getLogger(UserEventBroadcaster.class);

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    @Qualifier("commonExecutor")
    private ThreadPoolTaskExecutor commonExecutor;

    public void broadcast(UserInfo userInfo) {

        commonExecutor.execute(() -> {
            RegistrationClient regClient = ServerContext.regClient;

            ServiceQuery query = new ServiceQuery();
            query.setBiz(BizServiceType.BIZ_GW_WS);
            Result<List<CandidateService>> services = regClient.getAllService(query);

            List<CandidateService> serviceList = services.getData();

            serviceList.stream().forEach(service -> {

                logger.info("send a broadcast message to {},{}", userInfo, service);

                Set<String> targetIds = new HashSet<>();
                targetIds.add("*");

                MultiRequest multiRequest = new MultiRequest.Builder()
                        .biz(BizServiceType.BIZ_CLIENT)
                        .cmd(10)
                        .serializer(SerializerType.JSON)
                        .type(RpcCommandType.REQUEST_ONEWAY)
                        .ver(RpcCommandVer.V1)
                        .payload(userInfo)
                        .topic(commonConfig.getMqProxy().getTopic())
                        .targetIds(targetIds)
                        .build();

                RocketmqProxy proxy = ServerContext.mqProxy;

                RocketmqTopic topic = new RocketmqTopic();
                topic.setTopic(service.getTopic());

                logger.info("send a multiRequest:{}", multiRequest);

                proxy.sendOneway(topic, multiRequest);

            });
        });

    }
}
