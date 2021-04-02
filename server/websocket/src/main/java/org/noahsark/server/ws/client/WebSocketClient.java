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

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;


public final class WebSocketClient extends AbstractRemotingClient {

    private static Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    private String url;

    private URI uri;

    public WebSocketClient() {
    }

    public WebSocketClient(String url) {
        this.url = url;

        init();
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(AbstractRemotingClient server) {
        return new WebSocketClientInitializer(this);
    }

    public void sendMessage(WebSocketFrame frame) {
        this.channel.writeAndFlush(frame);

    }

    @Override
    public void sendMessage(RpcCommand command) {

        String text = JsonUtils.toJson(command);
        WebSocketFrame frame = new TextWebSocketFrame(text);
        this.channel.writeAndFlush(frame);
    }

    @Override
    protected void preInit() {

        try {
            uri = new URI(url);
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            this.host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }

            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                log.warn("Only WS(S) is supported.");
            }

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);
        }

    }

    @Override
    public URI getUri() {
        return this.uri;
    }

    @Override
    public void ping() {
        WebSocketFrame frame = new PingWebSocketFrame(
                Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
        this.sendMessage(frame);
    }

    public static void main(String[] args) throws Exception {

    }
}
