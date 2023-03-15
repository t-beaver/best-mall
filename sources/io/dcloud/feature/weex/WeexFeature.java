package io.dcloud.feature.weex;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taobao.weex.bridge.WXBridgeManager;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.ReceiveSystemEventVoucher;
import io.dcloud.common.DHInterface.StandardFeature;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class WeexFeature extends StandardFeature implements IWaiter, ReceiveSystemEventVoucher {
    boolean isRegisterAllEvent = false;
    IApp mApp;

    public void init(AbsMgr absMgr, String str) {
        super.init(absMgr, str);
        WeexInstanceMgr.self().init(absMgr);
    }

    public void onStart(Context context, Bundle bundle, String[] strArr) {
        super.onStart(context, bundle, strArr);
        if (context instanceof Application) {
            WeexInstanceMgr.self().initWeexEnv((Application) context);
        }
    }

    public void onResume() {
        super.onResume();
        WeexInstanceMgr.self().onActivityResume();
    }

    public void onPause() {
        super.onPause();
        WeexInstanceMgr.self().onActivityPause();
    }

    /* access modifiers changed from: protected */
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        WeexInstanceMgr.self().onRequestPermissionsResult(i, strArr, iArr);
    }

    public void onDestroy() {
        IApp iApp = this.mApp;
        if (iApp == null || iApp.getQuitModel() != 2) {
            WeexInstanceMgr.self().onActivityDestroy();
        } else {
            WeexInstanceMgr.self().onActivityDestroy(false);
        }
        WeexInstanceMgr.self().setUniServiceCreated(false, (IApp) null);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        WeexInstanceMgr.self().onActivityResult(i, i2, intent);
    }

    public void registerAllEvent(IApp iApp) {
        this.mApp = iApp;
        if (!this.isRegisterAllEvent) {
            this.isRegisterAllEvent = true;
            unregisterSysEvent(iApp);
            registerSysEvent(this.mApp);
        }
    }

    public void dispose(String str) {
        super.dispose(str);
        if (this.mApp != null) {
            onDestroy();
            if (this.isRegisterAllEvent) {
                unregisterSysEvent(this.mApp);
                this.isRegisterAllEvent = false;
                this.mApp = null;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01ed, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:?, code lost:
        return io.dcloud.feature.weex.WeexInstanceMgr.self().findWebviewByInstanceId((java.lang.String) ((java.lang.Object[]) r9)[0]);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object doForFeature(java.lang.String r8, java.lang.Object r9) {
        /*
            r7 = this;
            r8.hashCode()
            int r0 = r8.hashCode()
            r1 = 4
            r2 = 3
            r3 = 2
            r4 = 1
            r5 = 0
            r6 = -1
            switch(r0) {
                case -2004248131: goto L_0x008a;
                case -1209764397: goto L_0x007f;
                case -982897726: goto L_0x0074;
                case -852986777: goto L_0x0069;
                case -623580498: goto L_0x005e;
                case -163799780: goto L_0x0053;
                case -149733713: goto L_0x0047;
                case 369306469: goto L_0x003c;
                case 594866751: goto L_0x002e;
                case 715247196: goto L_0x0020;
                case 2133581413: goto L_0x0012;
                default: goto L_0x0010;
            }
        L_0x0010:
            goto L_0x0094
        L_0x0012:
            java.lang.String r0 = "updateServiceReload"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x001c
            goto L_0x0094
        L_0x001c:
            r6 = 10
            goto L_0x0094
        L_0x0020:
            java.lang.String r0 = "createServiceUniNView"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x002a
            goto L_0x0094
        L_0x002a:
            r6 = 9
            goto L_0x0094
        L_0x002e:
            java.lang.String r0 = "createUniNView"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0038
            goto L_0x0094
        L_0x0038:
            r6 = 8
            goto L_0x0094
        L_0x003c:
            java.lang.String r0 = "postMessageToUniNView"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0045
            goto L_0x0094
        L_0x0045:
            r6 = 7
            goto L_0x0094
        L_0x0047:
            java.lang.String r0 = "weexViewUpdate"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0051
            goto L_0x0094
        L_0x0051:
            r6 = 6
            goto L_0x0094
        L_0x0053:
            java.lang.String r0 = "callNativeModuleSync"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x005c
            goto L_0x0094
        L_0x005c:
            r6 = 5
            goto L_0x0094
        L_0x005e:
            java.lang.String r0 = "getUniNViewModules"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0067
            goto L_0x0094
        L_0x0067:
            r6 = 4
            goto L_0x0094
        L_0x0069:
            java.lang.String r0 = "findWebviewByInstanceId"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0072
            goto L_0x0094
        L_0x0072:
            r6 = 3
            goto L_0x0094
        L_0x0074:
            java.lang.String r0 = "updateReload"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x007d
            goto L_0x0094
        L_0x007d:
            r6 = 2
            goto L_0x0094
        L_0x007f:
            java.lang.String r0 = "setUniNViewModuleReladyCallBack"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0088
            goto L_0x0094
        L_0x0088:
            r6 = 1
            goto L_0x0094
        L_0x008a:
            java.lang.String r0 = "onKeyboardHeightChange"
            boolean r8 = r8.equals(r0)
            if (r8 != 0) goto L_0x0093
            goto L_0x0094
        L_0x0093:
            r6 = 0
        L_0x0094:
            java.lang.String r8 = "data"
            switch(r6) {
                case 0: goto L_0x01ef;
                case 1: goto L_0x01e0;
                case 2: goto L_0x01cc;
                case 3: goto L_0x0224;
                case 4: goto L_0x01b7;
                case 5: goto L_0x0173;
                case 6: goto L_0x010b;
                case 7: goto L_0x00b4;
                case 8: goto L_0x00ac;
                case 9: goto L_0x00a4;
                case 10: goto L_0x009b;
                default: goto L_0x0099;
            }
        L_0x0099:
            goto L_0x01ed
        L_0x009b:
            io.dcloud.feature.weex.WeexInstanceMgr r8 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            r8.reloadWXServiceWrapper()
            goto L_0x01ed
        L_0x00a4:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            java.lang.Object r8 = r7.createWeexService(r9)
            goto L_0x0232
        L_0x00ac:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            java.lang.Object r8 = r7.createWeexWindow(r9)
            goto L_0x0232
        L_0x00b4:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            r0 = r9[r5]
            io.dcloud.common.DHInterface.IWebview r0 = (io.dcloud.common.DHInterface.IWebview) r0
            r4 = r9[r4]
            java.lang.String r4 = (java.lang.String) r4
            r3 = r9[r3]
            java.lang.String r3 = (java.lang.String) r3
            r2 = r9[r2]
            java.lang.String r2 = (java.lang.String) r2
            r9 = r9[r1]
            java.lang.String r9 = (java.lang.String) r9
            com.alibaba.fastjson.JSONObject r9 = com.alibaba.fastjson.JSON.parseObject(r9)
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.String r5 = "targetId"
            r1.put(r5, r3)
            java.lang.String r0 = r0.getWebviewUUID()
            java.lang.String r5 = "originId"
            r1.put(r5, r0)
            r1.put(r8, r9)
            boolean r8 = io.dcloud.common.util.PdrUtil.isEmpty(r4)
            if (r8 == 0) goto L_0x00eb
            r4 = r3
        L_0x00eb:
            boolean r8 = r2.equalsIgnoreCase(r4)
            if (r8 == 0) goto L_0x00fa
            io.dcloud.feature.weex.WeexInstanceMgr r8 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.feature.weex.WXBaseWrapper r8 = r8.findWXBaseWrapper((java.lang.String) r2)
            goto L_0x0102
        L_0x00fa:
            io.dcloud.feature.weex.WeexInstanceMgr r8 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.feature.weex.WXBaseWrapper r8 = r8.findWXBaseWrapper((java.lang.String) r4)
        L_0x0102:
            if (r8 == 0) goto L_0x01ed
            java.lang.String r9 = "plusMessage"
            r8.fireGlobalEvent(r9, r1)
            goto L_0x01ed
        L_0x010b:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            r0 = r9[r5]
            io.dcloud.common.DHInterface.IWebview r0 = (io.dcloud.common.DHInterface.IWebview) r0
            r1 = r9[r4]
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            r3 = r9[r3]
            org.json.JSONObject r3 = (org.json.JSONObject) r3
            r9 = r9[r2]
            java.lang.String r9 = (java.lang.String) r9
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r9)
            if (r2 == 0) goto L_0x012b
            io.dcloud.common.DHInterface.IApp r9 = r0.obtainApp()
            java.lang.String r9 = r9.obtainAppId()
        L_0x012b:
            java.lang.String r2 = "path"
            boolean r4 = r3.has(r2)
            if (r4 == 0) goto L_0x01ed
            java.lang.String r4 = r3.optString(r2)
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r4)
            if (r4 != 0) goto L_0x01ed
            io.dcloud.feature.weex.WeexInstanceMgr r4 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.feature.weex.WXBaseWrapper r4 = r4.findWXBaseWrapper((java.lang.String) r9)
            org.json.JSONObject r5 = new org.json.JSONObject
            r5.<init>()
            java.lang.String r2 = r3.optString(r2)
            boolean r6 = r0.isUniWebView()
            if (r6 == 0) goto L_0x015a
            r6 = r0
            io.dcloud.common.adapter.ui.AdaUniWebView r6 = (io.dcloud.common.adapter.ui.AdaUniWebView) r6
            r6.setNVuePath(r2)
        L_0x015a:
            java.lang.String r6 = "js"
            r5.put(r6, r2)     // Catch:{ JSONException -> 0x0163 }
            r5.put(r8, r3)     // Catch:{ JSONException -> 0x0163 }
            goto L_0x0167
        L_0x0163:
            r8 = move-exception
            r8.printStackTrace()
        L_0x0167:
            if (r4 == 0) goto L_0x016e
            r4.loadTemplate(r5)
            goto L_0x01ed
        L_0x016e:
            r7.createUniNView(r0, r1, r9, r5)
            goto L_0x01ed
        L_0x0173:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            r8 = r9[r5]
            io.dcloud.common.DHInterface.IWebview r8 = (io.dcloud.common.DHInterface.IWebview) r8
            r8 = r9[r4]
            java.lang.String r8 = (java.lang.String) r8
            r0 = r9[r3]
            java.lang.String r0 = (java.lang.String) r0
            r2 = r9[r2]
            java.lang.String r2 = (java.lang.String) r2
            r9 = r9[r1]
            java.lang.String r9 = (java.lang.String) r9
            com.alibaba.fastjson.JSONObject r9 = com.alibaba.fastjson.JSON.parseObject(r9)
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r8)
            if (r1 == 0) goto L_0x0194
            r8 = r0
        L_0x0194:
            boolean r0 = r2.equalsIgnoreCase(r8)
            if (r0 == 0) goto L_0x01a3
            io.dcloud.feature.weex.WeexInstanceMgr r8 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r8 = r8.findWXSDKInstance(r2)
            goto L_0x01ab
        L_0x01a3:
            io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r8 = r0.findWXSDKInstance(r8)
        L_0x01ab:
            if (r8 == 0) goto L_0x01ed
            java.lang.String r8 = r8.getInstanceId()
            java.lang.String r8 = r7.callNativeModule(r8, r9)
            goto L_0x0232
        L_0x01b7:
            io.dcloud.feature.weex.WeexInstanceMgr r8 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            java.lang.String r8 = r8.getUniNViewModules()
            if (r8 != 0) goto L_0x0232
            com.alibaba.fastjson.JSONObject r9 = com.taobao.weex.bridge.WXModuleManager.getRegisterJsModules()
            if (r9 == 0) goto L_0x0232
            java.lang.String r8 = r9.toJSONString()
            goto L_0x0232
        L_0x01cc:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            r8 = r9[r5]
            java.lang.String r8 = (java.lang.String) r8
            io.dcloud.feature.weex.WeexInstanceMgr r9 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.feature.weex.WXViewWrapper r8 = r9.findPathByWrapper(r8)
            if (r8 == 0) goto L_0x01ed
            r8.reload()
            goto L_0x01ed
        L_0x01e0:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            r8 = r9[r5]
            io.dcloud.common.DHInterface.ICallBack r8 = (io.dcloud.common.DHInterface.ICallBack) r8
            io.dcloud.feature.weex.WeexInstanceMgr r9 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            r9.setUniNViewModuleReladyCallBack(r8)
        L_0x01ed:
            r8 = 0
            goto L_0x0232
        L_0x01ef:
            r8 = r9
            java.lang.Object[] r8 = (java.lang.Object[]) r8
            r0 = r8[r5]
            java.lang.String r0 = (java.lang.String) r0
            r8 = r8[r4]
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            io.dcloud.feature.weex.WeexInstanceMgr r1 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.feature.weex.WXBaseWrapper r0 = r1.findWXBaseWrapper((java.lang.String) r0)
            if (r0 == 0) goto L_0x0224
            float r8 = (float) r8
            io.dcloud.common.DHInterface.IWebview r1 = r0.mWebview
            float r1 = r1.getScale()
            float r8 = r8 / r1
            int r8 = (int) r8
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            java.lang.String r2 = "height"
            r1.put(r2, r8)
            java.lang.String r8 = "KeyboardHeightChange"
            r0.fireGlobalEvent(r8, r1)
        L_0x0224:
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            r8 = r9[r5]
            java.lang.String r8 = (java.lang.String) r8
            io.dcloud.feature.weex.WeexInstanceMgr r9 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.common.DHInterface.IWebview r8 = r9.findWebviewByInstanceId(r8)
        L_0x0232:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.WeexFeature.doForFeature(java.lang.String, java.lang.Object):java.lang.Object");
    }

    private void createUniNView(IWebview iWebview, ViewGroup viewGroup, String str, JSONObject jSONObject) {
        createUniNView(iWebview, viewGroup, str, jSONObject, WXBaseWrapper.DE_INDEX);
    }

    private void createUniNView(IWebview iWebview, ViewGroup viewGroup, String str, JSONObject jSONObject, int i) {
        WeexInstanceMgr.self().createWeexView(iWebview, viewGroup, jSONObject, str, i);
    }

    private Object createWeexWindow(Object[] objArr) {
        IWebview iWebview = objArr[0];
        registerAllEvent(iWebview.obtainApp());
        return WeexInstanceMgr.self().createWeexView(iWebview, objArr[1], objArr[2], objArr[3], WXViewWrapper.DE_INDEX);
    }

    private Object createWeexService(Object[] objArr) {
        IApp iApp = objArr[0];
        registerAllEvent(iApp);
        ViewGroup viewGroup = objArr[2];
        String str = objArr[3];
        return WeexInstanceMgr.self().createWeexService(iApp, viewGroup, str, objArr[1]);
    }

    public String callNativeModule(String str, com.alibaba.fastjson.JSONObject jSONObject) {
        Object callNativeModule = WXBridgeManager.getInstance().callNativeModule(str, jSONObject.getString("plugin"), jSONObject.getString("method"), JSON.parseArray(jSONObject.getString("args")), (com.alibaba.fastjson.JSONObject) null);
        if (callNativeModule != null) {
            if (callNativeModule instanceof com.alibaba.fastjson.JSONObject) {
                return JSUtil.wrapJsVar(JSONUtil.createJSONObject(((com.alibaba.fastjson.JSONObject) callNativeModule).toJSONString()));
            }
            if (callNativeModule instanceof HashMap) {
                return JSUtil.wrapJsVar(JSONUtil.createJSONObject(new com.alibaba.fastjson.JSONObject((Map<String, Object>) (HashMap) callNativeModule).toJSONString()));
            }
            if (callNativeModule instanceof JSONArray) {
                return JSUtil.wrapJsVar(JSONUtil.createJSONArray(((JSONArray) callNativeModule).toJSONString()));
            }
            if (callNativeModule instanceof String) {
                return JSUtil.wrapJsVar(String.valueOf(callNativeModule));
            }
            if (callNativeModule instanceof JSONObject) {
                return JSUtil.wrapJsVar((JSONObject) callNativeModule);
            }
            if (callNativeModule instanceof org.json.JSONArray) {
                return JSUtil.wrapJsVar((org.json.JSONArray) callNativeModule);
            }
        }
        return JSUtil.wrapJsVar("");
    }
}
