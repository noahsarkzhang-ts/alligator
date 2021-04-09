package org.noahsark.server.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.Connection;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.client.manager.ConnectionManager;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.thread.ClientClearThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hadoop on 2021/3/14.
 */
public abstract class AbstractRemotingClient implements RemotingClient {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingClient.class);

    protected ServerInfo current;

    private EventLoopGroup group;

    private Map<RemoteOption<?>, Object> clientOptions = new HashMap<>();

    protected ConnectionManager connectionManager;

    protected ServerManager serverManager;

    protected Connection connection;

    private Bootstrap bootstrap;

    private ClientClearThread clearThread;

    public AbstractRemotingClient() {
    }

    public AbstractRemotingClient(String url) {

        ServerInfo serverInfo = convert(url);

        List<ServerInfo> servers = new ArrayList<>();
        servers.add(serverInfo);

        serverManager = new ServerManager(servers);

        this.current = serverManager.toggleServer();

        init();
    }

    public AbstractRemotingClient(List<String> urls) {
        List<ServerInfo> servers = new ArrayList<>();

        urls.stream().forEach(url -> servers.add(this.convert(url)));

        serverManager = new ServerManager(servers);
        this.current = serverManager.toggleServer();

        init();
    }

    public AbstractRemotingClient(String host, int port) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setOriginUrl(host + ":" + port);
        serverInfo.setHost(host);
        serverInfo.setPort(port);

        List<ServerInfo> servers = new ArrayList<>();
        servers.add(serverInfo);

        serverManager = new ServerManager(servers);

        this.current = serverManager.toggleServer();

        init();
    }

    protected void init() {
        try {
            preInit();

            connection = new Connection();

            clearThread = new ClientClearThread();
            clearThread.start();

            group = new NioEventLoopGroup();

            bootstrap = new Bootstrap();
            bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(getChannelInitializer(this));

        } catch (Exception ex) {
            log.warn("catch an exception.", ex);
        }
    }

    protected abstract ChannelInitializer<SocketChannel> getChannelInitializer(
        AbstractRemotingClient server);

    public <T> void option(RemoteOption<T> option, T value) {
        this.clientOptions.put(option, value);

    }

    public <T> T option(RemoteOption<T> option) {
        return this.clientOptions.containsKey(option) ? (T) this.clientOptions.get(option)
            : option.getDefaultValue();
    }

    @Override
    public void connect() {
        synchronized (bootstrap) {
            ChannelFuture future = bootstrap
                .connect(this.current.getHost(), this.current.getPort());
            future.addListener(getConnectionListener());
            Channel channel = future.channel();
            connection.setChannel(channel);

            future.awaitUninterruptibly();

        }

    }

    @Override
    public void shutdown() {
        if (this.connection != null) {
            connection.close();
        }
        group.shutdownGracefully();

        if (clearThread != null) {
            clearThread.shutdown();
        }

    }

    @Override
    public RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis) {

        RpcPromise promise = new RpcPromise();
        promise.invoke(this.connection,request,commandCallback,timeoutMillis);

        return promise;
    }

    @Override
    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

    @Override
    public void toggleServer() {
        ServerInfo serverInfo = serverManager.toggleServer();

        if (serverInfo != null) {
            this.current = serverInfo;

            log.info("toggle to new server: {} : {}",
                serverInfo.getHost(), serverInfo.getPort());

            this.connect();
        } else {

            // TODO
            log.info("No server to toggle,reset servers.");
            serverManager.reset();

            final EventLoop eventLoop = this.connection.getChannel().eventLoop();

            eventLoop.schedule(() -> {
                log.info("Try Reconnecting ...");

                this.toggleServer();
            }, 3000, TimeUnit.MILLISECONDS);
        }

    }

    @Override
    public ServerInfo getServerInfo() {
        return this.current;
    }

    protected abstract void preInit();

    public abstract ServerInfo convert(String url);

    private ChannelFutureListener getConnectionListener() {
        return new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        };
    }
}
