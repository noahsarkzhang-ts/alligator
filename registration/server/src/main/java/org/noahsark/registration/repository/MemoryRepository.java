package org.noahsark.registration.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.noahsark.client.future.RpcPromise;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;
import org.noahsark.server.exception.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/4/10.
 */
public class MemoryRepository implements Repository {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    /**
     * 用户id --> user
     */
    private Map<String, User> userMap = new HashMap<>();

    /**
     * service id --> service
     */
    private Map<String, Service> serviceMap = new HashMap<>();

    /**
     * user id --> service id
     */
    private Map<String, String> user2Servie = new HashMap<>();

    /**
     * service id --> user id list
     */
    private Map<String, List<String>> serviceUsers = new HashMap<>();

    /**
     * biz id --> service id list
     */
    private Map<Integer, List<String>> bizServices = new HashMap<>();

    /**
     * ordered service list
     */
    private PriorityQueue<Service> queue = new PriorityQueue<>();

    @Override
    public void login(User user, Service service) {
        userMap.put(user.getUserId(), user);
        user2Servie.put(user.getUserId(), service.getId());

        List<String> users = serviceUsers.get(service.getId());
        if (users != null) {
            users.add(user.getUserId());
        } else {
            users = new ArrayList<>();
            users.add(user.getUserId());

            serviceUsers.put(service.getId(), users);
        }

    }

    @Override
    public void logout(String userId) {
        userMap.remove(userId);

        String serviceId = user2Servie.get(userId);

        user2Servie.remove(userId);

        List<String> users = serviceUsers.get(serviceId);
        if (users != null) {
            users.remove(userId);
        }
    }

    @Override
    public void registerService(Service service) {

        serviceMap.put(service.getId(), service);

        /*
         * 如果是网关服务，则需要构建服务用户信息记录
         */
        if (service.getBiz() >= 100 && service.getBiz() < 200) {
            serviceUsers.put(service.getId(), new ArrayList<>());
        }

        List<String> services = bizServices.get(service.getId());
        if (services != null) {
            services.add(service.getId());
        } else {
            services = new ArrayList<>();
            services.add(service.getId());

            bizServices.put(service.getBiz(), services);
        }
        queue.add(service);

    }

    @Override
    public void unRegisterService(String serviceId) {
        Service service;
        service = serviceMap.get(serviceId);

        serviceMap.remove(serviceId);

        /*
         * 如果是网关服务，则需要清理用户信息
         */
        if (service.getBiz() >= 100 && service.getBiz() < 200) {
            List<String> users = serviceUsers.get(serviceId);

            users.stream().forEach(userId -> {
                userMap.remove(userId);
                user2Servie.remove(userId);
            });

            serviceUsers.remove(serviceId);
        }

        List<String> services = bizServices.get(service.getId());
        if (services != null) {
            services.remove(service.getId());
        }

        queue.remove(service);

    }

    @Override
    public List<Service> getServicesByBiz(int biz) {

        List<Service> services = new ArrayList<>();
        List<String> serviceIds = bizServices.get(biz);

        serviceIds.stream().forEach(serviceId -> services.add(serviceMap.get(serviceId)));

        return services;
    }

    @Override
    public Service getServiceByUser(String userId) {

        return serviceMap.get(user2Servie.get(userId));
    }

    @Override
    public List<String> getExpireServices(int expireMillis) {

        List<String> services = new ArrayList<>();

        Instant instant = Instant.now();
        long currentMillis = instant.toEpochMilli();

        Service service = null;
        long timeStampMillis = 0L;
        long timeoutMillis = 0L;

        while (!queue.isEmpty()) {
            service = queue.peek();

            timeStampMillis = service.getLastPingTime();
            timeoutMillis = currentMillis - timeStampMillis;

            if (timeoutMillis >= expireMillis) {

                services.add(service.getId());
                queue.remove(service);

                log.warn("Service timeout: {},timeout: {}", service.getId(), timeoutMillis);
            } else {
                break;
            }
        }

        return services;
    }

    @Override
    public void updateService(Service service) {
        queue.remove(service);
        queue.add(service);

        serviceMap.put(service.getId(), service);
    }
}
