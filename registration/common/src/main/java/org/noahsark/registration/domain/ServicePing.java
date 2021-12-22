package org.noahsark.registration.domain;

/**
 * 服务Ping信息
 *
 * @author zhangxt
 * @date 2021/4/8
 */
public class ServicePing {

    private int load;

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "ServicePing{" +
                "load=" + load +
                '}';
    }
}
