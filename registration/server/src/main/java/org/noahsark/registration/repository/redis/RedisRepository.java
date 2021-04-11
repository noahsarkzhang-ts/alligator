package org.noahsark.registration.repository.redis;

import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.registration.repository.Repository;

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
    public void unRegisterService(String serviceId) {

    }
}
