package org.noahsark.server.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Session {

    public static final String SESSION_KEY_NAME = "NOAHSARK_SESSION";

    public static final AttributeKey<String> SESSION_KEY = AttributeKey.newInstance(SESSION_KEY_NAME);

    private String sessionId;

    private UserInfo user;

    private Channel channel;

    private Date lastAccessTime;

    public Session(Channel channel) {
        this.sessionId = UUID.randomUUID().toString();
        this.lastAccessTime = new Date();

        this.channel = channel;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static Session getOrCreatedSession(Channel channel) {

        String sessionId = channel.attr(SESSION_KEY).get();
        Session session;

        if (sessionId == null) {
            session = new Session(channel);
            channel.attr(SESSION_KEY).set(session.getSessionId());
            SessionManager.getInstance().addSession(session.getSessionId(),session);
        } else {
            session = SessionManager.getInstance().getSession(sessionId);
        }

        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return getSessionId().equals(session.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId());
    }
}
