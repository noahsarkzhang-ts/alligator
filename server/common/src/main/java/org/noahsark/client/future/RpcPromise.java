package org.noahsark.client.future;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.Popup;
import org.noahsark.server.exception.InvokeExcption;
import org.noahsark.server.exception.TimeoutException;
import org.noahsark.server.remote.TimerHolder;
import org.noahsark.server.rpc.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/25
 */
public class RpcPromise extends DefaultPromise<Object> implements Comparable<RpcPromise> {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(
            5);

    private long timeStampMillis;

    private int requestId;

    private Timeout timeout;

    public RpcPromise() {
        super(EVENT_EXECUTOR);

        Instant instant = Instant.now();
        timeStampMillis = instant.toEpochMilli();

    }

    public void addCallback(PromisHolder promisHolder, Request request, CommandCallback callback) {
        this.addListener(future -> {

            Object result;

            RpcPromise promise = (RpcPromise) future;

            try {
                result = future.get();

                callback.callback(result);

            } catch (Throwable ex) {
                callback.failure(ex);
                log.warn("catch an ception.", ex);
            } finally {

                promise.cancelTimeout();
                promisHolder.removePromis(request.getRequestId());
            }
        });
    }

    public Object invokeSync(PromisHolder promisHolder, Request request, int timeoutMillis) {

        this.invoke(promisHolder, request, null, timeoutMillis);

        try {
            return this.get();

        } catch (Exception ex) {
            log.warn("catch an exception. ", ex);
        }
        return null;
    }


    public void invoke(PromisHolder promisHolder, Request request, CommandCallback commandCallback,
                       int timeoutMillis) {

        promisHolder.registerPromise(request.getRequestId(), this);
        if (commandCallback != null) {
            addCallback(promisHolder, request, commandCallback);
        }

        try {
            //add timeout
            if (timeoutMillis > 0) {
                Timeout timeout = TimerHolder.getTimer().newTimeout(new TimerTask() {
                    @Override
                    public void run(Timeout timeout) throws Exception {
                        RpcPromise promise = promisHolder.removePromis(request.getRequestId());

                        if (promise != null) {
                            promise.setFailure(new TimeoutException());
                        }
                    }

                }, timeoutMillis, TimeUnit.MILLISECONDS);
                setTimeout(timeout);
            }

            if (promisHolder instanceof Connection) {
                Connection connection = (Connection) promisHolder;

                connection.getChannel().writeAndFlush(request).addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture cf) throws Exception {
                        if (!cf.isSuccess()) {
                            RpcPromise promise = connection.removePromis(request.getRequestId());
                            if (promise != null) {
                                promise.cancelTimeout();
                                promise.setFailure(new InvokeExcption());
                            }
                            log.error("Invoke send failed. The requestid is {}",
                                    request.getRequestId(), cf.cause());
                        }
                    }

                });
            } else {
                promisHolder.write(request);
            }

        } catch (Exception ex) {
            RpcPromise promise = promisHolder.removePromis(request.getRequestId());
            if (promise != null) {
                promise.cancelTimeout();
                promise.setFailure(new InvokeExcption());
            }
            log.error("Exception caught when sending invocation. The requestId is {}",
                    request.getRequestId(), ex);
        }
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(long timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Timeout getTimeout() {
        return timeout;
    }

    public void setTimeout(Timeout timeout) {
        this.timeout = timeout;
    }

    public void cancelTimeout() {
        if (this.timeout != null) {
            this.timeout.cancel();
        }
    }

    public static void main(String[] args) {

    }

    @Override
    public int compareTo(RpcPromise o) {
        return (int) (this.timeStampMillis - o.getTimeStampMillis());
    }
}
