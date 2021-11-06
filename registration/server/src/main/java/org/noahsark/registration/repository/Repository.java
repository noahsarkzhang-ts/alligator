package org.noahsark.registration.repository;

import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;

import java.util.List;

/**
 * 注册中心数据库访问接口
 * @author zhangxt
 * @date 2021/4/10 20:38
 **/
public interface Repository {

    void registerService(Service service);

    void unRegisterService(String serviceId);

    List<Service> getServicesByBiz(int biz);

    Service getServiceById(String id);

    List<String> getExpireServices(int timeoutMillis);

    void updateService(Service service);

}
