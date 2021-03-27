package org.noahsark.server.util;

import java.lang.reflect.*;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/27
 */
public class TypeUtils {

    public static Class<?> getFirstParameterizedType(Object object, Class<?> parametrizedSuperclass) {
        Class<?> thisClass = object.getClass();
        Class currentClass = thisClass;

        do {
            while (currentClass.getSuperclass() != parametrizedSuperclass) {
                currentClass = currentClass.getSuperclass();
                if (currentClass == null) {
                    throw new IllegalStateException("cannot determine the type of the type parameter: "
                            + currentClass.getSimpleName() + "");
                }
            }

            Type genericSuperType = currentClass.getGenericSuperclass();
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

            if (actualTypeParam instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
                if (componentType instanceof ParameterizedType) {
                    componentType = ((ParameterizedType) componentType).getRawType();
                }

                if (componentType instanceof Class) {
                    return Array.newInstance((Class) componentType, 0).getClass();
                }
            }

            if (!(actualTypeParam instanceof TypeVariable)) {
                throw new IllegalStateException("cannot determine the type of the type parameter  :" + thisClass);
            }

            TypeVariable<?> v = (TypeVariable) actualTypeParam;
            currentClass = thisClass;
            if (!(v.getGenericDeclaration() instanceof Class)) {
                return Object.class;
            }

            parametrizedSuperclass = (Class) v.getGenericDeclaration();
        } while (parametrizedSuperclass.isAssignableFrom(thisClass));

        return Object.class;

    }

    public static Class<?> getFirstParameterizedType(Object object) {


        Class<?> thisClass = object.getClass();
        Type type = thisClass.getGenericSuperclass();

        if (type instanceof ParameterizedType ) {
            System.out.println("type = " + type);
        }

        return thisClass;


    }
}
