package com.taobao.weex.utils.batch;

public interface BactchExecutor {
    void post(Runnable runnable);

    void setInterceptor(Interceptor interceptor);
}
