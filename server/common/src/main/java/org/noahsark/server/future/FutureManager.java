package org.noahsark.server.future;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/25
 */
public class FutureManager {
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

}
