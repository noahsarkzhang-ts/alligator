package org.noahsark.server.eventbus;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/9
 */
public class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }
}
