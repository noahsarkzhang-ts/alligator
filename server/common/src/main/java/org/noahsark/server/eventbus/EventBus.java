package org.noahsark.server.eventbus;

import org.noahsark.server.util.TypeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/9
 */
public final class EventBus {

    private Map<Class<?>, List<ApplicationListener>> listenerMap = new HashMap<>();

    private ExecutorService executor;

    private EventBus() {
        executor = Executors.newFixedThreadPool(2);
    }

    private static class EventBusHolder {
        private static final EventBus INSTACE = new EventBus();
    }

    public static EventBus getInstance() {
        return EventBusHolder.INSTACE;
    }

    public void register(ApplicationListener listener) {
        Class<?> classz = TypeUtils.getFirstParameterizedType(listener);
        List<ApplicationListener> listeners = listenerMap.get(classz);

        if (listeners != null) {
            listeners.add(listener);
        } else {
            listeners = new ArrayList<>();
            listeners.add(listener);

            listenerMap.put(classz, listeners);
        }

    }

    public void post(ApplicationEvent event) {
        List<ApplicationListener> listeners = listenerMap.get(event.getClass());

        executor.execute(() -> listeners.stream().forEach(listener -> listener.onApplicationEvent(event)));
    }

    public void close() {
        if (executor != null) {
            executor.shutdown();
        }
    }

}
