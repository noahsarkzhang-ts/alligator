package org.noahsark.server.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import java.util.List;
import org.noahsark.client.heartbeat.PingPayloadGenerator;
import org.noahsark.client.manager.ConnectionManager;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.remote.ExponentialBackOffRetry;
import org.noahsark.server.remote.ServerInfo;
import org.noahsark.server.rpc.RpcCommand;

import java.net.URI;

public class TcpClient extends AbstractRemotingClient {

    public TcpClient(String host, int port) {
        super(host, port);
    }

    public TcpClient(String url) {
        super(url);
    }

    public TcpClient(List<String> urls) {
        super(urls);
    }

    @Override
    protected ChannelInitializer<SocketChannel> getChannelInitializer(
        AbstractRemotingClient server) {
        return new ClientHandlersInitializer(this);
    }

    @Override
    protected void preInit() {

        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.setHeartbeatFactory(new TcpHeartbeatFactory());
        connectionManager
            .setRetryPolicy(new ExponentialBackOffRetry(1000, 4, 60 * 1000));

        this.connectionManager = connectionManager;

    }

    @Override
    public ServerInfo convert(String url) {

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(url);

        String[] parts = url.split(":");

        serverInfo.setHost(parts[0]);
        serverInfo.setPort(Integer.valueOf(parts[1]));

        return serverInfo;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    @Override
    public void ping() {

        this.sendMessage((RpcCommand) this.connectionManager.getHeartbeatFactory().getPing());
    }

    @Override
    public void sendMessage(RpcCommand command) {
        this.connection.getChannel().writeAndFlush(command);
    }

    public void registerPingPayloadGenerator(PingPayloadGenerator payloadGenerator) {
        if (this.connectionManager != null) {
            this.connectionManager.getHeartbeatFactory()
                .setPayloadGenerator(payloadGenerator);
        }
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 2222);
        tcpClient.connect();
    }

}
