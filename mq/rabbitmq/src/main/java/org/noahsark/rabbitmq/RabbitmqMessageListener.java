package org.noahsark.rabbitmq;

import org.noahsark.mq.MessageListener;

import java.util.List;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqMessageListener implements MessageListener<RabbitmqMessage> {

    @Override
    public boolean consumeMessage(List<RabbitmqMessage> messageList) {
        return false;
    }
}
