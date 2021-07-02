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
import org.noahsark.server.event.ClientConnectionSuccessEvent;
import org.noahsark.server.eventbus.EventBus;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.queue.WorkQueue;
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

    private WorkQueue workQueue;

    protected ConnectionManager connectionManager;

    protected ServerManager serverManager;

    protected Connection connection;

    private Bootstrap bootstrap;

    private ClientClearThread clearThread;

    private Thread thread;

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

            if (!this.existOption(RemoteOption.THREAD_NUM_OF_QUEUE)) {
                this.option(RemoteOption.THREAD_NUM_OF_QUEUE, 5);
            }

            initWorkQueue();

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

    private void initWorkQueue() {
        this.workQueue = new WorkQueue();
        this.workQueue.setMaxQueueNum(this.option(RemoteOption.CAPACITY_OF_QUEUE));
        this.workQueue.setMaxThreadNum(this.option(RemoteOption.THREAD_NUM_OF_QUEUE));

        this.workQueue.init();
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

    public <T> boolean existOption(RemoteOption<T> option) {
        return this.clientOptions.containsKey(option);
    }

    @Override
    public void connect() {

        Runnable runnable = () -> {
            synchronized (bootstrap) {
                ChannelFuture future = bootstrap
                        .connect(current.getHost(), current.getPort());
                future.addListener(getConnectionListener());
                Channel channel = future.channel();
                connection.setChannel(channel);

                future.awaitUninterruptibly();
            }
        };

        thread = new Thread(runnable, "client-thread-");
        thread.start();
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
        request.setRequestId(this.connection.nextId());

        promise.invoke(this.connection, request, commandCallback, timeoutMillis);

        return promise;
    }

    public Object invokeSync(Request request, int timeoutMillis) {
        RpcPromise promise = new RpcPromise();
        request.setRequestId(this.connection.nextId());

        Object result = promise.invokeSync(this.connection, request, timeoutMillis);

        return result;
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

            log.info("No server to toggle,reset servers.");
            serverManager.reset();

            final EventLoop eventLoop = this.connection.getChannel().eventLoop();

            eventLoop.schedule(() -> {
                log.info("Try Reconnecting ...");

                this.toggleServer();
            }, 3000, TimeUnit.MILLISECONDS);
        }

    }

    public WorkQueue getWorkQueue() {
        return this.workQueue;
    }

    @Override
    public ServerInfo getServerInfo() {
        return this.current;
    }

    protected abstract void preInit();

    public abstract ServerInfo convert(String url);

    public void registerProcessor(AbstractProcessor<?> processor) {
        processor.register();
    }

    public void unregisterProcessor(AbstractProcessor<?> processor) {
        processor.unregister();
    }

    private ChannelFutureListener getConnectionListener() {
        return future -> {
            if (!future.isSuccess()) {
                future.channel().pipeline().fireChannelInactive();
            } else {
                EventBus.getInstance().post(new ClientConnectionSuccessEvent(null));
            }
        };
    }


}
