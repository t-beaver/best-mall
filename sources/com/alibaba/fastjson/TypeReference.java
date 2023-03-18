package com.alibaba.fastjson;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeReference<T> {
    public static final Type LIST_STRING = new TypeReference<List<String>>() {
    }.getType();
    static ConcurrentMap<Type, Type> classTypeCache = new ConcurrentHashMap(16, 0.75f, 1);
    protected final Type type;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.lang.reflect.Type} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected TypeReference() {
        /*
            r2 = this;
            r2.<init>()
            java.lang.Class r0 = r2.getClass()
            java.lang.reflect.Type r0 = r0.getGenericSuperclass()
            java.lang.reflect.ParameterizedType r0 = (java.lang.reflect.ParameterizedType) r0
            java.lang.reflect.Type[] r0 = r0.getActualTypeArguments()
            r1 = 0
            r0 = r0[r1]
            java.util.concurrent.ConcurrentMap<java.lang.reflect.Type, java.lang.reflect.Type> r1 = classTypeCache
            java.lang.Object r1 = r1.get(r0)
            java.lang.reflect.Type r1 = (java.lang.reflect.Type) r1
            if (r1 != 0) goto L_0x002c
            java.util.concurrent.ConcurrentMap<java.lang.reflect.Type, java.lang.reflect.Type> r1 = classTypeCache
            r1.putIfAbsent(r0, r0)
            java.util.concurrent.ConcurrentMap<java.lang.reflect.Type, java.lang.reflect.Type> r1 = classTypeCache
            java.lang.Object r0 = r1.get(r0)
            r1 = r0
            java.lang.reflect.Type r1 = (java.lang.reflect.Type) r1
        L_0x002c:
            r2.type = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.TypeReference.<init>():void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: java.lang.reflect.Type} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected TypeReference(java.lang.reflect.Type... r7) {
        /*
            r6 = this;
            r6.<init>()
            java.lang.Class r0 = r6.getClass()
            java.lang.reflect.Type r1 = r0.getGenericSuperclass()
            java.lang.reflect.ParameterizedType r1 = (java.lang.reflect.ParameterizedType) r1
            java.lang.reflect.Type[] r1 = r1.getActualTypeArguments()
            r2 = 0
            r1 = r1[r2]
            java.lang.reflect.ParameterizedType r1 = (java.lang.reflect.ParameterizedType) r1
            java.lang.reflect.Type r3 = r1.getRawType()
            java.lang.reflect.Type[] r1 = r1.getActualTypeArguments()
            r4 = 0
        L_0x001f:
            int r5 = r1.length
            if (r2 >= r5) goto L_0x0055
            r5 = r1[r2]
            boolean r5 = r5 instanceof java.lang.reflect.TypeVariable
            if (r5 == 0) goto L_0x0032
            int r5 = r7.length
            if (r4 >= r5) goto L_0x0032
            int r5 = r4 + 1
            r4 = r7[r4]
            r1[r2] = r4
            r4 = r5
        L_0x0032:
            r5 = r1[r2]
            boolean r5 = r5 instanceof java.lang.reflect.GenericArrayType
            if (r5 == 0) goto L_0x0042
            r5 = r1[r2]
            java.lang.reflect.GenericArrayType r5 = (java.lang.reflect.GenericArrayType) r5
            java.lang.reflect.Type r5 = com.alibaba.fastjson.util.TypeUtils.checkPrimitiveArray(r5)
            r1[r2] = r5
        L_0x0042:
            r5 = r1[r2]
            boolean r5 = r5 instanceof java.lang.reflect.ParameterizedType
            if (r5 == 0) goto L_0x0052
            r5 = r1[r2]
            java.lang.reflect.ParameterizedType r5 = (java.lang.reflect.ParameterizedType) r5
            java.lang.reflect.Type r5 = r6.handlerParameterizedType(r5, r7, r4)
            r1[r2] = r5
        L_0x0052:
            int r2 = r2 + 1
            goto L_0x001f
        L_0x0055:
            com.alibaba.fastjson.util.ParameterizedTypeImpl r7 = new com.alibaba.fastjson.util.ParameterizedTypeImpl
            r7.<init>(r1, r0, r3)
            java.util.concurrent.ConcurrentMap<java.lang.reflect.Type, java.lang.reflect.Type> r0 = classTypeCache
            java.lang.Object r0 = r0.get(r7)
            java.lang.reflect.Type r0 = (java.lang.reflect.Type) r0
            if (r0 != 0) goto L_0x0072
            java.util.concurrent.ConcurrentMap<java.lang.reflect.Type, java.lang.reflect.Type> r0 = classTypeCache
            r0.putIfAbsent(r7, r7)
            java.util.concurrent.ConcurrentMap<java.lang.reflect.Type, java.lang.reflect.Type> r0 = classTypeCache
            java.lang.Object r7 = r0.get(r7)
            r0 = r7
            java.lang.reflect.Type r0 = (java.lang.reflect.Type) r0
        L_0x0072:
            r6.type = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.TypeReference.<init>(java.lang.reflect.Type[]):void");
    }

    public static Type intern(ParameterizedTypeImpl parameterizedTypeImpl) {
        Type type2 = (Type) classTypeCache.get(parameterizedTypeImpl);
        if (type2 != null) {
            return type2;
        }
        classTypeCache.putIfAbsent(parameterizedTypeImpl, parameterizedTypeImpl);
        return (Type) classTypeCache.get(parameterizedTypeImpl);
    }

    private Type handlerParameterizedType(ParameterizedType parameterizedType, Type[] typeArr, int i) {
        Class<?> cls = getClass();
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (int i2 = 0; i2 < actualTypeArguments.length; i2++) {
            if ((actualTypeArguments[i2] instanceof TypeVariable) && i < typeArr.length) {
                actualTypeArguments[i2] = typeArr[i];
                i++;
            }
            if (actualTypeArguments[i2] instanceof GenericArrayType) {
                actualTypeArguments[i2] = TypeUtils.checkPrimitiveArray((GenericArrayType) actualTypeArguments[i2]);
            }
            if (actualTypeArguments[i2] instanceof ParameterizedType) {
                actualTypeArguments[i2] = handlerParameterizedType((ParameterizedType) actualTypeArguments[i2], typeArr, i);
            }
        }
        return new ParameterizedTypeImpl(actualTypeArguments, cls, rawType);
    }

    public Type getType() {
        return this.type;
    }
}
