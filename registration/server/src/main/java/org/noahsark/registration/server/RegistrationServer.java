package org.noahsark.registration.server;


import org.noahsark.server.tcp.server.TcpServer;

/**
 * 注册中心服务器
 *
 * @author zhangxt
 * @date 2021/4/10
 */
public class RegistrationServer extends TcpServer {

    public RegistrationServer(String host, int port) {
        super(host, port);
    }
}
