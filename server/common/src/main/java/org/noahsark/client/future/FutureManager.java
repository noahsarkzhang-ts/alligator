package org.noahsark.client.future;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import org.noahsark.exception.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RPC 调用管理类
 * @author zhangxt
 * @date 2021/3/25
 */
public class FutureManager {

    private static Logger log = LoggerFactory.getLogger(FutureManager.class);

    private Map<Integer, RpcPromise> futures = new HashMap<>();
    private PriorityQueue<RpcPromise> queue = new PriorityQueue<>();

    private static class FutureManagerHolder {
        private static final FutureManager instance = new FutureManager();
    }

    private FutureManager() {
    }

    public static FutureManager getInstance() {
        return FutureManagerHolder.instance;
    }

    public RpcPromise getPromise(Integer requestId) {
        return futures.get(requestId);
    }

    public void registerPromise(Integer requestId, RpcPromise promise) {
        futures.put(requestId, promise);
        queue.add(promise);
    }

    public void removePromis(Integer requestId) {
        this.queue.remove(this.futures.get(requestId));
        this.futures.remove(requestId);
    }

    public void removePromis(RpcPromise promise) {
        queue.remove(promise);
        this.futures.remove(promise.getRequestId());
    }

    public void clear(int intervalMillis) {

        Instant instant = Instant.now();
        long currentMillis = instant.toEpochMilli();

        RpcPromise promise = null;
        long timeStampMillis = 0L;
        long timeoutMillis = 0L;

        while (!queue.isEmpty()) {
            promise = queue.peek();

            timeStampMillis = promise.getTimestampMillis();
            timeoutMillis = currentMillis - timeStampMillis;

            if (timeoutMillis >= intervalMillis) {

                promise.setFailure(new TimeoutException());
                this.removePromis(promise);

                log.warn("Request timeout: {},timeout: {}",promise.getRequestId(),timeoutMillis);
            } else {
                break;
            }
        }

    }

}
