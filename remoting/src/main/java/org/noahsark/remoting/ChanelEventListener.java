package org.noahsark.remoting;

import io.netty.channel.Channel;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/1/14
 */
public interface ChanelEventListener {

    void onChannelConnect(final String remoteAddr, final Channel channel);

    void onChannelClose(final String remoteAddr, final Channel channel);

    void onChannelException(final String remoteAddr, final Channel channel);

    void onChannelIdle(final String remoteAddr, final Channel channel);

}
