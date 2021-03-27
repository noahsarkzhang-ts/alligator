package org.noahsark.server.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.server.future.RpcPromise;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.rpc.Request;

import java.net.URI;

public class TcpClient extends AbstractRemotingClient {

    public TcpClient(String host, int port) {
        super(host,port);
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(AbstractRemotingClient server) {
        return new ClientHandlersInitializer(this);
    }

    @Override
    protected void preInit() {
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public void ping() {
        String heartBeat = "heart beat!";
        this.sendMessage(heartBeat);
    }

    @Override
    public void sendMessage(String text) {
        this.channel.writeAndFlush(text);
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 2222);
        tcpClient.connect();
    }
}
