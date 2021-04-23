package org.noahsark.registration.repository;

import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;

import java.util.List;

/**
 * Created by hadoop on 2021/4/10.
 */
public interface Repository {

    void login(User user, Service service);

    void logout(String userId);

    void registerService(Service service);

    void unRegisterService(String serviceId);

    List<String> getExpireServices(int timeoutMillis);

    void updateService(Service service);

}
