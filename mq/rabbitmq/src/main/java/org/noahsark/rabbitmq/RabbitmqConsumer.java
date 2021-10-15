package org.noahsark.rabbitmq;

import com.rabbitmq.client.*;
import org.noahsark.mq.Consumer;
import org.noahsark.mq.MessageListener;
import org.noahsark.rabbitmq.pool.RabbitmqChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqConsumer implements Consumer<RabbitmqTopic> {

    private static Logger logger = LoggerFactory.getLogger(RabbitmqConsumer.class);

    private MessageListener listener;

    private List<RabbitmqTopic> topics;

    private RabbitmqConnection connection;

    private RabbitmqChannelPool pool;

    public RabbitmqConsumer() {
        this.topics = new ArrayList<>();
    }

    public RabbitmqConsumer(RabbitmqConnection connection, RabbitmqChannelPool pool) {
        this();

        this.connection = connection;
        this.pool = pool;
    }

    @Override
    public void registerMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void subscribe(RabbitmqTopic topic) {
        this.topics.add(topic);
    }

    @Override
    public void subscribe(List<RabbitmqTopic> topics) {
        this.topics = topics;
    }

    @Override
    public void start() {

        try {
            for (RabbitmqTopic topic : topics) {

                Channel channel = pool.borrowObject();

                channel.queueDeclare(topic.getQueueName(), true, false, false, null);

                channel.basicQos(topic.getQos());

                final com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {

                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                               byte[] body) throws IOException {
                        try {

                            listener.consumeMessage(body);

                            this.getChannel().basicAck(envelope.getDeliveryTag(), false);

                        } catch (Exception ex) {
                            logger.warn("catch an exception in consumer.", ex);

                            try {
                                this.getChannel().basicNack(envelope.getDeliveryTag(), false, false);
                            } catch (Exception cex) {
                                logger.warn("catch an exception in consumer.", ex);
                            }
                        }
                    }
                };

                channel.basicConsume(topic.getQueueName(), false, consumer);
            }

        } catch (Exception ex) {
            logger.error("Catch an exception when starting RabbitmqConsumer.",ex);
        }

    }

    @Override
    public void shutdown() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (IOException ex) {
            logger.error("Catch an exception when closing connection.",ex);
        }
    }
}
