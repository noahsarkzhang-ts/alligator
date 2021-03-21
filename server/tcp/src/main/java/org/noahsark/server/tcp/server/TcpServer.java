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
package org.noahsark.server.tcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.server.remote.AbstractRemotingServer;


public final class TcpServer extends AbstractRemotingServer {

  public TcpServer(String host, int port) {
    super(host,port);
  }

  @Override
  protected ChannelInitializer<SocketChannel> getChannelInitializer(AbstractRemotingServer server) {
    return new TcpServerInitializer(this);
  }

  public static void main(String[] args) {

    String host = "192.168.9.101";
    int port = 9090;

    final TcpServer tcpServer = new TcpServer(host, port);

    tcpServer.init();


    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        tcpServer.shutdown();
      }
    });

    tcpServer.start();
  }
}
