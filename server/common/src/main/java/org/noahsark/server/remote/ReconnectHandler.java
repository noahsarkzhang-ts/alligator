package org.noahsark.server.remote;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(ReconnectHandler.class);

    private int retries = 0;
    private RetryPolicy retryPolicy;

    private RemotingClient remotringClient;

    public ReconnectHandler(RemotingClient remotringClient) {
        this.remotringClient = remotringClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Successfully established a connection to the server.");
        retries = 3;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (retries == 0) {
            log.info("Lost the TCP connection with the server.");
            ctx.close();
        }

        boolean allowRetry = getRetryPolicy().allowRetry(retries);
        if (allowRetry) {

            long sleepTimeMs = getRetryPolicy().getSleepTimeMs(retries);

            log.info(String.format("Try to reconnect to the server after %dms. Retry count: %d.", sleepTimeMs, ++retries));

            final EventLoop eventLoop = ctx.channel().eventLoop();

            eventLoop.schedule(() -> {
                log.info("Try Reconnecting ...");

                remotringClient.connect();
            }, sleepTimeMs, TimeUnit.MILLISECONDS);

            retries--;
        }
        ctx.fireChannelInactive();
    }


    private RetryPolicy getRetryPolicy() {
        if (this.retryPolicy == null) {
            this.retryPolicy = remotringClient.getRetryPolicy();
        }
        return this.retryPolicy;
    }
}
