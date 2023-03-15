package com.taobao.weex.ui.action;

import android.util.Log;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;

public class ActionReloadPage implements IExecutable {
    private final String TAG = "ReloadPageAction";
    private String mPageId;
    private boolean mReloadThis;

    public ActionReloadPage(String str, boolean z) {
        this.mPageId = str;
        this.mReloadThis = z;
    }

    public void executeAction() {
        WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getWXRenderManager().getWXSDKInstance(this.mPageId);
        if (wXSDKInstance != null) {
            wXSDKInstance.reloadPage(this.mReloadThis);
        } else {
            Log.e("ReloadPageAction", "ReloadPageAction executeDom reloadPage instance is null");
        }
    }
}
