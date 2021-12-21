package org.noahsark.server.event;

import org.noahsark.server.eventbus.ApplicationEvent;

/**
 * 提供给上层业务订阅的客户端上线事件
 * @author zhangxt
 * @date 2021/4/12
 */
public class ClientConnectionSuccessEvent extends ApplicationEvent {

    public ClientConnectionSuccessEvent(Object source) {
        super(source);
    }
}
