package org.noahsark.registration.main;

import org.noahsark.registration.config.CommonConfig;
import org.noahsark.registration.server.RegistrationServer;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.remote.RemoteOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 注册中心启动类
 *
 * @author zhangxt
 * @date 22021/3/13
 */
@Component
public class RegistrationCommandLineRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(RegistrationCommandLineRunner.class);

    @Autowired
    private CommonConfig config;

    @Resource
    private AbstractProcessor pingProcessor;

    @Override
    public void run(String... strings) throws Exception {

        final RegistrationServer registrationServer = new RegistrationServer(config.getServerConfig().getHost(),
                config.getServerConfig().getPort());

        registrationServer
                .option(RemoteOption.CAPACITY_OF_QUEUE, config.getWorkQueue().getMaxQueueNum());
        registrationServer
                .option(RemoteOption.THREAD_NUM_OF_QUEUE, config.getWorkQueue().getMaxThreadNum());

        registrationServer.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                registrationServer.shutdown();
            }
        });

        pingProcessor.register();

        registrationServer.start();

        log.info("Start RegistrationServer server!!!");

    }
}
