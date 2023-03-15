package com.taobao.weex.bridge;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.collection.ArrayMap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.Script;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.adapter.IWXJscProcessManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.bridge.WXValidateProcessor;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.IWXBridge;
import com.taobao.weex.common.IWXDebugConfig;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXJSExceptionInfo;
import com.taobao.weex.common.WXRefreshData;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.ui.WXComponentRegistry;
import com.taobao.weex.ui.WXRenderManager;
import com.taobao.weex.ui.action.ActionReloadPage;
import com.taobao.weex.ui.action.GraphicActionAddChildToRichtext;
import com.taobao.weex.ui.action.GraphicActionAddElement;
import com.taobao.weex.ui.action.GraphicActionAddEvent;
import com.taobao.weex.ui.action.GraphicActionAppendTreeCreateFinish;
import com.taobao.weex.ui.action.GraphicActionBatchBegin;
import com.taobao.weex.ui.action.GraphicActionBatchEnd;
import com.taobao.weex.ui.action.GraphicActionCreateBody;
import com.taobao.weex.ui.action.GraphicActionCreateFinish;
import com.taobao.weex.ui.action.GraphicActionLayout;
import com.taobao.weex.ui.action.GraphicActionMoveElement;
import com.taobao.weex.ui.action.GraphicActionRefreshFinish;
import com.taobao.weex.ui.action.GraphicActionRemoveChildFromRichtext;
import com.taobao.weex.ui.action.GraphicActionRemoveElement;
import com.taobao.weex.ui.action.GraphicActionRemoveEvent;
import com.taobao.weex.ui.action.GraphicActionRenderSuccess;
import com.taobao.weex.ui.action.GraphicActionUpdateAttr;
import com.taobao.weex.ui.action.GraphicActionUpdateRichtextAttr;
import com.taobao.weex.ui.action.GraphicActionUpdateRichtextStyle;
import com.taobao.weex.ui.action.GraphicActionUpdateStyle;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.module.WXDomModule;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXJsonUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import com.taobao.weex.utils.WXWsonJSONSwitch;
import com.taobao.weex.utils.batch.BactchExecutor;
import com.taobao.weex.utils.batch.Interceptor;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.common.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WXBridgeManager implements Handler.Callback, BactchExecutor {
    public static final String ARGS = "args";
    private static final boolean BRIDGE_LOG_SWITCH = false;
    private static final String BUNDLE_TYPE = "bundleType";
    public static final String COMPONENT = "component";
    private static final int CRASHREINIT = 50;
    private static String GLOBAL_CONFIG_KEY = "global_switch_config";
    public static final String INITLOGFILE = "/jsserver_start.log";
    private static final int INIT_FRAMEWORK_OK = 1;
    public static final String KEY_ARGS = "args";
    public static final String KEY_METHOD = "method";
    public static final String KEY_PARAMS = "params";
    private static long LOW_MEM_VALUE = 120;
    public static final String METHD_COMPONENT_HOOK_SYNC = "componentHook";
    public static final String METHD_FIRE_EVENT_SYNC = "fireEventSync";
    public static final String METHOD = "method";
    public static final String METHOD_CALLBACK = "callback";
    public static final String METHOD_CALL_JS = "callJS";
    public static final String METHOD_CHECK_APPKEY = "dc_checkappkey";
    public static final String METHOD_CREATE_INSTANCE = "createInstance";
    public static final String METHOD_CREATE_INSTANCE_CONTEXT = "createInstanceContext";
    public static final String METHOD_CREATE_PAGE_WITH_CONTENT = "CreatePageWithContent";
    public static final String METHOD_DESTROY_INSTANCE = "destroyInstance";
    public static final String METHOD_FIRE_EVENT = "fireEvent";
    public static final String METHOD_FIRE_EVENT_ON_DATA_RENDER_NODE = "fireEventOnDataRenderNode";
    private static final String METHOD_JSFM_NOT_INIT_IN_EAGLE_MODE = "JsfmNotInitInEagleMode";
    public static final String METHOD_NOTIFY_SERIALIZE_CODE_CACHE = "notifySerializeCodeCache";
    public static final String METHOD_NOTIFY_TRIM_MEMORY = "notifyTrimMemory";
    private static final String METHOD_POST_TASK_TO_MSG_LOOP = "PostTaskToMsgLoop";
    public static final String METHOD_REFRESH_INSTANCE = "refreshInstance";
    public static final String METHOD_REGISTER_COMPONENTS = "registerComponents";
    public static final String METHOD_REGISTER_MODULES = "registerModules";
    public static final String METHOD_SET_TIMEOUT = "setTimeoutCallback";
    public static final String METHOD_UPDATE_COMPONENT_WITH_DATA = "UpdateComponentData";
    public static final String MODULE = "module";
    private static final String NON_CALLBACK = "-1";
    public static final String OPTIONS = "options";
    public static final String REF = "ref";
    private static final String RENDER_STRATEGY = "renderStrategy";
    private static final String UNDEFINED = "undefined";
    private static Class clazz_debugProxy = null;
    private static String crashUrl = null;
    /* access modifiers changed from: private */
    public static String globalConfig = "none";
    private static volatile boolean isJsEngineMultiThreadEnable = false;
    /* access modifiers changed from: private */
    public static volatile boolean isSandBoxContext = true;
    private static boolean isUseSingleProcess = false;
    private static long lastCrashTime = 0;
    static volatile WXBridgeManager mBridgeManager = null;
    private static volatile boolean mInit = false;
    private static String mRaxApi = null;
    private static Map<String, String> mWeexCoreEnvOptions = new HashMap();
    public static volatile int reInitCount = 1;
    public static StringBuilder sInitFrameWorkMsg = new StringBuilder();
    public static long sInitFrameWorkTimeOrigin;
    private HashSet<String> mDestroyedInstanceId = new HashSet<>();
    private WXParams mInitParams;
    private Interceptor mInterceptor;
    Handler mJSHandler;
    private WXThread mJSThread;
    private StringBuilder mLodBuilder = new StringBuilder(50);
    private boolean mMock = false;
    /* access modifiers changed from: private */
    public WXHashMap<String, ArrayList<WXHashMap<String, Object>>> mNextTickTasks = new WXHashMap<>();
    /* access modifiers changed from: private */
    public List<Map<String, Object>> mRegisterComponentFailList = new ArrayList(8);
    /* access modifiers changed from: private */
    public List<Map<String, Object>> mRegisterModuleFailList = new ArrayList(8);
    /* access modifiers changed from: private */
    public List<String> mRegisterServiceFailList = new ArrayList(8);
    /* access modifiers changed from: private */
    public IWXBridge mWXBridge;
    private Object mWxDebugProxy;

    public enum BundType {
        Vue,
        Rax,
        Others
    }

    public static class TimerInfo {
        public String callbackId;
        public String instanceId;
        public long time;
    }

    private void mock(String str) {
    }

    @Deprecated
    public void notifyTrimMemory() {
    }

    private WXBridgeManager() {
        initWXBridge(WXEnvironment.sRemoteDebugMode);
        WXThread wXThread = new WXThread("WeexJSBridgeThread", (Handler.Callback) this);
        this.mJSThread = wXThread;
        this.mJSHandler = wXThread.getHandler();
    }

    public static WXBridgeManager getInstance() {
        if (mBridgeManager == null) {
            synchronized (WXBridgeManager.class) {
                if (mBridgeManager == null) {
                    mBridgeManager = new WXBridgeManager();
                }
            }
        }
        return mBridgeManager;
    }

    public void setUseSingleProcess(boolean z) {
        if (z != isUseSingleProcess) {
            isUseSingleProcess = z;
        }
    }

    public void onInteractionTimeUpdate(final String str) {
        post(new Runnable() {
            public void run() {
                if (WXBridgeManager.this.mWXBridge instanceof WXBridge) {
                    ((WXBridge) WXBridgeManager.this.mWXBridge).nativeOnInteractionTimeUpdate(str);
                }
            }
        });
    }

    public boolean jsEngineMultiThreadEnable() {
        return isJsEngineMultiThreadEnable;
    }

    public void checkJsEngineMultiThread() {
        IWXJscProcessManager wXJscProcessManager = WXSDKManager.getInstance().getWXJscProcessManager();
        boolean enableBackupThread = wXJscProcessManager != null ? wXJscProcessManager.enableBackupThread() : false;
        if (enableBackupThread != isJsEngineMultiThreadEnable) {
            isJsEngineMultiThreadEnable = enableBackupThread;
            if (!isJSFrameworkInit()) {
                return;
            }
            if (isJSThread()) {
                WXSDKEngine.reload();
            } else {
                post(new Runnable() {
                    public void run() {
                        WXSDKEngine.reload();
                    }
                });
            }
        }
    }

    public void setSandBoxContext(boolean z) {
        String str;
        if (z != isSandBoxContext) {
            isSandBoxContext = z;
            if (isJSThread()) {
                setJSFrameworkInit(false);
                WXModuleManager.resetAllModuleState();
                if (!isSandBoxContext) {
                    str = WXFileUtils.loadAsset("main.js", WXEnvironment.getApplication());
                } else {
                    str = WXFileUtils.loadAsset("weex-main-jsfm.js", WXEnvironment.getApplication());
                }
                initFramework(str);
                WXServiceManager.reload();
                WXModuleManager.reload();
                WXComponentRegistry.reload();
                return;
            }
            post(new Runnable() {
                public void run() {
                    String str;
                    WXBridgeManager.this.setJSFrameworkInit(false);
                    WXModuleManager.resetAllModuleState();
                    if (!WXBridgeManager.isSandBoxContext) {
                        str = WXFileUtils.loadAsset("main.js", WXEnvironment.getApplication());
                    } else {
                        str = WXFileUtils.loadAsset("weex-main-jsfm.js", WXEnvironment.getApplication());
                    }
                    WXBridgeManager.this.initFramework(str);
                    WXServiceManager.reload();
                    WXModuleManager.reload();
                    WXComponentRegistry.reload();
                }
            });
        }
    }

    public boolean isJSFrameworkInit() {
        return mInit;
    }

    /* access modifiers changed from: private */
    public void setJSFrameworkInit(boolean z) {
        mInit = z;
        if (z) {
            onJsFrameWorkInitSuccees();
        }
    }

    private void initWXBridge(boolean z) {
        Method method;
        boolean z2;
        Constructor constructor;
        Method method2;
        if (z && WXEnvironment.isApkDebugable()) {
            WXEnvironment.sDebugServerConnectable = true;
        }
        if (WXEnvironment.sDebugServerConnectable) {
            WXEnvironment.isApkDebugable();
            if (WXEnvironment.getApplication() != null) {
                try {
                    if (clazz_debugProxy == null) {
                        clazz_debugProxy = Class.forName("com.taobao.weex.devtools.debug.DebugServerProxy");
                    }
                    Class cls = clazz_debugProxy;
                    if (cls != null) {
                        if (this.mWxDebugProxy != null) {
                            Method method3 = cls.getMethod("isActive", new Class[0]);
                            if (method3 != null ? ((Boolean) method3.invoke(this.mWxDebugProxy, new Object[0])).booleanValue() : false) {
                                z2 = false;
                                if (z2 && (constructor = clazz_debugProxy.getConstructor(new Class[]{Context.class, IWXDebugConfig.class})) != null) {
                                    Object newInstance = constructor.newInstance(new Object[]{WXEnvironment.getApplication(), new IWXDebugConfig() {
                                        public WXBridgeManager getWXJSManager() {
                                            return WXBridgeManager.this;
                                        }

                                        public WXDebugJsBridge getWXDebugJsBridge() {
                                            return new WXDebugJsBridge();
                                        }
                                    }});
                                    this.mWxDebugProxy = newInstance;
                                    if (!(newInstance == null || (method2 = clazz_debugProxy.getMethod("start", new Class[0])) == null)) {
                                        method2.invoke(this.mWxDebugProxy, new Object[0]);
                                    }
                                }
                            }
                        }
                        z2 = true;
                        Object newInstance2 = constructor.newInstance(new Object[]{WXEnvironment.getApplication(), new IWXDebugConfig() {
                            public WXBridgeManager getWXJSManager() {
                                return WXBridgeManager.this;
                            }

                            public WXDebugJsBridge getWXDebugJsBridge() {
                                return new WXDebugJsBridge();
                            }
                        }});
                        this.mWxDebugProxy = newInstance2;
                        method2.invoke(this.mWxDebugProxy, new Object[0]);
                    }
                } catch (Throwable unused) {
                }
                WXServiceManager.execAllCacheJsService();
            } else {
                WXLogUtils.e("WXBridgeManager", "WXEnvironment.sApplication is null, skip init Inspector");
            }
        }
        if (!z || this.mWxDebugProxy == null) {
            this.mWXBridge = new WXBridge();
            return;
        }
        try {
            if (clazz_debugProxy == null) {
                clazz_debugProxy = Class.forName("com.taobao.weex.devtools.debug.DebugServerProxy");
            }
            Class cls2 = clazz_debugProxy;
            if (cls2 != null && (method = cls2.getMethod("getWXBridge", new Class[0])) != null) {
                this.mWXBridge = (IWXBridge) method.invoke(this.mWxDebugProxy, new Object[0]);
            }
        } catch (Throwable unused2) {
        }
    }

    public String dumpIpcPageInfo() {
        IWXBridge iWXBridge = this.mWXBridge;
        return iWXBridge instanceof WXBridge ? ((WXBridge) iWXBridge).nativeDumpIpcPageQueueInfo() : "";
    }

    public void stopRemoteDebug() {
        Method method;
        if (this.mWxDebugProxy != null) {
            try {
                if (clazz_debugProxy == null) {
                    clazz_debugProxy = Class.forName("com.taobao.weex.devtools.debug.DebugServerProxy");
                }
                Class cls = clazz_debugProxy;
                if (cls != null && (method = cls.getMethod(Constants.Value.STOP, new Class[]{Boolean.TYPE})) != null) {
                    method.invoke(this.mWxDebugProxy, new Object[]{false});
                    this.mWxDebugProxy = null;
                }
            } catch (Throwable unused) {
            }
        }
    }

    public Object callModuleMethod(String str, String str2, String str3, JSONArray jSONArray) {
        return callModuleMethod(str, str2, str3, jSONArray, (JSONObject) null);
    }

    public Object callModuleMethod(String str, String str2, String str3, JSONArray jSONArray, JSONObject jSONObject) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return null;
        }
        if (!sDKInstance.isNeedValidate() || WXSDKManager.getInstance().getValidateProcessor() == null) {
            try {
                return WXModuleManager.callModuleMethod(str, str2, str3, jSONArray);
            } catch (NumberFormatException unused) {
                ArrayMap arrayMap = new ArrayMap();
                arrayMap.put("moduleName", str2);
                arrayMap.put("methodName", str3);
                arrayMap.put("args", jSONArray.toJSONString());
                WXLogUtils.e("[WXBridgeManager] callNative : numberFormatException when parsing string to numbers in args", arrayMap.toString());
                return null;
            }
        } else {
            WXValidateProcessor.WXModuleValidateResult onModuleValidate = WXSDKManager.getInstance().getValidateProcessor().onModuleValidate(sDKInstance, str2, str3, jSONArray, jSONObject);
            if (onModuleValidate == null) {
                return null;
            }
            if (onModuleValidate.isSuccess) {
                return WXModuleManager.callModuleMethod(str, str2, str3, jSONArray);
            }
            JSONObject jSONObject2 = onModuleValidate.validateInfo;
            if (jSONObject2 != null) {
                WXLogUtils.e("[WXBridgeManager] module validate fail. >>> " + jSONObject2.toJSONString());
            }
            return jSONObject2;
        }
    }

    public void restart() {
        setJSFrameworkInit(false);
        WXModuleManager.resetAllModuleState();
        initWXBridge(WXEnvironment.sRemoteDebugMode);
        this.mWXBridge.resetWXBridge(WXEnvironment.sRemoteDebugMode);
    }

    public synchronized void setStackTopInstance(final String str) {
        post(new Runnable() {
            public void run() {
                WXBridgeManager.this.mNextTickTasks.setStackTopInstance(str);
            }
        }, str, (WXSDKInstance) null, (String) null);
    }

    public void post(Runnable runnable) {
        postWithName(runnable, (WXSDKInstance) null, (String) null);
    }

    public void postWithName(Runnable runnable, WXSDKInstance wXSDKInstance, String str) {
        Handler handler;
        Runnable secure = WXThread.secure(runnable, wXSDKInstance, str);
        Interceptor interceptor = this.mInterceptor;
        if ((interceptor == null || !interceptor.take(secure)) && (handler = this.mJSHandler) != null) {
            handler.post(secure);
        }
    }

    public void setInterceptor(Interceptor interceptor) {
        this.mInterceptor = interceptor;
    }

    public void post(Runnable runnable, Object obj, WXSDKInstance wXSDKInstance, String str) {
        Handler handler = this.mJSHandler;
        if (handler != null) {
            Message obtain = Message.obtain(handler, WXThread.secure(runnable, wXSDKInstance, str));
            obtain.obj = obj;
            obtain.sendToTarget();
        }
    }

    public void post(Runnable runnable, Object obj) {
        post(runnable, obj, (WXSDKInstance) null, (String) null);
    }

    public void postDelay(Runnable runnable, long j) {
        Handler handler = this.mJSHandler;
        if (handler != null) {
            handler.postDelayed(WXThread.secure(runnable), j);
        }
    }

    public void setLogLevel(final int i, final boolean z) {
        post(new Runnable() {
            public void run() {
                if (WXBridgeManager.this.mWXBridge != null) {
                    WXBridgeManager.this.mWXBridge.setLogType((float) i, z);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setTimeout(String str, String str2) {
        Message obtain = Message.obtain();
        obtain.what = 1;
        TimerInfo timerInfo = new TimerInfo();
        timerInfo.callbackId = str;
        timerInfo.time = (long) Float.parseFloat(str2);
        obtain.obj = timerInfo;
        this.mJSHandler.sendMessageDelayed(obtain, timerInfo.time);
    }

    public void sendMessageDelayed(Message message, long j) {
        WXThread wXThread;
        if (message != null && this.mJSHandler != null && (wXThread = this.mJSThread) != null && wXThread.isWXThreadAlive() && this.mJSThread.getLooper() != null) {
            this.mJSHandler.sendMessageDelayed(message, j);
        }
    }

    public void removeMessage(int i, Object obj) {
        WXThread wXThread;
        if (this.mJSHandler != null && (wXThread = this.mJSThread) != null && wXThread.isWXThreadAlive() && this.mJSThread.getLooper() != null) {
            this.mJSHandler.removeMessages(i, obj);
        }
    }

    public Object callNativeModule(String str, String str2, String str3, JSONArray jSONArray, Object obj) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callNativeModule arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNativeModule", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        try {
            if (WXDomModule.WXDOM.equals(str2)) {
                return WXModuleManager.getDomModule(str).callDomMethod(str3, jSONArray, new long[0]);
            }
            return callModuleMethod(str, str2, str3, jSONArray);
        } catch (Exception e) {
            String str4 = "[WXBridgeManager] callNative exception: " + WXLogUtils.getStackTrace(e);
            WXLogUtils.e(str4);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callNativeModule", str4, (Map<String, String>) null);
            return null;
        }
    }

    public Object callNativeModule(String str, String str2, String str3, JSONArray jSONArray, JSONObject jSONObject) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callNativeModule arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNativeModule", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "callNativeModule:" + str2 + Operators.DOT_STR + str3);
        WXEnvironment.isApkDebugable();
        try {
            if (!WXDomModule.WXDOM.equals(str2)) {
                return callModuleMethod(str, str2, str3, jSONArray, jSONObject);
            }
            WXDomModule domModule = WXModuleManager.getDomModule(str);
            if (domModule != null) {
                return domModule.callDomMethod(str3, jSONArray, new long[0]);
            }
            WXModuleManager.createDomModule(WXSDKManager.getInstance().getSDKInstance(str));
            return null;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callNativeModule exception: " + WXLogUtils.getStackTrace(e));
        }
    }

    public Object callNativeComponent(String str, String str2, String str3, JSONArray jSONArray, Object obj) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callNativeComponent arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNativeComponent", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        try {
            WXDomModule domModule = WXModuleManager.getDomModule(str);
            if (domModule != null) {
                domModule.invokeMethod(str2, str3, jSONArray);
            } else {
                WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
                if (sDKInstance == null || !sDKInstance.isDestroy()) {
                    WXLogUtils.e("WXBridgeManager", "callNativeComponent exception :null == dom ,method:" + str3);
                }
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callNativeComponent exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callNativeComponent", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        return null;
    }

    public int callNative(String str, JSONArray jSONArray, String str2) {
        int i;
        int i2;
        String str3 = str;
        JSONArray jSONArray2 = jSONArray;
        String str4 = str2;
        if (TextUtils.isEmpty(str) || jSONArray2 == null) {
            WXLogUtils.d("[WXBridgeManager] call callNative arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str3, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callNative", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str3)) {
            return -1;
        }
        long nanoTime = System.nanoTime() - System.nanoTime();
        if (jSONArray2 != null && jSONArray.size() > 0) {
            int size = jSONArray.size();
            int i3 = 0;
            while (i3 < size) {
                try {
                    JSONObject jSONObject = (JSONObject) jSONArray2.get(i3);
                    if (!(jSONObject == null || WXSDKManager.getInstance().getSDKInstance(str3) == null)) {
                        Object obj = jSONObject.get("module");
                        if (obj == null) {
                            i2 = i3;
                            i = size;
                            if (jSONObject.get(COMPONENT) != null) {
                                WXModuleManager.getDomModule(str).invokeMethod((String) jSONObject.get("ref"), (String) jSONObject.get("method"), (JSONArray) jSONObject.get("args"));
                            } else {
                                throw new IllegalArgumentException("unknown callNative");
                            }
                        } else if (WXDomModule.WXDOM.equals(obj)) {
                            WXModuleManager.getDomModule(str).callDomMethod(jSONObject, nanoTime);
                        } else {
                            JSONObject jSONObject2 = jSONObject.getJSONObject("options");
                            i2 = i3;
                            i = size;
                            callModuleMethod(str, (String) obj, (String) jSONObject.get("method"), (JSONArray) jSONObject.get("args"), jSONObject2);
                        }
                        i3 = i2 + 1;
                        size = i;
                    }
                    i2 = i3;
                    i = size;
                    i3 = i2 + 1;
                    size = i;
                } catch (Exception e) {
                    WXLogUtils.e("[WXBridgeManager] callNative exception: ", (Throwable) e);
                    WXExceptionUtils.commitCriticalExceptionRT(str3, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callNative", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
                }
            }
        }
        if ("undefined".equals(str4) || NON_CALLBACK.equals(str4)) {
            return 0;
        }
        getNextTick(str3, str4);
        return 1;
    }

    public int callUpdateFinish(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callUpdateFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKManager.getInstance().getSDKInstance(str);
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        if (str2 == null || str2.isEmpty() || "undefined".equals(str2) || NON_CALLBACK.equals(str2)) {
            return 0;
        }
        getNextTick(str, str2);
        return 1;
    }

    public int callRefreshFinish(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callRefreshFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRefreshFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionRefreshFinish(sDKInstance));
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRefreshFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRefreshFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        if ("undefined".equals(str2) || NON_CALLBACK.equals(str2)) {
            return 0;
        }
        getNextTick(str, str2);
        return 1;
    }

    public int callReportCrashReloadPage(String str, String str2) {
        String str3;
        boolean isEmpty = TextUtils.isEmpty(str2);
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                String bundleUrl = sDKInstance.getBundleUrl();
                sDKInstance.setHasException(true);
                str3 = bundleUrl;
            } else {
                str3 = null;
            }
            HashMap hashMap = new HashMap(2);
            hashMap.put("weexCoreThreadStackTrace:", getInstance().getWeexCoreThreadStackTrace());
            hashMap.put("wxStateInfo", WXStateRecord.getInstance().getStateInfo().toString());
            String str4 = "null";
            if (!isEmpty) {
                try {
                    if (WXEnvironment.getApplication() != null) {
                        str2 = this.mInitParams.getCrashFilePath() + str2;
                        Log.d("jsengine", "callReportCrashReloadPage crashFile:" + str2);
                    }
                } catch (Throwable th) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(th));
                }
                WXStateRecord instance = WXStateRecord.getInstance();
                if (!TextUtils.isEmpty(str)) {
                    str4 = str;
                }
                instance.onJSCCrash(str4);
                callReportCrash(str2, str, str3, hashMap);
            } else {
                WXStateRecord instance2 = WXStateRecord.getInstance();
                if (!TextUtils.isEmpty(str)) {
                    str4 = str;
                }
                instance2.onJSEngineReload(str4);
                commitJscCrashAlarmMonitor(IWXUserTrackAdapter.JS_BRIDGE, WXErrorCode.WX_ERR_RELOAD_PAGE, "reboot jsc Engine", str, str3, hashMap);
            }
            if (reInitCount > 50) {
                WXExceptionUtils.commitCriticalExceptionRT("jsEngine", WXErrorCode.WX_ERR_RELOAD_PAGE_EXCEED_LIMIT, "callReportCrashReloadPage", "reInitCount:" + reInitCount, hashMap);
                return 0;
            }
            reInitCount++;
            setJSFrameworkInit(false);
            WXModuleManager.resetAllModuleState();
            initScriptsFramework("");
            HashSet<String> hashSet = this.mDestroyedInstanceId;
            if (hashSet != null && hashSet.contains(str)) {
                return -1;
            }
            try {
                if (WXSDKManager.getInstance().getSDKInstance(str) != null) {
                    new ActionReloadPage(str, shouldReloadCurrentInstance(WXSDKManager.getInstance().getSDKInstance(str).getBundleUrl())).executeAction();
                }
            } catch (Exception e) {
                WXLogUtils.e("[WXBridgeManager] callReloadPage exception: ", (Throwable) e);
                WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callReportCrashReloadPage", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            }
            return 0;
        } catch (Exception e2) {
            WXLogUtils.e("[WXBridgeManager] callReportCrashReloadPage exception: ", (Throwable) e2);
        }
    }

    public boolean shouldReloadCurrentInstance(String str) {
        Uri parse;
        long currentTimeMillis = System.currentTimeMillis();
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter != null) {
            boolean parseBoolean = Boolean.parseBoolean(wxConfigAdapter.getConfig("android_weex_ext_config", "check_biz_url", AbsoluteConst.TRUE));
            WXLogUtils.e("check_biz_url : " + parseBoolean);
            if (parseBoolean && !TextUtils.isEmpty(str) && (parse = Uri.parse(str)) != null) {
                str = parse.buildUpon().clearQuery().build().toString();
            }
        }
        String str2 = crashUrl;
        if (str2 == null || ((str2 != null && !str2.equals(str)) || currentTimeMillis - lastCrashTime > 15000)) {
            crashUrl = str;
            lastCrashTime = currentTimeMillis;
            return true;
        }
        lastCrashTime = currentTimeMillis;
        return false;
    }

    public void callReportCrash(String str, String str2, String str3, Map<String, String> map) {
        final String str4 = str + Operators.DOT_STR + new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        File file = new File(str);
        File file2 = new File(str4);
        if (file.exists()) {
            file.renameTo(file2);
        }
        final String str5 = str2;
        final String str6 = str3;
        final Map<String, String> map2 = map;
        new Thread(new Runnable() {
            public void run() {
                try {
                    File file = new File(str4);
                    if (file.exists()) {
                        if (file.length() > 0) {
                            StringBuilder sb = new StringBuilder();
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(str4));
                            while (true) {
                                String readLine = bufferedReader.readLine();
                                if (readLine == null) {
                                    break;
                                } else if (!"".equals(readLine)) {
                                    sb.append(readLine + "\n");
                                }
                            }
                            WXBridgeManager.this.commitJscCrashAlarmMonitor(IWXUserTrackAdapter.JS_BRIDGE, WXErrorCode.WX_ERR_JSC_CRASH, sb.toString(), str5, str6, map2);
                            bufferedReader.close();
                        } else {
                            WXLogUtils.e("[WXBridgeManager] callReportCrash crash file is empty");
                        }
                        if (!WXEnvironment.isApkDebugable()) {
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(e));
                } catch (Throwable th) {
                    WXLogUtils.e("[WXBridgeManager] callReportCrash exception: ", th);
                }
            }
        }).start();
    }

    private void getNextTick(String str, String str2) {
        addJSTask(METHOD_CALLBACK, str, str2, "{}");
        sendMessage(str, 6);
    }

    private void getNextTick(String str) {
        addJSTask(METHOD_CALLBACK, str, "", "{}");
        sendMessage(str, 6);
    }

    public String syncExecJsOnInstanceWithResult(String str, String str2, int i) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AnonymousClass8 r2 = new EventResult() {
            public void onCallback(Object obj) {
                super.onCallback(obj);
                countDownLatch.countDown();
            }
        };
        try {
            execJSOnInstance(r2, str, str2, i);
            countDownLatch.await(100, TimeUnit.MILLISECONDS);
            if (r2.getResult() != null) {
                return r2.getResult().toString();
            }
            return "";
        } catch (Throwable th) {
            WXLogUtils.e("syncCallExecJsOnInstance", th);
            return "";
        }
    }

    public void loadJsBundleInPreInitMode(final String str, final String str2) {
        post(new Runnable() {
            public void run() {
                String unused = WXBridgeManager.this.invokeExecJSOnInstance(str, str2, -1);
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
                if (wXSDKInstance != null && wXSDKInstance.isPreInitMode()) {
                    wXSDKInstance.getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_END);
                    wXSDKInstance.getApmForInstance().onStageWithTime(WXInstanceApm.KEY_PAGE_STAGES_END_EXCUTE_BUNDLE, WXUtils.getFixUnixTime() + 600);
                }
            }
        });
    }

    public EventResult syncCallJSEventWithResult(String str, String str2, List<Object> list, Object... objArr) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AnonymousClass10 r1 = new EventResult() {
            public void onCallback(Object obj) {
                super.onCallback(obj);
                countDownLatch.countDown();
            }
        };
        try {
            asyncCallJSEventWithResult(r1, str, str2, list, objArr);
            countDownLatch.await(100, TimeUnit.MILLISECONDS);
            return r1;
        } catch (Exception e) {
            WXLogUtils.e("syncCallJSEventWithResult", (Throwable) e);
            return r1;
        }
    }

    public void asyncCallJSEventVoidResult(String str, String str2, List<Object> list, Object... objArr) {
        final Object[] objArr2 = objArr;
        final List<Object> list2 = list;
        final String str3 = str;
        final String str4 = str2;
        post(new Runnable() {
            public void run() {
                try {
                    Object[] objArr = objArr2;
                    if (objArr == null) {
                        return;
                    }
                    if (objArr.length != 0) {
                        ArrayList arrayList = new ArrayList();
                        for (Object add : objArr2) {
                            arrayList.add(add);
                        }
                        if (list2 != null) {
                            ArrayMap arrayMap = new ArrayMap(4);
                            arrayMap.put("params", list2);
                            arrayList.add(arrayMap);
                        }
                        WXHashMap wXHashMap = new WXHashMap();
                        wXHashMap.put("method", str3);
                        wXHashMap.put("args", arrayList);
                        WXJSObject[] wXJSObjectArr = {new WXJSObject(2, str4), WXWsonJSONSwitch.toWsonOrJsonWXJSObject(new Object[]{wXHashMap})};
                        WXBridgeManager.this.invokeExecJS(String.valueOf(str4), (String) null, WXBridgeManager.METHOD_CALL_JS, wXJSObjectArr, true);
                        wXJSObjectArr[0] = null;
                    }
                } catch (Exception e) {
                    WXLogUtils.e("asyncCallJSEventVoidResult", (Throwable) e);
                }
            }
        });
    }

    private void asyncCallJSEventWithResult(EventResult eventResult, String str, String str2, List<Object> list, Object... objArr) {
        final Object[] objArr2 = objArr;
        final List<Object> list2 = list;
        final String str3 = str;
        final String str4 = str2;
        final EventResult eventResult2 = eventResult;
        post(new Runnable() {
            public void run() {
                try {
                    Object[] objArr = objArr2;
                    if (objArr == null) {
                        return;
                    }
                    if (objArr.length != 0) {
                        ArrayList arrayList = new ArrayList();
                        for (Object add : objArr2) {
                            arrayList.add(add);
                        }
                        if (list2 != null) {
                            ArrayMap arrayMap = new ArrayMap(4);
                            arrayMap.put("params", list2);
                            arrayList.add(arrayMap);
                        }
                        WXHashMap wXHashMap = new WXHashMap();
                        wXHashMap.put("method", str3);
                        wXHashMap.put("args", arrayList);
                        WXJSObject[] wXJSObjectArr = {new WXJSObject(2, str4), WXWsonJSONSwitch.toWsonOrJsonWXJSObject(new Object[]{wXHashMap})};
                        WXBridgeManager.this.invokeExecJSWithCallback(String.valueOf(str4), (String) null, WXBridgeManager.METHOD_CALL_JS, wXJSObjectArr, eventResult2 != null ? new ResultCallback<byte[]>() {
                            public void onReceiveResult(byte[] bArr) {
                                JSONArray jSONArray = (JSONArray) WXWsonJSONSwitch.parseWsonOrJSON(bArr);
                                if (jSONArray != null && jSONArray.size() > 0) {
                                    eventResult2.onCallback(jSONArray.get(0));
                                }
                            }
                        } : null, true);
                        wXJSObjectArr[0] = null;
                    }
                } catch (Exception e) {
                    WXLogUtils.e("asyncCallJSEventWithResult", (Throwable) e);
                }
            }
        });
    }

    private void addJSEventTask(String str, String str2, List<Object> list, Object... objArr) {
        final Object[] objArr2 = objArr;
        final List<Object> list2 = list;
        final String str3 = str;
        final String str4 = str2;
        post(new Runnable() {
            public void run() {
                Object[] objArr = objArr2;
                if (objArr != null && objArr.length != 0) {
                    ArrayList arrayList = new ArrayList();
                    for (Object add : objArr2) {
                        arrayList.add(add);
                    }
                    if (list2 != null) {
                        ArrayMap arrayMap = new ArrayMap(4);
                        arrayMap.put("params", list2);
                        arrayList.add(arrayMap);
                    }
                    WXHashMap wXHashMap = new WXHashMap();
                    wXHashMap.put("method", str3);
                    wXHashMap.put("args", arrayList);
                    if (WXBridgeManager.this.mNextTickTasks.get(str4) == null) {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(wXHashMap);
                        WXBridgeManager.this.mNextTickTasks.put(str4, arrayList2);
                        return;
                    }
                    ((ArrayList) WXBridgeManager.this.mNextTickTasks.get(str4)).add(wXHashMap);
                }
            }
        });
    }

    private void addJSTask(String str, String str2, Object... objArr) {
        addJSEventTask(str, str2, (List<Object>) null, objArr);
    }

    private void sendMessage(String str, int i) {
        Message obtain = Message.obtain(this.mJSHandler);
        obtain.obj = str;
        obtain.what = i;
        obtain.sendToTarget();
    }

    public synchronized void initScriptsFramework(String str) {
        Message obtainMessage = this.mJSHandler.obtainMessage();
        obtainMessage.obj = str;
        obtainMessage.what = 7;
        obtainMessage.setTarget(this.mJSHandler);
        obtainMessage.sendToTarget();
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3, Map<String, Object> map) {
        fireEvent(str, str2, str3, map, (Map<String, Object>) null);
    }

    @Deprecated
    public void fireEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        fireEventOnNode(str, str2, str3, map, map2);
    }

    public void fireEventOnNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        fireEventOnNode(str, str2, str3, map, map2, (List<Object>) null, (EventResult) null);
    }

    public void fireEventOnNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2, List<Object> list) {
        fireEventOnNode(str, str2, str3, map, map2, list, (EventResult) null);
    }

    public void fireEventOnNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2, List<Object> list, EventResult eventResult) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && this.mJSHandler != null) {
            if (checkMainThread()) {
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
                if (wXSDKInstance != null && (wXSDKInstance.getRenderStrategy() == WXRenderStrategy.DATA_RENDER || wXSDKInstance.getRenderStrategy() == WXRenderStrategy.DATA_RENDER_BINARY)) {
                    fireEventOnDataRenderNode(str, str2, str3, map, map2);
                } else if (eventResult == null) {
                    addJSEventTask(METHOD_FIRE_EVENT, str, list, str2, str3, map, map2);
                    sendMessage(str, 6);
                } else {
                    asyncCallJSEventWithResult(eventResult, METHD_FIRE_EVENT_SYNC, str, list, str2, str3, map, map2);
                }
            } else {
                throw new WXRuntimeException("fireEvent must be called by main thread");
            }
        }
    }

    private void fireEventOnDataRenderNode(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        final String str4 = str;
        final Map<String, Object> map3 = map;
        final String str5 = str2;
        final String str6 = str3;
        final Map<String, Object> map4 = map2;
        this.mJSHandler.postDelayed(WXThread.secure((Runnable) new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:18:0x0068 A[Catch:{ all -> 0x0083 }] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r10 = this;
                    java.lang.String r0 = "fireEventOnDataRenderNode"
                    com.taobao.weex.WXSDKManager r1 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x0083 }
                    java.lang.String r2 = r3     // Catch:{ all -> 0x0083 }
                    r1.getSDKInstance(r2)     // Catch:{ all -> 0x0083 }
                    long r1 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0083 }
                    boolean r3 = com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ all -> 0x0083 }
                    if (r3 == 0) goto L_0x0035
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0083 }
                    r3.<init>()     // Catch:{ all -> 0x0083 }
                    java.lang.String r4 = "fireEventOnDataRenderNode >>>> instanceId:"
                    r3.append(r4)     // Catch:{ all -> 0x0083 }
                    java.lang.String r4 = r3     // Catch:{ all -> 0x0083 }
                    r3.append(r4)     // Catch:{ all -> 0x0083 }
                    java.lang.String r4 = ", data:"
                    r3.append(r4)     // Catch:{ all -> 0x0083 }
                    java.util.Map r4 = r4     // Catch:{ all -> 0x0083 }
                    r3.append(r4)     // Catch:{ all -> 0x0083 }
                    java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0083 }
                    com.taobao.weex.utils.WXLogUtils.d(r3)     // Catch:{ all -> 0x0083 }
                L_0x0035:
                    com.taobao.weex.bridge.WXBridgeManager r3 = com.taobao.weex.bridge.WXBridgeManager.this     // Catch:{ all -> 0x0083 }
                    com.taobao.weex.common.IWXBridge r3 = r3.mWXBridge     // Catch:{ all -> 0x0083 }
                    boolean r3 = r3 instanceof com.taobao.weex.bridge.WXBridge     // Catch:{ all -> 0x0083 }
                    if (r3 == 0) goto L_0x007a
                    com.taobao.weex.bridge.WXBridgeManager r3 = com.taobao.weex.bridge.WXBridgeManager.this     // Catch:{ all -> 0x0083 }
                    com.taobao.weex.common.IWXBridge r3 = r3.mWXBridge     // Catch:{ all -> 0x0083 }
                    r4 = r3
                    com.taobao.weex.bridge.WXBridge r4 = (com.taobao.weex.bridge.WXBridge) r4     // Catch:{ all -> 0x0083 }
                    java.lang.String r5 = r3     // Catch:{ all -> 0x0083 }
                    java.lang.String r6 = r5     // Catch:{ all -> 0x0083 }
                    java.lang.String r7 = r6     // Catch:{ all -> 0x0083 }
                    java.util.Map r3 = r4     // Catch:{ all -> 0x0083 }
                    java.lang.String r8 = "{}"
                    if (r3 == 0) goto L_0x0063
                    boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0083 }
                    if (r3 == 0) goto L_0x005c
                    goto L_0x0063
                L_0x005c:
                    java.util.Map r3 = r4     // Catch:{ all -> 0x0083 }
                    java.lang.String r3 = com.alibaba.fastjson.JSON.toJSONString(r3)     // Catch:{ all -> 0x0083 }
                    goto L_0x0064
                L_0x0063:
                    r3 = r8
                L_0x0064:
                    java.util.Map r9 = r7     // Catch:{ all -> 0x0083 }
                    if (r9 == 0) goto L_0x0075
                    boolean r9 = r9.isEmpty()     // Catch:{ all -> 0x0083 }
                    if (r9 == 0) goto L_0x006f
                    goto L_0x0075
                L_0x006f:
                    java.util.Map r8 = r7     // Catch:{ all -> 0x0083 }
                    java.lang.String r8 = com.alibaba.fastjson.JSON.toJSONString(r8)     // Catch:{ all -> 0x0083 }
                L_0x0075:
                    r9 = r8
                    r8 = r3
                    r4.fireEventOnDataRenderNode(r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0083 }
                L_0x007a:
                    long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0083 }
                    long r3 = r3 - r1
                    com.taobao.weex.utils.WXLogUtils.renderPerformanceLog(r0, r3)     // Catch:{ all -> 0x0083 }
                    goto L_0x00a4
                L_0x0083:
                    r1 = move-exception
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    r2.<init>()
                    java.lang.String r3 = "[WXBridgeManager] fireEventOnDataRenderNode "
                    r2.append(r3)
                    java.lang.String r1 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r1)
                    r2.append(r1)
                    java.lang.String r1 = r2.toString()
                    java.lang.String r2 = r3
                    com.taobao.weex.common.WXErrorCode r3 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE
                    r4 = 0
                    com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r2, r3, r0, r1, r4)
                    com.taobao.weex.utils.WXLogUtils.e(r1)
                L_0x00a4:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.AnonymousClass14.run():void");
            }
        }), 0);
    }

    private boolean checkMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @Deprecated
    public void callback(String str, String str2, String str3) {
        callback(str, str2, str3, false);
    }

    @Deprecated
    public void callback(String str, String str2, Map<String, Object> map) {
        callback(str, str2, map, false);
    }

    @Deprecated
    public void callback(String str, String str2, Object obj, boolean z) {
        callbackJavascript(str, str2, obj, z);
    }

    /* access modifiers changed from: package-private */
    public void callbackJavascript(String str, String str2, Object obj, boolean z) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && this.mJSHandler != null && RuningAcitvityUtil.isRuningActivity) {
            WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
            if (wXSDKInstance == null || wXSDKInstance.getRenderStrategy() != WXRenderStrategy.DATA_RENDER_BINARY) {
                addJSTask(METHOD_CALLBACK, str, str2, obj, Boolean.valueOf(z));
                sendMessage(str, 6);
                return;
            }
            callbackJavascriptOnDataRender(str, str2, obj, z);
        }
    }

    /* access modifiers changed from: package-private */
    public void callbackJavascriptOnDataRender(String str, String str2, Object obj, boolean z) {
        final Object obj2 = obj;
        final String str3 = str;
        final String str4 = str2;
        final boolean z2 = z;
        this.mJSHandler.postDelayed(WXThread.secure((Runnable) new Runnable() {
            public void run() {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    String jSONString = JSON.toJSONString(obj2);
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.d("callbackJavascriptOnDataRender >>>> instanceId:" + str3 + ", data:" + jSONString);
                    }
                    if (WXBridgeManager.this.mWXBridge instanceof WXBridge) {
                        ((WXBridge) WXBridgeManager.this.mWXBridge).invokeCallbackOnDataRender(str3, str4, jSONString, z2);
                    }
                    WXLogUtils.renderPerformanceLog("callbackJavascriptOnDataRender", System.currentTimeMillis() - currentTimeMillis);
                } catch (Throwable th) {
                    String str = "[WXBridgeManager] callbackJavascriptOnDataRender " + WXLogUtils.getStackTrace(th);
                    WXExceptionUtils.commitCriticalExceptionRT(str3, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callbackJavascriptOnDataRender", str, (Map<String, String>) null);
                    WXLogUtils.e(str);
                }
            }
        }), 0);
    }

    public void refreshInstance(final String str, final WXRefreshData wXRefreshData) {
        if (!TextUtils.isEmpty(str) && wXRefreshData != null) {
            this.mJSHandler.postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    WXBridgeManager.this.invokeRefreshInstance(str, wXRefreshData);
                }
            }), 0);
        }
    }

    /* access modifiers changed from: private */
    public void invokeRefreshInstance(String str, WXRefreshData wXRefreshData) {
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
                System.currentTimeMillis();
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("refreshInstance >>>> instanceId:" + str + ", data:" + wXRefreshData.data + ", isDirty:" + wXRefreshData.isDirty);
                }
                if (!wXRefreshData.isDirty) {
                    this.mWXBridge.refreshInstance(str, (String) null, METHOD_REFRESH_INSTANCE, new WXJSObject[]{new WXJSObject(2, str), new WXJSObject(3, wXRefreshData.data == null ? "{}" : wXRefreshData.data)});
                    return;
                }
                return;
            }
            if (sDKInstance != null) {
                sDKInstance.onRenderError(WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode(), WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + "invokeRefreshInstance FAILED for JSFrameworkInit FAILED, intance will invoke instance.onRenderError");
            }
            WXLogUtils.e("[WXBridgeManager] invokeRefreshInstance: framework.js uninitialized.");
        } catch (Throwable th) {
            String str2 = "[WXBridgeManager] invokeRefreshInstance " + WXLogUtils.getStackTrace(th);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "invokeRefreshInstance", str2, (Map<String, String>) null);
            WXLogUtils.e(str2);
        }
    }

    public void commitJscCrashAlarmMonitor(String str, WXErrorCode wXErrorCode, String str2, String str3, String str4, Map<String, String> map) {
        if (!TextUtils.isEmpty(str) && wXErrorCode != null) {
            Log.d("ReportCrash", " commitJscCrashAlarmMonitor errMsg " + str2);
            HashMap hashMap = new HashMap();
            hashMap.put("jscCrashStack", str2);
            if (map != null) {
                hashMap.putAll(map);
            }
            IWXJSExceptionAdapter iWXJSExceptionAdapter = WXSDKManager.getInstance().getIWXJSExceptionAdapter();
            if (iWXJSExceptionAdapter != null) {
                WXJSExceptionInfo wXJSExceptionInfo = new WXJSExceptionInfo(str3, str4, wXErrorCode, "callReportCrash", "weex core process crash and restart exception", hashMap);
                iWXJSExceptionAdapter.onJSException(wXJSExceptionInfo);
                WXLogUtils.e(wXJSExceptionInfo.toString());
            }
        }
    }

    private boolean isSkipFrameworkInit(String str) {
        return isSkipFrameworkInit(WXSDKManager.getInstance().getSDKInstance(str));
    }

    private boolean isSkipFrameworkInit(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance == null) {
            return false;
        }
        return wXSDKInstance.skipFrameworkInit();
    }

    public void createInstance(String str, String str2, Map<String, Object> map, String str3) {
        createInstance(str, new Script(str2), map, str3);
    }

    public void createInstance(String str, Script script, Map<String, Object> map, String str2) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            WXLogUtils.e("WXBridgeManager", "createInstance failed, SDKInstance does not exist");
        } else if (TextUtils.isEmpty(str) || script == null || script.isEmpty() || this.mJSHandler == null) {
            String errorCode = WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode();
            sDKInstance.onRenderError(errorCode, WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + " instanceId==" + str + " template ==" + script + " mJSHandler== " + this.mJSHandler.toString());
        } else if (isSkipFrameworkInit(str) || isJSFrameworkInit() || reInitCount != 1 || WXEnvironment.sDebugServerConnectable) {
            WXModuleManager.createDomModule(sDKInstance);
            sDKInstance.getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_LOAD_BUNDLE_START);
            final String str3 = str;
            final WXSDKInstance wXSDKInstance = sDKInstance;
            final Script script2 = script;
            final Map<String, Object> map2 = map;
            final String str4 = str2;
            post(new Runnable() {
                public void run() {
                    long currentTimeMillis = System.currentTimeMillis();
                    WXBridgeManager.this.mWXBridge.setPageArgument(str3, "renderTimeOrigin", String.valueOf(wXSDKInstance.getWXPerformance().renderTimeOrigin));
                    WXBridgeManager.this.mWXBridge.setInstanceRenderType(wXSDKInstance.getInstanceId(), wXSDKInstance.getRenderType());
                    WXBridgeManager.this.invokeCreateInstance(wXSDKInstance, script2, map2, str4);
                    wXSDKInstance.getWXPerformance().callCreateInstanceTime = System.currentTimeMillis() - currentTimeMillis;
                    wXSDKInstance.getWXPerformance().communicateTime = wXSDKInstance.getWXPerformance().callCreateInstanceTime;
                }
            }, str, sDKInstance, METHOD_CREATE_INSTANCE);
        } else {
            String errorCode2 = WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode();
            sDKInstance.onRenderError(errorCode2, WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + " isJSFrameworkInit==" + isJSFrameworkInit() + " reInitCount == 1");
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.initFramework("");
                }
            }, str, sDKInstance, "initFrameworkInCreateInstance");
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x01f5 A[SYNTHETIC, Splitter:B:102:0x01f5] */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0227 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0114 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x011c A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0124  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0126 A[SYNTHETIC, Splitter:B:65:0x0126] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0132 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0134 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0145 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0146 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x014f A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0162 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0192 A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x019e A[Catch:{ all -> 0x0086, all -> 0x02c9 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void invokeCreateInstance(com.taobao.weex.WXSDKInstance r16, com.taobao.weex.Script r17, java.util.Map<java.lang.String, java.lang.Object> r18, java.lang.String r19) {
        /*
            r15 = this;
            r7 = r15
            r8 = r16
            java.lang.String r1 = "env"
            java.lang.String r9 = "wxEndLoadBundle"
            java.lang.String r2 = "extraOption"
            java.lang.String r3 = "bundleType"
            boolean r0 = r15.isSkipFrameworkInit((com.taobao.weex.WXSDKInstance) r16)
            java.lang.String r4 = ""
            if (r0 != 0) goto L_0x0017
            r15.initFramework(r4)
        L_0x0017:
            boolean r0 = r7.mMock
            if (r0 == 0) goto L_0x0024
            java.lang.String r0 = r16.getInstanceId()
            r15.mock(r0)
            goto L_0x0307
        L_0x0024:
            boolean r0 = r15.isSkipFrameworkInit((com.taobao.weex.WXSDKInstance) r16)
            if (r0 != 0) goto L_0x0045
            boolean r0 = r15.isJSFrameworkInit()
            if (r0 != 0) goto L_0x0045
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r0 = r0.getErrorCode()
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r1 = r1.getErrorMsg()
            r8.onRenderError(r0, r1)
            java.lang.String r0 = "[WXBridgeManager] invokeCreateInstance: framework.js uninitialized."
            com.taobao.weex.utils.WXLogUtils.e(r0)
            return
        L_0x0045:
            com.taobao.weex.bridge.WXModuleManager.registerWhenCreateInstance()
            com.taobao.weex.bridge.WXBridgeManager$BundType r5 = com.taobao.weex.bridge.WXBridgeManager.BundType.Others     // Catch:{ all -> 0x02c9 }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0086 }
            java.lang.String r0 = r16.getBundleUrl()     // Catch:{ all -> 0x0086 }
            java.lang.String r6 = r17.getContent()     // Catch:{ all -> 0x0086 }
            com.taobao.weex.bridge.WXBridgeManager$BundType r5 = r15.getBundleType(r0, r6)     // Catch:{ all -> 0x0086 }
            boolean r0 = com.taobao.weex.WXEnvironment.isOpenDebugLog()     // Catch:{ all -> 0x0086 }
            if (r0 == 0) goto L_0x008e
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0086 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0086 }
            r0.<init>()     // Catch:{ all -> 0x0086 }
            java.lang.String r6 = "end getBundleType type:"
            r0.append(r6)     // Catch:{ all -> 0x0086 }
            java.lang.String r6 = r5.toString()     // Catch:{ all -> 0x0086 }
            r0.append(r6)     // Catch:{ all -> 0x0086 }
            java.lang.String r6 = " time:"
            r0.append(r6)     // Catch:{ all -> 0x0086 }
            long r12 = r12 - r10
            r0.append(r12)     // Catch:{ all -> 0x0086 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0086 }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ all -> 0x0086 }
            goto L_0x008e
        L_0x0086:
            r0 = move-exception
            java.lang.String r0 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r0)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ all -> 0x02c9 }
        L_0x008e:
            if (r18 != 0) goto L_0x009b
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0097 }
            r0.<init>()     // Catch:{ all -> 0x0097 }
            r6 = r0
            goto L_0x009d
        L_0x0097:
            r0 = move-exception
            r6 = r18
            goto L_0x00ec
        L_0x009b:
            r6 = r18
        L_0x009d:
            java.lang.Object r0 = r6.get(r3)     // Catch:{ all -> 0x00eb }
            if (r0 != 0) goto L_0x00db
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Vue     // Catch:{ all -> 0x00eb }
            java.lang.String r10 = "Others"
            if (r5 != r0) goto L_0x00af
            java.lang.String r0 = "Vue"
            r6.put(r3, r0)     // Catch:{ all -> 0x00eb }
            goto L_0x00bc
        L_0x00af:
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ all -> 0x00eb }
            if (r5 != r0) goto L_0x00b9
            java.lang.String r0 = "Rax"
            r6.put(r3, r0)     // Catch:{ all -> 0x00eb }
            goto L_0x00bc
        L_0x00b9:
            r6.put(r3, r10)     // Catch:{ all -> 0x00eb }
        L_0x00bc:
            java.lang.Object r0 = r6.get(r3)     // Catch:{ all -> 0x00eb }
            boolean r3 = r0 instanceof java.lang.String     // Catch:{ all -> 0x00eb }
            if (r3 == 0) goto L_0x00cf
            r3 = r0
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x00eb }
            boolean r3 = r10.equalsIgnoreCase(r3)     // Catch:{ all -> 0x00eb }
            if (r3 == 0) goto L_0x00cf
            java.lang.String r0 = "other"
        L_0x00cf:
            if (r0 == 0) goto L_0x00db
            com.taobao.weex.performance.WXInstanceApm r3 = r16.getApmForInstance()     // Catch:{ all -> 0x00eb }
            java.lang.String r10 = "wxBundleType"
            r3.addProperty(r10, r0)     // Catch:{ all -> 0x00eb }
        L_0x00db:
            java.lang.Object r0 = r6.get(r1)     // Catch:{ all -> 0x00eb }
            if (r0 != 0) goto L_0x00f3
            com.taobao.weex.bridge.WXParams r0 = r7.mInitParams     // Catch:{ all -> 0x00eb }
            java.util.Map r0 = r0.toMap()     // Catch:{ all -> 0x00eb }
            r6.put(r1, r0)     // Catch:{ all -> 0x00eb }
            goto L_0x00f3
        L_0x00eb:
            r0 = move-exception
        L_0x00ec:
            java.lang.String r0 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r0)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ all -> 0x02c9 }
        L_0x00f3:
            r8.bundleType = r5     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.WXEnvironment.isApkDebugable()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.bridge.WXJSObject r0 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = r16.getInstanceId()     // Catch:{ all -> 0x02c9 }
            r3 = 2
            r0.<init>(r3, r1)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.bridge.WXJSObject r1 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            java.lang.String r10 = r17.getContent()     // Catch:{ all -> 0x02c9 }
            r1.<init>(r3, r10)     // Catch:{ all -> 0x02c9 }
            r10 = 0
            if (r6 == 0) goto L_0x011c
            boolean r11 = r6.containsKey(r2)     // Catch:{ all -> 0x02c9 }
            if (r11 == 0) goto L_0x011c
            java.lang.Object r11 = r6.get(r2)     // Catch:{ all -> 0x02c9 }
            r6.remove(r2)     // Catch:{ all -> 0x02c9 }
            goto L_0x011d
        L_0x011c:
            r11 = r10
        L_0x011d:
            com.taobao.weex.bridge.WXJSObject r2 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            java.lang.String r12 = "{}"
            if (r11 != 0) goto L_0x0126
            r11 = r12
            goto L_0x012a
        L_0x0126:
            java.lang.String r11 = com.taobao.weex.utils.WXJsonUtils.fromObjectToJSONString(r11)     // Catch:{ all -> 0x02c9 }
        L_0x012a:
            r13 = 3
            r2.<init>(r13, r11)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.bridge.WXJSObject r11 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            if (r6 != 0) goto L_0x0134
            r6 = r12
            goto L_0x0138
        L_0x0134:
            java.lang.String r6 = com.taobao.weex.utils.WXJsonUtils.fromObjectToJSONString(r6)     // Catch:{ all -> 0x02c9 }
        L_0x0138:
            r11.<init>(r13, r6)     // Catch:{ all -> 0x02c9 }
            boolean r6 = isSandBoxContext     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.bridge.WXJSObject r6 = r15.optionObjConvert(r6, r5, r11)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.bridge.WXJSObject r11 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            if (r19 != 0) goto L_0x0146
            goto L_0x0148
        L_0x0146:
            r12 = r19
        L_0x0148:
            r11.<init>(r13, r12)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.bridge.WXBridgeManager$BundType r12 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ all -> 0x02c9 }
            if (r5 == r12) goto L_0x015e
            com.taobao.weex.common.WXRenderStrategy r12 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r14 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ all -> 0x02c9 }
            if (r12 != r14) goto L_0x0158
            goto L_0x015e
        L_0x0158:
            com.taobao.weex.bridge.WXJSObject r12 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            r12.<init>(r3, r4)     // Catch:{ all -> 0x02c9 }
            goto L_0x018a
        L_0x015e:
            java.lang.String r4 = mRaxApi     // Catch:{ all -> 0x02c9 }
            if (r4 != 0) goto L_0x0183
            com.taobao.weex.adapter.IWXJsFileLoaderAdapter r4 = com.taobao.weex.WXSDKEngine.getIWXJsFileLoaderAdapter()     // Catch:{ all -> 0x02c9 }
            if (r4 == 0) goto L_0x016e
            java.lang.String r4 = r4.loadRaxApi()     // Catch:{ all -> 0x02c9 }
            mRaxApi = r4     // Catch:{ all -> 0x02c9 }
        L_0x016e:
            java.lang.String r4 = mRaxApi     // Catch:{ all -> 0x02c9 }
            boolean r4 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x02c9 }
            if (r4 == 0) goto L_0x0183
            java.lang.String r4 = "weex-rax-api.js"
            android.app.Application r12 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ all -> 0x02c9 }
            java.lang.String r4 = com.taobao.weex.utils.WXFileUtils.loadAsset(r4, r12)     // Catch:{ all -> 0x02c9 }
            mRaxApi = r4     // Catch:{ all -> 0x02c9 }
        L_0x0183:
            com.taobao.weex.bridge.WXJSObject r12 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            java.lang.String r4 = mRaxApi     // Catch:{ all -> 0x02c9 }
            r12.<init>(r3, r4)     // Catch:{ all -> 0x02c9 }
        L_0x018a:
            com.taobao.weex.common.WXRenderStrategy r4 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r14 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ all -> 0x02c9 }
            if (r4 != r14) goto L_0x019e
            com.taobao.weex.bridge.WXJSObject r10 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r4 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ all -> 0x02c9 }
            java.lang.String r4 = r4.getFlag()     // Catch:{ all -> 0x02c9 }
            r10.<init>(r3, r4)     // Catch:{ all -> 0x02c9 }
            goto L_0x01cb
        L_0x019e:
            com.taobao.weex.common.WXRenderStrategy r4 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r14 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ all -> 0x02c9 }
            if (r4 != r14) goto L_0x01b8
            com.taobao.weex.bridge.WXJSObject r10 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r4 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ all -> 0x02c9 }
            java.lang.String r4 = r4.getFlag()     // Catch:{ all -> 0x02c9 }
            r10.<init>(r3, r4)     // Catch:{ all -> 0x02c9 }
            byte[] r4 = r17.getBinary()     // Catch:{ all -> 0x02c9 }
            r1.data = r4     // Catch:{ all -> 0x02c9 }
            goto L_0x01cb
        L_0x01b8:
            com.taobao.weex.common.WXRenderStrategy r4 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r14 = com.taobao.weex.common.WXRenderStrategy.JSON_RENDER     // Catch:{ all -> 0x02c9 }
            if (r4 != r14) goto L_0x01cb
            com.taobao.weex.bridge.WXJSObject r10 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r4 = com.taobao.weex.common.WXRenderStrategy.JSON_RENDER     // Catch:{ all -> 0x02c9 }
            java.lang.String r4 = r4.getFlag()     // Catch:{ all -> 0x02c9 }
            r10.<init>(r3, r4)     // Catch:{ all -> 0x02c9 }
        L_0x01cb:
            r4 = 7
            com.taobao.weex.bridge.WXJSObject[] r14 = new com.taobao.weex.bridge.WXJSObject[r4]     // Catch:{ all -> 0x02c9 }
            r4 = 0
            r14[r4] = r0     // Catch:{ all -> 0x02c9 }
            r0 = 1
            r14[r0] = r1     // Catch:{ all -> 0x02c9 }
            r14[r3] = r6     // Catch:{ all -> 0x02c9 }
            r14[r13] = r11     // Catch:{ all -> 0x02c9 }
            r0 = 4
            r14[r0] = r12     // Catch:{ all -> 0x02c9 }
            r0 = 5
            r14[r0] = r10     // Catch:{ all -> 0x02c9 }
            r0 = 6
            r14[r0] = r2     // Catch:{ all -> 0x02c9 }
            java.lang.String r0 = r17.getContent()     // Catch:{ all -> 0x02c9 }
            r8.setTemplate(r0)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.performance.WXInstanceApm r0 = r16.getApmForInstance()     // Catch:{ all -> 0x02c9 }
            r0.onStage(r9)     // Catch:{ all -> 0x02c9 }
            boolean r0 = isSandBoxContext     // Catch:{ all -> 0x02c9 }
            java.lang.String r10 = "Instance "
            if (r0 != 0) goto L_0x0227
            com.taobao.weex.performance.WXInstanceApm r0 = r16.getApmForInstance()     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = "!isSandBoxContext,and excute"
            r0.onStage(r1)     // Catch:{ all -> 0x02c9 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02c9 }
            r0.<init>()     // Catch:{ all -> 0x02c9 }
            r0.append(r10)     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = r16.getInstanceId()     // Catch:{ all -> 0x02c9 }
            r0.append(r1)     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = " Did Not Render in SandBox Mode"
            r0.append(r1)     // Catch:{ all -> 0x02c9 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.utils.WXLogUtils.e(r0)     // Catch:{ all -> 0x02c9 }
            java.lang.String r2 = r16.getInstanceId()     // Catch:{ all -> 0x02c9 }
            r3 = 0
            java.lang.String r4 = "createInstance"
            r6 = 0
            r1 = r15
            r5 = r14
            r1.invokeExecJS(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x02c9 }
            return
        L_0x0227:
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Vue     // Catch:{ all -> 0x02c9 }
            if (r5 == r0) goto L_0x025d
            com.taobao.weex.bridge.WXBridgeManager$BundType r0 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ all -> 0x02c9 }
            if (r5 == r0) goto L_0x025d
            com.taobao.weex.common.WXRenderStrategy r0 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ all -> 0x02c9 }
            if (r0 == r1) goto L_0x025d
            com.taobao.weex.common.WXRenderStrategy r0 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ all -> 0x02c9 }
            if (r0 == r1) goto L_0x025d
            com.taobao.weex.common.WXRenderStrategy r0 = r16.getRenderStrategy()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.JSON_RENDER     // Catch:{ all -> 0x02c9 }
            if (r0 != r1) goto L_0x0248
            goto L_0x025d
        L_0x0248:
            java.lang.String r2 = r16.getInstanceId()     // Catch:{ all -> 0x02c9 }
            r3 = 0
            java.lang.String r4 = "createInstance"
            r6 = 0
            r1 = r15
            r5 = r14
            r1.invokeExecJS(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.performance.WXInstanceApm r0 = r16.getApmForInstance()     // Catch:{ all -> 0x02c9 }
            r0.onStage(r9)     // Catch:{ all -> 0x02c9 }
            return
        L_0x025d:
            java.lang.String r2 = r16.getInstanceId()     // Catch:{ all -> 0x02c9 }
            java.lang.String r3 = r16.getUniPagePath()     // Catch:{ all -> 0x02c9 }
            java.lang.String r4 = "createInstanceContext"
            r6 = 0
            r1 = r15
            r5 = r14
            int r0 = r1.invokeCreateInstanceContext(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.performance.WXInstanceApm r1 = r16.getApmForInstance()     // Catch:{ all -> 0x02c9 }
            r1.onStage(r9)     // Catch:{ all -> 0x02c9 }
            if (r0 != 0) goto L_0x02c8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x02c9 }
            r0.<init>()     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = "[WXBridgeManager] invokeCreateInstance : "
            r0.append(r1)     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = r16.getTemplateInfo()     // Catch:{ all -> 0x02c9 }
            r0.append(r1)     // Catch:{ all -> 0x02c9 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x02c9 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x02c9 }
            r1.<init>()     // Catch:{ all -> 0x02c9 }
            r1.append(r10)     // Catch:{ all -> 0x02c9 }
            java.lang.String r2 = r16.getInstanceId()     // Catch:{ all -> 0x02c9 }
            r1.append(r2)     // Catch:{ all -> 0x02c9 }
            java.lang.String r2 = "Render error : "
            r1.append(r2)     // Catch:{ all -> 0x02c9 }
            r1.append(r0)     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.utils.WXLogUtils.e(r1)     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED     // Catch:{ all -> 0x02c9 }
            java.lang.String r1 = r1.getErrorCode()     // Catch:{ all -> 0x02c9 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x02c9 }
            r2.<init>()     // Catch:{ all -> 0x02c9 }
            com.taobao.weex.common.WXErrorCode r3 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED     // Catch:{ all -> 0x02c9 }
            java.lang.String r3 = r3.getErrorMsg()     // Catch:{ all -> 0x02c9 }
            r2.append(r3)     // Catch:{ all -> 0x02c9 }
            r2.append(r0)     // Catch:{ all -> 0x02c9 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x02c9 }
            r8.onRenderError(r1, r0)     // Catch:{ all -> 0x02c9 }
        L_0x02c8:
            return
        L_0x02c9:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "[WXBridgeManager] invokeCreateInstance "
            r1.append(r2)
            java.lang.Throwable r0 = r0.getCause()
            r1.append(r0)
            java.lang.String r0 = r16.getTemplateInfo()
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r1 = r1.getErrorCode()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            com.taobao.weex.common.WXErrorCode r3 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED
            java.lang.String r3 = r3.getErrorMsg()
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            r8.onRenderError(r1, r2)
            com.taobao.weex.utils.WXLogUtils.e(r0)
        L_0x0307:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.invokeCreateInstance(com.taobao.weex.WXSDKInstance, com.taobao.weex.Script, java.util.Map, java.lang.String):void");
    }

    public WXJSObject optionObjConvert(boolean z, BundType bundType, WXJSObject wXJSObject) {
        JSONObject jSONObject;
        if (!z) {
            return wXJSObject;
        }
        try {
            JSONObject parseObject = JSON.parseObject(wXJSObject.data.toString());
            JSONObject jSONObject2 = parseObject.getJSONObject("env");
            if (!(jSONObject2 == null || (jSONObject = jSONObject2.getJSONObject("options")) == null)) {
                for (String next : jSONObject.keySet()) {
                    jSONObject2.put(next, (Object) jSONObject.getString(next));
                }
            }
            return new WXJSObject(3, parseObject.toString());
        } catch (Throwable th) {
            WXLogUtils.e(WXLogUtils.getStackTrace(th));
            return wXJSObject;
        }
    }

    public BundType getBundleType(String str, String str2) {
        if (str != null) {
            try {
                String queryParameter = Uri.parse(str).getQueryParameter(BUNDLE_TYPE);
                if (!"Vue".equals(queryParameter)) {
                    if (!"vue".equals(queryParameter)) {
                        if ("Rax".equals(queryParameter) || "rax".equals(queryParameter)) {
                            return BundType.Rax;
                        }
                    }
                }
                return BundType.Vue;
            } catch (Throwable th) {
                WXLogUtils.e(WXLogUtils.getStackTrace(th));
                return BundType.Others;
            }
        }
        if (str2 != null) {
            return BundType.Vue;
        }
        return BundType.Others;
    }

    public void destroyInstance(final String str) {
        if (this.mJSHandler != null && !TextUtils.isEmpty(str)) {
            HashSet<String> hashSet = this.mDestroyedInstanceId;
            if (hashSet != null) {
                hashSet.add(str);
            }
            this.mJSHandler.removeCallbacksAndMessages(str);
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.removeTaskByInstance(str);
                    WXBridgeManager.this.invokeDestroyInstance(str);
                }
            }, str, (WXSDKInstance) null, METHOD_DESTROY_INSTANCE);
        }
    }

    /* access modifiers changed from: private */
    public void removeTaskByInstance(String str) {
        this.mNextTickTasks.removeFromMapAndStack(str);
    }

    /* access modifiers changed from: private */
    public void invokeDestroyInstance(String str) {
        try {
            WXEnvironment.isApkDebugable();
            WXJSObject[] wXJSObjectArr = {new WXJSObject(2, str)};
            if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
                invokeDestoryInstance(str, (String) null, METHOD_DESTROY_INSTANCE, wXJSObjectArr, true);
            }
        } catch (Throwable th) {
            String str2 = "[WXBridgeManager] invokeDestroyInstance " + th.getCause();
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "invokeDestroyInstance", str2, (Map<String, String>) null);
            WXLogUtils.e(str2);
        }
    }

    public boolean handleMessage(Message message) {
        if (message == null) {
            return false;
        }
        int i = message.what;
        if (i == 1) {
            TimerInfo timerInfo = (TimerInfo) message.obj;
            if (timerInfo != null) {
                invokeExecJS("", (String) null, METHOD_SET_TIMEOUT, new WXJSObject[]{new WXJSObject(2, timerInfo.callbackId)});
            }
        } else if (i != 13) {
            if (i == 6) {
                invokeCallJSBatch(message);
            } else if (i == 7) {
                invokeInitFramework(message);
            }
        } else if (message.obj != null) {
            this.mWXBridge.takeHeapSnapshot((String) message.obj);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void invokeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        invokeExecJS(str, str2, str3, wXJSObjectArr, true);
    }

    public void invokeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, boolean z) {
        Pair<Pair<String, Object>, Boolean> extractCallbackArgs;
        WXEnvironment.isOpenDebugLog();
        if (RuningAcitvityUtil.isRuningActivity) {
            long currentTimeMillis = System.currentTimeMillis();
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null || sDKInstance.getRenderStrategy() != WXRenderStrategy.DATA_RENDER_BINARY) {
                final String str4 = str;
                final String str5 = str2;
                final String str6 = str3;
                final WXJSObject[] wXJSObjectArr2 = wXJSObjectArr;
                WXThread.secure(new Runnable() {
                    public void run() {
                        WXBridgeManager.this.mWXBridge.execJS(str4, str5, str6, wXJSObjectArr2);
                    }
                }, sDKInstance, "ExecJs").run();
            } else if (wXJSObjectArr.length != 2 || !(wXJSObjectArr[0].data instanceof String) || !(wXJSObjectArr[1].data instanceof String) || (extractCallbackArgs = extractCallbackArgs((String) wXJSObjectArr[1].data)) == null) {
                WXLogUtils.w("invokeExecJS on data render that is not a callback call");
                return;
            } else {
                callbackJavascriptOnDataRender(str, (String) ((Pair) extractCallbackArgs.first).first, ((Pair) extractCallbackArgs.first).second, ((Boolean) extractCallbackArgs.second).booleanValue());
            }
            if (sDKInstance != null) {
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                sDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_JS_NUM, 1.0d);
                sDKInstance.getApmForInstance().updateFSDiffStats(WXInstanceApm.KEY_PAGE_STATS_FS_CALL_JS_TIME, (double) currentTimeMillis2);
                sDKInstance.callJsTime(currentTimeMillis2);
            }
        }
    }

    private Pair<Pair<String, Object>, Boolean> extractCallbackArgs(String str) {
        try {
            JSONObject jSONObject = JSON.parseArray(str).getJSONObject(0);
            JSONArray jSONArray = jSONObject.getJSONArray("args");
            if (jSONArray.size() == 3 && METHOD_CALLBACK.equals(jSONObject.getString("method"))) {
                return new Pair<>(new Pair(jSONArray.getString(0), jSONArray.getJSONObject(1)), Boolean.valueOf(jSONArray.getBooleanValue(2)));
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public int invokeCreateInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, boolean z) {
        WXLogUtils.d("invokeCreateInstanceContext instanceId:" + str + " function:" + str3 + StringUtil.format(" isJSFrameworkInit：%b", Boolean.valueOf(isJSFrameworkInit())));
        StringBuilder sb = this.mLodBuilder;
        sb.append("createInstanceContext >>>> instanceId:");
        sb.append(str);
        sb.append("function:");
        sb.append(str3);
        if (z) {
            StringBuilder sb2 = this.mLodBuilder;
            sb2.append(" tasks:");
            sb2.append(WXJsonUtils.fromObjectToJSONString(wXJSObjectArr));
        }
        WXLogUtils.d(this.mLodBuilder.substring(0));
        this.mLodBuilder.setLength(0);
        return this.mWXBridge.createInstanceContext(str, str2, str3, wXJSObjectArr);
    }

    public void invokeDestoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, boolean z) {
        StringBuilder sb = this.mLodBuilder;
        sb.append("callJS >>>> instanceId:");
        sb.append(str);
        sb.append("function:");
        sb.append(str3);
        if (z) {
            StringBuilder sb2 = this.mLodBuilder;
            sb2.append(" tasks:");
            sb2.append(WXJsonUtils.fromObjectToJSONString(wXJSObjectArr));
        }
        WXLogUtils.d(this.mLodBuilder.substring(0));
        this.mLodBuilder.setLength(0);
        this.mWXBridge.removeInstanceRenderType(str);
        this.mWXBridge.destoryInstance(str, str2, str3, wXJSObjectArr);
    }

    private void execJSOnInstance(EventResult eventResult, String str, String str2, int i) {
        final String str3 = str;
        final String str4 = str2;
        final int i2 = i;
        final EventResult eventResult2 = eventResult;
        post(new Runnable() {
            public void run() {
                eventResult2.onCallback(WXBridgeManager.this.invokeExecJSOnInstance(str3, str4, i2));
            }
        });
    }

    /* access modifiers changed from: private */
    public String invokeExecJSOnInstance(String str, String str2, int i) {
        StringBuilder sb = this.mLodBuilder;
        sb.append("execJSOnInstance >>>> instanceId:");
        sb.append(str);
        WXLogUtils.d(this.mLodBuilder.substring(0));
        this.mLodBuilder.setLength(0);
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            return this.mWXBridge.execJSOnInstance(str, str2, i);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void invokeExecJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, ResultCallback resultCallback, boolean z) {
        WXEnvironment.isOpenDebugLog();
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.execJSWithCallback(str, str2, str3, wXJSObjectArr, resultCallback);
        }
    }

    public static String argsToJSON(WXJSObject[] wXJSObjectArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        for (WXJSObject fromObjectToJSONString : wXJSObjectArr) {
            sb.append(WXWsonJSONSwitch.fromObjectToJSONString(fromObjectToJSONString));
            sb.append(",");
        }
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    private void invokeInitFramework(Message message) {
        String str = message.obj != null ? (String) message.obj : "";
        if (WXUtils.getAvailMemory(WXEnvironment.getApplication()) > LOW_MEM_VALUE) {
            initFramework(str);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0122, code lost:
        if (android.os.Build.VERSION.SDK_INT < 16) goto L_0x012e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initFramework(java.lang.String r11) {
        /*
            r10 = this;
            com.taobao.weex.utils.tools.LogDetail r0 = new com.taobao.weex.utils.tools.LogDetail
            r0.<init>()
            java.lang.String r1 = "initFramework"
            r0.name(r1)
            r0.taskStart()
            boolean r2 = com.taobao.weex.WXSDKEngine.isSoInitialized()
            if (r2 == 0) goto L_0x01fd
            boolean r2 = r10.isJSFrameworkInit()
            if (r2 != 0) goto L_0x01fd
            long r2 = java.lang.System.currentTimeMillis()
            sInitFrameWorkTimeOrigin = r2
            boolean r2 = android.text.TextUtils.isEmpty(r11)
            if (r2 == 0) goto L_0x008d
            java.lang.String r2 = "weex JS framework from assets"
            com.taobao.weex.utils.WXLogUtils.d(r2)
            com.taobao.weex.utils.tools.LogDetail r2 = new com.taobao.weex.utils.tools.LogDetail
            r2.<init>()
            java.lang.String r3 = "loadJSFramework"
            r2.name(r3)
            r2.taskStart()
            com.taobao.weex.adapter.IWXJsFileLoaderAdapter r3 = com.taobao.weex.WXSDKEngine.getIWXJsFileLoaderAdapter()
            boolean r4 = isSandBoxContext
            if (r4 != 0) goto L_0x0057
            if (r3 == 0) goto L_0x0046
            java.lang.String r11 = r3.loadJsFramework()
        L_0x0046:
            boolean r3 = android.text.TextUtils.isEmpty(r11)
            if (r3 == 0) goto L_0x006e
            android.app.Application r11 = com.taobao.weex.WXEnvironment.getApplication()
            java.lang.String r3 = "main.js"
            java.lang.String r11 = com.taobao.weex.utils.WXFileUtils.loadAsset(r3, r11)
            goto L_0x006e
        L_0x0057:
            if (r3 == 0) goto L_0x005d
            java.lang.String r11 = r3.loadJsFrameworkForSandBox()
        L_0x005d:
            boolean r3 = android.text.TextUtils.isEmpty(r11)
            if (r3 == 0) goto L_0x006e
            android.app.Application r11 = com.taobao.weex.WXEnvironment.getApplication()
            java.lang.String r3 = "weex-main-jsfm.js"
            java.lang.String r11 = com.taobao.weex.utils.WXFileUtils.loadAsset(r3, r11)
        L_0x006e:
            java.lang.StringBuilder r3 = sInitFrameWorkMsg
            java.lang.String r4 = "| weex JS framework from assets, isSandBoxContext: "
            r3.append(r4)
            boolean r4 = isSandBoxContext
            r3.append(r4)
            r2.taskEnd()
            com.taobao.weex.utils.tools.Info r3 = r2.info
            java.lang.String r3 = r3.taskName
            com.taobao.weex.utils.tools.Time r2 = r2.time
            long r4 = r2.execTime
            java.lang.Long r2 = java.lang.Long.valueOf(r4)
            io.dcloud.weex.WXDotDataUtil.setValue(r3, r2)
        L_0x008d:
            java.lang.String r2 = "jsEngine"
            java.lang.String r3 = "v8"
            io.dcloud.weex.WXDotDataUtil.setValue(r2, r3)
            boolean r2 = android.text.TextUtils.isEmpty(r11)
            r3 = 0
            if (r2 == 0) goto L_0x00af
            r10.setJSFrameworkInit(r3)
            java.lang.StringBuilder r11 = sInitFrameWorkMsg
            java.lang.String r0 = "| framework isEmpty "
            r11.append(r0)
            com.taobao.weex.common.WXErrorCode r11 = com.taobao.weex.common.WXErrorCode.WX_ERR_JS_FRAMEWORK
            r0 = 0
            java.lang.String r2 = "framework is empty!! "
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r0, r11, r1, r2, r0)
            return
        L_0x00af:
            r1 = 1
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.IWXStatisticsListener r2 = r2.getWXStatisticsListener()     // Catch:{ all -> 0x01e1 }
            if (r2 == 0) goto L_0x00ff
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.IWXStatisticsListener r2 = r2.getWXStatisticsListener()     // Catch:{ all -> 0x01e1 }
            r2.onJsFrameworkStart()     // Catch:{ all -> 0x01e1 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01e1 }
            long r6 = r6 - r4
            com.taobao.weex.WXEnvironment.sJSFMStartListenerTime = r6     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x00f7 }
            com.taobao.weex.adapter.IWXUserTrackAdapter r4 = r2.getIWXUserTrackAdapter()     // Catch:{ Exception -> 0x00f7 }
            if (r4 == 0) goto L_0x00ff
            java.util.HashMap r9 = new java.util.HashMap     // Catch:{ Exception -> 0x00f7 }
            r9.<init>(r1)     // Catch:{ Exception -> 0x00f7 }
            java.lang.String r2 = "time"
            long r5 = com.taobao.weex.WXEnvironment.sJSFMStartListenerTime     // Catch:{ Exception -> 0x00f7 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x00f7 }
            r9.put(r2, r5)     // Catch:{ Exception -> 0x00f7 }
            android.app.Application r5 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Exception -> 0x00f7 }
            java.lang.String r6 = "sJSFMStartListener"
            java.lang.String r7 = "counter"
            r8 = 0
            r4.commit(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x00f7 }
            goto L_0x00ff
        L_0x00f7:
            r2 = move-exception
            java.lang.String r2 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r2)     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.WXLogUtils.e(r2)     // Catch:{ all -> 0x01e1 }
        L_0x00ff:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01e1 }
            java.lang.String r2 = ""
            android.app.Application r6 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Exception -> 0x0116 }
            android.content.Context r6 = r6.getApplicationContext()     // Catch:{ Exception -> 0x0116 }
            java.io.File r6 = r6.getCacheDir()     // Catch:{ Exception -> 0x0116 }
            java.lang.String r2 = r6.getPath()     // Catch:{ Exception -> 0x0116 }
            goto L_0x011e
        L_0x0116:
            r6 = move-exception
            java.lang.String r6 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r6)     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.WXLogUtils.e(r6)     // Catch:{ all -> 0x01e1 }
        L_0x011e:
            int r6 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x0125 }
            r7 = 16
            if (r6 >= r7) goto L_0x012d
            goto L_0x012e
        L_0x0125:
            r3 = move-exception
            java.lang.String r3 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r3)     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.WXLogUtils.e(r3)     // Catch:{ all -> 0x01e1 }
        L_0x012d:
            r3 = 1
        L_0x012e:
            java.lang.StringBuilder r6 = sInitFrameWorkMsg     // Catch:{ all -> 0x01e1 }
            java.lang.String r7 = " | pieSupport:"
            r6.append(r7)     // Catch:{ all -> 0x01e1 }
            r6.append(r3)     // Catch:{ all -> 0x01e1 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x01e1 }
            r6.<init>()     // Catch:{ all -> 0x01e1 }
            java.lang.String r7 = "[WXBridgeManager] initFrameworkEnv crashFile:"
            r6.append(r7)     // Catch:{ all -> 0x01e1 }
            r6.append(r2)     // Catch:{ all -> 0x01e1 }
            java.lang.String r7 = " pieSupport:"
            r6.append(r7)     // Catch:{ all -> 0x01e1 }
            r6.append(r3)     // Catch:{ all -> 0x01e1 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.WXLogUtils.d(r6)     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.tools.LogDetail r6 = new com.taobao.weex.utils.tools.LogDetail     // Catch:{ all -> 0x01e1 }
            r6.<init>()     // Catch:{ all -> 0x01e1 }
            java.lang.String r7 = "native initFrameworkEnv"
            r6.name(r7)     // Catch:{ all -> 0x01e1 }
            r6.taskStart()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.common.IWXBridge r7 = r10.mWXBridge     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.bridge.WXParams r8 = r10.assembleDefaultOptions()     // Catch:{ all -> 0x01e1 }
            int r11 = r7.initFrameworkEnv(r11, r8, r2, r3)     // Catch:{ all -> 0x01e1 }
            if (r11 != r1) goto L_0x01c5
            r6.taskEnd()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.tools.Info r11 = r6.info     // Catch:{ all -> 0x01e1 }
            java.lang.String r11 = r11.taskName     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.tools.Time r2 = r6.time     // Catch:{ all -> 0x01e1 }
            long r2 = r2.execTime     // Catch:{ all -> 0x01e1 }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x01e1 }
            io.dcloud.weex.WXDotDataUtil.setValue(r11, r2)     // Catch:{ all -> 0x01e1 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01e1 }
            long r2 = r2 - r4
            com.taobao.weex.WXEnvironment.sJSLibInitTime = r2     // Catch:{ all -> 0x01e1 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01e1 }
            long r4 = com.taobao.weex.WXEnvironment.sSDKInitStart     // Catch:{ all -> 0x01e1 }
            long r2 = r2 - r4
            com.taobao.weex.WXEnvironment.sSDKInitTime = r2     // Catch:{ all -> 0x01e1 }
            r10.setJSFrameworkInit(r1)     // Catch:{ all -> 0x01e1 }
            r0.taskEnd()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.tools.Info r11 = r0.info     // Catch:{ all -> 0x01e1 }
            java.lang.String r11 = r11.taskName     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.utils.tools.Time r0 = r0.time     // Catch:{ all -> 0x01e1 }
            long r2 = r0.execTime     // Catch:{ all -> 0x01e1 }
            java.lang.Long r0 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x01e1 }
            io.dcloud.weex.WXDotDataUtil.setValue(r11, r0)     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.WXSDKManager r11 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.IWXStatisticsListener r11 = r11.getWXStatisticsListener()     // Catch:{ all -> 0x01e1 }
            if (r11 == 0) goto L_0x01b9
            com.taobao.weex.WXSDKManager r11 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.IWXStatisticsListener r11 = r11.getWXStatisticsListener()     // Catch:{ all -> 0x01e1 }
            r11.onJsFrameworkReady()     // Catch:{ all -> 0x01e1 }
        L_0x01b9:
            r10.execRegisterFailTask()     // Catch:{ all -> 0x01e1 }
            com.taobao.weex.WXEnvironment.JsFrameworkInit = r1     // Catch:{ all -> 0x01e1 }
            r10.registerDomModule()     // Catch:{ all -> 0x01e1 }
            r10.trackComponentAndModulesTime()     // Catch:{ all -> 0x01e1 }
            goto L_0x01fd
        L_0x01c5:
            java.lang.StringBuilder r11 = sInitFrameWorkMsg     // Catch:{ all -> 0x01e1 }
            java.lang.String r0 = " | ExecuteJavaScript fail, reInitCount"
            r11.append(r0)     // Catch:{ all -> 0x01e1 }
            int r0 = reInitCount     // Catch:{ all -> 0x01e1 }
            r11.append(r0)     // Catch:{ all -> 0x01e1 }
            int r11 = reInitCount     // Catch:{ all -> 0x01e1 }
            if (r11 <= r1) goto L_0x01db
            java.lang.String r11 = "[WXBridgeManager] invokeReInitFramework  ExecuteJavaScript fail"
            com.taobao.weex.utils.WXLogUtils.e(r11)     // Catch:{ all -> 0x01e1 }
            goto L_0x01fd
        L_0x01db:
            java.lang.String r11 = "[WXBridgeManager] invokeInitFramework  ExecuteJavaScript fail"
            com.taobao.weex.utils.WXLogUtils.e(r11)     // Catch:{ all -> 0x01e1 }
            goto L_0x01fd
        L_0x01e1:
            r11 = move-exception
            java.lang.StringBuilder r0 = sInitFrameWorkMsg
            java.lang.String r2 = " | invokeInitFramework exception "
            r0.append(r2)
            java.lang.String r2 = r11.toString()
            r0.append(r2)
            int r0 = reInitCount
            java.lang.String r2 = "[WXBridgeManager] invokeInitFramework "
            if (r0 <= r1) goto L_0x01fa
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r11)
            goto L_0x01fd
        L_0x01fa:
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r11)
        L_0x01fd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.initFramework(java.lang.String):void");
    }

    private void trackComponentAndModulesTime() {
        post(new Runnable() {
            public void run() {
                WXEnvironment.sComponentsAndModulesReadyTime = System.currentTimeMillis() - WXEnvironment.sSDKInitStart;
            }
        });
    }

    private void invokeCallJSBatch(Message message) {
        if (!this.mNextTickTasks.isEmpty() && isJSFrameworkInit()) {
            try {
                Object obj = message.obj;
                Stack<String> instanceStack = this.mNextTickTasks.getInstanceStack();
                int size = instanceStack.size() - 1;
                ArrayList<WXHashMap<String, Object>> arrayList = null;
                while (true) {
                    if (size >= 0) {
                        obj = instanceStack.get(size);
                        arrayList = this.mNextTickTasks.remove(obj);
                        if (arrayList != null && !arrayList.isEmpty()) {
                            break;
                        }
                        size--;
                    } else {
                        break;
                    }
                }
                if (arrayList != null) {
                    invokeExecJS(String.valueOf(obj), (String) null, METHOD_CALL_JS, new WXJSObject[]{new WXJSObject(2, obj), WXWsonJSONSwitch.toWsonOrJsonWXJSObject(arrayList.toArray())});
                }
            } catch (Throwable th) {
                WXLogUtils.e("WXBridgeManager", th);
                WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_ERR_JS_FRAMEWORK, "invokeCallJSBatch", "invokeCallJSBatch#" + WXLogUtils.getStackTrace(th), (Map<String, String>) null);
            }
            if (!this.mNextTickTasks.isEmpty()) {
                this.mJSHandler.sendEmptyMessage(6);
            }
        } else if (!isJSFrameworkInit()) {
            WXLogUtils.e("[WXBridgeManager] invokeCallJSBatch: framework.js uninitialized!!  message:" + message.toString());
        }
    }

    private WXParams assembleDefaultOptions() {
        checkJsEngineMultiThread();
        Map<String, String> config = WXEnvironment.getConfig();
        WXParams wXParams = new WXParams();
        wXParams.setPlatform(config.get(WXConfig.os));
        wXParams.setCacheDir(config.get(WXConfig.cacheDir));
        wXParams.setOsVersion(config.get(WXConfig.sysVersion));
        wXParams.setAppVersion(config.get(WXConfig.appVersion));
        wXParams.setWeexVersion(config.get(WXConfig.weexVersion));
        wXParams.setDeviceModel(config.get(WXConfig.sysModel));
        wXParams.setShouldInfoCollect(config.get("infoCollect"));
        wXParams.setLogLevel(config.get(WXConfig.logLevel));
        wXParams.setLayoutDirection(config.get(WXConfig.layoutDirection));
        wXParams.setUseSingleProcess(isUseSingleProcess ? AbsoluteConst.TRUE : AbsoluteConst.FALSE);
        wXParams.setCrashFilePath(WXEnvironment.getCrashFilePath(WXEnvironment.getApplication().getApplicationContext()));
        wXParams.setLibJsbPath(WXEnvironment.CORE_JSB_SO_PATH);
        wXParams.setLibJssPath(WXEnvironment.getLibJssRealPath());
        wXParams.setLibIcuPath(WXEnvironment.getLibJssIcuPath());
        wXParams.setLibLdPath(WXEnvironment.getLibLdPath());
        String libJScRealPath = WXEnvironment.getLibJScRealPath();
        wXParams.setLibJscPath(TextUtils.isEmpty(libJScRealPath) ? "" : new File(libJScRealPath).getParent());
        String str = config.get(WXConfig.appName);
        if (!TextUtils.isEmpty(str)) {
            wXParams.setAppName(str);
        }
        wXParams.setDeviceWidth(TextUtils.isEmpty(config.get(WXConfig.deviceWidth)) ? String.valueOf(WXViewUtils.getScreenWidth(WXEnvironment.sApplication)) : config.get(WXConfig.deviceWidth));
        wXParams.setDeviceHeight(TextUtils.isEmpty(config.get(WXConfig.deviceHeight)) ? String.valueOf(WXViewUtils.getScreenHeight((Context) WXEnvironment.sApplication)) : config.get(WXConfig.deviceHeight));
        Map<String, String> customOptions = WXEnvironment.getCustomOptions();
        customOptions.put("enableBackupThread", String.valueOf(jsEngineMultiThreadEnable()));
        IWXJscProcessManager wXJscProcessManager = WXSDKManager.getInstance().getWXJscProcessManager();
        if (wXJscProcessManager != null) {
            customOptions.put("enableBackupThreadCache", String.valueOf(wXJscProcessManager.enableBackUpThreadCache()));
        }
        if (!WXEnvironment.sUseRunTimeApi) {
            customOptions.put("__enable_native_promise__", AbsoluteConst.TRUE);
        }
        wXParams.setOptions(customOptions);
        wXParams.setNeedInitV8(WXSDKManager.getInstance().needInitV8());
        this.mInitParams = wXParams;
        return wXParams;
    }

    public WXParams getInitParams() {
        return this.mInitParams;
    }

    private void execRegisterFailTask() {
        if (this.mRegisterModuleFailList.size() > 0) {
            ArrayList arrayList = new ArrayList();
            int size = this.mRegisterModuleFailList.size();
            for (int i = 0; i < size; i++) {
                invokeRegisterModules(this.mRegisterModuleFailList.get(i), arrayList);
            }
            this.mRegisterModuleFailList.clear();
            if (arrayList.size() > 0) {
                this.mRegisterModuleFailList.addAll(arrayList);
            }
        }
        if (this.mRegisterComponentFailList.size() > 0) {
            ArrayList arrayList2 = new ArrayList();
            invokeRegisterComponents(this.mRegisterComponentFailList, arrayList2);
            this.mRegisterComponentFailList.clear();
            if (arrayList2.size() > 0) {
                this.mRegisterComponentFailList.addAll(arrayList2);
            }
        }
        if (this.mRegisterServiceFailList.size() > 0) {
            ArrayList arrayList3 = new ArrayList();
            for (String invokeExecJSService : this.mRegisterServiceFailList) {
                invokeExecJSService(invokeExecJSService, arrayList3);
            }
            this.mRegisterServiceFailList.clear();
            if (arrayList3.size() > 0) {
                this.mRegisterServiceFailList.addAll(arrayList3);
            }
        }
    }

    public void registerModules(final Map<String, Object> map) {
        if (map != null && map.size() != 0) {
            if (isJSThread()) {
                invokeRegisterModules(map, this.mRegisterModuleFailList);
            } else {
                post(new Runnable() {
                    public void run() {
                        WXBridgeManager wXBridgeManager = WXBridgeManager.this;
                        wXBridgeManager.invokeRegisterModules(map, wXBridgeManager.mRegisterModuleFailList);
                    }
                }, (Object) null);
            }
        }
    }

    public void registerComponents(final List<Map<String, Object>> list) {
        if (this.mJSHandler != null && list != null && list.size() != 0) {
            AnonymousClass24 r0 = new Runnable() {
                public void run() {
                    WXBridgeManager wXBridgeManager = WXBridgeManager.this;
                    wXBridgeManager.invokeRegisterComponents(list, wXBridgeManager.mRegisterComponentFailList);
                }
            };
            if (!isJSThread() || !isJSFrameworkInit()) {
                post(r0);
            } else {
                r0.run();
            }
        }
    }

    public void execJSService(final String str) {
        postWithName(new Runnable() {
            public void run() {
                WXBridgeManager wXBridgeManager = WXBridgeManager.this;
                wXBridgeManager.invokeExecJSService(str, wXBridgeManager.mRegisterServiceFailList);
            }
        }, (WXSDKInstance) null, "execJSService");
    }

    /* access modifiers changed from: private */
    public void invokeExecJSService(String str, List<String> list) {
        try {
            if (!isJSFrameworkInit()) {
                WXLogUtils.e("[WXBridgeManager] invoke execJSService: framework.js uninitialized.");
                list.add(str);
                return;
            }
            this.mWXBridge.execJSService(str);
        } catch (Throwable th) {
            WXLogUtils.e("[WXBridgeManager] invokeRegisterService:", th);
            HashMap hashMap = new HashMap();
            hashMap.put("inputParams", str + Operators.OR + list.toString());
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_INVOKE_JSSERVICE_EXECUTE;
            WXExceptionUtils.commitCriticalExceptionRT("invokeExecJSService", wXErrorCode, "invokeExecJSService", WXErrorCode.WX_KEY_EXCEPTION_INVOKE_JSSERVICE_EXECUTE.getErrorMsg() + "[WXBridgeManager] invokeRegisterService:" + WXLogUtils.getStackTrace(th), hashMap);
        }
    }

    public boolean isJSThread() {
        WXThread wXThread = this.mJSThread;
        return wXThread != null && wXThread.getId() == Thread.currentThread().getId();
    }

    /* access modifiers changed from: private */
    public void invokeRegisterModules(Map<String, Object> map, List<Map<String, Object>> list) {
        String str;
        if (map == null || !isJSFrameworkInit()) {
            if (!isJSFrameworkInit()) {
                WXLogUtils.d("[WXinvokeRegisterModulesBridgeManager] invokeRegisterModules: framework.js uninitialized.");
            }
            list.add(map);
            return;
        }
        WXJSObject[] wXJSObjectArr = {WXWsonJSONSwitch.toWsonOrJsonWXJSObject(map)};
        try {
            IWXBridge iWXBridge = this.mWXBridge;
            if (iWXBridge instanceof WXBridge) {
                ((WXBridge) iWXBridge).registerModuleOnDataRenderNode(WXJsonUtils.fromObjectToJSONString(map));
            }
        } catch (Throwable th) {
            WXLogUtils.e("Weex [data_render register err]", th);
        }
        try {
            str = this.mWXBridge.execJS("", (String) null, METHOD_REGISTER_MODULES, wXJSObjectArr) == 0 ? "execJS error" : null;
            for (String next : map.keySet()) {
                if (next != null) {
                    WXModuleManager.resetModuleState(next, true);
                }
            }
        } catch (Throwable th2) {
            str = WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_MODULES.getErrorMsg() + " \n " + th2.getMessage() + map.entrySet().toString();
        }
        if (!TextUtils.isEmpty(str)) {
            WXLogUtils.e("[WXBridgeManager] invokeRegisterModules:", str);
            WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_MODULES, "invokeRegisterModules", str, (Map<String, String>) null);
        }
    }

    /* access modifiers changed from: private */
    public void invokeRegisterComponents(List<Map<String, Object>> list, List<Map<String, Object>> list2) {
        String str;
        if (list == list2) {
            throw new RuntimeException("Fail receiver should not use source.");
        } else if (!isJSFrameworkInit()) {
            for (Map<String, Object> add : list) {
                list2.add(add);
            }
        } else if (list != null) {
            try {
                IWXBridge iWXBridge = this.mWXBridge;
                if (iWXBridge instanceof WXBridge) {
                    ((WXBridge) iWXBridge).registerComponentOnDataRenderNode(WXJsonUtils.fromObjectToJSONString(list));
                }
            } catch (Throwable th) {
                WXLogUtils.e("Weex [data_render register err]", th);
            }
            WXJSObject[] wXJSObjectArr = {WXWsonJSONSwitch.toWsonOrJsonWXJSObject(list)};
            try {
                str = this.mWXBridge.execJS("", (String) null, METHOD_REGISTER_COMPONENTS, wXJSObjectArr) == 0 ? "execJS error" : null;
            } catch (Throwable th2) {
                str = WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT + wXJSObjectArr.toString() + WXLogUtils.getStackTrace(th2);
            }
            if (!TextUtils.isEmpty(str)) {
                WXLogUtils.e("[WXBridgeManager] invokeRegisterComponents ", str);
                WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_REGISTER_COMPONENT, METHOD_REGISTER_COMPONENTS, str, (Map<String, String>) null);
            }
        }
    }

    public void destroy() {
        WXThread wXThread = this.mJSThread;
        if (wXThread != null) {
            wXThread.quit();
        }
        mBridgeManager = null;
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null) {
            hashSet.clear();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0070, code lost:
        if (r6 == "app-service.js") goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0076, code lost:
        r4 = 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0077, code lost:
        r2 = io.dcloud.common.util.JSONUtil.createJSONObject(io.dcloud.common.util.AppRuntime.getUniStatistics());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007f, code lost:
        if (r2 == null) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0081, code lost:
        r3 = io.dcloud.common.util.JSONUtil.getString(r2, "version");
        r2 = io.dcloud.common.util.JSONUtil.getBoolean(r2, io.dcloud.common.adapter.ui.webview.WebLoadEvent.ENABLE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0094, code lost:
        if (io.dcloud.common.util.PdrUtil.isEquals(io.dcloud.common.util.ExifInterface.GPS_MEASUREMENT_2D, r3) == false) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0096, code lost:
        if (r2 == false) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0098, code lost:
        r7 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0099, code lost:
        if (r7 == false) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x009b, code lost:
        io.dcloud.common.adapter.util.UEH.commitUncatchException(r0, r6, r11, r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reportJSException(java.lang.String r9, java.lang.String r10, java.lang.String r11) {
        /*
            r8 = this;
            java.lang.String r0 = "jsscope::"
            boolean r0 = r10.startsWith(r0)
            r1 = 1
            if (r0 == 0) goto L_0x00a4
            android.app.Application r0 = com.taobao.weex.WXEnvironment.getApplication()
            android.content.Context r0 = r0.getApplicationContext()
            if (r0 == 0) goto L_0x009e
            boolean r2 = io.dcloud.common.util.BaseInfo.isBase(r0)
            if (r2 != 0) goto L_0x009e
            r9.hashCode()
            r2 = -1
            int r3 = r9.hashCode()
            r4 = 4
            java.lang.String r5 = "app-service.js"
            java.lang.String r6 = "uni-jsframework.js"
            r7 = 0
            switch(r3) {
                case -2076326187: goto L_0x0053;
                case -518688385: goto L_0x004a;
                case 3271632: goto L_0x003f;
                case 293174862: goto L_0x0036;
                case 1984153269: goto L_0x002b;
                default: goto L_0x002a;
            }
        L_0x002a:
            goto L_0x005d
        L_0x002b:
            java.lang.String r3 = "service"
            boolean r3 = r9.equals(r3)
            if (r3 != 0) goto L_0x0034
            goto L_0x005d
        L_0x0034:
            r2 = 4
            goto L_0x005d
        L_0x0036:
            boolean r3 = r9.equals(r5)
            if (r3 != 0) goto L_0x003d
            goto L_0x005d
        L_0x003d:
            r2 = 3
            goto L_0x005d
        L_0x003f:
            java.lang.String r3 = "jsfm"
            boolean r3 = r9.equals(r3)
            if (r3 != 0) goto L_0x0048
            goto L_0x005d
        L_0x0048:
            r2 = 2
            goto L_0x005d
        L_0x004a:
            boolean r3 = r9.equals(r6)
            if (r3 != 0) goto L_0x0051
            goto L_0x005d
        L_0x0051:
            r2 = 1
            goto L_0x005d
        L_0x0053:
            java.lang.String r3 = "jsframework"
            boolean r3 = r9.equals(r3)
            if (r3 != 0) goto L_0x005c
            goto L_0x005d
        L_0x005c:
            r2 = 0
        L_0x005d:
            r3 = 5
            switch(r2) {
                case 0: goto L_0x0077;
                case 1: goto L_0x0077;
                case 2: goto L_0x0077;
                case 3: goto L_0x0075;
                case 4: goto L_0x0077;
                default: goto L_0x0061;
            }
        L_0x0061:
            r4 = 6
            com.taobao.weex.WXSDKManager r2 = com.taobao.weex.WXSDKManager.getInstance()
            com.taobao.weex.WXSDKInstance r2 = r2.getSDKInstance(r9)
            if (r2 == 0) goto L_0x0073
            java.lang.String r6 = r2.getUniPagePath()
            if (r6 != r5) goto L_0x0077
            goto L_0x0076
        L_0x0073:
            r6 = r9
            goto L_0x0077
        L_0x0075:
            r6 = r9
        L_0x0076:
            r4 = 5
        L_0x0077:
            java.lang.String r2 = io.dcloud.common.util.AppRuntime.getUniStatistics()
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.createJSONObject(r2)
            if (r2 == 0) goto L_0x0099
            java.lang.String r3 = "version"
            java.lang.String r3 = io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r2, (java.lang.String) r3)
            java.lang.String r5 = "enable"
            boolean r2 = io.dcloud.common.util.JSONUtil.getBoolean(r2, r5)
            java.lang.String r5 = "2"
            boolean r3 = io.dcloud.common.util.PdrUtil.isEquals(r5, r3)
            if (r3 == 0) goto L_0x0099
            if (r2 == 0) goto L_0x0099
            r7 = 1
        L_0x0099:
            if (r7 == 0) goto L_0x009e
            io.dcloud.common.adapter.util.UEH.commitUncatchException(r0, r6, r11, r4)
        L_0x009e:
            r0 = 9
            java.lang.String r10 = r10.substring(r0)
        L_0x00a4:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "reportJSException >>>> instanceId:"
            r0.append(r2)
            r0.append(r9)
            java.lang.String r2 = ", exception function:"
            r0.append(r2)
            r0.append(r10)
            java.lang.String r3 = ", exception:"
            r0.append(r3)
            r0.append(r11)
            java.lang.String r0 = r0.toString()
            com.taobao.weex.utils.WXLogUtils.e(r0)
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_ERR_JS_EXECUTE
            if (r9 == 0) goto L_0x01d3
            com.taobao.weex.WXSDKManager r4 = com.taobao.weex.WXSDKManager.getInstance()
            com.taobao.weex.WXSDKInstance r4 = r4.getSDKInstance(r9)
            if (r4 == 0) goto L_0x01d3
            r4.setHasException(r1)
            java.lang.String r5 = "createInstance"
            boolean r6 = r5.equals(r10)
            if (r6 != 0) goto L_0x00e7
            boolean r6 = r4.isContentMd5Match()
            if (r6 != 0) goto L_0x0173
        L_0x00e7:
            boolean r6 = r8.isSkipFrameworkInit((java.lang.String) r9)     // Catch:{ Exception -> 0x016b }
            if (r6 != 0) goto L_0x010f
            boolean r6 = r8.isJSFrameworkInit()     // Catch:{ Exception -> 0x016b }
            if (r6 == 0) goto L_0x010f
            int r6 = reInitCount     // Catch:{ Exception -> 0x016b }
            if (r6 <= r1) goto L_0x010f
            int r6 = reInitCount     // Catch:{ Exception -> 0x016b }
            r7 = 10
            if (r6 >= r7) goto L_0x010f
            boolean r6 = r4.isNeedReLoad()     // Catch:{ Exception -> 0x016b }
            if (r6 != 0) goto L_0x010f
            com.taobao.weex.ui.action.ActionReloadPage r2 = new com.taobao.weex.ui.action.ActionReloadPage     // Catch:{ Exception -> 0x016b }
            r2.<init>(r9, r1)     // Catch:{ Exception -> 0x016b }
            r2.executeAction()     // Catch:{ Exception -> 0x016b }
            r4.setNeedLoad(r1)     // Catch:{ Exception -> 0x016b }
            return
        L_0x010f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x016b }
            r1.<init>()     // Catch:{ Exception -> 0x016b }
            com.taobao.weex.common.WXErrorCode r6 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED     // Catch:{ Exception -> 0x016b }
            java.lang.String r6 = r6.getErrorMsg()     // Catch:{ Exception -> 0x016b }
            r1.append(r6)     // Catch:{ Exception -> 0x016b }
            java.lang.String r6 = ", reportJSException >>>> instanceId:"
            r1.append(r6)     // Catch:{ Exception -> 0x016b }
            r1.append(r9)     // Catch:{ Exception -> 0x016b }
            r1.append(r2)     // Catch:{ Exception -> 0x016b }
            r1.append(r10)     // Catch:{ Exception -> 0x016b }
            r1.append(r3)     // Catch:{ Exception -> 0x016b }
            r1.append(r11)     // Catch:{ Exception -> 0x016b }
            java.lang.String r2 = ", extInitTime:"
            r1.append(r2)     // Catch:{ Exception -> 0x016b }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x016b }
            long r6 = sInitFrameWorkTimeOrigin     // Catch:{ Exception -> 0x016b }
            long r2 = r2 - r6
            r1.append(r2)     // Catch:{ Exception -> 0x016b }
            java.lang.String r2 = "ms"
            r1.append(r2)     // Catch:{ Exception -> 0x016b }
            java.lang.String r2 = ", extInitErrorMsg:"
            r1.append(r2)     // Catch:{ Exception -> 0x016b }
            java.lang.StringBuilder r2 = sInitFrameWorkMsg     // Catch:{ Exception -> 0x016b }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x016b }
            r1.append(r2)     // Catch:{ Exception -> 0x016b }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x016b }
            com.taobao.weex.common.WXErrorCode r2 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED     // Catch:{ Exception -> 0x016b }
            java.lang.String r2 = r2.getErrorCode()     // Catch:{ Exception -> 0x016b }
            r4.onRenderError(r2, r1)     // Catch:{ Exception -> 0x016b }
            boolean r1 = com.taobao.weex.WXEnvironment.sInAliWeex     // Catch:{ Exception -> 0x016b }
            if (r1 != 0) goto L_0x016a
            com.taobao.weex.common.WXErrorCode r1 = com.taobao.weex.common.WXErrorCode.WX_RENDER_ERR_JS_CREATE_INSTANCE     // Catch:{ Exception -> 0x016b }
            r2 = 0
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r9, r1, r10, r11, r2)     // Catch:{ Exception -> 0x016b }
        L_0x016a:
            return
        L_0x016b:
            r1 = move-exception
            java.lang.String r1 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r1)
            com.taobao.weex.utils.WXLogUtils.e(r1)
        L_0x0173:
            boolean r1 = r5.equals(r10)
            if (r1 == 0) goto L_0x0184
            com.taobao.weex.performance.WXInstanceApm r1 = r4.getApmForInstance()
            boolean r1 = r1.hasAddView
            if (r1 != 0) goto L_0x0184
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_RENDER_ERR_JS_CREATE_INSTANCE
            goto L_0x01cc
        L_0x0184:
            java.lang.String r1 = "createInstanceContext"
            boolean r1 = r1.equals(r10)
            if (r1 == 0) goto L_0x0197
            com.taobao.weex.performance.WXInstanceApm r1 = r4.getApmForInstance()
            boolean r1 = r1.hasAddView
            if (r1 != 0) goto L_0x0197
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_RENDER_ERR_JS_CREATE_INSTANCE_CONTEXT
            goto L_0x01cc
        L_0x0197:
            java.lang.String r1 = "UpdateComponentData"
            boolean r1 = r1.equals(r10)
            if (r1 != 0) goto L_0x01b7
            java.lang.String r1 = "CreatePageWithContent"
            boolean r1 = r1.equals(r10)
            if (r1 != 0) goto L_0x01b7
            java.lang.String r1 = "PostTaskToMsgLoop"
            boolean r1 = r1.equals(r10)
            if (r1 != 0) goto L_0x01b7
            java.lang.String r1 = "JsfmNotInitInEagleMode"
            boolean r1 = r1.equals(r10)
            if (r1 == 0) goto L_0x01c2
        L_0x01b7:
            com.taobao.weex.performance.WXInstanceApm r1 = r4.getApmForInstance()
            boolean r1 = r1.hasAddView
            if (r1 != 0) goto L_0x01c2
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_DEGRAD_EAGLE_RENDER_ERROR
            goto L_0x01cc
        L_0x01c2:
            java.lang.String r1 = "dc_checkappkey"
            boolean r1 = r1.equals(r10)
            if (r1 == 0) goto L_0x01cc
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_VALIDAPPKEY
        L_0x01cc:
            java.lang.String r1 = r0.getErrorCode()
            r4.onJSException(r1, r10, r11)
        L_0x01d3:
            r8.doReportJSException(r9, r10, r0, r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridgeManager.reportJSException(java.lang.String, java.lang.String, java.lang.String):void");
    }

    private void doReportJSException(String str, String str2, WXErrorCode wXErrorCode, String str3) {
        String str4;
        String str5;
        WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
        if (WXSDKManager.getInstance().getIWXJSExceptionAdapter() != null) {
            if (TextUtils.isEmpty(str)) {
                str = "instanceIdisNull";
            }
            if (wXSDKInstance == null && IWXUserTrackAdapter.INIT_FRAMEWORK.equals(str2)) {
                try {
                    if (WXEnvironment.getApplication() != null) {
                        try {
                            File file = new File(WXEnvironment.getApplication().getApplicationContext().getCacheDir().getPath() + INITLOGFILE);
                            if (file.exists()) {
                                if (file.length() > 0) {
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                                        while (true) {
                                            String readLine = bufferedReader.readLine();
                                            if (readLine == null) {
                                                break;
                                            }
                                            sb.append(readLine + "\n");
                                        }
                                        str4 = sb.toString();
                                        try {
                                            bufferedReader.close();
                                        } catch (Exception e) {
                                            Exception exc = e;
                                            str5 = str4;
                                            e = exc;
                                        }
                                    } catch (Exception e2) {
                                        e = e2;
                                        str5 = null;
                                        try {
                                            WXLogUtils.e(WXLogUtils.getStackTrace(e));
                                            str4 = str5;
                                            file.delete();
                                        } catch (Throwable th) {
                                            th = th;
                                            try {
                                                WXLogUtils.e(WXLogUtils.getStackTrace(th));
                                            } catch (Throwable th2) {
                                                th = th2;
                                            }
                                            str4 = str5;
                                            str3 = str3 + "\n" + str4;
                                            WXLogUtils.e("reportJSException:" + str3);
                                            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                                        }
                                        str3 = str3 + "\n" + str4;
                                        WXLogUtils.e("reportJSException:" + str3);
                                        WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                                    }
                                } else {
                                    str4 = null;
                                }
                                try {
                                    file.delete();
                                } catch (Throwable th3) {
                                    str5 = str4;
                                    th = th3;
                                }
                                str3 = str3 + "\n" + str4;
                                WXLogUtils.e("reportJSException:" + str3);
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            str5 = null;
                            WXLogUtils.e(WXLogUtils.getStackTrace(th));
                            str4 = str5;
                            str3 = str3 + "\n" + str4;
                            WXLogUtils.e("reportJSException:" + str3);
                            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                        }
                    }
                    str4 = null;
                } catch (Throwable th5) {
                    th = th5;
                    str5 = null;
                    WXLogUtils.e(WXLogUtils.getStackTrace(th));
                    str4 = str5;
                    str3 = str3 + "\n" + str4;
                    WXLogUtils.e("reportJSException:" + str3);
                    WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
                }
                str3 = str3 + "\n" + str4;
                WXLogUtils.e("reportJSException:" + str3);
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, wXErrorCode, str2, wXErrorCode.getErrorMsg() + str3, (Map<String, String>) null);
        }
    }

    private void registerDomModule() throws WXException {
        HashMap hashMap = new HashMap();
        hashMap.put(WXDomModule.WXDOM, WXDomModule.METHODS);
        registerModules(hashMap);
    }

    public static void updateGlobalConfig(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "none";
        }
        if (!TextUtils.equals(str, globalConfig)) {
            globalConfig = str;
            WXEnvironment.addCustomOptions(GLOBAL_CONFIG_KEY, str);
            AnonymousClass26 r1 = new Runnable() {
                public void run() {
                    if (WXBridgeManager.mBridgeManager != null && WXBridgeManager.mBridgeManager.isJSFrameworkInit() && (WXBridgeManager.mBridgeManager.mWXBridge instanceof WXBridge)) {
                        ((WXBridge) WXBridgeManager.mBridgeManager.mWXBridge).nativeUpdateGlobalConfig(WXBridgeManager.globalConfig);
                    }
                    if (WXBridgeManager.globalConfig.contains(WXWsonJSONSwitch.WSON_OFF)) {
                        WXWsonJSONSwitch.USE_WSON = false;
                    } else {
                        WXWsonJSONSwitch.USE_WSON = true;
                    }
                }
            };
            if (mBridgeManager == null || !mBridgeManager.isJSFrameworkInit()) {
                r1.run();
            } else {
                mBridgeManager.post(r1);
            }
        }
    }

    public Looper getJSLooper() {
        WXThread wXThread = this.mJSThread;
        if (wXThread != null) {
            return wXThread.getLooper();
        }
        return null;
    }

    public void notifySerializeCodeCache() {
        post(new Runnable() {
            public void run() {
                if (WXBridgeManager.this.isJSFrameworkInit()) {
                    WXBridgeManager.this.invokeExecJS("", (String) null, WXBridgeManager.METHOD_NOTIFY_SERIALIZE_CODE_CACHE, new WXJSObject[0]);
                }
            }
        });
    }

    public void takeJSHeapSnapshot(String str) {
        Message obtainMessage = this.mJSHandler.obtainMessage();
        obtainMessage.obj = str;
        obtainMessage.what = 13;
        obtainMessage.setTarget(this.mJSHandler);
        obtainMessage.sendToTarget();
    }

    public int callCreateBody(String str, String str2, String str3, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3) {
        String str4 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            WXLogUtils.d("[WXBridgeManager] call callCreateBody arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str4, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callCreateBody", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet2 = this.mDestroyedInstanceId;
        if (hashSet2 != null && hashSet2.contains(str4)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str4);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionCreateBody graphicActionCreateBody = new GraphicActionCreateBody(sDKInstance, str3, str2, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionCreateBody.getPageId(), graphicActionCreateBody);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callCreateBody exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str4, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callCreateBody", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callAddElement(String str, String str2, String str3, int i, String str4, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3, boolean z) {
        String str5 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callAddElement arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str5, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAddElement", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet2 = this.mDestroyedInstanceId;
        if (hashSet2 != null && hashSet2.contains(str5)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str5);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionAddElement graphicActionAddElement = r5;
            GraphicActionAddElement graphicActionAddElement2 = new GraphicActionAddElement(sDKInstance, str3, str2, str4, i, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3);
            if (z) {
                sDKInstance.addInActiveAddElementAction(str3, graphicActionAddElement);
                return 1;
            }
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str5, graphicActionAddElement);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAddElement exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str5, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAddElement", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callRemoveElement(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveElement arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveElement", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionRemoveElement graphicActionRemoveElement = new GraphicActionRemoveElement(sDKInstance, str2);
            if (sDKInstance.getInActiveAddElementAction(str2) != null) {
                sDKInstance.removeInActiveAddElmentAction(str2);
                return 1;
            }
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionRemoveElement.getPageId(), graphicActionRemoveElement);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveElement exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveElement", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callMoveElement(String str, String str2, String str3, int i) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callMoveElement arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callMoveElement", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionMoveElement graphicActionMoveElement = new GraphicActionMoveElement(sDKInstance, str2, str3, i);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionMoveElement.getPageId(), graphicActionMoveElement);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callMoveElement exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callMoveElement", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callAddEvent(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callAddEvent arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAddEvent", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                new GraphicActionAddEvent(sDKInstance, str2, str3).executeActionOnRender();
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAddEvent exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAddEvent", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        getNextTick(str);
        return 1;
    }

    public int callRemoveEvent(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveEvent arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveEvent", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                new GraphicActionRemoveEvent(sDKInstance, str2, str3).executeActionOnRender();
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveEvent exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveEvent", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        getNextTick(str);
        return 1;
    }

    public int callUpdateStyle(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3, HashMap<String, String> hashMap4) {
        String str3 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateStyle arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateStyle", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateStyle graphicActionUpdateStyle = new GraphicActionUpdateStyle(sDKInstance, str2, hashMap, hashMap2, hashMap3, hashMap4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateStyle.getPageId(), graphicActionUpdateStyle);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateStyle exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateStyle", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callUpdateAttrs(String str, String str2, HashMap<String, String> hashMap) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateAttrs arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateAttrs", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateAttr graphicActionUpdateAttr = new GraphicActionUpdateAttr(sDKInstance, str2, hashMap);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateAttr.getPageId(), graphicActionUpdateAttr);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateAttrs exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateAttrs", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    private void setExceedGPULimitComponentsInfo(String str, String str2, GraphicSize graphicSize) {
        float openGLRenderLimitValue = (float) WXRenderManager.getOpenGLRenderLimitValue();
        if (openGLRenderLimitValue <= 0.0f) {
            return;
        }
        if (graphicSize.getHeight() > openGLRenderLimitValue || graphicSize.getWidth() > openGLRenderLimitValue) {
            JSONObject jSONObject = new JSONObject();
            WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
            jSONObject.put("GPU limit", (Object) String.valueOf(openGLRenderLimitValue));
            jSONObject.put("component.width", (Object) String.valueOf(graphicSize.getWidth()));
            jSONObject.put("component.height", (Object) String.valueOf(graphicSize.getHeight()));
            if (wXComponent.getComponentType() != null && !wXComponent.getComponentType().isEmpty()) {
                jSONObject.put("component.type", (Object) wXComponent.getComponentType());
            }
            if (wXComponent.getStyles() != null && !wXComponent.getStyles().isEmpty()) {
                jSONObject.put("component.style", (Object) wXComponent.getStyles().toString());
            }
            if (wXComponent.getAttrs() != null && !wXComponent.getAttrs().isEmpty()) {
                jSONObject.put("component.attr", (Object) wXComponent.getAttrs().toString());
            }
            if (wXComponent.getEvents() != null && !wXComponent.getEvents().isEmpty()) {
                jSONObject.put("component.event", (Object) wXComponent.getEvents().toString());
            }
            if (wXComponent.getMargin() != null) {
                jSONObject.put("component.margin", (Object) wXComponent.getMargin().toString());
            }
            if (wXComponent.getPadding() != null) {
                jSONObject.put("component.padding", (Object) wXComponent.getPadding().toString());
            }
            if (wXComponent.getBorder() != null) {
                jSONObject.put("component.border", (Object) wXComponent.getBorder().toString());
            }
            WXSDKManager.getInstance().getSDKInstance(str).setComponentsInfoExceedGPULimit(jSONObject);
        }
    }

    public int callAddChildToRichtext(String str, String str2, String str3, String str4, String str5, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        String str6 = str;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str3)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callAddChildToRichtext arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAddChildToRichtext", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionAddChildToRichtext graphicActionAddChildToRichtext = new GraphicActionAddChildToRichtext(sDKInstance, str2, str3, str4, str5, hashMap, hashMap2);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAddChildToRichtext.getPageId(), graphicActionAddChildToRichtext);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAddChildToRichtext exception: ", WXLogUtils.getStackTrace(e));
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAddChildToRichtext", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callRemoveChildFromRichtext(String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveChildFromRichtext arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveChildFromRichtext", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionRemoveChildFromRichtext graphicActionRemoveChildFromRichtext = new GraphicActionRemoveChildFromRichtext(sDKInstance, str2, str3, str4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionRemoveChildFromRichtext.getPageId(), graphicActionRemoveChildFromRichtext);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveChildFromRichtext exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveChildFromRichtext", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public void callBacthStart(String str) {
        if (TextUtils.isEmpty(str)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveChildFromRichtext arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveChildFromRichtext", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                GraphicActionBatchBegin graphicActionBatchBegin = new GraphicActionBatchBegin(sDKInstance, "");
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionBatchBegin.getPageId(), graphicActionBatchBegin);
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveChildFromRichtext exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveChildFromRichtext", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
    }

    public void callBacthEnd(String str) {
        if (TextUtils.isEmpty(str)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callRemoveChildFromRichtext arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRemoveChildFromRichtext", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance != null) {
                GraphicActionBatchEnd graphicActionBatchEnd = new GraphicActionBatchEnd(sDKInstance, "");
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionBatchEnd.getPageId(), graphicActionBatchEnd);
            }
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRemoveChildFromRichtext exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRemoveChildFromRichtext", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
    }

    public int callUpdateRichtextStyle(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateRichtextStyle arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateRichtextStyle", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateRichtextStyle graphicActionUpdateRichtextStyle = new GraphicActionUpdateRichtextStyle(sDKInstance, str2, hashMap, str3, str4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateRichtextStyle.getPageId(), graphicActionUpdateRichtextStyle);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateRichtextStyle exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateRichtextStyle", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callUpdateRichtextChildAttr(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callUpdateRichtextChildAttr arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callUpdateRichtextChildAttr", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicActionUpdateRichtextAttr graphicActionUpdateRichtextAttr = new GraphicActionUpdateRichtextAttr(sDKInstance, str2, hashMap, str3, str4);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionUpdateRichtextAttr.getPageId(), graphicActionUpdateRichtextAttr);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callUpdateRichtextChildAttr exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callUpdateRichtextChildAttr", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callLayout(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, boolean z, int i7) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("[WXBridgeManager] call callLayout arguments is null");
            }
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callLayout", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            GraphicSize graphicSize = new GraphicSize((float) i6, (float) i5);
            GraphicPosition graphicPosition = new GraphicPosition((float) i3, (float) i, (float) i4, (float) i2);
            setExceedGPULimitComponentsInfo(str, str2, graphicSize);
            GraphicActionAddElement inActiveAddElementAction = sDKInstance.getInActiveAddElementAction(str2);
            if (inActiveAddElementAction != null) {
                inActiveAddElementAction.setRTL(z);
                inActiveAddElementAction.setSize(graphicSize);
                inActiveAddElementAction.setPosition(graphicPosition);
                if (!TextUtils.equals(str2, WXComponent.ROOT)) {
                    inActiveAddElementAction.setIndex(i7);
                }
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, inActiveAddElementAction);
                sDKInstance.removeInActiveAddElmentAction(str2);
                return 1;
            }
            GraphicActionLayout graphicActionLayout = new GraphicActionLayout(sDKInstance, str2, graphicPosition, graphicSize, z);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionLayout.getPageId(), graphicActionLayout);
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callLayout exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callLayout", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callAppendTreeCreateFinish(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            WXLogUtils.d("[WXBridgeManager] call callAppendTreeCreateFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callAppendTreeCreateFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionAppendTreeCreateFinish(WXSDKManager.getInstance().getSDKInstance(str), str2));
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callAppendTreeCreateFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callAppendTreeCreateFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callCreateFinish(String str) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callCreateFinish arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callCreateFinish", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            sDKInstance.firstScreenCreateInstanceTime(currentTimeMillis);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionCreateFinish(sDKInstance));
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callCreateFinish exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callCreateFinish", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public int callRenderSuccess(String str) {
        if (TextUtils.isEmpty(str)) {
            WXLogUtils.d("[WXBridgeManager] call callRenderSuccess arguments is null");
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_RENDER_ERR_BRIDGE_ARG_NULL, "callRenderSuccess", "arguments is empty, INSTANCE_RENDERING_ERROR will be set", (Map<String, String>) null);
            return 0;
        }
        WXEnvironment.isApkDebugable();
        HashSet<String> hashSet = this.mDestroyedInstanceId;
        if (hashSet != null && hashSet.contains(str)) {
            return -1;
        }
        try {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
            if (sDKInstance == null) {
                return 1;
            }
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(str, new GraphicActionRenderSuccess(sDKInstance));
            return 1;
        } catch (Exception e) {
            WXLogUtils.e("[WXBridgeManager] callRenderSuccess exception: ", (Throwable) e);
            WXExceptionUtils.commitCriticalExceptionRT(str, WXErrorCode.WX_KEY_EXCEPTION_INVOKE_BRIDGE, "callRenderSuccess", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
            return 1;
        }
    }

    public ContentBoxMeasurement getMeasurementFunc(String str, long j) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            return sDKInstance.getContentBoxMeasurement(j);
        }
        return null;
    }

    public void bindMeasurementToRenderObject(long j) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.bindMeasurementToRenderObject(j);
        }
    }

    public boolean notifyLayout(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            return this.mWXBridge.notifyLayout(str);
        }
        return false;
    }

    public void forceLayout(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.forceLayout(str);
        }
    }

    public void onInstanceClose(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.onInstanceClose(str);
        }
    }

    public void setDefaultRootSize(String str, float f, float f2, boolean z, boolean z2) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setDefaultHeightAndWidthIntoRootDom(str, f, f2, z, z2);
        }
    }

    public void setRenderContentWrapContentToCore(boolean z, String str) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.setRenderContainerWrapContent(z, str);
        }
    }

    public void setStyleWidth(String str, String str2, float f) {
        setStyleWidth(str, str2, f, false);
    }

    public void setStyleWidth(String str, String str2, float f, boolean z) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setStyleWidth(str, str2, f, z);
        }
    }

    public void setStyleHeight(String str, String str2, float f) {
        setStyleHeight(str, str2, f, false);
    }

    public void setStyleHeight(String str, String str2, float f, boolean z) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setStyleHeight(str, str2, f, z);
        }
    }

    public long[] getFirstScreenRenderTime(String str) {
        return isJSFrameworkInit() ? this.mWXBridge.getFirstScreenRenderTime(str) : new long[]{0, 0, 0};
    }

    public long[] getRenderFinishTime(String str) {
        return isJSFrameworkInit() ? this.mWXBridge.getRenderFinishTime(str) : new long[]{0, 0, 0};
    }

    public void setDeviceDisplay(String str, float f, float f2, float f3) {
        final String str2 = str;
        final float f4 = f;
        final float f5 = f2;
        final float f6 = f3;
        post(new Runnable() {
            public void run() {
                WXBridgeManager.this.mWXBridge.setDeviceDisplay(str2, f4, f5, f6);
            }
        });
    }

    public void updateInitDeviceParams(final String str, final String str2, final String str3, final String str4) {
        if (isJSFrameworkInit()) {
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.mWXBridge.updateInitFrameworkParams(WXConfig.deviceWidth, str, WXConfig.deviceWidth);
                }
            });
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.mWXBridge.updateInitFrameworkParams(WXConfig.deviceHeight, str2, WXConfig.deviceHeight);
                }
            });
            post(new Runnable() {
                public void run() {
                    WXBridgeManager.this.mWXBridge.updateInitFrameworkParams("scale", str3, "scale");
                }
            });
            if (str4 != null) {
                post(new Runnable() {
                    public void run() {
                        WXBridgeManager.this.mWXBridge.updateInitFrameworkParams(WXConfig.androidStatusBarHeight, str4, WXConfig.androidStatusBarHeight);
                    }
                });
            }
        }
    }

    public void setMargin(String str, String str2, CSSShorthand.EDGE edge, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setMargin(str, str2, edge, f);
        }
    }

    public void setPadding(String str, String str2, CSSShorthand.EDGE edge, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setPadding(str, str2, edge, f);
        }
    }

    public void setPosition(String str, String str2, CSSShorthand.EDGE edge, float f) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setPosition(str, str2, edge, f);
        }
    }

    public void markDirty(String str, String str2, boolean z) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.markDirty(str, str2, z);
        }
    }

    public void setPageArgument(String str, String str2, String str3) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setPageArgument(str, str2, str3);
        }
    }

    public void reloadPageLayout(String str) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.reloadPageLayout(str);
        }
    }

    public void setDeviceDisplayOfPage(String str, float f, float f2) {
        if (isSkipFrameworkInit(str) || isJSFrameworkInit()) {
            this.mWXBridge.setDeviceDisplayOfPage(str, f, f2);
        }
    }

    public int callHasTransitionPros(String str, String str2, HashMap<String, String> hashMap) {
        WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(str, str2);
        if (wXComponent == null || wXComponent.getTransition() == null || wXComponent.getTransition().getProperties() == null) {
            return -1;
        }
        for (String containsKey : wXComponent.getTransition().getProperties()) {
            if (hashMap.containsKey(containsKey)) {
                return 1;
            }
        }
        return 0;
    }

    public void registerCoreEnv(String str, String str2) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.registerCoreEnv(str, str2);
        } else {
            mWeexCoreEnvOptions.put(str, str2);
        }
    }

    private void onJsFrameWorkInitSuccees() {
        for (Map.Entry next : mWeexCoreEnvOptions.entrySet()) {
            this.mWXBridge.registerCoreEnv((String) next.getKey(), (String) next.getValue());
        }
        mWeexCoreEnvOptions.clear();
    }

    public void setViewPortWidth(String str, float f) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.setViewPortWidth(str, f);
        }
    }

    public void setFlexDirectionDef(String str) {
        if (isJSFrameworkInit()) {
            this.mWXBridge.setFlexDirectionDef(str);
        }
    }

    public String getWeexCoreThreadStackTrace() {
        if (this.mJSThread == null) {
            return "null == mJSThread";
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(StringUtil.format("Thread Name: '%s'\n", this.mJSThread.getName()));
            sb.append(String.format(Locale.ENGLISH, "\"%s\" prio=%d tid=%d %s\n", new Object[]{this.mJSThread.getName(), Integer.valueOf(this.mJSThread.getPriority()), Long.valueOf(this.mJSThread.getId()), this.mJSThread.getState()}));
            StackTraceElement[] stackTrace = this.mJSThread.getStackTrace();
            int length = stackTrace.length;
            for (int i = 0; i < length; i++) {
                sb.append(StringUtil.format("\tat %s\n", stackTrace[i].toString()));
            }
        } catch (Exception e) {
            Log.e("weex", "getJSThreadStackTrace error:", e);
        }
        return sb.toString();
    }

    public String encryptGetClientKeyPayload(String str, String str2, String str3) {
        IWXBridge iWXBridge = this.mWXBridge;
        return iWXBridge instanceof WXBridge ? ((WXBridge) iWXBridge).encryptGetClientKeyPayload(str, str2, str3) : "";
    }

    public String encrypt(String str, String str2, String str3, String str4) {
        IWXBridge iWXBridge = this.mWXBridge;
        return iWXBridge instanceof WXBridge ? ((WXBridge) iWXBridge).encrypt(str, str2, str3, str4) : "";
    }

    public String decrypt(String str, String str2, String str3, String str4) {
        IWXBridge iWXBridge = this.mWXBridge;
        return iWXBridge instanceof WXBridge ? ((WXBridge) iWXBridge).decrypt(str, str2, str3, str4) : "";
    }

    public boolean verifyClientKeyPayload(String str, String str2, String str3) {
        IWXBridge iWXBridge = this.mWXBridge;
        if (iWXBridge instanceof WXBridge) {
            return ((WXBridge) iWXBridge).verifyClientKeyPayload(str, str2, str3);
        }
        return false;
    }
}
