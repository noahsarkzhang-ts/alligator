package org.noahsark.client.future;

import org.noahsark.server.rpc.Request;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/21
 */
public interface PromisHolder  {

    RpcPromise removePromis(Integer requestId);

    void registerPromise(Integer requestId, RpcPromise promise);

    RpcPromise getPromise(Integer requestId);

    void removePromis(RpcPromise promise);

    void write(Request request);
}
