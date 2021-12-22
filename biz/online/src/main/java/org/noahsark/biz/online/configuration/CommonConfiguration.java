package org.noahsark.biz.online.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 通用配置
 *
 * @author zhangxt
 * @date 2021/6/30
 */
@Configuration
public class CommonConfiguration {

    @Bean("commonExecutor")
    public ThreadPoolTaskExecutor getAsyncTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setKeepAliveSeconds(120);
        taskExecutor.setThreadNamePrefix("commonExecutor-");

        return taskExecutor;

    }
}
