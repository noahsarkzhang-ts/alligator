package org.noahsark.rocketmq;

import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.exception.InvokeExcption;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hadoop on 2021/5/4.
 */
public class RocketmqPromiseHolder implements PromisHolder {

    private static Logger logger = LoggerFactory.getLogger(RocketmqPromiseHolder.class);

    private AtomicInteger nextId = new AtomicInteger(1);

    private RocketmqProducer producer;

    private final ConcurrentHashMap<Integer, RpcPromise> futures = new ConcurrentHashMap<>(16);

    public RocketmqPromiseHolder() {
    }

    public RocketmqPromiseHolder(RocketmqProducer producer) {
        this.producer = producer;
    }

    @Override
    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    @Override
    public void registerPromise(Integer requestId, RpcPromise promise) {
        logger.info("register promis:{}:{}", requestId, promise.getRequestId());
        futures.put(requestId, promise);
    }

    @Override
    public RpcPromise removePromis(Integer requestId) {
        logger.info("remove promis:{}", requestId);

        return this.futures.remove(requestId);
    }

    @Override
    public void removePromis(RpcPromise promise) {
        logger.info("remove promis:{}", promise.getRequestId());

        this.futures.remove(promise.getRequestId());
    }

    @Override
    public int nextId() {
        return nextId.getAndIncrement();
    }

    @Override
    public void write(RpcCommand command) {

        RocketmqMessage msg = buildRocketmqMessage(command);
        //producer.send(msg);

        producer.send(msg, new RocketmqSendCallback() {

            @Override
            public void onSuccess(RocketmqSendResult var1) {
                logger.info("send command successfully:{}", command.getRequestId());
            }

            @Override
            public void onException(Throwable var1) {

                RpcPromise promise = RocketmqPromiseHolder.this.removePromis(command.getRequestId());
                if (promise != null) {
                    promise.cancelTimeout();
                    promise.setFailure(new InvokeExcption());
                }
                logger.error("Invoke send failed. The requestid is {}",
                        command.getRequestId(), var1.getCause());

            }
        }, 3000);
    }

    private RocketmqMessage buildRocketmqMessage(RpcCommand command) {
        RocketmqMessage msg = new RocketmqMessage();
        RocketmqTopic topic = (RocketmqTopic) command.getAttachment();

        msg.setTopic(topic.getTopic());
        msg.setTag(topic.getTag());
        msg.setKey(topic.getKey());

        byte[] body = null;

        if (command instanceof MultiRequest) {
            body = MultiRequest.encode((MultiRequest) command);
        } else {
            body = RpcCommand.encode(command);
        }

        msg.setContent(body);
        return msg;
    }

}
