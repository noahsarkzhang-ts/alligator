package org.noahsark.mq;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface Producer<M extends Message,
    C extends SendCallback, R extends SendResult> {

    void send(M msg, C sendCallback, long timeout);

    R send(M msg);

    void sendOneway(M msg);

    void shutdown();

}
