package org.noahsark.server.future;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.session.UserInfo;
import org.noahsark.server.util.TypeUtils;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/25
 */
public class RpcPromise extends DefaultPromise<Object> {

    private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(5);

    public RpcPromise() {
        super(EVENT_EXECUTOR);
    }

    public void addCallback(CommandCallback callback) {
        this.addListener(new GenericFutureListener<Future<? super Object>>() {
            @Override
            public void operationComplete(Future<? super Object> future) throws Exception {
                callback.callback(future.get());
            }
        });
    }

    public static void main(String[] args) {
        /*System.out.println("class = " + TypeUtils.getFirstParameterizedType(new RpcPromise<UserInfo>(){}));*/
        
    }

}
