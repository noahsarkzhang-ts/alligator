package org.noahsark.server.event;

import org.noahsark.server.eventbus.ApplicationEvent;

/**
 * 提供给上层业务订阅的服务器启动事件
 * @author zhangxt
 * @date 2021/4/12
 */
public class ServerStartupEvent extends ApplicationEvent {

    public ServerStartupEvent(Object source) {
        super(source);
    }
}
