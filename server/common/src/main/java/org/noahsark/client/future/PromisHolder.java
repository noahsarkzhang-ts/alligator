package org.noahsark.client.future;

import org.noahsark.server.rpc.RpcCommand;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/21
 */
public interface PromisHolder  {

    /**
     * 移除 RpcPromise
     * @param requestId 请求id
     * @return RpcPromise
     */
    RpcPromise removePromis(Integer requestId);

    /**
     * 使用 requestId 注册RpcPromise
     * @param requestId 请求id
     * @param promise 注册RpcPromise
     */
    void registerPromise(Integer requestId, RpcPromise promise);

    /**
     * 根据 requestId 查询 请求id
     * @param requestId 请求id
     * @return RpcPromise
     */
    RpcPromise getPromise(Integer requestId);

    /**
     *  移除 RpcPromise
     * @param promise RpcPromise
     */
    void removePromis(RpcPromise promise);

    /**
     * 向通道中发送指令
     * @param command
     */
    void write(RpcCommand command);

    /**
     * 获取下一个 reqeustId
     * @return 请求id
     */
    int nextId();
}
