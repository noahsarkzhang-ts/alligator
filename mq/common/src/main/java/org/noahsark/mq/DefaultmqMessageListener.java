package org.noahsark.mq;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.enums.PromiseEnum;
import org.noahsark.server.constant.RpcCommandType;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.rpc.RpcRequest;
import org.noahsark.server.session.ChannelHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultmqMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DefaultmqMessageListener.class);

    private MqProxy proxy;

    public DefaultmqMessageListener() {
    }

    public DefaultmqMessageListener(MqProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean consumeMessage(byte[] message) {

        try {
            ByteBuf buf = Unpooled.wrappedBuffer(message);

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

            if (command.getType() == RpcCommandType.REQUEST
                    || command.getType() == RpcCommandType.REQUEST_ONEWAY) { // 处理请求
                ChannelHolder channelHolder = proxy.getChannelHolder();

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
                    if (command.getType() == RpcCommandType.RESPONSE) {
                        promise.setSuccess(command.getPayload());
                    } else if (command.getType() == RpcCommandType.STREAM) {
                        promise.setType(PromiseEnum.STREAM);
                        if (command.getEnd() == (byte) 1) {
                            promise.end(command.getPayload());
                        } else {
                            promise.flow(command.getPayload());
                        }
                    }

                } else {
                    logger.warn("promis is null : {}", command.getRequestId());
                }
            }

            return true;
        } catch (Exception ex) {
            logger.error("Catch an exception in MessageListener.", ex);
        }

        return false;
    }
}
