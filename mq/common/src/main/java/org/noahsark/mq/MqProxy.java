package org.noahsark.mq;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.session.ChannelHolder;

public interface MqProxy {

    PromisHolder getPromiseHolder();

    ChannelHolder getChannelHolder();

    RpcPromise sendAsync(Topic topic, Request request,CommandCallback commandCallback,int timeoutMillis);

    Object sendSync(Topic topic, Request request, int timeoutMillis);

    void sendOneway(Topic topic, Request request);

    void start();

    void shutdown();
}
