package org.noahsark.mq;

/**
 * 监听器接口
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public interface MessageListener {

    boolean consumeMessage(byte[] message);
}
