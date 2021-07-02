package org.noahsark.biz.online.processor.event.user;


import org.noahsark.biz.online.manager.UserEventBroadcaster;
import org.noahsark.biz.online.repository.OnlineRepository;
import org.noahsark.common.dto.UserInfo;
import org.noahsark.common.event.UserEvent;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/6/27.
 */
@Component
public class UserEventProcessor extends AbstractProcessor<UserEvent> {

    private static Logger log = LoggerFactory.getLogger(UserEventProcessor.class);

    @Autowired
    private OnlineRepository repository;

    @Autowired
    private UserEventBroadcaster userEventBroadcaster;

    @Override
    protected void execute(UserEvent request, RpcContext context) {

        log.info("receive user event: {}", request);
        UserInfo userInfo;
        userInfo = new UserInfo();
        userInfo.setUserId(request.getUserId());
        userInfo.setName(request.getName());

        if (request.getType().equals((byte) 1)) {
            // 1、用户登陆
            repository.userLogin(request);

            // 2、推送用户上线信息
            userInfo.setState((byte) 1);
            userEventBroadcaster.broadcast(userInfo);
        } else {

            // 1、用户退出
            repository.userLogout(request);

            // 2、推送用户下线信息
            userInfo.setState((byte) 0);
            userEventBroadcaster.broadcast(userInfo);
        }


    }

    @Override
    protected Class<UserEvent> getParamsClass() {
        return UserEvent.class;
    }

    @Override
    protected int getBiz() {
        return 3;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}
