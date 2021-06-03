package org.noahsark.gw.ws.main;

import org.noahsark.gw.config.CommonConfig;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.server.remote.RemoteOption;
import org.noahsark.server.ws.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
public class GwCommandLineRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(GwCommandLineRunner.class);

    @Autowired
    private CommonConfig config;

    @Override
    public void run(String... strings) throws Exception {

        final WebSocketServer webSocketServer = new WebSocketServer(config.getServerConfig().getHost(),
                config.getServerConfig().getPort());

        webSocketServer
                .option(RemoteOption.THREAD_NUM_OF_QUEUE, config.getWorkQueue().getMaxThreadNum());
        webSocketServer
                .option(RemoteOption.CAPACITY_OF_QUEUE, config.getWorkQueue().getMaxQueueNum());

        webSocketServer.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> webSocketServer.shutdown()));

        ServerContext.server = webSocketServer;

        webSocketServer.start();

        log.info("Stat server!!!");

    }
}
