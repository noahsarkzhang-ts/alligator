package org.noahsark.server.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.noahsark.server.future.CommandCallback;
import org.noahsark.server.future.FutureManager;
import org.noahsark.server.future.RpcPromise;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.thread.ClientClearThread;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2021/3/14.
 */
public abstract class AbstractRemotingClient implements RemotingClient {

    private static Logger log = LoggerFactory.getLogger(AbstractRemotingClient.class);

    protected String host;

    protected int port;

    private EventLoopGroup group;

    private Map<RemoteOption<?>, Object> clientOptions = new HashMap<>();

    protected Channel channel;

    private Bootstrap bootstrap;

    private RetryPolicy retryPolicy;

    private ClientClearThread clearThread;

    public AbstractRemotingClient() {
    }

    public AbstractRemotingClient(String host, int port) {
        this.host = host;
        this.port = port;

        init();
    }

    protected void init() {
        try {
            preInit();

            clearThread = new ClientClearThread();
            clearThread.start();

            group = new NioEventLoopGroup();
            retryPolicy = new ExponentialBackOffRetry(1000, Integer.MAX_VALUE, 60 * 1000);

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
            ChannelFuture future = bootstrap.connect(this.host, this.port);
            future.addListener(getConnectionListener());
            this.channel = future.channel();
            future.awaitUninterruptibly();

        }

    }

    @Override
    public void shutdown() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();

        if (clearThread != null) {
            clearThread.shutdown();
        }

    }

    @Override
    public  RpcPromise invoke(Request request, CommandCallback callback) {

        RpcPromise promise = new RpcPromise();
        promise.addCallback(callback);

        this.sendMessage(request);

        /*this.channel.writeAndFlush(request);*/

        FutureManager.getInstance().registerPromise(request.getRequestId(), promise);
        return promise;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    @Override
    public void toggleServer() {
        // TODO
    }

    protected abstract void preInit();

    public abstract URI getUri();

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
