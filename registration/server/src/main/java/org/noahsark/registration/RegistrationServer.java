package org.noahsark.registration;

import org.noahsark.server.tcp.server.TcpServer;

/**
 * Created by hadoop on 2021/4/10.
 */
public class RegistrationServer {

    private TcpServer tcpServer;

    public RegistrationServer(String host, int port) {
        tcpServer = new TcpServer(host, port);
    }
}
