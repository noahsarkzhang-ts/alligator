package org.noahsark.gw.manager;

import org.noahsark.common.event.UserEvent;
import org.noahsark.gw.config.CommonConfig;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.gw.user.UserSubject;
import org.noahsark.mq.MqProxy;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.constant.BizServiceType;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;
import org.noahsark.server.rpc.MultiRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 广播用户上线事件
 *
 * @author zhangxt
 * @date 2021/6/30
 */
@Component
public class UserEventEmitter {

    private Logger logger = LoggerFactory.getLogger(UserEventEmitter.class);

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    @Qualifier("commonExecutor")
    private ThreadPoolTaskExecutor commonExecutor;

    public void eimit(final UserSubject user, final boolean online) {

        commonExecutor.execute(() -> {
            UserEvent userEvent = new UserEvent();
            userEvent.setName(user.getName());
            userEvent.setServiceId(commonConfig.getServerConfig().getId());
            userEvent.setUserId(user.getUserId());
            userEvent.setTimestamp(user.getLoginTime());
            if (online) {
                userEvent.setType((byte) 1);
            } else {
                userEvent.setType((byte) 0);
            }

            MultiRequest multiRequest = new MultiRequest.Builder()
                    .biz(BizServiceType.BIZ_USER_EVENT)
                    .cmd(1)
                    .serializer(SerializerType.JSON)
                    .type(RpcCommandType.REQUEST_ONEWAY)
                    .ver(RpcCommandVer.V1)
                    .payload(userEvent)
                    .topic(commonConfig.getMqProxy().getTopic())
                    .build();

            logger.info("send a request: {}", multiRequest);

            MqProxy proxy = ServerContext.mqProxy;

            RocketmqTopic topic = new RocketmqTopic();
            topic.setTopic(commonConfig.getSysEvent().getUserTopic());

            proxy.sendOneway(topic, multiRequest);
        });

    }


}
