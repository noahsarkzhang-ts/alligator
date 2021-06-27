package org.noahsark.biz.online.processor.event.service;


import org.noahsark.common.event.ServiceEvent;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/6/27.
 */
@Component
public class ServiceEventProcessor extends AbstractProcessor<ServiceEvent> {

    private static Logger log = LoggerFactory.getLogger(ServiceEventProcessor.class);

    @Override
    protected void execute(ServiceEvent request, RpcContext context) {

    }

    @Override
    protected Class<ServiceEvent> getParamsClass() {
        return ServiceEvent.class;
    }

    @Override
    protected int getBiz() {
        return 4;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}
