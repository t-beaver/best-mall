package com.taobao.weex.bridge;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.config.ConfigModuleFactory;
import com.taobao.weex.ui.module.WXDomModule;
import com.taobao.weex.ui.module.WXTimerModule;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.cache.RegisterCache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WXModuleManager {
    private static ArrayList<String> mBlackModuleList;
    private static Map<String, WXDomModule> sDomModuleMap = new HashMap();
    /* access modifiers changed from: private */
    public static Map<String, WXModule> sGlobalModuleMap = new HashMap();
    private static Map<String, Map<String, WXModule>> sInstanceModuleMap = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public static volatile ConcurrentMap<String, ModuleFactoryImpl> sModuleFactoryMap = new ConcurrentHashMap();

    public static boolean registerModule(Map<String, RegisterCache.ModuleCache> map) {
        if (map.isEmpty()) {
            return true;
        }
        final Iterator<Map.Entry<String, RegisterCache.ModuleCache>> it = map.entrySet().iterator();
        WXBridgeManager.getInstance().post(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                while (it.hasNext()) {
                    RegisterCache.ModuleCache moduleCache = (RegisterCache.ModuleCache) ((Map.Entry) it.next()).getValue();
                    String str = moduleCache.name;
                    if (TextUtils.equals(str, WXDomModule.WXDOM)) {
                        WXLogUtils.e("Cannot registered module with name 'dom'.");
                    } else {
                        if (WXModuleManager.sModuleFactoryMap != null && WXModuleManager.sModuleFactoryMap.containsKey(str)) {
                            WXLogUtils.w("WXComponentRegistry Duplicate the Module name: " + str);
                        }
                        ModuleFactory moduleFactory = moduleCache.factory;
                        try {
                            WXModuleManager.registerNativeModule(str, moduleFactory);
                        } catch (WXException e) {
                            WXLogUtils.e("registerNativeModule" + e);
                        }
                        if (moduleCache.global) {
                            try {
                                WXModule buildInstance = moduleFactory.buildInstance();
                                buildInstance.setModuleName(str);
                                WXModuleManager.sGlobalModuleMap.put(str, buildInstance);
                            } catch (Exception e2) {
                                WXLogUtils.e(str + " class must have a default constructor without params. ", (Throwable) e2);
                            }
                        }
                        try {
                            WXModuleManager.sModuleFactoryMap.put(str, new ModuleFactoryImpl(moduleFactory));
                        } catch (Throwable unused) {
                        }
                        hashMap.put(str, moduleFactory.getMethods());
                    }
                }
                WXSDKManager.getInstance().registerModules(hashMap);
            }
        });
        return true;
    }

    public static boolean registerModule(final String str, final ModuleFactory moduleFactory, final boolean z) throws WXException {
        if (str == null || moduleFactory == null) {
            return false;
        }
        if (TextUtils.equals(str, WXDomModule.WXDOM)) {
            WXLogUtils.e("Cannot registered module with name 'dom'.");
            return false;
        } else if (RegisterCache.getInstance().cacheModule(str, moduleFactory, z)) {
            return true;
        } else {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    if (WXModuleManager.sModuleFactoryMap != null && WXModuleManager.sModuleFactoryMap.containsKey(str)) {
                        WXLogUtils.w("WXComponentRegistry Duplicate the Module name: " + str);
                    }
                    try {
                        WXModuleManager.registerNativeModule(str, moduleFactory);
                    } catch (WXException e) {
                        WXLogUtils.e("registerNativeModule" + e);
                    }
                    if (z) {
                        try {
                            WXModule buildInstance = moduleFactory.buildInstance();
                            buildInstance.setModuleName(str);
                            WXModuleManager.sGlobalModuleMap.put(str, buildInstance);
                        } catch (Exception e2) {
                            WXLogUtils.e(str + " class must have a default constructor without params. ", (Throwable) e2);
                        }
                    }
                    WXModuleManager.registerJSModule(str, moduleFactory);
                    try {
                        WXModuleManager.sModuleFactoryMap.put(str, new ModuleFactoryImpl(moduleFactory));
                    } catch (Throwable unused) {
                    }
                }
            });
            return true;
        }
    }

    static boolean registerNativeModule(String str, ModuleFactory moduleFactory) throws WXException {
        if (moduleFactory == null) {
            return false;
        }
        try {
            if (sModuleFactoryMap.containsKey(str)) {
                return true;
            }
            sModuleFactoryMap.put(str, new ModuleFactoryImpl(moduleFactory));
            return true;
        } catch (ArrayStoreException e) {
            e.printStackTrace();
            WXLogUtils.e("[WXModuleManager] registerNativeModule Error moduleName:" + str + " Error:" + e.toString());
            return true;
        }
    }

    static boolean registerJSModule(String str, ModuleFactory moduleFactory) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, moduleFactory.getMethods());
        WXSDKManager.getInstance().registerModules(hashMap);
        return true;
    }

    static Object callModuleMethod(String str, String str2, String str3, JSONArray jSONArray) {
        ModuleFactory moduleFactory = ((ModuleFactoryImpl) sModuleFactoryMap.get(str2)).mFactory;
        if (moduleFactory == null) {
            WXLogUtils.e("[WXModuleManager] module factory not found.");
            return null;
        }
        WXModule findModule = findModule(str, str2, moduleFactory);
        if (findModule == null) {
            return null;
        }
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        findModule.mWXSDKInstance = sDKInstance;
        findModule.mUniSDKInstance = sDKInstance;
        Invoker methodInvoker = moduleFactory.getMethodInvoker(str3);
        if (sDKInstance != null) {
            try {
                IWXUserTrackAdapter iWXUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
                if (iWXUserTrackAdapter != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_CODE, "101");
                    hashMap.put(IWXUserTrackAdapter.MONITOR_ARG, str2 + Operators.DOT_STR + str3);
                    hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, sDKInstance.getBundleUrl());
                    iWXUserTrackAdapter.commit(sDKInstance.getContext(), (String) null, IWXUserTrackAdapter.INVOKE_MODULE, (WXPerformance) null, hashMap);
                }
                Object dispatchCallModuleMethod = dispatchCallModuleMethod(sDKInstance, findModule, jSONArray, methodInvoker);
                if ((findModule instanceof WXDomModule) || (findModule instanceof WXTimerModule)) {
                    findModule.mWXSDKInstance = null;
                    findModule.mUniSDKInstance = null;
                }
                return dispatchCallModuleMethod;
            } catch (Exception e) {
                WXLogUtils.e("callModuleMethod >>> invoke module:" + str2 + ", method:" + str3 + " failed. ", (Throwable) e);
                if ((findModule instanceof WXDomModule) || (findModule instanceof WXTimerModule)) {
                    findModule.mWXSDKInstance = null;
                    findModule.mUniSDKInstance = null;
                }
                return null;
            } catch (Throwable th) {
                if ((findModule instanceof WXDomModule) || (findModule instanceof WXTimerModule)) {
                    findModule.mWXSDKInstance = null;
                    findModule.mUniSDKInstance = null;
                }
                throw th;
            }
        } else {
            WXLogUtils.e("callModuleMethod >>> instance is null");
            if ((findModule instanceof WXDomModule) || (findModule instanceof WXTimerModule)) {
                findModule.mWXSDKInstance = null;
                findModule.mUniSDKInstance = null;
            }
            return null;
        }
    }

    private static Object dispatchCallModuleMethod(WXSDKInstance wXSDKInstance, WXModule wXModule, JSONArray jSONArray, Invoker invoker) throws Exception {
        if (!wXSDKInstance.isPreRenderMode()) {
            return wXSDKInstance.getNativeInvokeHelper().invoke(wXModule, invoker, jSONArray);
        }
        if (invoker.isRunOnUIThread()) {
            return null;
        }
        return wXSDKInstance.getNativeInvokeHelper().invoke(wXModule, invoker, jSONArray);
    }

    public static boolean hasModule(String str) {
        return sGlobalModuleMap.containsKey(str) || sModuleFactoryMap.containsKey(str);
    }

    private static WXModule findModule(String str, String str2, ModuleFactory moduleFactory) {
        WXModule wXModule;
        WXModule wXModule2 = sGlobalModuleMap.get(str2);
        if (wXModule2 != null) {
            return wXModule2;
        }
        Map map = sInstanceModuleMap.get(str);
        if (map == null) {
            map = new ConcurrentHashMap();
            sInstanceModuleMap.put(str, map);
        }
        WXModule wXModule3 = (WXModule) map.get(str2);
        if (wXModule3 != null) {
            return wXModule3;
        }
        try {
            if (moduleFactory instanceof ConfigModuleFactory) {
                wXModule = ((ConfigModuleFactory) moduleFactory).buildInstance(WXSDKManager.getInstance().getSDKInstance(str));
            } else {
                wXModule = moduleFactory.buildInstance();
            }
            wXModule.setModuleName(str2);
            map.put(str2, wXModule);
            return wXModule;
        } catch (Exception e) {
            WXLogUtils.e(str2 + " module build instace failed.", (Throwable) e);
            return null;
        }
    }

    public static void onActivityCreate(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityCreate();
                } else {
                    WXLogUtils.w("onActivityCreate can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityStart(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityStart();
                } else {
                    WXLogUtils.w("onActivityStart can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityPause(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityPause();
                } else {
                    WXLogUtils.w("onActivityPause can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityResume(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityResume();
                } else {
                    WXLogUtils.w("onActivityResume can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityStop(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityStop();
                } else {
                    WXLogUtils.w("onActivityStop can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void onActivityDestroy(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityDestroy();
                } else {
                    WXLogUtils.w("onActivityDestroy can not find the " + str2 + " module");
                }
            }
        }
    }

    public static boolean onActivityBack(String str) {
        Map map = sInstanceModuleMap.get(str);
        if (map == null) {
            return false;
        }
        for (String str2 : map.keySet()) {
            WXModule wXModule = (WXModule) map.get(str2);
            if (wXModule != null) {
                return wXModule.onActivityBack();
            }
            WXLogUtils.w("onActivityCreate can not find the " + str2 + " module");
        }
        return false;
    }

    public static void onActivityResult(String str, int i, int i2, Intent intent) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onActivityResult(i, i2, intent);
                } else {
                    WXLogUtils.w("onActivityResult can not find the " + str2 + " module");
                }
            }
        }
    }

    public static boolean onCreateOptionsMenu(String str, Menu menu) {
        Map map = sInstanceModuleMap.get(str);
        if (map == null) {
            return false;
        }
        for (String str2 : map.keySet()) {
            WXModule wXModule = (WXModule) map.get(str2);
            if (wXModule != null) {
                wXModule.onCreateOptionsMenu(menu);
            } else {
                WXLogUtils.w("onActivityResult can not find the " + str2 + " module");
            }
        }
        return false;
    }

    public static void onRequestPermissionsResult(String str, int i, String[] strArr, int[] iArr) {
        Map map = sInstanceModuleMap.get(str);
        if (map != null) {
            for (String str2 : map.keySet()) {
                WXModule wXModule = (WXModule) map.get(str2);
                if (wXModule != null) {
                    wXModule.onRequestPermissionsResult(i, strArr, iArr);
                } else {
                    WXLogUtils.w("onActivityResult can not find the " + str2 + " module");
                }
            }
        }
    }

    public static void destroyInstanceModules(String str) {
        sDomModuleMap.remove(str);
        Map remove = sInstanceModuleMap.remove(str);
        if (remove != null && remove.size() >= 1) {
            for (Map.Entry value : remove.entrySet()) {
                WXModule wXModule = (WXModule) value.getValue();
                if (wXModule instanceof Destroyable) {
                    ((Destroyable) wXModule).destroy();
                }
            }
        }
    }

    public static void createDomModule(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance != null) {
            sDomModuleMap.put(wXSDKInstance.getInstanceId(), new WXDomModule(wXSDKInstance));
        }
    }

    public static void destoryDomModule(String str) {
        sDomModuleMap.remove(str);
    }

    public static WXDomModule getDomModule(String str) {
        return sDomModuleMap.get(str);
    }

    public static void reload() {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
                try {
                    registerJSModule((String) entry.getKey(), ((ModuleFactoryImpl) entry.getValue()).mFactory);
                } catch (Throwable unused) {
                }
            }
        }
    }

    public static void registerWhenCreateInstance() {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
                try {
                    if (!((ModuleFactoryImpl) entry.getValue()).hasRigster) {
                        registerJSModule((String) entry.getKey(), ((ModuleFactoryImpl) entry.getValue()).mFactory);
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }

    public static void resetAllModuleState() {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry value : sModuleFactoryMap.entrySet()) {
                ((ModuleFactoryImpl) value.getValue()).hasRigster = false;
            }
        }
    }

    public static void resetModuleState(String str, boolean z) {
        if (sModuleFactoryMap != null && sModuleFactoryMap.size() > 0) {
            for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
                try {
                    if (entry.getKey() != null && ((String) entry.getKey()).equals(str)) {
                        ((ModuleFactoryImpl) entry.getValue()).hasRigster = z;
                    }
                } catch (Throwable unused) {
                }
            }
        }
    }

    public static JSONObject getRegisterJsModules() {
        if (sModuleFactoryMap == null || sModuleFactoryMap.size() <= 0) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        for (Map.Entry entry : sModuleFactoryMap.entrySet()) {
            try {
                if (!getBlackList().contains(entry.getKey())) {
                    if (((ModuleFactoryImpl) entry.getValue()).mFactory != null) {
                        jSONObject.put((String) entry.getKey(), (Object) modulesToJSONArray(((ModuleFactoryImpl) entry.getValue()).mFactory.getMethods(), ((ModuleFactoryImpl) entry.getValue()).mFactory));
                    }
                }
            } catch (Throwable unused) {
            }
        }
        return jSONObject;
    }

    private static ArrayList<String> getBlackList() {
        if (mBlackModuleList == null) {
            ArrayList<String> arrayList = new ArrayList<>();
            mBlackModuleList = arrayList;
            arrayList.add("webview");
            mBlackModuleList.add("animation");
            mBlackModuleList.add("binding");
            mBlackModuleList.add("bindingx");
            mBlackModuleList.add("instanceWrap");
            mBlackModuleList.add("meta");
            mBlackModuleList.add("navigator");
            mBlackModuleList.add("expressionBinding");
        }
        return mBlackModuleList;
    }

    public static JSONArray modulesToJSONArray(String[] strArr, ModuleFactory moduleFactory) {
        JSONArray jSONArray = null;
        if (strArr != null && strArr.length > 0) {
            for (int i = 0; i < strArr.length; i++) {
                if (jSONArray == null) {
                    jSONArray = new JSONArray();
                }
                if (!strArr[i].equals("addEventListener") && !strArr[i].equals("removeAllEventListeners")) {
                    boolean z = !moduleFactory.getMethodInvoker(strArr[i]).isRunOnUIThread();
                    StringBuilder sb = new StringBuilder();
                    sb.append(strArr[i]);
                    sb.append(z ? ":sync" : ":async");
                    jSONArray.add(sb.toString());
                }
            }
        }
        return jSONArray;
    }
}
