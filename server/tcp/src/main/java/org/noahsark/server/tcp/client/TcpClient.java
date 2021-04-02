package org.noahsark.server.tcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.noahsark.server.future.RpcPromise;
import org.noahsark.server.remote.AbstractRemotingClient;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.RpcCommand;
import org.noahsark.server.tcp.common.HearBeat;

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

        HearBeat hearBeat = new HearBeat();
        hearBeat.setLoad(10);

        Request request = new Request.Builder()
                .biz(1)
                .cmd(1000)
                .payload(hearBeat)
                .build();

        this.sendMessage(request);
    }

    @Override
    public void sendMessage(RpcCommand command) {
        this.channel.writeAndFlush(command);
    }

    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost", 2222);
        tcpClient.connect();
    }

}
