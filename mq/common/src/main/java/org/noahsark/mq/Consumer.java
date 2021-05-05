package org.noahsark.mq;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/29
 */
public interface Consumer<L extends MessageListener, T extends Topic> {

    void registerMessageListener(L listener);

    void subscribe(T topic);

    void start();

    void shutdown();


}
