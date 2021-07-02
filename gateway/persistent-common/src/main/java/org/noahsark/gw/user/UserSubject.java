package org.noahsark.gw.user;

import org.noahsark.server.session.Subject;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/13
 */
public class UserSubject implements Subject {

    private String userId;

    private String name;

    private String token;

    private Long loginTime;

    @Override
    public String getId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }
}
