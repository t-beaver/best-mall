package io.dcloud.e.c;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import java.io.File;

public class d {
    Context a = null;
    c b = null;
    private long c = 0;

    public d(Context context, ICore.ICoreStatusListener iCoreStatusListener) {
        this.a = context;
        this.b = c.a(context, iCoreStatusListener);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
        r5 = r4.getExtras();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(android.app.Activity r3, android.os.Bundle r4, io.dcloud.feature.internal.sdk.SDK.IntegratedMode r5, io.dcloud.common.DHInterface.IOnCreateSplashView r6) {
        /*
            r2 = this;
            r0 = 0
            if (r6 == 0) goto L_0x000e
            io.dcloud.feature.internal.sdk.SDK$IntegratedMode r1 = io.dcloud.feature.internal.sdk.SDK.IntegratedMode.WEBAPP
            if (r5 == r1) goto L_0x000e
            io.dcloud.feature.internal.sdk.SDK$IntegratedMode r1 = io.dcloud.feature.internal.sdk.SDK.IntegratedMode.WEBVIEW
            if (r5 == r1) goto L_0x000e
            r6.onCreateSplash(r0)
        L_0x000e:
            io.dcloud.e.c.c r6 = r2.b
            r6.a((android.app.Activity) r3, (android.os.Bundle) r4, (io.dcloud.feature.internal.sdk.SDK.IntegratedMode) r5)
            android.content.Intent r4 = r3.getIntent()
            if (r4 == 0) goto L_0x0026
            android.os.Bundle r5 = r4.getExtras()
            if (r5 == 0) goto L_0x0026
            java.lang.String r6 = "appid"
            java.lang.String r5 = r5.getString(r6)
            goto L_0x0027
        L_0x0026:
            r5 = r0
        L_0x0027:
            boolean r6 = io.dcloud.common.util.PdrUtil.isEmpty(r5)
            if (r6 == 0) goto L_0x002f
            java.lang.String r5 = io.dcloud.common.util.BaseInfo.sDefaultBootApp
        L_0x002f:
            boolean r6 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()
            if (r6 == 0) goto L_0x0039
            r2.a((android.app.Activity) r3, (android.content.Intent) r0, (io.dcloud.common.DHInterface.IOnCreateSplashView) r0, (java.lang.String) r5)
            goto L_0x004a
        L_0x0039:
            boolean r4 = r2.a((android.content.Intent) r4, (java.lang.String) r5)
            if (r4 == 0) goto L_0x004a
            io.dcloud.feature.internal.sdk.SDK$IntegratedMode r4 = io.dcloud.common.util.BaseInfo.sRuntimeMode
            if (r4 != 0) goto L_0x004a
            android.content.Intent r4 = r3.getIntent()
            r2.a((android.app.Activity) r3, (android.content.Intent) r4, (io.dcloud.common.DHInterface.IOnCreateSplashView) r0, (java.lang.String) r5)
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.d.a(android.app.Activity, android.os.Bundle, io.dcloud.feature.internal.sdk.SDK$IntegratedMode, io.dcloud.common.DHInterface.IOnCreateSplashView):void");
    }

    public void b(Activity activity) {
        this.c = System.currentTimeMillis();
        Logger.i("onResume resumeTime=" + this.c);
        this.b.c(activity);
    }

    public boolean c(Activity activity) {
        Logger.i("onStop");
        if (!this.b.d(activity)) {
            return false;
        }
        this.b = null;
        return true;
    }

    public Context b() {
        return this.a;
    }

    /* access modifiers changed from: package-private */
    public boolean a(Intent intent, String str) {
        boolean booleanExtra = intent.getBooleanExtra(IntentConst.WEBAPP_ACTIVITY_HAS_STREAM_SPLASH, false);
        if (booleanExtra) {
            if (TextUtils.isEmpty(str)) {
                str = intent.getStringExtra("appid");
            }
            String str2 = BaseInfo.sCacheFsAppsPath + str + "/www/";
            if (new File(str2).exists()) {
                File file = new File(str2 + "/manifest.json");
                if (file.exists() && file.length() > 0) {
                    return true;
                }
            }
            if (intent.hasExtra(IntentConst.DIRECT_PAGE) && BaseInfo.isWap2AppAppid(str)) {
                return true;
            }
        }
        return !booleanExtra;
    }

    public IApp a(Activity activity, Intent intent, IOnCreateSplashView iOnCreateSplashView, String str) {
        String obtainArgs = IntentConst.obtainArgs(intent, str);
        Logger.i("onStart appid=" + str + ";intentArgs=" + obtainArgs);
        if (iOnCreateSplashView != null) {
            return this.b.a(activity, str, obtainArgs, iOnCreateSplashView);
        }
        this.b.a(activity, str, obtainArgs);
        return null;
    }

    public boolean a(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj) {
        if (!a(activity.getIntent(), (String) null) && !"all".equalsIgnoreCase(BaseInfo.sSplashExitCondition)) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.c;
        if (currentTimeMillis - j > 500) {
            return this.b.onActivityExecute(activity, sysEventType, obj);
        }
        if (j <= 0 || sysEventType != ISysEventListener.SysEventType.onKeyUp) {
            return false;
        }
        return true;
    }

    public void a(Activity activity) {
        Logger.i("onPause");
        this.b.b(activity);
        this.c = 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
        r1 = r10.getExtras();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(android.app.Activity r9, android.content.Intent r10) {
        /*
            r8 = this;
            r0 = 0
            if (r10 == 0) goto L_0x0011
            android.os.Bundle r1 = r10.getExtras()
            if (r1 == 0) goto L_0x0011
            java.lang.String r2 = "appid"
            java.lang.String r1 = r1.getString(r2)
            r4 = r1
            goto L_0x0012
        L_0x0011:
            r4 = r0
        L_0x0012:
            java.lang.String r5 = io.dcloud.common.constant.IntentConst.obtainArgs(r10, r4)
            boolean r1 = io.dcloud.common.util.PdrUtil.isEmpty(r4)
            if (r1 != 0) goto L_0x0032
            io.dcloud.e.c.c r2 = r8.b
            boolean r1 = r9 instanceof io.dcloud.common.DHInterface.IOnCreateSplashView
            if (r1 == 0) goto L_0x0025
            r0 = r9
            io.dcloud.common.DHInterface.IOnCreateSplashView r0 = (io.dcloud.common.DHInterface.IOnCreateSplashView) r0
        L_0x0025:
            r6 = r0
            r0 = 1
            java.lang.String r1 = "exec_new_intent"
            boolean r7 = r10.getBooleanExtra(r1, r0)
            r3 = r9
            r2.a(r3, r4, r5, r6, r7)
            goto L_0x0039
        L_0x0032:
            io.dcloud.e.c.c r10 = r8.b
            io.dcloud.common.DHInterface.ISysEventListener$SysEventType r0 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onNewIntent
            r10.onActivityExecute(r9, r0, r5)
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.d.a(android.app.Activity, android.content.Intent):void");
    }

    public void a(Activity activity, int i) {
        Logger.i(Logger.LAYOUT_TAG, "onConfigurationChanged pConfig=" + i);
        this.b.onActivityExecute(activity, ISysEventListener.SysEventType.onConfigurationChanged, 1);
    }

    @Deprecated
    public ICore a() {
        return this.b;
    }
}
