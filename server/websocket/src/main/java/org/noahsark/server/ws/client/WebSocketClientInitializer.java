package org.noahsark.server.ws.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.timeout.IdleStateHandler;

import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.remote.ClientIdleStateTrigger;
import org.noahsark.server.remote.ReconnectHandler;

/**
 * Created by hadoop on 2021/3/7.
 */
public class WebSocketClientInitializer extends ChannelInitializer<SocketChannel> {

  private final AbstractRemotingClient client;


  public WebSocketClientInitializer(AbstractRemotingClient client) {
    this.client = client;
  }

  @Override
  public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new IdleStateHandler(0, 3, 0));
    pipeline.addLast(new ClientIdleStateTrigger(this.client));
    pipeline.addLast(new ReconnectHandler(this.client));
    pipeline.addLast(new HttpClientCodec());
    pipeline.addLast(new HttpObjectAggregator(8192));
    pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
    pipeline.addLast(new WebSocketClientHandler(
        WebSocketClientHandshakerFactory.newHandshaker(
            this.client.getUri(), WebSocketVersion.V13, null, true, new DefaultHttpHeaders())));

  }

}
