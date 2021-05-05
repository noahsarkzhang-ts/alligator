package org.noahsark.rocketmq;

import org.noahsark.client.future.PromisHolder;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.session.ChannelHolder;

/**
 * Created by hadoop on 2021/5/4.
 */
public class RocketmqChannelHolder implements ChannelHolder {

    private RocketmqProducer producer;

    private PromisHolder promisHolder;

    public RocketmqChannelHolder(){}

    public RocketmqChannelHolder(RocketmqProducer producer, PromisHolder promisHolder) {
        this.producer = producer;
        this.promisHolder = promisHolder;
    }

    @Override
    public void write(Object response) {
        RocketmqMessage msg = new RocketmqMessage();
        Response resp = (Response)response;

        RocketmqTopic topic = (RocketmqTopic) resp.getAttachment();

        msg.setTopic(topic.getTopic());
        msg.setTag(topic.getTag());
        msg.setKey(topic.getKey());

        byte[] body = RpcCommand.encode(resp);
        msg.setContent(body);

        producer.send(msg);

    }

    @Override
    public PromisHolder getPromisHolder() {
        return this.promisHolder;
    }
}
