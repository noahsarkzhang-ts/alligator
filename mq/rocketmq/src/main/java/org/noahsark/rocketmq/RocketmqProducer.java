package org.noahsark.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.noahsark.mq.Producer;
import org.noahsark.mq.exception.MQOprationException;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.RpcCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * RocketMQ 生产者
 *
 * @author zhangxt
 * @date 2021/5/1
 */
public class RocketmqProducer implements Producer<RocketmqMessage, RocketmqSendResult> {

    private static Logger logger = LoggerFactory.getLogger(RocketmqProducer.class);

    private static final String GROUP_NAME = UUID.randomUUID().toString();

    private DefaultMQProducer producer;

    public RocketmqProducer(String namesrvAddr) {
        try {
            producer = new DefaultMQProducer(GROUP_NAME);

            producer.setNamesrvAddr(namesrvAddr);
            producer.setVipChannelEnabled(false);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    public RocketmqProducer(String groupName, String namesrvAddr) {
        try {
            producer = new DefaultMQProducer(groupName);
            producer.setVipChannelEnabled(false);
            producer.setNamesrvAddr(namesrvAddr);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    @Override
    public void send(RocketmqMessage msg, org.noahsark.mq.SendCallback sendCallback, long timeout) {

        Message message = new Message(msg.getTopic(), msg.getTag(),
                msg.getKey(), msg.getContent());

        try {
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    RocketmqSendResult rocketmqSendResult = new RocketmqSendResult();
                    rocketmqSendResult.setMsgId(sendResult.getMsgId());

                    if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                        rocketmqSendResult.setSuccess(true);
                    } else {
                        rocketmqSendResult.setSuccess(false);
                    }

                    sendCallback.onSuccess(rocketmqSendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    sendCallback.onException(throwable);
                }
            }, timeout);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }

    }

    @Override
    public RocketmqMessage buildMessage(RpcCommand command) {
        RocketmqMessage msg = new RocketmqMessage();
        RocketmqTopic topic = (RocketmqTopic) command.getAttachment();

        msg.setTopic(topic.getTopic());
        msg.setTag(topic.getTag());
        msg.setKey(topic.getKey());

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
    public RocketmqSendResult send(RocketmqMessage msg) {

        RocketmqSendResult rocketmqSendResult = new RocketmqSendResult();
        rocketmqSendResult.setSuccess(false);

        try {

            Message message = new Message(msg.getTopic(), msg.getTag(),
                    msg.getKey(), msg.getContent());

            SendResult sendResult = producer.send(message);

            rocketmqSendResult.setMsgId(sendResult.getMsgId());

            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                rocketmqSendResult.setSuccess(true);
            } else {
                rocketmqSendResult.setSuccess(false);
            }
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }

        return rocketmqSendResult;
    }

    @Override
    public void sendOneway(RocketmqMessage msg) {
        Message message = new Message(msg.getTopic(), msg.getTag(),
                msg.getContent());

        try {
            producer.sendOneway(message);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    @Override
    public void shutdown() {
        if (producer != null) {
            producer.shutdown();
        }

    }

    @Override
    public void start() {
        if (producer != null) {
            try {
                producer.start();

                logger.info("rocketmq producer is started: {}", this.producer.getProducerGroup());
            } catch (Exception ex) {
                logger.error("catch an excepion.", ex);
            }
        }
    }
}
