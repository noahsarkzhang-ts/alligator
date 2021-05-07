package org.noahsark.server.rpc;

import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;

import java.io.Serializable;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Response extends RpcCommand implements Serializable {

    public Response() {

        this.setType(RpcCommandType.RESPONSE);
        this.setVer(RpcCommandVer.V1);
        this.setSerializer(SerializerType.JSON);
    }

    public Response(Builder builder) {

        super(builder.commandBuilder);

        this.setType(RpcCommandType.RESPONSE);
        this.setVer(RpcCommandVer.V1);
        this.setSerializer(SerializerType.JSON);
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

        public Response build() {
            this.commandBuilder.type(RpcCommandType.RESPONSE);
            this.commandBuilder.ver(RpcCommandVer.V1);
            this.commandBuilder.serializer(SerializerType.JSON);

            return new Response(this);
        }
    }

    public static Response buildCommonResponse(RpcCommand request, int code, String message) {
        Result<Void> result = new Result.Builder<Void>()
            .code(code)
            .message(message)
            .build();

        Response command = new Response.Builder()
            .requestId(request.getRequestId())
            .biz(request.getBiz())
            .cmd(request.getCmd())
            .payload(result)
            .build();

        return command;
    }

    public static <T> Response buildResponse(RpcCommand request, T t ,int code, String message) {

        Result<T> result = new Result.Builder<T>()
                .code(code)
                .message(message)
                .data(t)
                .build();

        Response command = new Response.Builder()
                .requestId(request.getRequestId())
                .biz(request.getBiz())
                .cmd(request.getCmd())
                .payload(result)
                .build();

        return command;
    }

}
