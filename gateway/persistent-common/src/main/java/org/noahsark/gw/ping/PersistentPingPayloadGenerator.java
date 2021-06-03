package org.noahsark.gw.ping;

import org.noahsark.client.heartbeat.PingPayloadGenerator;
import org.noahsark.gw.context.GlobalStatus;
import org.noahsark.registration.domain.ServicePing;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/21
 */
public class PersistentPingPayloadGenerator implements PingPayloadGenerator {

    @Override
    public Object getPayload() {

        ServicePing ping = new ServicePing();
        ping.setLoad(GlobalStatus.getInstance().get());

        return ping;
    }
}
