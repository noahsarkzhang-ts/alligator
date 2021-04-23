package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.constant.RpcCommandVer;
import org.noahsark.server.constant.SerializerType;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/4/10.
 */
@Component
public class RegisterServiceProcessor extends AbstractProcessor<Service> {

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceProcessor.class);

    @Autowired
    private Repository repository;

    @Override
    protected void execute(Service request, RpcContext context) {

        logger.info("receive reg service request: {}" , JsonUtils.toJson(request));

        context.getSession().setSubject(request);

        long currentTime = System.currentTimeMillis();

        request.setLastPingTime(currentTime);
        request.setLoginTime(currentTime);

        repository.registerService(request);

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
        return RegistrationConstants.CMD_REGISTER_SERVICE;
    }
}
