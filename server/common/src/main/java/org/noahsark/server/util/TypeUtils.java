package org.noahsark.server.util;

import java.lang.reflect.*;

/**
 * 类对象工具类
 * @author zhangxt
 * @date 2021/11/06 11:57
 **/
public class TypeUtils {

    public static Class<?> getFirstParameterizedType(Object object) {

        Class<?> thisClass = object.getClass();

        Type genericSuperType = thisClass.getGenericSuperclass();
        if (!(genericSuperType instanceof ParameterizedType)) {
            return Object.class;
        }

        int typeParamIndex = 0;

        Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();
        Type actualTypeParam = actualTypeParams[typeParamIndex];
        if (actualTypeParam instanceof ParameterizedType) {
            actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
        }

        if (actualTypeParam instanceof Class) {
            return (Class) actualTypeParam;
        }

        return Object.class;


    }
}
