package org.noahsark.registration.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/18
 */
public class ServiceQuery implements Serializable {

    /**
     *  biz
     */
    private int biz;

    /**
     *  客户端 ip
     */
    private String ip;

    /**
     *  扩展信息
     */
    private Map<String,String> conf;

    public int getBiz() {
        return biz;
    }

    public void setBiz(int biz) {
        this.biz = biz;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String> getConf() {
        return conf;
    }

    public void setConf(Map<String, String> conf) {
        this.conf = conf;
    }
}
