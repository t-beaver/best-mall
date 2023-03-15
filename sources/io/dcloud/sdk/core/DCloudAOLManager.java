package io.dcloud.sdk.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.webkit.WebView;
import io.dcloud.h.c.a;
import io.dcloud.sdk.core.api.AOLLoader;
import java.util.concurrent.atomic.AtomicBoolean;

public class DCloudAOLManager {
    private static String a = "";
    protected static AOLLoader b;
    protected static AtomicBoolean c = new AtomicBoolean(false);
    private static AtomicBoolean d = new AtomicBoolean(false);

    public static class InitConfig {
        private String a;
        private String b;
        private String c;
        private String d;
        private boolean e = false;

        public String getAdId() {
            return this.b;
        }

        public String getAppId() {
            return this.a;
        }

        public String getName() {
            return this.d;
        }

        public String getVersion() {
            return this.c;
        }

        public boolean isDebug() {
            return this.e;
        }

        public InitConfig setAdId(String str) {
            this.b = str;
            return this;
        }

        public InitConfig setAppId(String str) {
            this.a = str;
            return this;
        }

        public void setDebug(boolean z) {
            this.e = z;
        }

        public InitConfig setName(String str) {
            this.d = str;
            return this;
        }

        public InitConfig setVersion(String str) {
            this.c = str;
            return this;
        }
    }

    public static class PrivacyConfig {
        private boolean isAdult = true;
        private boolean isCanGetAndroidId = true;
        private boolean isCanGetInstallAppList = true;
        private boolean isCanGetMacAddress = true;
        private boolean isCanGetRunningApps = true;
        private boolean isCanUseLocation = true;
        private boolean isCanUsePhoneState = true;
        private boolean isCanUseStorage = true;
        private boolean isCanUseWifiState = true;

        public boolean isAdult() {
            return this.isAdult;
        }

        public boolean isCanGetAndroidId() {
            return this.isCanGetAndroidId;
        }

        public boolean isCanGetInstallAppList() {
            return this.isCanGetInstallAppList;
        }

        public boolean isCanGetMacAddress() {
            return this.isCanGetMacAddress;
        }

        public boolean isCanGetRunningApps() {
            return this.isCanGetRunningApps;
        }

        public boolean isCanUseLocation() {
            return this.isCanUseLocation;
        }

        public boolean isCanUsePhoneState() {
            return this.isCanUsePhoneState;
        }

        public boolean isCanUseStorage() {
            return this.isCanUseStorage;
        }

        public boolean isCanUseWifiState() {
            return this.isCanUseWifiState;
        }

        public void setAdult(boolean z) {
            this.isAdult = z;
        }

        public void setCanGetAndroidId(boolean z) {
            this.isCanGetAndroidId = z;
        }

        public void setCanGetInstallAppList(boolean z) {
            this.isCanGetInstallAppList = z;
        }

        public void setCanGetMacAddress(boolean z) {
            this.isCanGetMacAddress = z;
        }

        public void setCanGetRunningApps(boolean z) {
            this.isCanGetRunningApps = z;
        }

        public void setCanUseLocation(boolean z) {
            this.isCanUseLocation = z;
        }

        public void setCanUsePhoneState(boolean z) {
            this.isCanUsePhoneState = z;
        }

        public void setCanUseStorage(boolean z) {
            this.isCanUseStorage = z;
        }

        public void setCanUseWifiState(boolean z) {
            this.isCanUseWifiState = z;
        }
    }

    public static boolean getPersonalAd(Context context) {
        AOLLoader aOLLoader = b;
        if (aOLLoader == null) {
            return false;
        }
        return aOLLoader.getPersonalAd(context);
    }

    public static String getVersion() {
        return a;
    }

    public static void init(Context context, InitConfig initConfig) {
        if (context == null || initConfig == null) {
            throw new NullPointerException("context or config is null");
        } else if (((context instanceof Activity) || (context instanceof Application)) && !c.get()) {
            c.set(true);
            if (b == null) {
                a d2 = a.d();
                d2.b(context);
                d2.a(initConfig);
                b = d2;
                d2.a(context);
            }
        }
    }

    public static void initWebViewWithMultiProcess(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (next.pid == Process.myPid()) {
                    if (next.processName.equals(context.getPackageName())) {
                        WebView.setDataDirectorySuffix(next.processName);
                        return;
                    }
                    return;
                }
            }
        }
    }

    public static boolean isInit() {
        return c.get();
    }

    public static void setPersonalAd(Context context, boolean z) {
        AOLLoader aOLLoader = b;
        if (aOLLoader != null) {
            aOLLoader.setPersonalAd(context, z);
        }
    }

    public static void setPrivacyConfig(PrivacyConfig privacyConfig) {
        AOLLoader aOLLoader = b;
        if (aOLLoader == null) {
            throw new RuntimeException("please init first");
        } else if (privacyConfig != null) {
            aOLLoader.setPrivacyConfig(privacyConfig);
        } else {
            throw new RuntimeException("config is null");
        }
    }
}
