package org.noahsark.biz.online.processor.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.noahsark.biz.online.config.CommonConfig;
import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.constant.BizServiceType;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/11
 */
@Component
public class InviteProcessor extends AbstractProcessor<InviteInfo> {

    private static Logger log = LoggerFactory.getLogger(InviteProcessor.class);

    @Autowired
    private CommonConfig commonConfig;

    @Override
    protected void execute(InviteInfo request, RpcContext context) {

        try {
            MultiRequest command = (MultiRequest) context.getCommand();
            log.info("receive a request: {}", command);

            List<String> targetIds = new ArrayList<>();
            targetIds.add(request.getUserId());

            // TODO 业务操作
            // 向用户发起请求

            // 获取用户所在服务器
            CandidateService service = ServerContext.userServiceCache.get(request.getUserId());

            MultiRequest multiRequest = new MultiRequest.Builder()
                    .biz(BizServiceType.BIZ_CLIENT)
                    .cmd(command.getCmd())
                    .serializer(command.getSerializer())
                    .type(command.getType())
                    .ver(command.getVer())
                    .payload(command.getPayload())
                    .topic(commonConfig.getMqProxy().getTopic())
                    .targetIds(targetIds)
                    .build();

            log.info("send a request: {}", multiRequest);

            RocketmqProxy proxy = ServerContext.mqProxy;

            RocketmqTopic topic = new RocketmqTopic();
            topic.setTopic(service.getTopic());
            proxy.sendAsync(topic, multiRequest, new CommandCallback() {
                @Override
                public void callback(Object result) {

                    String sPayload = new String((byte[]) result);
                    log.info("receive a response: {}", sPayload);
                    JsonObject paylpad = new JsonParser().parse(sPayload).getAsJsonObject();
                    log.info("receive a response: {}", paylpad);

                    InviteResult inviteResult = new InviteResult();
                    inviteResult.setStatus((byte) 1);

                    RocketmqTopic topic = new RocketmqTopic();
                    topic.setTopic(command.getTopic());

                    Response response = Response.buildResponse(command, inviteResult, 0, "success");
                    response.setAttachment(topic);

                    context.sendResponse(response);
                }

                @Override
                public void failure(Throwable cause) {
                    log.warn("Invoke catch an exception!", cause);

                    context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                            -1, "failed"));
                }
            }, 3000);

        } catch (Exception ex) {
            log.warn("Invoke catch an exception!", ex);

            context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                    -1, "failed"));
        }

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
