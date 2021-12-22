package org.noahsark.gw.ping;

import org.noahsark.client.heartbeat.PingPayloadGenerator;
import org.noahsark.gw.context.GlobalStatus;
import org.noahsark.registration.domain.ServicePing;

/**
 * Ping 消息体对象构造器
 *
 * @author zhangxt
 * @date 2021/4/21
 */
public class PersistentPingPayloadGenerator implements PingPayloadGenerator {

    @Override
    public Object getPayload() {

        ServicePing ping = new ServicePing();
        ping.setLoad(GlobalStatus.getInstance().get());

        return ping;
    }
}
