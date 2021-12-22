package org.noahsark.mq;

/**
 * 发送回调
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public interface SendCallback<R extends SendResult> {

    void onSuccess(R var1);

    void onException(Throwable var1);
}
