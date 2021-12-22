package org.noahsark.registration.processor;

import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.ServicePing;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 处理服务器心跳(上报负载)信息
 *
 * @author zhangxt
 * @date 2021/4/11
 */
@Component("pingProcessor")
public class PingProcessor extends AbstractProcessor<ServicePing> {

    private static Logger logger = LoggerFactory.getLogger(PingProcessor.class);

    @Autowired
    private Repository repository;

    @Override
    protected void execute(ServicePing request, RpcContext context) {

        logger.info("receive service ping: {}", request);

        Service service = (Service) context.getSession().getSubject();

        service.setLoad(request.getLoad());
        service.setLastPingTime(System.currentTimeMillis());

        repository.updateService(service);

        context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                0, "success"));
    }

    @Override
    protected Class<ServicePing> getParamsClass() {
        return ServicePing.class;
    }

    @Override
    protected int getBiz() {
        return 1;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}
