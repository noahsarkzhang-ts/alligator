package org.noahsark.server.event;

import org.noahsark.server.eventbus.ApplicationEvent;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
public class ClientConnectionEvent  extends ApplicationEvent {

    public ClientConnectionEvent(Object source) {
        super(source);
    }
}
