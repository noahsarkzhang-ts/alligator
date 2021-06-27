package org.noahsark.server.ws.server;

import org.junit.Test;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.util.JsonUtils;

import java.util.UUID;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class WebSocketServerTest {

    @Test
    public void testServer() {
        String host = "192.168.9.103";
        int port = 9090;

        // 请求
        // request = {"className":"inviter","method":"login","requestId":1,"version":"V1.0","payload":{"userName":"allan","password":"test"}}

        final WebSocketServer webSocketServer = new WebSocketServer(host, port);
        webSocketServer.init();

        UserLoginProcessor processor = new UserLoginProcessor();
        processor.register();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                webSocketServer.shutdown();
            }
        });

        webSocketServer.start();
    }

    public static class UserInfo {

        private String userId;

        private String userName;

        private String password;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public static class TokenInfo {
        private String userName;

        private String token;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private static class UserLoginProcessor extends AbstractProcessor<UserInfo> {

        @Override
        protected void execute(UserInfo request, RpcContext context) {

            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setUserName(request.getUserName());
            tokenInfo.setToken(UUID.randomUUID().toString());

            Result<TokenInfo> result = new Result.Builder<TokenInfo>()
                    .code(1)
                    .message("success")
                    .data(tokenInfo)
                    .build();

            Response response = new Response.Builder()
                    .requestId(context.getCommand().getRequestId())
                    .biz(context.getCommand().getBiz())
                    .cmd(context.getCommand().getCmd())
                    .payload(result)
                    .build();

            String text = JsonUtils.toJson(response);

            System.out.println("text = " + text);

            context.sendResponse(response);
        }

        @Override
        protected Class<UserInfo> getParamsClass() {
            return UserInfo.class;
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
}
