package org.noahsark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello Http App!
 */
@SpringBootApplication
//@EnableAsync
public class HttpApp {
    public static void main(String[] args) {
        SpringApplication.run(HttpApp.class, args);
    }
}
