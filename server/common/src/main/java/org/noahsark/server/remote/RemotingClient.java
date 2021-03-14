package org.noahsark.server.remote;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Created by hadoop on 2021/3/7.
 */
public interface RemotingClient {

  void connect();

  void shutdown();

  RetryPolicy getRetryPolicy();

  void ping();

  void toggleServer();

  void sendMessage(WebSocketFrame frame);

  void sendMessage(String text);

}
