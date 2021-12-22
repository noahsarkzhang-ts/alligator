package org.noahsark.registration.domain;

import java.io.Serializable;

/**
 * 满足条件的服务信息
 *
 * @author zhangxt
 * @date 2021/5/18
 */
public class CandidateService implements Serializable {

    private String address;

    private String topic;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "CandidateService{" +
                "address='" + address + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
