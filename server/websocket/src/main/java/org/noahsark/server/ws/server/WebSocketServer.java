package org.noahsark.server.ws.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.server.processor.PingProcessor;
import org.noahsark.server.remote.AbstractRemotingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器
 *
 * @author zhangxt
 * @date 2021/4/3
 */
public final class WebSocketServer extends AbstractRemotingServer {

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    public WebSocketServer() {
    }

    public WebSocketServer(String host, int port) {
        super(host, port);
        registerDefaultPorcessor();
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(AbstractRemotingServer server) {
        return new WebSocketServerInitializer(this);
    }

    private void registerDefaultPorcessor() {
        this.registerProcessor(new PingProcessor());
    }

    public static void main(String[] args) {

    }


}
