package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.Id;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户退出
 *
 * @author zhangxt
 * @date 2021/4/10
 */
@Deprecated
@Component
public class LogoutProcessor extends AbstractProcessor<Id> {

    @Autowired
    private Repository repository;

    @Override
    protected void execute(Id request, RpcContext context) {

        // TODO 从注册中心移除
        context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                0, "success"));

    }

    @Override
    protected Class<Id> getParamsClass() {
        return Id.class;
    }

    @Override
    protected int getBiz() {
        return RegistrationConstants.BIZ_TYPE;
    }

    @Override
    protected int getCmd() {
        return RegistrationConstants.CMD_LOGOUT;
    }
}
