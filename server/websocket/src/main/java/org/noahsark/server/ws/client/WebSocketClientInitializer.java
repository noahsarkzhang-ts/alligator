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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;
import java.net.URI;
import org.noahsark.server.remote.ClientIdleStateTrigger;
import org.noahsark.server.remote.ReconnectHandler;
import org.noahsark.server.remote.RemotingClient;

/**
 * Created by hadoop on 2021/3/7.
 */
public class WebSocketClientInitializer extends ChannelInitializer<SocketChannel> {

  private final SslContext sslCtx;

  private final String host;

  private final int port;

  private URI uri;

  private final RemotingClient remotringClient;


  public WebSocketClientInitializer(SslContext sslCtx, URI uri,
      String host, int port, RemotingClient remotringClient) {
    this.sslCtx = sslCtx;
    this.uri = uri;
    this.host = host;
    this.port = port;
    this.remotringClient = remotringClient;
  }

  @Override
  public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    if (sslCtx != null) {
      pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));
    }

    pipeline.addLast(new IdleStateHandler(0, 3, 0));
    pipeline.addLast(new ClientIdleStateTrigger(this.remotringClient));
    pipeline.addLast(new ReconnectHandler(this.remotringClient));
    //pipeline.addLast(new Pinger(this.remotringClient));
    pipeline.addLast(new HttpClientCodec());
    pipeline.addLast(new HttpObjectAggregator(8192));
    pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
    pipeline.addLast(new WebSocketClientHandler(
        WebSocketClientHandshakerFactory.newHandshaker(
            this.uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders())));

  }

}
