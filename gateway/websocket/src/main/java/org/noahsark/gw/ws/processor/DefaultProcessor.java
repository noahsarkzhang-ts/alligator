package org.noahsark.gw.ws.processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.gw.ws.context.ServerContext;
import org.noahsark.gw.ws.user.UserManager;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.*;
import org.noahsark.server.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/10
 */
@Component
public class DefaultProcessor extends AbstractProcessor<Void> {

    private Logger logger = LoggerFactory.getLogger(DefaultProcessor.class);

    @Override
    protected void execute(Void request, RpcContext context) {

        // 1. 请求处理
        RpcCommand command = context.getCommand();

        logger.info("receive a request:{}", command);

        int biz = command.getBiz();

        /**
         * 向客户端发送请求
         */
        if (biz >= 300 && biz < 400) {
            downstream(context);
        } else { // 向业务服务发送请求
            upstream(context);
        }

    }

    private void downstream(RpcContext context) {

        MultiRequest request = (MultiRequest) context.getCommand();

        List<String> targetIds = request.getTargetIds();
        if (targetIds == null || targetIds.size() == 0) {

            logger.warn("target ids is null.");

            return;
        }

        targetIds.stream().forEach(userId -> {
            Session session = UserManager.getInstance().getSession(userId);

            if (session == null) {
                logger.info("userId not online:{}", userId);
            }
           /* String sPayload = new String((byte[]) request.getPayload());
            JsonObject paylpad = new JsonParser().parse(sPayload).getAsJsonObject();
            logger.info("payload:{}", sPayload);*/

            Request downRequest = new Request.Builder()
                    .biz(request.getBiz())
                    .cmd(request.getCmd())
                    .serializer(request.getSerializer())
                    .type(request.getType())
                    .ver(request.getVer())
                    .payload(request.getPayload())
                    .build();

            logger.info("send a downstream:{}", downRequest);

            session.invoke(downRequest, new CommandCallback() {
                @Override
                public void callback(Object result) {

                    RocketmqTopic topic = new RocketmqTopic();
                    topic.setTopic(request.getTopic());

                    Response response = Response.buildResponseFromResult(request, result);
                    response.setAttachment(topic);

                    context.sendResponse(response);
                }

                @Override
                public void failure(Throwable cause) {
                    logger.warn("catch an exception.", cause);
                }
            }, 6000);

        });

    }

    public void upstream(RpcContext context) {
        RpcCommand command = context.getCommand();

        // TODO
        // 根据 biz 从注册中心获取 topc

        MultiRequest multiRequest = new MultiRequest.Builder()
                .biz(command.getBiz())
                .cmd(command.getCmd())
                .serializer(command.getSerializer())
                .type(command.getType())
                .ver(command.getVer())
                .payload(command.getPayload())
                .topic("TopicTest-1")
                .build();

        logger.info("send a request: {}", multiRequest);
        logger.info("payload: {}", command.getPayload());

        RocketmqProxy proxy = ServerContext.mqProxy;

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic("TopicTest");
        proxy.sendAsync(topic, multiRequest, new CommandCallback() {
            @Override
            public void callback(Object result) {

                /*String sPayload = new String((byte[]) result);
                JsonObject paylpad = new JsonParser().parse(sPayload).getAsJsonObject();
                logger.info("receive a response: {}", paylpad);*/

                // 2. 响应
                context.sendResponse(Response.buildResponseFromResult(context.getCommand(), result));
            }

            @Override
            public void failure(Throwable cause) {
                context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                        -1, "failed"));
            }
        }, 3000);
    }

    @Override
    protected Class<Void> getParamsClass() {
        return Void.class;
    }

    @Override
    protected int getBiz() {
        return -1;
    }

    @Override
    protected int getCmd() {
        return -1;
    }
}
