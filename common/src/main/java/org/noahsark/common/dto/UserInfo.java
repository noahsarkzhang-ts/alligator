package org.noahsark.common.dto;

import java.io.Serializable;

/**
 * Created by hadoop on 2021/6/27.
 */
public class UserInfo implements Serializable {

    private String userId;

    private String name;

    private String serviceId;

    /**
     *  用户在线状态：0：离线，1：上线
     */
    private byte state;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserInfo userInfo = (UserInfo) o;

        return userId.equals(userInfo.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "userId='" + userId + '\'' +
            ", name='" + name + '\'' +
            ", serviceId='" + serviceId + '\'' +
            ", state=" + state +
            '}';
    }
}
