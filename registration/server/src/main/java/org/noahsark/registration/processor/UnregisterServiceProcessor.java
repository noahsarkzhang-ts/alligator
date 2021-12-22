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
 * 服务下线
 * @author zhangxt
 * @date 2021/4/11
 */
@Component
public class UnregisterServiceProcessor extends AbstractProcessor<Id> {

    @Autowired
    private Repository repository;

    @Override
    protected void execute(Id request, RpcContext context) {

        repository.unRegisterService(request.getId());

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
        return RegistrationConstants.CMD_UNREGISTER_SERVICE;
    }
}
