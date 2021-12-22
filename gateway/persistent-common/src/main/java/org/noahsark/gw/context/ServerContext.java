package org.noahsark.gw.context;

import org.noahsark.mq.MqProxy;
import org.noahsark.registration.BizServiceCache;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.server.remote.AbstractRemotingServer;

/**
 *  服务器上下文
 * @author zhangxt
 * @date 2021/4/12
 */
public final class ServerContext {

    public static AbstractRemotingServer server;

    public static RegistrationClient regClient;

    public static MqProxy mqProxy;

    public static BizServiceCache bizServiceCache;

}
