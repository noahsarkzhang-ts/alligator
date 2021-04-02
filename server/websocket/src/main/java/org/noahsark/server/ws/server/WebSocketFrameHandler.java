/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.noahsark.server.ws.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.rpc.RpcRequest;
import org.noahsark.server.session.Session;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private WorkQueue workQueue;

    public WebSocketFrameHandler(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand command) throws Exception {
        // ping and pong frames already handled

        Response response = null;
        Result<Void> result = new Result<>();

        try {

            Session session = Session.getOrCreatedSession(ctx.channel());

            RpcContext rpcContext = new RpcContext.Builder()
                    .command(command)
                    .session(session)
                    .build();

            RpcRequest rpcRequest = new RpcRequest.Builder()
                    .request(command)
                    .context(rpcContext)
                    .build();

            if (workQueue.isBusy()) {
                log.info("server is busy: {}", JsonUtils.toJson(command));

                result.setCode(1000);
                result.setMessage("server is busy");

                response = new Response.Builder()
                        .requestId(command.getRequestId())
                        .biz(command.getBiz())
                        .cmd(command.getCmd())
                        .payload(result)
                        .build();

                ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));

            } else {
                workQueue.add(() -> {
                    String processName = command.getBiz() + ":" + command.getCmd();
                    log.info("processName: {}", processName);

                    AbstractProcessor processor = Dispatcher.getInstance().getProcessor(processName);
                    processor.process(rpcRequest);
                });
            }

            return;

        } catch (Exception ex) {
            result.setCode(1003);
            result.setMessage("System exception!");

            response = new Response.Builder()
                    .requestId(0)
                    .biz(0)
                    .cmd(0)
                    .payload(result)
                    .build();

        }

        ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));
    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }
}
