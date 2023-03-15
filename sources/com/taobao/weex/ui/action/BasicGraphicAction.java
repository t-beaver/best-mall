package com.taobao.weex.ui.action;

import android.text.TextUtils;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;

public abstract class BasicGraphicAction implements IExecutable, Runnable {
    public static final int ActionTypeBatchBegin = 1;
    public static final int ActionTypeBatchEnd = 2;
    public static final int ActionTypeNormal = 0;
    public int mActionType = 0;
    private WXSDKInstance mInstance;
    public boolean mIsRunByBatch = false;
    private final String mRef;

    public BasicGraphicAction(WXSDKInstance wXSDKInstance, String str) {
        this.mInstance = wXSDKInstance;
        this.mRef = str;
    }

    public final WXSDKInstance getWXSDKIntance() {
        return this.mInstance;
    }

    public final String getPageId() {
        WXSDKInstance wXSDKInstance = this.mInstance;
        if (wXSDKInstance != null) {
            return wXSDKInstance.getInstanceId();
        }
        return null;
    }

    public final String getRef() {
        return this.mRef;
    }

    public void executeActionOnRender() {
        WXSDKInstance wXSDKInstance = this.mInstance;
        if (wXSDKInstance == null) {
            return;
        }
        if (TextUtils.isEmpty(wXSDKInstance.getInstanceId())) {
            WXLogUtils.e("[BasicGraphicAction] pageId can not be null");
            if (WXEnvironment.isApkDebugable()) {
                throw new RuntimeException(Operators.ARRAY_START_STR + getClass().getName() + "] pageId can not be null");
            }
            return;
        }
        WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(this.mInstance.getInstanceId(), this);
    }

    public void run() {
        try {
            executeAction();
        } catch (Throwable th) {
            if (!WXEnvironment.isApkDebugable()) {
                WXLogUtils.w("BasicGraphicAction", th);
                return;
            }
            WXLogUtils.e("BasicGraphicAction", "SafeRunnable run throw expection:" + th.getMessage());
            throw th;
        }
    }
}
