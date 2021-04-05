package org.noahsark.server.rpc;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.session.Session;

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

    public void sendResponse(Object repsponse) {
        session.getChannel().writeAndFlush(repsponse);
    }

    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        promise.invoke(this.getSession().getConnection(),request,commandCallback,timeoutMillis);

        return promise;
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
