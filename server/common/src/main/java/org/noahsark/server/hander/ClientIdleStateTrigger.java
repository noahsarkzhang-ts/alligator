package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.noahsark.client.heartbeat.HeartbeatStatus;
import org.noahsark.server.remote.RemotingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于捕获{@link IdleState#WRITER_IDLE}事件（未在指定时间内向服务器发送数据），然后向Server端发送一个心跳包。
 * @author zhangxt
 * @date 2021/4/3
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

                HeartbeatStatus heartbeatStatus = remotingClient.getConnectionManager()
                    .getHeartbeatStatus();

                if (heartbeatStatus.incAndtimeout()) {
                    log.info("server time out, and toggle server");

                    heartbeatStatus.reset();

                    this.remotingClient.toggleServer();

                    return;
                }

                this.remotingClient.ping();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
