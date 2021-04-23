package org.noahsark.registration.domain;

import java.util.Map;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.session.Subject;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/8
 */
public class Service implements Subject,Comparable<Service> {

    private int zone;

    private int biz;

    private long loginTime;

    private long lastPingTime;

    private String id;

    private String name;

    private int load;

    private Map<String,Object> conf;

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    public Map<String, Object> getConf() {
        return conf;
    }

    public void setConf(Map<String, Object> conf) {
        this.conf = conf;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public long getLastPingTime() {
        return lastPingTime;
    }

    public void setLastPingTime(long lastPingTime) {
        this.lastPingTime = lastPingTime;
    }

    @Override
    public int compareTo(Service o) {
        return (int) (this.loginTime - o.getLoginTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Service service = (Service) o;

        if (zone != service.zone) {
            return false;
        }
        if (biz != service.biz) {
            return false;
        }
        return id.equals(service.id);
    }

    @Override
    public int hashCode() {
        int result = zone;
        result = 31 * result + biz;
        result = 31 * result + id.hashCode();
        return result;
    }
}
