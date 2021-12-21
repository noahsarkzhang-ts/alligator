package org.noahsark.server.eventbus;

/**
 * 定义给业务上层的应用事件
 * @author zhangxt
 * @date 2021/4/9
 */
public class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }
}
