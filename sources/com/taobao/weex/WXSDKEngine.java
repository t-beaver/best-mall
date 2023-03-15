package com.taobao.weex;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.InitConfig;
import com.taobao.weex.adapter.IDrawableLoader;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.adapter.IWXJsFileLoaderAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.appfram.clipboard.WXClipboardModule;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.appfram.navigator.INavigator;
import com.taobao.weex.appfram.navigator.WXNavigatorModule;
import com.taobao.weex.appfram.pickers.WXPickersModule;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.appfram.storage.WXStorageModule;
import com.taobao.weex.appfram.websocket.WebSocketModule;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.bridge.WXServiceManager;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.TypeModuleFactory;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXInstanceWrap;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.http.WXStreamModule;
import com.taobao.weex.ui.ExternalLoaderComponentHolder;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.IExternalModuleGetter;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.SimpleComponentHolder;
import com.taobao.weex.ui.WXComponentRegistry;
import com.taobao.weex.ui.animation.WXAnimationModule;
import com.taobao.weex.ui.component.Textarea;
import com.taobao.weex.ui.component.WXA;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.component.WXHeader;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXIndicator;
import com.taobao.weex.ui.component.WXInput;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXLoadingIndicator;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.WXSlider;
import com.taobao.weex.ui.component.WXSliderNeighbor;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVideo;
import com.taobao.weex.ui.component.WXWeb;
import com.taobao.weex.ui.component.list.HorizontalListComponent;
import com.taobao.weex.ui.component.list.SimpleListComponent;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList;
import com.taobao.weex.ui.component.richtext.WXRichText;
import com.taobao.weex.ui.config.AutoScanConfigRegister;
import com.taobao.weex.ui.module.ConsoleLogModule;
import com.taobao.weex.ui.module.WXDeviceInfoModule;
import com.taobao.weex.ui.module.WXLocaleModule;
import com.taobao.weex.ui.module.WXMetaModule;
import com.taobao.weex.ui.module.WXModalUIModule;
import com.taobao.weex.ui.module.WXTimerModule;
import com.taobao.weex.ui.module.WXWebViewModule;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXSoInstallMgrSdk;
import com.taobao.weex.utils.batch.BatchOperationHelper;
import com.taobao.weex.utils.cache.RegisterCache;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.feature.uniapp.AbsSDKEngine;
import java.util.HashMap;
import java.util.Map;

public class WXSDKEngine extends AbsSDKEngine {
    public static final String JS_FRAMEWORK_RELOAD = "js_framework_reload";
    private static final String TAG = "WXSDKEngine";
    private static final String V8_SO_NAME = "weexcore";
    private static volatile boolean mIsInit = false;
    /* access modifiers changed from: private */
    public static volatile boolean mIsSoInit = false;
    private static final Object mLock = new Object();

    public static abstract class DestroyableModule extends WXModule implements Destroyable {
    }

    @Deprecated
    public static void init(Application application) {
        init(application, (IWXUserTrackAdapter) null);
    }

    @Deprecated
    public static void init(Application application, IWXUserTrackAdapter iWXUserTrackAdapter) {
        init(application, iWXUserTrackAdapter, (String) null);
    }

    @Deprecated
    public static void init(Application application, IWXUserTrackAdapter iWXUserTrackAdapter, String str) {
        initialize(application, new InitConfig.Builder().setUtAdapter(iWXUserTrackAdapter).build());
    }

    public static boolean isInitialized() {
        boolean z;
        synchronized (mLock) {
            z = mIsInit && WXEnvironment.JsFrameworkInit;
        }
        return z;
    }

