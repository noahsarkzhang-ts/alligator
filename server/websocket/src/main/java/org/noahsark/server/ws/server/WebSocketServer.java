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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.server.remote.AbstractRemotingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class WebSocketServer extends AbstractRemotingServer {

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    public WebSocketServer() {
    }

    public WebSocketServer(String host, int port) {
        super(host, port);
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(AbstractRemotingServer server) {
        return new WebSocketServerInitializer(this);
    }

    public static void main(String[] args) {

    }


}
