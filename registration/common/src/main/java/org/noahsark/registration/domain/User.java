package org.noahsark.registration.domain;

/**
 * 用户信息
 *
 * @author zhangxt
 * @date 2021/4/8
 */
public class User {

    private String userId;

    private long loginTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
