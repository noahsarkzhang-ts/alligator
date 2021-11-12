package org.noahsark.server.rpc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import org.noahsark.server.serializer.Serializer;
import org.noahsark.server.serializer.SerializerManager;

import java.io.Serializable;

/**
 * RPC 命令类
 * @author zhangxt
 * @date 2021/3/26
 */
public class RpcCommand implements Serializable {

    public static final short RPC_COMMAND_SIZE = 18;

    /**
     * 消息头长度，消息头有定长和不定长
     */
    private short headSize;

    /**
     * 请求id
     */
    private int requestId;

    /**
     * 业务分类
     */
    private int biz;

    /**
     * 命令id
     *
     */
    private int cmd;

    /**
     * 消息类型，1：请求，2：响应，3：oneway，4：stream
     */
    private byte type;

    /**
     * 是否最后一个响应，0：不是，1：是
     */
    private byte end;

    /**
     * 接口版本
     */
    private byte ver;

    /**
     * 序列化类型
     */
    private byte serializer;

    /**
     * 消息内容
     */
    private Object payload;

    /**
     * 附加字段
     */
    private Object attachment;

    public RpcCommand() {
    }

    public RpcCommand(Builder builder) {

        this.headSize = RPC_COMMAND_SIZE;
        this.requestId = builder.requestId;
        this.biz = builder.biz;
        this.cmd = builder.cmd;
        this.type = builder.type;
        this.end = builder.end;
        this.ver = builder.ver;
        this.serializer = builder.serializer;
        this.payload = builder.payload;
    }

    public static ByteBuf encode(ChannelHandlerContext ctx, RpcCommand command) {

        ByteBuf buf = ctx.alloc().buffer();

        encode(buf, command);

        return buf;
    }

    public static RpcCommand decode(ByteBuf msg) {

        RpcCommand command = new RpcCommand();

        decode(msg, command);

        return command;
    }

    public static RpcCommand decode(byte[] msg) {

        RpcCommand command = new RpcCommand();
        ByteBuf buf = Unpooled.wrappedBuffer(msg);

        decode(buf, command);

        return command;
    }

    public static void decode(ByteBuf buf, RpcCommand command) {

        command.setHeadSize(buf.readShort());
        command.setRequestId(buf.readInt());
        command.setBiz(buf.readInt());
        command.setCmd(buf.readInt());
        command.setType(buf.readByte());
        command.setEnd(buf.readByte());
        command.setVer(buf.readByte());
        command.setSerializer(buf.readByte());

        byte[] payload = new byte[buf.readableBytes()];
        buf.readBytes(payload);

        command.setPayload(payload);
    }

    public static byte[] encode(RpcCommand command) {

        ByteBuf buf = Unpooled.buffer();

        encode(buf, command);

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        return data;

    }

    private static void encode(ByteBuf buf, RpcCommand command) {

        buf.writeShort(RPC_COMMAND_SIZE);
        buf.writeInt(command.getRequestId());
        buf.writeInt(command.getBiz());
        buf.writeInt(command.getCmd());
        buf.writeByte(command.getType());
        buf.writeByte(command.getEnd());
        buf.writeByte(command.getVer());
        buf.writeByte(command.getSerializer());

        byte[] payload;

        if (! (command.getPayload() instanceof byte [])) {
            Serializer serializer = SerializerManager.getInstance()
                    .getSerializer(command.getSerializer());
            payload = serializer.encode(command.getPayload());
        } else {
            payload = (byte[]) command.getPayload();
        }

        buf.writeBytes(payload);

    }

    public static RpcCommand marshalFromJson(String json) {
        RpcCommand command = new RpcCommand();

        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        command.setHeadSize(data.get("headSize").getAsShort());
        command.setRequestId(data.get("requestId").getAsInt());
        command.setBiz(data.get("biz").getAsInt());
        command.setCmd(data.get("cmd").getAsInt());
        command.setType(data.get("type").getAsByte());
        command.setEnd(data.get("end").getAsByte());
        command.setVer(data.get("ver").getAsByte());
        command.setSerializer(data.get("serializer").getAsByte());
        command.setPayload(data.get("payload").getAsJsonObject().toString().getBytes(CharsetUtil.UTF_8));

        return command;
    }

    @Override
    public String toString() {
        return "RpcCommand{" +
                "headSize=" + headSize +
                ", requestId=" + requestId +
                ", biz=" + biz +
                ", cmd=" + cmd +
                ", type=" + type +
                ", end=" + end +
                ", ver=" + ver +
                ", serializer=" + serializer +
                ", payload=" + ((payload instanceof byte[]) ? new JsonParser().parse(new String((byte[]) payload)).getAsJsonObject() : payload) +
                '}';
    }

    public static class Builder {

        private int requestId;

        private int biz;

        private int cmd;

        private byte type;

        private byte end;

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

        public Builder end(byte end) {
            this.end = end;

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

    public byte getEnd() {
        return end;
    }

    public void setEnd(byte end) {
        this.end = end;
    }

    public short getHeadSize() {
        return headSize;
    }

    public void setHeadSize(short headSize) {
        this.headSize = headSize;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
