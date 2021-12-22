package org.noahsark.rabbitmq;

import org.noahsark.mq.SendCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RabbitMQ 发送回调
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public abstract class RabbitmqSendCallback implements SendCallback<RabbitmqSendResult> {

    public static class DefaultRabbitmqSendCallback extends RabbitmqSendCallback {

        private static Logger logger = LoggerFactory.getLogger(DefaultRabbitmqSendCallback.class);

        @Override
        public void onSuccess(RabbitmqSendResult var1) {
            logger.info("send message successfully: {}", var1.getMsgId());
        }

        @Override
        public void onException(Throwable var1) {
            logger.warn("send message fail.", var1);
        }
    }

}
