package org.noahsark.registration.processor;

import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.ServicePing;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.RpcContext;

/**
 * Created by hadoop on 2021/4/11.
 */
public class PingProcessor extends AbstractProcessor<ServicePing> {

    @Override
    protected void execute(ServicePing request, RpcContext context) {

    }

    @Override
    protected Class<ServicePing> getParamsClass() {
        return ServicePing.class;
    }

    @Override
    protected int getBiz() {
        return 0;
    }

    @Override
    protected int getCmd() {
        return 0;
    }
}
