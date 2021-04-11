package org.noahsark.client.heartbeat;

/**
 * Created by hadoop on 2021/4/3.
 */
public interface HeartbeatFactory<T> {

    T getPing();

    default PingPayloadGenerator getPayloadGenerator() {
        return null;
    }

    default void setPayloadGenerator(PingPayloadGenerator payload) {}

}
