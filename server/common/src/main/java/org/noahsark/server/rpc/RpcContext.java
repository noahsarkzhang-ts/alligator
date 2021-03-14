package org.noahsark.server.rpc;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.noahsark.server.session.Session;
import org.noahsark.server.util.JsonUtils;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class RpcContext {

    private Session session;

    private Request request;


    public RpcContext() {}

    public void sendResponse(Response<?> response) {
        session.getChannel().writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(response)));
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
