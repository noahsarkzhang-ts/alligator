package org.noahsark.server.ws.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import org.noahsark.server.remote.RemotingClient;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解码处理器
 *
 * @author zhangxt
 * @date 2021/4/3
 */
@ChannelHandler.Sharable
public class WebSocketClientDecoder extends SimpleChannelInboundHandler<Object> {

    private static Logger log = LoggerFactory.getLogger(WebSocketClientDecoder.class);

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;
    private RemotingClient client;

    public WebSocketClientDecoder(WebSocketClientHandshaker handshaker, RemotingClient client) {
        this.handshaker = handshaker;
        this.client = client;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                log.info("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException ex) {
                log.warn("WebSocket Client failed to connect");
                handshakeFuture.setFailure(ex);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String response = textFrame.text();
            log.info("WebSocket Client received message: " + response);

            if (StringUtil.isNullOrEmpty(response)) {
                return;
            }

            RpcCommand command = RpcCommand.marshalFromJson(response);

            ctx.fireChannelRead(command);

        } else if (frame instanceof PongWebSocketFrame) {
            // 清空心跳计数
            client.getConnectionManager().getHeartbeatStatus().reset();

            log.info("WebSocket Client received pong");

        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("WebSocket Client received closing");
            ch.close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
