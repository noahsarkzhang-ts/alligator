package org.noahsark.server.ws.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.List;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.util.JsonUtils;

/**
 * Created by hadoop on 2021/4/3.
 */
public class WebsocketEncoder extends MessageToMessageEncoder<RpcCommand> {

  @Override
  protected void encode(ChannelHandlerContext ctx, RpcCommand msg, List<Object> out)
      throws Exception {
    out.add(new TextWebSocketFrame(JsonUtils.toJson(msg)));
  }
}
