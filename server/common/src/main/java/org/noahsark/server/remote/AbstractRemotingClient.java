package org.noahsark.server.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2021/3/14.
 */
public abstract class AbstractRemotingClient implements RemotingClient {

  private String url;

  private String host;

  private int port;

  private EventLoopGroup group;

  private ChannelInitializer<SocketChannel> clientInitializer;

  private Map<RemoteOption<?>, Object> clientOptions = new HashMap<>();

  private Channel channel;

  private Bootstrap bootstrap;

  private RetryPolicy retryPolicy;


  public AbstractRemotingClient(String url) {
    this.url = url;

    init();
  }

  public AbstractRemotingClient(String host, int port) {
    this.host = host;
    this.port = port;

    init();
  }

  private boolean init() {
    try {

      group = new NioEventLoopGroup();
      retryPolicy = new ExponentialBackOffRetry(1000, Integer.MAX_VALUE, 60 * 1000);

      bootstrap = new Bootstrap();
      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .handler(getChannelInitializer(this));

    } catch (Exception ex) {
      ex.printStackTrace();

      return false;
    }

    return true;
  }

  protected abstract ChannelInitializer<SocketChannel> getChannelInitializer(
      AbstractRemotingClient server);

  public <T> void option(RemoteOption<T> option, T value) {
    this.clientOptions.put(option, value);

  }

  public <T> T option(RemoteOption<T> option) {
    return this.clientOptions.containsKey(option) ? (T) this.clientOptions.get(option)
        : option.getDefaultValue();
  }




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
