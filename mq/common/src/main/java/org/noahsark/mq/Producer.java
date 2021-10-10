package org.noahsark.mq;

import org.noahsark.server.rpc.RpcCommand;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface Producer<M extends Message,R extends SendResult> {

    void send(M msg, SendCallback sendCallback, long timeout);

    M buildMessage(RpcCommand command);

    R send(M msg);

    void sendOneway(M msg);

    void start();

    void shutdown();

}
