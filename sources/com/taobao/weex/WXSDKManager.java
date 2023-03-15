package com.taobao.weex;

import android.os.Looper;
import android.text.TextUtils;
import com.taobao.weex.adapter.ClassLoaderAdapter;
import com.taobao.weex.adapter.DefaultUriAdapter;
import com.taobao.weex.adapter.DefaultWXHttpAdapter;
import com.taobao.weex.adapter.ICrashInfoReporter;
import com.taobao.weex.adapter.IDrawableLoader;
import com.taobao.weex.adapter.ITracingAdapter;
import com.taobao.weex.adapter.IWXAccessibilityRoleAdapter;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.adapter.IWXJsFileLoaderAdapter;
import com.taobao.weex.adapter.IWXJscProcessManager;
import com.taobao.weex.adapter.IWXSoLoaderAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.appfram.navigator.INavigator;
import com.taobao.weex.appfram.storage.DefaultWXStorage;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.appfram.websocket.IWebSocketAdapterFactory;
import com.taobao.weex.bridge.IDCVueBridgeAdapter;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.bridge.WXValidateProcessor;
import com.taobao.weex.common.WXRefreshData;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.common.WXWorkThreadManager;
import com.taobao.weex.font.FontAdapter;
import com.taobao.weex.performance.IApmGenerator;
import com.taobao.weex.performance.IWXAnalyzer;
import com.taobao.weex.ui.WXRenderManager;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class WXSDKManager {
    private static final int DEFAULT_VIEWPORT_WIDTH = 750;
    private static AtomicInteger sInstanceId = new AtomicInteger(0);
    private static volatile WXSDKManager sManager;
    private IActivityNavBarSetter mActivityNavBarSetter;
    private Map<String, WXSDKInstance> mAllInstanceMap;
    private IApmGenerator mApmGenerater;
    private WXBridgeManager mBridgeManager;
    private ClassLoaderAdapter mClassLoaderAdapter;
    private IWXConfigAdapter mConfigAdapter;
    private ICrashInfoReporter mCrashInfo;
    private IDrawableLoader mDrawableLoader;
    private FontAdapter mFontAdapter;
    private IWXHttpAdapter mIWXHttpAdapter;
    private IWXImgLoaderAdapter mIWXImgLoaderAdapter;
    private IWXJSExceptionAdapter mIWXJSExceptionAdapter;
    private IWXSoLoaderAdapter mIWXSoLoaderAdapter;
    private IWXStorageAdapter mIWXStorageAdapter;
    private IWXUserTrackAdapter mIWXUserTrackAdapter;
    private IWebSocketAdapterFactory mIWebSocketAdapterFactory;
    private List<InstanceLifeCycleCallbacks> mLifeCycleCallbacks;
    private INavigator mNavigator;
    private boolean mNeedInitV8;
    private IWXAccessibilityRoleAdapter mRoleAdapter;
    private IWXStatisticsListener mStatisticsListener;
    private ITracingAdapter mTracingAdapter;
    private URIAdapter mURIAdapter;
    private IDCVueBridgeAdapter mVueBridgeAdapter;
    private List<IWXAnalyzer> mWXAnalyzerList;
    private IWXJsFileLoaderAdapter mWXJsFileLoaderAdapter;
    private IWXJscProcessManager mWXJscProcessManager;
    WXRenderManager mWXRenderManager;
    private WXValidateProcessor mWXValidateProcessor;
    private final WXWorkThreadManager mWXWorkThreadManager;

    public interface InstanceLifeCycleCallbacks {
        void onInstanceCreated(String str);

        void onInstanceDestroyed(String str);
    }

    private WXSDKManager() {
        this(new WXRenderManager());
    }

    private WXSDKManager(WXRenderManager wXRenderManager) {
        this.mNeedInitV8 = true;
        this.mWXRenderManager = wXRenderManager;
        this.mBridgeManager = WXBridgeManager.getInstance();
        this.mWXWorkThreadManager = new WXWorkThreadManager();
        this.mWXAnalyzerList = new CopyOnWriteArrayList();
        this.mAllInstanceMap = new HashMap();
    }

    static void initInstance(WXRenderManager wXRenderManager) {
        sManager = new WXSDKManager(wXRenderManager);
    }

    public void registerStatisticsListener(IWXStatisticsListener iWXStatisticsListener) {
        this.mStatisticsListener = iWXStatisticsListener;
    }

    public IWXStatisticsListener getWXStatisticsListener() {
        return this.mStatisticsListener;
    }

    public void onSDKEngineInitialize() {
        IWXStatisticsListener iWXStatisticsListener = this.mStatisticsListener;
        if (iWXStatisticsListener != null) {
            iWXStatisticsListener.onSDKEngineInitialize();
        }
    }

    public void setNeedInitV8(boolean z) {
        this.mNeedInitV8 = z;
    }

    public boolean needInitV8() {
        return this.mNeedInitV8;
    }

    public void takeJSHeapSnapshot(String str) {
        File file = new File(str);
        if (file.exists() || file.mkdir()) {
            String valueOf = String.valueOf(sInstanceId.get());
            if (!str.endsWith(File.separator)) {
                str = str + File.separator;
            }
            this.mBridgeManager.takeJSHeapSnapshot((str + valueOf) + ".heapsnapshot");
        }
    }

    public static WXSDKManager getInstance() {
        if (sManager == null) {
            synchronized (WXSDKManager.class) {
                if (sManager == null) {
                    sManager = new WXSDKManager();
                }
            }
        }
        return sManager;
    }

    public static float getInstanceViewPortWidth(String str) {
        WXSDKInstance sDKInstance = getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return 750.0f;
        }
        return sDKInstance.getInstanceViewPortWidthWithFloat();
    }

    static void setInstance(WXSDKManager wXSDKManager) {
        sManager = wXSDKManager;
    }

    public IActivityNavBarSetter getActivityNavBarSetter() {
        return this.mActivityNavBarSetter;
    }

    public void setActivityNavBarSetter(IActivityNavBarSetter iActivityNavBarSetter) {
        this.mActivityNavBarSetter = iActivityNavBarSetter;
    }

    public void restartBridge() {
        this.mBridgeManager.restart();
    }

    public WXBridgeManager getWXBridgeManager() {
        return this.mBridgeManager;
    }

    public WXRenderManager getWXRenderManager() {
        return this.mWXRenderManager;
    }

    public IWXJscProcessManager getWXJscProcessManager() {
        return this.mWXJscProcessManager;
    }

    public WXWorkThreadManager getWXWorkThreadManager() {
        return this.mWXWorkThreadManager;
    }

    public void setWxConfigAdapter(IWXConfigAdapter iWXConfigAdapter) {
        this.mConfigAdapter = iWXConfigAdapter;
    }

    public IWXConfigAdapter getWxConfigAdapter() {
        return this.mConfigAdapter;
    }

    public WXSDKInstance getSDKInstance(String str) {
        if (str == null) {
            return null;
        }
        return this.mWXRenderManager.getWXSDKInstance(str);
    }

    public void postOnUiThread(Runnable runnable, long j) {
        this.mWXRenderManager.postOnUiThread(WXThread.secure(runnable), j);
    }

    public Map<String, WXSDKInstance> getAllInstanceMap() {
        return this.mAllInstanceMap;
    }

    public void destroy() {
        WXWorkThreadManager wXWorkThreadManager = this.mWXWorkThreadManager;
        if (wXWorkThreadManager != null) {
            wXWorkThreadManager.destroy();
        }
        this.mAllInstanceMap.clear();
    }

    @Deprecated
    public void callback(String str, String str2, Map<String, Object> map) {
        this.mBridgeManager.callback(str, str2, map);
    }

    @Deprecated
    public void callback(String str, String str2, Map<String, Object> map, boolean z) {
        this.mBridgeManager.callback(str, str2, map, z);
    }

    public void initScriptsFramework(String str) {
        this.mBridgeManager.initScriptsFramework(str);
    }

    public void registerComponents(List<Map<String, Object>> list) {
        this.mBridgeManager.registerComponents(list);
    }

    public void registerModules(Map<String, Object> map) {
        this.mBridgeManager.registerModules(map);
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3) {
        fireEvent(str, str2, str3, new HashMap());
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3, Map<String, Object> map) {
        fireEvent(str, str2, str3, map, (Map<String, Object>) null);
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        if (!WXEnvironment.isApkDebugable() || Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()) {
            this.mBridgeManager.fireEventOnNode(str, str2, str3, map, map2);
            return;
        }
        throw new WXRuntimeException("[WXSDKManager]  fireEvent error");
    }

    /* access modifiers changed from: package-private */
    public void createInstance(WXSDKInstance wXSDKInstance, Script script, Map<String, Object> map, String str) {
        this.mWXRenderManager.registerInstance(wXSDKInstance);
        this.mBridgeManager.createInstance(wXSDKInstance.getInstanceId(), script, map, str);
        List<InstanceLifeCycleCallbacks> list = this.mLifeCycleCallbacks;
        if (list != null) {
            for (InstanceLifeCycleCallbacks onInstanceCreated : list) {
                onInstanceCreated.onInstanceCreated(wXSDKInstance.getInstanceId());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshInstance(String str, WXRefreshData wXRefreshData) {
        this.mBridgeManager.refreshInstance(str, wXRefreshData);
    }

    /* access modifiers changed from: package-private */
    public void destroyInstance(String str) {
        setCrashInfo(WXEnvironment.WEEX_CURRENT_KEY, "");
        if (!TextUtils.isEmpty(str)) {
            if (WXUtils.isUiThread()) {
                List<InstanceLifeCycleCallbacks> list = this.mLifeCycleCallbacks;
                if (list != null) {
                    for (InstanceLifeCycleCallbacks onInstanceDestroyed : list) {
                        onInstanceDestroyed.onInstanceDestroyed(str);
                    }
                }
                this.mWXRenderManager.removeRenderStatement(str);
                this.mBridgeManager.destroyInstance(str);
                WXModuleManager.destroyInstanceModules(str);
                return;
            }
            throw new WXRuntimeException("[WXSDKManager] destroyInstance error");
        }
    }

    /* access modifiers changed from: package-private */
    public String generateInstanceId() {
        return String.valueOf(sInstanceId.incrementAndGet());
    }

    public IWXUserTrackAdapter getIWXUserTrackAdapter() {
        return this.mIWXUserTrackAdapter;
    }

    public IWXImgLoaderAdapter getIWXImgLoaderAdapter() {
        return this.mIWXImgLoaderAdapter;
    }

    public IWXJsFileLoaderAdapter getIWXJsFileLoaderAdapter() {
        return this.mWXJsFileLoaderAdapter;
    }

    public IDrawableLoader getDrawableLoader() {
        return this.mDrawableLoader;
    }

    public IWXJSExceptionAdapter getIWXJSExceptionAdapter() {
        return this.mIWXJSExceptionAdapter;
    }

    public void setIWXJSExceptionAdapter(IWXJSExceptionAdapter iWXJSExceptionAdapter) {
        this.mIWXJSExceptionAdapter = iWXJSExceptionAdapter;
    }

    public IWXHttpAdapter getIWXHttpAdapter() {
        if (this.mIWXHttpAdapter == null) {
            this.mIWXHttpAdapter = new DefaultWXHttpAdapter();
        }
        return this.mIWXHttpAdapter;
    }

    public IApmGenerator getApmGenerater() {
        return this.mApmGenerater;
    }

    public URIAdapter getURIAdapter() {
        if (this.mURIAdapter == null) {
            this.mURIAdapter = new DefaultUriAdapter();
        }
        return this.mURIAdapter;
    }

    public IDCVueBridgeAdapter getVueBridgeAdapter() {
        return this.mVueBridgeAdapter;
    }

    public ClassLoaderAdapter getClassLoaderAdapter() {
        if (this.mClassLoaderAdapter == null) {
            this.mClassLoaderAdapter = new ClassLoaderAdapter();
        }
        return this.mClassLoaderAdapter;
    }

    public IWXSoLoaderAdapter getIWXSoLoaderAdapter() {
        return this.mIWXSoLoaderAdapter;
    }

    public List<IWXAnalyzer> getWXAnalyzerList() {
        return this.mWXAnalyzerList;
    }

    public void addWXAnalyzer(IWXAnalyzer iWXAnalyzer) {
        if (!this.mWXAnalyzerList.contains(iWXAnalyzer)) {
            this.mWXAnalyzerList.add(iWXAnalyzer);
        }
    }

    public void rmWXAnalyzer(IWXAnalyzer iWXAnalyzer) {
        this.mWXAnalyzerList.remove(iWXAnalyzer);
    }

    /* access modifiers changed from: package-private */
    public void setInitConfig(InitConfig initConfig) {
        this.mIWXHttpAdapter = initConfig.getHttpAdapter();
        this.mIWXImgLoaderAdapter = initConfig.getImgAdapter();
        this.mDrawableLoader = initConfig.getDrawableLoader();
        this.mIWXStorageAdapter = initConfig.getStorageAdapter();
        this.mVueBridgeAdapter = initConfig.getVueBridgeAdaper();
        this.mIWXUserTrackAdapter = initConfig.getUtAdapter();
        this.mURIAdapter = initConfig.getURIAdapter();
        this.mIWebSocketAdapterFactory = initConfig.getWebSocketAdapterFactory();
        this.mIWXJSExceptionAdapter = initConfig.getJSExceptionAdapter();
        this.mIWXSoLoaderAdapter = initConfig.getIWXSoLoaderAdapter();
        this.mClassLoaderAdapter = initConfig.getClassLoaderAdapter();
        this.mApmGenerater = initConfig.getApmGenerater();
        this.mWXJsFileLoaderAdapter = initConfig.getJsFileLoaderAdapter();
        this.mWXJscProcessManager = initConfig.getJscProcessManager();
    }

    public IWXStorageAdapter getIWXStorageAdapter() {
        if (this.mIWXStorageAdapter == null) {
            if (WXEnvironment.sApplication != null) {
                this.mIWXStorageAdapter = new DefaultWXStorage(WXEnvironment.sApplication);
            } else {
                WXLogUtils.e("WXStorageModule", "No Application context found,you should call WXSDKEngine#initialize() method in your application");
            }
        }
        return this.mIWXStorageAdapter;
    }

    public void notifyTrimMemory() {
        this.mBridgeManager.notifyTrimMemory();
    }

    public void notifySerializeCodeCache() {
        this.mBridgeManager.notifySerializeCodeCache();
    }

    public IWebSocketAdapter getIWXWebSocketAdapter() {
        IWebSocketAdapterFactory iWebSocketAdapterFactory = this.mIWebSocketAdapterFactory;
        if (iWebSocketAdapterFactory != null) {
            return iWebSocketAdapterFactory.createWebSocketAdapter();
        }
        return null;
    }

    public void registerValidateProcessor(WXValidateProcessor wXValidateProcessor) {
        this.mWXValidateProcessor = wXValidateProcessor;
    }

    public WXValidateProcessor getValidateProcessor() {
        return this.mWXValidateProcessor;
    }

    public void setCrashInfoReporter(ICrashInfoReporter iCrashInfoReporter) {
        this.mCrashInfo = iCrashInfoReporter;
    }

    public void setCrashInfo(String str, String str2) {
        ICrashInfoReporter iCrashInfoReporter = this.mCrashInfo;
        if (iCrashInfoReporter != null) {
            iCrashInfoReporter.addCrashInfo(str, str2);
        }
    }

    public void setTracingAdapter(ITracingAdapter iTracingAdapter) {
        this.mTracingAdapter = iTracingAdapter;
    }

    public ITracingAdapter getTracingAdapter() {
        return this.mTracingAdapter;
    }

    public void registerInstanceLifeCycleCallbacks(InstanceLifeCycleCallbacks instanceLifeCycleCallbacks) {
        if (this.mLifeCycleCallbacks == null) {
            this.mLifeCycleCallbacks = new ArrayList();
        }
        this.mLifeCycleCallbacks.add(instanceLifeCycleCallbacks);
    }

    public void setAccessibilityRoleAdapter(IWXAccessibilityRoleAdapter iWXAccessibilityRoleAdapter) {
        this.mRoleAdapter = iWXAccessibilityRoleAdapter;
    }

    public IWXAccessibilityRoleAdapter getAccessibilityRoleAdapter() {
        return this.mRoleAdapter;
    }

    public INavigator getNavigator() {
        return this.mNavigator;
    }

    public void setNavigator(INavigator iNavigator) {
        this.mNavigator = iNavigator;
    }

    public FontAdapter getFontAdapter() {
        if (this.mFontAdapter == null) {
            synchronized (this) {
                if (this.mFontAdapter == null) {
                    this.mFontAdapter = new FontAdapter();
                }
            }
        }
        return this.mFontAdapter;
    }
}
