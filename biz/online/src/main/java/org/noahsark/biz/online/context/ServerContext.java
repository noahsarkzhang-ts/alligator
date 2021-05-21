package org.noahsark.biz.online.context;

import org.noahsark.registration.RegistrationClient;
import org.noahsark.registration.UserServiceCache;
import org.noahsark.rocketmq.RocketmqProxy;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
public final class ServerContext {

    public static RocketmqProxy mqProxy;

    public static RegistrationClient regClient;

    public static UserServiceCache userServiceCache;

}
