package org.noahsark.remoting.netty;

import io.netty.channel.ChannelHandlerContext;
import org.noahsark.remoting.protocol.RemotingCommand;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/1/14
 */
public interface NettyRequestProcessor {

    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request);

    boolean rejectRequest();

}
