package org.noahsark.server.event;

import org.noahsark.server.eventbus.ApplicationEvent;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/6/30
 */
public class ClientDisconnectEvent extends ApplicationEvent {

    public ClientDisconnectEvent(Object source) {
        super(source);
    }
}
