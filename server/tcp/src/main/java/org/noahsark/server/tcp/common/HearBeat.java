package org.noahsark.server.tcp.common;

import java.io.Serializable;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/2
 */
public class HearBeat implements Serializable {

    private int load;

    public HearBeat() {
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "HearBeat{" +
                "load=" + load +
                '}';
    }
}
