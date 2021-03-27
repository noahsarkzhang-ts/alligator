package org.noahsark.server.rpc;

import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Request extends RpcCommand implements Serializable {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    public Request() {

        this.setRequestId(nextId());
        this.setType(RpcCommandType.REQUEST);
        this.setVer(RpcCommandVer.V1);

    }

    public Request(Builder builder) {

        super(builder.commandBuilder);

        this.setRequestId(nextId());
        this.setType(RpcCommandType.REQUEST);
        this.setVer(RpcCommandVer.V1);
    }

    public static final int nextId() {
        return NEXT_ID.getAndIncrement();
    }


    public static class Builder {
        private RpcCommand.Builder commandBuilder = new RpcCommand.Builder();

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

        public Request build() {
            return new Request(this);
        }
    }
}
