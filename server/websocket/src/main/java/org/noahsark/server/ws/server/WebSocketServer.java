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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import java.net.InetSocketAddress;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.remote.RemotingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An HTTP server which serves Web Socket requests at:
 *
 * http://localhost:8080/websocket
 *
 * Open your browser at <a href="http://localhost:8080/">http://localhost:8080/</a>, then the demo
 * page will be loaded and a Web Socket connection will be made automatically.
 *
 * This server illustrates support for the different web socket specification versions and will work
 * with:
 *
 * <ul> <li>Safari 5+ (draft-ietf-hybi-thewebsocketprotocol-00) <li>Chrome 6-13
 * (draft-ietf-hybi-thewebsocketprotocol-00) <li>Chrome 14+ (draft-ietf-hybi-thewebsocketprotocol-10)
 * <li>Chrome 16+ (RFC 6455 aka draft-ietf-hybi-thewebsocketprotocol-17) <li>Firefox 7+
 * (draft-ietf-hybi-thewebsocketprotocol-10) <li>Firefox 11+ (RFC 6455 aka
 * draft-ietf-hybi-thewebsocketprotocol-17) </ul>
 */
public final class WebSocketServer implements RemotingServer {

  private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

  static final boolean SSL = System.getProperty("ssl") != null;
  static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "9090"));

  private EventLoopGroup bossGroup;

  private EventLoopGroup workerGroup;

  private WebSocketServerInitializer webSocketServerInitializer;

  private String host;

  private int port = PORT;

  private Channel channel;

  private WorkQueue workQueue;

  public void init() {
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();

    SslContext sslCtx = null;
    try {
      if (SSL) {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
      } else {
        sslCtx = null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    webSocketServerInitializer = new WebSocketServerInitializer(sslCtx,workQueue);
  }

  public WebSocketServer() {
  }

  public WebSocketServer(String host, int port) {

    this.host = host;

    this.port = port;
  }

  @Override
  public void start() {

    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(webSocketServerInitializer);

      InetSocketAddress address = new InetSocketAddress(host, port);

      channel = bootstrap.bind(address).sync().channel();

      log.info("Open your web browser and navigate to " +
          (SSL ? "https" : "http") + "://" + host + ":" + port + '/');

      channel.closeFuture().sync();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  @Override
  public void shutdown() {
    if (channel != null) {
      channel.close();
    }
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();

    log.info("Shutdown the websocket server...");
  }

  public WorkQueue getWorkQueue() {
    return workQueue;
  }

  public void setWorkQueue(WorkQueue workQueue) {
    this.workQueue = workQueue;
  }

  public static void main(String[] args) {

    String host = "192.168.9.102";
    int port = 9090;

    final WebSocketServer webSocketServer = new WebSocketServer(host,port);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        webSocketServer.shutdown();
      }
    });

    webSocketServer.start();
  }
}
