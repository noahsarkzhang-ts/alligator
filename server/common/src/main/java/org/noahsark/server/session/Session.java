package org.noahsark.server.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.Connection;
import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.RpcCommand;

/**
 * 长连接会话
 *
 * @author zhangxt
 * @date 2021/3/13
 */
public class Session implements ChannelHolder {

    public static final String SESSION_KEY_NAME = "NOAHSARK_SESSION";

    public static final AttributeKey<String> SESSION_KEY = AttributeKey
            .newInstance(SESSION_KEY_NAME);

    private String sessionId;

    private Subject subject;

    private Connection connection;

    private Date lastAccessTime;

    public Session(Connection connection) {
        this.sessionId = UUID.randomUUID().toString();
        this.lastAccessTime = new Date();

        this.connection = connection;
    }

    @Override
    public void write(RpcCommand repsponse) {
        this.connection.getChannel().writeAndFlush(repsponse);
    }

    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis) {

        request.setRequestId(this.connection.nextId());
        RpcPromise promise = new RpcPromise();

        promise.invoke(this.connection, request, commandCallback, timeoutMillis);

        return promise;
    }

    public Object invokeSyc(Request request, int timeoutMillis) {

        request.setRequestId(this.connection.nextId());
        RpcPromise promise = new RpcPromise();

        Object result = promise.invokeSync(this.connection, request, timeoutMillis);

        return result;
    }

    public void invokeOneway(Request request) {
        this.connection.getChannel().writeAndFlush(request);
    }

    @Override
    public PromisHolder getPromisHolder() {
        return connection;
    }

    public static Session getOrCreatedSession(Channel channel) {

        String sessionId = channel.attr(SESSION_KEY).get();
        Session session;

        if (sessionId == null) {
            Connection connection = new Connection(channel);

            session = new Session(connection);
            channel.attr(SESSION_KEY).set(session.getSessionId());
            SessionManager.getInstance().addSession(session.getSessionId(), session);
        } else {
            session = SessionManager.getInstance().getSession(sessionId);
        }

        return session;
    }

    public static Session getOrCreatedSession(Connection connection) {

        Channel channel = connection.getChannel();

        String sessionId = channel.attr(SESSION_KEY).get();
        Session session;

        if (sessionId == null) {
            session = new Session(connection);
            channel.attr(SESSION_KEY).set(session.getSessionId());
            SessionManager.getInstance().addSession(session.getSessionId(), session);
        } else {
            session = SessionManager.getInstance().getSession(sessionId);
        }

        return session;
    }

    @Override
    public Subject getSubject() {
        return subject;
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
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

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Channel getChannel() {
        return connection.getChannel();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        Session session = (Session) o;
        return getSessionId().equals(session.getSessionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId());
    }

}
