package org.noahsark.server.session;

import org.noahsark.client.future.PromisHolder;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.server.rpc.RpcCommand;

/**
 * 链路通道
 *
 * @author zhangxt
 * @date 2021/5/4
 */
public interface ChannelHolder {

    /**
     * 向通道中写入数据
     *
     * @param response 响应结果
     */
    void write(RpcCommand response);

    /**
     * 获取promisHolder结构
     *
     * @return PromisHolder
     */
    PromisHolder getPromisHolder();

    /**
     * 获取subject
     *
     * @return Subject
     */
    Subject getSubject();

    /**
     * 设置subject
     */
    void setSubject(Subject subject);

}

