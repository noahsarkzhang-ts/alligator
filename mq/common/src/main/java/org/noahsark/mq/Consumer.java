package org.noahsark.mq;

import java.util.List;

/**
 * MQ 消费者
 *
 * @author zhangxt
 * @date 2021/4/29
 */
public interface Consumer<T extends Topic> {

    void registerMessageListener(MessageListener listener);

    void subscribe(T topic);

    void subscribe(List<T> topics);

    void start();

    void shutdown();


}
