package org.noahsark.server.rpc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/26
 */
public class RpcCommand implements Serializable {

    private int requestId;

    private int biz;

    private int cmd;

    private byte type;

    private byte ver;

    private Object payload;

    public RpcCommand() {
    }

    public RpcCommand(Builder builder) {
        this.requestId = builder.requestId;
        this.biz = builder.biz;
        this.cmd = builder.cmd;
        this.type = builder.type;
        this.ver = builder.ver;
        this.payload = builder.payload;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getVer() {
        return ver;
    }

    public void setVer(byte ver) {
        this.ver = ver;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static RpcCommand marshalFromJson(String json) {
        RpcCommand command = new RpcCommand();

        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        command.setRequestId(data.get("requestId").getAsInt());
        command.setBiz(data.get("biz").getAsInt());
        command.setCmd(data.get("cmd").getAsInt());
        command.setType(data.get("type").getAsByte());
        command.setVer(data.get("ver").getAsByte());
        command.setPayload(data.get("payload").getAsJsonObject());

        return command;
    }

    public static class Builder {
        private int requestId;

        private int biz;

        private int cmd;

        private byte type;

        private byte ver;

        private Object payload;

        public Builder requestId(int requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder biz(int biz) {
            this.biz = biz;
            return this;
        }

        public Builder cmd(int cmd) {
            this.cmd = cmd;
            return this;
        }

        public Builder type(byte type) {
            this.type = type;
            return this;
        }

        public Builder ver(byte ver) {
            this.ver = ver;
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public RpcCommand build() {
            return new RpcCommand(this);
        }

    }
}
