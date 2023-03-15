package com.taobao.weex.ui.action;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.SimpleJSCallback;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXViewUtils;
import java.util.HashMap;

public class ActionGetComponentRect extends BasicGraphicAction {
    private final String mCallback;

    public ActionGetComponentRect(WXSDKInstance wXSDKInstance, String str, String str2) {
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
                    HashMap hashMap2 = new HashMap();
                    if (wXComponent != null) {
                        float instanceViewPortWidthWithFloat = wXSDKIntance.getInstanceViewPortWidthWithFloat();
                        HashMap hashMap3 = new HashMap();
                        Rect componentSize = wXComponent.getComponentSize();
                        hashMap3.put("width", Float.valueOf(getWebPxValue(componentSize.width(), instanceViewPortWidthWithFloat)));
                        hashMap3.put("height", Float.valueOf(getWebPxValue(componentSize.height(), instanceViewPortWidthWithFloat)));
                        hashMap3.put("bottom", Float.valueOf(getWebPxValue(componentSize.bottom, instanceViewPortWidthWithFloat)));
                        hashMap3.put("left", Float.valueOf(getWebPxValue(componentSize.left, instanceViewPortWidthWithFloat)));
                        hashMap3.put("right", Float.valueOf(getWebPxValue(componentSize.right, instanceViewPortWidthWithFloat)));
                        hashMap3.put("top", Float.valueOf(getWebPxValue(componentSize.top, instanceViewPortWidthWithFloat)));
                        hashMap2.put(AbsoluteConst.JSON_KEY_SIZE, hashMap3);
                        hashMap2.put("result", true);
                    } else {
                        hashMap2.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, "Component does not exist");
                    }
                    simpleJSCallback.invoke(hashMap2);
                }
            }
        }
    }

    private void callbackViewport(WXSDKInstance wXSDKInstance, JSCallback jSCallback) {
        View containerView = wXSDKInstance.getContainerView();
        if (containerView != null) {
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            wXSDKInstance.getContainerView().getLocationOnScreen(new int[2]);
            float instanceViewPortWidthWithFloat = wXSDKInstance.getInstanceViewPortWidthWithFloat();
            hashMap2.put("left", Float.valueOf(0.0f));
            hashMap2.put("top", Float.valueOf(0.0f));
            hashMap2.put("right", Float.valueOf(getWebPxValue(containerView.getWidth(), instanceViewPortWidthWithFloat)));
            hashMap2.put("bottom", Float.valueOf(getWebPxValue(containerView.getHeight(), instanceViewPortWidthWithFloat)));
            hashMap2.put("width", Float.valueOf(getWebPxValue(containerView.getWidth(), instanceViewPortWidthWithFloat)));
            hashMap2.put("height", Float.valueOf(getWebPxValue(containerView.getHeight(), instanceViewPortWidthWithFloat)));
            hashMap.put(AbsoluteConst.JSON_KEY_SIZE, hashMap2);
            hashMap.put("result", true);
            jSCallback.invoke(hashMap);
            return;
        }
        HashMap hashMap3 = new HashMap();
        hashMap3.put("result", false);
        hashMap3.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, "Component does not exist");
        jSCallback.invoke(hashMap3);
    }

    private float getWebPxValue(int i, float f) {
        return WXViewUtils.getWebPxByWidth((float) i, f);
    }
}
