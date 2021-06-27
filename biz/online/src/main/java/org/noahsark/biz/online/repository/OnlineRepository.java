package org.noahsark.biz.online.repository;

import org.noahsark.common.event.ServiceEvent;
import org.noahsark.common.event.UserEvent;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.domain.User;

/**
 * Created by hadoop on 2021/6/27.
 */
public interface OnlineRepository {

    void userLogin(UserEvent event);

    void userLogout(UserEvent event);

    void serviceRegistor(ServiceEvent event);

    void serviceUnRegistor(ServiceEvent event);

}
