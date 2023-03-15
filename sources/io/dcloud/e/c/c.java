package io.dcloud.e.c;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.WXConfig;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IBoot;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.AsyncTaskHandler;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.DownloadUtil;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.core.ui.l;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.db.DCStorage;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.common.util.net.NetMgr;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONObject;

class c implements ICore {
    private static c a;
    boolean b = false;
    Context c = null;
    Context d = null;
    AbsMgr e = null;
    AbsMgr f = null;
    AbsMgr g = null;
    AbsMgr h = null;
    String i = "CORE";
    private ICore.ICoreStatusListener j = null;
    HashMap<String, IBoot> k = null;
    final int l = 0;
    final int m = 1;
    final int n = 2;
    final int o = 3;

    class a implements MessageHandler.IMessages {
        final /* synthetic */ IApp a;
        final /* synthetic */ Activity b;
        final /* synthetic */ String c;

        a(IApp iApp, Activity activity, String str) {
            this.a = iApp;
            this.b = activity;
            this.c = str;
        }

        public void execute(Object obj) {
            IApp iApp = this.a;
            if (iApp != null) {
                c.this.a(this.b, iApp);
            }
            boolean z = false;
            Object dispatchEvent = c.this.dispatchEvent(IMgr.MgrType.WindowMgr, 32, new Object[]{this.b, this.c});
            if (dispatchEvent instanceof Boolean) {
                z = ((Boolean) dispatchEvent).booleanValue();
            }
            if (!z && TextUtils.equals(BaseInfo.sLastRunApp, this.c)) {
                BaseInfo.sLastRunApp = null;
                this.b.finish();
            }
        }
    }

    class b implements AsyncTaskHandler.IAsyncTaskListener {
        final /* synthetic */ String a;
        final /* synthetic */ Activity b;
        final /* synthetic */ String c;

        b(String str, Activity activity, String str2) {
            this.a = str;
            this.b = activity;
            this.c = str2;
        }

        public void onCancel() {
        }

        public void onExecuteBegin() {
        }

        public void onExecuteEnd(Object obj) {
            c cVar = c.this;
            Activity activity = this.b;
            cVar.a(activity, this.a, this.c, activity instanceof IOnCreateSplashView ? (IOnCreateSplashView) activity : null);
            if (SDK.isUniMPSDK() && obj != null) {
                Activity activity2 = this.b;
                if (activity2 instanceof IActivityHandler) {
                    ((IActivityHandler) activity2).onAsyncStartAppEnd(this.a, obj);
                }
            }
        }

        public Object onExecuting() {
            try {
                String str = TextUtils.isEmpty(this.a) ? BaseInfo.sDefaultBootApp : this.a;
                DCStorage dCStorage = DCStorage.getDCStorage(this.b);
                if (dCStorage != null) {
                    dCStorage.checkSPstorageToDB(this.b, str);
                }
            } catch (Exception unused) {
            }
            if (SDK.isUniMPSDK()) {
                Activity activity = this.b;
                if (activity instanceof IActivityHandler) {
                    return ((IActivityHandler) activity).onAsyncStartAppStart(this.a);
                }
            }
            return 0;
        }
    }

