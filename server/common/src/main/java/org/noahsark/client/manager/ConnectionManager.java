package org.noahsark.client.manager;

import org.noahsark.client.heartbeat.HeartbeatFactory;
import org.noahsark.client.heartbeat.HeartbeatStatus;
import org.noahsark.server.remote.RetryPolicy;

/**
 * Created by hadoop on 2021/4/4.
 */
public class ConnectionManager {

    private HeartbeatFactory<?> heartbeatFactory;

    private HeartbeatStatus heartbeatStatus;

    private RetryPolicy retryPolicy;

    public ConnectionManager() {
        heartbeatStatus = new HeartbeatStatus();
    }

    public HeartbeatFactory<?> getHeartbeatFactory() {
        return heartbeatFactory;
    }

    public void setHeartbeatFactory(HeartbeatFactory<?> heartbeatFactory) {
        this.heartbeatFactory = heartbeatFactory;
    }

    public HeartbeatStatus getHeartbeatStatus() {
        return heartbeatStatus;
    }

    public void setHeartbeatStatus(HeartbeatStatus heartbeatStatus) {
        this.heartbeatStatus = heartbeatStatus;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }
}
