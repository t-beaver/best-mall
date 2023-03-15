package com.taobao.weex.ui.config;

import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.SimpleComponentHolder;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ConfigComponentHolder implements IFComponentHolder {
    public static final String TAG = "WeexScanConfigRegister";
    private boolean mAppendTree;
    private Class mClass;
    private ClassLoader mClassLoader;
    private String mClassName;
    private Map<String, Invoker> mMethodInvokers;
    private Map<String, Invoker> mPropertyInvokers;
    private String mType;
    private String[] methods;

    public void loadIfNonLazy() {
    }

    public ConfigComponentHolder(String str, boolean z, String str2, String[] strArr) {
        this.mType = str;
        this.mAppendTree = z;
        this.mClassName = str2;
        this.methods = strArr;
    }

    private synchronized boolean generate() {
        Class cls = this.mClass;
        if (cls == null) {
            return false;
        }
        Pair<Map<String, Invoker>, Map<String, Invoker>> methods2 = SimpleComponentHolder.getMethods(cls);
        this.mPropertyInvokers = (Map) methods2.first;
        this.mMethodInvokers = (Map) methods2.second;
        return true;
    }

    public synchronized WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        WXComponent createInstance;
        if (this.mClass == null || this.mClassLoader != wXSDKInstance.getContext().getClassLoader()) {
            this.mClass = WXSDKManager.getInstance().getClassLoaderAdapter().getComponentClass(this.mType, this.mClassName, wXSDKInstance);
            this.mClassLoader = wXSDKInstance.getContext().getClassLoader();
        }
        createInstance = new SimpleComponentHolder.ClazzComponentCreator(this.mClass).createInstance(wXSDKInstance, wXVContainer, basicComponentData);
        createInstance.bindHolder(this);
        return createInstance;
    }

    public synchronized Invoker getPropertyInvoker(String str) {
        if (this.mPropertyInvokers == null && !generate()) {
            return null;
        }
        return this.mPropertyInvokers.get(str);
    }

    public Invoker getMethodInvoker(String str) {
        if (this.mMethodInvokers != null || generate()) {
            return this.mMethodInvokers.get(str);
        }
        return null;
    }

    public String[] getMethods() {
        String[] strArr = this.methods;
        return strArr == null ? new String[0] : strArr;
    }

    public static final ConfigComponentHolder fromConfig(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        try {
            String string = jSONObject.getString("name");
            boolean booleanValue = jSONObject.getBooleanValue("appendTree");
            String string2 = jSONObject.getString("className");
            JSONArray jSONArray = jSONObject.containsKey("methods") ? jSONObject.getJSONArray("methods") : null;
            if (!TextUtils.isEmpty(string)) {
                if (!TextUtils.isEmpty(string2)) {
                    String[] strArr = new String[0];
                    if (jSONArray != null) {
                        strArr = new String[jSONArray.size()];
                        jSONArray.toArray(strArr);
                    }
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.d("WeexScanConfigRegister", "resolve component " + string + " className " + string2 + " methods " + jSONArray);
                    }
                    return new ConfigComponentHolder(string, booleanValue, string2, strArr);
                }
            }
            return null;
        } catch (Exception e) {
            WXLogUtils.e("WeexScanConfigRegister", (Throwable) e);
            return null;
        }
    }

    public boolean isAppendTree() {
        return this.mAppendTree;
    }

    public String getType() {
        return this.mType;
    }
}
