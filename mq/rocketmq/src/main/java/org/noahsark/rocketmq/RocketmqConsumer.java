package org.noahsark.rocketmq;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.mq.Consumer;
import org.noahsark.mq.exception.MQOprationException;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.rpc.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hadoop on 2021/5/1.
 */
public class RocketmqConsumer implements Consumer<RocketmqMessageListener, RocketmqTopic> {

    private static Logger logger = LoggerFactory.getLogger(RocketmqConsumer.class);

    private static final String GROUP_NAME = UUID.randomUUID().toString();

    private List<RocketmqTopic> topics = new ArrayList<>();

    private RocketmqMessageListener listener;

    private DefaultMQPushConsumer consumer;

    private RocketmqProxy proxy;

    // 120.79.235.83:9876

    public RocketmqConsumer(String namesrvAddr) {
        consumer = new DefaultMQPushConsumer(GROUP_NAME);
        try {
            consumer.setNamesrvAddr(namesrvAddr);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    public RocketmqConsumer(String groupName, String namesrvAddr) {
        try {
            consumer = new DefaultMQPushConsumer(groupName);
            consumer.setNamesrvAddr(namesrvAddr);
        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }

    }

    @Override
    public void registerMessageListener(RocketmqMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void subscribe(RocketmqTopic topic) {
        topics.add(topic);
    }

    @Override
    public void start() {

        try {
            topics.stream().forEach(topic -> {
                try {
                    System.out.println("topic:" + topic.getTopic());
                    consumer.subscribe(topic.getTopic(), topic.getTag());
                } catch (Exception ex) {
                    logger.error("catch an excepion.", ex);
                }
            });

            consumer.registerMessageListener(
                    (MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {

                        list.stream().forEach(messageExt -> {
                            try {
                                byte[] body = messageExt.getBody();

                                ByteBuf buf = Unpooled.wrappedBuffer(body);

                                buf.markReaderIndex();
                                short headSize = buf.readShort();
                                buf.resetReaderIndex();

                                RpcCommand command = null;

                                if (headSize == RpcCommand.RPC_COMMAND_SIZE) {
                                    command = RpcCommand.decode(buf);
                                } else {
                                    command = MultiRequest.decode(buf);
                                }

                                logger.info("receive a command: {}", command);

                                if (!(command.getType() == RpcCommandType.RESPONSE)) { // 处理请求
                                    RocketmqChannelHolder channelHolder = proxy.getChannelHolder();

                                    RpcContext rpcContext = new RpcContext.Builder()
                                            .command(command)
                                            .session(channelHolder)
                                            .build();

                                    RpcRequest rpcRequest = new RpcRequest.Builder()
                                            .request(command)
                                            .context(rpcContext)
                                            .build();

                                    String processName = command.getBiz() + ":" + command.getCmd();
                                    logger.info("processName: {}", processName);

                                    AbstractProcessor processor = Dispatcher.getInstance()
                                            .getProcessor(processName);

                                    if (processor != null) {
                                        processor.process(rpcRequest);
                                    } else {
                                        // 使用默认的处理器
                                        processName = -1 + ":" + -1;
                                        processor = Dispatcher.getInstance().getProcessor(processName);

                                        if (processor != null) {
                                            processor.process(rpcRequest);
                                        } else {
                                            logger.warn("No processor: {}", processName);
                                        }
                                    }
                                } else { // 处理响应
                                    RpcPromise promise = proxy.getPromiseHolder()
                                            .getPromise(command.getRequestId());

                                    if (promise != null) {
                                        promise.setSuccess(command.getPayload());

                                    } else {
                                        logger.warn("promis is null : {}", command.getRequestId());
                                    }
                                }

                            } catch (Exception ex) {
                                logger.error("catch an exception.", ex);
                            }
                        });

                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    });

            consumer.start();

        } catch (Exception ex) {
            logger.error("catch an excepion.", ex);
            throw new MQOprationException(ex);
        }
    }

    @Override
    public void shutdown() {

        if (consumer != null) {
            consumer.shutdown();
        }
    }

    public RocketmqProxy getProxy() {
        return proxy;
    }

    public void setProxy(RocketmqProxy proxy) {
        this.proxy = proxy;
    }
}