    /* renamed from: io.dcloud.e.c.c$c  reason: collision with other inner class name */
    static /* synthetic */ class C0031c {
        static final /* synthetic */ int[] a;
        static final /* synthetic */ int[] b;

        /* JADX WARNING: Can't wrap try/catch for region: R(17:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|21|22|(3:23|24|26)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(21:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|21|22|23|24|26) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0044 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0058 */
        static {
            /*
                io.dcloud.common.DHInterface.IMgr$MgrType[] r0 = io.dcloud.common.DHInterface.IMgr.MgrType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                b = r0
                r1 = 1
                io.dcloud.common.DHInterface.IMgr$MgrType r2 = io.dcloud.common.DHInterface.IMgr.MgrType.AppMgr     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = b     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.common.DHInterface.IMgr$MgrType r3 = io.dcloud.common.DHInterface.IMgr.MgrType.NetMgr     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = b     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.common.DHInterface.IMgr$MgrType r4 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = b     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.common.DHInterface.IMgr$MgrType r5 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType[] r4 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.values()
                int r4 = r4.length
                int[] r4 = new int[r4]
                a = r4
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r5 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onStart     // Catch:{ NoSuchFieldError -> 0x0044 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0044 }
                r4[r5] = r1     // Catch:{ NoSuchFieldError -> 0x0044 }
            L_0x0044:
                int[] r1 = a     // Catch:{ NoSuchFieldError -> 0x004e }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r4 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onStop     // Catch:{ NoSuchFieldError -> 0x004e }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x004e }
                r1[r4] = r0     // Catch:{ NoSuchFieldError -> 0x004e }
            L_0x004e:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0058 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onPause     // Catch:{ NoSuchFieldError -> 0x0058 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0058 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0058 }
            L_0x0058:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x0062 }
                io.dcloud.common.DHInterface.ISysEventListener$SysEventType r1 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onResume     // Catch:{ NoSuchFieldError -> 0x0062 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0062 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0062 }
            L_0x0062:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.c.C0031c.<clinit>():void");
        }
    }

    private c() {
    }

    public void b(Activity activity) {
        ISysEventListener.SysEventType sysEventType = ISysEventListener.SysEventType.onPause;
        a((Context) activity, sysEventType, (Object) null);
        if (this.b) {
            AbsMgr absMgr = this.g;
            if (absMgr != null) {
                absMgr.onExecute(sysEventType, (Object) null);
            }
            onActivityExecute(activity, sysEventType, (Object) null);
        }
        System.gc();
    }

    public void c(Activity activity) {
        ISysEventListener.SysEventType sysEventType = ISysEventListener.SysEventType.onResume;
        a((Context) activity, sysEventType, (Object) null);
        this.g.onExecute(sysEventType, (Object) null);
        onActivityExecute(activity, sysEventType, (Object) null);
    }

    public boolean d(Activity activity) {
        DownloadUtil.getInstance(activity).stop();
        PlatformUtil.invokeMethod("io.dcloud.feature.apsqh.QHNotifactionReceiver", "doSaveNotifications", (Object) null, new Class[]{Context.class}, new Object[]{activity.getBaseContext()});
        try {
            ISysEventListener.SysEventType sysEventType = ISysEventListener.SysEventType.onStop;
            onActivityExecute(activity, sysEventType, (Object) null);
            this.b = false;
            BaseInfo.setLoadingLaunchePage(false, "onStop");
            a((Context) activity, sysEventType, (Object) null);
            a = null;
            this.e.dispose();
            this.e = null;
            this.f.dispose();
            this.f = null;
            this.g.dispose();
            this.g = null;
            this.h.dispose();
            this.h = null;
            Logger.e(Logger.MAIN_TAG, "core exit");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return true;
    }

    public Object dispatchEvent(IMgr.MgrType mgrType, int i2, Object obj) {
        if (mgrType == null) {
            return a(i2, obj);
        }
        try {
            if (a == null) {
                a = this;
            }
            int i3 = C0031c.b[mgrType.ordinal()];
            if (i3 == 1) {
                AbsMgr absMgr = a.f;
                if (absMgr != null) {
                    return absMgr.processEvent(mgrType, i2, obj);
                }
            } else if (i3 == 2) {
                AbsMgr absMgr2 = a.g;
                if (absMgr2 != null) {
                    return absMgr2.processEvent(mgrType, i2, obj);
                }
            } else if (i3 == 3) {
                AbsMgr absMgr3 = a.h;
                if (absMgr3 != null) {
                    return absMgr3.processEvent(mgrType, i2, obj);
                }
            } else if (i3 == 4) {
                AbsMgr absMgr4 = a.e;
                if (absMgr4 != null) {
                    return absMgr4.processEvent(mgrType, i2, obj);
                }
            }
        } catch (Exception e2) {
            Logger.w("Core.dispatchEvent", e2);
        }
        return null;
    }

    public Context obtainActivityContext() {
        return this.d;
    }

    public Context obtainContext() {
        return this.c;
    }

    public boolean onActivityExecute(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj) {
        String str;
        Bundle extras;
        if (obj instanceof IApp) {
            str = ((IApp) obj).obtainAppId();
        } else {
            str = BaseInfo.sRuntimeMode != null ? null : BaseInfo.sDefaultBootApp;
            Intent intent = activity.getIntent();
            if (!(intent == null || (extras = intent.getExtras()) == null || !extras.containsKey("appid"))) {
                str = extras.getString("appid");
            }
        }
        Object dispatchEvent = dispatchEvent(IMgr.MgrType.AppMgr, 1, new Object[]{sysEventType, obj, str});
        ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onKeyUp;
        if (!sysEventType.equals(sysEventType2) || dispatchEvent == null || ((Boolean) dispatchEvent).booleanValue() || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
            return Boolean.parseBoolean(String.valueOf(dispatchEvent));
        }
        if (sysEventType.equals(sysEventType2)) {
            if (activity instanceof IActivityHandler) {
                ((IActivityHandler) activity).closeAppStreamSplash(str);
            }
            a(activity, activity.getIntent(), (IApp) null, str);
        }
        return true;
    }

    public void onRestart(Context context) {
        Collection<IBoot> values;
        HashMap<String, IBoot> hashMap = this.k;
        if (hashMap != null && (values = hashMap.values()) != null) {
            for (IBoot next : values) {
                if (next != null) {
                    next.onRestart(context);
                }
            }
        }
    }

    public void setmCoreListener(ICore.ICoreStatusListener iCoreStatusListener) {
        this.j = iCoreStatusListener;
    }

    public static c a(Context context, ICore.ICoreStatusListener iCoreStatusListener) {
        if (a == null) {
            a = new c();
        }
        c cVar = a;
        cVar.c = context;
        cVar.j = iCoreStatusListener;
        SDK.initSDK(cVar);
        return a;
    }

    public void a(Activity activity, Bundle bundle, SDK.IntegratedMode integratedMode) {
        this.d = activity;
        DownloadUtil.getInstance(activity);
        WebViewFactory.resetSysWebViewState();
        a();
        a(activity);
        BaseInfo.sRuntimeMode = integratedMode;
        if (integratedMode != null) {
            BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT = true;
        }
        if (!this.b) {
            DCloudAdapterUtil.getIActivityHandler(activity);
            a(activity.getApplicationContext());
            Logger.i("Core onInit mode=" + integratedMode);
            a((Context) activity, ISysEventListener.SysEventType.onStart, (Object) bundle);
            Logger.i("Core onInit mCoreListener=" + this.j);
            try {
                SDK.IntegratedMode integratedMode2 = BaseInfo.sRuntimeMode;
                if (integratedMode2 != null) {
                    if (integratedMode2 != SDK.IntegratedMode.RUNTIME) {
                        ICore.ICoreStatusListener iCoreStatusListener = this.j;
                        if (iCoreStatusListener != null) {
                            iCoreStatusListener.onCoreInitEnd(this);
                            return;
                        }
                        return;
                    }
                }
                ICore.ICoreStatusListener iCoreStatusListener2 = this.j;
                if (iCoreStatusListener2 != null) {
                    iCoreStatusListener2.onCoreInitEnd(this);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void a(Activity activity) {
        JSONObject jSONObject = new JSONObject();
        try {
            String str = Build.MANUFACTURER;
            jSONObject.put("deviceBrand", str);
            jSONObject.put("deviceModel", Build.MODEL);
            jSONObject.put("deviceType", DeviceInfo.getSystemUIModeType(activity));
            jSONObject.put("uniPlatform", AbsoluteConst.XML_APP);
            jSONObject.put(WXConfig.osName, WXEnvironment.OS);
            jSONObject.put("osAndroidAPILevel", Build.VERSION.SDK_INT);
            jSONObject.put("osVersion", Build.VERSION.RELEASE);
            jSONObject.put("osLanguage", LanguageUtil.getDeviceDefLocalLanguage());
            jSONObject.put("romName", io.dcloud.e.c.h.c.b(str));
            jSONObject.put("romVersion", io.dcloud.e.c.h.c.c(Build.BRAND));
        } catch (Exception unused) {
        }
        DeviceInfo.sSystemInfo = jSONObject;
    }

    private void a(Context context) {
        TestUtil.record("core", (Object) Thread.currentThread());
        this.h = new io.dcloud.g.b(this);
        this.k = (HashMap) dispatchEvent(IMgr.MgrType.FeatureMgr, 0, this.c);
        BaseInfo.parseControl(context, this, this.j);
        Logger.initLogger(context);
        DeviceInfo.init(context);
        this.f = new io.dcloud.e.b.a(this);
        this.e = new l(this);
        this.g = new NetMgr(this);
        this.b = true;
    }

    /* access modifiers changed from: private */
    public void a(Activity activity, IApp iApp) {
        onActivityExecute(activity, ISysEventListener.SysEventType.onWebAppStop, iApp);
    }

    public void a(Activity activity, Intent intent, IApp iApp, String str) {
        ICore.ICoreStatusListener iCoreStatusListener = this.j;
        boolean z = iCoreStatusListener != null ? !iCoreStatusListener.onCoreStop() : true;
        this.d = null;
        if (iApp != null) {
            dispatchEvent(IMgr.MgrType.AppMgr, 13, iApp);
        }
        if (z) {
            IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
            TextUtils.equals(activity.getIntent().getStringExtra("appid"), str);
            if (iActivityHandler == null || iApp != null) {
                activity.finish();
            } else if (!MobilePhoneModel.HUAWEI.equalsIgnoreCase(Build.MANUFACTURER) || Build.VERSION.SDK_INT < 24) {
                MessageHandler.sendMessage(new a(iApp, activity, str), (long) 10, (Object) null);
            } else {
                if (iApp != null) {
                    a(activity, iApp);
                }
                Object dispatchEvent = dispatchEvent(IMgr.MgrType.WindowMgr, 32, new Object[]{activity, str});
                if (!(dispatchEvent instanceof Boolean ? ((Boolean) dispatchEvent).booleanValue() : false)) {
                    activity.finish();
                }
            }
        }
        BaseInfo.sGlobalFullScreen = false;
    }

    public IApp a(Activity activity, String str, String str2, IOnCreateSplashView iOnCreateSplashView) {
        return a(activity, str, str2, iOnCreateSplashView, false);
    }

    /* access modifiers changed from: package-private */
    public boolean a(Intent intent, String str) {
        String str2 = BaseInfo.sCacheFsAppsPath + str + "/www/";
        if (new File(str2).exists()) {
            File file = new File(str2 + "/manifest.json");
            if (file.exists() && file.length() > 0) {
                return true;
            }
        }
        if (!intent.hasExtra(IntentConst.DIRECT_PAGE) || !BaseInfo.isWap2AppAppid(str)) {
            return !intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false);
        }
        return true;
    }

    public IApp a(Activity activity, String str, String str2, IOnCreateSplashView iOnCreateSplashView, boolean z) {
        TestUtil.record("GET_STATUS_BY_APPID");
        Logger.d("syncStartApp " + str);
        IMgr.MgrType mgrType = IMgr.MgrType.AppMgr;
        Object dispatchEvent = dispatchEvent(mgrType, 12, str);
        byte parseByte = dispatchEvent != null ? Byte.parseByte(String.valueOf(dispatchEvent)) : 1;
        TestUtil.print("GET_STATUS_BY_APPID");
        boolean a2 = a(activity.getIntent(), str);
        if (1 == parseByte) {
            Logger.d("syncStartApp " + str + " STATUS_UN_RUNNING");
            if (iOnCreateSplashView != null) {
                Logger.d("syncStartApp " + str + " ShowSplash");
                iOnCreateSplashView.onCreateSplash(activity);
            }
        }
        if (!a2) {
            return null;
        }
        try {
            String str3 = TestUtil.START_APP_SET_ROOTVIEW;
            TestUtil.record(str3, "启动" + str);
            IApp iApp = (IApp) dispatchEvent(mgrType, 0, new Object[]{activity, str, str2});
            iApp.setOnCreateSplashView(iOnCreateSplashView);
            if (z && (3 == parseByte || 2 == parseByte)) {
                onActivityExecute(activity, ISysEventListener.SysEventType.onNewIntent, str2);
            }
            return iApp;
        } catch (Exception unused) {
            Logger.d("syncStartApp appid=" + str);
            return null;
        }
    }

    public void a(Activity activity, String str, String str2) {
        AsyncTaskHandler.executeThreadTask(new b(str, activity, str2));
    }

    private void a(Context context, ISysEventListener.SysEventType sysEventType, Object obj) {
        Collection<IBoot> values;
        HashMap<String, IBoot> hashMap = this.k;
        if (hashMap != null && (values = hashMap.values()) != null) {
            for (IBoot next : values) {
                if (next != null) {
                    try {
                        int i2 = C0031c.a[sysEventType.ordinal()];
                        if (i2 == 1) {
                            next.onStart(context, (Bundle) obj, new String[]{BaseInfo.sDefaultBootApp});
                        } else if (i2 == 2) {
                            next.onStop();
                        } else if (i2 == 3) {
                            next.onPause();
                        } else if (i2 == 4) {
                            next.onResume();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.Object[]} */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object a(int r5, java.lang.Object r6) {
        /*
            r4 = this;
            r0 = -1
            r1 = 0
            if (r5 == r0) goto L_0x006f
            r0 = 0
            r2 = 2
            r3 = 1
            if (r5 == 0) goto L_0x0032
            if (r5 == r3) goto L_0x0025
            if (r5 == r2) goto L_0x000e
            goto L_0x0071
        L_0x000e:
            java.lang.Object[] r6 = (java.lang.Object[]) r6
            r5 = r6[r0]
            android.app.Activity r5 = (android.app.Activity) r5
            r0 = r6[r3]
            java.lang.String r0 = (java.lang.String) r0
            r2 = r6[r2]
            java.lang.String r2 = (java.lang.String) r2
            r3 = 3
            r6 = r6[r3]
            io.dcloud.common.DHInterface.IOnCreateSplashView r6 = (io.dcloud.common.DHInterface.IOnCreateSplashView) r6
            r4.a((android.app.Activity) r5, (java.lang.String) r0, (java.lang.String) r2, (io.dcloud.common.DHInterface.IOnCreateSplashView) r6)
            goto L_0x0071
        L_0x0025:
            java.util.HashMap<java.lang.String, io.dcloud.common.DHInterface.IBoot> r5 = r4.k
            io.dcloud.common.DHInterface.IBoot r6 = (io.dcloud.common.DHInterface.IBoot) r6
            boolean r5 = r5.containsValue(r6)
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r5)
            goto L_0x0071
        L_0x0032:
            boolean r5 = r6 instanceof io.dcloud.common.DHInterface.IApp
            if (r5 == 0) goto L_0x0045
            io.dcloud.common.DHInterface.IApp r6 = (io.dcloud.common.DHInterface.IApp) r6
            android.app.Activity r5 = r6.getActivity()
            android.content.Intent r0 = r6.obtainWebAppIntent()
            java.lang.String r2 = r6.obtainAppId()
            goto L_0x005e
        L_0x0045:
            boolean r5 = r6 instanceof java.lang.Object[]
            if (r5 == 0) goto L_0x005a
            java.lang.Object[] r6 = (java.lang.Object[]) r6
            r5 = r6[r0]
            android.app.Activity r5 = (android.app.Activity) r5
            r0 = r6[r3]
            android.content.Intent r0 = (android.content.Intent) r0
            r6 = r6[r2]
            r2 = r6
            java.lang.String r2 = (java.lang.String) r2
            r6 = r1
            goto L_0x005e
        L_0x005a:
            r5 = r1
            r6 = r5
            r0 = r6
            r2 = r0
        L_0x005e:
            boolean r3 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r3 != 0) goto L_0x006b
            io.dcloud.e.c.a r3 = io.dcloud.e.c.a.f()
            r3.g()
        L_0x006b:
            r4.a((android.app.Activity) r5, (android.content.Intent) r0, (io.dcloud.common.DHInterface.IApp) r6, (java.lang.String) r2)
            goto L_0x0071
        L_0x006f:
            io.dcloud.feature.internal.sdk.SDK$IntegratedMode r1 = io.dcloud.common.util.BaseInfo.sRuntimeMode
        L_0x0071:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.c.a(int, java.lang.Object):java.lang.Object");
    }

    private void a() {
        String metaValue = AndroidResources.getMetaValue("DCLOUD_LOCALE");
        if (!PdrUtil.isEmpty(metaValue) && !metaValue.equalsIgnoreCase("default")) {
            String[] split = metaValue.split("_");
            String str = "";
            String lowerCase = (split.length <= 0 || split[0] == null) ? str : split[0].toLowerCase(Locale.ENGLISH);
            if (split.length > 1 && split[1] != null) {
                str = split[1].toUpperCase();
            }
            Locale.setDefault(new Locale(lowerCase, str));
        }
    }
}
