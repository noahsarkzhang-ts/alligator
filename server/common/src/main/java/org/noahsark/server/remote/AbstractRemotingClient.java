package org.noahsark.server.remote;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Created by hadoop on 2021/3/14.
 */
public class AbstractRemotingClient implements RemotingClient{

  @Override
  public void connect() {

  }

  @Override
  public void shutdown() {

  }

  @Override
  public RetryPolicy getRetryPolicy() {
    return null;
  }

  @Override
  public void ping() {

  }

  @Override
  public void toggleServer() {

  }

  @Override
  public void sendMessage(WebSocketFrame frame) {

  }

  @Override
  public void sendMessage(String text) {

  }
}
