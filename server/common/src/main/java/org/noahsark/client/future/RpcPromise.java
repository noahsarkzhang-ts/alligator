package org.noahsark.client.future;

import io.netty.channel.ChannelFutureListener;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReflectionUtil;
import org.noahsark.enums.PromiseEnum;
import org.noahsark.exception.InvokeExcption;
import org.noahsark.exception.OperationNotSupported;
import org.noahsark.exception.TimeoutException;
import org.noahsark.server.remote.TimerHolder;
import org.noahsark.server.rpc.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/25
 */
public class RpcPromise extends DefaultPromise<Object> implements Comparable<RpcPromise>, StreamPromise<Object> {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(
            5);

    private AtomicInteger currentFanout = new AtomicInteger(0);

    private int fanout = 1;

    private volatile boolean isStop = true;

    private boolean isFailure = false;

    private PromiseEnum type = PromiseEnum.GENERAL;

    private BlockingQueue<Object> streams = new LinkedBlockingQueue<>();

    private List<GenericFutureListener<RpcPromise>> rpcListeners = new ArrayList<>();

    private long timestampMillis;

    private int requestId;

    private Timeout timeout;

    private List<Object> results;

    public RpcPromise() {
        super(EVENT_EXECUTOR);

        Instant instant = Instant.now();
        timestampMillis = instant.toEpochMilli();

    }

    public RpcPromise(PromiseEnum type) {
        this();

        this.type = type;
        if (PromiseEnum.STREAM.equals(type)) {
            isStop = false;

        }
    }

    @Override
    public Promise<Object> setSuccess(Object result) {

        if (PromiseEnum.GENERAL.equals(this.type)) {
            return super.setSuccess(result);
        } else if (PromiseEnum.MULTIPLE.equals(this.type)) {
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
        } else if (PromiseEnum.STREAM.equals(this.type)) {
            this.end(result);
        }

        return this;
    }

    @Override
    public Promise<Object> setFailure(Throwable cause) {

        if (PromiseEnum.GENERAL.equals(this.type)) {
            isFailure = true;
            return super.setFailure(cause);
        } else if (PromiseEnum.MULTIPLE.equals(this.type)) {

            log.error(String.format("RpcPromise catch an exception,requestId is : %d", this.requestId), cause);

            synchronized (this) {
                if (results == null) {
                    results = new ArrayList<>();
                }

                isFailure = true;
                return super.setSuccess(results);
            }
        } else if (PromiseEnum.STREAM.equals(this.type)) {
            isFailure = true;
            log.error(String.format("RpcPromise catch an exception,requestId is : %d", this.requestId), cause);

            this.end(null);
        }

        return this;
    }


    public void addCallback(PromisHolder promisHolder, Request request, CommandCallback callback) {

        GenericFutureListener<RpcPromise> listener = future -> {
            Object result;

            RpcPromise promise = future;

            try {

                switch (promise.type) {
                    case GENERAL:
                    case MULTIPLE: {
                        result = future.get();
                        callback.callback(result, currentFanout.get(), fanout);
                        break;
                    }
                    case STREAM: {
                        result = streams.poll();
                        while (result != null) {
                            callback.callback(result, currentFanout.get(), fanout);

                            result = streams.poll();
                        }

                        break;
                    }
                    default:
                        break;
                }
            } catch (Throwable ex) {
                callback.failure(ex, currentFanout.get(), fanout);
                log.warn("catch an ception.", ex);
            } finally {
                promise.cancelTimeout();
                promisHolder.removePromis(request.getRequestId());
            }
        };

        this.addListener(listener);

        if (this.rpcListeners != null) {
            this.rpcListeners.add(listener);
        }
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

        if (fanout > 1) {
            type = PromiseEnum.MULTIPLE;
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

                connection.getChannel().writeAndFlush(request)
                        .addListener((ChannelFutureListener) cf -> {
                            if (!cf.isSuccess()) {
                                RpcPromise promise = connection.getPromise(request.getRequestId());
                                if (promise != null) {
                                    promise.setFailure(new InvokeExcption());
                                }
                                log.error("Invoke send failed. The requestId is {} " + request
                                        .getRequestId(), cf.cause());
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

        return isFailure || isStop || this.currentFanout.get() >= fanout;
    }

    public PromiseEnum getType() {
        return type;
    }

    public void setType(PromiseEnum type) {
        this.type = type;

        if (this.type == PromiseEnum.STREAM) {
            isStop = false;

            // stream 模式取消超时，由用户主动关闭
            cancelTimeout();
        }
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public void setTimestampMillis(long timestampMillis) {
        this.timestampMillis = timestampMillis;
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
        return (int) (this.timestampMillis - o.getTimestampMillis());
    }

    @Override
    public StreamPromise<Object> flow(Object result) {

        if (!PromiseEnum.STREAM.equals(this.type)) {
            throw new OperationNotSupported();
        }

        // 1、加入队列
        if (result != null) {
            streams.offer(result);
        }

        // 2、调用回调
        safeExecute(EVENT_EXECUTOR, () -> notifyListeners());

        return this;
    }

    private void notifyListeners() {

        if (this.rpcListeners == null) {
            return;
        }

        for (GenericFutureListener<RpcPromise> listener : this.rpcListeners) {
            notifyListener(this, listener);
        }

    }

    @Override
    public StreamPromise<Object> end(Object result) {

        if (!PromiseEnum.STREAM.equals(this.type)) {
            throw new OperationNotSupported();
        }

        // 1、加入队列
        if (result != null) {
            streams.offer(result);
        }

        // 2、设置结束标志
        isStop = true;

        // 3、调用回调
        safeExecute(EVENT_EXECUTOR, new Runnable() {
            @Override
            public void run() {
                notifyListeners();
            }
        });

        return this;
    }

    private void notifyListener(Future future, GenericFutureListener listener) {
        try {
            listener.operationComplete(future);
        } catch (Throwable throwable) {
            if (log.isWarnEnabled()) {
                log.warn("An exception was thrown by " + throwable.getClass().getName() + ".operationComplete()", throwable);
            }
        }
    }

    private void safeExecute(EventExecutor executor, Runnable task) {
        try {
            executor.execute(task);
        } catch (Throwable t) {
            log.error("Failed to submit a listener notification task.", t);
        }
    }

    private Object exploreListeners() {
        final Class<?> rpcPromiseClass = (Class<?>) this.getClass();
        final RpcPromise holder = this;

        Object listeners = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                Object result = null;

                try {

                    Field listenersField = rpcPromiseClass.getDeclaredField("listeners");

                    if (PlatformDependent.javaVersion() >= 9 && PlatformDependent.hasUnsafe()) {
                        // Let us try to use sun.misc.Unsafe to replace the SelectionKeySet.
                        // This allows us to also do this in Java9+ without any extra flags.
                        long listenersFieldOffset = PlatformDependent.objectFieldOffset(listenersField);

                        if (listenersFieldOffset != -1) {

                            return PlatformDependent.getObject(holder, listenersFieldOffset);
                        }
                        // We could not retrieve the offset, lets try reflection as last-resort.
                    }

                    Throwable cause = ReflectionUtil.trySetAccessible(listenersField, true);
                    if (cause != null) {
                        return result;
                    }

                    result = listenersField.get(holder);

                    return result;
                } catch (Exception ex) {
                    log.error("explore listener fails.", ex);
                }

                return result;
            }
        });

        return listeners;
    }


}
