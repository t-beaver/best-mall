package com.taobao.weex.ui.action;

import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.SimpleJSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.list.template.jni.NativeRenderObjectUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.HashMap;

public class ActionGetLayoutDirection extends BasicGraphicAction {
    private final String mCallback;

    public ActionGetLayoutDirection(WXSDKInstance wXSDKInstance, String str, String str2) {
        super(wXSDKInstance, str);
        this.mCallback = str2;
    }

    public void executeAction() {
        WXSDKInstance wXSDKIntance = getWXSDKIntance();
        if (wXSDKIntance != null && !wXSDKIntance.isDestroy()) {
            SimpleJSCallback simpleJSCallback = new SimpleJSCallback(wXSDKIntance.getInstanceId(), this.mCallback);
            if (TextUtils.isEmpty(getRef())) {
                HashMap hashMap = new HashMap();
                hashMap.put("result", false);
                hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, "Illegal parameter");
                simpleJSCallback.invoke(hashMap);
            } else if ("viewport".equalsIgnoreCase(getRef())) {
                callbackViewport(wXSDKIntance, simpleJSCallback);
            } else {
                WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
                if (wXComponent != null) {
                    String str = "ltr";
                    if (wXComponent != null) {
                        int nativeRenderObjectGetLayoutDirectionFromPathNode = NativeRenderObjectUtils.nativeRenderObjectGetLayoutDirectionFromPathNode(wXComponent.getRenderObjectPtr());
                        if (nativeRenderObjectGetLayoutDirectionFromPathNode == 0) {
                            str = "inherit";
                        } else if (nativeRenderObjectGetLayoutDirectionFromPathNode != 1 && nativeRenderObjectGetLayoutDirectionFromPathNode == 2) {
                            str = Constants.Name.RTL;
                        }
                    }
                    simpleJSCallback.invoke(str);
                }
            }
        }
    }

    private void callbackViewport(WXSDKInstance wXSDKInstance, JSCallback jSCallback) {
        if (wXSDKInstance.getContainerView() != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("direction", "ltr");
            hashMap.put("result", true);
            jSCallback.invoke(hashMap);
            return;
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put("result", false);
        hashMap2.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, "Component does not exist");
        jSCallback.invoke(hashMap2);
    }

    private float getWebPxValue(int i, int i2) {
        return WXViewUtils.getWebPxByWidth((float) i, i2);
    }
}
