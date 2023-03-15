package com.taobao.weex.bridge;

public interface JavascriptInvokable {
    Invoker getMethodInvoker(String str);

    String[] getMethods();
}
