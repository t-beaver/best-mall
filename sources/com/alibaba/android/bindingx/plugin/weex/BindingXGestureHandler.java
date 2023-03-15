package com.alibaba.android.bindingx.plugin.weex;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.BindingXTouchHandler;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXUtils;
import java.util.Map;

public class BindingXGestureHandler extends BindingXTouchHandler {
    private boolean experimental = false;
    private WXGesture mWeexGestureHandler = null;

    public BindingXGestureHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    public void setGlobalConfig(Map<String, Object> map) {
        super.setGlobalConfig(map);
        if (map != null) {
            this.experimental = WXUtils.getBoolean(map.get("experimentalGestureFeatures"), false).booleanValue();
        }
    }

    public boolean onCreate(String str, String str2) {
        if (!this.experimental) {
            return super.onCreate(str, str2);
        }
        WXComponent findComponentByRef = WXModuleUtils.findComponentByRef(TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId, str);
        if (findComponentByRef == null) {
            return super.onCreate(str, str2);
        }
        View hostView = findComponentByRef.getHostView();
        if (!(hostView instanceof ViewGroup) || !(hostView instanceof WXGestureObservable)) {
            return super.onCreate(str, str2);
        }
        try {
            WXGesture gestureListener = ((WXGestureObservable) hostView).getGestureListener();
            this.mWeexGestureHandler = gestureListener;
            if (gestureListener == null) {
                return super.onCreate(str, str2);
            }
            gestureListener.addOnTouchListener(this);
            LogProxy.d("[ExpressionGestureHandler] onCreate success. {source:" + str + ",type:" + str2 + Operators.BLOCK_END_STR);
            return true;
        } catch (Throwable th) {
            LogProxy.e("experimental gesture features open failed." + th.getMessage());
            return super.onCreate(str, str2);
        }
    }

    public boolean onDisable(String str, String str2) {
        WXGesture wXGesture;
        boolean onDisable = super.onDisable(str, str2);
        if (!this.experimental || (wXGesture = this.mWeexGestureHandler) == null) {
            return onDisable;
        }
        try {
            return onDisable | wXGesture.removeTouchListener(this);
        } catch (Throwable th) {
            LogProxy.e("[ExpressionGestureHandler]  disabled failed." + th.getMessage());
            return onDisable;
        }
    }
}
