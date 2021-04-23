package org.noahsark.gw.ws.context;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/21
 */
public class GlobalStatus {

    private int load;

    private static class GlobalStatusHolder {
        private static final GlobalStatus INSTANCE = new GlobalStatus();
    }

    private GlobalStatus() {
        this.load = 0;
    }

    public static GlobalStatus getInstance() {
        return GlobalStatusHolder.INSTANCE;
    }

    public synchronized void increment() {
        load++;
    }

    public synchronized void decrement() {
        load--;
    }

    public synchronized int get() {
        return load;
    }
}
