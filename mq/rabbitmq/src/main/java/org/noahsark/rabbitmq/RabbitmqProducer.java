package org.noahsark.rabbitmq;

import org.noahsark.mq.Producer;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqProducer implements Producer<RabbitmqMessage,RabbitmqSendCallback,RabbitmqSendResult> {

    @Override
    public void send(RabbitmqMessage msg, RabbitmqSendCallback sendCallback, long timeout) {

    }

    @Override
    public RabbitmqSendResult send(RabbitmqMessage msg) {
        return null;
    }

    @Override
    public void sendOneway(RabbitmqMessage msg) {

    }

    @Override
    public void shutdown() {

    }
}
