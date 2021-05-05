package org.noahsark.rocketmq;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Request;

/**
 * Created by hadoop on 2021/5/4.
 */
public class RocketmqPromiseHolder implements PromisHolder {

    public static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private RocketmqProducer producer;

    private final ConcurrentHashMap<Integer, RpcPromise> futures = new ConcurrentHashMap<>(16);

    public RocketmqPromiseHolder() {}

    public RocketmqPromiseHolder(RocketmqProducer producer) {
        this.producer = producer;
    }

    @Override
    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    @Override
    public void registerPromise(Integer requestId, RpcPromise promise) {
        futures.put(requestId, promise);
    }

    @Override
    public RpcPromise removePromis(Integer requestId) {
        return this.futures.remove(requestId);
    }

    @Override
    public void removePromis(RpcPromise promise) {
        this.futures.remove(promise.getRequestId());
    }


    public static int nextId() {
        return NEXT_ID.getAndIncrement();
    }

    @Override
    public void write(Request request) {

        request.setRequestId(nextId());

        RocketmqMessage msg = new RocketmqMessage();
        RocketmqTopic topic = (RocketmqTopic) request.getAttachment();

        msg.setTopic(topic.getTopic());
        msg.setTag(topic.getTag());
        msg.setKey(topic.getKey());

        byte[] body = null;

        if (request instanceof MultiRequest) {
            body = MultiRequest.encode((MultiRequest) request);
        }

        msg.setContent(body);
        producer.send(msg);
    }

}
