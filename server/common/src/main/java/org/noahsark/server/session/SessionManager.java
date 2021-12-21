package org.noahsark.server.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.noahsark.server.event.ClientDisconnectEvent;
import org.noahsark.server.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话管理
 * @author zhangxt
 * @date 2021/3/13
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

    public synchronized void disconnect(ChannelHandlerContext ctx) {

        String sessionId = ctx.channel().attr(Session.SESSION_KEY).get();
        if (!StringUtil.isNullOrEmpty(sessionId)) {
            Session session = getSession(sessionId);
            Subject subject = session.getSubject();
            EventBus.getInstance().post(new ClientDisconnectEvent(subject));

            ctx.channel().attr(Session.SESSION_KEY).set("");
            remove(sessionId);

            ctx.disconnect();
        }
    }

}
