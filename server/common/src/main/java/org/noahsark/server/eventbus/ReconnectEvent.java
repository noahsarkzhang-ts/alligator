package org.noahsark.server.eventbus;

/**
 * 重连事件
 * @author zhangxt
 * @date 2021/4/9
 */
public class ReconnectEvent extends ApplicationEvent {

    public ReconnectEvent(Object source) {
        super(source);
    }
}
