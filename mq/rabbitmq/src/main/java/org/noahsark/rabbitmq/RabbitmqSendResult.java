package org.noahsark.rabbitmq;

import org.noahsark.mq.SendResult;

import java.util.Objects;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqSendResult implements SendResult {

    private boolean success;

    /**
     *  消息序号，一个 channel 中的序号唯一
     */
    private long deliveryTag;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getDeliveryTag() {
        return deliveryTag;
    }

    public void setDeliveryTag(long deliveryTag) {
        this.deliveryTag = deliveryTag;
    }

    @Override
    public String toString() {
        return "RabbitmqSendResult{" +
                "success=" + success +
                ", deliveryTag=" + deliveryTag +
                '}';
    }
}
