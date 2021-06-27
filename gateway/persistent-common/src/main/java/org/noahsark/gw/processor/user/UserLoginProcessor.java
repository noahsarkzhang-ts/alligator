package org.noahsark.gw.processor.user;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.gw.user.UserManager;
import org.noahsark.gw.user.UserSubject;
import org.noahsark.registration.RegistrationClient;
import org.noahsark.registration.domain.User;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.session.Session;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
        user.setUserId(request.getUserId());
        user.setLoginTime(System.currentTimeMillis());

        regClient.loginAsync(user, new CommandCallback() {
            @Override
            public void callback(Object result, int currentFanout, int fanout) {

                Result<Void> result1 = JsonUtils.fromCommonObject((byte[]) result);

                logger.info("inviter login: {}", result1);

                UserLoginResult userLoginResult = new UserLoginResult();
                userLoginResult.setToken(UUID.randomUUID().toString());

                context.sendResponse(Response.buildResponse(context.getCommand(),
                    userLoginResult, 0, "success"));

            }

            @Override
            public void failure(Throwable cause, int currentFanout, int fanout) {

            }
        });

        // 将用户添加到会话中
        UserSubject subject = new UserSubject();
        subject.setUserId(request.getUserId());
        Session session = (Session) context.getSession();
        session.setSubject(subject);

        UserManager.getInstance().putSession(request.getUserId(), session);

    }

    @Override
    protected Class<UserLoginInfo> getParamsClass() {
        return UserLoginInfo.class;
    }

    @Override
    protected int getBiz() {
        return 2;
    }

    @Override
    protected int getCmd() {
        return 1;
    }

}
