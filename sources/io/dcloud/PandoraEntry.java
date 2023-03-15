package io.dcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PandoraEntry extends Activity {

    class a implements Runnable {
        a() {
        }

        public void run() {
            PandoraEntry.this.finish();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x007c A[Catch:{ Exception -> 0x0096 }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x007d A[Catch:{ Exception -> 0x0096 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(android.content.Intent r7) {
        /*
            r6 = this;
            boolean r0 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0096 }
            r1.<init>()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sDefaultBootApp     // Catch:{ Exception -> 0x0096 }
            r1.append(r2)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r2 = "/www/manifest.json"
            r1.append(r2)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0096 }
            r2 = 0
            if (r0 == 0) goto L_0x0049
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0096 }
            r0.<init>()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sCacheFsAppsPath     // Catch:{ Exception -> 0x0096 }
            r0.append(r2)     // Catch:{ Exception -> 0x0096 }
            r0.append(r1)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0096 }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x0096 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0096 }
            boolean r0 = r2.exists()     // Catch:{ Exception -> 0x0096 }
            if (r0 == 0) goto L_0x003c
            java.io.InputStream r0 = io.dcloud.common.adapter.io.DHFile.getInputStream(r2)     // Catch:{ Exception -> 0x0096 }
        L_0x003a:
            r2 = r0
            goto L_0x007a
        L_0x003c:
            android.content.res.Resources r0 = r6.getResources()     // Catch:{ Exception -> 0x0096 }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x0096 }
            java.io.InputStream r0 = r0.open(r1)     // Catch:{ Exception -> 0x0096 }
            goto L_0x003a
        L_0x0049:
            java.lang.String r0 = android.os.Environment.DIRECTORY_PICTURES     // Catch:{ Exception -> 0x0096 }
            java.io.File r0 = r6.getExternalFilesDir(r0)     // Catch:{ Exception -> 0x0096 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0096 }
            r3.<init>()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x0096 }
            java.lang.String r4 = "files/Pictures"
            java.lang.String r5 = "apps/"
            java.lang.String r0 = r0.replace(r4, r5)     // Catch:{ Exception -> 0x0096 }
            r3.append(r0)     // Catch:{ Exception -> 0x0096 }
            r3.append(r1)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x0096 }
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0096 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0096 }
            boolean r0 = r1.exists()     // Catch:{ Exception -> 0x0096 }
            if (r0 == 0) goto L_0x007a
            java.io.InputStream r0 = io.dcloud.common.adapter.io.DHFile.getInputStream(r1)     // Catch:{ Exception -> 0x0096 }
            goto L_0x003a
        L_0x007a:
            if (r2 != 0) goto L_0x007d
            return
        L_0x007d:
            java.lang.String r0 = io.dcloud.common.util.IOUtil.toString(r2)     // Catch:{ Exception -> 0x0096 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0096 }
            if (r1 == 0) goto L_0x0088
            return
        L_0x0088:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0096 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0096 }
            int r0 = io.dcloud.common.util.PdrUtil.getConfigOrientation(r1)     // Catch:{ Exception -> 0x0096 }
            java.lang.String r1 = "__intetn_orientation__"
            r7.putExtra(r1, r0)     // Catch:{ Exception -> 0x0096 }
        L_0x0096:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.PandoraEntry.a(android.content.Intent):void");
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            super.attachBaseContext(context);
        } else {
            super.attachBaseContext(LanguageUtil.updateContextLanguageAfterO(context, false));
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        if (Build.VERSION.SDK_INT == 26 && a()) {
            a((Activity) this);
        }
        super.onCreate(bundle);
        Intent intent = getIntent();
        AndroidResources.initAndroidResources(getApplication());
        BaseInfo.parseControl();
        if (SDK.isUniMPSDK() && intent.hasExtra("appid")) {
            BaseInfo.sDefaultBootApp = intent.getStringExtra("appid");
        }
        if (SDK.isUniMPSDK()) {
            a(intent);
        } else if (BaseInfo.SyncDebug) {
            a(intent);
        }
        if (intent.hasExtra(IntentConst.START_FROM_TO_CLASS)) {
            intent.setClassName(getPackageName(), intent.getStringExtra(IntentConst.START_FROM_TO_CLASS));
            intent.removeExtra(IntentConst.START_FROM_TO_CLASS);
        } else {
            intent.putExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME, PandoraEntry.class.getName());
            intent.setClass(this, PandoraEntryActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(0, 0);
        if (SDK.isUniMPSDK()) {
            finish();
        } else {
            new Handler().postDelayed(new a(), 20);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    private static void a(Activity activity) {
        try {
            Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
            declaredField.setAccessible(true);
            ((ActivityInfo) declaredField.get(activity)).screenOrientation = -1;
        } catch (Exception unused) {
        }
    }

    private boolean a() {
        boolean z = false;
        try {
            TypedArray obtainStyledAttributes = obtainStyledAttributes((int[]) Class.forName("com.android.internal.R$styleable").getField(AbsoluteConst.FEATURE_WINDOW).get((Object) null));
            Method method = ActivityInfo.class.getMethod("isTranslucentOrFloating", new Class[]{TypedArray.class});
            method.setAccessible(true);
            boolean booleanValue = ((Boolean) method.invoke((Object) null, new Object[]{obtainStyledAttributes})).booleanValue();
            try {
                method.setAccessible(false);
                return booleanValue;
            } catch (Exception unused) {
                z = booleanValue;
            }
        } catch (Exception unused2) {
            return z;
        }
    }
}
