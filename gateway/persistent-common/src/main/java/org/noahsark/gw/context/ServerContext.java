package org.noahsark.gw.context;

import org.noahsark.mq.MqProxy;
import org.noahsark.registration.BizServiceCache;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.server.remote.AbstractRemotingServer;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
public final class ServerContext {

    public static AbstractRemotingServer server;

    public static RegistrationClient regClient;

    public static MqProxy mqProxy;

    public static BizServiceCache bizServiceCache;

}
