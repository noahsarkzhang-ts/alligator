package org.noahsark.registration;

import io.netty.util.CharsetUtil;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.Id;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.tcp.client.TcpClient;
import org.noahsark.server.util.JsonUtils;

import java.util.List;

/**
 * Created by hadoop on 2021/4/10.
 */
public class RegistrationClient extends TcpClient {

    public RegistrationClient(String host, int port) {
        super(host, port);
    }

    public RegistrationClient(String url) {
        super(url);
    }

    public RegistrationClient(List<String> urls) {
        super(urls);
    }

    public Result<Void> login(User user) {

        Result<Void> result = commonInvoke(user, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGIN);

        return result;
    }

    public RpcPromise loginAsync(User user, CommandCallback callback) {
        return commonInvokeAsync(user, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGIN, callback);
    }

    public Result<Void> logout(Id userId) {

        Result<Void> result = commonInvoke(userId, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGOUT);

        return result;
    }

    public RpcPromise logoutAsync(Id userId, CommandCallback callback) {
        return commonInvokeAsync(userId, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGOUT, callback);
    }

    public Result<Void> registerService(Service service) {
        Result<Void> result = commonInvoke(service, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_REGISTER_SERVICE);

        return result;
    }

    public RpcPromise registerServiceAsync(Service service, CommandCallback callback) {
        return commonInvokeAsync(service, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_REGISTER_SERVICE, callback);
    }

    public Result<Void> unRegisterService(Id serviceId) {
        Result<Void> result = commonInvoke(serviceId, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_UNREGISTER_SERVICE);

        return result;
    }

    public RpcPromise unRegisterService(Id serviceId, CommandCallback callback) {
        return commonInvokeAsync(serviceId, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_UNREGISTER_SERVICE, callback);
    }

    private RpcPromise commonInvokeAsync(Object payload, int biz, int cmd, CommandCallback commandCallback) {

        Request request = new Request.Builder()
                .biz(biz)
                .cmd(cmd)
                .payload(payload)
                .build();

        return invoke(request, commandCallback, 3000);
    }

    private Result<Void> commonInvoke(Object payload, int biz, int cmd) {
        Request request = new Request.Builder()
                .biz(biz)
                .cmd(cmd)
                .payload(payload)
                .build();

        Object object = invokeSync(request, 30000);
        Result<Void> result = null;

        if (object != null) {
            String json = new String((byte[]) object, CharsetUtil.UTF_8);

            result = JsonUtils.fromJsonObject(json, Void.class);
        }

        return result;
    }
}
