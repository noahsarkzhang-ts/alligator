package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.noahsark.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/6/30
 */
public class ConnectionInactiveHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ConnectionInactiveHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        log.info("client inactive!!!");

        // 连接超过，删除会话
        SessionManager.getInstance().disconnect(ctx);

    }
}
