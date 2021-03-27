package org.noahsark.server.rpc;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.noahsark.server.session.Session;
import org.noahsark.server.util.JsonUtils;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class RpcContext {

    private Session session;

    private RpcCommand command;

    public RpcContext() {
    }

    public RpcContext(Builder builder) {
        this.command = builder.command;
        this.session = builder.session;
    }

   /* public void sendResponse(Result<?> result) {

        Response response = new Response.Builder()
                .biz(command.getBiz())
                .cmd(command.getCmd())
                .requestId(command.getRequestId())
                .payload(result)
                .build();

        sendResponse(response);
    }

    public void sendResponse(Response response) {
        String text = JsonUtils.toJson(response);

        System.out.println("text = " + text);

        WebSocketFrame frame = new TextWebSocketFrame(text);

        session.getChannel().writeAndFlush(frame);
    }*/

    public void sendResponse(Object repsponse) {
        session.getChannel().writeAndFlush(repsponse);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public RpcCommand getCommand() {
        return command;
    }

    public void setCommand(RpcCommand command) {
        this.command = command;
    }

    public static class Builder {
        private Session session;

        private RpcCommand command;

        public Builder session(Session session) {
            this.session = session;
            return this;
        }

        public Builder command(RpcCommand command) {
            this.command = command;
            return this;
        }

        public RpcContext build() {
            return new RpcContext(this);
        }
    }
}
