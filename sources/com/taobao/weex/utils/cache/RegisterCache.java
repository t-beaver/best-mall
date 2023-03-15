package com.taobao.weex.utils.cache;

import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.WXComponentRegistry;
import com.taobao.weex.ui.config.AutoScanConfigRegister;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterCache {
    private static Map<String, ComponentCache> componentCacheMap = new ConcurrentHashMap();
    private static Map<String, ModuleCache> moduleCacheMap = new ConcurrentHashMap();
    private static RegisterCache registerCache;
    private volatile int doNotCacheSize = Integer.MAX_VALUE;
    private boolean enable = false;
    private boolean enableAutoScan = true;
    private volatile boolean finished = false;

    public static RegisterCache getInstance() {
        if (registerCache == null) {
            synchronized (RegisterCache.class) {
                if (registerCache == null) {
                    registerCache = new RegisterCache();
                }
            }
        }
        return registerCache;
    }

    private RegisterCache() {
    }

    public void setEnable(boolean z) {
        this.enable = z;
    }

    private boolean enableCache() {
        return this.enable;
    }

    private boolean canCache() {
        if (!enableCache() || this.finished || getDoNotCacheSize() >= 1) {
            return false;
        }
        return true;
    }

    public boolean enableAutoScan() {
        return this.enableAutoScan;
    }

    public void setEnableAutoScan(boolean z) {
        if (this.enableAutoScan != z) {
            if (z) {
                AutoScanConfigRegister.doScanConfig();
            }
            this.enableAutoScan = z;
        }
    }

    private int getDoNotCacheSize() {
        int i = this.doNotCacheSize;
        this.doNotCacheSize = i - 1;
        return i;
    }

    public void setDoNotCacheSize(int i) {
        this.doNotCacheSize = i;
    }

    public boolean idle(boolean z) {
        if (this.finished) {
            return true;
        }
        String str = z ? "idle from create instance" : "idle from external";
        WXLogUtils.e(str + " cache size is " + (moduleCacheMap.size() + componentCacheMap.size()));
        this.finished = true;
        CacheComponentRegister();
        CacheModuleRegister();
        return true;
    }

    public boolean cacheModule(String str, ModuleFactory moduleFactory, boolean z) {
        if (!canCache()) {
            return false;
        }
        try {
            moduleCacheMap.put(str, new ModuleCache(str, moduleFactory, z));
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean cacheComponent(String str, IFComponentHolder iFComponentHolder, Map<String, Object> map) {
        if (!canCache()) {
            return false;
        }
        try {
            componentCacheMap.put(str, new ComponentCache(str, iFComponentHolder, map));
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private void CacheComponentRegister() {
        if (!componentCacheMap.isEmpty()) {
            WXComponentRegistry.registerComponent(componentCacheMap);
        }
    }

    private void CacheModuleRegister() {
        if (!moduleCacheMap.isEmpty()) {
            WXModuleManager.registerModule(moduleCacheMap);
        }
    }

    public class ModuleCache {
        public final ModuleFactory factory;
        public final boolean global;
        public final String name;

        ModuleCache(String str, ModuleFactory moduleFactory, boolean z) {
            this.name = str;
            this.factory = moduleFactory;
            this.global = z;
        }
    }

    public class ComponentCache {
        public final Map<String, Object> componentInfo;
        public final IFComponentHolder holder;
        public final String type;

        ComponentCache(String str, IFComponentHolder iFComponentHolder, Map<String, Object> map) {
            this.type = str;
            this.componentInfo = map;
            this.holder = iFComponentHolder;
        }
    }
}
