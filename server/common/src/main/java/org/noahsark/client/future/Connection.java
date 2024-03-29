package org.noahsark.client.future;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接，是一个抽象概念，
 * 标示 TCP/WebSocket 中的一个 Channel
 * @author zhangxt
 * @date 2021/4/5.
 */
public class Connection implements PromisHolder {

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    private final ConcurrentHashMap<Integer, RpcPromise> futures = new ConcurrentHashMap<>(16);

    public static final AttributeKey<Connection> CONNECTION = AttributeKey.valueOf("connection");

    private AtomicInteger nextId = new AtomicInteger(1);

    private Channel channel;

    public Connection() {
    }

    public Connection(Channel channel) {
        this.channel = channel;

        this.channel.attr(CONNECTION).set(this);
    }

    @Override
    public int nextId() {
        return nextId.getAndIncrement();
    }


    @Override
    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    @Override
    public void registerPromise(Integer requestId, RpcPromise promise) {
        log.info("register promise: {}", requestId);

        futures.put(requestId, promise);
    }

    @Override
    public RpcPromise removePromis(Integer requestId) {
        log.info("remove an promise: {}", requestId);

        RpcPromise promise = futures.get(requestId);

        if (promise.isRemoving()) {
            this.futures.remove(requestId);

        }

        return promise;
    }

    @Override
    public void removePromis(RpcPromise promise) {
        log.info("remove promise: {}", promise.getRequestId());

        if (promise.isRemoving()) {
            this.futures.remove(promise.getRequestId());
        }
    }

    @Override
    public void write(RpcCommand command) {
        this.getChannel().writeAndFlush(command);
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
