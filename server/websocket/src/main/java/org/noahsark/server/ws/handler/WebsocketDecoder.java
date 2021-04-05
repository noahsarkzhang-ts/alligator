package org.noahsark.server.ws.handler;

import com.google.gson.JsonSyntaxException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/3/28.
 */
public class WebsocketDecoder extends SimpleChannelInboundHandler<WebSocketFrame> {

  private static Logger log = LoggerFactory.getLogger(WebsocketDecoder.class);

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
    Response response = null;
    Result<Void> result = new Result<>();
    String message = null;

    try {

      if (msg instanceof TextWebSocketFrame) {
        // Send the uppercase string back.
        String request = ((TextWebSocketFrame) msg).text();
        log.info("receive request: {}", request);

        RpcCommand command = RpcCommand.marshalFromJson(request);

        ctx.fireChannelRead(command);

        return;

        //ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
      } else {
        message = "unsupported frame type: " + msg.getClass().getName();
        throw new UnsupportedOperationException(message);
      }
    } catch (JsonSyntaxException e) {
      result.setCode(1001);
      result.setMessage("Format Error!");

      response = new Response.Builder()
          .requestId(0)
          .biz(0)
          .cmd(0)
          .payload(result)
          .build();

    } catch (UnsupportedOperationException e) {
      result.setCode(1002);
      result.setMessage(message);

      response = new Response.Builder()
          .requestId(0)
          .biz(0)
          .cmd(0)
          .payload(result)
          .build();
    }

    ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));
  }
}
