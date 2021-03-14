package org.noahsark.server.session;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2021/3/13.
 */
public class SessionManager {

  private Map<String, Session> sessions = new HashMap<>();

  private static class Holder {

    private static final SessionManager INSTANCE = new SessionManager();
  }

  public static SessionManager getInstance() {
    return Holder.INSTANCE;
  }

  public synchronized void addSession(String key, Session value) {
    this.sessions.put(key, value);
  }

  public synchronized Session getSession(String key) {
    return (Session) this.sessions.get(key);
  }

  public synchronized Session remove(String key) {
    return (Session) this.sessions.remove(key);
  }

  public static boolean validate(Session session) {
    return null != session;
  }

}
