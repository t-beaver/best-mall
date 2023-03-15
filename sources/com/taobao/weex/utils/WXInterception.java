package com.taobao.weex.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WXInterception {

    private interface Intercepted {
    }

    private WXInterception() {
    }

    public static <T> T proxy(Object obj, Class<T> cls, InterceptionHandler<T> interceptionHandler) throws IllegalArgumentException {
        if (obj instanceof Intercepted) {
            return obj;
        }
        interceptionHandler.setDelegate(obj);
        return Proxy.newProxyInstance(WXInterception.class.getClassLoader(), new Class[]{cls, Intercepted.class}, interceptionHandler);
    }

    public static <T> T proxy(Object obj, InterceptionHandler<T> interceptionHandler, Class<?>... clsArr) throws IllegalArgumentException {
        interceptionHandler.setDelegate(obj);
        return Proxy.newProxyInstance(WXInterception.class.getClassLoader(), clsArr, interceptionHandler);
    }

    public static abstract class InterceptionHandler<T> implements InvocationHandler {
        private T mDelegate;

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            try {
                return method.invoke(delegate(), objArr);
            } catch (IllegalArgumentException e) {
                WXLogUtils.e("", (Throwable) e);
                return null;
            } catch (IllegalAccessException e2) {
                WXLogUtils.e("", (Throwable) e2);
                return null;
            } catch (InvocationTargetException e3) {
                throw e3.getTargetException();
            }
        }

        /* access modifiers changed from: protected */
        public T delegate() {
            return this.mDelegate;
        }

        /* access modifiers changed from: package-private */
        public void setDelegate(T t) {
            this.mDelegate = t;
        }
    }
}
