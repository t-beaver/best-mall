package io.dcloud.e.b;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.constant.DataInterface;
import io.dcloud.common.constant.StringConst;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DataUtil;
import io.dcloud.common.util.ErrorDialogUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.internal.sdk.SDK;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public final class a extends AbsMgr implements IMgr.AppEvent {
    /* access modifiers changed from: private */
    public static String a;
    d b = null;
    ArrayList<String> c = new ArrayList<>(1);
    ArrayList<e> d = new ArrayList<>(1);
    c e = null;
    Class[] f = new Class[0];
    /* access modifiers changed from: private */
    public AlertDialog g;
    JSONObject h = null;
    private AlertDialog i;
    private AlertDialog j;

    /* renamed from: io.dcloud.e.b.a$a  reason: collision with other inner class name */
    class C0026a implements ICallBack {
        C0026a() {
        }

        public Object onCallBack(int i, Object obj) {
            String unused = a.a = String.valueOf(obj);
            return null;
        }
    }

    class b implements Runnable {
        final /* synthetic */ Context a;

        b(Context context) {
            this.a = context;
        }

        public void run() {
            String str = "uni-jsframework-dev.js";
            if (!BaseInfo.SyncDebug || PlatformUtil.getResInputStream(str) == null || SDK.isUniMPSDK()) {
                str = "uni-jsframework.js";
            }
            try {
                BaseInfo.uniVersionV3 = new JSONObject(new BufferedReader(new InputStreamReader(this.a.getAssets().open(str))).readLine().substring(2)).optString("version");
            } catch (Exception unused) {
            }
        }
    }

    class c implements DialogInterface.OnClickListener {
        final /* synthetic */ Activity a;
        final /* synthetic */ String b;
        final /* synthetic */ e c;
        final /* synthetic */ CheckBox d;
        final /* synthetic */ String e;
        final /* synthetic */ e f;
        final /* synthetic */ e g;
        final /* synthetic */ boolean h;

        c(Activity activity, String str, e eVar, CheckBox checkBox, String str2, e eVar2, e eVar3, boolean z) {
            this.a = activity;
            this.b = str;
            this.c = eVar;
            this.d = checkBox;
            this.e = str2;
            this.f = eVar2;
            this.g = eVar3;
            this.h = z;
        }

        public void onClick(DialogInterface dialogInterface, int i2) {
            if (i2 == -2) {
                a.this.g.dismiss();
                IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(this.a);
                if (iActivityHandler != null) {
                    iActivityHandler.closeAppStreamSplash(this.b);
                    BaseInfo.setLoadingLaunchePage(false, "closeSplashScreen0");
                    if (a.this.e.e() == 0) {
                        this.a.finish();
                        return;
                    }
                    e eVar = this.c;
                    if (eVar != null) {
                        eVar.w();
                    }
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    this.a.startActivity(intent);
                }
            } else if (i2 != -3 && i2 == -1) {
                if (this.d.isChecked()) {
                    Activity activity = this.a;
                    SP.setBundleData(activity, "pdr", AbsoluteConst.TEST_RUN + this.b, "__am=t");
                }
                a.this.a(this.a, this.b, this.e, this.c, this.f, this.g, this.h);
                a.this.g.dismiss();
            }
        }
    }

    class d implements DialogInterface.OnClickListener {
        final /* synthetic */ Activity a;

        d(Activity activity) {
            this.a = activity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://ask.dcloud.net.cn/article/35627"));
            this.a.startActivity(intent);
        }
    }

    class e implements DialogInterface.OnClickListener {
        final /* synthetic */ Activity a;

        e(Activity activity) {
            this.a = activity;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://ask.dcloud.net.cn/article/35877"));
            this.a.startActivity(intent);
        }
    }

    class f implements ICallBack {
        final /* synthetic */ e a;

        f(e eVar) {
            this.a = eVar;
        }

        public Object onCallBack(int i, Object obj) {
            if (AppRuntime.hasPrivacyForNotShown(this.a.getActivity())) {
                return null;
            }
            a.this.mCore.onRestart(this.a.getActivity());
            return null;
        }
    }

    class g implements ICallBack {
        final /* synthetic */ e a;
        final /* synthetic */ boolean b;
        final /* synthetic */ String c;
        final /* synthetic */ ICallBack d;

        g(e eVar, boolean z, String str, ICallBack iCallBack) {
            this.a = eVar;
            this.b = z;
            this.c = str;
            this.d = iCallBack;
        }

        public Object onCallBack(int i, Object obj) {
            if (!this.a.a(this.b)) {
                Logger.e(Logger.AppMgr_TAG, "reboot " + this.c + " app failed !!!");
            } else {
                this.d.onCallBack(0, (Object) null);
            }
            return null;
        }
    }

    class h implements ICallBack {
        final /* synthetic */ e a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;
        final /* synthetic */ boolean d;

        /* renamed from: io.dcloud.e.b.a$h$a  reason: collision with other inner class name */
        class C0027a implements ICallBack {
            C0027a() {
            }

            public Object onCallBack(int i, Object obj) {
                h hVar = h.this;
                a.this.a(hVar.a, hVar.b, hVar.c, hVar.d);
                return null;
            }
        }

        h(e eVar, String str, String str2, boolean z) {
            this.a = eVar;
            this.b = str;
            this.c = str2;
            this.d = z;
        }

        public Object onCallBack(int i, Object obj) {
            if (!WebViewFactory.isOther() || WebViewFactory.isOtherInitialised() || WebViewFactory.isIsLoadOtherTimeOut()) {
                a.this.a(this.a, this.b, this.c, this.d);
                return null;
            }
            WebViewFactory.setOtherCallBack(new C0027a());
            return null;
        }
    }

    class i implements Runnable {
        i() {
        }

        public void run() {
            try {
                DHFile.deleteFile(StringConst.STREAMAPP_KEY_ROOTPATH + "splash_temp/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public a(ICore iCore) {
        super(iCore, Logger.AppMgr_TAG, IMgr.MgrType.AppMgr);
        if (iCore != null) {
            a(iCore.obtainContext());
        }
        c();
        b();
        d();
        a();
        this.e = new c(this);
    }

    private void d(e eVar) {
        Object newInstance = PlatformUtil.newInstance("android.app.ActivityManager$TaskDescription", new Class[]{String.class, Bitmap.class}, new Object[]{eVar.obtainAppName(), BitmapFactory.decodeResource(getContext().getResources(), getContext().getApplicationInfo().icon)});
        PlatformUtil.invokeMethod(eVar.getActivity(), "setTaskDescription", new Class[]{newInstance.getClass()}, newInstance);
    }

    /* access modifiers changed from: package-private */
    public e c(String str) {
        return a((Activity) null, str);
    }

    public void dispose() {
        ArrayList<e> arrayList = this.d;
        if (arrayList != null) {
            Iterator<e> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().g();
            }
        }
        this.d.clear();
        this.c.clear();
        c cVar = this.e;
        if (cVar != null) {
            cVar.a();
        }
        this.e = null;
        ThreadPool.self().addThreadTask(new i());
    }

    /* access modifiers changed from: package-private */
    public void e(e eVar) {
        this.e.b(eVar.p);
        b(eVar);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0437 A[Catch:{ all -> 0x067e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object processEvent(io.dcloud.common.DHInterface.IMgr.MgrType r24, int r25, java.lang.Object r26) {
        /*
            r23 = this;
            r11 = r23
            r0 = r25
            r1 = r26
            java.lang.String r2 = "test_runing"
            java.lang.String r3 = "pdr"
            r12 = 0
            boolean r4 = r23.checkMgrId(r24)     // Catch:{ all -> 0x067e }
            if (r4 != 0) goto L_0x001b
            io.dcloud.common.DHInterface.ICore r2 = r11.mCore     // Catch:{ all -> 0x067e }
            r3 = r24
            java.lang.Object r12 = r2.dispatchEvent(r3, r0, r1)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x001b:
            r4 = 20
            java.lang.String r5 = "appid"
            r6 = 3
            java.lang.String r7 = "/"
            java.lang.String r8 = "appmgr"
            r9 = 2
            r10 = 1
            r13 = 0
            switch(r0) {
                case 0: goto L_0x0444;
                case 1: goto L_0x03dd;
                case 2: goto L_0x0356;
                case 3: goto L_0x02e3;
                case 4: goto L_0x0213;
                case 5: goto L_0x0204;
                case 6: goto L_0x01fb;
                case 7: goto L_0x01d2;
                case 8: goto L_0x01be;
                case 9: goto L_0x0196;
                case 10: goto L_0x0167;
                case 11: goto L_0x0155;
                case 12: goto L_0x012d;
                case 13: goto L_0x011a;
                case 14: goto L_0x00df;
                case 15: goto L_0x002a;
                case 16: goto L_0x00cd;
                case 17: goto L_0x002a;
                case 18: goto L_0x002a;
                case 19: goto L_0x00c5;
                case 20: goto L_0x005d;
                case 21: goto L_0x005d;
                case 22: goto L_0x002a;
                case 23: goto L_0x002a;
                case 24: goto L_0x0059;
                case 25: goto L_0x002a;
                case 26: goto L_0x002a;
                case 27: goto L_0x0045;
                case 28: goto L_0x002c;
                default: goto L_0x002a;
            }
        L_0x002a:
            goto L_0x067c
        L_0x002c:
            io.dcloud.e.b.c r0 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r0.b()     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x003c
            io.dcloud.e.b.c r0 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r0.b()     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x003c:
            r0 = r1
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.a((java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x0045:
            io.dcloud.e.b.c r0 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r0.b()     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x0052
            r11.a((io.dcloud.e.b.e) r0, (java.lang.String) r12, (boolean) r10)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x0052:
            java.lang.String r0 = "not app!!!"
            io.dcloud.common.adapter.util.Logger.e(r8, r0)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x0059:
            java.lang.String r0 = a     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x005d:
            boolean r2 = r1 instanceof java.lang.String     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x0065
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x067e }
            r2 = r12
            goto L_0x0073
        L_0x0065:
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x067e }
            r2 = r1[r13]     // Catch:{ all -> 0x067e }
            android.app.Activity r2 = (android.app.Activity) r2     // Catch:{ all -> 0x067e }
            r2 = r1[r10]     // Catch:{ all -> 0x067e }
            android.content.Intent r2 = (android.content.Intent) r2     // Catch:{ all -> 0x067e }
            r1 = r1[r9]     // Catch:{ all -> 0x067e }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x067e }
        L_0x0073:
            io.dcloud.e.b.c r3 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r3 = r3.a(r1)     // Catch:{ all -> 0x067e }
            java.lang.Boolean r12 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x067e }
            if (r3 == 0) goto L_0x00c1
            io.dcloud.e.b.c r4 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r4 = r4.b()     // Catch:{ all -> 0x067e }
            if (r4 == r3) goto L_0x067c
            if (r4 == 0) goto L_0x008a
            r4.w()     // Catch:{ all -> 0x067e }
        L_0x008a:
            if (r2 == 0) goto L_0x0090
            r3.setWebAppIntent(r2)     // Catch:{ all -> 0x067e }
            goto L_0x0094
        L_0x0090:
            android.content.Intent r2 = r3.obtainWebAppIntent()     // Catch:{ all -> 0x067e }
        L_0x0094:
            r4 = 21
            if (r4 != r0) goto L_0x009d
            java.lang.String r0 = "__webapp_reply__"
            r2.putExtra(r0, r10)     // Catch:{ all -> 0x067e }
        L_0x009d:
            android.app.Activity r0 = r3.getActivity()     // Catch:{ all -> 0x067e }
            r0.setIntent(r2)     // Catch:{ all -> 0x067e }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r0.<init>()     // Catch:{ all -> 0x067e }
            r0.append(r1)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = " will unactive change to active STREAM_START_APP"
            r0.append(r1)     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x067e }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r0)     // Catch:{ all -> 0x067e }
            byte r0 = r3.u     // Catch:{ all -> 0x067e }
            if (r0 != r9) goto L_0x067c
            r3.c()     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x00c1:
            java.lang.Boolean r0 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x00c5:
            io.dcloud.e.b.c r0 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r0.c()     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x00cd:
            r0 = r1
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x067e }
            r1 = r0[r13]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x067e }
            r0 = r0[r10]     // Catch:{ all -> 0x067e }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r1 = (io.dcloud.e.b.e) r1     // Catch:{ all -> 0x067e }
            r1.a((java.lang.String) r0, (int) r10)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x00df:
            java.lang.String r0 = java.lang.String.valueOf(r26)     // Catch:{ all -> 0x067e }
            boolean r1 = r0.endsWith(r7)     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x00f2
            int r1 = r0.length()     // Catch:{ all -> 0x067e }
            int r1 = r1 - r10
            java.lang.String r0 = r0.substring(r13, r1)     // Catch:{ all -> 0x067e }
        L_0x00f2:
            int r1 = r0.lastIndexOf(r7)     // Catch:{ all -> 0x067e }
            int r1 = r1 + r10
            java.lang.String r1 = r0.substring(r1)     // Catch:{ all -> 0x067e }
            boolean r2 = r11.b((java.lang.String) r1)     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x0107
            io.dcloud.e.b.e r0 = r11.c((java.lang.String) r1)     // Catch:{ all -> 0x067e }
        L_0x0105:
            r1 = r0
            goto L_0x010c
        L_0x0107:
            io.dcloud.e.b.e r0 = r11.b(r0, r1)     // Catch:{ all -> 0x067e }
            goto L_0x0105
        L_0x010c:
            io.dcloud.e.b.g r0 = r1.s     // Catch:{ all -> 0x067e }
            boolean r0 = r0.a     // Catch:{ all -> 0x067e }
            if (r0 != 0) goto L_0x067c
            r11.c((io.dcloud.e.b.e) r1)     // Catch:{ all -> 0x0117 }
            goto L_0x0685
        L_0x0117:
            r0 = move-exception
            goto L_0x0680
        L_0x011a:
            r0 = r1
            io.dcloud.e.b.e r0 = (io.dcloud.e.b.e) r0     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x0129
            boolean r0 = r0.r()     // Catch:{ all -> 0x067e }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ all -> 0x067e }
            goto L_0x0685
        L_0x0129:
            java.lang.String r0 = "false"
            goto L_0x0686
        L_0x012d:
            r0 = r1
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.a((java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x014e
            byte r1 = r0.u     // Catch:{ all -> 0x067e }
            if (r1 != r6) goto L_0x0149
            boolean r1 = r0.p()     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x0142
            byte r9 = r0.u     // Catch:{ all -> 0x067e }
        L_0x0142:
            r0.u = r9     // Catch:{ all -> 0x067e }
            java.lang.Byte r0 = java.lang.Byte.valueOf(r9)     // Catch:{ all -> 0x067e }
            goto L_0x0152
        L_0x0149:
            java.lang.Byte r0 = java.lang.Byte.valueOf(r1)     // Catch:{ all -> 0x067e }
            goto L_0x0152
        L_0x014e:
            java.lang.Byte r0 = java.lang.Byte.valueOf(r10)     // Catch:{ all -> 0x067e }
        L_0x0152:
            r1 = r0
            goto L_0x0685
        L_0x0155:
            io.dcloud.e.b.c r0 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r0.b()     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x067c
            io.dcloud.e.b.c r0 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r0.b()     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r0.p     // Catch:{ all -> 0x067e }
            goto L_0x0685
        L_0x0167:
            boolean r0 = r1 instanceof java.lang.String     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x0174
            java.lang.String r0 = java.lang.String.valueOf(r26)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.a((java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            goto L_0x018f
        L_0x0174:
            boolean r0 = r1 instanceof io.dcloud.e.b.e     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x017c
            r0 = r1
            io.dcloud.e.b.e r0 = (io.dcloud.e.b.e) r0     // Catch:{ all -> 0x067e }
            goto L_0x018f
        L_0x017c:
            boolean r0 = r1 instanceof java.util.Map     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x018e
            r0 = r1
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ all -> 0x067e }
            java.lang.Object r0 = r0.get(r5)     // Catch:{ all -> 0x067e }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.a((java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            goto L_0x018f
        L_0x018e:
            r0 = r12
        L_0x018f:
            if (r0 == 0) goto L_0x067c
            r0.u()     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x0196:
            r0 = r1
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x067e }
            r1 = r0[r13]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IApp r1 = (io.dcloud.common.DHInterface.IApp) r1     // Catch:{ all -> 0x067e }
            r2 = r0[r10]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IWebviewStateListener r2 = (io.dcloud.common.DHInterface.IWebviewStateListener) r2     // Catch:{ all -> 0x067e }
            int r3 = r0.length     // Catch:{ all -> 0x067e }
            if (r3 <= r9) goto L_0x01b5
            r0 = r0[r9]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IDCloudWebviewClientListener r0 = (io.dcloud.common.DHInterface.IDCloudWebviewClientListener) r0     // Catch:{ all -> 0x067e }
            boolean r3 = io.dcloud.common.util.PdrUtil.isEmpty(r0)     // Catch:{ all -> 0x067e }
            if (r3 != 0) goto L_0x067c
            io.dcloud.e.b.e r1 = (io.dcloud.e.b.e) r1     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IFrameView r0 = r1.a((io.dcloud.common.DHInterface.IWebviewStateListener) r2, (io.dcloud.common.DHInterface.IDCloudWebviewClientListener) r0)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x01b5:
            io.dcloud.e.b.e r1 = (io.dcloud.e.b.e) r1     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IFrameView r0 = r1.a((io.dcloud.common.DHInterface.IWebviewStateListener) r2)     // Catch:{ all -> 0x067e }
        L_0x01bb:
            r12 = r0
            goto L_0x067c
        L_0x01be:
            r0 = r1
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ all -> 0x067e }
            r1 = r0[r13]     // Catch:{ all -> 0x067e }
            r2 = r0[r10]     // Catch:{ all -> 0x067e }
            r3 = r0[r9]     // Catch:{ all -> 0x067e }
            r0 = r0[r6]     // Catch:{ all -> 0x067e }
            byte r0 = java.lang.Byte.parseByte(r0)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.a((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3, (byte) r0)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x01d2:
            r0 = r1
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.d r1 = r11.b     // Catch:{ all -> 0x067e }
            if (r1 != 0) goto L_0x01e0
            io.dcloud.e.b.d r1 = new io.dcloud.e.b.d     // Catch:{ all -> 0x067e }
            r1.<init>(r11)     // Catch:{ all -> 0x067e }
            r11.b = r1     // Catch:{ all -> 0x067e }
        L_0x01e0:
            io.dcloud.e.b.d r1 = r11.b     // Catch:{ all -> 0x067e }
            r1.a((java.lang.String) r0)     // Catch:{ all -> 0x067e }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r1.<init>()     // Catch:{ all -> 0x067e }
            java.lang.String r2 = "data="
            r1.append(r2)     // Catch:{ all -> 0x067e }
            r1.append(r0)     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r1.toString()     // Catch:{ all -> 0x067e }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r0)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x01fb:
            java.lang.String r0 = java.lang.String.valueOf(r26)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.c((java.lang.String) r0)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x0204:
            java.lang.String r0 = java.lang.String.valueOf(r26)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.c((java.lang.String) r0)     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x067c
            java.lang.String r0 = r0.v()     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x0213:
            r0 = r1
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x067e }
            r1 = r0[r13]     // Catch:{ all -> 0x067e }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x067e }
            r2 = r0[r10]     // Catch:{ all -> 0x067e }
            org.json.JSONObject r2 = (org.json.JSONObject) r2     // Catch:{ all -> 0x067e }
            r0 = r0[r9]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IWebview r0 = (io.dcloud.common.DHInterface.IWebview) r0     // Catch:{ all -> 0x067e }
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r5)     // Catch:{ all -> 0x067e }
            java.lang.String r4 = "recognise"
            java.lang.String r4 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r4)     // Catch:{ all -> 0x067e }
            boolean r4 = java.lang.Boolean.parseBoolean(r4)     // Catch:{ all -> 0x067e }
            if (r4 == 0) goto L_0x0280
            boolean r4 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x067e }
            if (r4 != 0) goto L_0x0280
            java.util.Locale r4 = java.util.Locale.ENGLISH     // Catch:{ all -> 0x067e }
            java.lang.String r5 = r1.toLowerCase(r4)     // Catch:{ all -> 0x067e }
            java.lang.String r6 = ".wgtu"
            boolean r5 = r5.endsWith(r6)     // Catch:{ all -> 0x067e }
            if (r5 != 0) goto L_0x0280
            java.lang.String r4 = r1.toLowerCase(r4)     // Catch:{ all -> 0x067e }
            java.lang.String r5 = ".wgt"
            boolean r4 = r4.endsWith(r5)     // Catch:{ all -> 0x067e }
            if (r4 != 0) goto L_0x0280
            boolean r4 = io.dcloud.common.util.CheckSignatureUtil.check(r1)     // Catch:{ all -> 0x067e }
            if (r4 != 0) goto L_0x0280
            java.lang.Object[] r1 = new java.lang.Object[r9]     // Catch:{ all -> 0x067e }
            java.lang.Boolean r2 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x067e }
            r1[r13] = r2     // Catch:{ all -> 0x067e }
            java.lang.String r2 = "{code:%d,message:'%s'}"
            java.lang.Object[] r3 = new java.lang.Object[r9]     // Catch:{ all -> 0x067e }
            r4 = 10
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ all -> 0x067e }
            r3[r13] = r4     // Catch:{ all -> 0x067e }
            android.content.Context r0 = r0.getContext()     // Catch:{ all -> 0x067e }
            int r4 = io.dcloud.base.R.string.dcloud_common_app_check_failed     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r0.getString(r4)     // Catch:{ all -> 0x067e }
            r3[r10] = r0     // Catch:{ all -> 0x067e }
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r2, r3)     // Catch:{ all -> 0x067e }
            r1[r10] = r0     // Catch:{ all -> 0x067e }
            return r1
        L_0x0280:
            boolean r4 = io.dcloud.common.util.PdrUtil.isEmpty(r3)     // Catch:{ all -> 0x067e }
            if (r4 == 0) goto L_0x0292
            io.dcloud.common.DHInterface.IFrameView r0 = r0.obtainFrameView()     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()     // Catch:{ all -> 0x067e }
            java.lang.String r3 = r0.obtainAppId()     // Catch:{ all -> 0x067e }
        L_0x0292:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x067e }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r0.<init>()     // Catch:{ all -> 0x067e }
            java.lang.String r6 = "install begin _filePath = "
            r0.append(r6)     // Catch:{ all -> 0x067e }
            r0.append(r1)     // Catch:{ all -> 0x067e }
            java.lang.String r6 = ";_mayBeAppid = "
            r0.append(r6)     // Catch:{ all -> 0x067e }
            r0.append(r3)     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x067e }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r0)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r0 = r11.a((java.lang.String) r1, (java.lang.String) r3, (org.json.JSONObject) r2)     // Catch:{ all -> 0x067e }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r1.<init>()     // Catch:{ all -> 0x067e }
            java.lang.String r2 = "install end useTime="
            r1.append(r2)     // Catch:{ all -> 0x067e }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x067e }
            long r2 = r2 - r4
            r1.append(r2)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x067e }
            io.dcloud.common.adapter.util.Logger.d((java.lang.String) r8, (java.lang.String) r1)     // Catch:{ all -> 0x067e }
            java.lang.Object[] r1 = new java.lang.Object[r9]     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.g r2 = r0.s     // Catch:{ all -> 0x067e }
            boolean r2 = r2.a     // Catch:{ all -> 0x067e }
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ all -> 0x067e }
            r1[r13] = r2     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.g r0 = r0.s     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r0.b     // Catch:{ all -> 0x067e }
            r1[r10] = r0     // Catch:{ all -> 0x067e }
            goto L_0x0685
        L_0x02e3:
            java.lang.String r0 = java.lang.String.valueOf(r26)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = "snc:CID"
            boolean r1 = r1.equals(r0)     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x02f6
            io.dcloud.e.b.c r1 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r1 = r1.b()     // Catch:{ all -> 0x067e }
            goto L_0x02fa
        L_0x02f6:
            io.dcloud.e.b.e r1 = r11.a((java.lang.String) r0, (boolean) r10)     // Catch:{ all -> 0x067e }
        L_0x02fa:
            android.app.Activity r2 = r1.getActivity()     // Catch:{ all -> 0x067e }
            boolean r2 = r2 instanceof io.dcloud.WebAppActivity     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x0351
            android.app.Activity r2 = r1.getActivity()     // Catch:{ all -> 0x067e }
            io.dcloud.WebAppActivity r2 = (io.dcloud.WebAppActivity) r2     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x034c
            boolean r3 = r2.isFinishing()     // Catch:{ all -> 0x067e }
            if (r3 != 0) goto L_0x034c
            io.dcloud.common.ui.b r3 = io.dcloud.common.ui.b.a()     // Catch:{ all -> 0x067e }
            android.content.Context r4 = r2.getContext()     // Catch:{ all -> 0x067e }
            boolean r3 = r3.c(r4)     // Catch:{ all -> 0x067e }
            if (r3 == 0) goto L_0x0347
            android.app.Application r0 = r2.getApplication()     // Catch:{ all -> 0x067e }
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch:{ all -> 0x067e }
            android.app.Application r1 = r2.getApplication()     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r1.getPackageName()     // Catch:{ all -> 0x067e }
            android.content.Intent r0 = r0.getLaunchIntentForPackage(r1)     // Catch:{ all -> 0x067e }
            r1 = 67108864(0x4000000, float:1.5046328E-36)
            r0.addFlags(r1)     // Catch:{ all -> 0x067e }
            android.app.Application r1 = r2.getApplication()     // Catch:{ all -> 0x067e }
            r1.startActivity(r0)     // Catch:{ all -> 0x067e }
            int r0 = android.os.Process.myPid()     // Catch:{ all -> 0x067e }
            android.os.Process.killProcess(r0)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x0347:
            r11.a((io.dcloud.e.b.e) r1, (java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x034c:
            r11.a((io.dcloud.e.b.e) r1, (java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x0351:
            r11.a((io.dcloud.e.b.e) r1, (java.lang.String) r0, (boolean) r13)     // Catch:{ all -> 0x067e }
            goto L_0x067c
        L_0x0356:
            java.lang.String r0 = java.lang.String.valueOf(r26)     // Catch:{ all -> 0x067e }
            boolean r1 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r0)     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x0397
            java.lang.String r1 = io.dcloud.common.util.BaseInfo.sBaseFsAppsPath     // Catch:{ all -> 0x067e }
            int r1 = r0.indexOf(r1)     // Catch:{ all -> 0x067e }
            if (r1 < 0) goto L_0x037c
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sBaseFsAppsPath     // Catch:{ all -> 0x067e }
            int r2 = r2.length()     // Catch:{ all -> 0x067e }
            int r1 = r1 + r2
            java.lang.String r1 = r0.substring(r1)     // Catch:{ all -> 0x067e }
            int r2 = r1.indexOf(r7)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r1.substring(r13, r2)     // Catch:{ all -> 0x067e }
            goto L_0x037d
        L_0x037c:
            r1 = r12
        L_0x037d:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x067e }
            if (r2 != 0) goto L_0x038b
            io.dcloud.e.b.e r1 = r11.c((java.lang.String) r1)     // Catch:{ all -> 0x067e }
            java.io.InputStream r12 = io.dcloud.common.adapter.ui.webview.WebResUtil.getEncryptionInputStream(r0, r1)     // Catch:{ all -> 0x067e }
        L_0x038b:
            if (r12 != 0) goto L_0x067c
            java.lang.Object r0 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r0)     // Catch:{ all -> 0x067e }
            java.io.InputStream r0 = io.dcloud.common.adapter.io.DHFile.getInputStream(r0)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x0397:
            io.dcloud.e.b.c r1 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r1 = r1.b()     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r1.p     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r1 = r11.c((java.lang.String) r1)     // Catch:{ all -> 0x067e }
            java.lang.String r2 = "_www/"
            boolean r2 = r0.startsWith(r2)     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x03b3
            if (r1 == 0) goto L_0x067c
            java.io.InputStream r0 = r1.obtainResInStream(r0)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x03b3:
            java.lang.String r2 = "_doc/"
            boolean r2 = r0.startsWith(r2)     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x067c
            r2 = 5
            java.lang.String r0 = r0.substring(r2)     // Catch:{ all -> 0x067e }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r2.<init>()     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r1.obtainAppDocPath()     // Catch:{ all -> 0x067e }
            r2.append(r1)     // Catch:{ all -> 0x067e }
            r2.append(r0)     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x067e }
            java.lang.Object r0 = io.dcloud.common.adapter.io.DHFile.createFileHandler(r0)     // Catch:{ all -> 0x067e }
            java.io.InputStream r0 = io.dcloud.common.adapter.io.DHFile.getInputStream(r0)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x03dd:
            r0 = r1
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x067e }
            r1 = r0[r9]     // Catch:{ all -> 0x067e }
            boolean r1 = r1 instanceof io.dcloud.common.DHInterface.IApp     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x03eb
            r1 = r0[r9]     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r1 = (io.dcloud.e.b.e) r1     // Catch:{ all -> 0x067e }
            goto L_0x03f5
        L_0x03eb:
            r1 = r0[r9]     // Catch:{ all -> 0x067e }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r1 = r11.a((java.lang.String) r1, (boolean) r13)     // Catch:{ all -> 0x067e }
        L_0x03f5:
            io.dcloud.feature.internal.sdk.SDK$IntegratedMode r2 = io.dcloud.common.util.BaseInfo.sRuntimeMode     // Catch:{ all -> 0x067e }
            if (r2 != 0) goto L_0x0408
            if (r1 != 0) goto L_0x0408
            r2 = r0[r13]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r2 = (io.dcloud.common.DHInterface.ISysEventListener.SysEventType) r2     // Catch:{ all -> 0x067e }
            boolean r2 = io.dcloud.e.b.e.a((io.dcloud.common.DHInterface.ISysEventListener.SysEventType) r2)     // Catch:{ all -> 0x067e }
            if (r2 == 0) goto L_0x0406
            goto L_0x0408
        L_0x0406:
            r2 = 0
            goto L_0x0414
        L_0x0408:
            io.dcloud.e.b.c r2 = r11.e     // Catch:{ all -> 0x067e }
            r3 = r0[r13]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r3 = (io.dcloud.common.DHInterface.ISysEventListener.SysEventType) r3     // Catch:{ all -> 0x067e }
            r5 = r0[r10]     // Catch:{ all -> 0x067e }
            boolean r2 = r2.a(r1, r3, r5)     // Catch:{ all -> 0x067e }
        L_0x0414:
            if (r2 != 0) goto L_0x043d
            if (r1 == 0) goto L_0x043d
            r3 = r0[r13]     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r3 = (io.dcloud.common.DHInterface.ISysEventListener.SysEventType) r3     // Catch:{ all -> 0x067e }
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r5 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onKeyUp     // Catch:{ all -> 0x067e }
            boolean r3 = r3.equals(r5)     // Catch:{ all -> 0x067e }
            if (r3 == 0) goto L_0x043d
            r3 = r0[r10]     // Catch:{ all -> 0x067e }
            java.lang.Object[] r3 = (java.lang.Object[]) r3     // Catch:{ all -> 0x067e }
            r3 = r3[r13]     // Catch:{ all -> 0x067e }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x067e }
            int r3 = r3.intValue()     // Catch:{ all -> 0x067e }
            r0 = r0[r9]     // Catch:{ all -> 0x067e }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x067e }
            r0 = 4
            if (r3 != r0) goto L_0x043d
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr     // Catch:{ all -> 0x067e }
            r11.processEvent(r0, r4, r1)     // Catch:{ all -> 0x067e }
            goto L_0x043e
        L_0x043d:
            r10 = r2
        L_0x043e:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r10)     // Catch:{ all -> 0x067e }
            goto L_0x01bb
        L_0x0444:
            r0 = r1
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ all -> 0x067e }
            r1 = r0[r13]     // Catch:{ all -> 0x067e }
            r14 = r1
            android.app.Activity r14 = (android.app.Activity) r14     // Catch:{ all -> 0x067e }
            r1 = r0[r10]     // Catch:{ all -> 0x067e }
            java.lang.String r5 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x067e }
            r0 = r0[r9]     // Catch:{ all -> 0x067e }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = "ylyl"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r6.<init>()     // Catch:{ all -> 0x067e }
            java.lang.String r7 = " AppMGr START_APP "
            r6.append(r7)     // Catch:{ all -> 0x067e }
            r6.append(r5)     // Catch:{ all -> 0x067e }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x067e }
            android.util.Log.i(r1, r6)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = "appMgr"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r6.<init>()     // Catch:{ all -> 0x067e }
            java.lang.String r7 = "START_APP"
            r6.append(r7)     // Catch:{ all -> 0x067e }
            r6.append(r5)     // Catch:{ all -> 0x067e }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x067e }
            io.dcloud.common.adapter.util.Logger.e(r1, r6)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r15 = r11.a((android.app.Activity) r14, (java.lang.String) r5)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r15.r0     // Catch:{ all -> 0x067e }
            boolean r16 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.c r1 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r6 = r1.b()     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.c r1 = r11.e     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.e r8 = r1.a((android.app.Activity) r14, (io.dcloud.e.b.e) r15)     // Catch:{ all -> 0x067e }
            if (r8 == 0) goto L_0x04a3
            if (r8 != r15) goto L_0x04a3
            boolean r1 = r15.v     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x067a
        L_0x04a3:
            boolean r1 = r15.q()     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x059b
            if (r8 != 0) goto L_0x04ad
            r1 = 1
            goto L_0x04ae
        L_0x04ad:
            r1 = 0
        L_0x04ae:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r7.<init>()     // Catch:{ all -> 0x067e }
            r7.append(r2)     // Catch:{ all -> 0x067e }
            r7.append(r5)     // Catch:{ all -> 0x067e }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x067e }
            java.lang.String r7 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r14, (java.lang.String) r3, (java.lang.String) r7)     // Catch:{ all -> 0x067e }
            boolean r9 = android.text.TextUtils.isEmpty(r7)     // Catch:{ all -> 0x067e }
            if (r9 != 0) goto L_0x04e2
            java.lang.String r1 = "popped"
            boolean r1 = r7.equals(r1)     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x04e1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x067e }
            r1.<init>()     // Catch:{ all -> 0x067e }
            r1.append(r2)     // Catch:{ all -> 0x067e }
            r1.append(r5)     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x067e }
            io.dcloud.common.adapter.util.SP.removeBundleData(r14, r3, r1)     // Catch:{ all -> 0x067e }
        L_0x04e1:
            r1 = 0
        L_0x04e2:
            boolean r2 = r15.v     // Catch:{ all -> 0x067e }
            if (r2 != 0) goto L_0x04f1
            android.content.Intent r2 = r14.getIntent()     // Catch:{ all -> 0x067e }
            java.lang.String r3 = "__start_first_web__"
            boolean r2 = r2.getBooleanExtra(r3, r13)     // Catch:{ all -> 0x067e }
            goto L_0x04f2
        L_0x04f1:
            r2 = 0
        L_0x04f2:
            boolean r3 = r15.n()     // Catch:{ all -> 0x067e }
            if (r3 != 0) goto L_0x0589
            if (r1 == 0) goto L_0x0589
            if (r2 != 0) goto L_0x0589
            byte r1 = r15.u     // Catch:{ all -> 0x067e }
            if (r1 != r10) goto L_0x0589
            android.app.AlertDialog r1 = r11.g     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x050f
            boolean r1 = r1.isShowing()     // Catch:{ all -> 0x067e }
            if (r1 == 0) goto L_0x050f
            android.app.AlertDialog r1 = r11.g     // Catch:{ all -> 0x067e }
            r1.dismiss()     // Catch:{ all -> 0x067e }
        L_0x050f:
            com.dcloud.android.widget.dialog.DCloudAlertDialog r1 = io.dcloud.common.util.DialogUtil.initDialogTheme(r14, r10)     // Catch:{ all -> 0x067e }
            r11.g = r1     // Catch:{ all -> 0x067e }
            r1.setCanceledOnTouchOutside(r13)     // Catch:{ all -> 0x067e }
            int r1 = io.dcloud.base.R.string.dcloud_common_app_test_tips     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r14.getString(r1)     // Catch:{ all -> 0x067e }
            android.widget.CheckBox r7 = new android.widget.CheckBox     // Catch:{ all -> 0x067e }
            r7.<init>(r14)     // Catch:{ all -> 0x067e }
            int r2 = io.dcloud.base.R.string.dcloud_common_app_trust_tips     // Catch:{ all -> 0x067e }
            r7.setText(r2)     // Catch:{ all -> 0x067e }
            r2 = -65536(0xffffffffffff0000, float:NaN)
            r7.setTextColor(r2)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r2 = r11.g     // Catch:{ all -> 0x067e }
            r2.setMessage(r1)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r1 = r11.g     // Catch:{ all -> 0x067e }
            int r19 = io.dcloud.common.adapter.util.DeviceInfo.getDeivceSuitablePixel(r14, r4)     // Catch:{ all -> 0x067e }
            r20 = 0
            r21 = 0
            r22 = 0
            r17 = r1
            r18 = r7
            r17.setView(r18, r19, r20, r21, r22)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.a$c r13 = new io.dcloud.e.b.a$c     // Catch:{ all -> 0x067e }
            r1 = r13
            r2 = r23
            r3 = r14
            r4 = r5
            r5 = r6
            r6 = r7
            r7 = r0
            r17 = r8
            r8 = r15
            r9 = r17
            r10 = r16
            r1.<init>(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r11.g     // Catch:{ all -> 0x067e }
            r1 = -2
            android.content.res.Resources r2 = r14.getResources()     // Catch:{ all -> 0x067e }
            r3 = 17039360(0x1040000, float:2.424457E-38)
            java.lang.String r2 = r2.getString(r3)     // Catch:{ all -> 0x067e }
            r0.setButton(r1, r2, r13)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r11.g     // Catch:{ all -> 0x067e }
            r1 = -1
            android.content.res.Resources r2 = r14.getResources()     // Catch:{ all -> 0x067e }
            r3 = 17039370(0x104000a, float:2.42446E-38)
            java.lang.String r2 = r2.getString(r3)     // Catch:{ all -> 0x067e }
            r0.setButton(r1, r2, r13)     // Catch:{ all -> 0x067e }
            java.lang.String r0 = "yl"
            java.lang.String r1 = "test show "
            io.dcloud.common.adapter.util.Logger.e(r0, r1)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r11.g     // Catch:{ all -> 0x067e }
            r0.show()     // Catch:{ all -> 0x067e }
            goto L_0x067a
        L_0x0589:
            r17 = r8
            r1 = r23
            r2 = r14
            r3 = r5
            r4 = r0
            r5 = r6
            r6 = r15
            r7 = r17
            r8 = r16
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x067e }
            goto L_0x067a
        L_0x059b:
            r17 = r8
            r1 = r23
            r2 = r14
            r3 = r5
            r4 = r0
            r5 = r6
            r6 = r15
            r7 = r17
            r8 = r16
            r1.a(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x067e }
            boolean r0 = r15.o()     // Catch:{ all -> 0x067e }
            r1 = 17301659(0x108009b, float:2.497969E-38)
            java.lang.String r2 = "HTML5+ Runtime"
            if (r0 == 0) goto L_0x0617
            android.app.AlertDialog r0 = r11.i     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x05c5
            boolean r0 = r0.isShowing()     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x05c5
            android.app.AlertDialog r0 = r11.i     // Catch:{ all -> 0x067e }
            r0.dismiss()     // Catch:{ all -> 0x067e }
        L_0x05c5:
            int r0 = io.dcloud.base.R.string.dcloud_common_app_tips1     // Catch:{ all -> 0x067e }
            java.lang.String r0 = r14.getString(r0)     // Catch:{ all -> 0x067e }
            java.lang.Object[] r3 = new java.lang.Object[r9]     // Catch:{ all -> 0x067e }
            java.lang.String r4 = "appUniVersion"
            java.lang.String r4 = r15.obtainConfigProperty(r4)     // Catch:{ all -> 0x067e }
            r3[r13] = r4     // Catch:{ all -> 0x067e }
            java.lang.String r4 = io.dcloud.common.util.BaseInfo.uniVersionV3     // Catch:{ all -> 0x067e }
            r3[r10] = r4     // Catch:{ all -> 0x067e }
            java.lang.String r0 = io.dcloud.common.util.StringUtil.format(r0, r3)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r3 = new android.app.AlertDialog$Builder     // Catch:{ all -> 0x067e }
            int r4 = io.dcloud.PdrR.FEATURE_LOSS_STYLE     // Catch:{ all -> 0x067e }
            r3.<init>(r14, r4)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r3 = r3.setTitle(r2)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r3 = r3.setIcon(r1)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r3.setMessage(r0)     // Catch:{ all -> 0x067e }
            int r3 = io.dcloud.base.R.string.dcloud_common_view_details     // Catch:{ all -> 0x067e }
            java.lang.String r3 = r14.getString(r3)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.a$d r4 = new io.dcloud.e.b.a$d     // Catch:{ all -> 0x067e }
            r4.<init>(r14)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setPositiveButton(r3, r4)     // Catch:{ all -> 0x067e }
            int r3 = io.dcloud.base.R.string.dcloud_common_ignore     // Catch:{ all -> 0x067e }
            java.lang.String r3 = r14.getString(r3)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setNegativeButton(r3, r12)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r0.create()     // Catch:{ all -> 0x067e }
            r11.i = r0     // Catch:{ all -> 0x067e }
            r0.setCanceledOnTouchOutside(r13)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r11.i     // Catch:{ all -> 0x067e }
            r0.show()     // Catch:{ all -> 0x067e }
        L_0x0617:
            java.lang.String r0 = "io.dcloud.feature.weex.WeexFeature"
            boolean r0 = io.dcloud.common.adapter.util.PlatformUtil.checkClass(r0)     // Catch:{ all -> 0x067e }
            if (r0 != 0) goto L_0x067a
            boolean r0 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r15)     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x067a
            boolean r0 = io.dcloud.common.util.BaseInfo.isWeexUniJs(r15)     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x067a
            android.app.AlertDialog r0 = r11.j     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x063a
            boolean r0 = r0.isShowing()     // Catch:{ all -> 0x067e }
            if (r0 == 0) goto L_0x063a
            android.app.AlertDialog r0 = r11.j     // Catch:{ all -> 0x067e }
            r0.dismiss()     // Catch:{ all -> 0x067e }
        L_0x063a:
            android.app.AlertDialog$Builder r0 = new android.app.AlertDialog$Builder     // Catch:{ all -> 0x067e }
            int r3 = io.dcloud.PdrR.FEATURE_LOSS_STYLE     // Catch:{ all -> 0x067e }
            r0.<init>(r14, r3)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setTitle(r2)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setIcon(r1)     // Catch:{ all -> 0x067e }
            int r1 = io.dcloud.base.R.string.dcloud_common_app_tips2     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r14.getString(r1)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setMessage(r1)     // Catch:{ all -> 0x067e }
            int r1 = io.dcloud.base.R.string.dcloud_common_view_details     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r14.getString(r1)     // Catch:{ all -> 0x067e }
            io.dcloud.e.b.a$e r2 = new io.dcloud.e.b.a$e     // Catch:{ all -> 0x067e }
            r2.<init>(r14)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setPositiveButton(r1, r2)     // Catch:{ all -> 0x067e }
            int r1 = io.dcloud.base.R.string.dcloud_common_ignore     // Catch:{ all -> 0x067e }
            java.lang.String r1 = r14.getString(r1)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog$Builder r0 = r0.setNegativeButton(r1, r12)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r0.create()     // Catch:{ all -> 0x067e }
            r11.j = r0     // Catch:{ all -> 0x067e }
            r0.setCanceledOnTouchOutside(r13)     // Catch:{ all -> 0x067e }
            android.app.AlertDialog r0 = r11.j     // Catch:{ all -> 0x067e }
            r0.show()     // Catch:{ all -> 0x067e }
        L_0x067a:
            r0 = r15
            goto L_0x0686
        L_0x067c:
            r0 = r12
            goto L_0x0686
        L_0x067e:
            r0 = move-exception
            r1 = r12
        L_0x0680:
            java.lang.String r2 = "AppMgr.processEvent"
            io.dcloud.common.adapter.util.Logger.w(r2, r0)
        L_0x0685:
            r0 = r1
        L_0x0686:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.a.processEvent(io.dcloud.common.DHInterface.IMgr$MgrType, int, java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: package-private */
    public void b() {
        g gVar;
        HashMap<String, BaseInfo.BaseAppInfo> hashMap = BaseInfo.mBaseAppInfoSet;
        if (hashMap != null && !hashMap.isEmpty()) {
            Set<String> keySet = BaseInfo.mBaseAppInfoSet.keySet();
            int size = keySet.size();
            String[] strArr = new String[size];
            keySet.toArray(strArr);
            for (int i2 = 0; i2 < size; i2++) {
                String str = strArr[i2];
                BaseInfo.BaseAppInfo baseAppInfo = BaseInfo.mBaseAppInfoSet.get(str);
                if (!BaseInfo.mUnInstalledAppInfoSet.containsKey(str) && !b(str)) {
                    e b2 = b(BaseInfo.sBaseResAppsPath + str, str);
                    if (!(b2 == null || (gVar = b2.s) == null)) {
                        if (!gVar.a) {
                            b2.t = baseAppInfo;
                            c(b2);
                        } else {
                            Logger.e("AppMgr", str + "  app error," + b2.s);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void c(e eVar) {
        this.c.add(eVar.obtainAppId());
        this.d.add(eVar);
    }

    private void a() {
        if (PdrUtil.isEmpty(a)) {
            DataUtil.datToJsString(BaseInfo.sUniNViewServiceJsPath, new C0026a());
        }
    }

    /* access modifiers changed from: package-private */
    public void c() {
        g gVar;
        HashMap<String, BaseInfo.BaseAppInfo> hashMap = BaseInfo.mInstalledAppInfoSet;
        if (hashMap != null && !hashMap.isEmpty()) {
            Set<String> keySet = BaseInfo.mInstalledAppInfoSet.keySet();
            int size = keySet.size();
            String[] strArr = new String[size];
            keySet.toArray(strArr);
            boolean z = false;
            for (int i2 = 0; i2 < size; i2++) {
                String str = strArr[i2];
                if (!BaseInfo.mUnInstalledAppInfoSet.containsKey(str) && !b(str)) {
                    e b2 = b(BaseInfo.sCacheFsAppsPath + str, str);
                    if (b2 == null || (gVar = b2.s) == null || gVar.a) {
                        BaseInfo.mInstalledAppInfoSet.get(str).clearBundleData();
                        BaseInfo.mInstalledAppInfoSet.remove(str);
                        z = true;
                    } else {
                        b2.deleteAppTemp();
                        if (SDK.isUniMPSDK()) {
                            b2.j0 = true;
                        } else {
                            b2.j0 = false;
                        }
                        c(b2);
                    }
                }
            }
            if (z) {
                BaseInfo.saveInstalledAppInfo(getContext());
            }
        }
    }

    private void a(Context context) {
        if (TextUtils.isEmpty(BaseInfo.uniVersionV3)) {
            ThreadPool.self().addThreadTask(new b(context), true);
        }
    }

    /* access modifiers changed from: package-private */
    public void d() {
        File file = new File(BaseInfo.sURDFilePath);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            DHFile.copyAssetsFile("data/dcloud_url.json", file.getAbsolutePath());
        }
        if (file.exists()) {
            try {
                this.h = new JSONObject(new String(DHFile.readAll(file)));
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void a(e eVar, String str, boolean z) {
        if (eVar != null) {
            String obtainConfigProperty = eVar.obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNIAPP_CONTROL);
            f fVar = new f(eVar);
            if (TextUtils.isEmpty(obtainConfigProperty) || !obtainConfigProperty.equals(AbsoluteConst.UNI_V3)) {
                if (!eVar.a(z)) {
                    Logger.e(Logger.AppMgr_TAG, "reboot " + str + " app failed !!!");
                    return;
                }
                fVar.onCallBack(0, (Object) null);
            } else if (eVar.getActivity() != null) {
                BaseInfo.isFirstRun = false;
                eVar.showSplash();
                AppRuntime.restartWeex(eVar.getActivity().getApplication(), new g(eVar, z, str, fVar), eVar.p);
            }
        } else {
            Logger.e(Logger.AppMgr_TAG, "not found " + str + " app!!!");
        }
    }

    /* access modifiers changed from: package-private */
    public void b(e eVar) {
        this.c.remove(eVar.p);
        this.d.remove(eVar);
    }

    private boolean b(String str) {
        return this.c.contains(str);
    }

    private e b(String str, String str2) {
        return a(str, str2);
    }

    private void a(e eVar) {
        if (SDK.isUniMPSDK() && SDK.isEnableBackground) {
            d(eVar);
        }
    }

    public void a(Activity activity, String str, String str2, e eVar, e eVar2, e eVar3, boolean z) {
        String str3 = str;
        e eVar4 = eVar;
        e eVar5 = eVar2;
        e eVar6 = eVar3;
        Log.i("ylyl", "startOneApp " + str);
        BaseInfo.sLastRunApp = str3;
        BaseInfo.CmtInfo cmitInfo = BaseInfo.getCmitInfo(str);
        if (cmitInfo.needUpdate) {
            cmitInfo.templateVersion = eVar5.D;
            cmitInfo.rptCrs = eVar5.M;
            cmitInfo.rptJse = eVar5.N;
            cmitInfo.plusLauncher = BaseInfo.getLaunchType(eVar2.obtainWebAppIntent());
            cmitInfo.sfd = DataInterface.getStreamappFrom(eVar2.obtainWebAppIntent());
            cmitInfo.needUpdate = false;
        }
        if (io.dcloud.e.c.g.c() || eVar5.u != 4) {
            if (eVar5.u == 3) {
                eVar5.u = eVar2.p() ? eVar5.u : 2;
            }
            if (!(eVar4 == null || eVar4 == eVar5 || eVar4 == eVar6)) {
                eVar.w();
            }
            byte b2 = eVar5.u;
            if (b2 == 1 || ((z && !eVar5.w) || ((eVar5.x && eVar5.v) || !z))) {
                Logger.d(Logger.AppMgr_TAG, str + " will unrunning change to active");
                Activity activity2 = activity;
                eVar5.a(activity);
                processEvent(IMgr.MgrType.WindowMgr, 4, new Object[]{eVar5, str3});
                eVar5.a((ICallBack) new h(eVar2, str, str2, z));
            } else if (b2 == 2) {
                Logger.d(Logger.AppMgr_TAG, str + " will unactive change to active");
                eVar2.c();
            } else {
                Logger.d(Logger.AppMgr_TAG, str + " is active");
            }
            if (SDK.isUniMPSDK() && Build.VERSION.SDK_INT >= 21) {
                a(eVar5);
            }
            if (eVar6 != null && eVar6 != eVar5) {
                eVar3.u();
                return;
            }
            return;
        }
        ErrorDialogUtil.checkAppKeyErrorTips(activity);
    }

    /* access modifiers changed from: private */
    public void a(e eVar, String str, String str2, boolean z) {
        if (this.e != null) {
            boolean f2 = z ? eVar.f(str2) : eVar.g(str2);
            if (!eVar.w && eVar.v) {
                eVar.f(str2);
            }
            if (f2) {
                this.e.a(str, eVar);
                return;
            }
            Logger.e(Logger.AppMgr_TAG, str + " run failed!!!");
        }
    }

    /* access modifiers changed from: package-private */
    public e a(Activity activity, String str) {
        return a(activity, str, true);
    }

    private e a(String str, boolean z) {
        return a((Activity) null, str, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r0 = r3.c.indexOf(r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private io.dcloud.e.b.e a(android.app.Activity r4, java.lang.String r5, boolean r6) {
        /*
            r3 = this;
            java.util.ArrayList<java.lang.String> r0 = r3.c
            boolean r0 = r0.contains(r5)
            r1 = 0
            if (r0 == 0) goto L_0x001a
            java.util.ArrayList<java.lang.String> r0 = r3.c
            int r0 = r0.indexOf(r5)
            if (r0 < 0) goto L_0x001a
            java.util.ArrayList<io.dcloud.e.b.e> r2 = r3.d
            java.lang.Object r0 = r2.get(r0)
            io.dcloud.e.b.e r0 = (io.dcloud.e.b.e) r0
            goto L_0x001b
        L_0x001a:
            r0 = r1
        L_0x001b:
            if (r0 != 0) goto L_0x0061
            if (r6 == 0) goto L_0x0061
            io.dcloud.e.b.e r0 = new io.dcloud.e.b.e
            r6 = 0
            r0.<init>(r3, r5, r6)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath
            r6.append(r2)
            r6.append(r5)
            char r2 = io.dcloud.common.adapter.util.DeviceInfo.sSeparatorChar
            r6.append(r2)
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_WWW_DIR
            r6.append(r2)
            java.lang.String r6 = r6.toString()
            r0.setAppDataPath(r6)
            android.app.Activity r6 = r0.a
            if (r6 != 0) goto L_0x0049
            r0.a = r4
        L_0x0049:
            if (r4 == 0) goto L_0x0052
            android.content.Intent r4 = r4.getIntent()
            r0.setWebAppIntent(r4)
        L_0x0052:
            r0.b(r5, r1)
            io.dcloud.e.b.g r4 = r0.s
            boolean r4 = r4.a
            if (r4 == 0) goto L_0x005d
            r0.p = r5
        L_0x005d:
            r3.c((io.dcloud.e.b.e) r0)
            goto L_0x0093
        L_0x0061:
            if (r0 == 0) goto L_0x0093
            if (r4 == 0) goto L_0x0093
            android.app.Activity r6 = r0.a
            if (r6 != 0) goto L_0x006b
            r0.a = r4
        L_0x006b:
            android.app.Activity r4 = r0.a
            android.content.Intent r4 = r4.getIntent()
            if (r4 == 0) goto L_0x0083
            boolean r4 = r0.manifestBeParsed()
            if (r4 == 0) goto L_0x008c
            android.app.Activity r4 = r0.a
            android.content.Intent r4 = r4.getIntent()
            r0.setWebAppIntent(r4)
            goto L_0x008c
        L_0x0083:
            android.app.Activity r4 = r0.a
            android.content.Intent r6 = r0.obtainWebAppIntent()
            r4.setIntent(r6)
        L_0x008c:
            boolean r4 = r0.v
            if (r4 != 0) goto L_0x0093
            r0.b(r5, r1)
        L_0x0093:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.a.a(android.app.Activity, java.lang.String, boolean):io.dcloud.e.b.e");
    }

    /* access modifiers changed from: package-private */
    public e a(String str, String str2, String str3, byte b2) {
        e a2 = a(str, false);
        if (a2 == null) {
            a2 = new e(this, str, b2);
            a2.u = 3;
            a2.p = str;
            if (!PdrUtil.isEmpty(str2)) {
                a2.setAppDataPath(str2);
            }
            a2.l0 = str3;
            c(a2);
            this.e.a(str, a2);
        }
        return a2;
    }

    /* access modifiers changed from: package-private */
    public e a(String str, String str2) {
        return a(str, str2, (JSONObject) null);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:48:0x00da=Splitter:B:48:0x00da, B:71:0x014e=Splitter:B:71:0x014e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.dcloud.e.b.e a(java.lang.String r10, java.lang.String r11, org.json.JSONObject r12) {
        /*
            r9 = this;
            r0 = 0
            io.dcloud.e.b.e r1 = r9.a((java.lang.String) r11, (boolean) r0)
            if (r1 == 0) goto L_0x000c
            io.dcloud.e.b.g r2 = r1.s     // Catch:{ Exception -> 0x0061 }
            r2.a()     // Catch:{ Exception -> 0x0061 }
        L_0x000c:
            boolean r2 = io.dcloud.common.adapter.io.DHFile.isExist((java.lang.String) r10)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r3 = ".wgt"
            r4 = 0
            r5 = 1
            if (r2 != 0) goto L_0x0064
            boolean r2 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r10)     // Catch:{ Exception -> 0x0061 }
            if (r2 == 0) goto L_0x001d
            goto L_0x0064
        L_0x001d:
            r0 = 47
            int r0 = r10.lastIndexOf(r0)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r0 = r10.substring(r0)     // Catch:{ Exception -> 0x0061 }
            boolean r0 = r0.contains(r3)     // Catch:{ Exception -> 0x0061 }
            if (r0 == 0) goto L_0x0031
            java.io.InputStream r4 = io.dcloud.common.adapter.util.PlatformUtil.getResInputStream(r10)     // Catch:{ Exception -> 0x0061 }
        L_0x0031:
            if (r1 != 0) goto L_0x0039
            io.dcloud.e.b.e r2 = new io.dcloud.e.b.e     // Catch:{ Exception -> 0x0061 }
            r2.<init>(r9, r11, r5)     // Catch:{ Exception -> 0x0061 }
            r1 = r2
        L_0x0039:
            if (r0 != 0) goto L_0x005c
            if (r4 == 0) goto L_0x003e
            goto L_0x005c
        L_0x003e:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0061 }
            r0.<init>()     // Catch:{ Exception -> 0x0061 }
            r0.append(r10)     // Catch:{ Exception -> 0x0061 }
            char r2 = io.dcloud.common.adapter.util.DeviceInfo.sSeparatorChar     // Catch:{ Exception -> 0x0061 }
            r0.append(r2)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_WWW_DIR     // Catch:{ Exception -> 0x0061 }
            r0.append(r2)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0061 }
            r1.setAppDataPath(r0)     // Catch:{ Exception -> 0x0061 }
            r1.b(r11, r12)     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x005c:
            r1.b((java.io.InputStream) r4)     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x0061:
            r11 = move-exception
            goto L_0x01ac
        L_0x0064:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0061 }
            r2.<init>(r10)     // Catch:{ Exception -> 0x0061 }
            boolean r2 = r2.isFile()     // Catch:{ Exception -> 0x0061 }
            if (r2 == 0) goto L_0x0180
            if (r2 == 0) goto L_0x0096
            java.util.Locale r6 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x0061 }
            java.lang.String r6 = r10.toLowerCase(r6)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r7 = ".wgtu"
            boolean r6 = r6.endsWith(r7)     // Catch:{ Exception -> 0x0061 }
            if (r6 == 0) goto L_0x0096
            if (r1 != 0) goto L_0x0082
            goto L_0x0083
        L_0x0082:
            r5 = 0
        L_0x0083:
            if (r5 == 0) goto L_0x008b
            io.dcloud.e.b.e r2 = new io.dcloud.e.b.e     // Catch:{ Exception -> 0x0061 }
            r2.<init>(r9, r11, r0)     // Catch:{ Exception -> 0x0061 }
            r1 = r2
        L_0x008b:
            r1.a((java.lang.String) r10, (org.json.JSONObject) r12)     // Catch:{ Exception -> 0x0061 }
            io.dcloud.e.b.g r11 = r1.s     // Catch:{ Exception -> 0x0061 }
            r11.c = r0     // Catch:{ Exception -> 0x0061 }
            r11.d = r0     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x0096:
            if (r2 == 0) goto L_0x00eb
            java.util.Locale r6 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x0061 }
            java.lang.String r6 = r10.toLowerCase(r6)     // Catch:{ Exception -> 0x0061 }
            boolean r3 = r6.endsWith(r3)     // Catch:{ Exception -> 0x0061 }
            if (r3 == 0) goto L_0x00eb
            if (r1 != 0) goto L_0x00a8
            r2 = 1
            goto L_0x00a9
        L_0x00a8:
            r2 = 0
        L_0x00a9:
            io.dcloud.e.b.g r3 = r1.s     // Catch:{ Exception -> 0x0061 }
            r3.d = r5     // Catch:{ Exception -> 0x0061 }
            if (r2 == 0) goto L_0x00da
            io.dcloud.e.b.e r3 = new io.dcloud.e.b.e     // Catch:{ Exception -> 0x0061 }
            r3.<init>(r9, r11, r0)     // Catch:{ Exception -> 0x0061 }
            r3.p = r11     // Catch:{ Exception -> 0x00d6 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d6 }
            r1.<init>()     // Catch:{ Exception -> 0x00d6 }
            java.lang.String r5 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath     // Catch:{ Exception -> 0x00d6 }
            r1.append(r5)     // Catch:{ Exception -> 0x00d6 }
            r1.append(r11)     // Catch:{ Exception -> 0x00d6 }
            char r11 = io.dcloud.common.adapter.util.DeviceInfo.sSeparatorChar     // Catch:{ Exception -> 0x00d6 }
            r1.append(r11)     // Catch:{ Exception -> 0x00d6 }
            java.lang.String r11 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_WWW_DIR     // Catch:{ Exception -> 0x00d6 }
            r1.append(r11)     // Catch:{ Exception -> 0x00d6 }
            java.lang.String r11 = r1.toString()     // Catch:{ Exception -> 0x00d6 }
            r3.setAppDataPath(r11)     // Catch:{ Exception -> 0x00d6 }
            r1 = r3
            goto L_0x00da
        L_0x00d6:
            r11 = move-exception
            r1 = r3
            goto L_0x01ac
        L_0x00da:
            boolean r11 = r1.c(r10, r12)     // Catch:{ Exception -> 0x0061 }
            io.dcloud.e.b.g r12 = r1.s     // Catch:{ Exception -> 0x0061 }
            r12.d = r0     // Catch:{ Exception -> 0x0061 }
            if (r11 == 0) goto L_0x01a8
            if (r2 == 0) goto L_0x01a8
            r9.c((io.dcloud.e.b.e) r1)     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x00eb:
            java.lang.String r11 = "{code:%d,message:'%s'}"
            r12 = 2
            if (r2 == 0) goto L_0x0167
            java.util.Locale r2 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x0061 }
            java.lang.String r2 = r10.toLowerCase(r2)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r3 = ".apk"
            boolean r2 = r2.endsWith(r3)     // Catch:{ Exception -> 0x0061 }
            if (r2 == 0) goto L_0x0167
            android.content.Context r2 = r9.getContext()     // Catch:{ Exception -> 0x0108 }
            android.content.pm.PackageInfo r11 = io.dcloud.common.adapter.util.PlatformUtil.parseApkInfo(r2, r10)     // Catch:{ Exception -> 0x0108 }
            goto L_0x0125
        L_0x0108:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ Exception -> 0x0061 }
            io.dcloud.e.b.g r3 = r1.s     // Catch:{ Exception -> 0x0061 }
            java.lang.Object[] r6 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x0061 }
            r7 = 10
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Exception -> 0x0061 }
            r6[r0] = r7     // Catch:{ Exception -> 0x0061 }
            java.lang.String r2 = r2.getMessage()     // Catch:{ Exception -> 0x0061 }
            r6[r5] = r2     // Catch:{ Exception -> 0x0061 }
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r11, r6)     // Catch:{ Exception -> 0x0061 }
            r3.b = r11     // Catch:{ Exception -> 0x0061 }
            r11 = r4
        L_0x0125:
            if (r11 != 0) goto L_0x012d
            io.dcloud.e.b.g r11 = r1.s     // Catch:{ Exception -> 0x0061 }
            r11.a = r5     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x012d:
            io.dcloud.e.b.g r2 = r1.s     // Catch:{ Exception -> 0x0061 }
            r2.a = r0     // Catch:{ Exception -> 0x0061 }
            java.lang.String r2 = "{pname:'%s',version:'%s',name:'%s'}"
            java.lang.String r3 = r11.versionName     // Catch:{ Exception -> 0x0061 }
            java.lang.String r6 = r11.packageName     // Catch:{ Exception -> 0x0061 }
            android.content.Context r7 = r9.getContext()     // Catch:{ Exception -> 0x0061 }
            android.content.pm.PackageManager r7 = r7.getPackageManager()     // Catch:{ Exception -> 0x0061 }
            android.content.pm.ApplicationInfo r11 = r11.applicationInfo     // Catch:{ Exception -> 0x0061 }
            java.lang.CharSequence r11 = r7.getApplicationLabel(r11)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x0061 }
            if (r11 != 0) goto L_0x014e
            java.lang.String r11 = ""
        L_0x014e:
            io.dcloud.e.b.g r7 = r1.s     // Catch:{ Exception -> 0x0061 }
            r8 = 3
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0061 }
            r8[r0] = r6     // Catch:{ Exception -> 0x0061 }
            r8[r5] = r3     // Catch:{ Exception -> 0x0061 }
            r8[r12] = r11     // Catch:{ Exception -> 0x0061 }
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r2, r8)     // Catch:{ Exception -> 0x0061 }
            r7.b = r11     // Catch:{ Exception -> 0x0061 }
            android.content.Context r11 = r9.getContext()     // Catch:{ Exception -> 0x0061 }
            io.dcloud.common.adapter.util.PlatformUtil.openFileBySystem(r11, r10, r4, r4, r4)     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x0167:
            io.dcloud.e.b.g r2 = r1.s     // Catch:{ Exception -> 0x0061 }
            r2.a = r5     // Catch:{ Exception -> 0x0061 }
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x0061 }
            r3 = -1201(0xfffffffffffffb4f, float:NaN)
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x0061 }
            r12[r0] = r3     // Catch:{ Exception -> 0x0061 }
            java.lang.String r0 = io.dcloud.common.constant.DOMException.MSG_RUNTIME_WGT_OR_WGTU_ERROR_MALFORMED     // Catch:{ Exception -> 0x0061 }
            r12[r5] = r0     // Catch:{ Exception -> 0x0061 }
            java.lang.String r11 = io.dcloud.common.util.StringUtil.format(r11, r12)     // Catch:{ Exception -> 0x0061 }
            r2.b = r11     // Catch:{ Exception -> 0x0061 }
            goto L_0x01a8
        L_0x0180:
            if (r1 == 0) goto L_0x0186
            r1.b((byte) r0)     // Catch:{ Exception -> 0x0061 }
            goto L_0x018c
        L_0x0186:
            io.dcloud.e.b.e r2 = new io.dcloud.e.b.e     // Catch:{ Exception -> 0x0061 }
            r2.<init>(r9, r11, r0)     // Catch:{ Exception -> 0x0061 }
            r1 = r2
        L_0x018c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0061 }
            r0.<init>()     // Catch:{ Exception -> 0x0061 }
            r0.append(r10)     // Catch:{ Exception -> 0x0061 }
            char r2 = io.dcloud.common.adapter.util.DeviceInfo.sSeparatorChar     // Catch:{ Exception -> 0x0061 }
            r0.append(r2)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.REAL_PRIVATE_WWW_DIR     // Catch:{ Exception -> 0x0061 }
            r0.append(r2)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0061 }
            r1.setAppDataPath(r0)     // Catch:{ Exception -> 0x0061 }
            r1.b(r11, r12)     // Catch:{ Exception -> 0x0061 }
        L_0x01a8:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r4)     // Catch:{ Exception -> 0x0061 }
            goto L_0x01ca
        L_0x01ac:
            r11.printStackTrace()
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "installWebApp "
            r11.append(r12)
            r11.append(r10)
            java.lang.String r10 = " is Illegal path"
            r11.append(r10)
            java.lang.String r10 = r11.toString()
            java.lang.String r11 = "appmgr"
            io.dcloud.common.adapter.util.Logger.e(r11, r10)
        L_0x01ca:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.b.a.a(java.lang.String, java.lang.String, org.json.JSONObject):io.dcloud.e.b.e");
    }
}
