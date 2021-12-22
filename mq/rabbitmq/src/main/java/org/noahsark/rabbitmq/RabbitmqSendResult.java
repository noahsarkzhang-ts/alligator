package org.noahsark.rabbitmq;

import org.noahsark.mq.SendResult;

import java.util.Objects;

/**
 * RabbitMQ 结果
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class RabbitmqSendResult implements SendResult {

    private boolean success;

    /**
     * 消息序号，一个 channel 中的序号唯一
     */
    private long deliveryTag;

    public RabbitmqSendResult() {
    }

    public RabbitmqSendResult(boolean success, long deliveryTag) {
        this.success = success;
        this.deliveryTag = deliveryTag;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String getMsgId() {
        return Long.toString(deliveryTag);
    }

    @Override
    public void setMsgId(String msgId) {
        this.deliveryTag = Long.parseLong(msgId);
    }

    @Override
    public String toString() {
        return "RabbitmqSendResult{" +
                "success=" + success +
                ", deliveryTag=" + deliveryTag +
                '}';
    }
}
