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

import org.noahsark.server.hander.ClientBizServiceHandler;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.hander.ClientIdleStateTrigger;
import org.noahsark.server.remote.ReconnectHandler;
import org.noahsark.server.remote.RemotingClient;
import org.noahsark.server.ws.handler.WebSocketClientDecoder;
import org.noahsark.server.ws.handler.WebsocketEncoder;


/**
 * Websocket 客户端初始化类
 *
 * @author zhangxt
 * @date 2021/3/7.
 */
public class WebSocketClientInitializer extends ChannelInitializer<SocketChannel> {

    private final AbstractRemotingClient client;

    private ReconnectHandler reconnectHandler;

    private WorkQueue workQueue;


    public WebSocketClientInitializer(AbstractRemotingClient client) {
        this.client = client;
        this.workQueue = client.getWorkQueue();

        this.reconnectHandler = new ReconnectHandler(this.client);
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new IdleStateHandler(0, 3, 0));
        pipeline.addLast(new ClientIdleStateTrigger(this.client));
        pipeline.addLast(this.reconnectHandler);
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
        pipeline.addLast(new WebsocketEncoder());
        pipeline.addLast(new WebSocketClientDecoder(
                WebSocketClientHandshakerFactory.newHandshaker(
                        this.client.getServerInfo().getUri(), WebSocketVersion.V13, null,
                        true, new DefaultHttpHeaders()), this.client));
        pipeline.addLast(new ClientBizServiceHandler(this.workQueue));

    }

}
