package org.noahsark.gw.processor.user;

import org.noahsark.gw.manager.UserEventEmitter;
import org.noahsark.gw.user.UserManager;
import org.noahsark.gw.user.UserSubject;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 用户登陆处理器
 *
 * @author zhangxt
 * @date 2021/3/13
 */
@Component
public class UserLoginProcessor extends AbstractProcessor<UserLoginInfo> {

    private Logger logger = LoggerFactory.getLogger(UserLoginProcessor.class);

    @Autowired
    private UserEventEmitter userEventEmitter;

    @Override
    protected void execute(UserLoginInfo request, RpcContext context) {

        logger.info("receive a login request:{}", request);
        /*RegistrationClient regClient = ServerContext.regClient;*/

        /*User user = new User();
        user.setUserId(request.getUserId());
        user.setLoginTime(System.currentTimeMillis());*/

        /*regClient.loginAsync(user, new CommandCallback() {
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
        });*/

        // 将用户添加到会话中
        UserSubject subject = new UserSubject();
        subject.setUserId(request.getUserId());

        // TODO 认证
        String token = UUID.randomUUID().toString();

        subject.setToken(token);
        subject.setName(request.getUserName());
        subject.setLoginTime(System.currentTimeMillis());

        // 将用户添加到会话中
        Session session = (Session) context.getSession();
        session.setSubject(subject);
        UserManager.getInstance().putSession(request.getUserId(), session);

        UserLoginResult userLoginResult = new UserLoginResult();
        userLoginResult.setToken(token);

        // 返回结果
        context.sendResponse(Response.buildResponse(context.getCommand(),
                userLoginResult, 0, "success"));

        // 广播用户上线事件
        userEventEmitter.eimit(subject, true);

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
