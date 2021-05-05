package org.noahsark.rocketmq;

import org.noahsark.mq.SendResult;

/**
 * Created by hadoop on 2021/5/2.
 */
public class RocketmqSendResult implements SendResult {

    private boolean success;

    private String msgId;


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
