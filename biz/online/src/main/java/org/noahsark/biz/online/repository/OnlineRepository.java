package org.noahsark.biz.online.repository;

import org.noahsark.common.dto.UserInfo;
import org.noahsark.common.event.ServiceEvent;
import org.noahsark.common.event.UserEvent;

import java.util.Set;

/**
 * 在线用户数据存储访问类
 * @author zhangxt
 * @date 2021/6/27 15:11
 **/
public interface OnlineRepository {

    /**
     * 根据用户登陆事件设置用户在线状态
     * @param event 用户事件
     */
    void userLogin(UserEvent event);

    /**
     * 根据用户事件设置用户离线状态
     * @param event 用户事件
     */
    void userLogout(UserEvent event);

    void serviceRegistor(ServiceEvent event);

    void serviceUnRegistor(ServiceEvent event);

    Set<UserInfo> getAllUser();

    String getResidedService(String userId);

}
