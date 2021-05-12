package org.noahsark.biz.online.processor.user;

import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/11
 */
@Component
public class InviteProcessor extends AbstractProcessor<InviteInfo> {

    private static Logger log = LoggerFactory.getLogger(InviteProcessor.class);

    @Override
    protected void execute(InviteInfo request, RpcContext context) {

        MultiRequest command = (MultiRequest) context.getCommand();
        log.info("receive a request: {}",command);

        // TODO 业务操作
        InviteResult inviteResult = new InviteResult();
        inviteResult.setStatus((byte)1);


        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic(command.getTopic());

        Response response = Response.buildResponse(command,inviteResult,0,"success");
        response.setAttachment(topic);

        context.sendResponse(response);
    }

    @Override
    protected Class<InviteInfo> getParamsClass() {
        return InviteInfo.class;
    }

    @Override
    protected int getBiz() {
        return 201;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}
