package org.noahsark.biz.online.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.noahsark.common.dto.UserInfo;
import org.noahsark.common.event.ServiceEvent;
import org.noahsark.common.event.UserEvent;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/6/27.
 */
@Component
public class MemoryOnlineRepository implements OnlineRepository {

    /**
     * 用户id --> UserInfo
     */
    private Map<String, UserInfo> userMap = new HashMap<>();

    /**
     * userId id --> service id
     */
    private Map<String, String> user2Servie = new HashMap<>();

    /**
     * service id --> inviter id list
     */
    private Map<String, Set<String>> serviceUsers = new HashMap<>();

    /**
     * 当前在线的所有用户
     */
    private Set<UserInfo> currentUsers = new HashSet<>();


    @Override
    public void userLogin(UserEvent event) {

        if (userMap.get(event.getUserId()) != null) {
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setName(event.getName());
        userInfo.setServiceId(event.getServiceId());
        userInfo.setUserId(event.getUserId());
        userInfo.setState((byte) 1);

        // 1、添加用户查询Map
        userMap.put(userInfo.getUserId(),userInfo);

        // 2、添加到当前用户列表
        currentUsers.add(userInfo);

        // 3、添加用户-->服务映射关系
        user2Servie.put(userInfo.getUserId(),userInfo.getServiceId());

        // 4、添加服务-用户列表
        Set<String> users = serviceUsers.get(userInfo.getServiceId());
        if (users != null) {
            users.add(userInfo.getUserId());
        } else {
            users = new HashSet<>();
            users.add(userInfo.getUserId());

            serviceUsers.put(userInfo.getServiceId(),users);
        }


    }

    @Override
    public void userLogout(UserEvent event) {

        if (userMap.get(event.getUserId()) == null) {
            return;
        }

        UserInfo userInfo = userMap.get(event.getUserId());

        // 1、删除用户查询Map
        userMap.remove(event.getUserId());

        // 2、删除当前用户列表
        currentUsers.remove(userInfo);

        // 3、删除用户-->服务映射关系
        user2Servie.remove(userInfo.getUserId());

        // 4、删除服务-用户列表
        Set<String> users = serviceUsers.get(userInfo.getServiceId());
        if (users != null) {
            users.remove(event.getUserId());
        }

    }

    @Override
    public void serviceRegistor(ServiceEvent event) {

    }

    @Override
    public void serviceUnRegistor(ServiceEvent event) {

    }
}
