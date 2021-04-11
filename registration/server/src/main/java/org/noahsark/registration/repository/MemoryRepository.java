package org.noahsark.registration.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;

/**
 * Created by hadoop on 2021/4/10.
 */
public class MemoryRepository implements Repository {

    private Map<String, User> userMap = new HashMap<>();

    private Map<String, Service> serviceMap = new HashMap<>();

    private Map<String, String> user2Servie = new HashMap<>();

    private Map<String, List<String>> serviceUsers = new HashMap<>();

    private Map<Integer, List<String>> bizServices = new HashMap<>();

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

            serviceUsers.put(service.getId(),users);
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

        serviceMap.put(service.getId(),service);
        serviceUsers.put(service.getId(),new ArrayList<>());

        List<String> services = bizServices.get(service.getId());
        if (services != null) {
            services.remove(service.getId());
        }

        queue.add(service);

    }

    @Override
    public void unRegisterService(String serviceId) {
        Service service = serviceMap.get(serviceId);

        serviceMap.remove(serviceId);

        List<String> users = serviceUsers.get(serviceId);
        users.stream().forEach(userId -> {
            userMap.remove(userId);
            user2Servie.remove(userId);
        });

        serviceUsers.remove(serviceId);
        queue.remove(service);

    }
}
