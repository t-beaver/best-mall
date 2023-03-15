package com.taobao.weex.bridge;

import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.common.RenderTypes;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.WXLogUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WXParams implements Serializable {
    private String appName;
    private String appVersion;
    private String cacheDir;
    private String crashFilePath;
    private String deviceHeight;
    private String deviceModel;
    private String deviceWidth;
    private String layoutDirection;
    private String libIcuPath;
    private String libJsbPath;
    private String libJscPath;
    private String libJssPath;
    private String libLdPath;
    private String logLevel;
    private String needInitV8;
    private Map<String, String> options;
    private String osVersion;
    private String platform;
    private String shouldInfoCollect;
    private String useSingleProcess;
    private String weexVersion;

    public Object getOptions() {
        return this.options;
    }

    public String getLibJsbPath() {
        WXLogUtils.e("getLibJsbPath is running " + this.libJsbPath);
        return this.libJsbPath;
    }

    public void setLibJsbPath(String str) {
        this.libJsbPath = str;
    }

    public void setOptions(Map<String, String> map) {
        this.options = map;
    }

    public String getShouldInfoCollect() {
        return this.shouldInfoCollect;
    }

    public void setShouldInfoCollect(String str) {
        this.shouldInfoCollect = str;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String str) {
        this.platform = str;
    }

    public void setCacheDir(String str) {
        this.cacheDir = str;
    }

    public String getCacheDir() {
        return this.cacheDir;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String str) {
        this.osVersion = str;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
    }

    public String getWeexVersion() {
        return this.weexVersion;
    }

    public void setWeexVersion(String str) {
        this.weexVersion = str;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public void setDeviceModel(String str) {
        this.deviceModel = str;
    }

    public String getLayoutDirection() {
        return this.layoutDirection;
    }

    public void setLayoutDirection(String str) {
        this.layoutDirection = str;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getDeviceWidth() {
        return this.deviceWidth;
    }

    public boolean getReleaseMap() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return false;
        }
        String configWhenInit = wxConfigAdapter.getConfigWhenInit("wxapm", "release_map", AbsoluteConst.TRUE);
        WXLogUtils.e("getReleaseMap:" + configWhenInit);
        return AbsoluteConst.TRUE.equalsIgnoreCase(configWhenInit);
    }

    @Deprecated
    public void setDeviceWidth(String str) {
        this.deviceWidth = str;
    }

    public String getDeviceHeight() {
        return this.deviceHeight;
    }

    public void setDeviceHeight(String str) {
        this.deviceHeight = str;
    }

    public String getLogLevel() {
        String str = this.logLevel;
        return str == null ? "" : str;
    }

    public String getUseSingleProcess() {
        WXLogUtils.e("getUseSingleProcess is running " + this.useSingleProcess);
        return this.useSingleProcess;
    }

    public void setUseSingleProcess(String str) {
        this.useSingleProcess = str;
    }

    public void setLogLevel(String str) {
        this.logLevel = str;
    }

    public String getNeedInitV8() {
        String str = this.needInitV8;
        return str == null ? "" : str;
    }

    public void setNeedInitV8(boolean z) {
        if (z) {
            this.needInitV8 = "1";
        } else {
            this.needInitV8 = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
        }
    }

    public void setCrashFilePath(String str) {
        WXLogUtils.e("WXParams", "setCrashFilePath: " + str);
        this.crashFilePath = str;
    }

    public String getCrashFilePath() {
        WXLogUtils.d("WXParams", "getCrashFilePath:" + this.crashFilePath);
        return this.crashFilePath;
    }

    public String getLibJssPath() {
        WXLogUtils.d("getLibJssPath is running " + this.libJssPath);
        return this.libJssPath;
    }

    public String getLibJscPath() {
        WXLogUtils.d("getLibJscPath is running " + this.libJscPath);
        return this.libJscPath;
    }

    public void setLibJscPath(String str) {
        this.libJscPath = str;
    }

    public void setLibJssPath(String str) {
        this.libJssPath = str;
    }

    public String getLibIcuPath() {
        WXLogUtils.d("getLibIcuPath is running " + this.libIcuPath);
        return this.libIcuPath;
    }

    public void setLibIcuPath(String str) {
        this.libIcuPath = str;
    }

    public String getLibLdPath() {
        WXLogUtils.e("getLibLdPath is running " + this.libLdPath);
        return this.libLdPath;
    }

    public void setLibLdPath(String str) {
        this.libLdPath = str;
    }

    public String getUseRunTimeApi() {
        return String.valueOf(WXEnvironment.sUseRunTimeApi);
    }

    public Map<String, Object> toMap() {
        HashMap hashMap = new HashMap();
        hashMap.put(WXConfig.appName, this.appName);
        hashMap.put(WXConfig.appVersion, this.appVersion);
        hashMap.put(WXConfig.cacheDir, this.cacheDir);
        hashMap.put(WXConfig.deviceHeight, this.deviceHeight);
        hashMap.put("deviceModel", this.deviceModel);
        hashMap.put(WXConfig.deviceWidth, this.deviceWidth);
        hashMap.put(WXConfig.layoutDirection, this.layoutDirection);
        hashMap.put("libJssPath", this.libJssPath);
        hashMap.put(WXConfig.logLevel, this.logLevel);
        hashMap.put("needInitV8", this.needInitV8);
        hashMap.put("osVersion", this.osVersion);
        hashMap.put(RenderTypes.RENDER_TYPE_NATIVE, this.platform);
        hashMap.put("useSingleProcess", this.useSingleProcess);
        hashMap.put("shouldInfoCollect", this.shouldInfoCollect);
        hashMap.put(WXConfig.weexVersion, this.weexVersion);
        hashMap.put("crashFilePath", this.crashFilePath);
        hashMap.put("libJscPath", this.libJscPath);
        hashMap.put("libIcuPath", this.libIcuPath);
        hashMap.put("libLdPath", this.libLdPath);
        hashMap.put("options", this.options);
        hashMap.put("useRunTimeApi", Boolean.valueOf(WXEnvironment.sUseRunTimeApi));
        hashMap.put("__enable_native_promise__", Boolean.valueOf(!WXEnvironment.sUseRunTimeApi));
        return hashMap;
    }
}
