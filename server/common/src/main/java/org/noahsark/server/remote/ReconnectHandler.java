package org.noahsark.server.remote;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 连接重连处理器
 * @author zhangxt
 * @date 2021/3/14
 */
@ChannelHandler.Sharable
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ReconnectHandler.class);

    private int retries = 1;
    private RetryPolicy retryPolicy;

    private RemotingClient remotringClient;

    public ReconnectHandler(RemotingClient remotringClient) {
        this.remotringClient = remotringClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Successfully established a connection to the server.");
        retries = 1;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        boolean allowRetry = getRetryPolicy().allowRetry(retries);
        if (allowRetry) {

            long sleepTimeMs = getRetryPolicy().getSleepTimeMs(retries);

            log.info(String
                .format("Try to reconnect to the server after %dms. Retry count: %d.", sleepTimeMs,
                    retries++));

            final EventLoop eventLoop = ctx.channel().eventLoop();

            eventLoop.schedule(() -> {
                log.info("Try Reconnecting ...");

                remotringClient.connect();
            }, sleepTimeMs, TimeUnit.MILLISECONDS);

        } else {

            retries = 1;

            remotringClient.toggleServer();

        }
        ctx.fireChannelInactive();
    }


    private RetryPolicy getRetryPolicy() {
        if (this.retryPolicy == null) {
            this.retryPolicy = remotringClient.getConnectionManager()
                .getRetryPolicy();
        }
        return this.retryPolicy;
    }
}
