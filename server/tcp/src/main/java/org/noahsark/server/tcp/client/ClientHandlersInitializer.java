package org.noahsark.server.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.noahsark.server.hander.ClientBizServiceHandler;
import org.noahsark.server.hander.ClientIdleStateTrigger;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.remote.ReconnectHandler;
import org.noahsark.server.remote.RemotingClient;
import org.noahsark.server.tcp.handler.CommandDecoder;
import org.noahsark.server.tcp.handler.CommandEncoder;
import org.noahsark.server.tcp.handler.PongHandler;

public class ClientHandlersInitializer extends ChannelInitializer<SocketChannel> {

    private AbstractRemotingClient client;

    private ReconnectHandler reconnectHandler;

    private WorkQueue workQueue;

    public ClientHandlersInitializer(AbstractRemotingClient client) {
        this.client = client;

        this.workQueue = client.getWorkQueue();

        reconnectHandler = new ReconnectHandler(this.client);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 15, 0));
        pipeline.addLast(new ClientIdleStateTrigger(this.client));
        pipeline.addLast(reconnectHandler);

        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new CommandDecoder());
        pipeline.addLast(new CommandEncoder());
        pipeline.addLast(new PongHandler(client));
        pipeline.addLast(new ClientBizServiceHandler(this.workQueue));
    }
}
