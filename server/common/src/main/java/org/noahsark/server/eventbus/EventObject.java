package org.noahsark.server.eventbus;

import java.io.Serializable;

/**
 * 事件对象
 * @author zhangxt
 * @date 021/4/9
 */
public class EventObject implements Serializable {

    private Object source;

    private long timestamp = System.currentTimeMillis();

    public EventObject(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
