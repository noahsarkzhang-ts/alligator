package org.noahsark.server.eventbus;

/**
 * 应用事件监听器
 * @author zhangxt
 * @date 2021/4/9
 */
public abstract class ApplicationListener<E extends ApplicationEvent> implements EventListener {
    public abstract void onApplicationEvent(E event);
}
