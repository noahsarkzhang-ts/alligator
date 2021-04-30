package org.noahsark.mq;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface Consumer {

    void registerMessageListener(MessageListener listener);

    void subscribe(Topic topic);


}
