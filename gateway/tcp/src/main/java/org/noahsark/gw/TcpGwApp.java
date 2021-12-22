package org.noahsark.gw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TCP 网关入口
 *
 * @author zhangxt
 * @date 2021/3/13
 */
@SpringBootApplication
public class TcpGwApp {
    public static void main(String[] args) {
        SpringApplication.run(TcpGwApp.class, args);
    }
}
