package org.noahsark.biz.online.repository;

import org.noahsark.common.dto.UserInfo;
import org.noahsark.common.event.ServiceEvent;
import org.noahsark.common.event.UserEvent;

import java.util.Set;

/**
 * Created by hadoop on 2021/6/27.
 */
public interface OnlineRepository {

    void userLogin(UserEvent event);

    void userLogout(UserEvent event);

    void serviceRegistor(ServiceEvent event);

    void serviceUnRegistor(ServiceEvent event);

    Set<UserInfo> getAllUser();

}
