package org.noahsark.gw.ws.processor.user;

import java.util.UUID;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.gw.ws.context.ServerContext;
import org.noahsark.gw.ws.event.listener.ClientConnectionListener;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.registration.domain.User;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
public class UserLoginProcessor extends AbstractProcessor<UserLoginInfo> {

    private Logger logger = LoggerFactory.getLogger(UserLoginProcessor.class);

    @Override
    protected void execute(UserLoginInfo request, RpcContext context) {

        // TODO 认证
        RegistrationClient regClient = ServerContext.regClient;

        User user = new User();
        user.setUserId("123");
        user.setLoginTime(System.currentTimeMillis());

        regClient.loginAsync(user, new CommandCallback() {
            @Override
            public void callback(Object result) {

                Result<Void> result1 = JsonUtils.fromCommonObject((byte[]) result);

                logger.info("user login: {}", result1);

                UserLoginResult userLoginResult = new UserLoginResult();
                userLoginResult.setToken(UUID.randomUUID().toString());

                context.sendResponse(Response.buildResponse(context.getCommand(),
                        userLoginResult, 0, "success"));

            }

            @Override
            public void failure(Throwable cause) {

            }
        });


    }

    @Override
    protected Class<UserLoginInfo> getParamsClass() {
        return UserLoginInfo.class;
    }

    @Override
    protected int getBiz() {
        return 1;
    }

    @Override
    protected int getCmd() {
        return 1000;
    }

}
