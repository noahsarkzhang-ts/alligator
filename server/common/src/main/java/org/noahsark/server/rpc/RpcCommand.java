package org.noahsark.server.rpc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.noahsark.server.serializer.Serializer;
import org.noahsark.server.serializer.SerializerManager;

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

    private byte serializer;

    private Object payload;

    public RpcCommand() {
    }

    public RpcCommand(Builder builder) {
        this.requestId = builder.requestId;
        this.biz = builder.biz;
        this.cmd = builder.cmd;
        this.type = builder.type;
        this.ver = builder.ver;
        this.serializer = builder.serializer;
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

    public byte getSerializer() {
        return serializer;
    }

    public void setSerializer(byte serializer) {
        this.serializer = serializer;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static ByteBuf encode(ChannelHandlerContext ctx, RpcCommand command) {

        ByteBuf buf = ctx.alloc().buffer();

        buf.writeInt(command.getRequestId());
        buf.writeInt(command.getBiz());
        buf.writeInt(command.getCmd());
        buf.writeByte(command.getType());
        buf.writeByte(command.getVer());
        buf.writeByte(command.getSerializer());

        Serializer serializer = SerializerManager.getInstance().getSerializer(command.getSerializer());
        byte [] payload = serializer.encode(command.getPayload());

        buf.writeBytes(payload);

        return buf;
    }

    public static RpcCommand decode(ByteBuf msg) {

        RpcCommand command = new RpcCommand();

        command.setRequestId(msg.readInt());
        command.setBiz(msg.readInt());
        command.setCmd(msg.readInt());
        command.setType(msg.readByte());
        command.setVer(msg.readByte());
        command.setSerializer(msg.readByte());

        byte [] payload = new byte[msg.readableBytes()];
        msg.readBytes(payload);

        command.setPayload(payload);

        return command;
    }

    public static RpcCommand marshalFromJson(String json) {
        RpcCommand command = new RpcCommand();

        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        command.setRequestId(data.get("requestId").getAsInt());
        command.setBiz(data.get("biz").getAsInt());
        command.setCmd(data.get("cmd").getAsInt());
        command.setType(data.get("type").getAsByte());
        command.setVer(data.get("ver").getAsByte());
        command.setSerializer(data.get("serializer").getAsByte());
        command.setPayload(data.get("payload").getAsJsonObject());

        return command;
    }

    @Override
    public String toString() {
        return "RpcCommand{" +
                "requestId=" + requestId +
                ", biz=" + biz +
                ", cmd=" + cmd +
                ", type=" + type +
                ", ver=" + ver +
                ", serializer=" + serializer +
                ", payload=" + payload +
                '}';
    }

    public static class Builder {
        private int requestId;

        private int biz;

        private int cmd;

        private byte type;

        private byte ver;

        private byte serializer;

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

        public Builder serializer(byte serializer) {
            this.serializer = serializer;
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
