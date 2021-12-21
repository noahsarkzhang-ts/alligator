package org.noahsark.server.event;

import org.noahsark.server.eventbus.ApplicationEvent;

/**
 * 提供给上层业务订阅的客户端下线事件
 * @author zhangxt
 * @date 2021/6/30
 */
public class ClientDisconnectEvent extends ApplicationEvent {

    public ClientDisconnectEvent(Object source) {
        super(source);
    }
}
