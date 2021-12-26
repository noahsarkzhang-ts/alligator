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
 * 代表一次 RPC 调用
 * 封装了 RPC 调用流程：
 * 1）异步操作，或异步转同步；
 * 2）支持消息的组播；
 * 3）支持消息的流处理；
 * 4）支持调用超时处理。
 *
 * @author zhangxt
 * @date 2021/3/25
 */
public class RpcPromise extends DefaultPromise<Object> implements Comparable<RpcPromise>, StreamPromise<Object> {

    private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

    /**
     * 处理Response的线程池
     */
    private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(5);

    /**
     * 记录当前收到的响应数
     */
    private AtomicInteger currentFanout = new AtomicInteger(0);

    /**
     * 请求的类型：GENERAL/MULTICAST/STREAM
     * request-response为GENERAL类型
     * 组播消息为MULTICAST类型
     * 流处理为STREAM类型
     */
    private PromiseEnum type = PromiseEnum.GENERAL;

    /**
     * 用于缓存收到的Response数据
     */
    private BlockingQueue<Object> streams = new LinkedBlockingQueue<>();

    /**
     * Rpc 回调处理列表
     */
    private List<GenericFutureListener<RpcPromise>> rpcListeners = new ArrayList<>();

    /**
     * 期望收到的 response 数量
     */
    private int fanout = 1;

    /**
     * 标示是否结束
     */
    private volatile boolean isStop = false;

    /**
     * 标示是否失败
     */
    private boolean isFailure = false;

    /**
     * 标示 time task 是否已经已经移除
     */
    private boolean isRemoved = false;

    /**
     * 时间戳
     */
    private long timestampMillis;

    /**
     * 请求的唯一id
     */
    private int requestId;

    /**
     * 定时任务，用于清除未及时响应的请求
     */
    private Timeout timeout;

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

        /**
         * GENERAL：正常调用,返回结果之后，Promise 不再允许调用；
         * MULTIPLE：将多次返回结果存放到缓存中，批量调用；
         * STREAM：执行 end 操作。
         */
        if (PromiseEnum.GENERAL.equals(this.type)) {
            this.currentFanout.incrementAndGet();

            return super.setSuccess(result);
        } else if (PromiseEnum.MULTIPLE.equals(this.type)) {
            if (this.currentFanout.get() > this.fanout) {
                return this;
            }

            // 1、 响应结果数加 1
            this.currentFanout.incrementAndGet();

            // 2、加入队列
            if (result != null) {
                streams.offer(result);
            }

            // 3、调用回调
            safeExecute(EVENT_EXECUTOR, () -> notifyListeners());
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

            if (!streams.isEmpty()) {
                // 1、调用回调
                safeExecute(EVENT_EXECUTOR, () -> notifyListeners());
            } else {
                isFailure = true;
                super.setFailure(cause);
            }

            return this;

        } else if (PromiseEnum.STREAM.equals(this.type)) {
            isFailure = true;
            log.error(String.format("RpcPromise catch an exception,requestId is : %d", this.requestId), cause);

            this.end(null);
        }

        return this;
    }

    /**
     * 添加异步回调函数
     *
     * @param promisHolder Promise管理类
     * @param request      请求
     * @param callback     回调
     */
    public void addCallback(PromisHolder promisHolder, Request request, CommandCallback callback) {

        GenericFutureListener<RpcPromise> listener = future -> {
            Object result;

            RpcPromise promise = future;

            try {

                switch (promise.type) {
                    case GENERAL: {
                        result = future.get();
                        callback.callback(result, currentFanout.get(), fanout);
                        break;
                    }
                    case MULTIPLE:
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

    /**
     * 同步方法调用
     *
     * @param promisHolder  Promise管理类
     * @param request       请求
     * @param timeoutMillis 超时时间
     * @return 结果
     */
    public Object invokeSync(PromisHolder promisHolder, Request request, int timeoutMillis) {

        this.invoke(promisHolder, request, null, timeoutMillis);

        try {
            return this.get();

        } catch (Exception ex) {
            log.warn("catch an exception. ", ex);
        }
        return null;
    }

    /**
     * 同步方法调用
     *
     * @param promisHolder    Promise管理类
     * @param request         请求
     * @param commandCallback 回调函数
     * @param timeoutMillis   超时时间
     */
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

    /**
     * 判断 Promis 是否可以被移除，以下条件返回true:
     * 1）已经调用 setFailure;
     * 2) 是否已经结束（流模式下）;
     * 3）所有结果已经返回
     *
     * @return 是否
     */
    public synchronized boolean isRemoving() {

        if (isRemoved) {
            return false;
        }

        isRemoved = isFailure || isStop || this.currentFanout.get() >= fanout;

        return isRemoved;

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

                return null;
            }
        });

        return listeners;
    }


}
