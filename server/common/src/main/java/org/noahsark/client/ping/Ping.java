package org.noahsark.client.ping;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/2
 */
public class Ping implements Serializable {

    private int load;

    private Map<String, Object> extensions = new HashMap<>();

    public Ping() {
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public Object getExtension(String key) {
        return extensions.get(key);
    }

    public void setExtension(String key, Object val) {
        this.extensions.put(key, val);
    }

    @Override
    public String toString() {
        return "Ping{" +
            "load=" + load +
            '}';
    }
}
