package org.noahsark.gw.ws.context;

import org.noahsark.registration.BizServiceCache;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.server.ws.server.WebSocketServer;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
public final class ServerContext {

    public static WebSocketServer server;

    public static RegistrationClient regClient;

    public static RocketmqProxy mqProxy;

    public static BizServiceCache bizServiceCache;

}
