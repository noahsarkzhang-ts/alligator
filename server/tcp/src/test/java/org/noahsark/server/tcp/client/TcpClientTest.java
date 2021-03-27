package org.noahsark.server.tcp.client;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class TcpClientTest {

    @Test
    public void tcpClientText() {
        TcpClient tcpClient = new TcpClient("192.168.68.25", 2222);
        tcpClient.connect();

        try {
            TimeUnit.SECONDS.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
