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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.rpc.RpcRequest;
import org.noahsark.server.session.Session;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static Logger log = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private WorkQueue workQueue;

    public WebSocketFrameHandler(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            log.info("receive request: {}", request);

            JsonObject data = new JsonParser().parse(request).getAsJsonObject();
            Request gwRequest = new Request();
            gwRequest.setClassName(data.get("className").getAsString());
            gwRequest.setMethod(data.get("method").getAsString());
            gwRequest.setRequestId(data.get("requestId").getAsInt());
            gwRequest.setVersion(data.get("version").getAsString());
            gwRequest.setPayload(data.getAsJsonObject("payload"));

            Session session = new Session();
            session.setChannel(ctx.channel());

            RpcContext rpcContext = new RpcContext();
            rpcContext.setRequest(gwRequest);
            rpcContext.setSession(session);

            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setContext(rpcContext);
            rpcRequest.setRequest(gwRequest);

            if (workQueue.isBusy()) {
                log.info("server is busy: {}", request);

                Response<Void> response = new Response<>();
                response.setCode(1000);
                response.setMessage("server is busy");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));

            } else {
                workQueue.add(rpcRequest);
            }

            //ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }
}
