package com.alibaba.android.bindingx.plugin.weex;

import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

public class BindingX {
    private BindingX() {
    }

    public static void register() throws WXException {
        WXSDKEngine.registerModule("expressionBinding", WXExpressionBindingModule.class);
        WXSDKEngine.registerModule("binding", WXBindingXModule.class);
        WXSDKEngine.registerModule("bindingx", WXBindingXModule.class);
    }
}
