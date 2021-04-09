package org.noahsark.registration.bean;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/8
 */
public class User {

    private int userId;

    private long loginTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
