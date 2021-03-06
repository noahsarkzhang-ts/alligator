package org.noahsark.server.rpc;

import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.session.ChannelHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class RpcContext {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    private ChannelHolder session;

    private RpcCommand command;

    public RpcContext() {
    }

    public RpcContext(Builder builder) {
        this.command = builder.command;
        this.session = builder.session;
    }

    public void sendResponse(RpcCommand repsponse) {
        log.info("send response:{}", repsponse);

        session.write(repsponse);
    }

    public ChannelHolder getSession() {
        return session;
    }

    public void setSession(ChannelHolder session) {
        this.session = session;
    }

    public RpcCommand getCommand() {
        return command;
    }

    public void setCommand(RpcCommand command) {
        this.command = command;
    }

    public static class Builder {
        private ChannelHolder session;

        private RpcCommand command;

        public Builder session(ChannelHolder session) {
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
