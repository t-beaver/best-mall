package io.dcloud.weex;

import android.app.Application;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXFileUtils;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.uniapp.UniAppHookProxy;
import java.io.File;
import java.lang.reflect.Method;

public class MoudlesLoader {
    private static MoudlesLoader sLoader;

    public static MoudlesLoader getInstance() {
        if (sLoader == null) {
            synchronized (MoudlesLoader.class) {
                if (sLoader == null) {
                    sLoader = new MoudlesLoader();
                }
            }
        }
        return sLoader;
    }

    public void onCreate(Application application) {
        JSONArray jSONArray;
        JSONObject pluginsValue = getPluginsValue(application);
        if (pluginsValue != null && (jSONArray = pluginsValue.getJSONArray("nativePlugins")) != null && jSONArray.size() > 0) {
            for (int i = 0; i < jSONArray.size(); i++) {
                initMoudle(jSONArray.getJSONObject(i), application);
            }
        }
    }

    public void onCreate(Application application, String str) {
        if (application != null && !PdrUtil.isEmpty(str) && DHFile.exists(str)) {
            JSONObject parseObject = JSON.parseObject(new String(DHFile.readAll(new File(str))));
            if (!PdrUtil.isEmpty(parseObject)) {
                JSONObject jSONObject = parseObject.getJSONObject("_dp_nativeplugin");
                if (!PdrUtil.isEmpty(jSONObject)) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject(WXEnvironment.OS);
                    if (!PdrUtil.isEmpty(jSONObject2)) {
                        initMoudle(jSONObject2, application);
                    }
                }
            }
        }
    }

    private void initMoudle(JSONObject jSONObject, Application application) {
        if (jSONObject != null) {
            String string = jSONObject.getString("hooksClass");
            if (!TextUtils.isEmpty(string)) {
                createAppMoudle(string, application, true);
            }
            JSONArray jSONArray = jSONObject.getJSONArray("plugins");
            if (jSONArray != null && jSONArray != null && jSONArray.size() > 0) {
                for (int i = 0; i < jSONArray.size(); i++) {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    if (jSONObject2 != null) {
                        registerMoudle(jSONObject2.getString("name"), jSONObject2.getString("class"), jSONObject2.getString("type"));
                    }
                }
            }
        }
    }

    private void createAppMoudle(String str, Application application, boolean z) {
        try {
            Class<?> cls = Class.forName(str);
            Object newInstance = cls.newInstance();
            if (!(newInstance instanceof AppHookProxy)) {
                Method method = cls.getMethod("onCreate", new Class[]{Application.class});
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(newInstance, new Object[]{application});
                }
            } else if (z) {
                ((AppHookProxy) newInstance).onCreate(application);
            } else if (newInstance instanceof UniAppHookProxy) {
                ((UniAppHookProxy) newInstance).onSubProcessCreate(application);
            }
        } catch (Exception unused) {
        }
    }

    private void registerMoudle(String str, String str2, String str3) {
        try {
            Class<?> cls = Class.forName(str2);
            if (!TextUtils.isEmpty(str3)) {
                if (!str3.equalsIgnoreCase("module")) {
                    if (str3.equalsIgnoreCase(WXBridgeManager.COMPONENT)) {
                        WXSDKEngine.registerComponent(str, (Class<? extends WXComponent>) cls);
                        return;
                    }
                    return;
                }
            }
            WXSDKEngine.registerModule(str, cls);
        } catch (Exception unused) {
        }
    }

    private JSONObject getPluginsValue(Application application) {
        String loadAsset = WXFileUtils.loadAsset("dcloud_uniplugins.json", application);
        if (!TextUtils.isEmpty(loadAsset)) {
            return JSONObject.parseObject(loadAsset);
        }
        return null;
    }

    public void onSubProcess(Application application) {
        JSONArray jSONArray;
        JSONObject pluginsValue = getPluginsValue(application);
        if (pluginsValue != null && (jSONArray = pluginsValue.getJSONArray("nativePlugins")) != null && jSONArray.size() > 0) {
            for (int i = 0; i < jSONArray.size(); i++) {
                createAppMoudle(jSONArray.getJSONObject(i).getString("hooksClass"), application, false);
            }
        }
    }
}
