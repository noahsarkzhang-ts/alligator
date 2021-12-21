package org.noahsark.client.future;

/**
 * 提供给业务上层的回调函数
 * @author zhangxt
 * @date 2021/3/27
 */
public interface CommandCallback {

    /**
     * 正常的回调函数（执行成功）
     * @param result 结果
     * @param currentFanout 第几个结果
     * @param fanout 期望的结果数量
     */
    void callback(Object result,int currentFanout, int fanout);

    /**
     * 失败之后的回调函数
     * @param cause 异常
     * @param currentFanout 第几个结果
     * @param fanout 期望的结果数量
     */
    void failure(Throwable cause, int currentFanout, int fanout);
}
