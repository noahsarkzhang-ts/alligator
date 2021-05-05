package org.noahsark.mq;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface SendCallback<R extends SendResult> {

    void onSuccess(R var1);

    void onException(Throwable var1);
}
