package org.noahsark.server.tcp.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.tcp.common.HearBeat;

/**
 * <p>收到来自客户端的数据包后, 直接在控制台打印出来.</p>
 */
@ChannelHandler.Sharable
public class TcpServerHandler extends SimpleChannelInboundHandler<RpcCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand data) throws Exception {
        try {
            System.out.println("receive data: " + data);

            HearBeat hearBeat = new HearBeat();
            hearBeat.setLoad(1);

            Response response = new Response.Builder()
                    .biz(data.getBiz())
                    .cmd(data.getCmd())
                    .requestId(data.getRequestId())
                    .payload(hearBeat)
                    .build();

            ctx.writeAndFlush(response);
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
