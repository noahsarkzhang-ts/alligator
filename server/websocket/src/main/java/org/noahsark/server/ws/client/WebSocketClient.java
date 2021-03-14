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

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import org.noahsark.server.remote.ExponentialBackOffRetry;
import org.noahsark.server.remote.RemotingClient;
import org.noahsark.server.remote.RetryPolicy;

/**
 * This is an example of a WebSocket client.
 * <p>
 * In order to run this example you need a compatible WebSocket server.
 * Therefore you can either start the WebSocket server from the examples
 * by running {@link org.noahsark.server.ws.server.WebSocketServer}
 * or connect to an existing WebSocket server such as
 * <a href="https://www.websocket.org/echo.html">ws://echo.websocket.org</a>.
 * <p>
 * The client will attempt to connect to the URI passed to it as the first argument.
 * You don't have to specify any arguments if you want to connect to the example WebSocket server,
 * as this is the default.
 */
public final class WebSocketClient implements RemotingClient {

  String URL = System.getProperty("url", "ws://127.0.0.1:9090/websocket");

  private String url;

  private String host;

  private int port;

  private EventLoopGroup group;

  private WebSocketClientHandler handler;

  private WebSocketClientInitializer webSocketClientInitializer;

  private Channel channel;

  private Bootstrap bootstrap;

  private RetryPolicy retryPolicy;

  public WebSocketClient(String url) {
    this.url = url;

    init();
  }

  private boolean init() {

    try {

      URI uri = new URI(url);
      String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
      host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
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
        System.err.println("Only WS(S) is supported.");
        return false;
      }

      final boolean ssl = "wss".equalsIgnoreCase(scheme);
      final SslContext sslCtx;
      if (ssl) {
        sslCtx = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
      } else {
        sslCtx = null;
      }

      group = new NioEventLoopGroup();

      handler =
          new WebSocketClientHandler(
              WebSocketClientHandshakerFactory.newHandshaker(
                  uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

      webSocketClientInitializer = new WebSocketClientInitializer(sslCtx, uri, host, port,
          this);

      retryPolicy = new ExponentialBackOffRetry(1000, Integer.MAX_VALUE, 60 * 1000);

      bootstrap = new Bootstrap();

      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .handler(webSocketClientInitializer);

    } catch (Exception ex) {
      ex.printStackTrace();

      return false;
    }

    return true;
  }

  @Override
  public void connect() {

    synchronized (bootstrap) {
      ChannelFuture future = bootstrap.connect(this.host, this.port);
      future.addListener(getConnectionListener());
      this.channel = future.channel();
    }

  }

  @Override
  public void shutdown() {
    if (channel != null) {
      channel.close();
    }
    group.shutdownGracefully();
  }

  @Override
  public void sendMessage(WebSocketFrame frame) {
    this.channel.writeAndFlush(frame);

  }

  @Override
  public void sendMessage(String text) {
    WebSocketFrame frame = new TextWebSocketFrame(text);
    this.channel.writeAndFlush(frame);
  }

  @Override
  public RetryPolicy getRetryPolicy() {
    return retryPolicy;
  }

  @Override
  public void ping() {
    WebSocketFrame frame = new PingWebSocketFrame(
        Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
    this.sendMessage(frame);
  }

  @Override
  public void toggleServer() {
    System.out.println("toggle server!!!");
  }

  private ChannelFutureListener getConnectionListener() {
    return new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
          future.channel().pipeline().fireChannelInactive();
        }
      }
    };
  }

  public static void main(String[] args) throws Exception {

    String url = System.getProperty("url", "ws://192.168.9.102:9090/websocket");

    WebSocketClient client = new WebSocketClient(url);
    client.connect();

    try {

      BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        String msg = console.readLine();
        if (msg == null) {
          break;
        } else if ("bye".equals(msg.toLowerCase())) {
          client.sendMessage(new CloseWebSocketFrame());
          client.shutdown();
          break;
        } else if ("ping".equals(msg.toLowerCase())) {
          WebSocketFrame frame = new PingWebSocketFrame(
              Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
          client.sendMessage(frame);
        } else {
          client.sendMessage(msg);
        }
      }
    } finally {
      client.shutdown();
    }
  }
}
