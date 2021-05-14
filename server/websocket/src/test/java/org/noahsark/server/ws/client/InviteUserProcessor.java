package org.noahsark.server.ws.client;

import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/13
 */
public class InviteUserProcessor extends AbstractProcessor<InviteInfo> {
    @Override
    protected void execute(InviteInfo request, RpcContext context) {
        context.sendResponse(Response.buildCommonResponse(context.getCommand(),0,"success"));
    }

    @Override
    protected Class<InviteInfo> getParamsClass() {
        return InviteInfo.class;
    }

    @Override
    protected int getBiz() {
        return 300;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}
