package org.noahsark.registration.processor;

import java.util.ArrayList;
import java.util.List;

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
 * 获取所有服务
 *
 * @author zhangxt
 * @date 2021/6/27
 */
@Component
public class GetAllServiceProcessor extends AbstractProcessor<ServiceQuery> {

    private static Logger logger = LoggerFactory.getLogger(GetAllServiceProcessor.class);

    @Autowired
    private Repository repository;

    @Override
    protected void execute(ServiceQuery request, RpcContext context) {

        logger.info("receive query service request: {}", JsonUtils.toJson(request));

        List<Service> serviceList = repository.getServicesByBiz(request.getBiz());

        List<CandidateService> candidateServices = new ArrayList<>();
        serviceList.stream().forEach(service -> {
            CandidateService candidateService = new CandidateService();

            candidateService.setAddress(service.getAddress());
            candidateService.setTopic(service.getTopic());

            candidateServices.add(candidateService);
        });


        context.sendResponse(Response.buildResponse(context.getCommand(),
                candidateServices, 0, "success"));

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
        return RegistrationConstants.CMD_GET_ALL_SERVICE_BIZ;
    }
}
