package org.noahsark.server.hander;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/3/28.
 */
public class AuthHandler extends SimpleChannelInboundHandler<RpcCommand> {

  private static Logger log = LoggerFactory.getLogger(AuthHandler.class);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {

    // TODO 用户认证

    ctx.fireChannelRead(msg);

  }
}
