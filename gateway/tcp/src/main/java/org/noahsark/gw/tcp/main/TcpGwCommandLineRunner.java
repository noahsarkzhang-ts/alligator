package org.noahsark.gw.tcp.main;

import org.noahsark.gw.config.CommonConfig;
import org.noahsark.gw.context.ServerContext;
import org.noahsark.server.remote.RemoteOption;
import org.noahsark.server.tcp.server.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
public class TcpGwCommandLineRunner implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(TcpGwCommandLineRunner.class);

    @Autowired
    private CommonConfig config;

    @Override
    public void run(String... strings) throws Exception {

        final TcpServer tcpServer = new TcpServer(config.getServerConfig().getHost(),
                config.getServerConfig().getPort());

        tcpServer.option(RemoteOption.THREAD_NUM_OF_QUEUE, config.getWorkQueue().getMaxThreadNum());
        tcpServer.option(RemoteOption.CAPACITY_OF_QUEUE, config.getWorkQueue().getMaxQueueNum());

        tcpServer.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                tcpServer.shutdown();
            }
        });

        ServerContext.server = tcpServer;

        tcpServer.start();

        log.info("Tcp server was Started!!!");

    }
}
