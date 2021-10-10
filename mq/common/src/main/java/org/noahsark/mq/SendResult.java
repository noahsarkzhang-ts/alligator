package org.noahsark.mq;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface SendResult {

    boolean isSuccess();

    void setSuccess(boolean success);

    String getMsgId();

    void setMsgId(String msgId);

}
