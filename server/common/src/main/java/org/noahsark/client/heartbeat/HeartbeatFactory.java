package org.noahsark.client.heartbeat;

/**
 * 心跳工厂
 *
 * @author zhangxt
 * @date 2021/4/3
 */
public interface HeartbeatFactory<T> {

    /**
     * 获取 ping 对象
     *
     * @return ping对象
     */
    T getPing();

    /**
     * 获取 ping 消息体构造器
     *
     * @return 消息构造器
     */
    default PingPayloadGenerator getPayloadGenerator() {
        return null;
    }

    /**
     * 设置ping 消息体构造器
     *
     * @param payload 消息构造器
     */
    default void setPayloadGenerator(PingPayloadGenerator payload) {
    }

}
