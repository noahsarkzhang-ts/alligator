package org.noahsark.biz.online.ping;

import org.noahsark.client.heartbeat.PingPayloadGenerator;
import org.noahsark.registration.domain.ServicePing;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/21
 */
public class OnlinePingPayloadGenerator implements PingPayloadGenerator {

    @Override
    public Object getPayload() {

        ServicePing ping = new ServicePing();
        ping.setLoad(0);

        return ping;
    }
}
