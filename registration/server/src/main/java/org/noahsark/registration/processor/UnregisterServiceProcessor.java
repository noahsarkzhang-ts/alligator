package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;

/**
 * Created by hadoop on 2021/4/11.
 */
public class UnregisterServiceProcessor extends AbstractProcessor<Service> {

    private Repository repository;

    @Override
    protected void execute(Service request, RpcContext context) {

        repository.unRegisterService(request.getId());

        context.sendResponse(Response.buildCommonResponse(context.getCommand(),
            0, "success"));

    }

    @Override
    protected Class<Service> getParamsClass() {
        return Service.class;
    }

    @Override
    protected int getBiz() {
        return RegistrationConstants.BIZ_TYPE;
    }

    @Override
    protected int getCmd() {
        return RegistrationConstants.CMD_UNREGISTER_SERVICE;
    }
}
