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
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.server.hander.AuthHandler;
import org.noahsark.server.hander.ServerBizServiceHandler;
import org.noahsark.server.ws.handler.WebsocketDecoder;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.remote.AbstractRemotingServer;
import org.noahsark.server.remote.RemoteOption;
import org.noahsark.server.hander.ServerIdleStateTrigger;
import org.noahsark.server.ws.handler.WebSocketIndexPageHandler;
import org.noahsark.server.ws.handler.WebsocketEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger log = LoggerFactory.getLogger(WebSocketServerInitializer.class);

    private static final String WEBSOCKET_PATH = "/websocket";

    private SslContext sslCtx;

    private WorkQueue workQueue;

    public WebSocketServerInitializer(AbstractRemotingServer server) {
        try {
            if (server.option(RemoteOption.SSL_ENABLE)) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            workQueue = server.getWorkQueue();

        } catch (Exception ex) {
            log.info("catch an exception!", ex);
        }
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new IdleStateHandler(120, 0, 0));
        pipeline.addLast(new ServerIdleStateTrigger());
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
        pipeline.addLast(new WebsocketDecoder());
        pipeline.addLast(new WebsocketEncoder());
        pipeline.addLast(new AuthHandler());
        pipeline.addLast(new ServerBizServiceHandler(workQueue));
    }
}
