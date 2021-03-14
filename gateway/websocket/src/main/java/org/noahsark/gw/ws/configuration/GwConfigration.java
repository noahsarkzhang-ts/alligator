package org.noahsark.gw.ws.configuration;

import org.noahsark.gw.ws.config.CommonConfig;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.queue.WorkQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
@Configuration
@ComponentScan("org.noahsark.gw.ws")
public class GwConfigration {

  /**
   *  构造工作队列
   * @param config 配置
   * @param dispatcher 分发类
   * @return 工作队列
   */
  @Bean(name = "workQueue")
  public WorkQueue workQueue(CommonConfig config, Dispatcher dispatcher) {
    WorkQueue workQueue = new WorkQueue();
    workQueue.setMaxQueueNum(config.getWorkQueue().getMaxQueueNum());
    workQueue.setMaxThreadNum(config.getWorkQueue().getMaxThreadNum());

    workQueue.setDispatcher(dispatcher);
    workQueue.init();

    return workQueue;
  }

  @Bean
  public Dispatcher dispatcher() {
    return new Dispatcher();
  }

}
