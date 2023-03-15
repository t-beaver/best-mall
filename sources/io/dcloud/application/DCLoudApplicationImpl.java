package io.dcloud.application;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.webkit.WebView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.dcloud.a;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.INativeAppInfo;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NativeCrashManager;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.e.b.b;
import io.dcloud.feature.internal.sdk.SDK;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DCLoudApplicationImpl {
    private static DCLoudApplicationImpl mInstance;
    private String Tag = "DCLoudApplicationImpl";
    private ActivityCallbacks activityCallbacks;
    private boolean isInit = false;
    boolean isUniMP = false;
    private Context mApplication;
    public ConcurrentHashMap<String, WeakReference<Activity>> topActiveMap = new ConcurrentHashMap<>();

    private class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {
        private int activityStartCount;
        private volatile boolean isBack;
        private boolean isStop;
        private List<ActivityStatusListener> listeners;

        private ActivityCallbacks() {
            this.activityStartCount = 0;
            this.isBack = false;
            this.isStop = false;
        }

        public void addListener(ActivityStatusListener activityStatusListener) {
            if (this.listeners == null) {
                this.listeners = new ArrayList();
            }
            this.listeners.add(activityStatusListener);
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
            try {
                DCLoudApplicationImpl.this.topActiveMap.remove(activity.getComponentName().getClassName());
            } catch (Exception unused) {
            }
        }

        public void onActivityResumed(Activity activity) {
            try {
                DCLoudApplicationImpl.this.topActiveMap.put(activity.getComponentName().getClassName(), new WeakReference(activity));
            } catch (Exception unused) {
            }
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
            if (this.isStop) {
                this.isStop = false;
            } else if (this.activityStartCount == 0 && this.isBack) {
                this.isBack = false;
                List<ActivityStatusListener> list = this.listeners;
                if (list != null && !list.isEmpty()) {
                    for (ActivityStatusListener next : this.listeners) {
                        if (next != null) {
                            next.onFront();
                        }
                    }
                }
            }
            this.activityStartCount++;
        }

        public void onActivityStopped(Activity activity) {
            this.activityStartCount--;
        }

        public void onApp2Back() {
            if (!this.isStop) {
                if (!this.isBack) {
                    Logger.d("DCloud_uniAd", "app is in back");
                    List<ActivityStatusListener> list = this.listeners;
                    if (list != null && !list.isEmpty()) {
                        for (ActivityStatusListener next : this.listeners) {
                            if (next != null) {
                                next.onBack();
                            }
                        }
                    }
                }
                this.isBack = true;
            }
        }

        public void removeListener(ActivityStatusListener activityStatusListener) {
            List<ActivityStatusListener> list = this.listeners;
            if (list != null) {
                list.remove(activityStatusListener);
            }
        }

        public void stopListener() {
            this.isStop = true;
        }
    }

    public interface ActivityStatusListener {
        void onBack();

        void onFront();
    }

    public class DynamicLanguageReceiver extends BroadcastReceiver {
        public DynamicLanguageReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (context != null) {
                Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                launchIntentForPackage.setFlags(268468224);
                context.startActivity(launchIntentForPackage);
                Process.killProcess(Process.myPid());
            }
        }
    }

    private INativeAppInfo getNativeInfo(Application application) {
        return new b(application);
    }

    private void initLanguageConfig(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LanguageUtil.LanguageBroadCastIntent);
        LocalBroadcastManager.getInstance(self().getContext()).registerReceiver(new DynamicLanguageReceiver(), intentFilter);
        if (Build.VERSION.SDK_INT < 26) {
            LanguageUtil.initAppLanguageForAppBeforeO(context);
        }
    }

    private void initX5(Application application, boolean z) {
        if (z) {
            AppRuntime.initX5(application, BaseInfo.allowDownloadWithoutWiFi, (ICallBack) null);
        }
    }

    public static DCLoudApplicationImpl self() {
        if (mInstance == null) {
            mInstance = new DCLoudApplicationImpl();
        }
        return mInstance;
    }

    public void addActivityStatusListener(ActivityStatusListener activityStatusListener) {
        ActivityCallbacks activityCallbacks2 = this.activityCallbacks;
        if (activityCallbacks2 != null) {
            activityCallbacks2.addListener(activityStatusListener);
        }
    }

    /* access modifiers changed from: protected */
    public Context attachBaseContext(Context context) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            context = LanguageUtil.updateContextLanguageAfterO(context, true, false);
        }
        if (i < 21) {
            supportMultiDex(context);
        }
        if (!SDK.isUniMPSDK()) {
            a.a(context);
        }
        return context;
    }

    public Context getContext() {
        return this.mApplication;
    }

    public void init(Application application, boolean z) {
        this.isUniMP = z;
        if (z && Build.VERSION.SDK_INT >= 26) {
            LanguageUtil.updateContextLanguageAfterO(application, true);
        }
        SDK.isUniMP = this.isUniMP;
        onCreate(application);
        if (BaseInfo.isBase(application) && this.isInit) {
            webviewSetPath(application, true);
        }
        if (z) {
            DeviceInfo.initGsmCdmaCell();
            TelephonyUtil.updateIMEI(application);
        }
    }

    public boolean isInit() {
        return this.isInit;
    }

    public boolean isMainProcess(Context context, boolean z) {
        if (!z) {
            return true;
        }
        boolean equals = context.getPackageName().equals(RuningAcitvityUtil.getAppName(context));
        if (!equals && !this.isUniMP) {
            String appName = RuningAcitvityUtil.getAppName(context);
            if (appName.startsWith(context.getPackageName() + ":unimp")) {
                this.isUniMP = true;
                SDK.isUniMP = true;
            }
        }
        return equals;
    }

    public void onApp2Back() {
        ActivityCallbacks activityCallbacks2 = this.activityCallbacks;
        if (activityCallbacks2 != null) {
            activityCallbacks2.onApp2Back();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Application application) {
        if (!this.isInit) {
            try {
                NativeCrashManager.initNativeCrash(application);
            } catch (Exception unused) {
            }
            AndroidResources.initAndroidResources(application.getBaseContext());
            BaseInfo.parseControl();
            DeviceInfo.initPath(application, false);
            boolean z = !AppRuntime.hasPrivacyForNotShown(application);
            if (z) {
                DeviceInfo.init(application);
            }
            boolean isMainProcess = isMainProcess(application, z);
            webviewSetPath(application, z);
            this.isInit = true;
            PdrUtil.closeAndroidPDialog();
            INativeAppInfo nativeInfo = getNativeInfo(application);
            io.dcloud.f.a.a(nativeInfo);
            BaseInfo.isFirstRun = true;
            if ((!SDK.isUniMPSDK() && isMainProcess) || (SDK.isUniMPSDK() && this.isUniMP)) {
                AppRuntime.initWeex(nativeInfo);
                initX5(application, z);
            } else if (!isMainProcess) {
                AppRuntime.onSubProcess(application);
            }
            if (!SDK.isUniMPSDK()) {
                a.a(application);
            }
            this.mApplication = application;
            setContext(application);
            UEH.catchUncaughtException(application);
            UEH.uploadNativeUncaughtException(application);
            if (PdrUtil.isSupportOaid()) {
                try {
                    Method declaredMethod = Class.forName("com.bun.miitmdid.core.JLibrary").getDeclaredMethod("InitEntry", new Class[]{Context.class});
                    if (declaredMethod != null) {
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke((Object) null, new Object[]{application});
                    }
                } catch (Exception unused2) {
                }
            }
            ActivityCallbacks activityCallbacks2 = new ActivityCallbacks();
            this.activityCallbacks = activityCallbacks2;
            application.registerActivityLifecycleCallbacks(activityCallbacks2);
            initLanguageConfig(getContext());
            io.dcloud.common.ui.b.a().a(self().getContext());
        }
    }

    public void removeActivityStatusListener(ActivityStatusListener activityStatusListener) {
        ActivityCallbacks activityCallbacks2 = this.activityCallbacks;
        if (activityCallbacks2 != null) {
            activityCallbacks2.removeListener(activityStatusListener);
        }
    }

    public void setContext(Context context) {
        if (this.mApplication == null) {
            this.mApplication = context;
        }
    }

    public void stopActivityStatusListener() {
        ActivityCallbacks activityCallbacks2 = this.activityCallbacks;
        if (activityCallbacks2 != null) {
            activityCallbacks2.stopListener();
        }
    }

    /* access modifiers changed from: protected */
    public void supportMultiDex(Context context) {
        try {
            Class.forName("androidx.multidex.MultiDex").getMethod("install", new Class[]{Context.class}).invoke((Object) null, new Object[]{context});
        } catch (Exception unused) {
        }
    }

    public void webviewSetPath(Context context, boolean z) {
        try {
            if (Build.VERSION.SDK_INT >= 28 && SDK.isUniMPSDK() && !isMainProcess(context, z)) {
                WebView.setDataDirectorySuffix(RuningAcitvityUtil.getAppName(context));
            }
        } catch (Exception unused) {
        }
    }
}
