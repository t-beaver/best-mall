package com.taobao.weex.bridge;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.IWXBridge;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXJsonUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.TimeCalculator;
import io.dcloud.feature.internal.sdk.SDK;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WXBridge implements IWXBridge {
    public static final boolean MULTIPROCESS = true;
    public static final String TAG = "WXBridge";

    private native void nativeBindMeasurementToRenderObject(long j);

    private native String nativeDecrypt(String str, String str2, String str3, String str4);

    private native String nativeEncrypt(String str, String str2, String str3, String str4);

    private native int nativeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    private native int nativeExecJSService(String str);

    private native void nativeForceLayout(String str);

    private native int nativeInitFramework(String str, WXParams wXParams);

    private native int nativeInitFrameworkEnv(String str, WXParams wXParams, String str2, boolean z);

    private native void nativeMarkDirty(String str, String str2, boolean z);

    private native boolean nativeNotifyLayout(String str);

    private native void nativeOnInstanceClose(String str);

    private native String nativePreGetClientKey(String str, String str2, String str3);

    private native void nativeRefreshInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    private native void nativeRegisterCoreEnv(String str, String str2);

    private native void nativeReloadPageLayout(String str);

    private native void nativeRemoveInstanceRenderType(String str);

    private native void nativeResetWXBridge(Object obj, String str);

    private native void nativeSetDefaultHeightAndWidthIntoRootDom(String str, float f, float f2, boolean z, boolean z2);

    private native void nativeSetDeviceDisplay(String str, float f, float f2, float f3);

    private native void nativeSetDeviceDisplayOfPage(String str, float f, float f2);

    private native void nativeSetFlexDirectionDef(String str);

    private native void nativeSetInstanceRenderType(String str, String str2);

    private native void nativeSetLogType(float f, float f2);

    private native void nativeSetMargin(String str, String str2, int i, float f);

    private native void nativeSetPadding(String str, String str2, int i, float f);

    private native void nativeSetPageArgument(String str, String str2, String str3);

    private native void nativeSetPosition(String str, String str2, int i, float f);

    private native void nativeSetRenderContainerWrapContent(boolean z, String str);

    private native void nativeSetStyleHeight(String str, String str2, float f, boolean z);

    private native void nativeSetStyleWidth(String str, String str2, float f, boolean z);

    private native void nativeSetViewPortWidth(String str, float f);

    private native void nativeTakeHeapSnapshot(String str);

    private native void nativeUpdateInitFrameworkParams(String str, String str2, String str3);

    private native boolean nativeVerifyClientKeyPayload(String str, String str2, String str3);

    public native int nativeCreateInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    public native int nativeDestoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    public native String nativeDumpIpcPageQueueInfo();

    public native String nativeExecJSOnInstance(String str, String str2, int i);

    public native void nativeExecJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, long j);

    public native byte[] nativeExecJSWithResult(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    public native void nativeFireEventOnDataRenderNode(String str, String str2, String str3, String str4, String str5);

    public native long[] nativeGetFirstScreenRenderTime(String str);

    public native long[] nativeGetRenderFinishTime(String str);

    public native void nativeInvokeCallbackOnDataRender(String str, String str2, String str3, boolean z);

    public native void nativeOnInteractionTimeUpdate(String str);

    public native void nativeRegisterComponentOnDataRenderNode(String str);

    public native void nativeRegisterModuleOnDataRenderNode(String str);

    public native void nativeUpdateGlobalConfig(String str);

    public void updateInitFrameworkParams(String str, String str2, String str3) {
        WXStateRecord.getInstance().recordAction("", "updateInitFrameworkParams:");
        nativeUpdateInitFrameworkParams(str, str2, str3);
    }

    public void setLogType(float f, boolean z) {
        Log.e("WeexCore", "setLog" + WXEnvironment.sLogLevel.getValue() + "isPerf : " + z);
        nativeSetLogType(f, z ? 1.0f : 0.0f);
    }

    public int initFramework(String str, WXParams wXParams) {
        return nativeInitFramework(str, wXParams);
    }

    public int initFrameworkEnv(String str, WXParams wXParams, String str2, boolean z) {
        WXStateRecord.getInstance().recordAction("", "nativeInitFrameworkEnv:");
        return nativeInitFrameworkEnv(str, wXParams, str2, z);
    }

    public void refreshInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "refreshInstance:" + str2 + "," + str3);
        nativeRefreshInstance(str, str2, str3, wXJSObjectArr);
    }

    public int execJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "execJS:" + str2 + "," + str3);
        return nativeExecJS(str, str2, str3, wXJSObjectArr);
    }

    public void execJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, ResultCallback resultCallback) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "execJSWithCallback:" + str2 + "," + str3);
        if (resultCallback == null) {
            execJS(str, str2, str3, wXJSObjectArr);
        }
        nativeExecJSWithCallback(str, str2, str3, wXJSObjectArr, ResultCallbackManager.generateCallbackId(resultCallback));
    }

    public void onNativePerformanceDataUpdate(String str, Map<String, String> map) {
        WXSDKInstance wXSDKInstance;
        if (!TextUtils.isEmpty(str) && map != null && map.size() >= 1 && (wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str)) != null && wXSDKInstance.getApmForInstance() != null) {
            wXSDKInstance.getApmForInstance().updateNativePerformanceData(map);
        }
    }

    public void onReceivedResult(long j, byte[] bArr) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction("onReceivedResult", SDK.UNIMP_EVENT_CALLBACKID + j);
        ResultCallback removeCallbackById = ResultCallbackManager.removeCallbackById(j);
        if (removeCallbackById != null) {
            removeCallbackById.onReceiveResult(bArr);
        }
    }

    public int execJSService(String str) {
        WXStateRecord.getInstance().recordAction("execJSService", "execJSService:");
        return nativeExecJSService(str);
    }

    public void takeHeapSnapshot(String str) {
        nativeTakeHeapSnapshot(str);
    }

    public int createInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        Log.e(TimeCalculator.TIMELINE_TAG, "createInstance :" + System.currentTimeMillis());
        WXStateRecord.getInstance().recordAction(str, "createInstanceContext:");
        return nativeCreateInstanceContext(str, str2, str3, wXJSObjectArr);
    }

    public int destoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXStateRecord.getInstance().recordAction(str, "destoryInstance:");
        return nativeDestoryInstance(str, str2, str3, wXJSObjectArr);
    }

    public String execJSOnInstance(String str, String str2, int i) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "execJSOnInstance:" + i);
        return nativeExecJSOnInstance(str, str2, i);
    }

    public int callNative(String str, byte[] bArr, String str2) {
        if (!"HeartBeat".equals(str2)) {
            return callNative(str, JSON.parseArray(new String(bArr)), str2);
        }
        Log.e("HeartBeat instanceId", str);
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return 1;
        }
        sDKInstance.createInstanceFuncHeartBeat();
        return 1;
    }

    public int callNative(String str, String str2, String str3) {
        try {
            return callNative(str, JSONArray.parseArray(str2), str3);
        } catch (Exception e) {
            WXLogUtils.e(TAG, "callNative throw exception: " + WXLogUtils.getStackTrace(e));
            return 1;
        }
    }

    private int callNative(String str, JSONArray jSONArray, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            sDKInstance.firstScreenCreateInstanceTime(currentTimeMillis);
        }
        int i = 1;
        try {
            i = WXBridgeManager.getInstance().callNative(str, jSONArray, str2);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callNative throw exception:" + WXLogUtils.getStackTrace(th));
        }
        if (WXEnvironment.isApkDebugable() && i == -1) {
            WXLogUtils.w("destroyInstance :" + str + " JSF must stop callNative");
        }
        return i;
    }

    public void reportJSException(String str, String str2, String str3) {
        WXBridgeManager.getInstance().reportJSException(str, str2, str3);
    }

    public Object callVueExecSync(String str, String str2, String str3) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return new WXJSObject((Object) null);
        }
        if (sDKInstance != null && sDKInstance.isDestroy()) {
            return new WXJSObject((Object) null);
        }
        String execSync = WXSDKManager.getInstance().getVueBridgeAdapter().execSync(sDKInstance, str2, str3);
        if (execSync == null) {
            return new WXJSObject((Object) null);
        }
        return new WXJSObject(3, WXJsonUtils.fromObjectToJSONString(execSync));
    }

    public void callVueExec(String str, final String str2, final String str3) {
        final WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return;
        }
        if (sDKInstance == null || !sDKInstance.isDestroy()) {
            WXSDKManager.getInstance().postOnUiThread(new Runnable() {
                public void run() {
                    WXSDKManager.getInstance().getVueBridgeAdapter().exec(sDKInstance, str2, str3);
                }
            }, 0);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r1 = (com.alibaba.fastjson.JSONArray) com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0114, code lost:
        return com.taobao.weex.utils.WXWsonJSONSwitch.toWsonOrJsonWXJSObject(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x011a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x011b, code lost:
        com.taobao.weex.utils.WXLogUtils.e(TAG, (java.lang.Throwable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0125, code lost:
        return new com.taobao.weex.bridge.WXJSObject((java.lang.Object) null);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0058 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:54:0x0110 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object callNativeModule(java.lang.String r14, java.lang.String r15, java.lang.String r16, byte[] r17, byte[] r18) {
        /*
            r13 = this;
            r0 = r14
            r1 = r17
            java.lang.String r2 = "__weex_options__"
            r7 = 0
            com.taobao.weex.performance.WXStateRecord r3 = com.taobao.weex.performance.WXStateRecord.getInstance()     // Catch:{ Exception -> 0x011a }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x011a }
            r4.<init>()     // Catch:{ Exception -> 0x011a }
            java.lang.String r5 = "callNativeModule:"
            r4.append(r5)     // Catch:{ Exception -> 0x011a }
            r5 = r15
            r4.append(r15)     // Catch:{ Exception -> 0x011a }
            java.lang.String r6 = "."
            r4.append(r6)     // Catch:{ Exception -> 0x011a }
            r6 = r16
            r4.append(r6)     // Catch:{ Exception -> 0x011a }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x011a }
            r3.recordAction(r14, r4)     // Catch:{ Exception -> 0x011a }
            long r8 = com.taobao.weex.utils.WXUtils.getFixUnixTime()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.WXSDKInstance r10 = r3.getSDKInstance(r14)     // Catch:{ Exception -> 0x011a }
            if (r1 == 0) goto L_0x0067
            if (r10 == 0) goto L_0x005f
            com.taobao.weex.common.WXRenderStrategy r3 = r10.getRenderStrategy()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.common.WXRenderStrategy r4 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Exception -> 0x011a }
            if (r3 == r4) goto L_0x0049
            com.taobao.weex.common.WXRenderStrategy r3 = r10.getRenderStrategy()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.common.WXRenderStrategy r4 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Exception -> 0x011a }
            if (r3 != r4) goto L_0x005f
        L_0x0049:
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x0058 }
            java.lang.String r4 = "UTF-8"
            r3.<init>(r1, r4)     // Catch:{ Exception -> 0x0058 }
            java.lang.Object r3 = com.alibaba.fastjson.JSON.parse(r3)     // Catch:{ Exception -> 0x0058 }
            com.alibaba.fastjson.JSONArray r3 = (com.alibaba.fastjson.JSONArray) r3     // Catch:{ Exception -> 0x0058 }
            r11 = r3
            goto L_0x0068
        L_0x0058:
            java.lang.Object r1 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r17)     // Catch:{ Exception -> 0x011a }
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x011a }
            goto L_0x0065
        L_0x005f:
            java.lang.Object r1 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r17)     // Catch:{ Exception -> 0x011a }
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x011a }
        L_0x0065:
            r11 = r1
            goto L_0x0068
        L_0x0067:
            r11 = r7
        L_0x0068:
            if (r18 == 0) goto L_0x0072
            java.lang.Object r1 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r18)     // Catch:{ Exception -> 0x011a }
            com.alibaba.fastjson.JSONObject r1 = (com.alibaba.fastjson.JSONObject) r1     // Catch:{ Exception -> 0x011a }
            r12 = r1
            goto L_0x00b4
        L_0x0072:
            if (r11 == 0) goto L_0x00b3
            com.taobao.weex.WXSDKManager r1 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.WXSDKInstance r1 = r1.getSDKInstance(r14)     // Catch:{ Exception -> 0x011a }
            if (r1 == 0) goto L_0x00b3
            com.taobao.weex.bridge.WXBridgeManager$BundType r3 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.bridge.WXBridgeManager$BundType r1 = r1.bundleType     // Catch:{ Exception -> 0x011a }
            boolean r1 = r3.equals(r1)     // Catch:{ Exception -> 0x011a }
            if (r1 == 0) goto L_0x00b3
            java.util.Iterator r1 = r11.iterator()     // Catch:{ Exception -> 0x011a }
            r3 = r7
        L_0x008d:
            boolean r4 = r1.hasNext()     // Catch:{ Exception -> 0x011a }
            if (r4 == 0) goto L_0x00ab
            java.lang.Object r4 = r1.next()     // Catch:{ Exception -> 0x011a }
            boolean r12 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x011a }
            if (r12 == 0) goto L_0x008d
            r12 = r4
            com.alibaba.fastjson.JSONObject r12 = (com.alibaba.fastjson.JSONObject) r12     // Catch:{ Exception -> 0x011a }
            boolean r12 = r12.containsKey(r2)     // Catch:{ Exception -> 0x011a }
            if (r12 == 0) goto L_0x008d
            com.alibaba.fastjson.JSONObject r4 = (com.alibaba.fastjson.JSONObject) r4     // Catch:{ Exception -> 0x011a }
            java.lang.Object r3 = r4.get(r2)     // Catch:{ Exception -> 0x011a }
            goto L_0x008d
        L_0x00ab:
            boolean r1 = r3 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x011a }
            if (r1 == 0) goto L_0x00b3
            com.alibaba.fastjson.JSONObject r3 = (com.alibaba.fastjson.JSONObject) r3     // Catch:{ Exception -> 0x011a }
            r12 = r3
            goto L_0x00b4
        L_0x00b3:
            r12 = r7
        L_0x00b4:
            com.taobao.weex.bridge.WXBridgeManager r1 = com.taobao.weex.bridge.WXBridgeManager.getInstance()     // Catch:{ Exception -> 0x011a }
            r2 = r14
            r3 = r15
            r4 = r16
            r5 = r11
            r6 = r12
            java.lang.Object r0 = r1.callNativeModule((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4, (com.alibaba.fastjson.JSONArray) r5, (com.alibaba.fastjson.JSONObject) r6)     // Catch:{ Exception -> 0x011a }
            if (r10 == 0) goto L_0x00e0
            com.taobao.weex.performance.WXInstanceApm r1 = r10.getApmForInstance()     // Catch:{ Exception -> 0x011a }
            java.lang.String r2 = "wxFSCallNativeTotalNum"
            r3 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r1.updateFSDiffStats(r2, r3)     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.performance.WXInstanceApm r1 = r10.getApmForInstance()     // Catch:{ Exception -> 0x011a }
            java.lang.String r2 = "wxFSCallNativeTotalTime"
            long r3 = com.taobao.weex.utils.WXUtils.getFixUnixTime()     // Catch:{ Exception -> 0x011a }
            long r3 = r3 - r8
            double r3 = (double) r3     // Catch:{ Exception -> 0x011a }
            r1.updateFSDiffStats(r2, r3)     // Catch:{ Exception -> 0x011a }
        L_0x00e0:
            if (r10 == 0) goto L_0x0115
            com.taobao.weex.common.WXRenderStrategy r1 = r10.getRenderStrategy()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Exception -> 0x011a }
            if (r1 == r2) goto L_0x00f2
            com.taobao.weex.common.WXRenderStrategy r1 = r10.getRenderStrategy()     // Catch:{ Exception -> 0x011a }
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Exception -> 0x011a }
            if (r1 != r2) goto L_0x0115
        L_0x00f2:
            if (r0 != 0) goto L_0x00fa
            com.taobao.weex.bridge.WXJSObject r1 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Exception -> 0x0110 }
            r1.<init>(r7)     // Catch:{ Exception -> 0x0110 }
            return r1
        L_0x00fa:
            java.lang.Class r1 = r0.getClass()     // Catch:{ Exception -> 0x0110 }
            java.lang.Class<com.taobao.weex.bridge.WXJSObject> r2 = com.taobao.weex.bridge.WXJSObject.class
            if (r1 != r2) goto L_0x0105
            com.taobao.weex.bridge.WXJSObject r0 = (com.taobao.weex.bridge.WXJSObject) r0     // Catch:{ Exception -> 0x0110 }
            return r0
        L_0x0105:
            com.taobao.weex.bridge.WXJSObject r1 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Exception -> 0x0110 }
            r2 = 3
            java.lang.String r3 = com.taobao.weex.utils.WXJsonUtils.fromObjectToJSONString(r0)     // Catch:{ Exception -> 0x0110 }
            r1.<init>(r2, r3)     // Catch:{ Exception -> 0x0110 }
            return r1
        L_0x0110:
            com.taobao.weex.bridge.WXJSObject r0 = com.taobao.weex.utils.WXWsonJSONSwitch.toWsonOrJsonWXJSObject(r0)     // Catch:{ Exception -> 0x011a }
            return r0
        L_0x0115:
            com.taobao.weex.bridge.WXJSObject r0 = com.taobao.weex.utils.WXWsonJSONSwitch.toWsonOrJsonWXJSObject(r0)     // Catch:{ Exception -> 0x011a }
            return r0
        L_0x011a:
            r0 = move-exception
            java.lang.String r1 = "WXBridge"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r0)
            com.taobao.weex.bridge.WXJSObject r0 = new com.taobao.weex.bridge.WXJSObject
            r0.<init>(r7)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridge.callNativeModule(java.lang.String, java.lang.String, java.lang.String, byte[], byte[]):java.lang.Object");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:9|10|11|12) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0044 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void callNativeComponent(java.lang.String r7, java.lang.String r8, java.lang.String r9, byte[] r10, byte[] r11) {
        /*
            r6 = this;
            com.taobao.weex.performance.WXStateRecord r0 = com.taobao.weex.performance.WXStateRecord.getInstance()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "callNativeComponent:"
            r1.append(r2)
            r1.append(r9)
            java.lang.String r1 = r1.toString()
            r0.recordAction(r7, r1)
            com.taobao.weex.WXSDKManager r0 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.WXSDKInstance r0 = r0.getSDKInstance(r7)     // Catch:{ Exception -> 0x0063 }
            r1 = 0
            if (r10 == 0) goto L_0x0053
            if (r0 == 0) goto L_0x004c
            com.taobao.weex.common.WXRenderStrategy r1 = r0.getRenderStrategy()     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Exception -> 0x0063 }
            if (r1 == r2) goto L_0x0035
            com.taobao.weex.common.WXRenderStrategy r0 = r0.getRenderStrategy()     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Exception -> 0x0063 }
            if (r0 != r1) goto L_0x004c
        L_0x0035:
            java.lang.String r0 = new java.lang.String     // Catch:{ Exception -> 0x0044 }
            java.lang.String r1 = "UTF-8"
            r0.<init>(r10, r1)     // Catch:{ Exception -> 0x0044 }
            java.lang.Object r0 = com.alibaba.fastjson.JSON.parse(r0)     // Catch:{ Exception -> 0x0044 }
            r1 = r0
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x0044 }
            goto L_0x0053
        L_0x0044:
            java.lang.Object r10 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r10)     // Catch:{ Exception -> 0x0063 }
            r1 = r10
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x0063 }
            goto L_0x0053
        L_0x004c:
            java.lang.Object r10 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r10)     // Catch:{ Exception -> 0x0063 }
            r1 = r10
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x0063 }
        L_0x0053:
            r4 = r1
            java.lang.Object r5 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r11)     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()     // Catch:{ Exception -> 0x0063 }
            r1 = r7
            r2 = r8
            r3 = r9
            r0.callNativeComponent(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0063 }
            goto L_0x0069
        L_0x0063:
            r7 = move-exception
            java.lang.String r8 = "WXBridge"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r8, (java.lang.Throwable) r7)
        L_0x0069:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridge.callNativeComponent(java.lang.String, java.lang.String, java.lang.String, byte[], byte[]):void");
    }

    public void setTimeoutNative(String str, String str2) {
        WXBridgeManager.getInstance().setTimeout(str, str2);
    }

    public void setJSFrmVersion(String str) {
        if (str != null) {
            WXEnvironment.JS_LIB_SDK_VERSION = str;
        }
        WXStateRecord.getInstance().onJSFMInit();
    }

    public void resetWXBridge(boolean z) {
        nativeResetWXBridge(this, getClass().getName().replace(Operators.DOT, '/'));
    }

    public int callUpdateFinish(String str, byte[] bArr, String str2) {
        try {
            return WXBridgeManager.getInstance().callUpdateFinish(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callCreateBody throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callRefreshFinish(String str, byte[] bArr, String str2) {
        try {
            return WXBridgeManager.getInstance().callRefreshFinish(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public void reportServerCrash(String str, String str2) {
        WXLogUtils.e(TAG, "reportServerCrash instanceId:" + str + " crashFile: " + str2);
        try {
            WXBridgeManager.getInstance().callReportCrashReloadPage(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "reloadPageNative throw exception:" + WXLogUtils.getStackTrace(th));
            }
        }
    }

    public int callCreateBody(String str, String str2, String str3, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3) {
        try {
            return WXBridgeManager.getInstance().callCreateBody(str, str2, str3, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callCreateBody throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callAddElement(String str, String str2, String str3, int i, String str4, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3, boolean z) {
        try {
            return WXBridgeManager.getInstance().callAddElement(str, str2, str3, i, str4, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3, z);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                th.printStackTrace();
                WXLogUtils.e(TAG, "callAddElement throw error:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callRemoveElement(String str, String str2) {
        try {
            return WXBridgeManager.getInstance().callRemoveElement(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callRemoveElement throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callMoveElement(String str, String str2, String str3, int i) {
        try {
            return WXBridgeManager.getInstance().callMoveElement(str, str2, str3, i);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callMoveElement throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callAddEvent(String str, String str2, String str3) {
        try {
            return WXBridgeManager.getInstance().callAddEvent(str, str2, str3);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callAddEvent throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    public void callBacthStart(String str) {
        try {
            WXBridgeManager.getInstance().callBacthStart(str);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "cal\n    return errorCode;\n  }lAddEvent throw exception:" + WXLogUtils.getStackTrace(th));
        }
    }

    public void callBacthEnd(String str) {
        try {
            WXBridgeManager.getInstance().callBacthEnd(str);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "cal\n    return errorCode;\n  }lAddEvent throw exception:" + WXLogUtils.getStackTrace(th));
        }
    }

    public int callRemoveEvent(String str, String str2, String str3) {
        try {
            return WXBridgeManager.getInstance().callRemoveEvent(str, str2, str3);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callRemoveEvent throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callUpdateStyle(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3, HashMap<String, String> hashMap4) {
        if (hashMap != null) {
            try {
                if (hashMap.isEmpty() && hashMap2 != null && hashMap2.isEmpty() && hashMap3 != null && hashMap3.isEmpty() && hashMap4 != null && hashMap4.isEmpty()) {
                    return 1;
                }
            } catch (Throwable th) {
                if (!WXEnvironment.isApkDebugable()) {
                    return 1;
                }
                WXLogUtils.e(TAG, "callUpdateStyle throw exception:" + WXLogUtils.getStackTrace(th));
                return 1;
            }
        }
        return WXBridgeManager.getInstance().callUpdateStyle(str, str2, hashMap, hashMap2, hashMap3, hashMap4);
    }

    public int callUpdateAttrs(String str, String str2, HashMap<String, String> hashMap) {
        try {
            return WXBridgeManager.getInstance().callUpdateAttrs(str, str2, hashMap);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateAttr throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callAddChildToRichtext(String str, String str2, String str3, String str4, String str5, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        try {
            return WXBridgeManager.getInstance().callAddChildToRichtext(str, str2, str3, str4, str5, hashMap, hashMap2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callAddChildToRichtext throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callRemoveChildFromRichtext(String str, String str2, String str3, String str4) {
        try {
            return WXBridgeManager.getInstance().callRemoveChildFromRichtext(str, str2, str3, str4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callRemoveChildFromRichtext throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callUpdateRichtextStyle(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        try {
            return WXBridgeManager.getInstance().callUpdateRichtextStyle(str, str2, hashMap, str3, str4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateRichtextStyle throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callUpdateRichtextChildAttr(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        try {
            return WXBridgeManager.getInstance().callUpdateRichtextChildAttr(str, str2, hashMap, str3, str4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateRichtextChildAttr throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callLayout(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, boolean z, int i7) {
        try {
            return WXBridgeManager.getInstance().callLayout(str, str2, i, i2, i3, i4, i5, i6, z, i7);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callLayout throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public int callCreateFinish(String str) {
        try {
            return WXBridgeManager.getInstance().callCreateFinish(str);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    public int callRenderSuccess(String str) {
        try {
            return WXBridgeManager.getInstance().callRenderSuccess(str);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    public int callAppendTreeCreateFinish(String str, String str2) {
        try {
            return WXBridgeManager.getInstance().callAppendTreeCreateFinish(str, str2);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callAppendTreeCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    public int callHasTransitionPros(String str, String str2, HashMap<String, String> hashMap) {
        try {
            return WXBridgeManager.getInstance().callHasTransitionPros(str, str2, hashMap);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callHasTransitionPros throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    public ContentBoxMeasurement getMeasurementFunc(String str, long j) {
        try {
            return WXBridgeManager.getInstance().getMeasurementFunc(str, j);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "getMeasurementFunc throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return null;
        }
    }

    public void bindMeasurementToRenderObject(long j) {
        nativeBindMeasurementToRenderObject(j);
    }

    public void setRenderContainerWrapContent(boolean z, String str) {
        nativeSetRenderContainerWrapContent(z, str);
    }

    public long[] getFirstScreenRenderTime(String str) {
        return nativeGetFirstScreenRenderTime(str);
    }

    public long[] getRenderFinishTime(String str) {
        return nativeGetRenderFinishTime(str);
    }

    public void setDefaultHeightAndWidthIntoRootDom(String str, float f, float f2, boolean z, boolean z2) {
        nativeSetDefaultHeightAndWidthIntoRootDom(str, f, f2, z, z2);
    }

    public void onInstanceClose(String str) {
        nativeOnInstanceClose(str);
    }

    public void forceLayout(String str) {
        nativeForceLayout(str);
    }

    public boolean notifyLayout(String str) {
        return nativeNotifyLayout(str);
    }

    public void setStyleWidth(String str, String str2, float f, boolean z) {
        nativeSetStyleWidth(str, str2, f, z);
    }

    public void setMargin(String str, String str2, CSSShorthand.EDGE edge, float f) {
        nativeSetMargin(str, str2, edge.ordinal(), f);
    }

    public void setPadding(String str, String str2, CSSShorthand.EDGE edge, float f) {
        nativeSetPadding(str, str2, edge.ordinal(), f);
    }

    public void setPosition(String str, String str2, CSSShorthand.EDGE edge, float f) {
        nativeSetPosition(str, str2, edge.ordinal(), f);
    }

    public void markDirty(String str, String str2, boolean z) {
        nativeMarkDirty(str, str2, z);
    }

    public void setDeviceDisplay(String str, float f, float f2, float f3) {
        nativeSetDeviceDisplay(str, f, f2, f3);
    }

    public void setStyleHeight(String str, String str2, float f, boolean z) {
        nativeSetStyleHeight(str, str2, f, z);
    }

    public void setStyleWidth(String str, String str2, float f) {
        nativeSetStyleWidth(str, str2, f, false);
    }

    public void setStyleHeight(String str, String str2, float f) {
        nativeSetStyleHeight(str, str2, f, false);
    }

    public void setInstanceRenderType(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            nativeSetInstanceRenderType(str, str2);
        }
    }

    public void setViewPortWidth(String str, float f) {
        nativeSetViewPortWidth(str, f);
    }

    public void setFlexDirectionDef(String str) {
        nativeSetFlexDirectionDef(str);
    }

    public void removeInstanceRenderType(String str) {
        nativeRemoveInstanceRenderType(str);
    }

    public void setPageArgument(String str, String str2, String str3) {
        nativeSetPageArgument(str, str2, str3);
    }

    public void registerCoreEnv(String str, String str2) {
        nativeRegisterCoreEnv(str, str2);
    }

    public void reportNativeInitStatus(String str, String str2) {
        if (WXErrorCode.WX_JS_FRAMEWORK_INIT_SINGLE_PROCESS_SUCCESS.getErrorCode().equals(str) || WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED.getErrorCode().equals(str)) {
            IWXUserTrackAdapter iWXUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
            if (iWXUserTrackAdapter != null) {
                HashMap hashMap = new HashMap(3);
                hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_CODE, str);
                hashMap.put(IWXUserTrackAdapter.MONITOR_ARG, "InitFrameworkNativeError");
                hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, str2);
                WXLogUtils.e("reportNativeInitStatus is running and errorCode is " + str + " And errorMsg is " + str2);
                iWXUserTrackAdapter.commit((Context) null, (String) null, IWXUserTrackAdapter.INIT_FRAMEWORK, (WXPerformance) null, hashMap);
            }
        } else if (WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED_PARAMS_NULL.getErrorCode().equals(str)) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED_PARAMS_NULL;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "WeexProxy::initFromParam()", WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED_PARAMS_NULL.getErrorMsg() + ": " + str2, (Map<String, String>) null);
        } else {
            WXErrorCode[] values = WXErrorCode.values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                WXErrorCode wXErrorCode2 = values[i];
                if (!wXErrorCode2.getErrorType().equals(WXErrorCode.ErrorType.NATIVE_ERROR) || !wXErrorCode2.getErrorCode().equals(str)) {
                    i++;
                } else {
                    WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode2, IWXUserTrackAdapter.INIT_FRAMEWORK, str2, (Map<String, String>) null);
                    return;
                }
            }
        }
    }

    public void fireEventOnDataRenderNode(String str, String str2, String str3, String str4, String str5) {
        nativeFireEventOnDataRenderNode(str, str2, str3, str4, str5);
    }

    public void invokeCallbackOnDataRender(String str, String str2, String str3, boolean z) {
        nativeInvokeCallbackOnDataRender(str, str2, str3, z);
    }

    public void registerModuleOnDataRenderNode(String str) {
        nativeRegisterModuleOnDataRenderNode(str);
    }

    public void registerComponentOnDataRenderNode(String str) {
        nativeRegisterComponentOnDataRenderNode(str);
    }

    public void reloadPageLayout(String str) {
        nativeReloadPageLayout(str);
    }

    public void setDeviceDisplayOfPage(String str, float f, float f2) {
        nativeSetDeviceDisplayOfPage(str, f, f2);
    }

    public String encryptGetClientKeyPayload(String str, String str2, String str3) {
        return nativePreGetClientKey(str, str2, str3);
    }

    public String encrypt(String str, String str2, String str3, String str4) {
        return nativeEncrypt(str, str2, str3, str4);
    }

    public String decrypt(String str, String str2, String str3, String str4) {
        return nativeDecrypt(str, str2, str3, str4);
    }

    public boolean verifyClientKeyPayload(String str, String str2, String str3) {
        return nativeVerifyClientKeyPayload(str, str2, str3);
    }
}
