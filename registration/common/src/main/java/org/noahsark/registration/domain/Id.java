package org.noahsark.registration.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * id 信息
 * @author zhangxt
 * @date 2021/4/12
 */
public class Id implements Serializable {

    private String id;

    public Id(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;
        Id id1 = (Id) o;
        return Objects.equals(getId(), id1.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
