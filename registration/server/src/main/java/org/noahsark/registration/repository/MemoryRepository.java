package org.noahsark.registration.repository;

import org.noahsark.registration.domain.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

/**
 * 注册中心 内存 数据访问类
 * @author zhangxt
 * @date 2021/4/10 20:37
 **/
public class MemoryRepository implements Repository {

    private static Logger log = LoggerFactory.getLogger(MemoryRepository.class);

    /**
     * service id --> service
     */
    private Map<String, Service> serviceMap = new HashMap<>();

    /**
     * biz id --> service id list
     */
    private Map<Integer, List<String>> bizServices = new HashMap<>();

    /**
     * ordered service list
     */
    private PriorityQueue<Service> queue = new PriorityQueue<>();

    @Override
    public void registerService(Service service) {

        serviceMap.put(service.getId(), service);

        List<String> services = bizServices.get(service.getBiz());
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


        List<String> services = bizServices.get(service.getBiz());
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
    public Service getServiceById(String id) {
        return serviceMap.get(id);
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
