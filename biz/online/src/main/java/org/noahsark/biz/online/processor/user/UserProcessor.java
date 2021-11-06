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
import java.util.concurrent.TimeUnit;

/**
 * 用户处理类
 * @author zhangxt
 * @date 2021/7/1 15:23
 **/
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

        Response response = Response.buildStream(command, users, 0, "success");
        response.setAttachment(topic);

        int i = 0;
        while (i < 4) {
            context.flow(response);

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                log.error("catch an exception:",ex);
            }

            i++;
        }

        context.end(response);
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
