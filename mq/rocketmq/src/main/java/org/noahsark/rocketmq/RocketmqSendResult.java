package org.noahsark.rocketmq;

import org.noahsark.mq.SendResult;

/**
 * 结果
 *
 * @author zhangxt
 * @date 2021/5/2
 */
public class RocketmqSendResult implements SendResult {

    private boolean success;

    private String msgId;

    public RocketmqSendResult() {
    }

    public RocketmqSendResult(boolean success, String msgId) {
        this.success = success;
        this.msgId = msgId;
    }

    @Override
    public String getMsgId() {
        return msgId;
    }

    @Override
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
