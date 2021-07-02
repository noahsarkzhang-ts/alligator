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
package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.client.future.Connection;
import org.noahsark.server.constant.RpcCommandType;
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
public class ServerBizServiceHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(ServerBizServiceHandler.class);

    private WorkQueue workQueue;

    public ServerBizServiceHandler(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand command) throws Exception {

        try {
            log.info("receive a request: {}", command);
            Session session = Session.getOrCreatedSession(ctx.channel());
            Connection connection = session.getConnection();

            if (command.getType() == RpcCommandType.REQUEST
                    || command.getType() == RpcCommandType.REQUEST_ONEWAY) {
                RequestHandler.processRequest(ctx, command, workQueue, session);
            } else {
                RequestHandler.processResponse(connection, command);
            }

        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);
        }

    }

    public WorkQueue getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }
}
