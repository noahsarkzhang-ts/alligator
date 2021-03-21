package org.noahsark.server.queue;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.RpcRequest;
import org.noahsark.server.thread.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class WorkQueue {

    private static Logger log = LoggerFactory.getLogger(WorkQueue.class);

    private static final int DEFAULT_MAX_QUEUE_NUM = 10000;

    private static final int DEFAULT_MAX_THREAD_NUM = 100;

    private int maxQueueNum = DEFAULT_MAX_QUEUE_NUM;

    private int maxThreadNum = DEFAULT_MAX_THREAD_NUM;

    // 工作线程池
    private WorkThread[] threads;

    // 阻塞队列
    private BlockingQueue<RpcRequest> queue;

    // 业务分发器
    private Dispatcher dispatcher = Dispatcher.getInstance();

    public WorkQueue() {
    }

    public void init() {
        queue = new LinkedBlockingQueue<>(this.maxQueueNum);
        threads = new WorkThread[this.maxThreadNum];

        WorkThread thread;
        for (int i = 0; i < maxThreadNum; i++) {
            thread = new WorkThread(i, queue, dispatcher);

            threads[i] = thread;

            thread.start();
        }

        log.info("WorkQueue start... ");
    }

    public boolean isBusy() {
        return queue.size() >= maxQueueNum;
    }

    public void add(RpcRequest request) {
        if (this.queue.size() <= maxQueueNum) {
            log.info("Add request: {}", request);

            this.queue.add(request);
        } else {
            log.warn("event request size[{}] enough, so drop this request {}", this.queue.size(), queue.toString());
        }
    }

    public int getMaxQueueNum() {
        return maxQueueNum;
    }

    public void setMaxQueueNum(int maxQueueNum) {
        this.maxQueueNum = maxQueueNum;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void shutdown() {
        for (WorkThread thread : threads) {
            thread.shutdown();
        }
    }

    private static class WorkThread extends ServiceThread {

        // 线程编号
        private int seq;

        // 工作队列
        private BlockingQueue<RpcRequest> queue;

        private Dispatcher dispatcher;

        public WorkThread(int seq, BlockingQueue<RpcRequest> queue, Dispatcher dispatcher) {
            this.seq = seq;
            this.queue = queue;
            this.dispatcher = dispatcher;
        }

        @Override
        public void run() {

            while (!this.isStopped()) {
                try {
                    RpcRequest request = this.queue.take();

                    String processName = request.getRequest().getClassName() + ":" + request.getRequest().getMethod();
                    log.info("processName: {}", processName);

                    AbstractProcessor processor = dispatcher.getProcessor(processName);
                    processor.process(request);

                } catch (Exception ex) {
                    log.warn(this.getServiceName() + " controller has exception. ", ex);
                }

            }
        }

        @Override
        public String getServiceName() {
            return "WorkThread - " + seq;
        }
    }


}
