package com.taobao.weex.ui.module;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXHashMap;
import com.taobao.weex.bridge.WXJSObject;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.WXJsonUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayList;
import java.util.HashMap;

public class WXTimerModule extends WXModule implements Destroyable, Handler.Callback {
    private static final String TAG = "timer";
    private SparseArray<Integer> antiIntAutoBoxing = new SparseArray<>();
    private Handler handler = new Handler(WXBridgeManager.getInstance().getJSLooper(), this);

    @JSMethod(uiThread = false)
    public void setTimeout(int i, float f) {
        if (this.mWXSDKInstance != null) {
            postOrHoldMessage(11, i, (int) f, WXUtils.parseInt(this.mWXSDKInstance.getInstanceId()));
            if (this.mWXSDKInstance.getWXPerformance() != null) {
                this.mWXSDKInstance.getWXPerformance().timerInvokeCount++;
            }
            this.mWXSDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_TIMER_NUM, 1.0d);
        }
    }

    @JSMethod(uiThread = false)
    public void setInterval(int i, float f) {
        if (this.mWXSDKInstance != null) {
            postOrHoldMessage(12, i, (int) f, WXUtils.parseInt(this.mWXSDKInstance.getInstanceId()));
            if (this.mWXSDKInstance.getWXPerformance() != null) {
                this.mWXSDKInstance.getWXPerformance().timerInvokeCount++;
            }
            this.mWXSDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_TIMER_NUM, 1.0d);
        }
    }

    @JSMethod(uiThread = false)
    public void clearTimeout(int i) {
        if (i > 0) {
            removeOrHoldMessage(11, i);
        }
    }

    @JSMethod(uiThread = false)
    public void clearInterval(int i) {
        if (i > 0) {
            removeOrHoldMessage(12, i);
        }
    }

    public void destroy() {
        if (this.handler != null) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d(TAG, "Timer Module removeAllMessages: ");
            }
            this.handler.removeCallbacksAndMessages((Object) null);
            this.antiIntAutoBoxing.clear();
        }
    }

    public boolean handleMessage(Message message) {
        if (message == null) {
            return false;
        }
        int i = message.what;
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d(TAG, "Timer Module handleMessage : " + message.what);
        }
        if (i != 11) {
            if (i != 12 || message.obj == null) {
                return false;
            }
            checkIfTimerInBack(message.arg1);
            postMessage(12, ((Integer) message.obj).intValue(), message.arg2, message.arg1);
            WXBridgeManager.getInstance().invokeExecJS(String.valueOf(message.arg1), (String) null, WXBridgeManager.METHOD_CALL_JS, createTimerArgs(message.arg1, ((Integer) message.obj).intValue(), true), true);
        } else if (message.obj == null) {
            return false;
        } else {
            checkIfTimerInBack(message.arg1);
            WXBridgeManager.getInstance().invokeExecJS(String.valueOf(message.arg1), (String) null, WXBridgeManager.METHOD_CALL_JS, createTimerArgs(message.arg1, ((Integer) message.obj).intValue(), false), true);
        }
        return true;
    }

    private void checkIfTimerInBack(int i) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(String.valueOf(i));
        if (sDKInstance != null && sDKInstance.isViewDisAppear()) {
            sDKInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_TIMER_BACK_NUM, 1.0d);
        }
    }

    /* access modifiers changed from: package-private */
    public void setHandler(Handler handler2) {
        this.handler = handler2;
    }

    private WXJSObject[] createTimerArgs(int i, int i2, boolean z) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i2));
        arrayList.add(new HashMap());
        arrayList.add(Boolean.valueOf(z));
        WXHashMap wXHashMap = new WXHashMap();
        wXHashMap.put("method", WXBridgeManager.METHOD_CALLBACK);
        wXHashMap.put("args", arrayList);
        return new WXJSObject[]{new WXJSObject(2, String.valueOf(i)), new WXJSObject(3, WXJsonUtils.fromObjectToJSONString(new Object[]{wXHashMap}))};
    }

    private void postOrHoldMessage(int i, int i2, int i3, int i4) {
        if (this.mWXSDKInstance.isPreRenderMode()) {
            postMessage(i, i2, i3, i4);
        } else {
            postMessage(i, i2, i3, i4);
        }
    }

    private void removeOrHoldMessage(int i, int i2) {
        if (this.mWXSDKInstance.isPreRenderMode()) {
            this.handler.removeMessages(i, this.antiIntAutoBoxing.get(i2, Integer.valueOf(i2)));
        } else {
            this.handler.removeMessages(i, this.antiIntAutoBoxing.get(i2, Integer.valueOf(i2)));
        }
    }

    private void postMessage(int i, int i2, int i3, int i4) {
        if (i3 < 0 || i2 <= 0) {
            WXLogUtils.e(TAG, "interval < 0 or funcId <=0");
            return;
        }
        if (this.antiIntAutoBoxing.get(i2) == null) {
            this.antiIntAutoBoxing.put(i2, Integer.valueOf(i2));
        }
        this.handler.sendMessageDelayed(this.handler.obtainMessage(i, i4, i3, this.antiIntAutoBoxing.get(i2)), (long) i3);
    }
}
