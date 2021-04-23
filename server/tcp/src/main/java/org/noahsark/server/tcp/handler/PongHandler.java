package org.noahsark.server.tcp.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.remote.RemotingClient;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/4/3.
 */
@ChannelHandler.Sharable
public class PongHandler extends SimpleChannelInboundHandler<RpcCommand> {

    private static Logger log = LoggerFactory.getLogger(PongHandler.class);

    private RemotingClient client;

    public PongHandler(RemotingClient client) {
        this.client = client;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCommand msg) throws Exception {
        if (msg.getBiz() == 1 && msg.getCmd() == 1
            && msg.getType() == RpcCommandType.RESPONSE) {

            byte [] result = (byte[]) msg.getPayload();
            String json = new String(result, CharsetUtil.UTF_8);

            // 清空心跳计数
            client.getConnectionManager().getHeartbeatStatus().reset();

            log.info("receive a pong message:{}", JsonUtils.toJson(JsonUtils
                .fromJsonObject(json,Void.class)));

            return;
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
