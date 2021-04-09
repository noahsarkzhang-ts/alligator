package org.noahsark.server.eventbus;

import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/9
 */
public class ReconnectListener extends ApplicationListener<ReconnectEvent> {

    @Override
    public void onApplicationEvent(ReconnectEvent event) {
        System.out.println("timestamp = " + event.getTimestamp());

    }

    public static void main(String[] args) throws InterruptedException {
        EventBus eventBus = EventBus.getInstance();
        ReconnectListener listener = new ReconnectListener();

        eventBus.register(listener);

        eventBus.post(new ReconnectEvent(null));

        TimeUnit.SECONDS.sleep(3);

        eventBus.close();

    }
}
