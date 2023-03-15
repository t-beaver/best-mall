package com.taobao.weex.bridge;

import com.taobao.weex.common.WXModule;

public interface ModuleFactory<T extends WXModule> extends JavascriptInvokable {
    T buildInstance() throws IllegalAccessException, InstantiationException;
}
