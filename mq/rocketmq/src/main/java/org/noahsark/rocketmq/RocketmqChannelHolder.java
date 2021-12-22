package org.noahsark.rocketmq;

import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.session.ChannelHolder;
import org.noahsark.server.session.Subject;

/**
 * RocketMQ 通道容器
 *
 * @author zhangxt
 * @date 2021/5/4
 */
public class RocketmqChannelHolder implements ChannelHolder {

    private RocketmqProducer producer;

    private PromisHolder promisHolder;

    public RocketmqChannelHolder() {
    }

    public RocketmqChannelHolder(RocketmqProducer producer, PromisHolder promisHolder) {
        this.producer = producer;
        this.promisHolder = promisHolder;
    }

    @Override
    public void write(RpcCommand response) {

        this.promisHolder.write(response);

    }

    @Override
    public PromisHolder getPromisHolder() {
        return this.promisHolder;
    }

    @Override
    public Subject getSubject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubject(Subject subject) {
        throw new UnsupportedOperationException();
    }
}
