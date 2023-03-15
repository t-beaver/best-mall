package com.taobao.weex.common;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;

public class WXInstanceWrap extends WXModule {
    @JSMethod
    public void error(String str, String str2, String str3) {
        if (this.mWXSDKInstance != null) {
            WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
            if (str3 != null && str3.contains("downgrade_to_root")) {
                while (wXSDKInstance.getParentInstance() != null) {
                    wXSDKInstance = wXSDKInstance.getParentInstance();
                }
            }
            wXSDKInstance.onRenderError(str + "|" + str2, str3);
        }
    }
}
