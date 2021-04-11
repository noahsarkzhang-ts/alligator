package org.noahsark.registration;

import java.util.List;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.tcp.client.TcpClient;

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
        return null;
    }

    public Result<Void> logout(User user) {
        return null;
    }

    public Result<Void> registerService(Service service) {
        return null;
    }

    public Result<Void> unRegisterService(Service service) {
        return null;
    }
}
