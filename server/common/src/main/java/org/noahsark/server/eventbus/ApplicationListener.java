package org.noahsark.server.eventbus;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/9
 */
public abstract class ApplicationListener<E extends ApplicationEvent> implements EventListener {
    abstract void onApplicationEvent(E event);
}
