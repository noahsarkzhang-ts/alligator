package org.noahsark.server.tcp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.RpcCommand;


/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/31
 */
@ChannelHandler.Sharable
public class CommandDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
        throws Exception {

        msg.markReaderIndex();
        short headSize = msg.readShort();
        msg.resetReaderIndex();

        if (RpcCommand.RPC_COMMAND_SIZE == headSize) {
            out.add(RpcCommand.decode(msg));
        } else {
            out.add(MultiRequest.decode(msg));
        }


    }
}
