package org.noahsark.registration.processor;

import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.ServiceQuery;
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
 * @author: noahsark
 * @version:
 * @date: 2021/7/9
 */
@Component
public class GetUniqueServiceProcessor extends AbstractProcessor<ServiceQuery> {
    private static Logger logger = LoggerFactory.getLogger(GetAllServiceProcessor.class);

    @Autowired
    private Repository repository;


    @Override
    protected void execute(ServiceQuery request, RpcContext context) {
        logger.info("receive query service request: {}" , JsonUtils.toJson(request));

        Service service = repository.getServiceById(request.getId());

        CandidateService candidateService = new CandidateService();
        candidateService.setAddress(service.getAddress());
        candidateService.setTopic(service.getTopic());

        context.sendResponse(Response.buildResponse(context.getCommand(),
                candidateService, 0, "success"));
    }

    @Override
    protected Class<ServiceQuery> getParamsClass() {
        return ServiceQuery.class;
    }

    @Override
    protected int getBiz() {
        return RegistrationConstants.BIZ_TYPE;
    }

    @Override
    protected int getCmd() {
        return RegistrationConstants.CMD_GET_SERVICE_BY_ID;
    }
}
