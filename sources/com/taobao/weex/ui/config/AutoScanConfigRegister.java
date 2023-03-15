package com.taobao.weex.ui.config;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.io.IOException;

public class AutoScanConfigRegister {
    public static final String TAG = "WeexScanConfigRegister";
    private static long scanDelay;

    public static void doScanConfig() {
        if (scanDelay > 0) {
            WXSDKManager.getInstance().getWXRenderManager().postOnUiThread((Runnable) new Runnable() {
                public void run() {
                    AutoScanConfigRegister.doScanConfigAsync();
                }
            }, scanDelay);
        } else {
            doScanConfigAsync();
        }
    }

    public static void doScanConfigAsync() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                AutoScanConfigRegister.doScanConfigSync();
            }
        });
        thread.setName("AutoScanConfigRegister");
        thread.start();
    }

    /* access modifiers changed from: private */
    public static void doScanConfigSync() {
        if (WXEnvironment.sApplication != null) {
            try {
                String[] strArr = new String[0];
                try {
                    strArr = WXEnvironment.sApplication.getApplicationContext().getAssets().list("");
                } catch (IOException e) {
                    WXLogUtils.e("WeexScanConfigRegister", (Throwable) e);
                }
                if (strArr == null) {
                    return;
                }
                if (strArr.length != 0) {
                    for (String str : strArr) {
                        if (!TextUtils.isEmpty(str)) {
                            if (str.startsWith("weex_config_") && str.endsWith(".json")) {
                                if (!TextUtils.isEmpty(str)) {
                                    try {
                                        String loadAsset = WXFileUtils.loadAsset(str, WXEnvironment.getApplication());
                                        if (!TextUtils.isEmpty(loadAsset)) {
                                            if (WXEnvironment.isApkDebugable()) {
                                                WXLogUtils.d("WeexScanConfigRegister", str + " find config " + loadAsset);
                                            }
                                            JSONObject parseObject = JSON.parseObject(loadAsset);
                                            if (parseObject.containsKey("modules")) {
                                                JSONArray jSONArray = parseObject.getJSONArray("modules");
                                                for (int i = 0; i < jSONArray.size(); i++) {
                                                    ConfigModuleFactory fromConfig = ConfigModuleFactory.fromConfig(jSONArray.getJSONObject(i));
                                                    if (fromConfig != null) {
                                                        WXSDKEngine.registerModule(fromConfig.getName(), (ModuleFactory) fromConfig, false);
                                                    }
                                                }
                                            }
                                            if (parseObject.containsKey("components")) {
                                                JSONArray jSONArray2 = parseObject.getJSONArray("components");
                                                int i2 = 0;
                                                while (i2 < jSONArray2.size()) {
                                                    ConfigComponentHolder fromConfig2 = ConfigComponentHolder.fromConfig(jSONArray2.getJSONObject(i2));
                                                    if (fromConfig2 != null) {
                                                        WXSDKEngine.registerComponent((IFComponentHolder) fromConfig2, fromConfig2.isAppendTree(), fromConfig2.getType());
                                                        i2++;
                                                    } else {
                                                        return;
                                                    }
                                                }
                                                continue;
                                            } else {
                                                continue;
                                            }
                                        }
                                    } catch (Throwable th) {
                                        WXLogUtils.e("WeexScanConfigRegister", th);
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e2) {
                WXLogUtils.e("WeexScanConfigRegister", (Throwable) e2);
            }
        }
    }

    public static void setScanDelay(long j) {
        scanDelay = j;
    }
}
