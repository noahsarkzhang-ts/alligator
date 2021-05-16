package org.noahsark.gw.ws.user;

import org.noahsark.server.session.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/13
 */
public class UserManager {

    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    private UserManager() {
    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }

    public Session getSession(String userId) {
        return sessionMap.get(userId);
    }

    public void putSession(String userId, Session session) {
        sessionMap.put(userId, session);
    }
}