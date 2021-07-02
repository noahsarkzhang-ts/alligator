package org.noahsark.gw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/6/30
 */
@Configuration
public class CommonConfiguration {

    @Bean("commonExecutor")
    public ThreadPoolTaskExecutor getAsyncTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setKeepAliveSeconds(120);
        taskExecutor.setThreadNamePrefix("commonExecutor-");

        return taskExecutor;

    }
}
