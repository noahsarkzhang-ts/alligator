package org.noahsark.biz.online.main;

import org.noahsark.biz.online.config.CommonConfig;
import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.registration.RegistrationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/21
 */
@Component
public class RegistrationCommandLineRunner implements CommandLineRunner {

    @Autowired
    private CommonConfig config;

    @Override
    public void run(String... args) throws Exception {
        RegistrationClient regClient = new RegistrationClient(config.getRegServer().getHost(),
                config.getRegServer().getPort());

        regClient.connect();

        ServerContext.regClient = regClient;
    }
}
