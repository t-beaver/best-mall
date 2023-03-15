package io.dcloud.g;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.BaseFeature;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IBoot;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.core.permission.PermissionControler;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DataUtil;
import io.dcloud.common.util.ErrorDialogUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class b extends AbsMgr implements IMgr.FeatureEvent {
    private HashMap<String, HashMap<String, String>> a = null;
    private HashMap<String, String> b = null;
    private HashMap<String, IFeature> c = null;
    private HashMap<String, String> d = null;
    private HashMap<String, IBoot> e = null;
    private String f = null;
    private ArrayList<String> g = new ArrayList<>();
    HashMap<String, String> h = new HashMap<>();
    c i = null;

    public b(ICore iCore) {
        super(iCore, "featuremgr", IMgr.MgrType.FeatureMgr);
        b();
        if (!c()) {
            c();
        }
    }

    private String a(IApp iApp, IFrameView iFrameView) {
        HashMap<String, String> hashMap = this.h;
        String str = hashMap.get(iApp.obtainAppId() + "_" + iApp.obtainAppVersionName());
        if (str == null) {
            str = a(iApp, iFrameView.obtainWebView());
            if (Build.VERSION.SDK_INT > 19) {
                HashMap<String, String> hashMap2 = this.h;
                hashMap2.put(iApp.obtainAppId() + "_" + iApp.obtainAppVersionName(), str);
            }
        }
        return str;
    }

    private String b(IApp iApp) {
        String metaValue = AndroidResources.getMetaValue("DCLOUD_UNIPUSH");
        return (PdrUtil.isEmpty(metaValue) || !Boolean.valueOf(metaValue).booleanValue()) ? "" : "window.__isUniPush__ = true;";
    }

    private void c(String str) {
        if (this.c.get(str) == null) {
            try {
                IFeature iFeature = (IFeature) Class.forName(str).newInstance();
                this.c.put(str, iFeature);
                iFeature.init(this, str);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException unused) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0023 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IBoot> d() {
        /*
            r10 = this;
            java.lang.String r0 = "Main_Path"
            java.lang.String r1 = "FeatureMgr.loadBootOptions "
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IBoot> r2 = r10.e
            java.util.HashMap<java.lang.String, java.lang.String> r3 = r10.d
            java.util.HashMap<java.lang.String, java.lang.String> r4 = r10.b
            java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>> r5 = r10.a
            java.lang.String r6 = io.dcloud.common.util.BaseInfo.s_properties
            io.dcloud.common.util.PdrUtil.loadProperties2HashMap(r3, r4, r5, r6)
            java.util.Set r4 = r3.keySet()
            if (r4 == 0) goto L_0x00cc
            java.util.HashMap r2 = new java.util.HashMap
            r5 = 2
            r2.<init>(r5)
            r10.e = r2
            java.util.Iterator r4 = r4.iterator()
        L_0x0023:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L_0x00cc
            java.lang.Object r5 = r4.next()
            java.lang.String r5 = (java.lang.String) r5
            io.dcloud.common.DHInterface.IBoot r6 = io.dcloud.g.a.b(r10, r5)
            if (r6 != 0) goto L_0x00c5
            java.lang.Object r7 = r3.get(r5)
            java.lang.String r7 = (java.lang.String) r7
            java.lang.Class r7 = java.lang.Class.forName(r7)     // Catch:{ InstantiationException -> 0x00aa, IllegalAccessException -> 0x008f, ClassNotFoundException -> 0x0074, ClassCastException -> 0x0059 }
            java.lang.Object r7 = r7.newInstance()     // Catch:{ InstantiationException -> 0x00aa, IllegalAccessException -> 0x008f, ClassNotFoundException -> 0x0074, ClassCastException -> 0x0059 }
            io.dcloud.common.DHInterface.IBoot r7 = (io.dcloud.common.DHInterface.IBoot) r7     // Catch:{ InstantiationException -> 0x00aa, IllegalAccessException -> 0x008f, ClassNotFoundException -> 0x0074, ClassCastException -> 0x0059 }
            boolean r6 = r7 instanceof io.dcloud.common.DHInterface.BaseFeature     // Catch:{ InstantiationException -> 0x0057, IllegalAccessException -> 0x0055, ClassNotFoundException -> 0x0053, ClassCastException -> 0x0051 }
            if (r6 == 0) goto L_0x00c4
            r6 = r7
            io.dcloud.common.DHInterface.BaseFeature r6 = (io.dcloud.common.DHInterface.BaseFeature) r6     // Catch:{ InstantiationException -> 0x0057, IllegalAccessException -> 0x0055, ClassNotFoundException -> 0x0053, ClassCastException -> 0x0051 }
            r6.init(r10, r5)     // Catch:{ InstantiationException -> 0x0057, IllegalAccessException -> 0x0055, ClassNotFoundException -> 0x0053, ClassCastException -> 0x0051 }
            goto L_0x00c4
        L_0x0051:
            r6 = move-exception
            goto L_0x005d
        L_0x0053:
            r6 = move-exception
            goto L_0x0078
        L_0x0055:
            r6 = move-exception
            goto L_0x0093
        L_0x0057:
            r6 = move-exception
            goto L_0x00ae
        L_0x0059:
            r7 = move-exception
            r9 = r7
            r7 = r6
            r6 = r9
        L_0x005d:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r1)
            java.lang.String r6 = r6.getMessage()
            r8.append(r6)
            java.lang.String r6 = r8.toString()
            io.dcloud.common.adapter.util.Logger.e(r0, r6)
            goto L_0x00c4
        L_0x0074:
            r7 = move-exception
            r9 = r7
            r7 = r6
            r6 = r9
        L_0x0078:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r1)
            java.lang.String r6 = r6.getMessage()
            r8.append(r6)
            java.lang.String r6 = r8.toString()
            io.dcloud.common.adapter.util.Logger.e(r0, r6)
            goto L_0x00c4
        L_0x008f:
            r7 = move-exception
            r9 = r7
            r7 = r6
            r6 = r9
        L_0x0093:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r1)
            java.lang.String r6 = r6.getMessage()
            r8.append(r6)
            java.lang.String r6 = r8.toString()
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r0, (java.lang.String) r6)
            goto L_0x00c4
        L_0x00aa:
            r7 = move-exception
            r9 = r7
            r7 = r6
            r6 = r9
        L_0x00ae:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r1)
            java.lang.String r6 = r6.getMessage()
            r8.append(r6)
            java.lang.String r6 = r8.toString()
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r0, (java.lang.String) r6)
        L_0x00c4:
            r6 = r7
        L_0x00c5:
            if (r6 == 0) goto L_0x0023
            r2.put(r5, r6)
            goto L_0x0023
        L_0x00cc:
            java.util.HashMap r0 = r10.a((java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IBoot>) r2)
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r10.b
            java.util.Collection r1 = r1.values()
            java.util.Iterator r1 = r1.iterator()
            r10.a((java.util.Iterator<java.lang.String>) r1)
            java.util.HashMap<java.lang.String, java.util.HashMap<java.lang.String, java.lang.String>> r1 = r10.a
            java.util.Collection r1 = r1.values()
            java.util.Iterator r1 = r1.iterator()
        L_0x00e7:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00ff
            java.lang.Object r2 = r1.next()
            java.util.HashMap r2 = (java.util.HashMap) r2
            java.util.Collection r2 = r2.values()
            java.util.Iterator r2 = r2.iterator()
            r10.a((java.util.Iterator<java.lang.String>) r2)
            goto L_0x00e7
        L_0x00ff:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.g.b.d():java.util.HashMap");
    }

    public void dispose() {
        HashMap<String, IFeature> hashMap = this.c;
        if (hashMap != null) {
            Collection<IFeature> values = hashMap.values();
            if (values != null) {
                for (IFeature dispose : values) {
                    dispose.dispose((String) null);
                }
            }
            this.c.clear();
            this.c = null;
        }
    }

    public Object processEvent(IMgr.MgrType mgrType, int i2, Object obj) {
        Object obj2;
        Activity activity;
        try {
            if (!checkMgrId(mgrType)) {
                obj2 = this.mCore.dispatchEvent(mgrType, i2, obj);
            } else {
                switch (i2) {
                    case 0:
                        this.c = new HashMap<>(1);
                        this.b = new HashMap<>(1);
                        this.a = new HashMap<>(1);
                        this.e = new HashMap<>();
                        this.d = new HashMap<>(1);
                        return d();
                    case 1:
                        Object[] objArr = (Object[]) obj;
                        if (!AbsoluteConst.UNI_SYNC_EXEC_METHOD.equalsIgnoreCase(String.valueOf(objArr[2]))) {
                            obj2 = b(objArr);
                            break;
                        } else {
                            obj2 = a(objArr);
                            break;
                        }
                    case 2:
                        Object[] objArr2 = (Object[]) obj;
                        String a2 = a((IApp) objArr2[0], (IFrameView) objArr2[1]);
                        if (a2 != null) {
                            return a2;
                        }
                        Process.killProcess(Process.myPid());
                        return a2;
                    case 3:
                        a(String.valueOf(obj));
                        return null;
                    case 4:
                        obj2 = this.a.get(String.valueOf(obj));
                        break;
                    case 5:
                        String[] strArr = (String[]) obj;
                        String str = strArr[0];
                        String str2 = strArr[1];
                        String str3 = strArr[2];
                        if (!PdrUtil.isEmpty(str) && !PdrUtil.isEmpty(str2)) {
                            this.b.put(str.toLowerCase(Locale.ENGLISH), str2);
                        }
                        if (PdrUtil.isEmpty(str3)) {
                            return null;
                        }
                        this.g.add(str3);
                        return null;
                    case 8:
                        String[] strArr2 = (String[]) obj;
                        obj2 = Boolean.valueOf(PermissionControler.checkPermission(strArr2[0], strArr2[1]));
                        break;
                    case 9:
                        obj2 = Boolean.valueOf(b(String.valueOf(obj)));
                        break;
                    case 10:
                        Object[] objArr3 = (Object[]) obj;
                        if (objArr3[0] instanceof IWebview) {
                            activity = ((IWebview) objArr3[0]).getActivity();
                        } else {
                            activity = objArr3[0] instanceof IApp ? ((IApp) objArr3[0]).getActivity() : null;
                        }
                        IFeature b2 = b(String.valueOf(objArr3[1]), activity);
                        if (b2 instanceof IWaiter) {
                            obj2 = ((IWaiter) b2).doForFeature(String.valueOf(objArr3[2]), objArr3[3]);
                            break;
                        } else {
                            return null;
                        }
                    case 11:
                        c((String) ((Object[]) obj)[0]);
                        return null;
                    default:
                        return null;
                }
            }
            return obj2;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        } catch (Throwable th) {
            Logger.w("FeatureMgr.processEvent", th);
            return null;
        }
    }

    private synchronized String b(Object[] objArr) {
        return a(objArr);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v15, resolved type: io.dcloud.common.DHInterface.IFeature} */
    /* JADX WARNING: type inference failed for: r2v7, types: [java.lang.Object, io.dcloud.common.DHInterface.IBoot] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0095 A[SYNTHETIC, Splitter:B:39:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:63:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.dcloud.common.DHInterface.IFeature b(java.lang.String r6, android.app.Activity r7) {
        /*
            r5 = this;
            java.lang.String r0 = ","
            boolean r1 = r6.contains(r0)
            if (r1 == 0) goto L_0x0016
            java.lang.String[] r6 = r6.split(r0)
            r0 = 0
            r0 = r6[r0]
            r1 = 1
            r6 = r6[r1]
            r4 = r0
            r0 = r6
            r6 = r4
            goto L_0x0017
        L_0x0016:
            r0 = 0
        L_0x0017:
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IFeature> r1 = r5.c
            java.lang.Object r1 = r1.get(r6)
            io.dcloud.common.DHInterface.IFeature r1 = (io.dcloud.common.DHInterface.IFeature) r1
            if (r1 != 0) goto L_0x00d0
            java.util.HashMap<java.lang.String, java.lang.String> r2 = r5.b
            java.lang.Object r2 = r2.get(r6)
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r2)
            if (r3 == 0) goto L_0x0030
            goto L_0x0031
        L_0x0030:
            r0 = r2
        L_0x0031:
            if (r0 == 0) goto L_0x0058
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IBoot> r2 = r5.e
            boolean r2 = r2.containsKey(r6)
            if (r2 == 0) goto L_0x0058
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IBoot> r2 = r5.e
            java.lang.Object r2 = r2.get(r6)
            io.dcloud.common.DHInterface.IBoot r2 = (io.dcloud.common.DHInterface.IBoot) r2
            boolean r3 = r2 instanceof io.dcloud.common.DHInterface.BaseFeature
            if (r3 == 0) goto L_0x0058
            java.lang.Class r3 = r2.getClass()
            java.lang.String r3 = r3.getName()
            boolean r3 = r0.equals(r3)
            if (r3 == 0) goto L_0x0058
            r1 = r2
            io.dcloud.common.DHInterface.BaseFeature r1 = (io.dcloud.common.DHInterface.BaseFeature) r1
        L_0x0058:
            if (r1 != 0) goto L_0x00cb
            if (r0 == 0) goto L_0x00b0
            io.dcloud.common.DHInterface.IFeature r1 = io.dcloud.g.a.a(r5, r6)     // Catch:{ InstantiationException -> 0x00ab, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x0085 }
            if (r1 != 0) goto L_0x007f
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ InstantiationException -> 0x00ab, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x0085 }
            java.lang.Object r0 = r0.newInstance()     // Catch:{ InstantiationException -> 0x00ab, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x0085 }
            io.dcloud.common.DHInterface.IFeature r0 = (io.dcloud.common.DHInterface.IFeature) r0     // Catch:{ InstantiationException -> 0x00ab, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x0085 }
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IFeature> r1 = r5.c     // Catch:{ InstantiationException -> 0x007c, IllegalAccessException -> 0x0079, ClassNotFoundException -> 0x0077 }
            r1.put(r6, r0)     // Catch:{ InstantiationException -> 0x007c, IllegalAccessException -> 0x0079, ClassNotFoundException -> 0x0077 }
            r0.init(r5, r6)     // Catch:{ InstantiationException -> 0x007c, IllegalAccessException -> 0x0079, ClassNotFoundException -> 0x0077 }
            r1 = r0
            goto L_0x00d0
        L_0x0077:
            r1 = move-exception
            goto L_0x0089
        L_0x0079:
            r6 = move-exception
            r1 = r0
            goto L_0x00a7
        L_0x007c:
            r6 = move-exception
            r1 = r0
            goto L_0x00ac
        L_0x007f:
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IFeature> r0 = r5.c     // Catch:{ InstantiationException -> 0x00ab, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x0085 }
            r0.put(r6, r1)     // Catch:{ InstantiationException -> 0x00ab, IllegalAccessException -> 0x00a6, ClassNotFoundException -> 0x0085 }
            goto L_0x00d0
        L_0x0085:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r0 = r4
        L_0x0089:
            r1.printStackTrace()
            java.lang.Object r7 = r5.a((java.lang.String) r6, (android.app.Activity) r7)     // Catch:{ Exception -> 0x00a0 }
            r1 = r7
            io.dcloud.common.DHInterface.IFeature r1 = (io.dcloud.common.DHInterface.IFeature) r1     // Catch:{ Exception -> 0x00a0 }
            if (r1 == 0) goto L_0x00d0
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IFeature> r7 = r5.c     // Catch:{ Exception -> 0x009e }
            r7.put(r6, r1)     // Catch:{ Exception -> 0x009e }
            r1.init(r5, r6)     // Catch:{ Exception -> 0x009e }
            goto L_0x00d0
        L_0x009e:
            r6 = move-exception
            goto L_0x00a2
        L_0x00a0:
            r6 = move-exception
            r1 = r0
        L_0x00a2:
            r6.printStackTrace()
            goto L_0x00d0
        L_0x00a6:
            r6 = move-exception
        L_0x00a7:
            r6.printStackTrace()
            goto L_0x00d0
        L_0x00ab:
            r6 = move-exception
        L_0x00ac:
            r6.printStackTrace()
            goto L_0x00d0
        L_0x00b0:
            java.lang.Object r7 = r5.a((java.lang.String) r6, (android.app.Activity) r7)     // Catch:{ Exception -> 0x00c6 }
            io.dcloud.common.DHInterface.IFeature r7 = (io.dcloud.common.DHInterface.IFeature) r7     // Catch:{ Exception -> 0x00c6 }
            if (r7 == 0) goto L_0x00c4
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IFeature> r0 = r5.c     // Catch:{ Exception -> 0x00c1 }
            r0.put(r6, r7)     // Catch:{ Exception -> 0x00c1 }
            r7.init(r5, r6)     // Catch:{ Exception -> 0x00c1 }
            goto L_0x00c4
        L_0x00c1:
            r6 = move-exception
            r1 = r7
            goto L_0x00c7
        L_0x00c4:
            r1 = r7
            goto L_0x00d0
        L_0x00c6:
            r6 = move-exception
        L_0x00c7:
            r6.printStackTrace()
            goto L_0x00d0
        L_0x00cb:
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IFeature> r7 = r5.c
            r7.put(r6, r1)
        L_0x00d0:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.g.b.b(java.lang.String, android.app.Activity):io.dcloud.common.DHInterface.IFeature");
    }

    public String a(IWebview iWebview) {
        StringBuffer stringBuffer = new StringBuffer();
        String webviewUUID = iWebview.getWebviewUUID();
        if (PdrUtil.isEmpty(webviewUUID)) {
            webviewUUID = String.valueOf(iWebview.obtainFrameView().hashCode());
        }
        stringBuffer.append("window.__HtMl_Id__= '" + webviewUUID + "';");
        if (PdrUtil.isEmpty(iWebview.obtainFrameId())) {
            stringBuffer.append("window.__WebVieW_Id__= undefined;");
        } else {
            stringBuffer.append("window.__WebVieW_Id__= '" + iWebview.obtainFrameId() + "';");
        }
        stringBuffer.append("try{window.plus.__tag__='_plus_all_'}catch(e){}");
        return stringBuffer.toString();
    }

    public boolean c() {
        try {
            if (!BaseInfo.ISDEBUG || !DHFile.isExist("/sdcard/dcloud/all.js")) {
                this.f = new String(PlatformUtil.getFileContent(DCloudAdapterUtil.getRuntimeJsPath(), 1));
            } else {
                this.f = new String(PlatformUtil.getFileContent("/sdcard/dcloud/all.js", 2));
            }
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    private String a(IApp iApp, IWebview iWebview) {
        String str;
        StringBuffer stringBuffer = new StringBuffer(AbsoluteConst.PROTOCOL_JAVASCRIPT);
        stringBuffer.append("function ");
        stringBuffer.append(AbsoluteConst.LOAD_PLUS_FUN_NAME);
        stringBuffer.append("(){try{");
        int i2 = Build.VERSION.SDK_INT;
        if (i2 <= 19) {
            stringBuffer.append(a(iWebview));
        }
        if (TextUtils.isEmpty(this.f) || this.f.length() < 400) {
            return null;
        }
        stringBuffer.append("window._____isDebug_____=" + BaseInfo.ISDEBUG + ";");
        stringBuffer.append("window._____platform_____=1;");
        stringBuffer.append("window._____platform_os_version_____=" + i2 + ";");
        stringBuffer.append(this.f);
        if (PermissionControler.checkPermission(iApp.obtainAppId(), IFeature.F_DEVICE.toLowerCase(Locale.ENGLISH)) || !iApp.manifestBeParsed()) {
            if (PdrUtil.isEmpty(DeviceInfo.DEVICESTATUS_JS)) {
                try {
                    DeviceInfo.initGsmCdmaCell();
                } catch (SecurityException e2) {
                    e2.printStackTrace();
                }
                DeviceInfo.getDevicestatus_js(iApp);
            }
            stringBuffer.append(DeviceInfo.DEVICESTATUS_JS);
        }
        SDK.IntegratedMode integratedMode = BaseInfo.sRuntimeMode;
        StringBuilder sb = new StringBuilder();
        sb.append("window.__NWin_Enable__=");
        sb.append(integratedMode == SDK.IntegratedMode.WEBVIEW ? String.valueOf(false) : String.valueOf(true));
        sb.append(";");
        stringBuffer.append(sb.toString());
        if (PermissionControler.checkPermission(iApp.obtainAppId(), IFeature.F_RUNTIME) || !iApp.manifestBeParsed()) {
            String obtainConfigProperty = iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_LOADED_TIME);
            if (BaseInfo.ISAMU) {
                str = StringUtil.format(AbsoluteConst.JS_RUNTIME_VERSIONs, iApp.obtainAppVersionName(), "1.9.9.81676", iApp.obtainAppId(), obtainConfigProperty);
            } else {
                str = StringUtil.format(AbsoluteConst.JS_RUNTIME_VERSIONs, AndroidResources.mApplicationInfo.versionName, "1.9.9.81676", iApp.obtainAppId(), obtainConfigProperty);
            }
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, str));
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_ARGUMENTS, DataUtil.utf8ToUnicode(iApp.obtainRuntimeArgs(true)))));
            String launcherData = BaseInfo.getLauncherData(iApp.obtainAppId());
            if (!PdrUtil.isEmpty(iApp.obtainWebAppIntent())) {
                String stringExtra = iApp.obtainWebAppIntent().getStringExtra(IntentConst.FROM_STREAM_OPEN_FLAG);
                if (!TextUtils.isEmpty(stringExtra) && !TextUtils.equals(stringExtra, iApp.obtainAppId())) {
                    launcherData = launcherData + ":" + stringExtra;
                }
            }
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_LAUNCHER, launcherData)));
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_CHANNEL, BaseInfo.getAnalysisChannel())));
            String bundleData = SP.getBundleData((Context) iApp.getActivity(), "pdr", iApp.obtainAppId() + AbsoluteConst.LAUNCHTYPE);
            if (TextUtils.isEmpty(bundleData)) {
                bundleData = "default";
            }
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_ORIGIN, bundleData)));
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_STARTUPTIME, String.valueOf(BaseInfo.getStartupTimeData(iApp.obtainAppId())))));
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format(AbsoluteConst.JS_RUNTIME_PROCESSID, Long.valueOf(BaseInfo.sProcessId))));
            stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format("p.runtime.versionCode = %d;", Integer.valueOf(AndroidResources.versionCode))));
            if (BaseInfo.isUniAppAppid(iApp)) {
                stringBuffer.append(StringUtil.format(AbsoluteConst.JS_RUNTIME_BASE, StringUtil.format("p.runtime.uniVersion = '%s';", BaseInfo.uniVersionV3)));
            }
        }
        if (PermissionControler.checkPermission(iApp.obtainAppId(), IFeature.F_NAVIGATOR)) {
            DeviceInfo.updateStatusBarHeight(iWebview.getActivity());
            stringBuffer.append(String.format(Locale.UK, AbsoluteConst.JS_NAVIGATOR_STATUSBAR_HEIGHT, new Object[]{Float.valueOf(((float) DeviceInfo.sStatusBarHeight) / iWebview.getScale())}));
        }
        int size = this.g.size();
        for (int i3 = 0; i3 < size; i3++) {
            stringBuffer.append(this.g.get(i3));
        }
        stringBuffer.append(a());
        stringBuffer.append("}catch(e){console.log('__load__plus__ function error=' + e);}}");
        stringBuffer.append(AbsoluteConst.EXECUTE_LOAD_FUNS_FUN);
        stringBuffer.append(a(iApp));
        stringBuffer.append(b(iApp));
        return stringBuffer.toString();
    }

    private void b() {
        try {
            this.i = (c) Class.forName("io.dcloud.feature.d").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{getContext()});
        } catch (Exception unused) {
            Logger.e("fmgr no dp");
        }
    }

    private boolean b(String str) {
        return this.b.containsKey(str);
    }

    private String a(IApp iApp) {
        if (BaseInfo.isUniNViewBackgroud()) {
            String str = (String) processEvent(IMgr.MgrType.FeatureMgr, 10, new Object[]{iApp, "weex,io.dcloud.feature.weex.WeexFeature", "getUniNViewModules", null});
            if (!PdrUtil.isEmpty(str)) {
                return "window.__NATIVE_PLUGINS__ = " + str + ";window.__NATIVE_PLUGINS_REGISTERED__ = true;";
            }
        }
        return "";
    }

    private String a() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(";Object.defineProperty(plus.screen,\"resolutionHeight\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"s.resolutionHeight\",[])}}),Object.defineProperty(plus.screen,\"resolutionWidth\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"s.resolutionWidth\",[])}}),Object.defineProperty(plus.display,\"resolutionHeight\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"d.resolutionHeight\",[])}}),Object.defineProperty(plus.display,\"resolutionWidth\",{get:function(){var e=window,l=e.__html5plus__&&e.__html5plus__.isReady?e.__html5plus__:n.plus&&n.plus.isReady?n.plus:window.plus;return l.bridge.execSync(\"Device\",\"d.resolutionWidth\",[])}});");
        stringBuffer.append(";plus.webview.__test__('save');");
        if (Build.VERSION.SDK_INT > 19) {
            stringBuffer.append("plus.webview.__test__('update');");
        }
        return stringBuffer.toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.lang.String[]} */
    /* JADX WARNING: type inference failed for: r6v0 */
    /* JADX WARNING: type inference failed for: r6v5 */
    /* JADX WARNING: type inference failed for: r6v6 */
    /* JADX WARNING: type inference failed for: r6v7 */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(java.lang.Object[] r9) {
        /*
            r8 = this;
            r0 = 0
            r1 = r9[r0]
            io.dcloud.common.DHInterface.IWebview r1 = (io.dcloud.common.DHInterface.IWebview) r1
            r2 = 1
            r3 = r9[r2]
            java.lang.String r3 = java.lang.String.valueOf(r3)
            java.util.Locale r4 = java.util.Locale.ENGLISH
            java.lang.String r3 = r3.toLowerCase(r4)
            r4 = 2
            r4 = r9[r4]
            java.lang.String r4 = java.lang.String.valueOf(r4)
            r5 = 3
            r9 = r9[r5]
            org.json.JSONArray r9 = (org.json.JSONArray) r9
            r6 = 0
            if (r1 == 0) goto L_0x00bb
            io.dcloud.common.DHInterface.IApp r7 = r1.obtainApp()
            if (r7 == 0) goto L_0x00bb
            io.dcloud.common.DHInterface.IFrameView r7 = r1.obtainFrameView()
            if (r7 == 0) goto L_0x00bb
            io.dcloud.common.DHInterface.IFrameView r7 = r1.obtainFrameView()
            io.dcloud.common.DHInterface.IWebview r7 = r7.obtainWebView()
            if (r7 != 0) goto L_0x0039
            goto L_0x00bb
        L_0x0039:
            android.app.Activity r7 = r1.getActivity()
            io.dcloud.common.DHInterface.IFeature r7 = r8.b(r3, r7)
            if (r7 == 0) goto L_0x0065
            boolean r0 = r7 instanceof io.dcloud.common.DHInterface.BaseFeature
            if (r0 == 0) goto L_0x0055
            r0 = r7
            io.dcloud.common.DHInterface.BaseFeature r0 = (io.dcloud.common.DHInterface.BaseFeature) r0
            boolean r2 = r0.isOldMode()
            if (r2 != 0) goto L_0x0055
            java.lang.String r9 = r0.execute((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r4, (org.json.JSONArray) r9)
            return r9
        L_0x0055:
            if (r9 == 0) goto L_0x0060
            java.lang.String[] r6 = io.dcloud.common.util.JSUtil.jsonArrayToStringArr(r9)     // Catch:{ JSONException -> 0x005c }
            goto L_0x0060
        L_0x005c:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0060:
            java.lang.String r9 = r7.execute(r1, r4, r6)
            return r9
        L_0x0065:
            io.dcloud.common.DHInterface.IFrameView r9 = r1.obtainFrameView()
            int r9 = r9.getFrameType()
            java.lang.String r6 = ""
            if (r9 == r5) goto L_0x00bb
            io.dcloud.common.DHInterface.IApp r9 = r1.obtainApp()
            java.lang.String r9 = r9.obtainAppId()
            boolean r9 = io.dcloud.common.core.permission.PermissionControler.checkSafePermission(r9, r3)
            if (r9 == 0) goto L_0x0080
            goto L_0x00bb
        L_0x0080:
            android.content.Context r9 = r8.getContext()
            int r5 = io.dcloud.base.R.string.dcloud_feature_error_tips
            java.lang.String r9 = r9.getString(r5)
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r0] = r3
            java.lang.String r9 = io.dcloud.common.util.StringUtil.format(r9, r2)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "not found "
            r0.append(r2)
            r0.append(r3)
            java.lang.String r2 = " feature plugin ; action="
            r0.append(r2)
            r0.append(r4)
            java.lang.String r2 = ";"
            r0.append(r2)
            r0.append(r9)
            java.lang.String r9 = r0.toString()
            java.lang.String r0 = "featuremgr"
            io.dcloud.common.adapter.util.Logger.e(r0, r9)
            r8.a((io.dcloud.common.DHInterface.IWebview) r1, (java.lang.String) r3)
        L_0x00bb:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.g.b.a(java.lang.Object[]):java.lang.String");
    }

    private Object a(String str, Activity activity) {
        c cVar = this.i;
        if (cVar != null) {
            return cVar.a(str, activity);
        }
        return null;
    }

    private HashMap<String, IBoot> a(HashMap<String, IBoot> hashMap) {
        if (this.i == null) {
            return hashMap;
        }
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        Set<String> keySet = this.i.a().keySet();
        if (keySet != null) {
            for (String str : keySet) {
                Object a2 = this.i.a(str);
                if (a2 != null) {
                    if (a2 instanceof BaseFeature) {
                        ((BaseFeature) a2).init(this, str);
                    }
                    hashMap.put(str, (IBoot) a2);
                }
            }
        }
        return hashMap;
    }

    private void a(Iterator<String> it) {
        if (!SDK.isUniMPSDK()) {
            while (it.hasNext()) {
                String valueOf = String.valueOf(PlatformUtil.invokeMethod(it.next(), "getJsContent"));
                if (!PdrUtil.isEmpty(valueOf)) {
                    this.g.add(valueOf);
                }
            }
        }
    }

    public void a(String str) {
        Collection<IFeature> values;
        HashMap<String, IFeature> hashMap = this.c;
        if (!(hashMap == null || (values = hashMap.values()) == null)) {
            for (IFeature dispose : values) {
                try {
                    dispose.dispose(str);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        HashMap<String, String> hashMap2 = this.h;
        if (hashMap2 != null) {
            String str2 = null;
            Iterator<String> it = hashMap2.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String next = it.next();
                if (next.startsWith(str + "_")) {
                    str2 = next;
                    break;
                }
            }
            if (!TextUtils.isEmpty(str2)) {
                this.h.remove(str2);
            }
        }
    }

    public void a(IWebview iWebview, String str) {
        Dialog lossDialog = ErrorDialogUtil.getLossDialog(iWebview, StringUtil.format(iWebview.getContext().getString(R.string.dcloud_feature_error_tips2) + "http://ask.dcloud.net.cn/article/283", str), "http://ask.dcloud.net.cn/article/283", str);
        if (lossDialog != null) {
            lossDialog.show();
        }
    }
}
