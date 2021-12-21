package org.noahsark.client.heartbeat;

/**
 * ping 消息体构造器
 * @author zhangxt
 * @date 2021/4/10
 */
public interface PingPayloadGenerator {
    /**
     * 获取消息体对象
     *
     * @return 消息体对象
     */
    Object getPayload();
}
