package org.noahsark.registration;

import io.netty.util.CharsetUtil;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.registration.constant.RegistrationConstants;
import org.noahsark.registration.domain.*;
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

        Result<Void> result = commonInvoke(user, Void.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGIN);

        return result;
    }

    public RpcPromise loginAsync(User user, CommandCallback callback) {
        return commonInvokeAsync(user, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGIN, callback);
    }

    public Result<Void> logout(Id userId) {

        Result<Void> result = commonInvoke(userId, Void.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGOUT);

        return result;
    }

    public RpcPromise logoutAsync(Id userId, CommandCallback callback) {
        return commonInvokeAsync(userId, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOGOUT, callback);
    }

    public Result<Void> registerService(Service service) {
        Result<Void> result = commonInvoke(service, Void.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_REGISTER_SERVICE);

        return result;
    }

    public RpcPromise registerServiceAsync(Service service, CommandCallback callback) {
        return commonInvokeAsync(service, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_REGISTER_SERVICE, callback);
    }

    public Result<Void> unRegisterService(Id serviceId) {
        Result<Void> result = commonInvoke(serviceId, Void.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_UNREGISTER_SERVICE);

        return result;
    }

    public RpcPromise unRegisterService(Id serviceId, CommandCallback callback) {
        return commonInvokeAsync(serviceId, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_UNREGISTER_SERVICE, callback);
    }

    public Result<CandidateService> serviceLookup(ServiceQuery query) {

        Result<CandidateService> result = commonInvoke(query, CandidateService.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOOKUP_BIZ);

        return result;
    }

    public RpcPromise serviceLookupAsync(ServiceQuery query, CommandCallback callback) {
        return commonInvokeAsync(query, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOOKUP_BIZ, callback);
    }

    public Result<List<CandidateService>> getAllService(ServiceQuery query) {

        Result<List<CandidateService>> result = commonArrayInvoke(query, CandidateService.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_GET_ALL_SERVICE_BIZ);

        return result;
    }

    public Result<CandidateService> userLookup(UserQuery query) {
        Result<CandidateService> result = commonInvoke(query, CandidateService.class, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOOKUP_USER);

        return result;
    }

    public RpcPromise userLookupAsync(UserQuery query, CommandCallback callback) {
        return commonInvokeAsync(query, RegistrationConstants.BIZ_TYPE, RegistrationConstants.CMD_LOOKUP_USER, callback);
    }

    private RpcPromise commonInvokeAsync(Object payload, int biz, int cmd, CommandCallback commandCallback) {

        Request request = new Request.Builder()
                .biz(biz)
                .cmd(cmd)
                .payload(payload)
                .build();

        return invoke(request, commandCallback, 3000);
    }

    private <T> Result<T> commonInvoke(Object payload, Class<T> type, int biz, int cmd) {
        Request request = new Request.Builder()
                .biz(biz)
                .cmd(cmd)
                .payload(payload)
                .build();

        Object object = invokeSync(request, 30000);
        Result<T> result = null;

        if (object != null) {
            String json = new String((byte[]) object, CharsetUtil.UTF_8);

            result = JsonUtils.fromJsonObject(json, type);
        }

        return result;
    }

    private <T> Result<List<T>> commonArrayInvoke(Object payload, Class<T> type, int biz, int cmd) {
        Request request = new Request.Builder()
                .biz(biz)
                .cmd(cmd)
                .payload(payload)
                .build();

        Object object = invokeSync(request, 30000);
        Result<List<T>> result = null;

        if (object != null) {
            String json = new String((byte[]) object, CharsetUtil.UTF_8);

            result = JsonUtils.fromJsonArray(json, type);
        }

        return result;
    }
}
