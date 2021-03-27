package org.noahsark.server.future;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import java.time.Instant;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/25
 */
public class RpcPromise extends DefaultPromise<Object> {

    private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(5);

    private long timeStampMillis;

    private int requestId;

    public RpcPromise() {
        super(EVENT_EXECUTOR);

        Instant instant = Instant.now();
        timeStampMillis = instant.toEpochMilli();
    }

    public void addCallback(CommandCallback callback) {
        this.addListener(new GenericFutureListener<Future<? super Object>>() {
            @Override
            public void operationComplete(Future<? super Object> future) throws Exception {
                callback.callback(future.get());
            }
        });
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

    public static void main(String[] args) {
        /*System.out.println("class = " + TypeUtils.getFirstParameterizedType(new RpcPromise<UserInfo>(){}));*/
        
    }

}
