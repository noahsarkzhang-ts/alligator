package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.noahsark.server.session.SessionManager;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/6/30
 */
public class ConnectionInactiveHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 连接超过，删除会话
        SessionManager.getInstance().disconnect(ctx);

    }
}
