package org.noahsark.gw.ws.processor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBufUtil;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.gw.ws.context.ServerContext;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

        int biz = command.getBiz();

        // TODO
        // 根据 biz 从注册中心获取 topc

        MultiRequest multiRequest = new MultiRequest.Builder()
                .biz(biz)
                .cmd(command.getCmd())
                .serializer(command.getSerializer())
                .type(command.getType())
                .ver(command.getVer())
                .payload(command.getPayload())
                .topic("TopicTest-1")
                .build();

        logger.info("send a request: {}",multiRequest);
        logger.info("payload: {}",command.getPayload());

        RocketmqProxy proxy = ServerContext.mqProxy;

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic("TopicTest");
        proxy.sendAsync(topic, multiRequest, new CommandCallback() {
            @Override
            public void callback(Object result) {

                logger.info("receive a response: {}", multiRequest.getRequestId());

                logger.info("receive a payload:{}", ByteBufUtil.hexDump((byte[]) result));

                String sPayload = new String((byte [])result);
                JsonObject paylpad = new JsonParser().parse(sPayload).getAsJsonObject();
                logger.info("payload:{}",sPayload);

                // 2. 响应
                context.sendResponse(Response.buildResponseFromResult(context.getCommand(), paylpad,
                        0, "success"));
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