    public static boolean isSoInitialized() {
        boolean z;
        synchronized (mLock) {
            z = mIsSoInit;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0043, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initialize(android.app.Application r5, com.taobao.weex.InitConfig r6) {
        /*
            java.lang.Object r0 = mLock
            monitor-enter(r0)
            boolean r1 = mIsInit     // Catch:{ all -> 0x0044 }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            return
        L_0x0009:
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0044 }
            com.taobao.weex.WXEnvironment.sSDKInitStart = r1     // Catch:{ all -> 0x0044 }
            boolean r3 = com.taobao.weex.WXEnvironment.isApkDebugable(r5)     // Catch:{ all -> 0x0044 }
            if (r3 == 0) goto L_0x001a
            com.taobao.weex.utils.LogLevel r3 = com.taobao.weex.utils.LogLevel.INFO     // Catch:{ all -> 0x0044 }
            com.taobao.weex.WXEnvironment.sLogLevel = r3     // Catch:{ all -> 0x0044 }
            goto L_0x001e
        L_0x001a:
            com.taobao.weex.utils.LogLevel r3 = com.taobao.weex.utils.LogLevel.WARN     // Catch:{ all -> 0x0044 }
            com.taobao.weex.WXEnvironment.sLogLevel = r3     // Catch:{ all -> 0x0044 }
        L_0x001e:
            doInitInternal(r5, r6)     // Catch:{ all -> 0x0044 }
            registerApplicationOptions(r5)     // Catch:{ all -> 0x0044 }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0044 }
            long r3 = r3 - r1
            com.taobao.weex.WXEnvironment.sSDKInitInvokeTime = r3     // Catch:{ all -> 0x0044 }
            java.lang.String r6 = "SDKInitInvokeTime"
            long r1 = com.taobao.weex.WXEnvironment.sSDKInitInvokeTime     // Catch:{ all -> 0x0044 }
            com.taobao.weex.utils.WXLogUtils.renderPerformanceLog(r6, r1)     // Catch:{ all -> 0x0044 }
            r6 = 1
            mIsInit = r6     // Catch:{ all -> 0x0044 }
            boolean r6 = io.dcloud.common.util.AppRuntime.hasPrivacyForNotShown(r5)     // Catch:{ all -> 0x0044 }
            if (r6 != 0) goto L_0x0042
            io.dcloud.weex.MoudlesLoader r6 = io.dcloud.feature.uniapp.UniMoudlesLoader.getInstance()     // Catch:{ all -> 0x0044 }
            r6.onCreate(r5)     // Catch:{ all -> 0x0044 }
        L_0x0042:
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            return
        L_0x0044:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXSDKEngine.initialize(android.app.Application, com.taobao.weex.InitConfig):void");
    }

    private static void registerApplicationOptions(Application application) {
        if (application == null) {
            WXLogUtils.e(TAG, "RegisterApplicationOptions application is null");
            return;
        }
        Resources resources = application.getResources();
        registerCoreEnv("screen_width_pixels", String.valueOf(resources.getDisplayMetrics().widthPixels));
        registerCoreEnv("screen_height_pixels", String.valueOf(resources.getDisplayMetrics().heightPixels));
        int identifier = resources.getIdentifier("status_bar_height", "dimen", WXEnvironment.OS);
        if (identifier > 0) {
            registerCoreEnv("status_bar_height", String.valueOf(resources.getDimensionPixelSize(identifier)));
        }
    }

    private static void doInitInternal(final Application application, final InitConfig initConfig) {
        WXEnvironment.sApplication = application;
        if (application == null) {
            WXLogUtils.e(TAG, " doInitInternal application is null");
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "doInitInternal", WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT.getErrorMsg() + "WXEnvironment sApplication is null", (Map<String, String>) null);
        }
        WXEnvironment.JsFrameworkInit = false;
        WXBridgeManager.getInstance().postWithName(new Runnable() {
            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                WXSDKManager instance = WXSDKManager.getInstance();
                instance.onSDKEngineInitialize();
                InitConfig initConfig = InitConfig.this;
                if (initConfig != null) {
                    instance.setInitConfig(initConfig);
                }
                WXSoInstallMgrSdk.init(application, instance.getIWXSoLoaderAdapter(), instance.getWXStatisticsListener());
                InitConfig initConfig2 = InitConfig.this;
                String str = null;
                IWXUserTrackAdapter utAdapter = initConfig2 != null ? initConfig2.getUtAdapter() : null;
                boolean unused = WXSDKEngine.mIsSoInit = WXSoInstallMgrSdk.initSo("weexcore", 1, utAdapter);
                WXSoInstallMgrSdk.copyJssRuntimeSo();
                InitConfig initConfig3 = InitConfig.this;
                if (initConfig3 != null) {
                    for (String initSo : initConfig3.getNativeLibraryList()) {
                        WXSoInstallMgrSdk.initSo(initSo, 1, utAdapter);
                    }
                }
                if (!WXSDKEngine.mIsSoInit) {
                    WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
                    WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "doInitInternal", WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT.getErrorMsg() + "isSoInit false", (Map<String, String>) null);
                    return;
                }
                InitConfig initConfig4 = InitConfig.this;
                if (initConfig4 != null) {
                    str = initConfig4.getFramework();
                }
                instance.initScriptsFramework(str);
                WXEnvironment.sSDKInitExecuteTime = System.currentTimeMillis() - currentTimeMillis;
                WXLogUtils.renderPerformanceLog("SDKInitExecuteTime", WXEnvironment.sSDKInitExecuteTime);
            }
        }, (WXSDKInstance) null, "doInitWeexSdkInternal");
        register();
    }

    @Deprecated
    public static void init(Application application, String str, IWXUserTrackAdapter iWXUserTrackAdapter, IWXImgLoaderAdapter iWXImgLoaderAdapter, IWXHttpAdapter iWXHttpAdapter) {
        initialize(application, new InitConfig.Builder().setUtAdapter(iWXUserTrackAdapter).setHttpAdapter(iWXHttpAdapter).setImgAdapter(iWXImgLoaderAdapter).build());
    }

    public static void setJSExcetptionAdapter(IWXJSExceptionAdapter iWXJSExceptionAdapter) {
        WXSDKManager.getInstance().setIWXJSExceptionAdapter(iWXJSExceptionAdapter);
    }

    private static void register() {
        BatchOperationHelper batchOperationHelper = new BatchOperationHelper(WXBridgeManager.getInstance());
        try {
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXText.class, new WXText.Creator()), false, "text");
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXDiv.class, new WXDiv.Ceator()), false, WXBasicComponentType.CONTAINER, WXBasicComponentType.DIV, WXBasicComponentType.HEADER, WXBasicComponentType.FOOTER);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXImage.class, new WXImage.Creator()), false, "image", WXBasicComponentType.IMG);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXScroller.class, new WXScroller.Creator()), false, WXBasicComponentType.SCROLLER);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXSlider.class, new WXSlider.Creator()), true, WXBasicComponentType.CYCLE_SLIDER);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXSliderNeighbor.class, new WXSliderNeighbor.Creator()), true, WXBasicComponentType.SLIDER_NEIGHBOR);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXCell.class, new WXCell.Creator()), true, WXBasicComponentType.CELL);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXListComponent.class, new WXListComponent.Creator()), true, WXBasicComponentType.LIST, WXBasicComponentType.VLIST, WXBasicComponentType.RECYCLER, WXBasicComponentType.WATERFALL);
            registerComponent((IFComponentHolder) new SimpleComponentHolder(WXRichText.class, new WXRichText.Creator()), false, WXBasicComponentType.RICHTEXT);
            registerComponent((Class<? extends WXComponent>) SimpleListComponent.class, false, "simplelist");
            registerComponent((Class<? extends WXComponent>) WXRecyclerTemplateList.class, false, WXBasicComponentType.RECYCLE_LIST);
            registerComponent((Class<? extends WXComponent>) HorizontalListComponent.class, false, WXBasicComponentType.HLIST);
            registerComponent(WXBasicComponentType.CELL_SLOT, (Class<? extends WXComponent>) WXCell.class, true);
            registerComponent(WXBasicComponentType.INDICATOR, (Class<? extends WXComponent>) WXIndicator.class, true);
            registerComponent("video", (Class<? extends WXComponent>) WXVideo.class, false);
            registerComponent("input", (Class<? extends WXComponent>) WXInput.class, false);
            registerComponent(WXBasicComponentType.TEXTAREA, (Class<? extends WXComponent>) Textarea.class, false);
            registerComponent("a", (Class<? extends WXComponent>) WXA.class, false);
            registerComponent(WXBasicComponentType.EMBED, (Class<? extends WXComponent>) WXEmbed.class, true);
            registerComponent("web", (Class<? extends WXComponent>) WXWeb.class);
            registerComponent("refresh", (Class<? extends WXComponent>) WXRefresh.class);
            registerComponent("loading", (Class<? extends WXComponent>) WXLoading.class);
            registerComponent(WXBasicComponentType.LOADING_INDICATOR, (Class<? extends WXComponent>) WXLoadingIndicator.class);
            registerComponent(WXBasicComponentType.HEADER, (Class<? extends WXComponent>) WXHeader.class);
            registerModule(AbsoluteConst.JSON_KEY_MODAL, WXModalUIModule.class);
            registerModule("instanceWrap", WXInstanceWrap.class);
            registerModule("animation", WXAnimationModule.class);
            registerModule("webview", WXWebViewModule.class);
            registerModule("navigator", WXNavigatorModule.class);
            registerModule(IApp.ConfigProperty.CONFIG_STREAM, WXStreamModule.class);
            registerModule("timer", WXTimerModule.class);
            registerModule("storage", WXStorageModule.class);
            registerModule("clipboard", WXClipboardModule.class);
            registerModule("globalEvent", WXGlobalEventModule.class);
            registerModule("picker", WXPickersModule.class);
            registerModule("meta", WXMetaModule.class);
            registerModule("webSocket", WebSocketModule.class);
            registerModule("locale", WXLocaleModule.class);
            registerModule("deviceInfo", WXDeviceInfoModule.class);
            registerModule("sdk-console-log", ConsoleLogModule.class);
        } catch (WXException e) {
            WXLogUtils.e("[WXSDKEngine] register:", (Throwable) e);
        }
        if (RegisterCache.getInstance().enableAutoScan()) {
            AutoScanConfigRegister.doScanConfig();
        }
        batchOperationHelper.flush();
    }

    public static boolean registerComponent(String str, Class<? extends WXComponent> cls, boolean z) throws WXException {
        return registerComponent(cls, z, str);
    }

    public static boolean registerComponent(String str, IExternalComponentGetter iExternalComponentGetter, boolean z) throws WXException {
        return registerComponent((IFComponentHolder) new ExternalLoaderComponentHolder(str, iExternalComponentGetter), z, str);
    }

    public static boolean registerComponent(Class<? extends WXComponent> cls, boolean z, String... strArr) throws WXException {
        if (cls == null) {
            return false;
        }
        return registerComponent((IFComponentHolder) new SimpleComponentHolder(cls), z, strArr);
    }

    public static boolean registerComponent(IFComponentHolder iFComponentHolder, boolean z, String... strArr) throws WXException {
        boolean z2 = true;
        try {
            int length = strArr.length;
            int i = 0;
            boolean z3 = true;
            while (i < length) {
                try {
                    String str = strArr[i];
                    HashMap hashMap = new HashMap();
                    if (z) {
                        hashMap.put("append", "tree");
                    }
                    z3 = z3 && WXComponentRegistry.registerComponent(str, iFComponentHolder, hashMap);
                    i++;
                } catch (Throwable th) {
                    th = th;
                    z2 = z3;
                    th.printStackTrace();
                    return z2;
                }
            }
            return z3;
        } catch (Throwable th2) {
            th = th2;
            th.printStackTrace();
            return z2;
        }
    }

    public static <T extends WXModule> boolean registerModule(String str, Class<T> cls, boolean z) throws WXException {
        return cls != null && registerModule(str, (ModuleFactory) new TypeModuleFactory(cls), z);
    }

    public static <T extends WXModule> boolean registerModuleWithFactory(String str, DestroyableModuleFactory destroyableModuleFactory, boolean z) throws WXException {
        return registerModule(str, (ModuleFactory) destroyableModuleFactory, z);
    }

    public static <T extends WXModule> boolean registerModuleWithFactory(String str, IExternalModuleGetter iExternalModuleGetter, boolean z) throws WXException {
        return registerModule(str, iExternalModuleGetter.getExternalModuleClass(str, WXEnvironment.getApplication()), z);
    }

    public static <T extends WXModule> boolean registerModule(String str, ModuleFactory moduleFactory, boolean z) throws WXException {
        return WXModuleManager.registerModule(str, moduleFactory, z);
    }

    public static boolean registerModule(String str, Class<? extends WXModule> cls) throws WXException {
        return registerModule(str, cls, false);
    }

    public static boolean registerService(String str, String str2, Map<String, Object> map) {
        return WXServiceManager.registerService(str, str2, map);
    }

    public static boolean unRegisterService(String str) {
        return WXServiceManager.unRegisterService(str);
    }

    public static abstract class DestroyableModuleFactory<T extends DestroyableModule> extends TypeModuleFactory<T> {
        public DestroyableModuleFactory(Class<T> cls) {
            super(cls);
        }
    }

    public static void callback(String str, String str2, Map<String, Object> map) {
        WXSDKManager.getInstance().callback(str, str2, map);
    }

    public static void restartBridge(boolean z) {
        WXEnvironment.sDebugMode = z;
        WXSDKManager.getInstance().restartBridge();
    }

    public static boolean registerComponent(String str, Class<? extends WXComponent> cls) throws WXException {
        return WXComponentRegistry.registerComponent(str, new SimpleComponentHolder(cls), new HashMap());
    }

    public static boolean registerComponent(Map<String, Object> map, Class<? extends WXComponent> cls) throws WXException {
        if (map == null) {
            return false;
        }
        String str = (String) map.get("type");
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return WXComponentRegistry.registerComponent(str, new SimpleComponentHolder(cls), map);
    }

    public static void addCustomOptions(String str, String str2) {
        WXEnvironment.addCustomOptions(str, str2);
    }

    public static IWXUserTrackAdapter getIWXUserTrackAdapter() {
        return WXSDKManager.getInstance().getIWXUserTrackAdapter();
    }

    public static IWXImgLoaderAdapter getIWXImgLoaderAdapter() {
        return WXSDKManager.getInstance().getIWXImgLoaderAdapter();
    }

    public static IDrawableLoader getDrawableLoader() {
        return WXSDKManager.getInstance().getDrawableLoader();
    }

    public static IWXHttpAdapter getIWXHttpAdapter() {
        return WXSDKManager.getInstance().getIWXHttpAdapter();
    }

    public static IWXStorageAdapter getIWXStorageAdapter() {
        return WXSDKManager.getInstance().getIWXStorageAdapter();
    }

    public static IWXJsFileLoaderAdapter getIWXJsFileLoaderAdapter() {
        return WXSDKManager.getInstance().getIWXJsFileLoaderAdapter();
    }

    public static IActivityNavBarSetter getActivityNavBarSetter() {
        return WXSDKManager.getInstance().getActivityNavBarSetter();
    }

    public static INavigator getNavigator() {
        return WXSDKManager.getInstance().getNavigator();
    }

    public static void setNavigator(INavigator iNavigator) {
        WXSDKManager.getInstance().setNavigator(iNavigator);
    }

    public static void setActivityNavBarSetter(IActivityNavBarSetter iActivityNavBarSetter) {
        WXSDKManager.getInstance().setActivityNavBarSetter(iActivityNavBarSetter);
    }

    public static void reload(final Context context, String str, boolean z) {
        WXEnvironment.sRemoteDebugMode = z;
        WXBridgeManager.getInstance().restart();
        WXBridgeManager.getInstance().initScriptsFramework(str);
        WXServiceManager.reload();
        WXModuleManager.reload();
        WXComponentRegistry.reload();
        WXSDKManager.getInstance().postOnUiThread(new Runnable() {
            public void run() {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(WXSDKEngine.JS_FRAMEWORK_RELOAD));
            }
        }, 0);
    }

    public static void reload(Context context, boolean z) {
        reload(context, (String) null, z);
    }

    public static void reload() {
        reload(WXEnvironment.getApplication(), WXEnvironment.sRemoteDebugMode);
    }

    public static void restartWeex() {
        WXEnvironment.sRemoteDebugMode = WXEnvironment.sRemoteDebugMode;
        WXBridgeManager.getInstance().restart();
        WXBridgeManager.getInstance().initScriptsFramework((String) null);
        WXServiceManager.reload();
        WXModuleManager.reload();
        WXComponentRegistry.reload();
    }

    public static void registerCoreEnv(String str, String str2) {
        WXBridgeManager.getInstance().registerCoreEnv(str, str2);
    }
}
