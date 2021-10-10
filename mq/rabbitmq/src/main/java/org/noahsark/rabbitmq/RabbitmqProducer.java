package org.noahsark.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import org.noahsark.mq.Producer;
import org.noahsark.mq.SendCallback;
import org.noahsark.mq.exception.MQOprationException;
import org.noahsark.rabbitmq.pool.RabbitmqChannelPool;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqProducer implements Producer<RabbitmqMessage, RabbitmqSendResult> {

    private static Logger logger = LoggerFactory.getLogger(RabbitmqProducer.class);

    private RabbitmqConnection connection;

    private RabbitmqChannelPool pool;

    public RabbitmqProducer() {
    }

    public RabbitmqProducer(RabbitmqConnection connection, RabbitmqChannelPool pool) {
        this.connection = connection;
        this.pool = pool;
    }

    @Override
    public void send(RabbitmqMessage msg, SendCallback sendCallback, long timeout) {

        Channel channel = null;
        try {
            channel = pool.borrowObject();
            channel.confirmSelect();

            channel.queueDeclare(msg.getTopic().getQueueName(), true, false,
                    false, null);

            /**
             * 消息没有被路由，打印出信息
             */
            channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
                try {
                    // TODO 未对结果做处理，只是简单的打印日志
                    byte[] content = body;

                    logger.error("Unroutable Message in RabbitMQ:" + replyCode + ":" + replyCode);
                } catch (Exception ex) {
                    logger.warn("catch an exception:" + ex);
                }
            });

            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {

                sendCallback.onSuccess(new RabbitmqSendResult(true, deliveryTag));

                logger.info("Receive an ack : {}", deliveryTag);
            };

            ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
                sendCallback.onException(new MQOprationException());

                logger.info("Receive an nack : {}", deliveryTag);
            };

            channel.addConfirmListener(ackCallback, nackCallback);

            /**
             * 如果 mandatory 为 true,消息没有路由信息，消息返回给 producer,进行重发。
             */
            channel.basicPublish("", msg.getTopic().getQueueName(), true, MessageProperties.MINIMAL_PERSISTENT_BASIC,
                    msg.getContent());


        } catch (Exception ex) {
            logger.warn("catch an exception when sending mssage:", ex);
        } finally {
            if (null != channel) {
                try {
                    pool.returnObject(channel);
                } catch (Exception ex) {
                    logger.warn("catch an exception when return channel:", ex);
                }
            }
        }

    }

    @Override
    public RabbitmqMessage buildMessage(RpcCommand command) {

        RabbitmqMessage msg = new RabbitmqMessage();
        RabbitmqTopic topic = (RabbitmqTopic) command.getAttachment();

        msg.setTopic(topic);

        byte[] body;

        if (command instanceof MultiRequest) {
            body = MultiRequest.encode((MultiRequest) command);
        } else {
            body = RpcCommand.encode(command);
        }

        msg.setContent(body);
        return msg;
    }

    @Override
    public RabbitmqSendResult send(RabbitmqMessage msg) {

        Channel channel = null;

        RabbitmqSendResult result = new RabbitmqSendResult();
        result.setSuccess(false);
        try {
            channel = pool.borrowObject();
            channel.confirmSelect();

            channel.queueDeclare(msg.getTopic().getQueueName(), true, false,
                    false, null);

            channel.basicPublish("", msg.getTopic().getQueueName(), MessageProperties.MINIMAL_PERSISTENT_BASIC,
                    msg.getContent());

            result.setSuccess(channel.waitForConfirms());

            return result;

        } catch (Exception ex) {
            logger.warn("catch an exception when sending mssage:", ex);
        } finally {
            if (null != channel) {
                try {
                    pool.returnObject(channel);
                } catch (Exception ex) {
                    logger.warn("catch an exception when return channel:", ex);
                }
            }
        }

        return result;
    }


    @Override
    public void sendOneway(RabbitmqMessage msg) {
        this.send(msg, new RabbitmqSendCallback.DefaultRabbitmqSendCallback(), 0);
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (Exception ex) {
            logger.error("Catch an exception when closing connection! ", ex);
        }
    }
}
