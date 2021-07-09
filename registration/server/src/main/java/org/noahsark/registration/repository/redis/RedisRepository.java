package org.noahsark.registration.repository.redis;

import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.registration.repository.Repository;

import java.util.List;

/**
 * Created by hadoop on 2021/4/10.
 */
public class RedisRepository implements Repository {

    @Override
    public void login(User user, Service service) {

    }

    @Override
    public void logout(String userId) {

    }

    @Override
    public void registerService(Service service) {

    }

    @Override
    public void unRegisterService(String id) {

    }

    @Override
    public List<Service> getServicesByBiz(int biz) {
        return null;
    }

    @Override
    public Service getServiceByUser(String userId) {
        return null;
    }

    @Override
    public Service getServiceById(String serviceId) {
        return null;
    }

    @Override
    public List<String> getExpireServices(int timeoutMillis) {
        return null;
    }

    @Override
    public void updateService(Service service) {

    }
}
