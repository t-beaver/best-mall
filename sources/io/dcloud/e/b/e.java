package io.dcloud.e.b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.WebAppActivity;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IBoot;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IConfusionMgr;
import io.dcloud.common.DHInterface.IDCloudWebviewClientListener;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IPdrModule;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.DHInterface.ReceiveSystemEventVoucher;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.UnicodeInputStream;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.core.permission.PermissionControler;
import io.dcloud.common.util.ADUtils;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.AppStatus;
import io.dcloud.common.util.AppStatusBarManager;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CreateShortResultReceiver;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.ZipUtils;
import io.dcloud.feature.gg.dcloud.ADSim;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class e extends f implements IApp, ISysEventListener {
    public static String r = "webapp";
    String A = "";
    private String A0 = "fast";
    private String A1 = "none";
    String B = "";
    ArrayList<String> B0 = null;
    private String B1 = AbsoluteConst.INSTALL_OPTIONS_FORCE;
    String C = "";
    HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> C0 = null;
    private String C1 = null;
    String D = null;
    JSONObject D0 = null;
    private String D1 = null;
    String E = "";
    JSONObject E0 = null;
    boolean E1 = true;
    String F = null;
    JSONObject F0 = null;
    HashMap<String, Integer> F1 = null;
    String G = null;
    JSONObject G0 = null;
    String G1 = null;
    String H = null;
    JSONObject H0 = null;
    boolean H1 = false;
    String I = null;
    JSONObject I0 = null;
    String J = null;
    JSONObject J0 = null;
    String K = null;
    JSONObject K0 = null;
    String L = null;
    JSONObject L0 = null;
    boolean M = true;
    String M0 = null;
    boolean N = true;
    String N0 = null;
    boolean O = true;
    Intent O0 = null;
    boolean P = false;
    IApp.IAppStatusListener P0 = null;
    boolean Q = false;
    String Q0 = null;
    boolean R = true;
    private String R0;
    boolean S = false;
    private String S0 = "none";
    private String T = null;
    boolean T0 = false;
    boolean U = false;
    private boolean U0 = false;
    /* access modifiers changed from: private */
    public byte V = 1;
    private boolean V0 = false;
    private boolean W = false;
    private String W0 = "default";
    private boolean X = false;
    private String X0 = null;
    private boolean Y = true;
    private String Y0 = null;
    private boolean Z = true;
    private String Z0 = null;
    private int a0 = ADSim.INTISPLSH;
    private String a1 = "";
    private int b0 = 0;
    protected boolean b1 = false;
    private int c0 = 0;
    private boolean c1 = false;
    private String d0 = null;
    private boolean d1 = false;
    private String e0 = null;
    private String e1 = null;
    private String f0;
    private String f1 = null;
    private String g0;
    private boolean g1 = false;
    private String h0;
    long h1 = 0;
    private String i0;
    boolean i1 = true;
    boolean j0 = false;
    boolean j1 = false;
    /* access modifiers changed from: private */
    public String k0 = null;
    boolean k1 = false;
    String l0 = null;
    ArrayList<String> l1 = new ArrayList<>();
    String m0 = null;
    ArrayList<String> m1 = new ArrayList<>();
    String n0 = null;
    String n1 = null;
    boolean o0 = false;
    String o1 = null;
    String p0 = "accept";
    private String p1 = "";
    String q0 = "file:///android_asset/data/dcloud_error.html";
    private boolean q1 = false;
    String r0 = null;
    private String r1 = null;
    g s = null;
    private String s0 = null;
    private boolean s1 = false;
    BaseInfo.BaseAppInfo t = null;
    String t0 = null;
    private int t1 = 1;
    byte u = 1;
    private String u0 = "-1";
    private IConfusionMgr u1;
    boolean v = false;
    private JSONObject v0 = null;
    private String v1;
    boolean w = false;
    private String w0 = "";
    IWebviewStateListener w1 = null;
    boolean x = false;
    private boolean x0 = true;
    boolean x1 = false;
    a y = null;
    private boolean y0 = false;
    JSONObject y1 = null;
    String z = null;
    private String z0 = AbsoluteConst.UNI_V3;
    private boolean z1 = false;

    class a implements Runnable {
        a() {
        }

        public void run() {
            try {
                DHFile.deleteFile(BaseInfo.sBaseWap2AppTemplatePath + "wap2app_temp/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class b implements Runnable {
        b() {
        }

        public void run() {
            try {
                DHFile.deleteFile(BaseInfo.sBaseWap2AppTemplatePath + "wap2app_temp/");
                DHFile.deleteFile(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template.zip");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class c implements Runnable {
        final /* synthetic */ ICallBack a;

        class a implements MessageHandler.IMessages {
            a() {
            }

            public void execute(Object obj) {
                c.this.a.onCallBack(0, (Object) null);
            }
        }

        c(ICallBack iCallBack) {
            this.a = iCallBack;
        }

        public void run() {
            String str = BaseInfo.sCacheFsAppsPath + e.this.p + DeviceInfo.sSeparatorChar + BaseInfo.APP_WWW_FS_DIR;
            long currentTimeMillis = System.currentTimeMillis();
            Logger.d(e.r, e.this.p + " copy resoure begin!!!");
            DHFile.delete(str);
            DHFile.copyDir(e.this.k0, str);
            long currentTimeMillis2 = System.currentTimeMillis();
            Logger.d(e.r, e.this.p + " copy resoure end!!! useTime=" + (currentTimeMillis2 - currentTimeMillis));
            byte unused = e.this.V = (byte) 0;
            e.this.setAppDataPath(str);
            e eVar = e.this;
            BaseInfo.BaseAppInfo baseAppInfo = eVar.t;
            if (baseAppInfo != null) {
                baseAppInfo.saveToBundleData(eVar.getActivity());
            }
            MessageHandler.sendMessage(new a(), (Object) null);
        }
    }

    class d implements Runnable {
        final /* synthetic */ String a;

        d(String str) {
            this.a = str;
        }

        public void run() {
            TestUtil.PointTime.commitTid(e.this.getActivity(), this.a, (String) null, e.this.N0, 1);
        }
    }

    /* renamed from: io.dcloud.e.b.e$e  reason: collision with other inner class name */
    class C0029e implements Runnable {
        C0029e() {
        }

        public void run() {
            try {
                DHFile.deleteFile(e.this.obtainAppTempPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static /* synthetic */ class f {
        static final /* synthetic */ int[] a;

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|(3:17|18|20)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo[] r0 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                a = r0
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.OverrideUrlJsonData     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.OverrideResourceJsonData     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SecondWebviewJsonData     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.LaunchWebviewJsonData     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.TitleNViewJsonData     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0049 }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.SitemapJsonData     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0054 }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.URDJsonData     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0060 }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.DirectPageJsonData     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x006c }
                io.dcloud.common.DHInterface.IApp$ConfigProperty$ThridInfo r1 = io.dcloud.common.DHInterface.IApp.ConfigProperty.ThridInfo.Tabbar     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.f.<clinit>():void");
        }
    }

    e(a aVar, String str, byte b2) {
        this.y = aVar;
        this.p = str;
        b(b2);
        this.u1 = io.dcloud.e.c.b.c();
        this.s = new g();
        this.B0 = new ArrayList<>(2);
        this.s1 = AppRuntime.isUniApp(str);
    }

    private void d() {
    }

    private void e() {
        JSONObject jSONObject = this.K0;
        if (jSONObject != null) {
            try {
                String optString = jSONObject.optString("webviewid");
                if (TextUtils.isEmpty(optString)) {
                    this.K0.put("webviewid", IntentConst.DIRECT_PAGE);
                }
                if (this.p.equals(optString)) {
                    this.J0 = this.K0.optJSONObject("titleNView");
                    return;
                }
                JSONObject jSONObject2 = null;
                if (this.K0.has("titleNView")) {
                    jSONObject2 = this.K0.optJSONObject("titleNView");
                }
                if (jSONObject2 == null) {
                    jSONObject2 = new JSONObject();
                    this.K0.put("titleNView", jSONObject2);
                }
                jSONObject2.put("autoBackButton", true);
                if (!jSONObject2.has("homeButton")) {
                    jSONObject2.put("homeButton", true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean e(String str) {
        return false;
    }

    private void f() {
        try {
            int rename = DHFile.rename(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/", BaseInfo.sBaseWap2AppTemplatePath + "wap2app_temp/");
            DHFile.copyDir("data/wap2app", BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/");
            if (rename == 1) {
                ThreadPool.self().addThreadTask(new a());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject h() {
        try {
            InputStream obtainResInStream = obtainResInStream("_www/__template.json");
            if (obtainResInStream == null) {
                return null;
            }
            JSONObject a2 = a(obtainResInStream);
            IOUtil.close(obtainResInStream);
            return a2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject i() {
        b bVar;
        ThreadPool threadPool;
        JSONObject jSONObject = null;
        try {
            boolean z2 = true;
            boolean z3 = false;
            if (BaseInfo.sCoverApkRuning) {
                if (new File(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/" + "__template.json").exists()) {
                    InputStream inputStream = PlatformUtil.getInputStream(BaseInfo.sBaseConfigTemplatePath);
                    JSONObject a2 = a(inputStream);
                    String optString = a2.optString("version");
                    IOUtil.close(inputStream);
                    InputStream inputStream2 = DHFile.getInputStream(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/" + "__template.json");
                    JSONObject a3 = a(inputStream2);
                    String optString2 = a3.optString("version");
                    IOUtil.close(inputStream2);
                    BaseInfo.mWap2appTemplateFiles.clear();
                    BaseInfo.mW2AE.clear();
                    if (BaseInfo.BaseAppInfo.compareVersion(optString, optString2)) {
                        f();
                        jSONObject = a2;
                        z3 = true;
                    } else {
                        jSONObject = a3;
                    }
                }
            }
            if (DHFile.isExist(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template.zip")) {
                DHFile.rename(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/", BaseInfo.sBaseWap2AppTemplatePath + "wap2app_temp/");
                try {
                    ZipUtils.upZipFile(new File(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template.zip"), BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/");
                    threadPool = ThreadPool.self();
                    bVar = new b();
                } catch (IOException e) {
                    e.printStackTrace();
                    threadPool = ThreadPool.self();
                    bVar = new b();
                    z2 = z3;
                }
                threadPool.addThreadTask(bVar);
            } else {
                if (!new File(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/" + "__template.json").exists()) {
                    f();
                } else {
                    z2 = z3;
                }
            }
            if (!z2) {
                if (!(TextUtils.isEmpty(BaseInfo.sWap2AppTemplateVersion) || BaseInfo.mWap2appTemplateFiles.size() == 0 || this.u1.getData("__w2a__template__") == null)) {
                    return jSONObject;
                }
            }
            if (!DHFile.isExist(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/" + "__template.json")) {
                return jSONObject;
            }
            InputStream inputStream3 = DHFile.getInputStream(BaseInfo.sBaseWap2AppTemplatePath + "wap2app__template/" + "__template.json");
            JSONObject a4 = a(inputStream3);
            IOUtil.close(inputStream3);
            BaseInfo.mWap2appTemplateFiles.clear();
            BaseInfo.mW2AE.clear();
            this.u1.removeData("__w2a__template__");
            return a4;
        } catch (Exception e2) {
            e2.printStackTrace();
            return jSONObject;
        } catch (Throwable th) {
            ThreadPool.self().addThreadTask(new b());
            throw th;
        }
    }

    private void l() {
        String[] split;
        this.F1 = new HashMap<>();
        String string = SP.getOrCreateBundle((Context) getActivity(), this.p + "_" + 1).getString("Authorize", (String) null);
        this.G1 = string;
        if (string != null && (split = string.split("&")) != null && split.length > 0) {
            for (String str : split) {
                if (!TextUtils.isEmpty(str)) {
                    String[] split2 = str.split("=");
                    this.F1.put(split2[0], Integer.valueOf(Integer.parseInt(split2[1])));
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0196, code lost:
        if (r1.u1.getData("__w2a__template__") == null) goto L_0x0198;
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0066 A[Catch:{ Exception -> 0x01f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0190 A[SYNTHETIC, Splitter:B:54:0x0190] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x01ae A[Catch:{ Exception -> 0x01f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01bc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void x() {
        /*
            r16 = this;
            r1 = r16
            java.lang.String r0 = "confusion"
            java.lang.String r2 = "files"
            java.lang.String r3 = "__template.json"
            java.lang.String r4 = r1.p
            boolean r4 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r4)
            if (r4 != 0) goto L_0x0011
            return
        L_0x0011:
            org.json.JSONObject r4 = r16.i()
            java.lang.String r5 = "version"
            if (r4 != 0) goto L_0x001d
            java.lang.String r6 = io.dcloud.common.util.BaseInfo.sWap2AppTemplateVersion
            goto L_0x0021
        L_0x001d:
            java.lang.String r6 = r4.optString(r5)
        L_0x0021:
            org.json.JSONObject r7 = r16.h()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r8 = "wap2app__template/"
            r10 = 1
            if (r7 == 0) goto L_0x005d
            java.lang.String r5 = r7.optString(r5)     // Catch:{ Exception -> 0x01f2 }
            boolean r11 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x01f2 }
            if (r11 != 0) goto L_0x005d
            boolean r11 = io.dcloud.common.util.BaseInfo.BaseAppInfo.compareVersion(r5, r6)     // Catch:{ Exception -> 0x01f2 }
            if (r11 == 0) goto L_0x005d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r4.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r6 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r4.append(r6)     // Catch:{ Exception -> 0x01f2 }
            r4.append(r8)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x01f2 }
            io.dcloud.common.adapter.io.DHFile.deleteFile(r4)     // Catch:{ Exception -> 0x01f2 }
            java.util.ArrayList<java.lang.String> r4 = io.dcloud.common.util.BaseInfo.mWap2appTemplateFiles     // Catch:{ Exception -> 0x01f2 }
            r4.clear()     // Catch:{ Exception -> 0x01f2 }
            java.util.HashMap<java.lang.String, byte[]> r4 = io.dcloud.common.util.BaseInfo.mW2AE     // Catch:{ Exception -> 0x01f2 }
            r4.clear()     // Catch:{ Exception -> 0x01f2 }
            r6 = r5
            r4 = r7
            r5 = 1
            goto L_0x005e
        L_0x005d:
            r5 = 0
        L_0x005e:
            java.util.ArrayList<java.lang.String> r7 = io.dcloud.common.util.BaseInfo.mWap2appTemplateFiles     // Catch:{ Exception -> 0x01f2 }
            int r7 = r7.size()     // Catch:{ Exception -> 0x01f2 }
            if (r7 != 0) goto L_0x018c
            if (r4 == 0) goto L_0x0161
            boolean r7 = r4.has(r2)     // Catch:{ Exception -> 0x01f2 }
            if (r7 == 0) goto L_0x0161
            org.json.JSONArray r2 = r4.optJSONArray(r2)     // Catch:{ Exception -> 0x01f2 }
            int r7 = r2.length()     // Catch:{ Exception -> 0x01f2 }
            byte r11 = r16.obtainRunningAppMode()     // Catch:{ Exception -> 0x01f2 }
            if (r11 != r10) goto L_0x007e
            r11 = 1
            goto L_0x007f
        L_0x007e:
            r11 = 0
        L_0x007f:
            r12 = 0
        L_0x0080:
            if (r12 >= r7) goto L_0x00f6
            java.lang.String r13 = r2.optString(r12)     // Catch:{ Exception -> 0x01f2 }
            java.util.ArrayList<java.lang.String> r14 = io.dcloud.common.util.BaseInfo.mWap2appTemplateFiles     // Catch:{ Exception -> 0x01f2 }
            r14.add(r13)     // Catch:{ Exception -> 0x01f2 }
            if (r5 == 0) goto L_0x00f3
            java.lang.String r14 = "/www/"
            if (r11 == 0) goto L_0x00c2
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r15.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.sBaseResAppsPath     // Catch:{ Exception -> 0x01f2 }
            r15.append(r9)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = r1.p     // Catch:{ Exception -> 0x01f2 }
            r15.append(r9)     // Catch:{ Exception -> 0x01f2 }
            r15.append(r14)     // Catch:{ Exception -> 0x01f2 }
            r15.append(r13)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = r15.toString()     // Catch:{ Exception -> 0x01f2 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r14.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r15 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r14.append(r15)     // Catch:{ Exception -> 0x01f2 }
            r14.append(r8)     // Catch:{ Exception -> 0x01f2 }
            r14.append(r13)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r13 = r14.toString()     // Catch:{ Exception -> 0x01f2 }
            io.dcloud.common.adapter.io.DHFile.copyAssetsFile(r9, r13)     // Catch:{ Exception -> 0x01f2 }
            goto L_0x00f3
        L_0x00c2:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r9.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r15 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath     // Catch:{ Exception -> 0x01f2 }
            r9.append(r15)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r15 = r1.p     // Catch:{ Exception -> 0x01f2 }
            r9.append(r15)     // Catch:{ Exception -> 0x01f2 }
            r9.append(r14)     // Catch:{ Exception -> 0x01f2 }
            r9.append(r13)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x01f2 }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r14.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r15 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r14.append(r15)     // Catch:{ Exception -> 0x01f2 }
            r14.append(r8)     // Catch:{ Exception -> 0x01f2 }
            r14.append(r13)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r13 = r14.toString()     // Catch:{ Exception -> 0x01f2 }
            r14 = 0
            io.dcloud.common.adapter.io.DHFile.copyFile(r9, r13, r10, r14)     // Catch:{ Exception -> 0x01f2 }
        L_0x00f3:
            int r12 = r12 + 1
            goto L_0x0080
        L_0x00f6:
            if (r5 == 0) goto L_0x018c
            java.util.ArrayList<java.lang.String> r2 = io.dcloud.common.util.BaseInfo.mWap2appTemplateFiles     // Catch:{ Exception -> 0x01f2 }
            boolean r2 = r2.contains(r3)     // Catch:{ Exception -> 0x01f2 }
            if (r2 != 0) goto L_0x018c
            java.lang.String r2 = "/www/__template.json"
            if (r11 == 0) goto L_0x0132
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r7.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.sBaseResAppsPath     // Catch:{ Exception -> 0x01f2 }
            r7.append(r9)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = r1.p     // Catch:{ Exception -> 0x01f2 }
            r7.append(r9)     // Catch:{ Exception -> 0x01f2 }
            r7.append(r2)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r2 = r7.toString()     // Catch:{ Exception -> 0x01f2 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r7.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r7.append(r9)     // Catch:{ Exception -> 0x01f2 }
            r7.append(r8)     // Catch:{ Exception -> 0x01f2 }
            r7.append(r3)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r3 = r7.toString()     // Catch:{ Exception -> 0x01f2 }
            io.dcloud.common.adapter.io.DHFile.copyAssetsFile(r2, r3)     // Catch:{ Exception -> 0x01f2 }
            goto L_0x018c
        L_0x0132:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r7.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath     // Catch:{ Exception -> 0x01f2 }
            r7.append(r9)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = r1.p     // Catch:{ Exception -> 0x01f2 }
            r7.append(r9)     // Catch:{ Exception -> 0x01f2 }
            r7.append(r2)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r2 = r7.toString()     // Catch:{ Exception -> 0x01f2 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r7.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r7.append(r9)     // Catch:{ Exception -> 0x01f2 }
            r7.append(r8)     // Catch:{ Exception -> 0x01f2 }
            r7.append(r3)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r3 = r7.toString()     // Catch:{ Exception -> 0x01f2 }
            r7 = 0
            io.dcloud.common.adapter.io.DHFile.copyFile(r2, r3, r10, r7)     // Catch:{ Exception -> 0x01f2 }
            goto L_0x018c
        L_0x0161:
            r7 = 0
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x01f2 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r3.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r3.append(r9)     // Catch:{ Exception -> 0x01f2 }
            r3.append(r8)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x01f2 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String[] r2 = r2.list()     // Catch:{ Exception -> 0x01f2 }
            if (r2 == 0) goto L_0x018c
            r9 = 0
        L_0x017f:
            int r3 = r2.length     // Catch:{ Exception -> 0x01f2 }
            if (r9 >= r3) goto L_0x018c
            java.util.ArrayList<java.lang.String> r3 = io.dcloud.common.util.BaseInfo.mWap2appTemplateFiles     // Catch:{ Exception -> 0x01f2 }
            r7 = r2[r9]     // Catch:{ Exception -> 0x01f2 }
            r3.add(r7)     // Catch:{ Exception -> 0x01f2 }
            int r9 = r9 + 1
            goto L_0x017f
        L_0x018c:
            java.lang.String r2 = "__w2a__template__"
            if (r5 != 0) goto L_0x0198
            io.dcloud.common.DHInterface.IConfusionMgr r3 = r1.u1     // Catch:{ Exception -> 0x01f2 }
            java.util.Map r3 = r3.getData(r2)     // Catch:{ Exception -> 0x01f2 }
            if (r3 != 0) goto L_0x01ce
        L_0x0198:
            if (r4 == 0) goto L_0x01ce
            boolean r3 = r4.has(r0)     // Catch:{ Exception -> 0x01f2 }
            if (r3 == 0) goto L_0x01ce
            java.lang.String r0 = r4.optString(r0)     // Catch:{ Exception -> 0x01f2 }
            byte[] r0 = io.dcloud.common.util.Base64.decode2bytes(r0)     // Catch:{ Exception -> 0x01f2 }
            android.app.Activity r3 = r16.getActivity()     // Catch:{ Exception -> 0x01f2 }
            if (r3 != 0) goto L_0x01b0
            android.content.Context r3 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch:{ Exception -> 0x01f2 }
        L_0x01b0:
            io.dcloud.common.DHInterface.IConfusionMgr r4 = r1.u1     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r0 = r4.handleEncryption(r3, r0)     // Catch:{ Exception -> 0x01f2 }
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r0)     // Catch:{ Exception -> 0x01f2 }
            if (r3 == 0) goto L_0x01bf
            java.lang.String r0 = "{}"
        L_0x01bf:
            io.dcloud.common.DHInterface.IConfusionMgr r3 = r1.u1     // Catch:{ Exception -> 0x01f2 }
            r3.removeData(r2)     // Catch:{ Exception -> 0x01f2 }
            io.dcloud.common.DHInterface.IConfusionMgr r3 = r1.u1     // Catch:{ Exception -> 0x01f2 }
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ Exception -> 0x01f2 }
            r4.<init>(r0)     // Catch:{ Exception -> 0x01f2 }
            r3.recordEncryptionResources(r2, r4)     // Catch:{ Exception -> 0x01f2 }
        L_0x01ce:
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x01f2 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01f2 }
            r2.<init>()     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.sBaseWap2AppTemplatePath     // Catch:{ Exception -> 0x01f2 }
            r2.append(r3)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r3 = "wap2app__template/__template.json"
            r2.append(r3)     // Catch:{ Exception -> 0x01f2 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x01f2 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x01f2 }
            long r2 = r0.lastModified()     // Catch:{ Exception -> 0x01f2 }
            io.dcloud.common.util.BaseInfo.sTemplateModifyTime = r2     // Catch:{ Exception -> 0x01f2 }
            io.dcloud.common.util.BaseInfo.sWap2AppTemplateVersion = r6     // Catch:{ Exception -> 0x01f2 }
            r1.D = r6     // Catch:{ Exception -> 0x01f2 }
            goto L_0x01f6
        L_0x01f2:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01f6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.x():void");
    }

    public void addAllFeaturePermission() {
        PermissionControler.registerRootPermission(this.p);
    }

    public void addFeaturePermission(String str) {
        this.B0.add(str.toLowerCase(Locale.ENGLISH));
    }

    public void applyMani() {
        try {
            a(DHFile.getInputStream(DHFile.createFileHandler(a(BaseInfo.sConfigXML))), this.p, (JSONObject) null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applySmartUpdate() {
        a(false);
    }

    /* access modifiers changed from: package-private */
    public void b(InputStream inputStream) {
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0053 A[Catch:{ Exception -> 0x00e1, all -> 0x00df }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0058 A[Catch:{ Exception -> 0x00e1, all -> 0x00df }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005b A[Catch:{ Exception -> 0x00e1, all -> 0x00df }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a2 A[SYNTHETIC, Splitter:B:42:0x00a2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean b(java.lang.String r6, org.json.JSONObject r7) {
        /*
            r5 = this;
            r0 = 0
            r1 = 0
            r5.p = r6     // Catch:{ Exception -> 0x00e1 }
            io.dcloud.common.DHInterface.IConfusionMgr r2 = r5.u1     // Catch:{ Exception -> 0x00e1 }
            r2.removeData(r6)     // Catch:{ Exception -> 0x00e1 }
            r5.k()     // Catch:{ Exception -> 0x00e1 }
            byte r2 = r5.V     // Catch:{ Exception -> 0x00e1 }
            r3 = 1
            if (r2 == 0) goto L_0x002d
            java.util.HashMap<java.lang.String, io.dcloud.common.util.BaseInfo$BaseAppInfo> r2 = io.dcloud.common.util.BaseInfo.mBaseAppInfoSet     // Catch:{ Exception -> 0x00e1 }
            if (r2 == 0) goto L_0x001e
            java.lang.String r4 = r5.p     // Catch:{ Exception -> 0x00e1 }
            boolean r2 = r2.containsKey(r4)     // Catch:{ Exception -> 0x00e1 }
            if (r2 != 0) goto L_0x001e
            goto L_0x002d
        L_0x001e:
            byte r2 = r5.V     // Catch:{ Exception -> 0x00e1 }
            if (r2 != r3) goto L_0x004b
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sConfigXML     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r2 = r5.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00e1 }
            java.io.InputStream r1 = io.dcloud.common.adapter.util.PlatformUtil.getResInputStream(r2)     // Catch:{ Exception -> 0x00e1 }
            goto L_0x004b
        L_0x002d:
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sConfigXML     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r2 = r5.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00e1 }
            java.lang.Object r2 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r2)     // Catch:{ Exception -> 0x00e1 }
            java.io.InputStream r1 = io.dcloud.common.adapter.io.DHFile.getInputStream(r2)     // Catch:{ Exception -> 0x00e1 }
            if (r1 != 0) goto L_0x004b
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sConfigXML     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r2 = r5.a((java.lang.String) r2)     // Catch:{ Exception -> 0x00e1 }
            java.io.InputStream r1 = io.dcloud.common.adapter.util.PlatformUtil.getResInputStream(r2)     // Catch:{ Exception -> 0x00e1 }
            if (r1 == 0) goto L_0x004b
            r5.V = r3     // Catch:{ Exception -> 0x00e1 }
        L_0x004b:
            java.lang.String r2 = r5.p     // Catch:{ Exception -> 0x00e1 }
            boolean r2 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r2)     // Catch:{ Exception -> 0x00e1 }
            if (r2 == 0) goto L_0x0058
            r5.x()     // Catch:{ Exception -> 0x00e1 }
            r2 = 0
            goto L_0x0059
        L_0x0058:
            r2 = 1
        L_0x0059:
            if (r1 != 0) goto L_0x00a2
            java.lang.String r6 = r5.p     // Catch:{ Exception -> 0x00e1 }
            boolean r6 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r6)     // Catch:{ Exception -> 0x00e1 }
            if (r6 == 0) goto L_0x006f
            java.lang.String r6 = r5.n1     // Catch:{ Exception -> 0x00e1 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x00e1 }
            if (r6 != 0) goto L_0x006f
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            return r3
        L_0x006f:
            boolean r6 = r5.q()     // Catch:{ Exception -> 0x00e1 }
            if (r6 == 0) goto L_0x0081
            java.lang.String r6 = r5.r0     // Catch:{ Exception -> 0x00e1 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x00e1 }
            if (r6 != 0) goto L_0x0081
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            return r3
        L_0x0081:
            io.dcloud.e.b.g r6 = r5.s     // Catch:{ Exception -> 0x00e1 }
            r6.a = r3     // Catch:{ Exception -> 0x00e1 }
            boolean r7 = r6.c     // Catch:{ Exception -> 0x00e1 }
            if (r7 == 0) goto L_0x0094
            r7 = -1225(0xfffffffffffffb37, float:NaN)
            java.lang.String r2 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_WWW_MANIFEST_NOT_EXIST     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r7, (java.lang.String) r2)     // Catch:{ Exception -> 0x00e1 }
            r6.b = r7     // Catch:{ Exception -> 0x00e1 }
            goto L_0x009e
        L_0x0094:
            r7 = -1202(0xfffffffffffffb4e, float:NaN)
            java.lang.String r2 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_MANIFEST_NOT_EXIST     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r7, (java.lang.String) r2)     // Catch:{ Exception -> 0x00e1 }
            r6.b = r7     // Catch:{ Exception -> 0x00e1 }
        L_0x009e:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            return r0
        L_0x00a2:
            boolean r0 = r5.a(r1, r6, r7)     // Catch:{ Exception -> 0x00e1 }
            if (r2 == 0) goto L_0x00ab
            r5.x()     // Catch:{ Exception -> 0x00e1 }
        L_0x00ab:
            io.dcloud.e.b.g r6 = r5.s     // Catch:{ Exception -> 0x00e1 }
            if (r6 == 0) goto L_0x00cd
            boolean r6 = r6.a     // Catch:{ Exception -> 0x00e1 }
            if (r6 == 0) goto L_0x00cd
            java.lang.String r6 = "WebApp"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e1 }
            r7.<init>()     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r2 = "InstallError---msg="
            r7.append(r2)     // Catch:{ Exception -> 0x00e1 }
            io.dcloud.e.b.g r2 = r5.s     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r2 = r2.b     // Catch:{ Exception -> 0x00e1 }
            r7.append(r2)     // Catch:{ Exception -> 0x00e1 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00e1 }
            io.dcloud.common.adapter.util.Logger.i(r6, r7)     // Catch:{ Exception -> 0x00e1 }
        L_0x00cd:
            android.app.Activity r6 = r5.getActivity()     // Catch:{ Exception -> 0x00e1 }
            io.dcloud.common.DHInterface.IActivityHandler r6 = io.src.dcloud.adapter.DCloudAdapterUtil.getIActivityHandler(r6)     // Catch:{ Exception -> 0x00e1 }
            if (r6 == 0) goto L_0x00dc
            java.lang.String r7 = r5.t0     // Catch:{ Exception -> 0x00e1 }
            r6.updateSplash(r7)     // Catch:{ Exception -> 0x00e1 }
        L_0x00dc:
            r5.v = r3     // Catch:{ Exception -> 0x00e1 }
            goto L_0x00e7
        L_0x00df:
            r6 = move-exception
            goto L_0x00eb
        L_0x00e1:
            r6 = move-exception
            java.lang.String r7 = "parseConfig"
            io.dcloud.common.adapter.util.Logger.w(r7, r6)     // Catch:{ all -> 0x00df }
        L_0x00e7:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            return r0
        L_0x00eb:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.b(java.lang.String, org.json.JSONObject):boolean");
    }

    /* access modifiers changed from: package-private */
    public void c() {
        Activity activity = this.a;
        if (activity != null && (activity instanceof WebAppActivity)) {
            ((WebAppActivity) activity).onAppActive(this.p);
            ((WebAppActivity) this.a).onAppActive((IApp) this);
        }
        diyStatusBarState();
        setStatus((byte) 3);
        this.b.onAppActive(this);
        callSysEventListener(ISysEventListener.SysEventType.onWebAppForeground, IntentConst.obtainArgs(obtainWebAppIntent(), this.p));
    }

    public boolean callSysEventListener(ISysEventListener.SysEventType sysEventType, Object obj) {
        HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> hashMap = this.C0;
        boolean z2 = false;
        if (hashMap == null) {
            return false;
        }
        ArrayList arrayList = hashMap.get(sysEventType);
        ArrayList arrayList2 = this.C0.get(ISysEventListener.SysEventType.AllSystemEvent);
        ArrayList arrayList3 = new ArrayList();
        if (arrayList != null) {
            arrayList3.addAll(arrayList);
        }
        if (arrayList2 != null) {
            arrayList3.addAll(arrayList2);
        }
        for (int size = arrayList3.size() - 1; size >= 0; size--) {
            ISysEventListener iSysEventListener = (ISysEventListener) arrayList3.get(size);
            if (a(iSysEventListener, sysEventType) && ((z2 = z2 | iSysEventListener.onExecute(sysEventType, obj))) && !a(sysEventType)) {
                break;
            }
        }
        return z2;
    }

    public boolean checkIsCustomPath() {
        return this.o0;
    }

    public void checkOrLoadlaunchWebview() {
        a aVar = this.y;
        if (aVar != null) {
            AdaFrameView adaFrameView = (AdaFrameView) aVar.processEvent(IMgr.MgrType.WindowMgr, 46, obtainAppId());
            Logger.d("Direct_page", "checkOrLoadlaunchWebview " + manifestBeParsed() + ";adaFrameView=" + adaFrameView);
            this.x1 = manifestBeParsed() ^ true;
            if (adaFrameView != null && manifestBeParsed()) {
                adaFrameView.obtainWebView().checkIfNeedLoadOriginalUrl();
            }
        }
    }

    public boolean checkPrivateDir(String str) {
        return str.startsWith(obtainAppDataPath()) || str.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR);
    }

    public String checkPrivateDirAndCopy2Temp(String str) {
        if (obtainRunningAppMode() == 1 && checkPrivateDir(str)) {
            String str2 = "/" + BaseInfo.APP_WWW_FS_DIR;
            String substring = str.substring(str.indexOf(str2) + str2.length());
            String str3 = this.k0 + substring;
            str = obtainAppTempPath() + substring;
            if (!DHFile.exists(str)) {
                DHFile.copyAssetsFile(str3, str);
            }
        }
        return str;
    }

    public boolean checkSchemeWhite(String str) {
        if (!q()) {
            return true;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Iterator<String> it = this.m1.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!TextUtils.equals(next, "*")) {
                if (str.startsWith(next + ":")) {
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkWhiteUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            return this.l1.contains("*") || this.l1.contains(str);
        }
        return false;
    }

    public void clearRuntimeArgs() {
        this.E = "";
    }

    public String convert2AbsFullPath(String str, String str2) {
        boolean z2 = true;
        try {
            if (!PdrUtil.isEmpty(str2) && ((this.V == 1 && PlatformUtil.isResFileExists(str2)) || DHFile.isExist(str2))) {
                return str2;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (PdrUtil.isEmpty(str2)) {
            return str2;
        }
        int indexOf = str2.indexOf(Operators.CONDITION_IF_STRING);
        if (indexOf > 0) {
            str2 = str2.substring(0, indexOf);
        }
        if (str2.startsWith("_documents/")) {
            return BaseInfo.sDocumentFullPath + str2.substring(11);
        } else if (str2.startsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR)) {
            return BaseInfo.sDocumentFullPath + str2.substring(10);
        } else if (str2.startsWith(AbsoluteConst.MINI_SERVER_APP_DOC)) {
            return obtainAppDocPath() + str2.substring(5);
        } else if (str2.startsWith(BaseInfo.REL_PRIVATE_DOC_DIR)) {
            return obtainAppDocPath() + str2.substring(4);
        } else if (str2.startsWith("_downloads/")) {
            return BaseInfo.sDownloadFullPath + str2.substring(11);
        } else if (str2.startsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR)) {
            return BaseInfo.sDownloadFullPath + str2.substring(10);
        } else if (str2.startsWith(AbsoluteConst.MINI_SERVER_APP_WWW)) {
            byte b2 = this.V;
            if (b2 == 1) {
                return BaseInfo.sBaseResAppsPath + this.p + "/" + BaseInfo.APP_WWW_FS_DIR + str2.substring(5);
            } else if (b2 != 0) {
                return str2;
            } else {
                return this.k0 + str2.substring(5);
            }
        } else if (str2.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR)) {
            byte b3 = this.V;
            if (b3 == 1) {
                return BaseInfo.sBaseResAppsPath + this.p + "/" + BaseInfo.APP_WWW_FS_DIR + str2.substring(4);
            } else if (b3 != 0) {
                return str2;
            } else {
                return this.k0 + str2.substring(4);
            }
        } else if (str2.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            return str2.substring(7);
        } else {
            if (str2.startsWith("content://") || str2.startsWith(DeviceInfo.sDeviceRootDir)) {
                return str2;
            }
            if (str2.startsWith("http://localhost")) {
                String substring = str2.substring(16);
                return convert2AbsFullPath((String) null, substring.substring(substring.indexOf("/") + 1));
            }
            if (!str2.startsWith("/") && str != null) {
                z2 = false;
            } else if (str2.startsWith("/")) {
                str2 = str2.substring(1);
            }
            if (str != null) {
                if (str.startsWith(SDK.ANDROID_ASSET)) {
                    str = str.substring(22);
                } else if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                    str = str.substring(7);
                }
            }
            if (str != null && !z2) {
                return PdrUtil.standardizedURL(str, str2);
            }
            if (!z2) {
                return str2;
            }
            String obtainAppDataPath = obtainAppDataPath();
            if (str != null && !PdrUtil.isEquals(str, obtainAppDataPath) && str.contains("/www/")) {
                obtainAppDataPath = str.substring(0, str.indexOf("/www/") + 5);
            }
            return obtainAppDataPath + b(str2);
        }
    }

    public String convert2LocalFullPath(String str, String str2) {
        InputStream inputStream;
        String convert2AbsFullPath = convert2AbsFullPath(str, str2);
        byte b2 = this.V;
        if (b2 == 1 || DeviceInfo.isPrivateDirectory) {
            if (b2 == 1) {
                inputStream = PlatformUtil.getResInputStream(convert2AbsFullPath);
            } else {
                inputStream = PlatformUtil.getInputStream(convert2AbsFullPath);
            }
            if (inputStream != null) {
                convert2AbsFullPath = obtainAppTempPath() + System.currentTimeMillis();
                try {
                    DHFile.writeFile(inputStream, convert2AbsFullPath);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (b2 == 0) {
            return convert2AbsFullPath;
        }
        return convert2AbsFullPath;
    }

    public String convert2RelPath(String str) {
        try {
            int length = obtainAppDataPath().length();
            int length2 = obtainAppDocPath().length();
            int length3 = BaseInfo.sDocumentFullPath.length();
            int length4 = BaseInfo.sDownloadFullPath.length();
            if (str.startsWith(obtainAppDataPath())) {
                return BaseInfo.REL_PRIVATE_WWW_DIR + str.substring(length - 1);
            }
            int i = length - 1;
            if (str.startsWith(obtainAppDataPath().substring(0, i))) {
                return BaseInfo.REL_PRIVATE_WWW_DIR + str.substring(i, str.length());
            } else if (str.startsWith(obtainAppDocPath())) {
                return BaseInfo.REL_PRIVATE_DOC_DIR + str.substring(length2 - 1);
            } else {
                int i2 = length2 - 1;
                if (str.startsWith(obtainAppDocPath().substring(0, i2))) {
                    return BaseInfo.REL_PRIVATE_DOC_DIR + str.substring(i2);
                } else if (str.startsWith(BaseInfo.sDocumentFullPath)) {
                    return BaseInfo.REL_PUBLIC_DOCUMENTS_DIR + str.substring(length3 - 1);
                } else {
                    int i3 = length3 - 1;
                    if (str.startsWith(BaseInfo.sDocumentFullPath.substring(0, i3))) {
                        return BaseInfo.REL_PUBLIC_DOCUMENTS_DIR + str.substring(i3);
                    } else if (str.startsWith(BaseInfo.sDownloadFullPath)) {
                        return BaseInfo.REL_PUBLIC_DOWNLOADS_DIR + str.substring(length4 - 1);
                    } else {
                        int i4 = length4 - 1;
                        if (!str.startsWith(BaseInfo.sDownloadFullPath.substring(0, i4))) {
                            return str;
                        }
                        return BaseInfo.REL_PUBLIC_DOWNLOADS_DIR + str.substring(i4);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public String convert2WebviewFullPath(String str, String str2) {
        if (PdrUtil.isEmpty(str2)) {
            return str2;
        }
        if (this.U) {
            if (str2.startsWith(DeviceInfo.HTTP_PROTOCOL)) {
                return str2;
            }
            return this.s0 + str2;
        } else if (str2.startsWith(DeviceInfo.FILE_PROTOCOL) || str2.startsWith(DeviceInfo.HTTP_PROTOCOL) || str2.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            return str2;
        } else {
            try {
                if (DHFile.isExist(str2)) {
                    return "file:///" + b(str2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (str2.startsWith(DeviceInfo.sDeviceRootDir)) {
                return DeviceInfo.FILE_PROTOCOL + str2;
            }
            boolean z2 = true;
            if (str2.startsWith("/")) {
                str2 = str2.substring(1);
            } else {
                z2 = false;
            }
            if (str2.startsWith(BaseInfo.REL_PRIVATE_WWW_DIR)) {
                return obtainWebviewBaseUrl() + b(str2.substring(4));
            } else if (str2.startsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR)) {
                return DeviceInfo.FILE_PROTOCOL + BaseInfo.sDocumentFullPath + b(str2.substring(10));
            } else if (str2.startsWith(BaseInfo.REL_PRIVATE_DOC_DIR)) {
                return DeviceInfo.FILE_PROTOCOL + obtainAppDocPath() + b(str2.substring(4));
            } else if (str2.startsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR)) {
                return DeviceInfo.FILE_PROTOCOL + BaseInfo.sDownloadFullPath + b(str2.substring(10));
            } else if (str != null && !z2) {
                return PdrUtil.standardizedURL(str, str2);
            } else {
                String obtainWebviewBaseUrl = obtainWebviewBaseUrl();
                if (str != null && !PdrUtil.isEquals(str, obtainWebviewBaseUrl) && str.contains("/www/")) {
                    obtainWebviewBaseUrl = str.substring(0, str.indexOf("/www/") + 5);
                }
                return obtainWebviewBaseUrl + b(str2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public JSONObject d(String str) {
        if (this.y1 == null) {
            m();
        }
        if (this.y1 == null || TextUtils.isEmpty(str)) {
            return null;
        }
        return PdrUtil.getSitemapParameters(this.y1, obtainAppId(), str);
    }

    public void deleteAppTemp() {
        ThreadPool.self().addThreadTask(new C0029e(), true);
    }

    public void diyStatusBarState() {
        AppStatusBarManager appStatusBarManager = this.m;
        if (appStatusBarManager != null) {
            if (this.i) {
                appStatusBarManager.setFullScreen(getActivity(), this.i);
            } else {
                int i = 0;
                if (appStatusBarManager.checkImmersedStatusBar(getActivity(), this.b1)) {
                    BaseInfo.isImmersive = true;
                    this.m.setImmersive(getActivity(), true);
                } else {
                    BaseInfo.isImmersive = false;
                    this.m.setImmersive(getActivity(), false);
                }
                if (Build.VERSION.SDK_INT >= 21 && getActivity() != null) {
                    if (!PdrUtil.isEmpty(this.X0)) {
                        if (this.X0.startsWith("#")) {
                            i = PdrUtil.stringToColor(this.X0);
                        }
                        this.m.setStatusBarColor(getActivity(), i);
                    } else {
                        this.m.setStatusBarColor(getActivity(), BaseInfo.mDeStatusBarBackground);
                    }
                }
                this.m.setStatusBarMode(getActivity(), this.a1);
            }
            if (this.m.isFullScreenOrImmersive()) {
                updateScreenInfo(2);
            }
        }
    }

    public String forceShortCut() {
        return this.B1;
    }

    /* access modifiers changed from: package-private */
    public boolean g(String str) {
        setRuntimeArgs(str);
        setStatus((byte) 3);
        Object processEvent = this.y.processEvent(IMgr.MgrType.WindowMgr, 41, new Object[]{this, convert2WebviewFullPath((String) null, this.r0), Boolean.valueOf(this.T0)});
        if (processEvent == null) {
            return true;
        }
        return Boolean.parseBoolean(String.valueOf(processEvent));
    }

    public IConfusionMgr getConfusionMgr() {
        return this.u1;
    }

    public String getDirectPage() {
        return this.n1;
    }

    public IApp.IAppStatusListener getIAppStatusListener() {
        return this.P0;
    }

    public String getOriginalDirectPage() {
        return this.o1;
    }

    public String getPathByType(byte b2) {
        if (b2 == 0) {
            return obtainAppDataPath();
        }
        if (b2 == 1) {
            return obtainAppDocPath();
        }
        if (b2 == 2) {
            return BaseInfo.sDocumentFullPath;
        }
        if (b2 == 3) {
            return BaseInfo.sDownloadFullPath;
        }
        if (b2 != -1) {
            return null;
        }
        return BaseInfo.sBaseResAppsPath + this.p + "/" + BaseInfo.APP_WWW_FS_DIR;
    }

    public String getPopGesture() {
        return this.S0;
    }

    public int getQuitModel() {
        return this.t1;
    }

    public String getSystemInfo() {
        try {
            if (DeviceInfo.sSystemInfo == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(DeviceInfo.sSystemInfo.toString());
            jSONObject.put("uniCompileVersion", this.u0);
            jSONObject.put("uniRuntimeVersion", BaseInfo.uniVersionV3);
            jSONObject.put("browserName", WebViewFactory.isOther() ? "x5webview" : "chrome");
            jSONObject.put("appId", BaseInfo.sCurrentAppOriginalAppid);
            jSONObject.put(WXConfig.appName, this.t0);
            if (SDK.isUniMP) {
                jSONObject.put(WXConfig.appVersion, this.A);
                jSONObject.put("appVersionCode", this.B);
            } else {
                jSONObject.put(WXConfig.appVersion, b((Context) getActivity()));
                jSONObject.put("appVersionCode", a((Context) getActivity()));
            }
            jSONObject.put("appWgtVersion", this.A);
            return jSONObject.toString();
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean isOnAppRunningMode() {
        return this.V == 1;
    }

    public boolean isUniApp() {
        return this.s1;
    }

    public float j() {
        return (!PermissionControler.checkPermission(this.p, IFeature.F_DEVICE.toLowerCase(Locale.ENGLISH)) || getActivity() == null || NetworkTypeUtil.getNetworkType(getActivity()) != 4) ? 0.0f : 1000.0f;
    }

    /* access modifiers changed from: package-private */
    public void k() {
        if (PdrUtil.isEmpty(this.k0) || !DeviceInfo.startsWithSdcard(this.k0)) {
            setAppDataPath(BaseInfo.sCacheFsAppsPath + this.p + "/" + BaseInfo.REAL_PRIVATE_WWW_DIR);
        }
        if (PdrUtil.isEmpty(this.C1) || !DeviceInfo.startsWithSdcard(this.C1)) {
            setAppDocPath(BaseInfo.sBaseFsAppsPath + this.p + "/" + BaseInfo.REAL_PRIVATE_DOC_DIR);
        }
        if (PdrUtil.isEmpty(this.D1) || !DeviceInfo.startsWithSdcard(this.D1)) {
            this.D1 = BaseInfo.sCacheFsAppsPath + this.p + "/" + BaseInfo.APP_WEB_CHACHE;
        }
    }

    /* access modifiers changed from: package-private */
    public void m() {
        File file = new File(c(this.p));
        if (file.exists()) {
            try {
                JSONObject jSONObject = new JSONObject(IOUtil.toString(new FileInputStream(file)));
                this.y1 = jSONObject;
                this.C = jSONObject.optString("version");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            byte[] fileContent = PlatformUtil.getFileContent("data/sitemap/" + this.p + ".json", 0);
            if (fileContent != null) {
                DHFile.writeFile(fileContent, 0, c(this.p));
                m();
            }
        }
    }

    public boolean manifestBeParsed() {
        return this.v || SDK.IntegratedMode.WEBVIEW == BaseInfo.sRuntimeMode;
    }

    public boolean n() {
        if (!q() || !this.v) {
            return true;
        }
        return this.P;
    }

    public boolean needRefreshApp() {
        return this.k1;
    }

    public boolean needReload() {
        return this.j1;
    }

    public boolean o() {
        String str;
        if (PdrUtil.isEmpty(BaseInfo.uniVersionV3)) {
            String str2 = "uni-jsframework-dev.js";
            if (!BaseInfo.SyncDebug || PlatformUtil.getResInputStream(str2) == null || SDK.isUniMPSDK()) {
                str2 = "uni-jsframework.js";
            }
            try {
                str = new JSONObject(new BufferedReader(new InputStreamReader(getActivity().getAssets().open(str2))).readLine().substring(2)).optString("version");
                BaseInfo.uniVersionV3 = str;
            } catch (IOException | JSONException unused) {
                str = "";
            }
        } else {
            str = BaseInfo.uniVersionV3;
        }
        if ((PdrUtil.isEmpty(this.w0) || !this.w0.contains(str) || this.x0) && !this.y0 && this.v && !this.u0.equals("-1") && !this.u0.trim().equals(str) && !PdrUtil.isEmpty(str) && this.s1) {
            return true;
        }
        return false;
    }

    public String obtainAdaptationJs() {
        if (this.T == null && !PdrUtil.isEmpty(this.K)) {
            byte[] fileContent = PlatformUtil.getFileContent(a(this.K), obtainRunningAppMode() == 1 ? 0 : 2);
            if (fileContent != null) {
                this.T = new String(fileContent);
            } else {
                this.T = "";
            }
        }
        return this.T;
    }

    public String obtainAppDataPath() {
        String str = this.k0;
        if (str != null) {
            return str;
        }
        return this.p + "/www/";
    }

    public String obtainAppDocPath() {
        return this.C1;
    }

    public String obtainAppId() {
        return this.p;
    }

    public String obtainAppInfo() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("appid", this.p);
            jSONObject.put("versionName", this.A);
            jSONObject.put("name", this.t0);
            jSONObject.put("versionCode", this.B);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public String obtainAppLog() {
        return BaseInfo.sBaseFsAppsPath + this.p + "/log/";
    }

    public String obtainAppName() {
        return this.t0;
    }

    public byte obtainAppStatus() {
        return this.u;
    }

    public String obtainAppTempPath() {
        return BaseInfo.sBaseFsAppsPath + this.p + "/temp/";
    }

    public String obtainAppVersionName() {
        return this.A;
    }

    public String obtainAppWebCachePath() {
        return this.D1;
    }

    public String obtainAuthority(String str) {
        String str2 = BaseInfo.sGlobalAuthority;
        if ((str2 != null && TextUtils.equals("*", str2)) || !q() || TextUtils.isEmpty(str) || e(this.p)) {
            return IApp.AUTHORITY_AUTHORIZED;
        }
        JSONObject jSONObject = this.I0;
        if (jSONObject == null) {
            return IApp.AUTHORITY_UNDETERMINED;
        }
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            if (str.equalsIgnoreCase(next)) {
                return this.I0.optString(next, IApp.AUTHORITY_UNDETERMINED);
            }
        }
        return IApp.AUTHORITY_UNDETERMINED;
    }

    public String obtainConfigProperty(String str) {
        String str2;
        if (PdrUtil.isEquals(str, "adid")) {
            str2 = this.N0;
        } else if (PdrUtil.isEquals(str, "launchError")) {
            str2 = this.M0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_AUTOCLOSE)) {
            str2 = String.valueOf(this.Y);
        } else if (PdrUtil.isEquals(str, "timeout")) {
            str2 = String.valueOf(this.a0);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_DELAY)) {
            str2 = String.valueOf(this.b0);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SPLASHSCREEN)) {
            str2 = String.valueOf(this.W);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_WAITING)) {
            str2 = String.valueOf(this.X);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_H5PLUS)) {
            str2 = String.valueOf(this.R);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_funSetUA)) {
            str2 = String.valueOf(this.S);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USER_AGENT)) {
            str2 = this.L;
        } else if (PdrUtil.isEquals(str, "error")) {
            str2 = this.q0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
            str2 = String.valueOf(this.i);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_UNTRUSTEDCA)) {
            str2 = this.p0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LOADED_TIME)) {
            str2 = this.Q0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_RAM_CACHE_MODE)) {
            str2 = this.R0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_JSERROR)) {
            str2 = this.N + "";
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_CRASH)) {
            str2 = this.M + "";
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USE_ENCRYPTION)) {
            str2 = this.U0 + "";
        } else if (PdrUtil.isEquals(str, "w2a_delay")) {
            str2 = String.valueOf(this.c0);
        } else if (PdrUtil.isEquals(str, "w2a_autoclose")) {
            str2 = String.valueOf(this.Z);
        } else if (PdrUtil.isEquals(str, "wap2app_running_mode")) {
            str2 = this.O + "";
        } else if (PdrUtil.isEquals(str, "injection")) {
            str2 = this.i1 + "";
        } else if (PdrUtil.isEquals(str, "event")) {
            str2 = this.d0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_TARGET)) {
            str2 = this.e0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LPLUSERQUIRE)) {
            str2 = this.f0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SPLUSERQUIRE)) {
            str2 = this.g0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LGEOLOCATION)) {
            str2 = this.h0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SGEOLOCATION)) {
            str2 = this.i0;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_BC)) {
            str2 = this.X0 + "";
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_MODE)) {
            str2 = this.a1;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED)) {
            str2 = this.b1 + "";
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_LAUNCH_ISSTATUS)) {
            str2 = String.valueOf(this.c1);
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_LAUNCH_STATUSBAR_COLOR)) {
            str2 = this.e1;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_SECOND_ISATATUS)) {
            str2 = String.valueOf(this.d1);
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_SECOND_STATUSBAR_COLOR)) {
            str2 = this.f1;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_MAP_COORD_TYPE)) {
            str2 = this.p1;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.UNIAPP_WEEX_JS_SERVICE)) {
            str2 = String.valueOf(this.g1);
        } else if (PdrUtil.isEquals(str, AbsoluteConst.APP_UNIAPP_VERSION)) {
            str2 = this.u0;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_UNIAPP_CONTROL)) {
            str2 = this.s1 ? this.z0 : "h5+";
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.UNI_NVUE_DATA)) {
            JSONObject jSONObject = this.v0;
            if (jSONObject == null) {
                return null;
            }
            return jSONObject.toString();
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_CONCATENATE)) {
            return this.Q + "";
        } else if (PdrUtil.isEquals(str, AbsoluteConst.NVUE_LAUNCH_MODE)) {
            return this.A0;
        } else {
            if (PdrUtil.isEquals(str, AbsoluteConst.JSON_KEY_DEBUG_REFRESH)) {
                return this.r1;
            }
            if (PdrUtil.isEquals(str, IApp.ConfigProperty.UNI_RESTART_TO_DIRECT)) {
                return String.valueOf(this.q1);
            }
            if (PdrUtil.isEquals(str, AbsoluteConst.APP_IS_UNIAPP)) {
                return String.valueOf(this.s1);
            }
            if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USE_V3_ENCRYPTION)) {
                return String.valueOf(this.V0);
            }
            if (PdrUtil.isEquals(str, IntentConst.UNIMP_RUN_EXTRA_INFO)) {
                return this.v1;
            }
            return null;
        }
        return str2;
    }

    public IWebviewStateListener obtainLaunchPageStateListener() {
        return this.w1;
    }

    public Object obtainMgrData(IMgr.MgrType mgrType, int i, Object[] objArr) {
        return this.y.processEvent(mgrType, i, objArr);
    }

    public String obtainOriginalAppId() {
        return this.z;
    }

    public InputStream obtainResInStream(String str, String str2) {
        String convert2AbsFullPath = convert2AbsFullPath(str, str2);
        byte b2 = this.V;
        if (b2 != 1) {
            if (b2 == 0) {
                try {
                    return DHFile.getInputStream(DHFile.createFileHandler(convert2AbsFullPath));
                } catch (IOException e) {
                    Logger.w("WebApp.obtainResInStream", e);
                }
            }
            return null;
        } else if (!PdrUtil.isDeviceRootDir(convert2AbsFullPath)) {
            return PlatformUtil.getResInputStream(convert2AbsFullPath);
        } else {
            try {
                return DHFile.getInputStream(DHFile.createFileHandler(convert2AbsFullPath));
            } catch (IOException e2) {
                Logger.w("WebApp.obtainResInStream", e2);
            }
        }
    }

    public byte obtainRunningAppMode() {
        return this.V;
    }

    public String obtainRuntimeArgs(boolean z2) {
        if (z2) {
            return JSONObject.quote(this.E);
        }
        return this.E;
    }

    public AppStatusBarManager obtainStatusBarMgr() {
        return this.m;
    }

    public JSONObject obtainThridInfo(IApp.ConfigProperty.ThridInfo thridInfo) {
        switch (f.a[thridInfo.ordinal()]) {
            case 1:
                return this.D0;
            case 2:
                return this.F0;
            case 3:
                return this.G0;
            case 4:
                return this.H0;
            case 5:
                return this.J0;
            case 6:
                m();
                return this.y1;
            case 7:
                return this.y.h;
            case 8:
                return this.K0;
            case 9:
                return this.E0;
            default:
                return null;
        }
    }

    public String obtainVersionSitemap() {
        return this.C;
    }

    public Intent obtainWebAppIntent() {
        return this.O0;
    }

    public String obtainWebviewBaseUrl() {
        return a(this.V);
    }

    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        byte b2 = this.u;
        if (b2 == 3) {
            return callSysEventListener(sysEventType, obj);
        }
        if (b2 == 1 && (sysEventType == ISysEventListener.SysEventType.onWebAppStop || sysEventType == ISysEventListener.SysEventType.onStop)) {
            s();
        }
        return false;
    }

    public void onSplashClosed() {
        diyStatusBarState();
    }

    public boolean p() {
        return this.u == 3;
    }

    public boolean q() {
        Intent obtainWebAppIntent = obtainWebAppIntent();
        if (obtainWebAppIntent != null) {
            boolean z2 = this.H1;
            this.H1 = obtainWebAppIntent.getBooleanExtra(IntentConst.IS_STREAM_APP, z2) | z2;
        }
        return this.H1;
    }

    public boolean r() {
        Logger.d(Logger.AppMgr_TAG, this.p + " onStop");
        IApp.IAppStatusListener iAppStatusListener = this.P0;
        if (iAppStatusListener != null) {
            return iAppStatusListener.onStop();
        }
        return true;
    }

    public void registerSysEventListener(ISysEventListener iSysEventListener, ISysEventListener.SysEventType sysEventType) {
        if (this.C0 == null) {
            this.C0 = new HashMap<>(1);
        }
        ArrayList arrayList = this.C0.get(sysEventType);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.C0.put(sysEventType, arrayList);
        }
        arrayList.add(iSysEventListener);
    }

    public void s() {
        this.l1.clear();
        this.m1.clear();
        Activity activity = this.a;
        if (activity != null && (activity instanceof WebAppActivity)) {
            ((WebAppActivity) activity).onAppStop(this.p);
        }
        Logger.d(Logger.AppMgr_TAG, "webapp.onStoped");
        BaseInfo.s_Runing_App_Count--;
        callSysEventListener(ISysEventListener.SysEventType.onWebAppStop, this);
        d();
        PermissionUtil.removeTempPermission(this.a, this.p);
        b();
        deleteAppTemp();
        PermissionControler.unregisterRootPermission(this.p);
        this.y.e(this);
        if (getIAppStatusListener() != null) {
            getIAppStatusListener().onStoped(false, (String) null);
        }
        this.y.processEvent(IMgr.MgrType.WindowMgr, 25, this);
    }

    public void setAppDataPath(String str) {
        if (this.V == 1) {
            if (str.startsWith(BaseInfo.sBaseResAppsPath)) {
                this.k0 = str;
                return;
            }
            this.k0 = BaseInfo.sBaseResAppsPath + this.p + "/" + BaseInfo.APP_WWW_FS_DIR;
        } else if (new File(str).exists()) {
            this.k0 = str;
        } else if (!str.startsWith(DeviceInfo.sCacheRootDir)) {
            this.k0 = DeviceInfo.sCacheRootDir + "/" + str;
        } else {
            this.k0 = str;
        }
    }

    public void setAppDocPath(String str) {
        this.C1 = PdrUtil.appendByDeviceRootDir(str);
    }

    public void setConfigProperty(String str, String str2) {
        if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_AUTOCLOSE)) {
            this.Y = PdrUtil.parseBoolean(str2, this.Y, false);
        } else if (PdrUtil.isEquals(str, "commit")) {
            a();
        } else if (PdrUtil.isEquals(str, "timeout")) {
            this.a0 = PdrUtil.parseInt(str2, this.a0);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_DELAY)) {
            this.b0 = PdrUtil.parseInt(str2, this.b0);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_SPLASHSCREEN)) {
            this.W = PdrUtil.parseBoolean(str2, this.W, false);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_WAITING)) {
            this.X = PdrUtil.parseBoolean(str2, this.X, false);
        } else if (PdrUtil.isEquals(str, "name")) {
            this.t0 = str2;
        } else if (PdrUtil.isEquals(str, "name")) {
            this.G = str2;
        } else if (PdrUtil.isEquals(str, "email")) {
            this.H = str2;
        } else if (PdrUtil.isEquals(str, "url")) {
            this.J = str2;
        } else if (PdrUtil.isEquals(str, "name")) {
            this.A = str2;
            BaseInfo.sLastAppVersionName = str2;
        } else if (PdrUtil.isEquals(str, "code")) {
            this.B = str2;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_RUNMODE_LIBERATE)) {
            this.j0 = PdrUtil.parseBoolean(str2, this.W, false);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_H5PLUS)) {
            this.R = PdrUtil.parseBoolean(str2, true, false);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_funSetUA)) {
            this.S = PdrUtil.parseBoolean(str2, true, false);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_USER_AGENT)) {
            this.L = str2;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_FULLSCREEN)) {
            this.i = PdrUtil.parseBoolean(str2, this.i, false);
        } else if (PdrUtil.isEquals(str, "webcache_path")) {
            this.D1 = str2;
        } else if (PdrUtil.isEquals(str, "wap2app_running_mode")) {
            this.O = PdrUtil.parseBoolean(str2, false, false);
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.CONFIG_LOADED_TIME)) {
            this.Q0 = str2;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_BC)) {
            this.X0 = str2;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_MODE)) {
            this.a1 = str2;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED)) {
            this.b1 = Boolean.valueOf(str2).booleanValue();
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_LAUNCH_ISSTATUS)) {
            this.c1 = Boolean.valueOf(str2).booleanValue();
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_LAUNCH_STATUSBAR_COLOR)) {
            this.e1 = str2;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_SECOND_ISATATUS)) {
            this.d1 = Boolean.valueOf(str2).booleanValue();
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSONKEY_STATUSBAR_SECOND_STATUSBAR_COLOR)) {
            this.f1 = str2;
        } else if (PdrUtil.isEquals(str, AbsoluteConst.UNIAPP_WEEX_JS_SERVICE)) {
            this.g1 = Boolean.valueOf(str2).booleanValue();
        } else if (PdrUtil.isEquals(str, AbsoluteConst.JSON_KEY_DEBUG_REFRESH)) {
            this.r1 = str2;
        } else if (PdrUtil.isEquals(str, IApp.ConfigProperty.UNI_RESTART_TO_DIRECT)) {
            this.q1 = Boolean.valueOf(str2).booleanValue();
        } else if (PdrUtil.isEquals(str, IntentConst.UNIMP_RUN_EXTRA_INFO)) {
            this.v1 = str2;
        }
    }

    public void setDirectPage(String str) {
        this.n1 = str;
    }

    public void setHideNavBarState(boolean z2) {
        this.q = z2;
    }

    public void setIAppStatusListener(IApp.IAppStatusListener iAppStatusListener) {
        this.P0 = iAppStatusListener;
    }

    public void setLaunchPageStateListener(IWebviewStateListener iWebviewStateListener) {
        this.w1 = iWebviewStateListener;
    }

    public void setNeedRefreshApp(boolean z2) {
        this.k1 = z2;
    }

    public void setQuitModel(int i) {
        this.t1 = i;
    }

    public void setRuntimeArgs(String str) {
        if (!PdrUtil.isEmpty(str)) {
            this.E = str;
        }
    }

    public void setStatus(byte b2) {
        this.u = b2;
        if (b2 == 3) {
            this.h1 = System.currentTimeMillis();
        }
    }

    public void setWebAppActivity(Activity activity) {
        this.a = activity;
        a(activity);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006f, code lost:
        if (r14.contains(r13) != false) goto L_0x0071;
     */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x012e A[SYNTHETIC, Splitter:B:48:0x012e] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0154 A[Catch:{ JSONException -> 0x01b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x01b0 A[Catch:{ JSONException -> 0x01b4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setWebAppIntent(android.content.Intent r18) {
        /*
            r17 = this;
            r1 = r17
            r2 = r18
            java.lang.String r3 = "background"
            java.lang.String r4 = "="
            java.lang.String r5 = "statusbar"
            java.lang.String r6 = "&"
            java.lang.String r0 = "webviewParameter"
            java.lang.String r7 = "exec_new_intent"
            r8 = 1
            boolean r7 = r2.getBooleanExtra(r7, r8)
            r9 = 0
            if (r7 != 0) goto L_0x001d
            android.content.Intent r10 = r1.O0
            if (r10 != 0) goto L_0x01c2
        L_0x001d:
            android.content.Intent r10 = new android.content.Intent
            r10.<init>(r2)
            r1.O0 = r10
            java.lang.String r10 = r1.t0
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            if (r10 == 0) goto L_0x0036
            android.content.Intent r10 = r1.O0
            java.lang.String r11 = "__name__"
            java.lang.String r10 = r10.getStringExtra(r11)
            r1.t0 = r10
        L_0x0036:
            android.content.Intent r10 = r1.O0
            java.lang.String r11 = "__first_web_url__"
            java.lang.String r10 = r10.getStringExtra(r11)
            java.lang.String r11 = r1.r0
            java.lang.String r12 = "__no__"
            boolean r11 = android.text.TextUtils.equals(r11, r12)
            if (r11 != 0) goto L_0x004a
            r1.r0 = r10
        L_0x004a:
            android.content.Intent r10 = r1.O0
            java.lang.String r11 = "direct_page"
            java.lang.String r10 = r10.getStringExtra(r11)
            boolean r12 = android.text.TextUtils.isEmpty(r10)
            if (r12 != 0) goto L_0x01c2
            java.net.URL r12 = new java.net.URL     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r12.<init>(r10)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r14 = r12.getQuery()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r14 == 0) goto L_0x0118
            boolean r15 = r14.contains(r0)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r13 = "__html5plusWebviewParameter"
            if (r15 != 0) goto L_0x0071
            boolean r15 = r14.contains(r13)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r15 == 0) goto L_0x0118
        L_0x0071:
            boolean r10 = r14.contains(r0)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r10 == 0) goto L_0x0078
            r13 = r0
        L_0x0078:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r0.<init>()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r10 = r12.getProtocol()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r0.append(r10)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r10 = "://"
            r0.append(r10)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r10 = r12.getHost()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r0.append(r10)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r10 = r12.getPath()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r0.append(r10)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r0 = r0.toString()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r1.n1 = r0     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String[] r10 = r14.split(r6)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r10 == 0) goto L_0x011a
            r12 = 0
            r14 = 1
            r16 = 0
        L_0x00a7:
            int r0 = r10.length     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r12 >= r0) goto L_0x0115
            r0 = r10[r12]     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String[] r0 = r0.split(r4)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r15 = r0[r9]     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            boolean r15 = r13.equals(r15)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r15 == 0) goto L_0x00bf
            r0 = r0[r8]     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r16 = java.net.URLDecoder.decode(r0)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            goto L_0x0111
        L_0x00bf:
            if (r14 == 0) goto L_0x00d8
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010d }
            r15.<init>()     // Catch:{ Exception -> 0x010d }
            java.lang.String r8 = r1.n1     // Catch:{ Exception -> 0x010d }
            r15.append(r8)     // Catch:{ Exception -> 0x010d }
            java.lang.String r8 = "?"
            r15.append(r8)     // Catch:{ Exception -> 0x010d }
            java.lang.String r8 = r15.toString()     // Catch:{ Exception -> 0x010d }
            r1.n1 = r8     // Catch:{ Exception -> 0x010d }
            r14 = 0
            goto L_0x00ee
        L_0x00d8:
            int r8 = r10.length     // Catch:{ Exception -> 0x010d }
            if (r12 >= r8) goto L_0x00ee
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010d }
            r8.<init>()     // Catch:{ Exception -> 0x010d }
            java.lang.String r15 = r1.n1     // Catch:{ Exception -> 0x010d }
            r8.append(r15)     // Catch:{ Exception -> 0x010d }
            r8.append(r6)     // Catch:{ Exception -> 0x010d }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x010d }
            r1.n1 = r8     // Catch:{ Exception -> 0x010d }
        L_0x00ee:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010d }
            r8.<init>()     // Catch:{ Exception -> 0x010d }
            java.lang.String r15 = r1.n1     // Catch:{ Exception -> 0x010d }
            r8.append(r15)     // Catch:{ Exception -> 0x010d }
            r15 = r0[r9]     // Catch:{ Exception -> 0x010d }
            r8.append(r15)     // Catch:{ Exception -> 0x010d }
            r8.append(r4)     // Catch:{ Exception -> 0x010d }
            r15 = 1
            r0 = r0[r15]     // Catch:{ Exception -> 0x010d }
            r8.append(r0)     // Catch:{ Exception -> 0x010d }
            java.lang.String r0 = r8.toString()     // Catch:{ Exception -> 0x010d }
            r1.n1 = r0     // Catch:{ Exception -> 0x010d }
            goto L_0x0111
        L_0x010d:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
        L_0x0111:
            int r12 = r12 + 1
            r8 = 1
            goto L_0x00a7
        L_0x0115:
            r13 = r16
            goto L_0x011b
        L_0x0118:
            r1.n1 = r10     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
        L_0x011a:
            r13 = 0
        L_0x011b:
            java.lang.String r0 = r1.n1     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r1.o1 = r0     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            android.content.Intent r4 = r1.O0     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r4.putExtra(r11, r0)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            java.lang.String r0 = r1.n1     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            org.json.JSONObject r0 = r1.d(r0)     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            r1.K0 = r0     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            if (r0 != 0) goto L_0x0143
            boolean r0 = android.text.TextUtils.isEmpty(r13)     // Catch:{ JSONException -> 0x01b4 }
            if (r0 != 0) goto L_0x013c
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x01b4 }
            r0.<init>(r13)     // Catch:{ JSONException -> 0x01b4 }
            r1.K0 = r0     // Catch:{ JSONException -> 0x01b4 }
            goto L_0x0143
        L_0x013c:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ JSONException -> 0x01b4 }
            r0.<init>()     // Catch:{ JSONException -> 0x01b4 }
            r1.K0 = r0     // Catch:{ JSONException -> 0x01b4 }
        L_0x0143:
            org.json.JSONObject r0 = r1.K0     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r4 = "webviewid"
            java.lang.String r0 = r0.optString(r4)     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r4 = r1.p     // Catch:{ JSONException -> 0x01b4 }
            boolean r0 = r4.equals(r0)     // Catch:{ JSONException -> 0x01b4 }
            if (r0 == 0) goto L_0x01b0
            java.io.File r0 = new java.io.File     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r4 = io.dcloud.common.util.BaseInfo.sConfigXML     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r4 = r1.a((java.lang.String) r4)     // Catch:{ JSONException -> 0x01b4 }
            r0.<init>(r4)     // Catch:{ JSONException -> 0x01b4 }
            boolean r0 = r0.exists()     // Catch:{ JSONException -> 0x01b4 }
            if (r0 != 0) goto L_0x0177
            org.json.JSONObject r0 = r1.K0     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r4 = "launch_path"
            java.lang.String r0 = r0.optString(r4)     // Catch:{ JSONException -> 0x01b4 }
            boolean r4 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x01b4 }
            if (r4 == 0) goto L_0x0175
            java.lang.String r0 = r1.n1     // Catch:{ JSONException -> 0x01b4 }
        L_0x0175:
            r1.m0 = r0     // Catch:{ JSONException -> 0x01b4 }
        L_0x0177:
            org.json.JSONObject r0 = r1.K0     // Catch:{ JSONException -> 0x01b4 }
            boolean r0 = r0.has(r5)     // Catch:{ JSONException -> 0x01b4 }
            if (r0 == 0) goto L_0x01a0
            org.json.JSONObject r0 = r1.K0     // Catch:{ JSONException -> 0x01b4 }
            org.json.JSONObject r0 = r0.optJSONObject(r5)     // Catch:{ JSONException -> 0x01b4 }
            if (r0 == 0) goto L_0x01a0
            java.lang.String r4 = "immersed"
            r5 = 1
            boolean r4 = r0.optBoolean(r4, r5)     // Catch:{ JSONException -> 0x01b4 }
            if (r4 == 0) goto L_0x01a0
            r1.c1 = r5     // Catch:{ JSONException -> 0x01b4 }
            boolean r4 = r0.has(r3)     // Catch:{ JSONException -> 0x01b4 }
            if (r4 == 0) goto L_0x01a0
            java.lang.String r4 = r1.X0     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r0 = r0.optString(r3, r4)     // Catch:{ JSONException -> 0x01b4 }
            r1.e1 = r0     // Catch:{ JSONException -> 0x01b4 }
        L_0x01a0:
            android.content.Intent r0 = r1.O0     // Catch:{ JSONException -> 0x01b4 }
            r0.removeExtra(r11)     // Catch:{ JSONException -> 0x01b4 }
            org.json.JSONObject r0 = r1.K0     // Catch:{ JSONException -> 0x01b4 }
            java.lang.String r3 = "titleNView"
            org.json.JSONObject r0 = r0.optJSONObject(r3)     // Catch:{ JSONException -> 0x01b4 }
            r1.J0 = r0     // Catch:{ JSONException -> 0x01b4 }
            goto L_0x01c2
        L_0x01b0:
            r17.e()     // Catch:{ JSONException -> 0x01b4 }
            goto L_0x01c2
        L_0x01b4:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ MalformedURLException -> 0x01be, Exception -> 0x01b9 }
            goto L_0x01c2
        L_0x01b9:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x01c2
        L_0x01be:
            r0 = move-exception
            r0.printStackTrace()
        L_0x01c2:
            boolean r0 = r17.q()
            if (r0 == 0) goto L_0x01e9
            java.lang.String r0 = r1.p
            boolean r0 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r0)
            if (r0 == 0) goto L_0x01e9
            if (r7 != 0) goto L_0x01e9
            java.lang.String r0 = "just_download"
            boolean r0 = r2.getBooleanExtra(r0, r9)
            if (r0 == 0) goto L_0x01e9
            boolean r0 = r1.E1
            if (r0 == 0) goto L_0x01e9
            io.dcloud.e.b.a r0 = r1.y
            io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            r3 = 50
            r0.processEvent(r2, r3, r1)
            r1.E1 = r9
        L_0x01e9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.setWebAppIntent(android.content.Intent):void");
    }

    public String shortcutQuit() {
        return this.A1;
    }

    public void showSplash() {
        Activity activity = getActivity();
        if (activity instanceof IOnCreateSplashView) {
            activity.setIntent(this.O0);
            ((IOnCreateSplashView) activity).onCreateSplash(activity);
        }
    }

    public boolean startFromShortCut() {
        return this.z1;
    }

    /* access modifiers changed from: package-private */
    public void t() {
        PermissionControler.registerPermission(this.p, this.B0);
    }

    public String toString() {
        return this.t0 + Operators.SUB + this.p + Operators.SUB + super.toString();
    }

    public void u() {
        b(false);
        setStatus((byte) 1);
        AppStatus.setAppStatus(this.p, 0);
        this.y.processEvent(IMgr.MgrType.FeatureMgr, 3, this.p);
        Logger.d(Logger.AppMgr_TAG, this.p + " will active change to unrunning");
        this.y.processEvent((IMgr.MgrType) null, 0, this);
        WebViewFactory.sUsePermissionWebviews.clear();
    }

    public void unregisterSysEventListener(ISysEventListener iSysEventListener, ISysEventListener.SysEventType sysEventType) {
        ArrayList arrayList;
        HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> hashMap = this.C0;
        if (hashMap != null && (arrayList = hashMap.get(sysEventType)) != null) {
            arrayList.remove(iSysEventListener);
            if (arrayList.isEmpty()) {
                this.C0.remove(sysEventType);
            }
        }
    }

    public void updateDirectPage(String str) {
        if (TextUtils.isEmpty(str)) {
            str = this.n1;
        }
        JSONObject d2 = d(str);
        if (d2 != null) {
            this.K0 = d2;
            if (d2 != null) {
                e();
                this.y.processEvent(IMgr.MgrType.WindowMgr, 48, this);
            }
        }
    }

    public String v() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("appid", this.p);
            jSONObject.put("version", this.A);
            jSONObject.put("name", this.t0);
            jSONObject.put("versionCode", this.B);
            jSONObject.put("description", this.F);
            jSONObject.put("author", this.G);
            jSONObject.put("email", this.H);
            jSONObject.put(IApp.ConfigProperty.CONFIG_LICENSE, this.I);
            jSONObject.put("licensehref", this.J);
            jSONObject.put(IApp.ConfigProperty.CONFIG_FEATURES, new JSONArray(this.B0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    /* access modifiers changed from: package-private */
    public void w() {
        b(true);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:345:0x08b2, code lost:
        if (r9.equalsIgnoreCase(r11) != false) goto L_0x08b4;
     */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0261  */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0283  */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x02f4  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x030c  */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x0396  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x03b4  */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x03c3  */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x03db  */
    /* JADX WARNING: Removed duplicated region for block: B:187:0x040b  */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0417  */
    /* JADX WARNING: Removed duplicated region for block: B:202:0x044b  */
    /* JADX WARNING: Removed duplicated region for block: B:221:0x0494  */
    /* JADX WARNING: Removed duplicated region for block: B:224:0x04a1  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x04c0  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x04c2  */
    /* JADX WARNING: Removed duplicated region for block: B:235:0x04dd  */
    /* JADX WARNING: Removed duplicated region for block: B:243:0x0520  */
    /* JADX WARNING: Removed duplicated region for block: B:251:0x0570  */
    /* JADX WARNING: Removed duplicated region for block: B:260:0x05ac  */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x05ae  */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x05ff  */
    /* JADX WARNING: Removed duplicated region for block: B:270:0x0601  */
    /* JADX WARNING: Removed duplicated region for block: B:273:0x060b  */
    /* JADX WARNING: Removed duplicated region for block: B:282:0x0665  */
    /* JADX WARNING: Removed duplicated region for block: B:289:0x06b2  */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x06d4  */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x06ec  */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x0704  */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x0751  */
    /* JADX WARNING: Removed duplicated region for block: B:308:0x0778  */
    /* JADX WARNING: Removed duplicated region for block: B:309:0x0794  */
    /* JADX WARNING: Removed duplicated region for block: B:310:0x07a4  */
    /* JADX WARNING: Removed duplicated region for block: B:315:0x07c6  */
    /* JADX WARNING: Removed duplicated region for block: B:316:0x07cb  */
    /* JADX WARNING: Removed duplicated region for block: B:321:0x07df  */
    /* JADX WARNING: Removed duplicated region for block: B:322:0x07e6  */
    /* JADX WARNING: Removed duplicated region for block: B:325:0x07fc  */
    /* JADX WARNING: Removed duplicated region for block: B:326:0x081b  */
    /* JADX WARNING: Removed duplicated region for block: B:329:0x0821  */
    /* JADX WARNING: Removed duplicated region for block: B:330:0x0825  */
    /* JADX WARNING: Removed duplicated region for block: B:335:0x0851  */
    /* JADX WARNING: Removed duplicated region for block: B:350:0x08bb  */
    /* JADX WARNING: Removed duplicated region for block: B:351:0x08c1  */
    /* JADX WARNING: Removed duplicated region for block: B:354:0x08dc  */
    /* JADX WARNING: Removed duplicated region for block: B:355:0x08e7  */
    /* JADX WARNING: Removed duplicated region for block: B:363:0x091c  */
    /* JADX WARNING: Removed duplicated region for block: B:364:0x0926  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:373:0x0980  */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0a69  */
    /* JADX WARNING: Removed duplicated region for block: B:412:0x0a77  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0162  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x018b  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01e1  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01e3  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01fb  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0222  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(java.io.InputStream r62, java.lang.String r63, org.json.JSONObject r64) {
        /*
            r61 = this;
            r1 = r61
            r0 = r62
            r2 = r63
            r3 = r64
            r4 = 0
            r1.j1 = r4
            io.dcloud.e.b.g r5 = r1.s
            r6 = 1
            if (r5 == 0) goto L_0x0016
            boolean r7 = r5.d
            if (r7 == 0) goto L_0x0016
            r7 = 1
            goto L_0x0017
        L_0x0016:
            r7 = 0
        L_0x0017:
            java.util.ArrayList<java.lang.String> r8 = r1.m1
            java.util.ArrayList<java.lang.String> r9 = io.dcloud.common.util.AppStreamUtil.AppStreamSchemeWhiteDefaultList
            r8.addAll(r9)
            android.app.Activity r8 = r61.getActivity()
            if (r8 != 0) goto L_0x0026
            android.content.Context r8 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext
        L_0x0026:
            java.lang.String r9 = ""
            if (r0 == 0) goto L_0x006f
            boolean r10 = r1.s1     // Catch:{ IOException -> 0x0053 }
            if (r10 != 0) goto L_0x003c
            io.dcloud.common.adapter.io.UnicodeInputStream r10 = new io.dcloud.common.adapter.io.UnicodeInputStream     // Catch:{ IOException -> 0x0053 }
            java.nio.charset.Charset r11 = java.nio.charset.Charset.defaultCharset()     // Catch:{ IOException -> 0x0053 }
            java.lang.String r11 = r11.name()     // Catch:{ IOException -> 0x0053 }
            r10.<init>(r0, r11)     // Catch:{ IOException -> 0x0053 }
            r0 = r10
        L_0x003c:
            byte[] r0 = io.dcloud.common.util.IOUtil.getBytes(r0)     // Catch:{ IOException -> 0x0053 }
            io.dcloud.common.DHInterface.IConfusionMgr r10 = r1.u1     // Catch:{ IOException -> 0x0053 }
            java.lang.String r10 = r10.handleEncryption(r8, r0)     // Catch:{ IOException -> 0x0053 }
            if (r10 == 0) goto L_0x004b
            r1.U0 = r6     // Catch:{ IOException -> 0x0053 }
            goto L_0x0070
        L_0x004b:
            r1.U0 = r4     // Catch:{ IOException -> 0x0053 }
            java.lang.String r10 = new java.lang.String     // Catch:{ IOException -> 0x0053 }
            r10.<init>(r0)     // Catch:{ IOException -> 0x0053 }
            goto L_0x0070
        L_0x0053:
            r0 = move-exception
            r0.printStackTrace()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "parseConfig error="
            r10.append(r11)
            java.lang.String r0 = r0.getMessage()
            r10.append(r0)
            java.lang.String r0 = r10.toString()
            io.dcloud.common.adapter.util.Logger.e(r0)
        L_0x006f:
            r10 = r9
        L_0x0070:
            boolean r0 = r1.U0
            if (r0 == 0) goto L_0x008f
            if (r7 == 0) goto L_0x008f
            boolean r0 = io.dcloud.common.util.BaseInfo.SyncDebug
            if (r0 == 0) goto L_0x008f
            boolean r0 = io.dcloud.common.util.BaseInfo.isBase(r8)
            if (r0 != 0) goto L_0x008f
            if (r7 == 0) goto L_0x008f
            r5.a = r6
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_BASE_DEBUG_WGT_INSTALL_NOT_CONFUSION
            r2 = -1206(0xfffffffffffffb4a, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r2, (java.lang.String) r0)
            r5.b = r0
            return r4
        L_0x008f:
            org.json.JSONObject r12 = new org.json.JSONObject     // Catch:{ Exception -> 0x0b3f }
            r12.<init>(r10)     // Catch:{ Exception -> 0x0b3f }
            java.lang.String r0 = "version"
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r12, (java.lang.String) r0)
            java.lang.String r10 = "name"
            java.lang.String r13 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r0, (java.lang.String) r10)
            java.lang.String r14 = "code"
            java.lang.String r14 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r0, (java.lang.String) r14)
            io.dcloud.common.util.BaseInfo.sLastAppVersionName = r13
            java.lang.String r15 = r1.p0
            java.lang.String r0 = io.dcloud.common.util.LoadAppUtils.getAppSignatureSHA1(r8)
            if (r7 == 0) goto L_0x00ee
            if (r3 == 0) goto L_0x00be
            java.lang.String r11 = "force"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r3, (java.lang.String) r11)
            boolean r3 = java.lang.Boolean.parseBoolean(r3)
            goto L_0x00bf
        L_0x00be:
            r3 = 0
        L_0x00bf:
            if (r3 == 0) goto L_0x00c2
            goto L_0x00ef
        L_0x00c2:
            java.lang.String r11 = r1.A
            boolean r11 = android.text.TextUtils.isEmpty(r11)
            if (r11 != 0) goto L_0x00ef
            java.lang.String r11 = r1.A
            boolean r11 = io.dcloud.common.util.BaseInfo.BaseAppInfo.compareVersion(r13, r11)
            if (r11 != 0) goto L_0x00ef
            boolean r0 = r5.c
            if (r0 == 0) goto L_0x00e1
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_WWW_MANIFEST_VERSION_NOT_MATCH
            r2 = -1228(0xfffffffffffffb34, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r2, (java.lang.String) r0)
            r5.b = r0
            goto L_0x00eb
        L_0x00e1:
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_MANIFEST_VERSION_NOT_MATCH
            r2 = -1205(0xfffffffffffffb4b, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r2, (java.lang.String) r0)
            r5.b = r0
        L_0x00eb:
            r5.a = r6
            return r4
        L_0x00ee:
            r3 = 0
        L_0x00ef:
            java.lang.String r11 = "id"
            java.lang.String r11 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r12, (java.lang.String) r11)
            io.dcloud.common.util.BaseInfo.sCurrentAppOriginalAppid = r11
            boolean r16 = io.dcloud.common.util.BaseInfo.ISDEBUG
            if (r16 == 0) goto L_0x0107
            android.content.Context r16 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext
            if (r16 == 0) goto L_0x0107
            boolean r16 = io.dcloud.common.util.BaseInfo.isBase(r16)
            if (r16 == 0) goto L_0x0107
            r4 = 1
            goto L_0x0156
        L_0x0107:
            boolean r16 = io.dcloud.common.util.PdrUtil.isEquals(r2, r11)
            if (r16 == 0) goto L_0x0117
            java.lang.String r4 = r1.p
            boolean r4 = io.dcloud.common.util.PdrUtil.isEquals(r4, r11)
            if (r4 == 0) goto L_0x0117
            r4 = 1
            goto L_0x0118
        L_0x0117:
            r4 = 0
        L_0x0118:
            if (r4 != 0) goto L_0x0155
            r5.a = r6
            boolean r0 = r5.c
            if (r0 == 0) goto L_0x012b
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_WWW_MANIFEST_APPID_NOT_MATCH
            r3 = -1226(0xfffffffffffffb36, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r3, (java.lang.String) r0)
            r5.b = r0
            goto L_0x0135
        L_0x012b:
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_MANIFEST_APPID_NOT_MATCH
            r3 = -1204(0xfffffffffffffb4c, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r3, (java.lang.String) r0)
            r5.b = r0
        L_0x0135:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "appid config is wrong pExpectAppid="
            r0.append(r3)
            r0.append(r2)
            java.lang.String r2 = ";appid="
            r0.append(r2)
            r0.append(r11)
            java.lang.String r0 = r0.toString()
            java.lang.String r2 = "Appmgr"
            android.util.Log.e(r2, r0)
        L_0x0153:
            r2 = 0
            return r2
        L_0x0155:
            r2 = r11
        L_0x0156:
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r12, (java.lang.String) r10)
            boolean r18 = io.dcloud.e.c.g.c()
            r62 = r4
            if (r18 != 0) goto L_0x018b
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            io.dcloud.common.DHInterface.IConfusionMgr r0 = r61.getConfusionMgr()
            io.dcloud.common.DHInterface.IConfusionMgr r18 = r61.getConfusionMgr()
            r19 = r15
            java.lang.String r15 = r18.getS5DS()
            r63 = r6
            r6 = 60
            r64 = r11
            r11 = 1
            java.lang.String r0 = r0.decodeString(r15, r11, r6)
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            goto L_0x0191
        L_0x018b:
            r63 = r6
            r64 = r11
            r19 = r15
        L_0x0191:
            r4 = r0
            java.lang.String r0 = "description"
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r12, (java.lang.String) r0)
            java.lang.String r11 = "developer"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r12, (java.lang.String) r11)
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r10)
            java.lang.String r15 = "email"
            java.lang.String r15 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r15)
            r18 = r15
            java.lang.String r15 = "url"
            io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r15)
            java.lang.String r11 = "license"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r12, (java.lang.String) r11)
            r20 = r10
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r15)
            io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r0)
            java.lang.String r0 = "launch_path"
            java.lang.String r11 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r12, (java.lang.String) r0)
            java.lang.String r0 = "launch_path_w2a"
            r21 = r10
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r12, (java.lang.String) r0)
            boolean r0 = r1.j1
            if (r0 != 0) goto L_0x01e3
            java.lang.String r0 = r1.l0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x01e1
            java.lang.String r0 = r1.l0
            boolean r0 = android.text.TextUtils.equals(r11, r0)
            if (r0 != 0) goto L_0x01e1
            goto L_0x01e3
        L_0x01e1:
            r0 = 0
            goto L_0x01e4
        L_0x01e3:
            r0 = 1
        L_0x01e4:
            r1.j1 = r0
            if (r0 != 0) goto L_0x01fb
            java.lang.String r0 = r1.n0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x01f9
            java.lang.String r0 = r1.n0
            boolean r0 = android.text.TextUtils.equals(r10, r0)
            if (r0 != 0) goto L_0x01f9
            goto L_0x01fb
        L_0x01f9:
            r0 = 0
            goto L_0x01fc
        L_0x01fb:
            r0 = 1
        L_0x01fc:
            r1.j1 = r0
            java.lang.String r0 = "baseUrl"
            r22 = r10
            java.lang.String r10 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r12, (java.lang.String) r0)
            java.lang.String r0 = r1.s0
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r0)
            r23 = r10
            r17 = 1
            r10 = r0 ^ 1
            java.lang.String r0 = "plus"
            r24 = r10
            org.json.JSONObject r10 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r12, (java.lang.String) r0)
            java.lang.String r0 = "confusion"
            boolean r25 = r10.has(r0)
            if (r25 == 0) goto L_0x0261
            r25 = r11
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r0)
            if (r11 != 0) goto L_0x0253
            java.lang.String r11 = r10.optString(r0)
            r26 = r6
            java.lang.String r6 = "BQ81KwABAA"
            boolean r6 = r11.startsWith(r6)
            if (r6 == 0) goto L_0x023e
            r6 = 1
            r1.U0 = r6
            r1.V0 = r6
            goto L_0x023f
        L_0x023e:
            r6 = 1
        L_0x023f:
            io.dcloud.common.DHInterface.IConfusionMgr r11 = r1.u1
            java.lang.String r6 = r1.p
            java.lang.String r0 = r10.optString(r0)
            boolean r0 = r11.recordEncryptionV3Resources(r6, r0)
            if (r0 == 0) goto L_0x0265
            r6 = 1
            r1.U0 = r6
            r1.V0 = r6
            goto L_0x0265
        L_0x0253:
            r26 = r6
            boolean r0 = r1.U0
            if (r0 == 0) goto L_0x0265
            io.dcloud.common.DHInterface.IConfusionMgr r0 = r1.u1
            java.lang.String r6 = r1.p
            r0.recordEncryptionResources(r6, r11)
            goto L_0x0265
        L_0x0261:
            r26 = r6
            r25 = r11
        L_0x0265:
            java.lang.String r0 = "checkPermissionDenied"
            boolean r0 = io.dcloud.common.util.JSONUtil.getBoolean(r10, r0)
            io.dcloud.common.adapter.util.PermissionUtil.isCheckPermissionDisabled = r0
            java.lang.String r0 = "tabBar"
            org.json.JSONObject r6 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r0)
            java.lang.String r11 = "statusbar"
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r11)
            r27 = r6
            java.lang.String r6 = "none"
            r28 = r14
            java.lang.String r14 = "background"
            if (r0 == 0) goto L_0x02f4
            boolean r29 = r0.has(r14)
            if (r29 == 0) goto L_0x0298
            r29 = r13
            java.lang.String r13 = r0.optString(r14)
            boolean r30 = io.dcloud.common.util.PdrUtil.isEmpty(r13)
            if (r30 != 0) goto L_0x029a
            r1.X0 = r13
            goto L_0x029a
        L_0x0298:
            r29 = r13
        L_0x029a:
            java.lang.String r13 = "style"
            boolean r30 = r0.has(r13)
            if (r30 == 0) goto L_0x02ad
            r30 = r3
            java.lang.String r3 = r1.a1
            java.lang.String r3 = r0.optString(r13, r3)
            r1.a1 = r3
            goto L_0x02af
        L_0x02ad:
            r30 = r3
        L_0x02af:
            java.lang.String r3 = "immersed"
            boolean r13 = r0.has(r3)
            if (r13 == 0) goto L_0x02f1
            int r13 = android.os.Build.VERSION.SDK_INT
            r31 = r7
            r7 = 19
            if (r13 < r7) goto L_0x02fa
            java.lang.String r7 = r0.optString(r3)
            r1.n = r7
            java.lang.String r13 = "supportedDevice"
            boolean r7 = r7.equals(r13)
            if (r7 != 0) goto L_0x02ed
            java.lang.String r7 = r1.n
            java.lang.String r13 = "suggestedDevice"
            boolean r7 = r7.equals(r13)
            if (r7 == 0) goto L_0x02d8
            goto L_0x02ed
        L_0x02d8:
            java.lang.String r7 = r1.n
            boolean r7 = r7.equals(r6)
            if (r7 == 0) goto L_0x02e4
            r7 = 0
            r1.b1 = r7
            goto L_0x02fa
        L_0x02e4:
            boolean r7 = r1.b1
            boolean r0 = r0.optBoolean(r3, r7)
            r1.b1 = r0
            goto L_0x02fa
        L_0x02ed:
            r3 = 1
            r1.b1 = r3
            goto L_0x02fa
        L_0x02f1:
            r31 = r7
            goto L_0x02fa
        L_0x02f4:
            r30 = r3
            r31 = r7
            r29 = r13
        L_0x02fa:
            java.lang.String r0 = "launchwebview"
            org.json.JSONObject r3 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r0)
            java.lang.String r7 = "replacewebapi"
            java.lang.String r13 = "normal"
            r32 = r12
            java.lang.String r12 = "geolocation"
            r33 = 0
            if (r3 == 0) goto L_0x03c3
            java.lang.String r0 = "overrideurl"
            org.json.JSONObject r34 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r3, (java.lang.String) r0)
            java.lang.String r0 = "overrideresource"
            org.json.JSONArray r0 = io.dcloud.common.util.JSONUtil.getJSONArray((org.json.JSONObject) r3, (java.lang.String) r0)
            r35 = r4
            if (r0 == 0) goto L_0x032e
            org.json.JSONObject r4 = new org.json.JSONObject
            r4.<init>()
            r36 = r8
            java.lang.String r8 = "0"
            r4.put(r8, r0)     // Catch:{ JSONException -> 0x0329 }
            goto L_0x0332
        L_0x0329:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0332
        L_0x032e:
            r36 = r8
            r4 = r33
        L_0x0332:
            boolean r0 = r1.i1
            java.lang.String r8 = "injection"
            boolean r0 = r3.optBoolean(r8, r0)
            java.lang.String r8 = "plusrequire"
            java.lang.String r8 = r3.optString(r8, r13)
            r37 = r0
            java.lang.String r0 = "titleNView"
            boolean r0 = r3.has(r0)
            if (r0 == 0) goto L_0x0351
            java.lang.String r0 = "titleNView"
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r3, (java.lang.String) r0)
            goto L_0x0362
        L_0x0351:
            java.lang.String r0 = "navigationbar"
            boolean r0 = r3.has(r0)
            if (r0 == 0) goto L_0x0360
            java.lang.String r0 = "navigationbar"
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r3, (java.lang.String) r0)
            goto L_0x0362
        L_0x0360:
            r0 = r33
        L_0x0362:
            boolean r38 = r3.has(r7)
            if (r38 == 0) goto L_0x037b
            r38 = r0
            org.json.JSONObject r0 = r3.optJSONObject(r7)
            if (r0 == 0) goto L_0x038a
            boolean r39 = r0.has(r12)
            if (r39 == 0) goto L_0x038a
            java.lang.String r0 = r0.optString(r12, r6)
            goto L_0x0387
        L_0x037b:
            r38 = r0
            boolean r0 = r3.has(r12)
            if (r0 == 0) goto L_0x038a
            java.lang.String r0 = r3.optString(r12, r6)
        L_0x0387:
            r39 = r0
            goto L_0x038c
        L_0x038a:
            r39 = r6
        L_0x038c:
            boolean r0 = r1.b1
            if (r0 == 0) goto L_0x03b4
            boolean r0 = r3.has(r11)
            if (r0 == 0) goto L_0x03b4
            r0 = r4
            r4 = 1
            r1.c1 = r4
            org.json.JSONObject r4 = r3.optJSONObject(r11)
            if (r4 == 0) goto L_0x03b1
            boolean r40 = r4.has(r14)
            if (r40 == 0) goto L_0x03b1
            r40 = r0
            java.lang.String r0 = r1.X0
            java.lang.String r0 = r4.optString(r14, r0)
            r1.e1 = r0
            goto L_0x03b6
        L_0x03b1:
            r40 = r0
            goto L_0x03b6
        L_0x03b4:
            r40 = r4
        L_0x03b6:
            r0 = r34
            r4 = r37
            r34 = r39
            r39 = r3
            r37 = r8
            r8 = r40
            goto L_0x03d3
        L_0x03c3:
            r35 = r4
            r36 = r8
            r39 = r3
            r34 = r6
            r37 = r13
            r0 = r33
            r8 = r0
            r38 = r8
            r4 = 1
        L_0x03d3:
            java.lang.String r3 = "appWhitelist"
            boolean r40 = r10.has(r3)
            if (r40 == 0) goto L_0x040b
            org.json.JSONArray r3 = r10.optJSONArray(r3)
            r40 = r4
            r41 = r8
            r4 = 0
        L_0x03e4:
            int r8 = r3.length()
            if (r4 >= r8) goto L_0x040f
            java.lang.String r8 = r3.optString(r4)
            boolean r42 = android.text.TextUtils.isEmpty(r8)
            if (r42 != 0) goto L_0x0404
            r42 = r3
            java.util.ArrayList<java.lang.String> r3 = r1.l1
            boolean r3 = r3.contains(r8)
            if (r3 != 0) goto L_0x0406
            java.util.ArrayList<java.lang.String> r3 = r1.l1
            r3.add(r8)
            goto L_0x0406
        L_0x0404:
            r42 = r3
        L_0x0406:
            int r4 = r4 + 1
            r3 = r42
            goto L_0x03e4
        L_0x040b:
            r40 = r4
            r41 = r8
        L_0x040f:
            java.lang.String r3 = "schemeWhitelist"
            boolean r4 = r10.has(r3)
            if (r4 == 0) goto L_0x0443
            org.json.JSONArray r3 = r10.optJSONArray(r3)
            r4 = 0
        L_0x041c:
            int r8 = r3.length()
            if (r4 >= r8) goto L_0x0443
            java.lang.String r8 = r3.optString(r4)
            boolean r42 = android.text.TextUtils.isEmpty(r8)
            if (r42 != 0) goto L_0x043c
            r42 = r3
            java.util.ArrayList<java.lang.String> r3 = r1.m1
            boolean r3 = r3.contains(r8)
            if (r3 != 0) goto L_0x043e
            java.util.ArrayList<java.lang.String> r3 = r1.m1
            r3.add(r8)
            goto L_0x043e
        L_0x043c:
            r42 = r3
        L_0x043e:
            int r4 = r4 + 1
            r3 = r42
            goto L_0x041c
        L_0x0443:
            java.lang.String r3 = "secondwebview"
            org.json.JSONObject r3 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r3)
            if (r3 == 0) goto L_0x0494
            java.lang.String r4 = "plusrequire"
            java.lang.String r4 = r3.optString(r4, r13)
            boolean r8 = r3.has(r7)
            if (r8 == 0) goto L_0x0468
            org.json.JSONObject r7 = r3.optJSONObject(r7)
            if (r7 == 0) goto L_0x0472
            boolean r8 = r7.has(r12)
            if (r8 == 0) goto L_0x0472
            java.lang.String r6 = r7.optString(r12, r6)
            goto L_0x0472
        L_0x0468:
            boolean r7 = r3.has(r12)
            if (r7 == 0) goto L_0x0472
            java.lang.String r6 = r3.optString(r12, r6)
        L_0x0472:
            boolean r7 = r1.b1
            if (r7 == 0) goto L_0x0495
            boolean r7 = r3.has(r11)
            if (r7 == 0) goto L_0x0495
            r7 = 1
            r1.d1 = r7
            org.json.JSONObject r7 = r3.optJSONObject(r11)
            if (r7 == 0) goto L_0x0495
            boolean r8 = r7.has(r14)
            if (r8 == 0) goto L_0x0495
            java.lang.String r8 = r1.X0
            java.lang.String r7 = r7.optString(r14, r8)
            r1.f1 = r7
            goto L_0x0495
        L_0x0494:
            r4 = r13
        L_0x0495:
            io.dcloud.e.b.a r7 = r1.y
            android.content.Context r7 = r7.getContext()
            boolean r7 = io.dcloud.common.util.BaseInfo.isBase(r7)
            if (r7 == 0) goto L_0x04a9
            java.lang.String r7 = "ramcachemode"
            java.lang.String r7 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r7)
            r1.R0 = r7
        L_0x04a9:
            java.lang.String r7 = android.os.Build.BRAND
            boolean r7 = io.dcloud.common.adapter.util.MobilePhoneModel.checkPhoneBanAcceleration(r7)
            if (r7 != 0) goto L_0x04c2
            java.lang.String r7 = r1.p
            boolean r7 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r7)
            if (r7 == 0) goto L_0x04c0
            boolean r7 = r61.q()
            if (r7 == 0) goto L_0x04c0
            goto L_0x04c2
        L_0x04c0:
            r7 = 0
            goto L_0x04c3
        L_0x04c2:
            r7 = 1
        L_0x04c3:
            java.lang.String r8 = "hardwareAccelerated"
            boolean r7 = r10.optBoolean(r8, r7)
            r1.T0 = r7
            java.lang.String r7 = r1.S0
            java.lang.String r8 = "popGesture"
            java.lang.String r7 = r10.optString(r8, r7)
            r1.S0 = r7
            java.lang.String r7 = "cache"
            org.json.JSONObject r7 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r7)
            if (r7 == 0) goto L_0x04ed
            java.lang.String r8 = "mode"
            java.lang.String r7 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r8)
            boolean r8 = android.text.TextUtils.isEmpty(r7)
            if (r8 == 0) goto L_0x04eb
            java.lang.String r7 = r1.W0
        L_0x04eb:
            r1.W0 = r7
        L_0x04ed:
            java.lang.String r7 = "cers"
            org.json.JSONObject r7 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r7)
            java.lang.String r8 = "crash"
            java.lang.String r8 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r8)
            boolean r11 = r1.M
            r12 = 0
            boolean r8 = io.dcloud.common.util.PdrUtil.parseBoolean(r8, r11, r12)
            java.lang.String r11 = "jserror"
            java.lang.String r7 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r7, (java.lang.String) r11)
            boolean r11 = r1.N
            boolean r7 = io.dcloud.common.util.PdrUtil.parseBoolean(r7, r11, r12)
            java.lang.String r11 = "compatible"
            org.json.JSONObject r11 = r10.optJSONObject(r11)
            java.lang.String r12 = "compilerVersion"
            r42 = r6
            java.lang.String r6 = "uni-app"
            if (r11 == 0) goto L_0x0570
            boolean r43 = r10.has(r6)
            if (r43 == 0) goto L_0x0570
            org.json.JSONObject r6 = r10.optJSONObject(r6)
            boolean r43 = r1.a((org.json.JSONObject) r6, (io.dcloud.e.b.g) r5)
            if (r43 != 0) goto L_0x052c
            r4 = 0
            return r4
        L_0x052c:
            r43 = r4
            r4 = 1
            r1.s1 = r4
            java.lang.String r4 = "uni-v3"
            r1.z0 = r4
            java.lang.String r4 = "ignoreVersion"
            r44 = r3
            r3 = 0
            boolean r4 = r11.optBoolean(r4, r3)
            r1.y0 = r4
            java.lang.String r3 = r11.optString(r12)
            java.lang.String r4 = "runtimeVersion"
            java.lang.String r4 = r11.optString(r4)
            r1.w0 = r4
            java.lang.String r4 = r6.optString(r12)
            r1.u0 = r4
            java.lang.String r4 = "nvueLaunchMode"
            java.lang.String r4 = r6.optString(r4, r13)
            r1.A0 = r4
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r3)
            if (r4 != 0) goto L_0x05a6
            java.lang.String r3 = r3.trim()
            java.lang.String r4 = r1.u0
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x05a6
            r3 = 0
            r1.x0 = r3
            goto L_0x05a6
        L_0x0570:
            r44 = r3
            r43 = r4
            r3 = 0
            boolean r4 = r10.has(r6)
            if (r4 == 0) goto L_0x05a4
            org.json.JSONObject r4 = r10.optJSONObject(r6)
            boolean r6 = r1.a((org.json.JSONObject) r4, (io.dcloud.e.b.g) r5)
            if (r6 != 0) goto L_0x0586
            return r3
        L_0x0586:
            r3 = 1
            r1.s1 = r3
            java.lang.String r3 = r4.optString(r12)
            r1.u0 = r3
            java.lang.String r3 = "uni-v3"
            r1.z0 = r3
            java.lang.String r3 = "nvue"
            org.json.JSONObject r3 = r4.optJSONObject(r3)
            r1.v0 = r3
            java.lang.String r3 = "nvueLaunchMode"
            java.lang.String r3 = r4.optString(r3, r13)
            r1.A0 = r3
            goto L_0x05a6
        L_0x05a4:
            r1.s1 = r3
        L_0x05a6:
            boolean r3 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r3 == 0) goto L_0x05ae
            r3 = 1
            goto L_0x05c6
        L_0x05ae:
            java.lang.String r3 = "runmode"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r3)
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 == 0) goto L_0x05c0
            boolean r4 = r1.s1
            if (r4 == 0) goto L_0x05c0
            java.lang.String r3 = "liberate"
        L_0x05c0:
            java.lang.String r4 = "liberate"
            boolean r3 = io.dcloud.common.util.PdrUtil.isEquals(r3, r4)
        L_0x05c6:
            java.lang.String r4 = "useragent"
            org.json.JSONObject r4 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r4)
            java.lang.String r6 = "value"
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r6)
            java.lang.String r11 = "concatenate"
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r11)
            boolean r11 = r1.Q
            r12 = 0
            boolean r4 = io.dcloud.common.util.PdrUtil.parseBoolean(r4, r11, r12)
            java.lang.String r11 = "useragent_android"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r11)
            java.lang.String r13 = "value"
            java.lang.String r13 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r13)
            r45 = r6
            java.lang.String r6 = "concatenate"
            java.lang.String r6 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r6)
            boolean r4 = io.dcloud.common.util.PdrUtil.parseBoolean(r6, r4, r12)
            boolean r6 = io.dcloud.common.util.PdrUtil.isEmpty(r13)
            if (r6 != 0) goto L_0x0601
            r6 = r13
            goto L_0x0603
        L_0x0601:
            r6 = r45
        L_0x0603:
            java.lang.String r11 = "splashscreen"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r11)
            if (r11 == 0) goto L_0x07a4
            android.app.Activity r12 = r61.getActivity()
            java.lang.String r13 = "pdr"
            android.content.SharedPreferences r12 = io.dcloud.common.adapter.util.SP.getOrCreateBundle((android.content.Context) r12, (java.lang.String) r13)
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r45 = r0
            java.lang.String r0 = r1.p
            r13.append(r0)
            java.lang.String r0 = "__update_splash_autoclose"
            r13.append(r0)
            java.lang.String r0 = r13.toString()
            boolean r0 = r12.contains(r0)
            if (r0 == 0) goto L_0x064a
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r13 = r1.p
            r0.append(r13)
            java.lang.String r13 = "__update_splash_autoclose"
            r0.append(r13)
            java.lang.String r0 = r0.toString()
            r13 = 1
            boolean r0 = r12.getBoolean(r0, r13)
        L_0x0648:
            r13 = r0
            goto L_0x0663
        L_0x064a:
            java.lang.String r0 = "autoclose"
            boolean r0 = io.dcloud.common.util.JSONUtil.isNull(r11, r0)
            if (r0 != 0) goto L_0x0661
            java.lang.String r0 = "autoclose"
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r0)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
            goto L_0x0648
        L_0x0661:
            r0 = 0
            r13 = 1
        L_0x0663:
            if (r13 == 0) goto L_0x06b2
            r46 = r13
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r47 = r4
            java.lang.String r4 = r1.p
            r13.append(r4)
            java.lang.String r4 = "__update_splash_delay"
            r13.append(r4)
            java.lang.String r4 = r13.toString()
            boolean r4 = r12.contains(r4)
            if (r4 == 0) goto L_0x069d
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r13 = r1.p
            r4.append(r13)
            java.lang.String r13 = "__update_splash_delay"
            r4.append(r13)
            java.lang.String r4 = r4.toString()
            r13 = 0
            int r4 = r12.getInt(r4, r13)
        L_0x069a:
            r48 = r4
            goto L_0x06b9
        L_0x069d:
            java.lang.String r4 = "delay"
            boolean r4 = io.dcloud.common.util.JSONUtil.isNull(r11, r4)
            if (r4 != 0) goto L_0x06b6
            java.lang.String r4 = "delay"
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r4)
            int r13 = r1.b0
            int r4 = io.dcloud.common.util.PdrUtil.parseInt(r4, r13)
            goto L_0x069a
        L_0x06b2:
            r47 = r4
            r46 = r13
        L_0x06b6:
            r4 = 0
            r48 = 0
        L_0x06b9:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            r49 = r3
            java.lang.String r3 = r1.p
            r13.append(r3)
            java.lang.String r3 = "__update_splash_autoclose_w2a"
            r13.append(r3)
            java.lang.String r3 = r13.toString()
            boolean r3 = r12.contains(r3)
            if (r3 == 0) goto L_0x06ec
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r1.p
            r3.append(r13)
            java.lang.String r13 = "__update_splash_autoclose_w2a"
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            boolean r0 = r12.getBoolean(r3, r0)
            goto L_0x0702
        L_0x06ec:
            java.lang.String r3 = "autoclose_w2a"
            boolean r3 = io.dcloud.common.util.JSONUtil.isNull(r11, r3)
            if (r3 != 0) goto L_0x0702
            java.lang.String r0 = "autoclose_w2a"
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r0)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
        L_0x0702:
            if (r0 == 0) goto L_0x0751
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r1.p
            r3.append(r13)
            java.lang.String r13 = "__update_splash_delay_w2a"
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            boolean r3 = r12.contains(r3)
            if (r3 == 0) goto L_0x0735
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r13 = r1.p
            r3.append(r13)
            java.lang.String r13 = "__update_splash_delay_w2a"
            r3.append(r13)
            java.lang.String r3 = r3.toString()
            int r4 = r12.getInt(r3, r4)
            goto L_0x0749
        L_0x0735:
            java.lang.String r3 = "delay_w2a"
            boolean r3 = io.dcloud.common.util.JSONUtil.isNull(r11, r3)
            if (r3 != 0) goto L_0x0749
            java.lang.String r3 = "delay_w2a"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r3)
            int r4 = r1.c0
            int r4 = io.dcloud.common.util.PdrUtil.parseInt(r3, r4)
        L_0x0749:
            float r3 = (float) r4
            float r4 = r61.j()
            float r3 = r3 + r4
            int r3 = (int) r3
            goto L_0x0752
        L_0x0751:
            r3 = r4
        L_0x0752:
            java.lang.String r4 = "waiting"
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r4)
            boolean r12 = r1.X
            r13 = 0
            boolean r4 = io.dcloud.common.util.PdrUtil.parseBoolean(r4, r12, r13)
            java.lang.String r12 = "event"
            java.lang.String r12 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r11, (java.lang.String) r12)
            java.lang.String r13 = "target"
            r50 = r0
            java.lang.String r0 = "default"
            java.lang.String r0 = r11.optString(r13, r0)
            java.lang.String r13 = "ads"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r11, (java.lang.String) r13)
            if (r11 == 0) goto L_0x0794
            java.lang.String r13 = "#ffffff"
            java.lang.String r13 = r11.optString(r14, r13)
            java.lang.String r14 = "image"
            java.lang.String r9 = r11.optString(r14, r9)
            r14 = r4
            r11 = r48
            r4 = 1
            r48 = r13
            r13 = r46
            r46 = r9
            r9 = r3
            r3 = r50
            r50 = r0
            goto L_0x07b8
        L_0x0794:
            r9 = r3
            r14 = r4
            r13 = r46
            r11 = r48
            r3 = r50
            r4 = 1
            r50 = r0
            r46 = r33
            r48 = r46
            goto L_0x07b8
        L_0x07a4:
            r45 = r0
            r49 = r3
            r47 = r4
            r12 = r33
            r46 = r12
            r48 = r46
            r50 = r48
            r3 = 0
            r4 = 0
            r9 = 0
            r11 = 0
            r13 = 1
            r14 = 0
        L_0x07b8:
            java.lang.String r0 = "error"
            org.json.JSONObject r0 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r0)
            if (r0 == 0) goto L_0x07cb
            boolean r51 = io.dcloud.common.util.JSONUtil.isNull(r0, r15)
            if (r51 != 0) goto L_0x07cb
            java.lang.String r0 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r0, (java.lang.String) r15)
            goto L_0x07cd
        L_0x07cb:
            java.lang.String r0 = "file:///android_asset/data/dcloud_error.html"
        L_0x07cd:
            java.lang.String r15 = "ssl"
            org.json.JSONObject r15 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r15)
            r51 = r12
            if (r15 == 0) goto L_0x07e6
            java.lang.String r12 = "untrustedca"
            boolean r12 = io.dcloud.common.util.JSONUtil.isNull(r15, r12)
            if (r12 != 0) goto L_0x07e6
            java.lang.String r12 = "untrustedca"
            java.lang.String r15 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r15, (java.lang.String) r12)
            goto L_0x07e8
        L_0x07e6:
            r15 = r19
        L_0x07e8:
            java.lang.String r12 = "stream"
            org.json.JSONObject r12 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r12)
            r19 = r9
            java.lang.String r9 = r1.p
            boolean r9 = io.dcloud.common.util.BaseInfo.isWap2AppAppid(r9)
            r1.P = r9
            r52 = r11
            if (r12 == 0) goto L_0x081b
            java.lang.String r11 = "competent"
            boolean r9 = r12.optBoolean(r11, r9)
            r1.P = r9
            java.lang.String r9 = "shortcut"
            java.lang.String r9 = r12.optString(r9)
            r1.B1 = r9
            java.lang.String r9 = "shortcutQuit"
            java.lang.String r9 = r12.optString(r9)
            r1.A1 = r9
            java.lang.String r9 = "authority"
            org.json.JSONObject r9 = r12.optJSONObject(r9)
            goto L_0x081d
        L_0x081b:
            r9 = r33
        L_0x081d:
            boolean r11 = r1.P
            if (r11 != 0) goto L_0x0825
            io.dcloud.common.util.BaseInfo.createAppTestFile(r2)
            goto L_0x0828
        L_0x0825:
            io.dcloud.common.util.BaseInfo.removeTestFile(r2)
        L_0x0828:
            java.lang.String r11 = r
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r53 = r9
            java.lang.String r9 = r1.p
            r12.append(r9)
            java.lang.String r9 = " app competent="
            r12.append(r9)
            boolean r9 = r1.P
            r12.append(r9)
            java.lang.String r9 = r12.toString()
            io.dcloud.common.adapter.util.Logger.i(r11, r9)
            boolean r9 = r1.s1
            if (r9 != 0) goto L_0x08c1
            boolean r9 = io.dcloud.e.c.g.c()
            if (r9 != 0) goto L_0x08c1
            io.dcloud.common.DHInterface.IConfusionMgr r9 = r61.getConfusionMgr()
            java.lang.String r11 = "lkdg}lWixxcmq"
            java.lang.String r9 = r9.decryptStr(r11)
            java.lang.String r9 = io.dcloud.common.adapter.util.AndroidResources.getMetaValue(r9)
            boolean r11 = io.dcloud.e.c.g.b()
            if (r11 == 0) goto L_0x0870
            r11 = r36
            boolean r9 = io.dcloud.e.c.h.a.c(r11, r2)
            if (r9 == 0) goto L_0x08b6
            r36 = r15
            goto L_0x08b4
        L_0x0870:
            r11 = r36
            boolean r12 = android.text.TextUtils.isEmpty(r9)
            if (r12 != 0) goto L_0x08b6
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r2)
            java.lang.String r11 = r11.getPackageName()
            r36 = r15
            java.util.Locale r15 = java.util.Locale.ENGLISH
            java.lang.String r11 = r11.toLowerCase(r15)
            r12.append(r11)
            java.lang.String r11 = r12.toString()
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r11)
            r11 = r35
            r12.append(r11)
            java.lang.String r11 = r12.toString()
            java.lang.String r11 = io.dcloud.common.util.Md5Utils.md5((java.lang.String) r11)
            boolean r12 = android.text.TextUtils.isEmpty(r11)
            if (r12 != 0) goto L_0x08b8
            boolean r9 = r9.equalsIgnoreCase(r11)
            if (r9 == 0) goto L_0x08b8
        L_0x08b4:
            r9 = 1
            goto L_0x08b9
        L_0x08b6:
            r36 = r15
        L_0x08b8:
            r9 = 0
        L_0x08b9:
            if (r9 != 0) goto L_0x08c5
            r0 = 4
            r1.setStatus(r0)
            goto L_0x0153
        L_0x08c1:
            r36 = r15
            r9 = r62
        L_0x08c5:
            java.lang.String r11 = "ads"
            org.json.JSONObject r11 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r11)
            java.lang.String r12 = "adid"
            java.lang.String r12 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r12)
            java.lang.String r15 = "wap2app"
            org.json.JSONObject r15 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r15)
            r62 = r9
            if (r15 == 0) goto L_0x08e7
            java.lang.String r9 = "launchError"
            r35 = r12
            java.lang.String r12 = "tip"
            java.lang.String r33 = r15.optString(r9, r12)
            goto L_0x08e9
        L_0x08e7:
            r35 = r12
        L_0x08e9:
            r9 = r33
            android.content.Intent r12 = r1.O0
            if (r12 == 0) goto L_0x0907
            java.lang.String r15 = "unimp_direct_data"
            boolean r12 = r12.hasExtra(r15)
            if (r12 == 0) goto L_0x0907
            r12 = 1
            r1.q1 = r12
            android.content.Intent r12 = r1.O0
            java.lang.String r12 = r12.getStringExtra(r15)
            r1.r1 = r12
            android.content.Intent r12 = r1.O0
            r12.removeExtra(r15)
        L_0x0907:
            android.content.Intent r12 = r1.O0
            java.lang.String r15 = "unimp_run_extra_info"
            r33 = r9
            r9 = 1
            java.lang.String r12 = io.dcloud.common.constant.IntentConst.obtainIntentStringExtra(r12, r15, r9)
            r1.v1 = r12
            java.lang.String r9 = "arguments"
            boolean r9 = r10.has(r9)
            if (r9 == 0) goto L_0x0926
            java.lang.String r9 = "arguments"
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r9)
            r1.setRuntimeArgs(r9)
            goto L_0x093d
        L_0x0926:
            android.content.Intent r9 = r1.O0
            if (r9 == 0) goto L_0x093d
            java.lang.String r10 = "unimp_run_arguments"
            boolean r9 = r9.hasExtra(r10)
            if (r9 == 0) goto L_0x093d
            android.content.Intent r9 = r1.O0
            java.lang.String r10 = "unimp_run_arguments"
            java.lang.String r9 = r9.getStringExtra(r10)
            r1.setRuntimeArgs(r9)
        L_0x093d:
            java.lang.String r9 = "fullscreen"
            r10 = r32
            java.lang.String r9 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r10, (java.lang.String) r9)
            boolean r12 = r1.i
            r15 = 0
            boolean r9 = io.dcloud.common.util.PdrUtil.parseBoolean(r9, r12, r15)
            java.lang.String r12 = r
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r32 = r11
            java.lang.String r11 = r1.p
            r15.append(r11)
            java.lang.String r11 = " app fullScreen="
            r15.append(r11)
            r15.append(r9)
            java.lang.String r11 = r15.toString()
            io.dcloud.common.adapter.util.Logger.i(r12, r11)
            java.util.ArrayList r11 = new java.util.ArrayList
            r11.<init>()
            boolean r12 = r61.q()
            java.lang.String r15 = "permissions"
            org.json.JSONObject r10 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r15)
            if (r10 == 0) goto L_0x0a69
            org.json.JSONArray r15 = r10.names()
            if (r15 == 0) goto L_0x0a69
            r54 = r9
            java.lang.StringBuffer r9 = new java.lang.StringBuffer
            r9.<init>()
            r56 = r62
            r57 = r3
            r55 = r14
            r14 = 0
        L_0x098e:
            int r3 = r15.length()
            if (r14 >= r3) goto L_0x0a47
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONArray) r15, (int) r14)
            r58 = r15
            java.util.Locale r15 = java.util.Locale.ENGLISH
            java.lang.String r3 = r3.toLowerCase(r15)
            r11.add(r3)
            java.lang.String r15 = "push"
            boolean r15 = r3.equals(r15)
            if (r15 == 0) goto L_0x09c3
            org.json.JSONObject r15 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r3)
            r59 = r13
            java.lang.String r13 = "cover"
            java.lang.String r13 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r15, (java.lang.String) r13)
            boolean r15 = io.dcloud.common.adapter.util.PlatformUtil.APS_COVER
            r60 = r4
            r4 = 0
            boolean r13 = io.dcloud.common.util.PdrUtil.parseBoolean(r13, r15, r4)
            io.dcloud.common.adapter.util.PlatformUtil.APS_COVER = r13
            goto L_0x0a0f
        L_0x09c3:
            r60 = r4
            r59 = r13
            java.lang.String r4 = "webview"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L_0x09d6
            java.lang.String r4 = "ui"
            r11.add(r4)
            goto L_0x0a0f
        L_0x09d6:
            java.lang.String r4 = "ui"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L_0x09ef
            java.lang.String r4 = "webview"
            r11.add(r4)
            java.lang.String r4 = "nativeui"
            r11.add(r4)
            java.lang.String r4 = "navigator"
            r11.add(r4)
            goto L_0x0a0f
        L_0x09ef:
            java.lang.String r4 = "maps"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L_0x0a0f
            java.lang.String r4 = "Maps"
            org.json.JSONObject r4 = io.dcloud.common.util.JSONUtil.getJSONObject((org.json.JSONObject) r10, (java.lang.String) r4)
            if (r4 == 0) goto L_0x0a0f
            java.lang.String r13 = "coordType"
            boolean r13 = r4.has(r13)
            if (r13 == 0) goto L_0x0a0f
            java.lang.String r13 = "coordType"
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r4, (java.lang.String) r13)
            r1.p1 = r4
        L_0x0a0f:
            if (r31 == 0) goto L_0x0a3d
            if (r30 != 0) goto L_0x0a3d
            if (r12 != 0) goto L_0x0a3d
            boolean r4 = io.dcloud.common.core.permission.PermissionControler.checkSafePermission(r2, r3)
            if (r4 != 0) goto L_0x0a3d
            io.dcloud.e.b.a r4 = r1.y
            io.dcloud.common.DHInterface.IMgr$MgrType r13 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r15 = 9
            java.lang.Object r4 = r4.processEvent(r13, r15, r3)
            java.lang.Boolean r4 = (java.lang.Boolean) r4
            boolean r4 = r4.booleanValue()
            if (r4 != 0) goto L_0x0a3d
            int r4 = r9.length()
            if (r4 <= 0) goto L_0x0a38
            java.lang.String r4 = ","
            r9.append(r4)
        L_0x0a38:
            r9.append(r3)
            r56 = 0
        L_0x0a3d:
            int r14 = r14 + 1
            r15 = r58
            r13 = r59
            r4 = r60
            goto L_0x098e
        L_0x0a47:
            r60 = r4
            r59 = r13
            if (r56 != 0) goto L_0x0a66
            if (r31 == 0) goto L_0x0a66
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_5PRUNTIME_LACK_MODULE
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]
            r4 = 0
            r3[r4] = r9
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r0, r3)
            r3 = -1229(0xfffffffffffffb33, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r3, (java.lang.String) r0)
            r5.b = r0
            r5.a = r2
            return r4
        L_0x0a66:
            r9 = r56
            goto L_0x0a75
        L_0x0a69:
            r57 = r3
            r60 = r4
            r54 = r9
            r59 = r13
            r55 = r14
            r9 = r62
        L_0x0a75:
            if (r9 == 0) goto L_0x0b3a
            io.dcloud.common.util.BaseInfo$BaseAppInfo r3 = r1.t
            if (r3 == 0) goto L_0x0a7f
            java.lang.String r4 = r1.A
            r3.mAppVer = r4
        L_0x0a7f:
            r3 = r29
            r1.A = r3
            r3 = r28
            r1.B = r3
            r1.B0 = r11
            r1.p = r2
            r2 = r64
            r1.z = r2
            r2 = r63
            r1.t0 = r2
            r1.M = r8
            r1.N = r7
            r2 = r26
            r1.F = r2
            r2 = r20
            r1.G = r2
            r2 = r18
            r1.H = r2
            r2 = r21
            r1.J = r2
            r2 = r25
            r1.l0 = r2
            r2 = r22
            r1.n0 = r2
            r2 = r23
            r1.s0 = r2
            r1.L = r6
            r1.q0 = r0
            r0 = r60
            r1.W = r0
            r0 = r59
            r1.Y = r0
            r0 = r57
            r1.Z = r0
            r3 = r49
            r1.j0 = r3
            r4 = r55
            r1.X = r4
            r0 = r47
            r1.Q = r0
            r0 = r54
            r1.i = r0
            r2 = r24
            r1.U = r2
            r15 = r36
            r1.p0 = r15
            io.dcloud.common.util.BaseInfo.untrustedca = r15
            r0 = r52
            r1.b0 = r0
            r3 = r19
            r1.c0 = r3
            r12 = r51
            r1.d0 = r12
            r0 = r50
            r1.e0 = r0
            r0 = r45
            r1.D0 = r0
            r0 = r41
            r1.F0 = r0
            r0 = r40
            r1.i1 = r0
            r0 = r44
            r1.G0 = r0
            r2 = r39
            r1.H0 = r2
            r0 = r38
            r1.J0 = r0
            r0 = r53
            r1.I0 = r0
            r0 = r32
            r1.L0 = r0
            r0 = r35
            r1.N0 = r0
            r13 = r37
            r1.f0 = r13
            r13 = r43
            r1.g0 = r13
            r6 = r34
            r1.h0 = r6
            r6 = r42
            r1.i0 = r6
            r13 = r48
            r1.Y0 = r13
            r0 = r46
            r1.Z0 = r0
            r0 = r33
            r1.M0 = r0
            r2 = r27
            r1.E0 = r2
            r61.l()
            java.lang.String r0 = r61.v()
            r5.b = r0
        L_0x0b3a:
            r0 = r9 ^ 1
            r5.a = r0
            return r9
        L_0x0b3f:
            r0 = move-exception
            r0.printStackTrace()
            r2 = 1
            r5.a = r2
            boolean r0 = r5.c
            if (r0 == 0) goto L_0x0b55
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_WWW_MANIFEST_ERROR_MALFORMED
            r2 = -1226(0xfffffffffffffb36, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r2, (java.lang.String) r0)
            r5.b = r0
            goto L_0x0b5f
        L_0x0b55:
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_MANIFEST_ERROR_MALFORMED
            r2 = -1203(0xfffffffffffffb4d, float:NaN)
            java.lang.String r0 = io.dcloud.common.constant.DOMException.toJSON((int) r2, (java.lang.String) r0)
            r5.b = r0
        L_0x0b5f:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.a(java.io.InputStream, java.lang.String, org.json.JSONObject):boolean");
    }

    public void g() {
        ArrayList<String> arrayList = this.B0;
        if (arrayList != null) {
            arrayList.clear();
            this.B0 = null;
        }
        HashMap<ISysEventListener.SysEventType, ArrayList<ISysEventListener>> hashMap = this.C0;
        if (hashMap != null) {
            hashMap.clear();
            this.C0 = null;
        }
        this.u1.removeData(this.p);
        this.y = null;
        this.t = null;
        this.g1 = false;
    }

    private static PackageInfo c(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00ef  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean c(java.lang.String r10, org.json.JSONObject r11) {
        /*
            r9 = this;
            java.lang.String r0 = r9.obtainAppDataPath()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            int r2 = r0.length()
            r3 = 1
            int r2 = r2 - r3
            r4 = 0
            java.lang.String r2 = r0.substring(r4, r2)
            r1.append(r2)
            java.lang.String r2 = "_backup"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            byte r5 = r9.V
            if (r5 != r3) goto L_0x005f
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005d }
            r6.<init>()     // Catch:{ Exception -> 0x005d }
            java.lang.String r7 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath     // Catch:{ Exception -> 0x005d }
            r6.append(r7)     // Catch:{ Exception -> 0x005d }
            java.lang.String r7 = r9.p     // Catch:{ Exception -> 0x005d }
            r6.append(r7)     // Catch:{ Exception -> 0x005d }
            char r7 = java.io.File.separatorChar     // Catch:{ Exception -> 0x005d }
            r6.append(r7)     // Catch:{ Exception -> 0x005d }
            java.lang.String r7 = io.dcloud.common.util.BaseInfo.APP_WWW_FS_DIR     // Catch:{ Exception -> 0x005d }
            r6.append(r7)     // Catch:{ Exception -> 0x005d }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x005d }
            io.dcloud.common.adapter.io.DHFile.deleteFile(r6)     // Catch:{ Exception -> 0x0097 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0097 }
            r7.<init>()     // Catch:{ Exception -> 0x0097 }
            int r8 = r6.length()     // Catch:{ Exception -> 0x0097 }
            int r8 = r8 - r3
            java.lang.String r8 = r6.substring(r4, r8)     // Catch:{ Exception -> 0x0097 }
            r7.append(r8)     // Catch:{ Exception -> 0x0097 }
            r7.append(r2)     // Catch:{ Exception -> 0x0097 }
            java.lang.String r1 = r7.toString()     // Catch:{ Exception -> 0x0097 }
            goto L_0x0072
        L_0x005d:
            r11 = move-exception
            goto L_0x006f
        L_0x005f:
            if (r5 != 0) goto L_0x0071
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x005d }
            r2.<init>(r0)     // Catch:{ Exception -> 0x005d }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x005d }
            r6.<init>(r1)     // Catch:{ Exception -> 0x005d }
            r2.renameTo(r6)     // Catch:{ Exception -> 0x005d }
            goto L_0x0071
        L_0x006f:
            r6 = r0
            goto L_0x0098
        L_0x0071:
            r6 = r0
        L_0x0072:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0097 }
            r2.<init>(r10)     // Catch:{ Exception -> 0x0097 }
            boolean r7 = io.dcloud.common.util.Zip4JUtil.isEncryptedZip(r2)     // Catch:{ Exception -> 0x0097 }
            if (r7 == 0) goto L_0x0087
            java.lang.String r7 = "password"
            java.lang.String r7 = r11.getString(r7)     // Catch:{ Exception -> 0x0097 }
            io.dcloud.common.util.Zip4JUtil.upZipFileWithPassword(r2, r6, r7)     // Catch:{ Exception -> 0x0097 }
            goto L_0x008a
        L_0x0087:
            io.dcloud.common.util.ZipUtils.upZipFile(r2, r6)     // Catch:{ Exception -> 0x0097 }
        L_0x008a:
            r9.b((byte) r4)     // Catch:{ Exception -> 0x0097 }
            r9.setAppDataPath(r6)     // Catch:{ Exception -> 0x0097 }
            java.lang.String r2 = r9.p     // Catch:{ Exception -> 0x0097 }
            boolean r4 = r9.b(r2, r11)     // Catch:{ Exception -> 0x0097 }
            goto L_0x00b9
        L_0x0097:
            r11 = move-exception
        L_0x0098:
            r11.printStackTrace()
            io.dcloud.e.b.g r2 = r9.s
            r2.a = r3
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r8 = 10
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r7[r4] = r8
            java.lang.String r11 = r11.getMessage()
            r7[r3] = r11
            java.lang.String r11 = "{code:%d,message:'%s'}"
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r11, r7)
            r2.b = r11
        L_0x00b9:
            if (r4 != 0) goto L_0x00ef
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r2 = "unZipWebApp failed pFilePath="
            r11.append(r2)
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            java.lang.String r11 = "appmgr"
            io.dcloud.common.adapter.util.Logger.e(r11, r10)
            r9.b((byte) r5)
            r9.setAppDataPath(r0)
            io.dcloud.common.adapter.io.DHFile.deleteFile(r6)     // Catch:{ IOException -> 0x00db }
            goto L_0x00df
        L_0x00db:
            r10 = move-exception
            r10.printStackTrace()
        L_0x00df:
            if (r5 != 0) goto L_0x010b
            java.io.File r10 = new java.io.File
            r10.<init>(r1)
            java.io.File r11 = new java.io.File
            r11.<init>(r6)
            r10.renameTo(r11)
            goto L_0x010b
        L_0x00ef:
            if (r5 != 0) goto L_0x00f9
            io.dcloud.common.adapter.io.DHFile.deleteFile(r1)     // Catch:{ IOException -> 0x00f5 }
            goto L_0x00f9
        L_0x00f5:
            r10 = move-exception
            r10.printStackTrace()
        L_0x00f9:
            io.dcloud.common.util.BaseInfo$BaseAppInfo r10 = new io.dcloud.common.util.BaseInfo$BaseAppInfo
            java.lang.String r11 = r9.p
            java.lang.String r0 = r9.A
            r10.<init>(r11, r0)
            r9.t = r10
            android.app.Activity r11 = r9.getActivity()
            r10.saveToBundleData(r11)
        L_0x010b:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.c(java.lang.String, org.json.JSONObject):boolean");
    }

    /* access modifiers changed from: package-private */
    public boolean f(String str) {
        if (!this.v && this.x) {
            return false;
        }
        Logger.e("Webapp start " + this.p);
        Activity activity = this.a;
        if (activity != null && (activity instanceof WebAppActivity)) {
            ((WebAppActivity) activity).onAppStart(this.p);
            ((WebAppActivity) this.a).onAppStart((IApp) this);
        }
        BaseInfo.s_Runing_App_Count++;
        this.w = true;
        this.x = !this.v;
        setRuntimeArgs(str);
        return a(5);
    }

    public InputStream obtainResInStream(String str) {
        return obtainResInStream((String) null, str);
    }

    private String c(String str) {
        return BaseInfo.sBaseFsSitMapPath + str + "/_sitemap.json";
    }

    public String convert2AbsFullPath(String str) {
        return convert2AbsFullPath((String) null, str);
    }

    /* access modifiers changed from: package-private */
    public void b(byte b2) {
        this.V = b2;
    }

    private static String b(String str) {
        return (str == null || str.length() <= 0 || str.charAt(0) != '/') ? str : b(str.substring(1));
    }

    /* access modifiers changed from: package-private */
    public void b(boolean z2) {
        this.b.onAppUnActive(this);
        if (z2) {
            callSysEventListener(ISysEventListener.SysEventType.onWebAppPause, this);
            callSysEventListener(ISysEventListener.SysEventType.onWebAppBackground, this);
        }
        IApp.IAppStatusListener iAppStatusListener = this.P0;
        if (iAppStatusListener != null) {
            iAppStatusListener.onPause(this, (IApp) null);
        }
        setStatus((byte) 2);
    }

    private void b() {
        if (!q() && io.dcloud.e.c.h.a.c(getActivity())) {
            ThreadPool.self().addThreadTask(new d(obtainAppId()));
        }
    }

    public static String b(Context context) {
        return c(context).versionName;
    }

    private JSONObject a(InputStream inputStream) {
        try {
            if (!this.s1) {
                inputStream = new UnicodeInputStream(inputStream, Charset.defaultCharset().name());
            }
            return new JSONObject(new String(IOUtil.getBytes(inputStream)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void a(Activity activity) {
        super.a(activity);
        AppStatusBarManager appStatusBarManager = this.m;
        if (appStatusBarManager != null) {
            appStatusBarManager.checkImmersedStatusBar(activity, this.b1);
            this.m.isFullScreen = isFullScreen();
        }
        this.l.mJsonViewOption = JSONUtil.createJSONObject("{}");
        this.f = PdrUtil.parseInt(SP.getBundleData((Context) getActivity(), BaseInfo.PDR, "StatusBarHeight"), 0);
        updateScreenInfo(4);
        this.z1 = false;
        IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(getActivity());
        if (!q() && iActivityHandler != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(CreateShortResultReceiver.KEY_VERSIONNAME, this.A);
            hashMap.put("appid", this.p);
            hashMap.put("name", this.t0);
            hashMap.put("adid", this.N0);
            hashMap.put("bg", this.Y0);
            hashMap.put(WXBasicComponentType.IMG, convert2AbsFullPath(this.Z0));
            io.dcloud.a.a(getActivity(), this.p, "save", hashMap);
        }
        Intent intent = activity.getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey(IntentConst.FROM_SHORT_CUT_STRAT) && extras.getBoolean(IntentConst.FROM_SHORT_CUT_STRAT)) {
                this.z1 = true;
            }
            if (extras != null && extras.containsKey(IntentConst.WEBAPP_ACTIVITY_CREATE_SHORTCUT)) {
                this.B1 = extras.getString(IntentConst.WEBAPP_ACTIVITY_CREATE_SHORTCUT);
            }
            if (extras != null && extras.containsKey("shortcutQuit")) {
                this.A1 = extras.getString("shortcutQuit");
            }
            if (extras != null && extras.containsKey(IntentConst.START_FORCE_SHORT_QUIT)) {
                this.A1 = extras.getString(IntentConst.START_FORCE_SHORT_QUIT);
            }
            if (intent.hasExtra(IntentConst.START_FORCE_SHORT)) {
                this.B1 = intent.getStringExtra(IntentConst.START_FORCE_SHORT);
            }
            if (TextUtils.isEmpty(this.B1)) {
                String launchType = BaseInfo.getLaunchType(intent);
                this.B1 = AbsoluteConst.INSTALL_OPTIONS_FORCE;
                if (launchType.equals("scheme")) {
                    this.B1 = "query";
                } else if (this.P) {
                    this.B1 = AbsoluteConst.INSTALL_OPTIONS_FORCE;
                } else {
                    SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) activity, "pdr");
                    String string = orCreateBundle.getString(AbsoluteConst.TEST_RUN + this.p, (String) null);
                    if (TextUtils.isEmpty(string) || !string.equals("__am=t")) {
                        this.B1 = "none";
                    } else {
                        this.B1 = AbsoluteConst.INSTALL_OPTIONS_FORCE;
                    }
                }
            }
        }
        try {
            ADUtils.runThreadCheckADDownload(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: package-private */
    public String a(String str) {
        return this.k0 + str;
    }

    /* access modifiers changed from: package-private */
    public void a(ICallBack iCallBack) {
        if ((BaseInfo.ISDEBUG || this.j0) && this.V == 1) {
            ThreadPool.self().addThreadTask(new c(iCallBack), true);
        } else {
            iCallBack.onCallBack(0, (Object) null);
        }
    }

    private boolean a(int i) {
        PermissionUtil.sUseStreamAppPermissionDialogCount = 0;
        WebViewFactory.sUsePermissionWebviews.clear();
        PermissionUtil.removeTempPermission(this.a, this.p);
        String str = r;
        Logger.e(str, "start0 mAppid===" + this.p);
        BaseInfo.sCurrentAppOriginalAppid = this.z;
        BaseInfo.putStartupTimeData(this.p, String.valueOf(System.currentTimeMillis()));
        BaseInfo.sProcessId = (long) Process.myPid();
        String str2 = r;
        StringBuilder sb = new StringBuilder();
        sb.append(this.p);
        sb.append(this.V == 1 ? " APP_RUNNING_MODE" : " FS_RUNNING_MODE");
        Logger.i(str2, sb.toString());
        t();
        setStatus((byte) 3);
        IApp.IAppStatusListener iAppStatusListener = this.P0;
        if (iAppStatusListener != null) {
            iAppStatusListener.onStart();
        }
        String str3 = r;
        Logger.i(str3, "mLaunchPath=" + this.l0);
        Logger.i("download_manager", "webapp start task begin success appid=" + this.p + " mLaunchPath=" + this.l0);
        String str4 = TestUtil.START_STREAM_APP;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("webapp start appid=");
        sb2.append(this.p);
        TestUtil.print(str4, sb2.toString());
        BaseInfo.setLoadingLaunchePage(true, "start0");
        String stringExtra = getActivity().getIntent().getStringExtra(IntentConst.WEBAPP_ACTIVITY_LAUNCH_PATH);
        if (stringExtra != null && !"".equals(stringExtra.trim())) {
            getActivity().getIntent().removeExtra(IntentConst.WEBAPP_ACTIVITY_LAUNCH_PATH);
            if (!"about:blank".equals(stringExtra)) {
                stringExtra = convert2WebviewFullPath((String) null, stringExtra);
            }
            this.o0 = true;
        } else if (!BaseInfo.isWap2AppAppid(this.p) || TextUtils.isEmpty(this.n0)) {
            stringExtra = convert2WebviewFullPath((String) null, this.l0);
        } else {
            stringExtra = convert2WebviewFullPath((String) null, this.n0);
        }
        if (a((IApp) this) && !new File(a(BaseInfo.sConfigXML)).exists()) {
            stringExtra = TextUtils.isEmpty(this.m0) ? this.o1 : this.m0;
        }
        Uri data = getActivity().getIntent().getData();
        if (data != null && data.toString().endsWith(".html")) {
            stringExtra = data.toString();
        }
        if (this.q1) {
            stringExtra = convert2WebviewFullPath((String) null, "__uniappview.html");
        }
        Object processEvent = this.y.processEvent(IMgr.MgrType.WindowMgr, i, new Object[]{this, stringExtra, Boolean.valueOf(this.T0), this.W0});
        if (processEvent == null) {
            return true;
        }
        return Boolean.parseBoolean(String.valueOf(processEvent));
    }

    private boolean a(IApp iApp) {
        return !TextUtils.isEmpty(iApp.getOriginalDirectPage()) && !iApp.obtainWebAppIntent().hasExtra(IntentConst.DIRECT_PAGE);
    }

    /* access modifiers changed from: package-private */
    public boolean a(boolean z2) {
        if (z2) {
            this.r1 = null;
            this.y.processEvent(IMgr.MgrType.WindowMgr, 76, this);
        }
        setAppDataPath(BaseInfo.sCacheFsAppsPath + this.p + DeviceInfo.sSeparatorChar + BaseInfo.REAL_PRIVATE_WWW_DIR);
        boolean b2 = b(this.p, (JSONObject) null);
        if (!b2) {
            return b2;
        }
        setConfigProperty(IApp.ConfigProperty.CONFIG_funSetUA, String.valueOf(false));
        PermissionUtil.clearUseRejectedCache();
        showSplash();
        this.y.processEvent(IMgr.MgrType.FeatureMgr, 3, this.p);
        callSysEventListener(ISysEventListener.SysEventType.onWebAppReStart, (Object) null);
        this.g1 = false;
        TestUtil.record(AbsoluteConst.RUN_5AP_TIME_KEY);
        return a(10);
    }

    private void a() {
        IPdrModule a2;
        if (!this.s1) {
            if (!SDK.isUniMPSDK()) {
                io.dcloud.e.c.a.f().a((Context) this.a);
            }
            if (!q() && (a2 = io.dcloud.e.c.e.a().a("commit")) != null) {
                Object[] objArr = new Object[3];
                objArr[0] = this;
                objArr[1] = this.N0;
                JSONObject jSONObject = this.y.h;
                objArr[2] = jSONObject != null ? jSONObject.optString("version") : "0.1";
                a2.execute("start_up", objArr);
            }
        }
    }

    public static int a(Context context) {
        return c(context).versionCode;
    }

    /* access modifiers changed from: package-private */
    public IFrameView a(IWebviewStateListener iWebviewStateListener) {
        t();
        return (IFrameView) this.y.processEvent(IMgr.MgrType.WindowMgr, 17, new Object[]{this, convert2WebviewFullPath((String) null, this.l0), iWebviewStateListener});
    }

    /* access modifiers changed from: package-private */
    public IFrameView a(IWebviewStateListener iWebviewStateListener, IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        t();
        return (IFrameView) this.y.processEvent(IMgr.MgrType.WindowMgr, 17, new Object[]{this, convert2WebviewFullPath((String) null, this.l0), iWebviewStateListener, iDCloudWebviewClientListener});
    }

    private boolean a(ISysEventListener iSysEventListener, ISysEventListener.SysEventType sysEventType) {
        if (!(iSysEventListener instanceof IBoot) || PdrUtil.parseBoolean(String.valueOf(this.y.processEvent((IMgr.MgrType) null, 1, iSysEventListener)), false, false)) {
            return true;
        }
        if ((sysEventType == ISysEventListener.SysEventType.onStart || sysEventType == ISysEventListener.SysEventType.onStop || sysEventType == ISysEventListener.SysEventType.onPause || sysEventType == ISysEventListener.SysEventType.onResume) && !(iSysEventListener instanceof ReceiveSystemEventVoucher)) {
            return false;
        }
        return true;
    }

    public static boolean a(ISysEventListener.SysEventType sysEventType) {
        return (sysEventType == ISysEventListener.SysEventType.onKeyDown || sysEventType == ISysEventListener.SysEventType.onKeyUp || sysEventType == ISysEventListener.SysEventType.onKeyLongPress) ? false : true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v2, resolved type: io.dcloud.common.util.XmlUtil$DHNode} */
    /* JADX WARNING: type inference failed for: r8v0 */
    /* JADX WARNING: type inference failed for: r8v1, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r8v6 */
    /* JADX WARNING: type inference failed for: r8v7 */
    /* JADX WARNING: type inference failed for: r8v8 */
    /* JADX WARNING: type inference failed for: r8v10 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0195, code lost:
        r14 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0198, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        io.dcloud.common.adapter.util.Logger.w(r15);
        io.dcloud.common.adapter.io.DHFile.delete(r3);
        r13.s.b = io.dcloud.common.constant.DOMException.toJSON((int) io.dcloud.common.constant.DOMException.CODE_RUNTIME_WGT_OR_WGTU_ERROR_MALFORMED, io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_OR_WGTU_ERROR_MALFORMED);
        r13.s.a = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x01af, code lost:
        io.dcloud.common.util.IOUtil.close((java.io.InputStream) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x01b2, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01b3, code lost:
        r15 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x01b4, code lost:
        r7 = null;
        r10 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
        io.dcloud.common.adapter.io.DHFile.delete(r3);
        r13.s.b = io.dcloud.common.constant.DOMException.toJSON(-4, io.dcloud.common.constant.DOMException.MSG_FILE_NOT_EXIST);
        r13.s.a = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01c9, code lost:
        io.dcloud.common.util.IOUtil.close((java.io.InputStream) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x01cc, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:60:0x01b7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x02ac  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0195 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:1:0x008b] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x01de A[SYNTHETIC, Splitter:B:68:0x01de] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0208 A[Catch:{ Exception -> 0x028f }, LOOP:0: B:80:0x0206->B:81:0x0208, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x022f A[Catch:{ Exception -> 0x028f }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x023f A[SYNTHETIC, Splitter:B:86:0x023f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(java.lang.String r14, org.json.JSONObject r15) {
        /*
            r13 = this;
            java.lang.String r0 = "IO Error"
            io.dcloud.e.b.g r1 = r13.s
            r2 = 1
            r1.c = r2
            r1.d = r2
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath
            r1.append(r3)
            java.lang.String r3 = r13.p
            r1.append(r3)
            char r3 = io.dcloud.common.adapter.util.DeviceInfo.sSeparatorChar
            r1.append(r3)
            java.lang.String r3 = io.dcloud.common.util.BaseInfo.APP_WWW_FS_DIR
            r1.append(r3)
            java.lang.String r1 = r1.toString()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            int r4 = r1.length()
            int r4 = r4 - r2
            r5 = 0
            java.lang.String r4 = r1.substring(r5, r4)
            r3.append(r4)
            java.lang.String r4 = "_unzip"
            r3.append(r4)
            char r4 = java.io.File.separatorChar
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            int r6 = r1.length()
            int r6 = r6 - r2
            java.lang.String r6 = r1.substring(r5, r6)
            r4.append(r6)
            java.lang.String r6 = "_backup"
            r4.append(r6)
            char r6 = java.io.File.separatorChar
            r4.append(r6)
            java.lang.String r4 = r4.toString()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            int r7 = r1.length()
            int r7 = r7 - r2
            java.lang.String r7 = r1.substring(r5, r7)
            r6.append(r7)
            java.lang.String r7 = "_backup1"
            r6.append(r7)
            char r7 = java.io.File.separatorChar
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            java.io.File r7 = new java.io.File
            r7.<init>(r14)
            r14 = -5
            r8 = 0
            io.dcloud.common.adapter.io.DHFile.delete(r3)     // Catch:{ FileNotFoundException -> 0x01b7, Exception -> 0x0198, all -> 0x0195 }
            io.dcloud.common.util.ZipUtils.upZipFile(r7, r3)     // Catch:{ FileNotFoundException -> 0x01b7, Exception -> 0x0198, all -> 0x0195 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            r7.<init>()     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            r7.append(r3)     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            java.lang.String r9 = io.dcloud.common.util.BaseInfo.WGTU_UPDATE_XML     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            r7.append(r9)     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            java.lang.Object r7 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r7)     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            java.io.InputStream r7 = io.dcloud.common.adapter.io.DHFile.getInputStream(r7)     // Catch:{ Exception -> 0x0179, all -> 0x0195 }
            if (r7 != 0) goto L_0x00c0
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ Exception -> 0x0177 }
            r9 = -1221(0xfffffffffffffb3b, float:NaN)
            java.lang.String r10 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_UPDATE_NOT_EXIST     // Catch:{ Exception -> 0x0177 }
            java.lang.String r9 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r10)     // Catch:{ Exception -> 0x0177 }
            r15.b = r9     // Catch:{ Exception -> 0x0177 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ Exception -> 0x0177 }
            r15.a = r2     // Catch:{ Exception -> 0x0177 }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r7)
            return r5
        L_0x00c0:
            io.dcloud.common.util.XmlUtil$DHNode r9 = io.dcloud.common.util.XmlUtil.XML_Parser(r7)     // Catch:{ Exception -> 0x0177 }
            if (r9 == 0) goto L_0x0171
            java.lang.String r10 = "appid"
            java.lang.String r10 = io.dcloud.common.util.XmlUtil.getAttributeValue(r9, r10)     // Catch:{ IOException -> 0x0192 }
            java.lang.String r11 = "basis"
            io.dcloud.common.util.XmlUtil$DHNode r11 = io.dcloud.common.util.XmlUtil.getElement(r9, r11)     // Catch:{ IOException -> 0x0192 }
            java.lang.String r12 = "version"
            java.lang.String r11 = io.dcloud.common.util.XmlUtil.getAttributeValue(r11, r12)     // Catch:{ IOException -> 0x0192 }
            boolean r12 = io.dcloud.common.util.BaseInfo.ISDEBUG     // Catch:{ IOException -> 0x0192 }
            if (r12 != 0) goto L_0x00f5
            java.lang.String r12 = r13.p     // Catch:{ IOException -> 0x0192 }
            boolean r10 = r12.equalsIgnoreCase(r10)     // Catch:{ IOException -> 0x0192 }
            if (r10 == 0) goto L_0x00e6
            goto L_0x00f5
        L_0x00e6:
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x0192 }
            r9 = -1223(0xfffffffffffffb39, float:NaN)
            java.lang.String r10 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_UPDATE_APPID_NOT_MATCH     // Catch:{ IOException -> 0x0192 }
            java.lang.String r9 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r10)     // Catch:{ IOException -> 0x0192 }
            r15.b = r9     // Catch:{ IOException -> 0x0192 }
        L_0x00f2:
            r9 = r8
            r15 = 0
            goto L_0x0154
        L_0x00f5:
            java.lang.String r10 = r13.A     // Catch:{ IOException -> 0x0192 }
            boolean r10 = r10.equals(r11)     // Catch:{ IOException -> 0x0192 }
            if (r10 != 0) goto L_0x0113
            java.lang.String r10 = "force"
            boolean r10 = r15.optBoolean(r10)     // Catch:{ IOException -> 0x0192 }
            if (r10 == 0) goto L_0x0106
            goto L_0x0113
        L_0x0106:
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x0192 }
            r9 = -1224(0xfffffffffffffb38, float:NaN)
            java.lang.String r10 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_UPDATE_VERSION_NOT_MATCH     // Catch:{ IOException -> 0x0192 }
            java.lang.String r9 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r10)     // Catch:{ IOException -> 0x0192 }
            r15.b = r9     // Catch:{ IOException -> 0x0192 }
            goto L_0x00f2
        L_0x0113:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0192 }
            r10.<init>()     // Catch:{ IOException -> 0x0192 }
            r10.append(r3)     // Catch:{ IOException -> 0x0192 }
            java.lang.String r11 = io.dcloud.common.util.BaseInfo.APP_WWW_FS_DIR     // Catch:{ IOException -> 0x0192 }
            r10.append(r11)     // Catch:{ IOException -> 0x0192 }
            java.lang.String r10 = r10.toString()     // Catch:{ IOException -> 0x0192 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x016f }
            r11.<init>()     // Catch:{ IOException -> 0x016f }
            r11.append(r10)     // Catch:{ IOException -> 0x016f }
            java.lang.String r12 = io.dcloud.common.util.BaseInfo.sConfigXML     // Catch:{ IOException -> 0x016f }
            r11.append(r12)     // Catch:{ IOException -> 0x016f }
            java.lang.String r11 = r11.toString()     // Catch:{ IOException -> 0x016f }
            boolean r12 = io.dcloud.common.adapter.io.DHFile.isExist((java.lang.String) r11)     // Catch:{ IOException -> 0x016f }
            if (r12 == 0) goto L_0x015b
            java.lang.Object r11 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r11)     // Catch:{ IOException -> 0x016f }
            java.io.InputStream r11 = io.dcloud.common.adapter.io.DHFile.getInputStream(r11)     // Catch:{ IOException -> 0x016f }
            java.lang.String r12 = r13.p     // Catch:{ IOException -> 0x016f }
            boolean r15 = r13.a(r11, r12, r15)     // Catch:{ IOException -> 0x016f }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r11)     // Catch:{ IOException -> 0x016f }
            java.lang.String r11 = "remove"
            io.dcloud.common.util.XmlUtil$DHNode r8 = io.dcloud.common.util.XmlUtil.getElement(r9, r11)     // Catch:{ IOException -> 0x016f }
            r9 = r8
            r8 = r10
        L_0x0154:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r7)
            r10 = r8
            r8 = r9
            goto L_0x01dc
        L_0x015b:
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x016f }
            r9 = -1225(0xfffffffffffffb37, float:NaN)
            java.lang.String r11 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_WWW_MANIFEST_NOT_EXIST     // Catch:{ IOException -> 0x016f }
            java.lang.String r9 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r11)     // Catch:{ IOException -> 0x016f }
            r15.b = r9     // Catch:{ IOException -> 0x016f }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x016f }
            r15.a = r2     // Catch:{ IOException -> 0x016f }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r7)
            return r5
        L_0x016f:
            r15 = move-exception
            goto L_0x01cd
        L_0x0171:
            java.lang.Exception r15 = new java.lang.Exception     // Catch:{ Exception -> 0x0177 }
            r15.<init>()     // Catch:{ Exception -> 0x0177 }
            throw r15     // Catch:{ Exception -> 0x0177 }
        L_0x0177:
            r15 = move-exception
            goto L_0x017b
        L_0x0179:
            r15 = move-exception
            r7 = r8
        L_0x017b:
            r15.printStackTrace()     // Catch:{ IOException -> 0x0192 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x0192 }
            r9 = -1222(0xfffffffffffffb3a, float:NaN)
            java.lang.String r10 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGTU_UPDATE_ERROR_MALFORMED     // Catch:{ IOException -> 0x0192 }
            java.lang.String r9 = io.dcloud.common.constant.DOMException.toJSON((int) r9, (java.lang.String) r10)     // Catch:{ IOException -> 0x0192 }
            r15.b = r9     // Catch:{ IOException -> 0x0192 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x0192 }
            r15.a = r2     // Catch:{ IOException -> 0x0192 }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r7)
            return r5
        L_0x0192:
            r15 = move-exception
            r10 = r8
            goto L_0x01cd
        L_0x0195:
            r14 = move-exception
            goto L_0x02bb
        L_0x0198:
            r15 = move-exception
            io.dcloud.common.adapter.util.Logger.w(r15)     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.common.adapter.io.DHFile.delete(r3)     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            r7 = -1201(0xfffffffffffffb4f, float:NaN)
            java.lang.String r9 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_OR_WGTU_ERROR_MALFORMED     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r7, (java.lang.String) r9)     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            r15.b = r7     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            r15.a = r2     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r8)
            return r5
        L_0x01b3:
            r15 = move-exception
            r7 = r8
            r10 = r7
            goto L_0x01cd
        L_0x01b7:
            io.dcloud.common.adapter.io.DHFile.delete(r3)     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            r7 = -4
            java.lang.String r9 = io.dcloud.common.constant.DOMException.MSG_FILE_NOT_EXIST     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            java.lang.String r7 = io.dcloud.common.constant.DOMException.toJSON((int) r7, (java.lang.String) r9)     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            r15.b = r7     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            r15.a = r2     // Catch:{ IOException -> 0x01b3, all -> 0x0195 }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r8)
            return r5
        L_0x01cd:
            r15.printStackTrace()     // Catch:{ all -> 0x02b9 }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ all -> 0x02b9 }
            java.lang.String r9 = io.dcloud.common.constant.DOMException.toJSON((int) r14, (java.lang.String) r0)     // Catch:{ all -> 0x02b9 }
            r15.b = r9     // Catch:{ all -> 0x02b9 }
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r7)
            r15 = 0
        L_0x01dc:
            if (r15 == 0) goto L_0x02ac
            io.dcloud.common.adapter.io.DHFile.deleteFile(r4)     // Catch:{ Exception -> 0x028f }
            byte r15 = r13.V     // Catch:{ Exception -> 0x028f }
            if (r15 != r2) goto L_0x01eb
            java.lang.String r15 = r13.k0     // Catch:{ Exception -> 0x028f }
            io.dcloud.common.adapter.io.DHFile.copyDir(r15, r4)     // Catch:{ Exception -> 0x028f }
            goto L_0x01f2
        L_0x01eb:
            if (r15 != 0) goto L_0x01f2
            io.dcloud.common.adapter.io.DHFile.copyFile(r1, r4, r2, r5)     // Catch:{ Exception -> 0x028f }
            r15 = 1
            goto L_0x01f3
        L_0x01f2:
            r15 = 0
        L_0x01f3:
            java.lang.String r7 = "item"
            java.util.ArrayList r7 = io.dcloud.common.util.XmlUtil.getElements(r8, r7)     // Catch:{ Exception -> 0x028f }
            if (r7 == 0) goto L_0x0229
            boolean r8 = r7.isEmpty()     // Catch:{ Exception -> 0x028f }
            if (r8 != 0) goto L_0x0229
            int r8 = r7.size()     // Catch:{ Exception -> 0x028f }
            r9 = 0
        L_0x0206:
            if (r9 >= r8) goto L_0x0229
            java.lang.Object r11 = r7.get(r9)     // Catch:{ Exception -> 0x028f }
            io.dcloud.common.util.XmlUtil$DHNode r11 = (io.dcloud.common.util.XmlUtil.DHNode) r11     // Catch:{ Exception -> 0x028f }
            java.lang.String r12 = "path"
            java.lang.String r11 = io.dcloud.common.util.XmlUtil.getAttributeValue(r11, r12)     // Catch:{ Exception -> 0x028f }
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x028f }
            r12.<init>()     // Catch:{ Exception -> 0x028f }
            r12.append(r4)     // Catch:{ Exception -> 0x028f }
            r12.append(r11)     // Catch:{ Exception -> 0x028f }
            java.lang.String r11 = r12.toString()     // Catch:{ Exception -> 0x028f }
            io.dcloud.common.adapter.io.DHFile.deleteFile(r11)     // Catch:{ Exception -> 0x028f }
            int r9 = r9 + 1
            goto L_0x0206
        L_0x0229:
            int r7 = io.dcloud.common.adapter.io.DHFile.copyFile(r10, r4, r2, r5)     // Catch:{ Exception -> 0x028f }
            if (r2 == r7) goto L_0x023f
            io.dcloud.common.adapter.io.DHFile.deleteFile(r4)     // Catch:{ Exception -> 0x028f }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ Exception -> 0x028f }
            java.lang.String r14 = io.dcloud.common.constant.DOMException.toJSON((int) r14, (java.lang.String) r0)     // Catch:{ Exception -> 0x028f }
            r15.b = r14     // Catch:{ Exception -> 0x028f }
            io.dcloud.e.b.g r14 = r13.s     // Catch:{ Exception -> 0x028f }
            r14.a = r2     // Catch:{ Exception -> 0x028f }
            return r5
        L_0x023f:
            io.dcloud.common.adapter.io.DHFile.deleteFile(r3)     // Catch:{ IOException -> 0x027e }
            if (r15 == 0) goto L_0x025b
            int r7 = r6.length()     // Catch:{ IOException -> 0x027e }
            int r7 = r7 - r2
            java.lang.String r7 = r6.substring(r5, r7)     // Catch:{ IOException -> 0x027e }
            r8 = 47
            int r7 = r7.lastIndexOf(r8)     // Catch:{ IOException -> 0x027e }
            int r7 = r7 + r2
            java.lang.String r7 = r6.substring(r7)     // Catch:{ IOException -> 0x027e }
            io.dcloud.common.adapter.io.DHFile.rename(r1, r7)     // Catch:{ IOException -> 0x027e }
        L_0x025b:
            java.lang.String r7 = io.dcloud.common.util.BaseInfo.APP_WWW_FS_DIR     // Catch:{ IOException -> 0x027e }
            io.dcloud.common.adapter.io.DHFile.rename(r4, r7)     // Catch:{ IOException -> 0x027e }
            if (r15 == 0) goto L_0x0265
            io.dcloud.common.adapter.io.DHFile.deleteFile(r6)     // Catch:{ IOException -> 0x027e }
        L_0x0265:
            r13.b((byte) r5)     // Catch:{ Exception -> 0x028f }
            r13.setAppDataPath(r1)     // Catch:{ Exception -> 0x028f }
            io.dcloud.common.util.BaseInfo$BaseAppInfo r14 = new io.dcloud.common.util.BaseInfo$BaseAppInfo     // Catch:{ Exception -> 0x028f }
            java.lang.String r15 = r13.p     // Catch:{ Exception -> 0x028f }
            java.lang.String r0 = r13.A     // Catch:{ Exception -> 0x028f }
            r14.<init>(r15, r0)     // Catch:{ Exception -> 0x028f }
            r13.t = r14     // Catch:{ Exception -> 0x028f }
            android.app.Activity r15 = r13.getActivity()     // Catch:{ Exception -> 0x028f }
            r14.saveToBundleData(r15)     // Catch:{ Exception -> 0x028f }
            goto L_0x02b8
        L_0x027e:
            r15 = move-exception
            r15.printStackTrace()     // Catch:{ Exception -> 0x028f }
            io.dcloud.e.b.g r15 = r13.s     // Catch:{ Exception -> 0x028f }
            java.lang.String r14 = io.dcloud.common.constant.DOMException.toJSON((int) r14, (java.lang.String) r0)     // Catch:{ Exception -> 0x028f }
            r15.b = r14     // Catch:{ Exception -> 0x028f }
            io.dcloud.e.b.g r14 = r13.s     // Catch:{ Exception -> 0x028f }
            r14.a = r2     // Catch:{ Exception -> 0x028f }
            return r5
        L_0x028f:
            r14 = move-exception
            r14.printStackTrace()
            io.dcloud.common.adapter.io.DHFile.deleteFile(r3)     // Catch:{ IOException -> 0x0297 }
            goto L_0x029b
        L_0x0297:
            r14 = move-exception
            r14.printStackTrace()
        L_0x029b:
            io.dcloud.e.b.g r14 = r13.s
            java.lang.String r15 = io.dcloud.common.constant.DOMException.MSG_UNKNOWN_ERROR
            r0 = -99
            java.lang.String r15 = io.dcloud.common.constant.DOMException.toJSON((int) r0, (java.lang.String) r15)
            r14.b = r15
            io.dcloud.e.b.g r14 = r13.s
            r14.a = r2
            return r5
        L_0x02ac:
            io.dcloud.e.b.g r14 = r13.s
            r14.a = r2
            io.dcloud.common.adapter.io.DHFile.deleteFile(r3)     // Catch:{ IOException -> 0x02b4 }
            goto L_0x02b8
        L_0x02b4:
            r14 = move-exception
            r14.printStackTrace()
        L_0x02b8:
            return r5
        L_0x02b9:
            r14 = move-exception
            r8 = r7
        L_0x02bb:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r8)
            goto L_0x02c0
        L_0x02bf:
            throw r14
        L_0x02c0:
            goto L_0x02bf
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.a(java.lang.String, org.json.JSONObject):boolean");
    }

    private String a(byte b2) {
        byte b3 = this.V;
        if (b3 == 1) {
            return BaseInfo.sBaseResAppsFullPath + this.p + "/" + BaseInfo.APP_WWW_FS_DIR;
        } else if (b3 != 0) {
            return null;
        } else {
            return DeviceInfo.FILE_PROTOCOL + this.k0;
        }
    }

    public void a(String str, int i) {
        this.F1.put(str, Integer.valueOf(i));
        if (TextUtils.isEmpty(this.G1)) {
            this.G1 = str + "=" + i;
        } else {
            this.G1 += "&" + str + "=" + i;
        }
        SP.getOrCreateBundle((Context) getActivity(), this.p + "_" + 1).edit().putString("Authorize", this.G1).commit();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001c, code lost:
        if (r4.equals(io.dcloud.common.constant.AbsoluteConst.UNI_V3) != false) goto L_0x001e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0021  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean a(org.json.JSONObject r4, io.dcloud.e.b.g r5) {
        /*
            r3 = this;
            r0 = 1
            if (r4 == 0) goto L_0x001e
            java.lang.String r1 = "control"
            boolean r2 = r4.has(r1)
            if (r2 == 0) goto L_0x001e
            r2 = 0
            java.lang.String r4 = r4.optString(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r4)
            if (r1 != 0) goto L_0x001f
            java.lang.String r1 = "uni-v3"
            boolean r4 = r4.equals(r1)
            if (r4 == 0) goto L_0x001f
        L_0x001e:
            r2 = 1
        L_0x001f:
            if (r2 != 0) goto L_0x002d
            r5.a = r0
            java.lang.String r4 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_COMPONENTS_MODE_NOT_SUPPORT
            r0 = 1250(0x4e2, float:1.752E-42)
            java.lang.String r4 = io.dcloud.common.constant.DOMException.toJSON((int) r0, (java.lang.String) r4)
            r5.b = r4
        L_0x002d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.e.a(org.json.JSONObject, io.dcloud.e.b.g):boolean");
    }
}
