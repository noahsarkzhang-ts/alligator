package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.client.future.Connection;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/4/3.
 */
public class ClientBizServiceHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(ClientBizServiceHandler.class);

    private WorkQueue workQueue;

    public ClientBizServiceHandler() {
    }

    public ClientBizServiceHandler(WorkQueue workQueue) {
        this.workQueue = workQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {

        Connection connection = ctx.channel().attr(Connection.CONNECTION).get();
        if (connection == null) {
            log.warn("No connection,requestId : {}", msg.getRequestId());
            return;
        }
        log.info("receive msg: {}", msg);

        try {
            Session session = Session.getOrCreatedSession(connection);

            if (msg.getType() == RpcCommandType.REQUEST
                    || msg.getType() == RpcCommandType.REQUEST_ONEWAY) {
                RequestHandler.processRequest(ctx, msg, workQueue, session);
            } else {
                RequestHandler.processResponse(connection, msg);
            }

        } catch (Exception ex) {
            log.warn("catch an exception:{}", ex);
        }
    }
}
