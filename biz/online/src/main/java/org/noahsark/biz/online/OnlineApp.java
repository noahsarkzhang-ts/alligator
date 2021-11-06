package org.noahsark.biz.online;

import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在线服务启动类
 * @author zhangxt
 * @date 2021/11/06 15:22
 **/
@SpringBootApplication
public class OnlineApp
{
    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J,"true");
        SpringApplication.run(OnlineApp.class, args);
    }
}
