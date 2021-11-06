package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.UserQuery;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/4/11.
 */
@Component
public class UserLookupingProcessor extends AbstractProcessor<UserQuery> {

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceProcessor.class);

    @Autowired
    private Repository repository;

    @Override
    protected void execute(UserQuery request, RpcContext context) {
        logger.info("receive query inviter service request: {}", JsonUtils.toJson(request));

        // TODO 从注册中心移除
    }

    @Override
    protected Class<UserQuery> getParamsClass() {
        return UserQuery.class;
    }

    @Override
    protected int getBiz() {
        return RegistrationConstants.BIZ_TYPE;
    }

    @Override
    protected int getCmd() {
        return RegistrationConstants.CMD_LOOKUP_USER;
    }
}
