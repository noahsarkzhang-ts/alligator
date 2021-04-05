package org.noahsark.server.tcp.processor;

import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.tcp.client.TcpHeartbeatFactory;
import org.noahsark.server.tcp.common.Ping;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/4/3.
 */
public class PingProcessor extends AbstractProcessor<Ping> {

    private static Logger log = LoggerFactory.getLogger(PingProcessor.class);


    @Override
    protected void execute(Ping request, RpcContext context) {
        log.info("Receive a ping message: {}", JsonUtils.toJson(request));

        RpcCommand command = TcpHeartbeatFactory.getPong(context.getCommand());

        context.sendResponse(command);
    }

    @Override
    protected Class<Ping> getParamsClass() {
        return Ping.class;
    }

    @Override
    protected int getBiz() {
        return 1;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}
