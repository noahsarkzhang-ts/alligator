package org.noahsark.server.rpc;

import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Request extends RpcCommand implements Serializable {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    public Request() {
    }

    public Request(Builder builder) {
        super(builder.commandBuilder);
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

        public Builder requestId(int requestId) {
            this.commandBuilder.requestId(requestId);
            return this;
        }

        public Builder type(byte type) {
            this.commandBuilder.type(type);
            return this;
        }

        public Builder ver(byte ver) {
            this.commandBuilder.ver(ver);
            return this;
        }

        public Builder serializer(byte serializer) {
            this.commandBuilder.serializer(serializer);
            return this;
        }

        public Builder payload(Object payload) {
            this.commandBuilder.payload(payload);
            return this;
        }

        public Request build() {

            this.commandBuilder.type(RpcCommandType.REQUEST);
            this.commandBuilder.ver(RpcCommandVer.V1);
            this.commandBuilder.serializer(SerializerType.JSON);

            return new Request(this);
        }
    }

    @Override
    public String toString() {
        return "Request{} " + super.toString();
    }
}
