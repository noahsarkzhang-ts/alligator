package org.noahsark.registration.domain;

import java.io.Serializable;

/**
 * 用户查询
 *
 * @author zhangxt
 * @date 2021/4/8
 */
public class UserQuery implements Serializable {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
