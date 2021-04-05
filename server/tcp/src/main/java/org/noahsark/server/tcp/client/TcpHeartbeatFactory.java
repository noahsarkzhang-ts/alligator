package org.noahsark.server.tcp.client;

import org.noahsark.client.heartbeat.HeartbeatFactory;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.tcp.common.Ping;

/**
 * Created by hadoop on 2021/4/3.
 */
public class TcpHeartbeatFactory implements HeartbeatFactory<RpcCommand> {

    @Override
    public RpcCommand getPing() {

        Ping hearBeat = new Ping();
        hearBeat.setLoad(10);

        RpcCommand command = new RpcCommand.Builder()
            .requestId(0)
            .biz(1)
            .cmd(1)
            .type(RpcCommandType.REQUEST)
            .ver(RpcCommandVer.V1)
            .serializer(SerializerType.JSON)
            .payload(hearBeat)
            .build();

        return command;
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
