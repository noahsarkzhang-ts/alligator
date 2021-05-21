package org.noahsark.registration.domain;

import java.io.Serializable;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/18
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
