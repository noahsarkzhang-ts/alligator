package org.noahsark.client.future;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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

    private int fanout = 1;

    private AtomicInteger currentFanout = new AtomicInteger(0);

    private boolean isFailure = false;

    private Timeout timeout;

    private List<Object> results;

    public RpcPromise() {
        super(EVENT_EXECUTOR);

        Instant instant = Instant.now();
        timeStampMillis = instant.toEpochMilli();


    }

    @Override
    public Promise<Object> setSuccess(Object result) {

        if (this.fanout == 1) {
            return super.setSuccess(result);
        }

        if (this.currentFanout.get() <= this.fanout) {
            synchronized (this) {

                this.currentFanout.incrementAndGet();

                if (results == null) {
                    results = new ArrayList<>();
                }

                this.results.add(result);

                if (this.currentFanout.get() == this.fanout) {
                    return super.setSuccess(results);
                }
            }
        }

        return this;
    }

    @Override
    public Promise<Object> setFailure(Throwable cause) {

        if (this.fanout == 1) {
            isFailure = true;
            return super.setFailure(cause);
        }

        synchronized (this) {
            if (results == null) {
                results = new ArrayList<>();
            }

            isFailure = true;
            return super.setSuccess(results);
        }

    }


    public void addCallback(PromisHolder promisHolder, Request request, CommandCallback callback) {
        this.addListener(future -> {

            Object result;

            RpcPromise promise = (RpcPromise) future;

            try {
                result = future.get();

                callback.callback(result, currentFanout.get(), fanout);

            } catch (Throwable ex) {
                callback.failure(ex, currentFanout.get(), fanout);
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

        this.requestId = request.getRequestId();
        this.fanout = request.getFanout();

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

                connection.getChannel().writeAndFlush(request)
                    .addListener(new ChannelFutureListener() {

                        @Override
                        public void operationComplete(ChannelFuture cf) throws Exception {
                            if (!cf.isSuccess()) {
                                RpcPromise promise = connection.getPromise(request.getRequestId());
                                if (promise != null) {
                                    promise.setFailure(new InvokeExcption());
                                }
                                log.error("Invoke send failed. The requestid is {} " + request
                                    .getRequestId(), cf.cause());
                            }
                        }

                    });
            } else {
                promisHolder.write(request);
            }

        } catch (Exception ex) {
            log.error("Exception caught when sending invocation. The requestId is " + request
                .getRequestId(), ex);

            RpcPromise promise = promisHolder.removePromis(request.getRequestId());
            if (promise != null) {
                promise.setFailure(new InvokeExcption());
            }
        }
    }

    public boolean isRemoving() {

        return isFailure || this.currentFanout.get() >= fanout;
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

            this.timeout = null;
        }
    }

    public static void main(String[] args) {

    }

    @Override
    public int compareTo(RpcPromise o) {
        return (int) (this.timeStampMillis - o.getTimeStampMillis());
    }
}
