package com.taobao.weex.bridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MethodInvoker implements Invoker {
    final Method mMethod;
    Type[] mParam;
    final boolean mRunOnUIThread;

    public MethodInvoker(Method method) {
        this(method, false);
    }

    public MethodInvoker(Method method, boolean z) {
        this.mMethod = method;
        this.mParam = method.getGenericParameterTypes();
        this.mRunOnUIThread = z;
    }

    public Object invoke(Object obj, Object... objArr) throws InvocationTargetException, IllegalAccessException {
        return this.mMethod.invoke(obj, objArr);
    }

    public Type[] getParameterTypes() {
        if (this.mParam == null) {
            this.mParam = this.mMethod.getGenericParameterTypes();
        }
        return this.mParam;
    }

    public boolean isRunOnUIThread() {
        return this.mRunOnUIThread;
    }

    public String toString() {
        return this.mMethod.getName();
    }
}
