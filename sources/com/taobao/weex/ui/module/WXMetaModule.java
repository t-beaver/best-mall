package com.taobao.weex.ui.module;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcloud.android.widget.toast.ToastCompat;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.weex.WeexInstanceMgr;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

public class WXMetaModule extends WXModule {
    public static final String DEVICE_WIDTH = "device-width";
    public static final String WIDTH = "width";

    @JSMethod(uiThread = false)
    public void setViewport(String str) {
        if (WeexInstanceMgr.self().getComplier().equalsIgnoreCase("weex") && !TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(URLDecoder.decode(str, "utf-8"));
                Context context = this.mWXSDKInstance.getContext();
                if (DEVICE_WIDTH.endsWith(parseObject.getString("width"))) {
                    int screenWidth = (int) (((float) WXViewUtils.getScreenWidth(context)) / WXViewUtils.getScreenDensity(context));
                    this.mWXSDKInstance.setInstanceViewPortWidth((float) screenWidth);
                    WXLogUtils.d("[WXMetaModule] setViewport success[device-width]=" + screenWidth);
                    return;
                }
                int intValue = parseObject.getInteger("width").intValue();
                if (intValue > 0) {
                    this.mWXSDKInstance.setInstanceViewPortWidth((float) intValue);
                }
                WXLogUtils.d("[WXMetaModule] setViewport success[width]=" + intValue);
            } catch (Exception e) {
                WXLogUtils.e("[WXMetaModule] alert param parse error ", (Throwable) e);
            }
        }
    }

    @JSMethod(uiThread = false)
    public float getViewPort() {
        return this.mWXSDKInstance.getInstanceViewPortWidthWithFloat();
    }

    @JSMethod(uiThread = true)
    public void openLog(String str) {
        Application application = WXEnvironment.getApplication();
        if (application != null && (application.getApplicationInfo().flags & 2) != 0) {
            if (WXUtils.getBoolean(str, true).booleanValue()) {
                WXEnvironment.setApkDebugable(true);
                if (this.mWXSDKInstance != null) {
                    ToastCompat.makeText(this.mWXSDKInstance.getContext(), (CharSequence) "log open success", 0).show();
                    return;
                }
                return;
            }
            WXEnvironment.setApkDebugable(false);
            if (this.mWXSDKInstance != null) {
                ToastCompat.makeText(this.mWXSDKInstance.getContext(), (CharSequence) "log close success", 0).show();
            }
        }
    }

    @JSMethod(uiThread = false)
    public void getPageInfo(JSCallback jSCallback) {
        if (jSCallback != null) {
            List<WXSDKInstance> allInstances = WXSDKManager.getInstance().getWXRenderManager().getAllInstances();
            HashMap hashMap = new HashMap(4);
            for (WXSDKInstance next : allInstances) {
                if (!TextUtils.isEmpty(next.getBundleUrl())) {
                    hashMap.put(next.getBundleUrl(), next.getTemplateInfo());
                }
            }
            jSCallback.invoke(hashMap);
        }
    }
}
