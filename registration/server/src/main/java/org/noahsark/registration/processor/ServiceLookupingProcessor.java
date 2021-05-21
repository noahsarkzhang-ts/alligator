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

import java.util.List;

/**
 * Created by hadoop on 2021/4/11.
 */
@Component
public class ServiceLookupingProcessor extends AbstractProcessor<ServiceQuery> {

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceProcessor.class);

    @Autowired
    private Repository repository;

    @Override
    protected void execute(ServiceQuery request, RpcContext context) {
        logger.info("receive query service request: {}" , JsonUtils.toJson(request));

        List<Service> serviceList = repository.getServicesByBiz(request.getBiz());

        // TODO 暂时返回第一个服务
        CandidateService candidateService = new CandidateService();
        Service service = serviceList.get(0);
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
        return RegistrationConstants.CMD_LOOKUP_BIZ;
    }
}
