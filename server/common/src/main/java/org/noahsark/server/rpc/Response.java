package org.noahsark.server.rpc;

import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;

import java.io.Serializable;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Response extends RpcCommand implements Serializable {

    public Response() {

        this.setType(RpcCommandType.RESPONSE);
        this.setVer(RpcCommandVer.V1);

    }

    public Response(Builder builder) {

        super(builder.commandBuilder);

        this.setType(RpcCommandType.RESPONSE);
        this.setVer(RpcCommandVer.V1);
    }

    public static class Builder {

        private RpcCommand.Builder commandBuilder = new RpcCommand.Builder();

        public Builder requestId(int requestId) {
            this.commandBuilder.requestId(requestId);
            return this;
        }

        public Builder biz(int biz) {
            this.commandBuilder.biz(biz);
            return this;
        }

        public Builder cmd(int cmd) {
            this.commandBuilder.cmd(cmd);
            return this;
        }

        public Builder payload(Object payload) {
            this.commandBuilder.payload(payload);
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

}
