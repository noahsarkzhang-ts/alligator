package org.noahsark.gw;

import org.apache.rocketmq.client.log.ClientLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Websocket 网关计入口
 *
 * @author zhangxt
 * @date 2021/3/13
 */
@SpringBootApplication
public class WebsocketGwApp {
    public static void main(String[] args) {
        System.setProperty(ClientLogger.CLIENT_LOG_USESLF4J, "true");
        SpringApplication.run(WebsocketGwApp.class, args);
    }
}
