package org.noahsark.client.future;

/**
 * 流处理接口，向 Rpc 中添加支持流处理的能力
 * @author zhangxt
 * @date 2021/9/9
 */
public interface StreamPromise<V> {

    /**
     *  发送中间结果数据
     * @param result 结果数据
     * @return StreamPromise
     */
    StreamPromise<V> flow(V result);

    /**
     *  发送最后一个结果数据
     * @param result 结果数据
     * @return StreamPromise
     */
    StreamPromise<V> end(V result);

}
