package org.noahsark.http.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/27
 */
@Configuration
public class CommonConfiguration {

    @Bean("asyncTaskExecutor")
    public ThreadPoolTaskExecutor getAsyncTaskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(5000);
        taskExecutor.setKeepAliveSeconds(120);
        taskExecutor.setThreadNamePrefix("AsyncExecutor-");

        return taskExecutor;

    }


}
