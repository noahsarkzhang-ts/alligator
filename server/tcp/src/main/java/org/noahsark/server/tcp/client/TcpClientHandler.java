package org.noahsark.server.tcp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.server.rpc.RpcCommand;

public class TcpClientHandler extends SimpleChannelInboundHandler<RpcCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand data) throws Exception {
        try {
            System.out.println("receive data: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Established connection with the remote client.");

        // do something

        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Disconnected with the remote client.");

        // do something

        ctx.fireChannelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
