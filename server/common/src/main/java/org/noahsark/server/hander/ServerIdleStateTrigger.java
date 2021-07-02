package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.internal.StringUtil;
import org.noahsark.server.event.ClientDisconnectEvent;
import org.noahsark.server.eventbus.EventBus;
import org.noahsark.server.session.Session;
import org.noahsark.server.session.SessionManager;
import org.noahsark.server.session.Subject;

/**
 * <p>在规定时间内未收到客户端的任何数据包, 将主动断开该连接</p>
 */
public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {

                // 连接超过，删除会话
                SessionManager.getInstance().disconnect(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
