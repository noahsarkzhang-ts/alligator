package org.noahsark.server.event;

import org.noahsark.server.eventbus.ApplicationEvent;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
public class ServerStartupEvent extends ApplicationEvent {

    public ServerStartupEvent(Object source) {
        super(source);
    }
}
