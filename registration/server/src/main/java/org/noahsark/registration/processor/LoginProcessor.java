package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户注册
 *
 * @author zhangxt
 * @date 2021/4/10
 */
@Deprecated
@Component
public class LoginProcessor extends AbstractProcessor<User> {

    @Autowired
    private Repository repository;

    @Override
    protected void execute(User request, RpcContext context) {

        // TODO 从注册中心移除
        context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                0, "success"));

    }

    @Override
    protected Class<User> getParamsClass() {
        return User.class;
    }

    @Override
    protected int getBiz() {
        return RegistrationConstants.BIZ_TYPE;
    }

    @Override
    protected int getCmd() {
        return RegistrationConstants.CMD_LOGIN;
    }
}
