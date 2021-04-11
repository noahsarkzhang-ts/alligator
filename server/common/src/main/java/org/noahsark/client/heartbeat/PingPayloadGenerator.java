package org.noahsark.client.heartbeat;

/**
 * Created by hadoop on 2021/4/10.
 */
public interface PingPayloadGenerator {
    Object getPayload();
}
