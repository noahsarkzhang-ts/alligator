package org.noahsark.gw.user;

import java.util.HashSet;
import java.util.Set;
import org.noahsark.server.session.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户状态管理
 * @author zhangxt
 * @date 2021/5/13
 */
public class UserManager {

    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private Map<String, UserSubject> userMap = new ConcurrentHashMap<>();

    private Set<String> userIds = new HashSet<>();

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

    public UserSubject getUser(String userId) {
        return userMap.get(userId);
    }

    public void putSession(String userId, Session session) {
        sessionMap.put(userId, session);
        userMap.put(userId, (UserSubject)session.getSubject());
        userIds.add(userId);
    }

    public void removeSession(String userId) {
        sessionMap.remove(userId);
        userMap.remove(userId);
        userIds.remove(userId);
    }

    public Set<String> getOnlineUsers() {
        return userIds;
    }
}
