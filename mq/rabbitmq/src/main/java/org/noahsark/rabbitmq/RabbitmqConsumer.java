package org.noahsark.rabbitmq;

import org.noahsark.mq.Consumer;
import org.noahsark.rabbitmq.pool.RabbitmqChannelPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqConsumer implements Consumer<RabbitmqMessageListener, RabbitmqTopic> {

    private RabbitmqMessageListener listener;

    private List<RabbitmqTopic> topics;

    private RabbitmqConnection connection;

    private RabbitmqChannelPool pool;

    @Override
    public void registerMessageListener(RabbitmqMessageListener listener) {
        this.listener = listener;
        this.topics = new ArrayList<>();
    }

    @Override
    public void subscribe(RabbitmqTopic topic) {
        this.topics.add(topic);
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }
}
