package org.noahsark.mq;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.session.ChannelHolder;

/**
 * MQ代理抽象类
 *
 * @author zhangxt
 * @date 22021/5/3
 */
public abstract class AbstractMqProxy implements MqProxy {

    protected PromisHolder promiseHolder;

    protected ChannelHolder channelHolder;

    protected abstract void initHolder();

    @Override
    public RpcPromise sendAsync(Topic topic, Request request,
                                CommandCallback commandCallback,
                                int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);
        promise.invoke(this.promiseHolder, request, commandCallback, timeoutMillis);

        return promise;
    }

    @Override
    public Object sendSync(Topic topic, Request request, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();

        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);
        Object result = promise.invokeSync(this.promiseHolder, request, timeoutMillis);

        return result;
    }

    @Override
    public PromisHolder getPromiseHolder() {
        return this.promiseHolder;
    }

    @Override
    public ChannelHolder getChannelHolder() {
        return this.channelHolder;
    }
}
