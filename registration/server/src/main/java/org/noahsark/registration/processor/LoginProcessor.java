package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;

/**
 * Created by hadoop on 2021/4/10.
 */
public class LoginProcessor extends AbstractProcessor<User> {

    private Repository repository;

    @Override
    protected void execute(User request, RpcContext context) {
        Service service = (Service) context.getSession().getSubject();

        repository.login(request,service);

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
