package org.noahsark.mq;

import org.noahsark.client.future.PromisHolder;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.session.ChannelHolder;
import org.noahsark.server.session.Subject;

/**
 * @author Admin
 */
public class MqChannelHolder implements ChannelHolder {

    private Producer producer;

    private PromisHolder promisHolder;

    public MqChannelHolder() {
    }

    public MqChannelHolder(Producer producer, PromisHolder promisHolder) {
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
