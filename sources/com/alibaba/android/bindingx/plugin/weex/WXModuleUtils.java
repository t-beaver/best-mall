package com.alibaba.android.bindingx.plugin.weex;

import android.view.View;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.WXComponent;

public class WXModuleUtils {
    private WXModuleUtils() {
    }

    public static View findViewByRef(String str, String str2) {
        WXComponent findComponentByRef = findComponentByRef(str, str2);
        if (findComponentByRef == null) {
            return null;
        }
        return findComponentByRef.getHostView();
    }

    public static WXComponent findComponentByRef(String str, String str2) {
        return WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
    }
}
