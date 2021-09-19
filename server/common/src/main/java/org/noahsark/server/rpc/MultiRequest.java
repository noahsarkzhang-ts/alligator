package org.noahsark.server.rpc;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    private Set<String> targetIds;

    public MultiRequest() {
    }

    public MultiRequest(Builder builder) {
        super(builder.requestBuilder);

        this.props = builder.props;
        this.topic = builder.topic;
        this.targetIds = builder.targetIds;

        if (targetIds != null && targetIds.size() > 0) {
            this.fanout = targetIds.size();
        }
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
        ByteBuf body = buf.slice(headSize, buf.readableBytes() - headSize + 2);

        command.setRequestId(header.readInt());
        command.setBiz(header.readInt());
        command.setCmd(header.readInt());
        command.setType(header.readByte());
        command.setEnd(header.readByte());
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

        byte [] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        return data;
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
        buf.writeByte(command.getEnd());
        buf.writeByte(command.getVer());
        buf.writeByte(command.getSerializer());

        if (bprops != null) {
            buf.writeBytes(bprops);
        }

        Object obj = command.getPayload();
        byte[] payload;

        if (obj instanceof JsonObject) {
            payload = obj.toString().getBytes();
        } else if (obj instanceof byte []) {
            payload = (byte []) obj;
        } else {
            Serializer serializer = SerializerManager.getInstance()
                    .getSerializer(command.getSerializer());
            payload = serializer.encode(obj);
        }

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

            Set<String> userIds = Arrays.stream(idSlice).collect(Collectors.toSet());

            this.targetIds = userIds;
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;

        props.put("topic", topic);
    }

    public Set<String> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(Set<String> targetIds) {
        this.targetIds = targetIds;

        String ids = String.join(";", targetIds);
        props.put("targetIds", ids);
    }

    @Override
    public String toString() {
        return "MultiRequest{" +
                "props=" + props +
                ", topic='" + topic + '\'' +
                ", targetIds=" + targetIds +
                "} " + super.toString();
    }

    public static class Builder {

        private Map<String, Object> props = new HashMap<>();

        private String topic;

        private Set<String> targetIds;

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

        public Builder payload(Object payload) {
            this.requestBuilder.payload(payload);
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            this.props.put("topic", topic);

            return this;
        }

        public Builder targetIds(Set<String> targetIds) {
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
        testMulitCode();
    }

    private static void testMulitCode() {
        String sData = "{\"headSize\":17,\"requestId\":100,\"biz\":201,\"cmd\":1,\"type\":1,\"ver\":1,\"serializer\":1,\"payload\":{\"userId\":\"1001\",\"type\":1}}";

        RpcCommand command = RpcCommand.marshalFromJson(sData);

        System.out.println("command = " + command);

        MultiRequest multiRequest = new MultiRequest.Builder()
                .biz(command.getBiz())
                .cmd(command.getCmd())
                .serializer(command.getSerializer())
                .type(command.getType())
                .ver(command.getVer())
                .payload(command.getPayload())
                .topic("TopicTest-1")
                .build();

        System.out.println("multiRequest = " + multiRequest);

        byte [] bData = MultiRequest.encode(multiRequest);

        System.out.println("ByteBufUtil.hexDump(bData) = " + ByteBufUtil.hexDump(bData));
        
        MultiRequest multiRequest2 = MultiRequest.decode(bData);

        System.out.println("multiRequest2 = " + multiRequest2);
        System.out.println("payload = " + ByteBufUtil.hexDump((byte[]) multiRequest2.getPayload()));
        System.out.println("payload = " + new String((byte[]) multiRequest2.getPayload()));
    }

    private static void test() {
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

        Set<String> targetIds = new HashSet<>();
        targetIds.add("inviter-1");
        targetIds.add("inviter-2");

        command.setTargetIds(targetIds);

        byte[] msg = MultiRequest.encode(command);

        System.out.println("");

        MultiRequest mqCommand = MultiRequest.decode(msg);

        System.out.println("command:" + mqCommand);

        System.out.println("ping:" + new String((byte[]) mqCommand.getPayload()));


    }
}

