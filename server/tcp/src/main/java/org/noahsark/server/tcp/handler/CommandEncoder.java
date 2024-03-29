package org.noahsark.server.tcp.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.RpcCommand;

/**
 * 编码处理器
 * @author zhangxt
 * @date 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToMessageEncoder<RpcCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out)
        throws Exception {

        if (msg instanceof MultiRequest) {
            out.add(MultiRequest.encode(ctx, msg));
        } else {
            out.add(RpcCommand.encode(ctx, msg));
        }

    }
}
