package com.taobao.weex;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import java.util.Map;

public class WXGlobalEventModule extends WXModule {
    @JSMethod
    public void addEventListener(String str, String str2) {
        this.mWXSDKInstance.addEventListener(str, str2);
    }

    public void removeEventListener(String str, String str2) {
        this.mWXSDKInstance.removeEventListener(str, str2);
    }

    @JSMethod
    public void removeEventListener(String str) {
        this.mWXSDKInstance.removeEventListener(str);
    }

    public void addEventListener(String str, String str2, Map<String, Object> map) {
        super.addEventListener(str, str2, map);
        addEventListener(str, str2);
    }
}
