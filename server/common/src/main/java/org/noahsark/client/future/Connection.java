package org.noahsark.client.future;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/4/5.
 */
public class Connection {

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    private Channel channel;

    private final ConcurrentHashMap<Integer, RpcPromise> futures = new ConcurrentHashMap<>(16);

    public static final AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("connection");


    public Connection() {
    }

    public Connection(Channel channel) {
        this.channel = channel;

        this.channel.attr(CONNECTION).set(this);
    }

    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    public void registerPromise(Integer requestId, RpcPromise promise) {
        futures.put(requestId, promise);
    }

    public RpcPromise removePromis(Integer requestId) {
        return this.futures.remove(requestId);
    }

    public void removePromis(RpcPromise promise) {
        this.futures.remove(promise.getRequestId());
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;

        this.channel.attr(CONNECTION).set(this);
    }

    public void close() {
        if (channel != null) {
            this.channel.close();
        }
    }
}
