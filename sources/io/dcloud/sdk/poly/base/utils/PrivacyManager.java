package io.dcloud.sdk.poly.base.utils;

import io.dcloud.sdk.core.DCloudAOLManager;
import io.dcloud.sdk.core.util.Const;
import java.util.HashMap;
import java.util.Map;

public class PrivacyManager {
    private static volatile PrivacyManager a;
    private DCloudAOLManager.PrivacyConfig b;
    private final Map<String, Boolean> c = new HashMap();

    public static class a extends DCloudAOLManager.PrivacyConfig {
        private String aId;
        private String[] imeis;
        private String imsis;
        private boolean isAllowPrivacy = true;
        private String mac;

        public String getAndroidId() {
            return this.aId;
        }

        public String[] getImeis() {
            return this.imeis;
        }

        public String getImsi() {
            return this.imsis;
        }

        public String getMacAddress() {
            return this.mac;
        }

        public boolean isAllowPrivacy() {
            return this.isAllowPrivacy;
        }

        public void setAllowPrivacy(boolean z) {
            this.isAllowPrivacy = z;
        }

        public void setAndroidId(String str) {
            this.aId = str;
        }

        public void setImeis(String[] strArr) {
            this.imeis = strArr;
        }

        public void setImsi(String str) {
            this.imsis = str;
        }

        public void setMacAddress(String str) {
            this.mac = str;
        }
    }

    private PrivacyManager() {
    }

    public static PrivacyManager getInstance() {
        if (a == null) {
            synchronized (PrivacyManager.class) {
                if (a == null) {
                    a = new PrivacyManager();
                }
            }
        }
        return a;
    }

    public String[] a() {
        DCloudAOLManager.PrivacyConfig privacyConfig = this.b;
        if (privacyConfig instanceof a) {
            return ((a) privacyConfig).getImeis();
        }
        return null;
    }

    public String b() {
        DCloudAOLManager.PrivacyConfig privacyConfig = this.b;
        return privacyConfig instanceof a ? ((a) privacyConfig).getImsi() : "";
    }

    public String c() {
        DCloudAOLManager.PrivacyConfig privacyConfig = this.b;
        return privacyConfig instanceof a ? ((a) privacyConfig).getAndroidId() : "";
    }

    public boolean d() {
        DCloudAOLManager.PrivacyConfig privacyConfig = this.b;
        if (privacyConfig instanceof a) {
            return ((a) privacyConfig).isAllowPrivacy();
        }
        return true;
    }

    public boolean e() {
        return this.b != null;
    }

    public Map<String, Boolean> getPrivacyMap() {
        return this.c;
    }

    public void updateConfig(DCloudAOLManager.PrivacyConfig privacyConfig) {
        this.b = privacyConfig;
        this.c.put(Const.PrivacyType.IS_ADULT, Boolean.valueOf(privacyConfig.isAdult()));
        this.c.put(Const.PrivacyType.IS_CAN_USE_PHONE_STATE, Boolean.valueOf(privacyConfig.isCanUsePhoneState()));
        this.c.put(Const.PrivacyType.IS_CAN_USE_STORAGE, Boolean.valueOf(privacyConfig.isCanUseStorage()));
        this.c.put(Const.PrivacyType.IS_CAN_USE_LOCATION, Boolean.valueOf(privacyConfig.isCanUseLocation()));
        this.c.put(Const.PrivacyType.IS_CAN_USE_WIFI_STATE, Boolean.valueOf(privacyConfig.isCanUseWifiState()));
        this.c.put(Const.PrivacyType.IS_CAN_GET_INSTALL_APP_LIST, Boolean.valueOf(privacyConfig.isCanGetInstallAppList()));
        this.c.put(Const.PrivacyType.IS_CAN_GET_RUNNING_APPS, Boolean.valueOf(privacyConfig.isCanGetRunningApps()));
        this.c.put(Const.PrivacyType.IS_CAN_GET_MAC, Boolean.valueOf(privacyConfig.isCanGetMacAddress()));
        this.c.put(Const.PrivacyType.IS_CAN_GET_ANDROID_ID, Boolean.valueOf(privacyConfig.isCanGetAndroidId()));
    }
}
