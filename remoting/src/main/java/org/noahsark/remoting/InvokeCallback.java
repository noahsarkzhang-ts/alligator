package org.noahsark.remoting;

import org.noahsark.remoting.netty.ResponseFuture;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/1/14
 */
public interface InvokeCallback {

    void onComplte(ResponseFuture responseFuture);
}
