package org.noahsark.server.hander;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.noahsark.server.rpc.RpcCommand;

import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandEncoder extends MessageToMessageEncoder<RpcCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out) throws Exception {
        out.add(RpcCommand.encode(ctx,msg));
    }
}
