package org.noahsark.server.hander;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.noahsark.server.rpc.RpcCommand;

import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(RpcCommand.decode(msg));
    }
}
