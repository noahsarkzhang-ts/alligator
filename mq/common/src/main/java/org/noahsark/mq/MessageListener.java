package org.noahsark.mq;

import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface MessageListener<M extends Message> {

    boolean consumeMessage(List<M> messageList);
}
