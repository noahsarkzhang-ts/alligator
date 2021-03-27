package org.noahsark.server.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.noahsark.server.remote.ClientIdleStateTrigger;
import org.noahsark.server.remote.ReconnectHandler;
import org.noahsark.server.remote.RemotingClient;

public class ClientHandlersInitializer extends ChannelInitializer<SocketChannel> {

    private RemotingClient client;

    public ClientHandlersInitializer(RemotingClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 3, 0));
        pipeline.addLast(new ClientIdleStateTrigger(this.client));
        pipeline.addLast(new ReconnectHandler(this.client));

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new TcpClientHandler());
    }
}
