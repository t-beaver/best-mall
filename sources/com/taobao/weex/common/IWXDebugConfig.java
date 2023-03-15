package com.taobao.weex.common;

import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXDebugJsBridge;

public interface IWXDebugConfig {
    WXDebugJsBridge getWXDebugJsBridge();

    WXBridgeManager getWXJSManager();
}
