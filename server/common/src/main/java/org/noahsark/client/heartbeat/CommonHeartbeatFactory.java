package org.noahsark.client.heartbeat;

import org.noahsark.client.ping.Ping;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcCommand;

/**
 * Created by hadoop on 2021/4/3.
 */
public class CommonHeartbeatFactory implements HeartbeatFactory<RpcCommand> {

    private PingPayloadGenerator payloadGenerator;

    @Override
    public RpcCommand getPing() {

        Object payload;
        if (payloadGenerator != null) {
            payload = payloadGenerator.getPayload();
        } else {
            Ping hearBeat = new Ping();
            hearBeat.setLoad(0);

            payload = hearBeat;
        }

        RpcCommand command = new RpcCommand.Builder()
            .requestId(0)
            .biz(1)
            .cmd(1)
            .type(RpcCommandType.REQUEST)
            .ver(RpcCommandVer.V1)
            .serializer(SerializerType.JSON)
            .payload(payload)
            .build();

        return command;
    }

    @Override
    public PingPayloadGenerator getPayloadGenerator() {
        return this.payloadGenerator;
    }

    @Override
    public void setPayloadGenerator(PingPayloadGenerator payload) {
        this.payloadGenerator = payload;

    }

    public static RpcCommand getPong(RpcCommand ping) {

        Result<Void> result = new Result.Builder<Void>()
            .code(0)
            .message("success")
            .build();

        RpcCommand command = new RpcCommand.Builder()
            .requestId(0)
            .biz(1)
            .cmd(1)
            .type(RpcCommandType.RESPONSE)
            .ver(RpcCommandVer.V1)
            .serializer(SerializerType.JSON)
            .payload(result)
            .build();

        return command;
    }
}
