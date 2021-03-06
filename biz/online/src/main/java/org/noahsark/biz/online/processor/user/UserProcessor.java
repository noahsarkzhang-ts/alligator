package org.noahsark.biz.online.processor.user;

import org.noahsark.biz.online.repository.OnlineRepository;
import org.noahsark.common.dto.UserInfo;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/7/1
 */
@Component
public class UserProcessor extends AbstractProcessor<UserQuery> {

    private static Logger log = LoggerFactory.getLogger(UserProcessor.class);

    @Autowired
    private OnlineRepository onlineRepository;

    @Override
    protected void execute(UserQuery request, RpcContext context) {

        MultiRequest command = (MultiRequest) context.getCommand();
        log.info("receive a request: {}", command);

        Set<UserInfo> users = onlineRepository.getAllUser();

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(command.getTopic());

        Response response = Response.buildResponse(command, users, 0, "success");
        response.setAttachment(topic);

        context.sendResponse(response);
    }

    @Override
    protected Class<UserQuery> getParamsClass() {
        return UserQuery.class;
    }

    @Override
    protected int getBiz() {
        return 201;
    }

    @Override
    protected int getCmd() {
        return 2;
    }
}
