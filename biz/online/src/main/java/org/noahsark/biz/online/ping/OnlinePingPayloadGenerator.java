package org.noahsark.biz.online.ping;

import org.noahsark.client.heartbeat.PingPayloadGenerator;
import org.noahsark.registration.domain.ServicePing;

/**
 * 在线服务负载(ping)构造器
 *
 * @author zhangxt
 * @date 2021/4/21
 */
public class OnlinePingPayloadGenerator implements PingPayloadGenerator {

    @Override
    public Object getPayload() {

        ServicePing ping = new ServicePing();
        ping.setLoad(0);

        return ping;
    }
}
