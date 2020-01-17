package org.noahsark.remoting;

import io.netty.channel.Channel;
import org.noahsark.remoting.netty.NettyRequestProcessor;
import org.noahsark.remoting.protocol.RemotingCommand;

import java.util.concurrent.ExecutorService;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/1/14
 */
public interface RemotingServer extends RemotingService {

    void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    RemotingCommand invokeSync(final Channel channel, final RemotingCommand request, final long timeoutMillis);

    void invokeAsync(final Channel channel, final RemotingCommand request, final long timeoutMillis, final InvokeCallback invokeCallback);

    void invokeOneway(final Channel channel, final RemotingCommand request, final long timeoutMillis);

}
