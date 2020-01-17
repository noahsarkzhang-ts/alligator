package org.noahsark.remoting;

import org.noahsark.remoting.netty.NettyRequestProcessor;
import org.noahsark.remoting.protocol.RemotingCommand;

import java.util.concurrent.ExecutorService;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/1/14
 */
public interface RemotingClient extends RemotingService {

    RemotingCommand invokeSync(final String addr, final RemotingCommand request, final long timeoutMillis);

    void invokeAsync(final String addr, final RemotingCommand request, final long timeoutMillis, final InvokeCallback invokeCallback);

    void invokeOneway(final String addr, final RemotingCommand request, final long timeoutMillis);

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor, final ExecutorService executor);
}
