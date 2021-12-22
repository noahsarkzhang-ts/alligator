package org.noahsark.common.event;

/**
 * 服务事件
 *
 * @author zhangxt
 * @date 2021/6/27
 */
public class ServiceEvent {

    private String serviceId;

    private Byte serviceType;

    private Byte type;

    private Long timestamep;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getTimestamep() {
        return timestamep;
    }

    public void setTimestamep(Long timestamep) {
        this.timestamep = timestamep;
    }
}
