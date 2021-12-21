package org.noahsark.server.serializer;

/**
 * 序列化接口
 * @author zhangxt
 * @date 2021/4/2
 */
public interface Serializer {

    byte [] encode(Object obj);

    <T> T decode(byte [] bytes, Class<T> classz);
}
