package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import org.noahsark.client.future.Connection;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.rpc.*;
import org.noahsark.server.session.Session;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/13
 */
public class RequestHandler {

    private static Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public static void processRequest(ChannelHandlerContext ctx, RpcCommand command,
                                      WorkQueue workQueue, Session session) {

        Response response;
        Result<Void> result = new Result<>();

        try {
            RpcContext rpcContext = new RpcContext.Builder()
                    .command(command)
                    .session(session)
                    .build();

            RpcRequest rpcRequest = new RpcRequest.Builder()
                    .request(command)
                    .context(rpcContext)
                    .build();

            if (workQueue.isBusy()) {
                log.info("service is busy: {}", JsonUtils.toJson(command));

                result.setCode(1000);
                result.setMessage("service is busy");

                response = new Response.Builder()
                        .requestId(command.getRequestId())
                        .biz(command.getBiz())
                        .cmd(command.getCmd())
                        .payload(result)
                        .build();

                ctx.channel().writeAndFlush(response);

            } else {
                workQueue.add(() -> {
                    String processName = command.getBiz() + ":" + command.getCmd();
                    log.info("processName: {}", processName);

                    AbstractProcessor processor = Dispatcher.getInstance().getProcessor(processName);

                    if (processor != null) {
                        processor.process(rpcRequest);
                    } else {

                        // 使用默认的处理器
                        processName = -1 + ":" + -1;
                        processor = Dispatcher.getInstance().getProcessor(processName);

                        if (processor != null) {
                            processor.process(rpcRequest);
                        } else {
                            log.warn("No processor: {}", processName);
                        }
                    }

                });
            }

            return;
        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);

            result.setCode(1003);
            result.setMessage("System exception!");

            response = new Response.Builder()
                    .requestId(command.getRequestId())
                    .biz(command.getBiz())
                    .cmd(command.getCmd())
                    .payload(result)
                    .build();
        }

        ctx.channel().writeAndFlush(response);
    }

    public static void processResponse(Connection connection, RpcCommand command) {
        RpcPromise promise = connection.getPromise(command.getRequestId());

        if (promise != null) {
            promise.setSuccess(command.getPayload());
            connection.removePromis(command.getRequestId());

        } else {
            log.warn("promis is null : {}", command.getRequestId());
        }
    }
}
