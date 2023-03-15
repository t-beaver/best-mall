package com.taobao.weex.bridge;

public interface JSCallback {
    void invoke(Object obj);

    void invokeAndKeepAlive(Object obj);
}
