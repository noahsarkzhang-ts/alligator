package org.noahsark.server.remote;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  用于捕获{@link IdleState#WRITER_IDLE}事件（未在指定时间内向服务器发送数据），然后向<code>Server</code>端发送一个心跳包。
 * </p>
 */
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ClientIdleStateTrigger.class);

    private RemotingClient remotingClient;

    public ClientIdleStateTrigger(RemotingClient remotingClient) {
        this.remotingClient = remotingClient;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {

                log.info("Idle timeout,send heart beat!");

                this.remotingClient.ping();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
