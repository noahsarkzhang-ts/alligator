package org.noahsark.biz.online;

import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineApp
{
    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J,"true");
        SpringApplication.run(OnlineApp.class, args);
    }
}
