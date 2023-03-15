package com.taobao.weex.ui.config;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;

public class ConfigModuleFactory<T extends WXModule> implements ModuleFactory<T> {
    public static final String TAG = "WeexScanConfigRegister";
    private ClassLoader mClassLoader;
    private String mClassName;
    private Class<T> mClazz;
    private Map<String, Invoker> mMethodMap;
    private String mName;
    private String[] methods;

    public ConfigModuleFactory(String str, String str2, String[] strArr) {
        this.mName = str;
        this.mClassName = str2;
        this.methods = strArr;
    }

    public String[] getMethods() {
        String[] strArr = this.methods;
        return strArr == null ? new String[0] : strArr;
    }

    public Invoker getMethodInvoker(String str) {
        if (this.mMethodMap == null) {
            generateMethodMap();
        }
        return this.mMethodMap.get(str);
    }

    public T buildInstance() throws IllegalAccessException, InstantiationException {
        if (this.mClazz == null) {
            this.mClazz = WXSDKManager.getInstance().getClassLoaderAdapter().getModuleClass(this.mName, this.mClassName, WXEnvironment.getApplication().getApplicationContext());
        }
        return (WXModule) this.mClazz.newInstance();
    }

    public T buildInstance(WXSDKInstance wXSDKInstance) throws IllegalAccessException, InstantiationException {
        if (wXSDKInstance == null) {
            return buildInstance();
        }
        if (this.mClazz == null || this.mClassLoader != wXSDKInstance.getContext().getClassLoader()) {
            this.mClazz = WXSDKManager.getInstance().getClassLoaderAdapter().getModuleClass(this.mName, this.mClassName, wXSDKInstance.getContext());
            this.mClassLoader = wXSDKInstance.getContext().getClassLoader();
        }
        return (WXModule) this.mClazz.newInstance();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r9 = (com.taobao.weex.annotation.JSMethod) r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0050, code lost:
        if ("_".equals(r9.alias()) == false) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0052, code lost:
        r6 = r5.getName();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0057, code lost:
        r6 = r9.alias();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005b, code lost:
        r0.put(r6, new com.taobao.weex.bridge.MethodInvoker(r5, r9.uiThread()));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void generateMethodMap() {
        /*
            r12 = this;
            boolean r0 = com.taobao.weex.WXEnvironment.isApkDebugable()
            if (r0 == 0) goto L_0x0022
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "extractMethodNames:"
            r0.append(r1)
            java.lang.Class<T> r1 = r12.mClazz
            java.lang.String r1 = r1.getSimpleName()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "WeexScanConfigRegister"
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r1, (java.lang.String) r0)
        L_0x0022:
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.Class<T> r1 = r12.mClazz     // Catch:{ all -> 0x00c4 }
            java.lang.reflect.Method[] r1 = r1.getMethods()     // Catch:{ all -> 0x00c4 }
            int r2 = r1.length     // Catch:{ all -> 0x00c4 }
            r3 = 0
            r4 = 0
        L_0x0030:
            if (r4 >= r2) goto L_0x00ca
            r5 = r1[r4]     // Catch:{ all -> 0x00c4 }
            java.lang.annotation.Annotation[] r6 = r5.getDeclaredAnnotations()     // Catch:{ all -> 0x00c4 }
            int r7 = r6.length     // Catch:{ all -> 0x00c4 }
            r8 = 0
        L_0x003a:
            if (r8 >= r7) goto L_0x00c0
            r9 = r6[r8]     // Catch:{ all -> 0x00c4 }
            if (r9 == 0) goto L_0x00bc
            boolean r10 = r9 instanceof com.taobao.weex.annotation.JSMethod     // Catch:{ all -> 0x00c4 }
            java.lang.String r11 = "_"
            if (r10 == 0) goto L_0x0068
            com.taobao.weex.annotation.JSMethod r9 = (com.taobao.weex.annotation.JSMethod) r9     // Catch:{ all -> 0x00c4 }
            java.lang.String r6 = r9.alias()     // Catch:{ all -> 0x00c4 }
            boolean r6 = r11.equals(r6)     // Catch:{ all -> 0x00c4 }
            if (r6 == 0) goto L_0x0057
            java.lang.String r6 = r5.getName()     // Catch:{ all -> 0x00c4 }
            goto L_0x005b
        L_0x0057:
            java.lang.String r6 = r9.alias()     // Catch:{ all -> 0x00c4 }
        L_0x005b:
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ all -> 0x00c4 }
            boolean r8 = r9.uiThread()     // Catch:{ all -> 0x00c4 }
            r7.<init>(r5, r8)     // Catch:{ all -> 0x00c4 }
            r0.put(r6, r7)     // Catch:{ all -> 0x00c4 }
            goto L_0x00c0
        L_0x0068:
            boolean r10 = r9 instanceof com.taobao.weex.common.WXModuleAnno     // Catch:{ all -> 0x00c4 }
            if (r10 == 0) goto L_0x007f
            com.taobao.weex.common.WXModuleAnno r9 = (com.taobao.weex.common.WXModuleAnno) r9     // Catch:{ all -> 0x00c4 }
            java.lang.String r6 = r5.getName()     // Catch:{ all -> 0x00c4 }
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ all -> 0x00c4 }
            boolean r8 = r9.runOnUIThread()     // Catch:{ all -> 0x00c4 }
            r7.<init>(r5, r8)     // Catch:{ all -> 0x00c4 }
            r0.put(r6, r7)     // Catch:{ all -> 0x00c4 }
            goto L_0x00c0
        L_0x007f:
            boolean r10 = r9 instanceof io.dcloud.feature.uniapp.annotation.UniJSMethod     // Catch:{ all -> 0x00c4 }
            if (r10 == 0) goto L_0x00a5
            io.dcloud.feature.uniapp.annotation.UniJSMethod r9 = (io.dcloud.feature.uniapp.annotation.UniJSMethod) r9     // Catch:{ all -> 0x00c4 }
            java.lang.String r6 = r9.alias()     // Catch:{ all -> 0x00c4 }
            boolean r6 = r11.equals(r6)     // Catch:{ all -> 0x00c4 }
            if (r6 == 0) goto L_0x0094
            java.lang.String r6 = r5.getName()     // Catch:{ all -> 0x00c4 }
            goto L_0x0098
        L_0x0094:
            java.lang.String r6 = r9.alias()     // Catch:{ all -> 0x00c4 }
        L_0x0098:
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ all -> 0x00c4 }
            boolean r8 = r9.uiThread()     // Catch:{ all -> 0x00c4 }
            r7.<init>(r5, r8)     // Catch:{ all -> 0x00c4 }
            r0.put(r6, r7)     // Catch:{ all -> 0x00c4 }
            goto L_0x00c0
        L_0x00a5:
            boolean r10 = r9 instanceof io.dcloud.feature.uniapp.common.UniModuleAnno     // Catch:{ all -> 0x00c4 }
            if (r10 == 0) goto L_0x00bc
            io.dcloud.feature.uniapp.common.UniModuleAnno r9 = (io.dcloud.feature.uniapp.common.UniModuleAnno) r9     // Catch:{ all -> 0x00c4 }
            java.lang.String r6 = r5.getName()     // Catch:{ all -> 0x00c4 }
            com.taobao.weex.bridge.MethodInvoker r7 = new com.taobao.weex.bridge.MethodInvoker     // Catch:{ all -> 0x00c4 }
            boolean r8 = r9.runOnUIThread()     // Catch:{ all -> 0x00c4 }
            r7.<init>(r5, r8)     // Catch:{ all -> 0x00c4 }
            r0.put(r6, r7)     // Catch:{ all -> 0x00c4 }
            goto L_0x00c0
        L_0x00bc:
            int r8 = r8 + 1
            goto L_0x003a
        L_0x00c0:
            int r4 = r4 + 1
            goto L_0x0030
        L_0x00c4:
            r1 = move-exception
            java.lang.String r2 = "[WXModuleManager] extractMethodNames:"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r1)
        L_0x00ca:
            r12.mMethodMap = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.config.ConfigModuleFactory.generateMethodMap():void");
    }

    public static ConfigModuleFactory fromConfig(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        try {
            String string = jSONObject.getString("name");
            String string2 = jSONObject.getString("className");
            JSONArray jSONArray = jSONObject.getJSONArray("methods");
            if (!TextUtils.isEmpty(string)) {
                if (!TextUtils.isEmpty(string2)) {
                    String[] strArr = new String[jSONArray.size()];
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.d("WeexScanConfigRegister", " resolve module " + string + " className " + string2 + " methods " + jSONArray);
                    }
                    return new ConfigModuleFactory(string, string2, (String[]) jSONArray.toArray(strArr));
                }
            }
            return null;
        } catch (Exception e) {
            WXLogUtils.e("WeexScanConfigRegister", (Throwable) e);
            return null;
        }
    }

    public String getName() {
        return this.mName;
    }
}
