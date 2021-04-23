package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.client.future.Connection;
import org.noahsark.client.future.FutureManager;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/4/3.
 */
public class ClientBizServiceHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(ClientBizServiceHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {

        Connection connection = ctx.channel().attr(Connection.CONNECTION).get();

        if (connection == null) {

            log.warn("No connection,requestId : {}", msg.getRequestId());
            return;
        }

        log.info("receive msg: {}", msg);

        RpcPromise promise = connection.getPromise(msg.getRequestId());

        if (promise != null) {
            promise.setSuccess(msg.getPayload());
            connection.removePromis(msg.getRequestId());

        } else {
            log.warn("promis is null : {}", msg.getRequestId());
        }
    }
}
