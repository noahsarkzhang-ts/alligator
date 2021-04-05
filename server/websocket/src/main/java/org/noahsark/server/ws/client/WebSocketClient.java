/*
 * Copyright 2014 The Netty Project
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
package org.noahsark.server.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.net.URI;
import java.util.List;
import org.noahsark.client.manager.ConnectionManager;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.remote.ExponentialBackOffRetry;
import org.noahsark.server.remote.ServerInfo;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class WebSocketClient extends AbstractRemotingClient {

    private static Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    public WebSocketClient() {
    }

    public WebSocketClient(String url) {
        super(url);
    }

    public WebSocketClient(List<String> urls) {
        super(urls);

    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(
        AbstractRemotingClient server) {
        return new WebSocketClientInitializer(this);
    }

    public void sendMessage(WebSocketFrame frame) {
        this.connection.getChannel().writeAndFlush(frame);

    }

    @Override
    public void sendMessage(RpcCommand command) {

        this.connection.getChannel().writeAndFlush(command);
    }

    @Override
    protected void preInit() {

        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.setHeartbeatFactory(new WebsocketHeartbeatFactory());
        connectionManager.setRetryPolicy(new ExponentialBackOffRetry(1000,
            4, 60 * 1000));

        this.connectionManager = connectionManager;

    }

    @Override
    public void ping() {
        this.sendMessage((WebSocketFrame) this.connectionManager.getHeartbeatFactory().getPing());
    }

    @Override
    public ServerInfo convert(String url) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(url);

        try {

            URI uri = new URI(url);

            String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            int port = uri.getPort();

            serverInfo.setHost(host);
            serverInfo.setPort(port);
            serverInfo.setUri(uri);

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);

        }

        return serverInfo;
    }


    public static void main(String[] args) throws Exception {

    }
}
