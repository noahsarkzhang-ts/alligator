package org.noahsark.server.tcp.server;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class TcpServerTest {

    @Test
    public void tcpServerTest() {
        String host = "192.168.68.25";
        int port = 2222;

        final TcpServer tcpServer = new TcpServer(host, port);
        tcpServer.init();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                tcpServer.shutdown();
            }
        });

        tcpServer.start();

        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
