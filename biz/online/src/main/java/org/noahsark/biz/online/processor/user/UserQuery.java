package org.noahsark.biz.online.processor.user;

import java.io.Serializable;

/**
 * 用户查询
 *
 * @author zhangxt
 * @date 2021/7/1
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
