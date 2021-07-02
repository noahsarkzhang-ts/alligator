package org.noahsark.biz.online.processor.user;

import java.io.Serializable;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/7/1
 */
public class UserQuery implements Serializable {

    private Integer current;

    private Integer size;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
