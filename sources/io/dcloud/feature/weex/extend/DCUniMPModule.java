package io.dcloud.feature.weex.extend;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.feature.uniapp.common.UniModule;

public class DCUniMPModule extends UniModule {
    @JSMethod
    public void showUniMP(final String str, final JSCallback jSCallback) {
        MessageHandler.post(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r7 = this;
                    boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMP     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0012
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0070
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION     // Catch:{ Exception -> 0x006c }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x006c }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x006c }
                    goto L_0x0070
                L_0x0012:
                    java.lang.String r0 = r2     // Catch:{ Exception -> 0x006c }
                    boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0028
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0027
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER     // Catch:{ Exception -> 0x006c }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x006c }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x006c }
                L_0x0027:
                    return
                L_0x0028:
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.extend.DCUniMPModule r1 = io.dcloud.feature.weex.extend.DCUniMPModule.this     // Catch:{ Exception -> 0x006c }
                    com.taobao.weex.WXSDKInstance r1 = r1.mWXSDKInstance     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IWebview r0 = r0.findWebview(r1)     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0070
                    io.dcloud.feature.weex.extend.DCUniMPModule$1$1 r1 = new io.dcloud.feature.weex.extend.DCUniMPModule$1$1     // Catch:{ Exception -> 0x006c }
                    r1.<init>()     // Catch:{ Exception -> 0x006c }
                    r2 = 4
                    java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()     // Catch:{ Exception -> 0x006c }
                    r3 = 0
                    r2[r3] = r0     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    java.lang.String r0 = r0.getUniMPFeature()     // Catch:{ Exception -> 0x006c }
                    r4 = 1
                    r2[r4] = r0     // Catch:{ Exception -> 0x006c }
                    java.lang.String r0 = "showUniMP"
                    r5 = 2
                    r2[r5] = r0     // Catch:{ Exception -> 0x006c }
                    r0 = 3
                    java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x006c }
                    java.lang.String r6 = r2     // Catch:{ Exception -> 0x006c }
                    r5[r3] = r6     // Catch:{ Exception -> 0x006c }
                    r5[r4] = r1     // Catch:{ Exception -> 0x006c }
                    r2[r0] = r5     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x006c }
                    r3 = 10
                    r0.doForFeature(r1, r3, r2)     // Catch:{ Exception -> 0x006c }
                    goto L_0x0070
                L_0x006c:
                    r0 = move-exception
                    r0.printStackTrace()
                L_0x0070:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.AnonymousClass1.run():void");
            }
        });
    }

    @JSMethod
    public void closeUniMP(final String str, final JSCallback jSCallback) {
        MessageHandler.post(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r7 = this;
                    boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMP     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0012
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0070
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION     // Catch:{ Exception -> 0x006c }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x006c }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x006c }
                    goto L_0x0070
                L_0x0012:
                    java.lang.String r0 = r2     // Catch:{ Exception -> 0x006c }
                    boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0028
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0027
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER     // Catch:{ Exception -> 0x006c }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x006c }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x006c }
                L_0x0027:
                    return
                L_0x0028:
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.extend.DCUniMPModule r1 = io.dcloud.feature.weex.extend.DCUniMPModule.this     // Catch:{ Exception -> 0x006c }
                    com.taobao.weex.WXSDKInstance r1 = r1.mWXSDKInstance     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IWebview r0 = r0.findWebview(r1)     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0070
                    io.dcloud.feature.weex.extend.DCUniMPModule$2$1 r1 = new io.dcloud.feature.weex.extend.DCUniMPModule$2$1     // Catch:{ Exception -> 0x006c }
                    r1.<init>()     // Catch:{ Exception -> 0x006c }
                    r2 = 4
                    java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()     // Catch:{ Exception -> 0x006c }
                    r3 = 0
                    r2[r3] = r0     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    java.lang.String r0 = r0.getUniMPFeature()     // Catch:{ Exception -> 0x006c }
                    r4 = 1
                    r2[r4] = r0     // Catch:{ Exception -> 0x006c }
                    java.lang.String r0 = "closeUniMP"
                    r5 = 2
                    r2[r5] = r0     // Catch:{ Exception -> 0x006c }
                    r0 = 3
                    java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x006c }
                    java.lang.String r6 = r2     // Catch:{ Exception -> 0x006c }
                    r5[r3] = r6     // Catch:{ Exception -> 0x006c }
                    r5[r4] = r1     // Catch:{ Exception -> 0x006c }
                    r2[r0] = r5     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x006c }
                    r3 = 10
                    r0.doForFeature(r1, r3, r2)     // Catch:{ Exception -> 0x006c }
                    goto L_0x0070
                L_0x006c:
                    r0 = move-exception
                    r0.printStackTrace()
                L_0x0070:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.AnonymousClass2.run():void");
            }
        });
    }

    @JSMethod
    public void hideUniMP(final String str, final JSCallback jSCallback) {
        MessageHandler.post(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r7 = this;
                    boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMP     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0012
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0070
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION     // Catch:{ Exception -> 0x006c }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x006c }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x006c }
                    goto L_0x0070
                L_0x0012:
                    java.lang.String r0 = r2     // Catch:{ Exception -> 0x006c }
                    boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0028
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0027
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER     // Catch:{ Exception -> 0x006c }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x006c }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x006c }
                L_0x0027:
                    return
                L_0x0028:
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.extend.DCUniMPModule r1 = io.dcloud.feature.weex.extend.DCUniMPModule.this     // Catch:{ Exception -> 0x006c }
                    com.taobao.weex.WXSDKInstance r1 = r1.mWXSDKInstance     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IWebview r0 = r0.findWebview(r1)     // Catch:{ Exception -> 0x006c }
                    if (r0 == 0) goto L_0x0070
                    io.dcloud.feature.weex.extend.DCUniMPModule$3$1 r1 = new io.dcloud.feature.weex.extend.DCUniMPModule$3$1     // Catch:{ Exception -> 0x006c }
                    r1.<init>()     // Catch:{ Exception -> 0x006c }
                    r2 = 4
                    java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()     // Catch:{ Exception -> 0x006c }
                    r3 = 0
                    r2[r3] = r0     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    java.lang.String r0 = r0.getUniMPFeature()     // Catch:{ Exception -> 0x006c }
                    r4 = 1
                    r2[r4] = r0     // Catch:{ Exception -> 0x006c }
                    java.lang.String r0 = "hideUniMP"
                    r5 = 2
                    r2[r5] = r0     // Catch:{ Exception -> 0x006c }
                    r0 = 3
                    java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x006c }
                    java.lang.String r6 = r2     // Catch:{ Exception -> 0x006c }
                    r5[r3] = r6     // Catch:{ Exception -> 0x006c }
                    r5[r4] = r1     // Catch:{ Exception -> 0x006c }
                    r2[r0] = r5     // Catch:{ Exception -> 0x006c }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x006c }
                    io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x006c }
                    r3 = 10
                    r0.doForFeature(r1, r3, r2)     // Catch:{ Exception -> 0x006c }
                    goto L_0x0070
                L_0x006c:
                    r0 = move-exception
                    r0.printStackTrace()
                L_0x0070:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.AnonymousClass3.run():void");
            }
        });
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setDefaultMenuItems(java.lang.String r6, final com.taobao.weex.bridge.JSCallback r7) {
        /*
            r5 = this;
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r6)     // Catch:{ Exception -> 0x0005 }
            goto L_0x0006
        L_0x0005:
            r0 = 0
        L_0x0006:
            boolean r1 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r1 == 0) goto L_0x000d
            goto L_0x005a
        L_0x000d:
            if (r0 == 0) goto L_0x004f
            io.dcloud.feature.weex.extend.DCUniMPModule$4 r0 = new io.dcloud.feature.weex.extend.DCUniMPModule$4
            r0.<init>(r7)
            io.dcloud.feature.weex.WeexInstanceMgr r7 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r1 = r5.mWXSDKInstance
            io.dcloud.common.DHInterface.IWebview r7 = r7.findWebview(r1)
            if (r7 == 0) goto L_0x005a
            r1 = 4
            java.lang.Object[] r1 = new java.lang.Object[r1]
            io.dcloud.common.DHInterface.IApp r7 = r7.obtainApp()
            r2 = 0
            r1[r2] = r7
            io.dcloud.feature.weex.WeexInstanceMgr r7 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            java.lang.String r7 = r7.getUniMPFeature()
            r3 = 1
            r1[r3] = r7
            java.lang.String r7 = "setDefaultMenuItems"
            r4 = 2
            r1[r4] = r7
            r7 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r4[r2] = r6
            r4[r3] = r0
            r1[r7] = r4
            io.dcloud.feature.weex.WeexInstanceMgr r6 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.common.DHInterface.IMgr$MgrType r7 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r0 = 10
            r6.doForFeature(r7, r0, r1)
            goto L_0x005a
        L_0x004f:
            if (r7 == 0) goto L_0x005a
            io.dcloud.feature.weex.EnumStateCode r6 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER
            java.util.Map r6 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r6)
            r7.invoke(r6)
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.setDefaultMenuItems(java.lang.String, com.taobao.weex.bridge.JSCallback):void");
    }

    @JSMethod
    public void openUniMP(final String str, final JSCallback jSCallback) {
        MessageHandler.post(new Runnable() {
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object[]} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r7 = this;
                    java.lang.String r0 = r2     // Catch:{ Exception -> 0x0007 }
                    com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r0)     // Catch:{ Exception -> 0x0007 }
                    goto L_0x0008
                L_0x0007:
                    r0 = 0
                L_0x0008:
                    boolean r1 = io.dcloud.feature.internal.sdk.SDK.isUniMP     // Catch:{ Exception -> 0x007d }
                    if (r1 == 0) goto L_0x001a
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x007d }
                    if (r0 == 0) goto L_0x0081
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION     // Catch:{ Exception -> 0x007d }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x007d }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x007d }
                    goto L_0x0081
                L_0x001a:
                    if (r0 != 0) goto L_0x002a
                    com.taobao.weex.bridge.JSCallback r0 = r3     // Catch:{ Exception -> 0x007d }
                    if (r0 == 0) goto L_0x0029
                    io.dcloud.feature.weex.EnumStateCode r1 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER     // Catch:{ Exception -> 0x007d }
                    java.util.Map r1 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r1)     // Catch:{ Exception -> 0x007d }
                    r0.invoke(r1)     // Catch:{ Exception -> 0x007d }
                L_0x0029:
                    return
                L_0x002a:
                    io.dcloud.feature.weex.WeexInstanceMgr r1 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x007d }
                    io.dcloud.feature.weex.extend.DCUniMPModule r2 = io.dcloud.feature.weex.extend.DCUniMPModule.this     // Catch:{ Exception -> 0x007d }
                    com.taobao.weex.WXSDKInstance r2 = r2.mWXSDKInstance     // Catch:{ Exception -> 0x007d }
                    io.dcloud.common.DHInterface.IWebview r1 = r1.findWebview(r2)     // Catch:{ Exception -> 0x007d }
                    if (r1 == 0) goto L_0x0081
                    io.dcloud.feature.weex.extend.DCUniMPModule$5$1 r2 = new io.dcloud.feature.weex.extend.DCUniMPModule$5$1     // Catch:{ Exception -> 0x007d }
                    r2.<init>()     // Catch:{ Exception -> 0x007d }
                    java.lang.String r3 = "appInfo"
                    io.dcloud.common.DHInterface.IApp r4 = r1.obtainApp()     // Catch:{ Exception -> 0x007d }
                    java.lang.String r4 = r4.obtainAppInfo()     // Catch:{ Exception -> 0x007d }
                    r0.put((java.lang.String) r3, (java.lang.Object) r4)     // Catch:{ Exception -> 0x007d }
                    r3 = 4
                    java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x007d }
                    io.dcloud.common.DHInterface.IApp r1 = r1.obtainApp()     // Catch:{ Exception -> 0x007d }
                    r4 = 0
                    r3[r4] = r1     // Catch:{ Exception -> 0x007d }
                    io.dcloud.feature.weex.WeexInstanceMgr r1 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x007d }
                    java.lang.String r1 = r1.getUniMPFeature()     // Catch:{ Exception -> 0x007d }
                    r5 = 1
                    r3[r5] = r1     // Catch:{ Exception -> 0x007d }
                    java.lang.String r1 = "openUniMP"
                    r6 = 2
                    r3[r6] = r1     // Catch:{ Exception -> 0x007d }
                    r1 = 3
                    java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x007d }
                    java.lang.String r0 = r0.toJSONString()     // Catch:{ Exception -> 0x007d }
                    r6[r4] = r0     // Catch:{ Exception -> 0x007d }
                    r6[r5] = r2     // Catch:{ Exception -> 0x007d }
                    r3[r1] = r6     // Catch:{ Exception -> 0x007d }
                    io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x007d }
                    io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ Exception -> 0x007d }
                    r2 = 10
                    r0.doForFeature(r1, r2, r3)     // Catch:{ Exception -> 0x007d }
                    goto L_0x0081
                L_0x007d:
                    r0 = move-exception
                    r0.printStackTrace()
                L_0x0081:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.AnonymousClass5.run():void");
            }
        });
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void installUniMP(java.lang.String r9, final com.taobao.weex.bridge.JSCallback r10) {
        /*
            r8 = this;
            com.alibaba.fastjson.JSONObject r9 = com.alibaba.fastjson.JSON.parseObject(r9)     // Catch:{ Exception -> 0x0005 }
            goto L_0x0006
        L_0x0005:
            r9 = 0
        L_0x0006:
            boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r0 == 0) goto L_0x0019
            if (r10 == 0) goto L_0x00aa
            io.dcloud.feature.weex.EnumStateCode r9 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION
            java.util.Map r9 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r9)
            r10.invoke(r9)
            goto L_0x00aa
        L_0x0019:
            if (r9 == 0) goto L_0x009f
            java.lang.String r0 = "appid"
            java.lang.String r0 = r9.getString(r0)
            java.lang.String r1 = "wgtFile"
            java.lang.String r1 = r9.getString(r1)
            java.lang.String r2 = "password"
            boolean r3 = r9.containsKey(r2)
            if (r3 == 0) goto L_0x0035
            java.lang.String r9 = r9.getString(r2)
            goto L_0x0037
        L_0x0035:
            java.lang.String r9 = ""
        L_0x0037:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L_0x0050
            io.dcloud.feature.uniapp.AbsSDKInstance r2 = r8.mUniSDKInstance
            android.net.Uri r3 = android.net.Uri.parse(r1)
            java.lang.String r4 = "file"
            android.net.Uri r2 = r2.rewriteUri(r3, r4)
            if (r2 == 0) goto L_0x005b
            java.lang.String r1 = r2.getPath()
            goto L_0x005b
        L_0x0050:
            if (r10 == 0) goto L_0x005b
            io.dcloud.feature.weex.EnumStateCode r2 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_RESOURCE_EXIST
            java.util.Map r2 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r2)
            r10.invoke(r2)
        L_0x005b:
            io.dcloud.feature.weex.extend.DCUniMPModule$6 r2 = new io.dcloud.feature.weex.extend.DCUniMPModule$6
            r2.<init>(r10)
            io.dcloud.feature.weex.WeexInstanceMgr r10 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r3 = r8.mWXSDKInstance
            io.dcloud.common.DHInterface.IWebview r10 = r10.findWebview(r3)
            if (r10 == 0) goto L_0x00aa
            r3 = 4
            java.lang.Object[] r4 = new java.lang.Object[r3]
            io.dcloud.common.DHInterface.IApp r10 = r10.obtainApp()
            r5 = 0
            r4[r5] = r10
            io.dcloud.feature.weex.WeexInstanceMgr r10 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            java.lang.String r10 = r10.getUniMPFeature()
            r6 = 1
            r4[r6] = r10
            java.lang.String r10 = "installUniMP"
            r7 = 2
            r4[r7] = r10
            java.lang.Object[] r10 = new java.lang.Object[r3]
            r10[r5] = r0
            r10[r6] = r1
            r10[r7] = r9
            r9 = 3
            r10[r9] = r2
            r4[r9] = r10
            io.dcloud.feature.weex.WeexInstanceMgr r9 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.common.DHInterface.IMgr$MgrType r10 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r0 = 10
            r9.doForFeature(r10, r0, r4)
            goto L_0x00aa
        L_0x009f:
            if (r10 == 0) goto L_0x00aa
            io.dcloud.feature.weex.EnumStateCode r9 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER
            java.util.Map r9 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r9)
            r10.invoke(r9)
        L_0x00aa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.installUniMP(java.lang.String, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: type inference failed for: r0v3, types: [com.alibaba.fastjson.JSONObject] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendUniMPEvent(java.lang.String r7, java.lang.String r8, java.lang.String r9, final com.taobao.weex.bridge.JSCallback r10) {
        /*
            r6 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 != 0) goto L_0x006a
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            if (r0 == 0) goto L_0x000d
            goto L_0x006a
        L_0x000d:
            boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r0 == 0) goto L_0x001f
            if (r10 == 0) goto L_0x001e
            io.dcloud.feature.weex.EnumStateCode r7 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION
            java.util.Map r7 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r7)
            r10.invoke(r7)
        L_0x001e:
            return
        L_0x001f:
            com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r9)
            if (r0 == 0) goto L_0x0026
            r9 = r0
        L_0x0026:
            io.dcloud.feature.weex.extend.DCUniMPModule$7 r0 = new io.dcloud.feature.weex.extend.DCUniMPModule$7
            r0.<init>(r10)
            io.dcloud.feature.weex.WeexInstanceMgr r10 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r1 = r6.mWXSDKInstance
            io.dcloud.common.DHInterface.IWebview r10 = r10.findWebview(r1)
            if (r10 == 0) goto L_0x0069
            r1 = 4
            java.lang.Object[] r2 = new java.lang.Object[r1]
            io.dcloud.common.DHInterface.IApp r10 = r10.obtainApp()
            r3 = 0
            r2[r3] = r10
            io.dcloud.feature.weex.WeexInstanceMgr r10 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            java.lang.String r10 = r10.getUniMPFeature()
            r4 = 1
            r2[r4] = r10
            java.lang.String r10 = "sendUniMPEvent"
            r5 = 2
            r2[r5] = r10
            java.lang.Object[] r10 = new java.lang.Object[r1]
            r10[r3] = r7
            r10[r4] = r8
            r10[r5] = r9
            r7 = 3
            r10[r7] = r0
            r2[r7] = r10
            io.dcloud.feature.weex.WeexInstanceMgr r7 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.common.DHInterface.IMgr$MgrType r8 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r9 = 10
            r7.doForFeature(r8, r9, r2)
        L_0x0069:
            return
        L_0x006a:
            if (r10 == 0) goto L_0x0075
            io.dcloud.feature.weex.EnumStateCode r7 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER
            java.util.Map r7 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r7)
            r10.invoke(r7)
        L_0x0075:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.sendUniMPEvent(java.lang.String, java.lang.String, java.lang.String, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getUniMPVersion(java.lang.String r6, final com.taobao.weex.bridge.JSCallback r7) {
        /*
            r5 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r0 == 0) goto L_0x0017
            if (r7 == 0) goto L_0x006f
            io.dcloud.feature.weex.EnumStateCode r6 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_NO_PERMISSION
            java.util.Map r6 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r6)
            r7.invoke(r6)
            goto L_0x006f
        L_0x0017:
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 != 0) goto L_0x0025
            java.lang.String r0 = "__UNI__"
            boolean r0 = r6.startsWith(r0)
            if (r0 != 0) goto L_0x0030
        L_0x0025:
            if (r7 == 0) goto L_0x0030
            io.dcloud.feature.weex.EnumStateCode r0 = io.dcloud.feature.weex.EnumStateCode.FAIL_BY_INVALID_PARAMETER
            java.util.Map r0 = io.dcloud.feature.weex.EnumStateCode.obtainMap(r0)
            r7.invoke(r0)
        L_0x0030:
            io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r1 = r5.mWXSDKInstance
            io.dcloud.common.DHInterface.IWebview r0 = r0.findWebview(r1)
            if (r0 == 0) goto L_0x006f
            io.dcloud.feature.weex.extend.DCUniMPModule$8 r1 = new io.dcloud.feature.weex.extend.DCUniMPModule$8
            r1.<init>(r7)
            r7 = 4
            java.lang.Object[] r7 = new java.lang.Object[r7]
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()
            r2 = 0
            r7[r2] = r0
            io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            java.lang.String r0 = r0.getUniMPFeature()
            r3 = 1
            r7[r3] = r0
            java.lang.String r0 = "getUniMPVersion"
            r4 = 2
            r7[r4] = r0
            r0 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r4[r2] = r6
            r4[r3] = r1
            r7[r0] = r4
            io.dcloud.feature.weex.WeexInstanceMgr r6 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r1 = 10
            r6.doForFeature(r0, r1, r7)
        L_0x006f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.getUniMPVersion(java.lang.String, com.taobao.weex.bridge.JSCallback):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onUniMPEventReceive(com.taobao.weex.bridge.JSCallback r8) {
        /*
            r7 = this;
            boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r0 == 0) goto L_0x0007
            goto L_0x005b
        L_0x0007:
            io.dcloud.feature.weex.WeexInstanceMgr r0 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            com.taobao.weex.WXSDKInstance r1 = r7.mWXSDKInstance
            io.dcloud.common.DHInterface.IWebview r0 = r0.findWebview(r1)
            if (r0 == 0) goto L_0x005b
            boolean r1 = r8 instanceof com.taobao.weex.bridge.SimpleJSCallback
            if (r1 == 0) goto L_0x005b
            r1 = 4
            java.lang.Object[] r1 = new java.lang.Object[r1]
            io.dcloud.common.DHInterface.IApp r2 = r0.obtainApp()
            r3 = 0
            r1[r3] = r2
            io.dcloud.feature.weex.WeexInstanceMgr r2 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            java.lang.String r2 = r2.getUniMPFeature()
            r4 = 1
            r1[r4] = r2
            java.lang.String r2 = "onUniMPEventReceive"
            r5 = 2
            r1[r5] = r2
            r2 = 3
            java.lang.Object[] r6 = new java.lang.Object[r2]
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()
            java.lang.String r0 = r0.obtainAppId()
            r6[r3] = r0
            com.taobao.weex.WXSDKInstance r0 = r7.mWXSDKInstance
            java.lang.String r0 = r0.getInstanceId()
            r6[r4] = r0
            com.taobao.weex.bridge.SimpleJSCallback r8 = (com.taobao.weex.bridge.SimpleJSCallback) r8
            java.lang.String r8 = r8.getCallbackId()
            r6[r5] = r8
            r1[r2] = r6
            io.dcloud.feature.weex.WeexInstanceMgr r8 = io.dcloud.feature.weex.WeexInstanceMgr.self()
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 10
            r8.doForFeature(r0, r2, r1)
        L_0x005b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCUniMPModule.onUniMPEventReceive(com.taobao.weex.bridge.JSCallback):void");
    }
}
