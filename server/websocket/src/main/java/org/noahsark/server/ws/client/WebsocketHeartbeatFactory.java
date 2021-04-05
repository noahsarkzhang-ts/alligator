package org.noahsark.server.ws.client;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.noahsark.client.heartbeat.HeartbeatFactory;

/**
 * Created by hadoop on 2021/4/3.
 */
public class WebsocketHeartbeatFactory implements HeartbeatFactory<WebSocketFrame> {

    @Override
    public WebSocketFrame getPing() {

        WebSocketFrame frame = new PingWebSocketFrame(
            Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));

        return frame;
    }

}
