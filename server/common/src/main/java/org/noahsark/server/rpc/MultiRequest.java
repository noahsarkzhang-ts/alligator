package org.noahsark.server.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;
import org.noahsark.server.serializer.Serializer;
import org.noahsark.server.serializer.SerializerManager;
import org.noahsark.server.util.JsonUtils;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/22
 */
public class MultiRequest extends Request {

    private static final int RPC_COMMAND_SIZE = 16;

    /**
     * 扩展字段
     * 包括：
     * 1：topic,发起方topic,用于接收结果；
     * 2: targetIds,目标用户
     */
    private Map<String, Object> props = new HashMap<>();

    /**
     * 发起方topic,用于接收结果
     */
    private String topic;

    /**
     * targetIds,目标用户
     */
    private List<String> targetIds;

    public MultiRequest() {
    }

    public MultiRequest(Builder builder) {
        super(builder.requestBuilder);

        this.props = builder.props;
        this.topic = builder.topic;
        this.targetIds = builder.targetIds;
    }

    public static ByteBuf encode(ChannelHandlerContext ctx, RpcCommand command) {

        ByteBuf buf = ctx.alloc().buffer();

        MultiRequest request = (MultiRequest) command;

        encode(buf, request);

        return buf;
    }

    public static MultiRequest decode(ByteBuf buf) {

        MultiRequest command = new MultiRequest();

        decode(buf, command);

        return command;
    }

    public static MultiRequest decode(byte[] msg) {

        MultiRequest command = new MultiRequest();
        ByteBuf buf = Unpooled.wrappedBuffer(msg);

        decode(buf, command);

        return command;
    }

    public static void decode(ByteBuf buf, MultiRequest command) {

        short headSize = buf.readShort();

        command.setHeadSize(headSize);

        ByteBuf header = buf.slice(2, headSize - 2);
        ByteBuf body = buf.slice(headSize, buf.readableBytes() - headSize);

        command.setRequestId(header.readInt());
        command.setBiz(header.readInt());
        command.setCmd(header.readInt());
        command.setType(header.readByte());
        command.setVer(header.readByte());
        command.setSerializer(header.readByte());

        byte[] props = new byte[header.readableBytes()];
        header.readBytes(props);

        command.setProps(JsonUtils.fromJson(new String(props), Map.class));

        byte[] payload = new byte[body.readableBytes()];

        body.readBytes(payload);

        command.setPayload(payload);
    }

    public static byte[] encode(MultiRequest command) {

        ByteBuf buf = Unpooled.buffer();

        encode(buf, command);

        return buf.array();
    }

    private static void encode(ByteBuf buf, MultiRequest command) {
        int headSize = RpcCommand.RPC_COMMAND_SIZE;
        byte[] bprops = null;

        if (!command.getProps().isEmpty()) {

            bprops = JsonUtils.toJson(command.getProps()).getBytes(CharsetUtil.UTF_8);
            headSize += bprops.length;
        }

        buf.writeShort(headSize);
        buf.writeInt(command.getRequestId());
        buf.writeInt(command.getBiz());
        buf.writeInt(command.getCmd());
        buf.writeByte(command.getType());
        buf.writeByte(command.getVer());
        buf.writeByte(command.getSerializer());

        if (bprops != null) {
            buf.writeBytes(bprops);
        }

        Serializer serializer = SerializerManager.getInstance()
            .getSerializer(command.getSerializer());
        byte[] payload = serializer.encode(command.getPayload());

        buf.writeBytes(payload);
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;

        if (props.containsKey("topic")) {
            this.topic = (String) props.get("topic");
        }

        if (props.containsKey("targetIds")) {
            String ids = (String) props.get("targetIds");

            String[] idSlice = ids.split(";");

            List<String> list = Arrays.asList(idSlice);

            this.targetIds = list;
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;

        props.put("topic", topic);
    }

    public List<String> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<String> targetIds) {
        this.targetIds = targetIds;

        String ids = String.join(";", targetIds);
        props.put("targetIds", ids);
    }

    @Override
    public String toString() {
        return "MultiRequest{" +
            "props=" + props +
            ", topic='" + topic + '\'' +
            ", tartgetIds=" + targetIds +
            '}';
    }

    public static class Builder {

        private Map<String, Object> props = new HashMap<>();

        private String topic;

        private List<String> targetIds;

        private Request.Builder requestBuilder = new Request.Builder();

        public Builder biz(int biz) {
            this.requestBuilder.biz(biz);
            return this;
        }

        public Builder cmd(int cmd) {
            this.requestBuilder.cmd(cmd);
            return this;
        }


        public Builder requestId(int requestId) {
            this.requestBuilder.requestId(requestId);
            return this;
        }

        public Builder type(byte type) {
            this.requestBuilder.type(type);
            return this;
        }

        public Builder ver(byte ver) {
            this.requestBuilder.ver(ver);
            return this;
        }

        public Builder serializer(byte serializer) {
            this.requestBuilder.serializer(serializer);
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            this.props.put("topic", topic);

            return this;
        }

        public Builder targetIds(List<String> targetIds) {
            this.targetIds = targetIds;

            String ids = String.join(";", targetIds);
            this.props.put("targetIds", ids);

            return this;
        }

        public Builder prop(String key, Object val) {
            this.props.put(key, val);

            return this;
        }

        public MultiRequest build() {
            this.requestBuilder.type(RpcCommandType.REQUEST);
            this.requestBuilder.ver(RpcCommandVer.V1);
            this.requestBuilder.serializer(SerializerType.JSON);

            return new MultiRequest(this);
        }
    }

    private static class Ping {

        private int load;

        public int getLoad() {
            return load;
        }

        public void setLoad(int load) {
            this.load = load;
        }

        @Override
        public String toString() {
            return "Ping{" +
                "load=" + load +
                '}';
        }
    }

    public static void main(String[] args) {
        MultiRequest command = new MultiRequest();

        command.setRequestId(1);
        command.setBiz(1);
        command.setCmd(23);
        command.setSerializer(SerializerType.JSON);
        command.setVer(RpcCommandVer.V1);
        command.setType(RpcCommandType.REQUEST);

        Ping ping = new Ping();
        ping.setLoad(10);

        command.setPayload(ping);

        command.setTopic("t_reg");

        ArrayList<String> targetIds = new ArrayList<>();
        targetIds.add("user-1");
        targetIds.add("user-2");

        command.setTargetIds(targetIds);

        byte[] msg = MultiRequest.encode(command);

        System.out.println("");

        MultiRequest mqCommand = MultiRequest.decode(msg);

        System.out.println("command:" + mqCommand);

        System.out.println("ping:" + new String((byte[]) mqCommand.getPayload()));
    }
}
