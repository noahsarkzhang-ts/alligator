package org.noahsark.gw.configuration;

import org.noahsark.gw.config.CommonConfig;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.queue.WorkQueue;

/**
 * Created by hadoop on 2021/3/13.
 */
public class GwConfigration {

    /**
     * 构造工作队列
     *
     * @param config     配置
     * @param dispatcher 分发类
     * @return 工作队列
     */
    public WorkQueue workQueue(CommonConfig config, Dispatcher dispatcher) {
        WorkQueue workQueue = new WorkQueue();
        workQueue.setMaxQueueNum(config.getWorkQueue().getMaxQueueNum());
        workQueue.setMaxThreadNum(config.getWorkQueue().getMaxThreadNum());

        workQueue.init();

        return workQueue;
    }

}
