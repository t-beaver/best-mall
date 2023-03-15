package com.taobao.weex.utils.batch;

public interface Interceptor {
    boolean take(Runnable runnable);
}
