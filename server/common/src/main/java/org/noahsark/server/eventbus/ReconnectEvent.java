package org.noahsark.server.eventbus;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/9
 */
public class ReconnectEvent extends ApplicationEvent {

    public ReconnectEvent(Object source) {
        super(source);
    }
}
