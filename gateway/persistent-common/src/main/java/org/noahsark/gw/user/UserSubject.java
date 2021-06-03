package org.noahsark.gw.user;

import org.noahsark.server.session.Subject;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/13
 */
public class UserSubject implements Subject {

    private String userId;

    @Override
    public String getId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
