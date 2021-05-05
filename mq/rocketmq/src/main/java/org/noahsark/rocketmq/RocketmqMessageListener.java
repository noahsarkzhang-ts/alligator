package org.noahsark.rocketmq;

import java.util.List;
import org.noahsark.mq.MessageListener;

/**
 * Created by hadoop on 2021/5/2.
 */
public class RocketmqMessageListener implements MessageListener<RocketmqMessage> {

    @Override
    public boolean consumeMessage(List<RocketmqMessage> messageList) {
        return false;
    }
}
