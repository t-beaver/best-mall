package io.dcloud.feature.weex;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.alibaba.android.bindingx.plugin.weex.BindingX;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.taobao.weex.IWXStatisticsListener;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.RenderTypes;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.SimpleComponentHolder;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.tools.TimeCalculator;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IConfusionMgr;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.INativeAppInfo;
import io.dcloud.common.DHInterface.IUniInstanceMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.io.UnicodeInputStream;
import io.dcloud.common.adapter.ui.webview.WebLoadEvent;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CustomPath;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.uniapp.UniMoudlesLoader;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.weex.adapter.DCDefaultConfigAdapter;
import io.dcloud.feature.weex.adapter.DCVueBridgeAdapter;
import io.dcloud.feature.weex.adapter.DCWXHttpAdapter;
import io.dcloud.feature.weex.adapter.DefaultWebSocketAdapterFactory;
import io.dcloud.feature.weex.adapter.Fresco.DCGenericDraweeView;
import io.dcloud.feature.weex.adapter.FrescoDrawableLoader;
import io.dcloud.feature.weex.adapter.FrescoImageAdapter;
import io.dcloud.feature.weex.adapter.FrescoImageComponent;
import io.dcloud.feature.weex.adapter.FrescoImageComponentU;
import io.dcloud.feature.weex.adapter.JSExceptionAdapter;
import io.dcloud.feature.weex.adapter.PlusUriAdapter;
import io.dcloud.feature.weex.adapter.ScalableViewComponent;
import io.dcloud.feature.weex.adapter.webview.WXDCWeb;
import io.dcloud.feature.weex.extend.DCCoverImageComponent;
import io.dcloud.feature.weex.extend.DCCoverViewComponent;
import io.dcloud.feature.weex.extend.DCTabBarModule;
import io.dcloud.feature.weex.extend.DCUniMPModule;
import io.dcloud.feature.weex.extend.DCWXSlider;
import io.dcloud.feature.weex.extend.DCWXView;
import io.dcloud.feature.weex.extend.PlusModule;
import io.dcloud.feature.weex.extend.PlusStorageModule;
import io.dcloud.feature.weex.extend.RandomBytesModule;
import io.dcloud.feature.weex.extend.WXEventModule;
import io.dcloud.feature.weex_websocket.UniWebSocketModule;
import io.dcloud.weex.MoudlesLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeexInstanceMgr implements IWXStatisticsListener, IUniInstanceMgr {
    static final String SERVICE_NAME = "weexPlus";
    private static WeexInstanceMgr instance;
    String TAG = "WeexInstanceMgr";
    private ArrayList<IWXStatisticsCallBack> callBacks = new ArrayList<>();
    private String complier = "weex";
    private String control = AbsoluteConst.UNI_V3;
    private AbsMgr featureMgr;
    private LinkedHashMap<String, WXViewWrapper> instanceHashMap = new LinkedHashMap<>(16);
    private boolean isAssetsRes = false;
    private boolean isJSFKFileNotFound = false;
    private boolean isJsFrameworkReady = false;
    private boolean isUniServiceCreated = false;
    private boolean isWeexInitEnd = false;
    private String jsSACName = "uni-app-config";
    /* access modifiers changed from: private */
    public Application mApplication = null;
    private IConfusionMgr mConfusionMgr;
    private Handler mHandler;
    private String mPreInstanceId = null;
    private String mPreUniAppid = null;
    private Map<String, ICallBack> mPreUniMPCallBackMap = new HashMap();
    private ArrayList<ICallBack> mReladyCallBacks = new ArrayList<>();
    /* access modifiers changed from: private */
    public ICallBack mRestartReadyCall;
    private String mUniNViewModules = null;
    private int mVueVersion = 2;
    private String render = "auto";
    private StringBuffer sb;
    private HashMap<String, WXServiceWrapper> serviceWrapperMapsCache = new HashMap<>(3);

    interface EachListener<T> {
        void onEach(T t);
    }

    public interface IWXStatisticsCallBack {
        void onJsFrameworkReady();
    }

    public void onException(String str, String str2, String str3) {
    }

    public void onFirstScreen() {
    }

    public void onFirstView() {
    }

    public void onHeadersReceived() {
    }

    public void onHttpFinish() {
    }

    public void onHttpStart() {
    }

    public void onJsFrameworkStart() {
    }

    public void onSDKEngineInitialize() {
    }

    public static synchronized WeexInstanceMgr self() {
        WeexInstanceMgr weexInstanceMgr;
        synchronized (WeexInstanceMgr.class) {
            if (instance == null) {
                instance = new WeexInstanceMgr();
            }
            weexInstanceMgr = instance;
        }
        return weexInstanceMgr;
    }

    public void setUniNViewModules(String str) {
        this.mUniNViewModules = str;
        if (this.mReladyCallBacks.size() > 0) {
            Iterator<ICallBack> it = this.mReladyCallBacks.iterator();
            while (it.hasNext()) {
                it.next().onCallBack(0, str);
            }
            this.mReladyCallBacks.clear();
        }
    }

    public String getUniNViewModules() {
        return this.mUniNViewModules;
    }

    public void setUniNViewModuleReladyCallBack(ICallBack iCallBack) {
        if (!this.mReladyCallBacks.contains(iCallBack)) {
            this.mReladyCallBacks.add(iCallBack);
        }
    }

    /* access modifiers changed from: package-private */
    public void init(AbsMgr absMgr) {
        this.featureMgr = absMgr;
    }

    public String getUniMPFeature() {
        return "UniMP".toLowerCase(Locale.ENGLISH);
    }

    public void initWeexEnv(INativeAppInfo iNativeAppInfo) {
        if (iNativeAppInfo != null) {
            this.mConfusionMgr = iNativeAppInfo.getCofusionMgr();
            initWeexEnv(iNativeAppInfo.getApplication());
        }
    }

    public void initWeexEnv(Application application) {
        if (!SDK.isUniMPSDK()) {
            PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "registerReloadReceiver", (Object) null, new Class[]{Application.class}, new Object[]{application});
        }
        self().setApplication(application);
        if (!WXSDKEngine.isInitialized()) {
            InitConfig.Builder builder = new InitConfig.Builder();
            self().initAppForPath(application, (String) null);
            self().initJSFramework(application, (String) null);
            if (!SDK.isUniMPSDK()) {
                PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexDevtoolImpl", "initDebugEnvironment", (Object) null, new Class[]{Application.class}, new Object[]{application});
            }
            builder.setHttpAdapter(new DCWXHttpAdapter());
            Fresco.initialize(application);
            JSON.setDefaultTypeKey("@type_ft");
            DCGenericDraweeView.initialize(Fresco.getDraweeControllerBuilderSupplier());
            builder.setImgAdapter(new FrescoImageAdapter());
            builder.setDrawableLoader(new FrescoDrawableLoader(application));
            builder.setURIAdapter(new PlusUriAdapter());
            builder.setDCVueBridgeAdapter(new DCVueBridgeAdapter());
            builder.setWebSocketAdapterFactory(new DefaultWebSocketAdapterFactory());
            builder.setJSExceptionAdapter(new JSExceptionAdapter());
            InitConfig build = builder.build();
            WXSDKManager.getInstance().setWxConfigAdapter(new DCDefaultConfigAdapter());
            WXSDKEngine.initialize(application, build);
            self().initStatisticsListener();
            try {
                String str = new String(PlatformUtil.getFileContent("io/dcloud/weexUniJs.js", 1));
                this.sb = new StringBuffer();
                String configParam = self().getConfigParam();
                StringBuffer stringBuffer = this.sb;
                stringBuffer.append("var plusContext = {};plusContext.getLocationHerf = function(plus){\n    return plus.weex.config.bundleUrl;\n};var param = " + configParam + ";");
                this.sb.append(str);
                WXSDKEngine.registerComponent("image", (Class<? extends WXComponent>) FrescoImageComponent.class);
                WXSDKEngine.registerComponent("cover-view", (Class<? extends WXComponent>) DCCoverViewComponent.class);
                WXSDKEngine.registerComponent("u-image", (Class<? extends WXComponent>) FrescoImageComponentU.class);
                WXSDKEngine.registerComponent("cover-image", (Class<? extends WXComponent>) DCCoverImageComponent.class);
                WXSDKEngine.registerComponent((IFComponentHolder) new SimpleComponentHolder(ScalableViewComponent.class, new ScalableViewComponent.Ceator()), false, "u-scalable");
                WXSDKEngine.registerComponent((IFComponentHolder) new SimpleComponentHolder(DCWXSlider.class, new DCWXSlider.Creator()), true, WXBasicComponentType.SLIDER);
                WXSDKEngine.registerComponent((IFComponentHolder) new SimpleComponentHolder(DCWXView.class, new DCWXView.Ceator()), false, WXBasicComponentType.VIEW);
                WXSDKEngine.registerComponent("u-web-view", (Class<? extends WXComponent>) WXDCWeb.class);
                WXSDKEngine.registerModule(IApp.ConfigProperty.CONFIG_PLUS, PlusModule.class);
                WXSDKEngine.registerModule("DCloud-Crypto", RandomBytesModule.class);
                WXSDKEngine.registerModule("plusstorage", PlusStorageModule.class);
                WXSDKEngine.registerModule("uni-tabview", DCTabBarModule.class);
                addComponentByName(WXBasicComponentType.DIV, ScalableViewComponent.class);
                if (PdrUtil.isUniMPHostForUniApp() && !SDK.isUniMPSDK()) {
                    WXSDKEngine.registerModule("uniMP", DCUniMPModule.class);
                }
                WXSDKEngine.registerModule("event", WXEventModule.class);
                if (this.sb != null) {
                    WXSDKEngine.registerService(SERVICE_NAME, this.sb.toString(), new HashMap());
                }
                if (!SDK.isUniMPSDK()) {
                    self().registerUniappService(application, "");
                }
                WXEnvironment.setGlobalFontFamily("unincomponents", Typeface.createFromAsset(application.getAssets(), "fonts/unincomponents.ttf"));
                WXSDKEngine.registerModule("uni-webSocket", UniWebSocketModule.class);
                BindingX.register();
                registerReflexWeexPlugin(application);
                setWeexInitEnd(true);
                loadDex(application);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setApplication(Application application) {
        this.mApplication = application;
    }

    public void restartWeex(final Application application, final ICallBack iCallBack, final String str) {
        if (((getControl().equals(AbsoluteConst.UNI_V3) && isWeexInitEnd() && !BaseInfo.isFirstRun) || (SDK.isUniMPSDK() && isWeexInitEnd())) && this.mPreInstanceId == null) {
            Logger.e(this.TAG, "restartWeex-------");
            if (this.instanceHashMap.size() > 0) {
                onActivityDestroy(false);
            }
            getHandler().post(new Runnable() {
                public void run() {
                    WeexInstanceMgr.this.unRegisterUniappService();
                    ICallBack unused = WeexInstanceMgr.this.mRestartReadyCall = iCallBack;
                    Application unused2 = WeexInstanceMgr.this.mApplication = application;
                    WeexInstanceMgr weexInstanceMgr = WeexInstanceMgr.this;
                    weexInstanceMgr.initAppForPath(weexInstanceMgr.mApplication, str);
                    WeexInstanceMgr.this.setJsFrameworkReady(false);
                    WeexInstanceMgr.this.initJSFramework(application, str);
                    WeexInstanceMgr.this.registerUniappService(application, str);
                    WXSDKEngine.restartWeex();
                }
            });
        }
    }

    public void loadWeexToAppid(Context context, String str, boolean z) {
        int vueVersion = getVueVersion();
        initAppForPath(context, str);
        initJSFramework(context, str);
        if (getVueVersion() != vueVersion) {
            z = true;
        }
        if (!WXBridgeManager.getInstance().isJSFrameworkInit() || z) {
            setJsFrameworkReady(false);
            WXSDKEngine.restartWeex();
        }
    }

    public void onSubProcess(Application application) {
        MoudlesLoader.getInstance().onSubProcess(application);
    }

    public void initStatisticsListener() {
        WXSDKManager.getInstance().registerStatisticsListener(this);
    }

    /* access modifiers changed from: private */
    public void initJSFramework(Context context, String str) {
        JSONObject jSONObject;
        JSONObject jSONObject2;
        Context context2 = context;
        String str2 = "uniCloud";
        String str3 = "version";
        String str4 = BaseInfo.sConfigXML;
        String str5 = WebLoadEvent.ENABLE;
        InputStream appFileStream = getAppFileStream(context2, str, str4);
        if (appFileStream != null) {
            try {
                JSONObject parseObject = JSON.parseObject(handleEncryptionInputStream(appFileStream, context2, true));
                if (parseObject != null && parseObject.containsKey(IApp.ConfigProperty.CONFIG_PLUS)) {
                    JSONObject jSONObject3 = parseObject.getJSONObject(IApp.ConfigProperty.CONFIG_PLUS);
                    if (jSONObject3 != null) {
                        if (jSONObject3.containsKey("uni-app")) {
                            JSONObject jSONObject4 = jSONObject3.getJSONObject("uni-app");
                            self().control = AbsoluteConst.UNI_V3;
                            if (jSONObject4.containsKey("renderer")) {
                                self().render = jSONObject4.getString("renderer");
                            }
                            if (jSONObject4.containsKey("nvueCompiler")) {
                                self().complier = jSONObject4.getString("nvueCompiler");
                            }
                            if (jSONObject4.containsKey("vueVersion")) {
                                self().mVueVersion = jSONObject4.getIntValue("vueVersion");
                            } else {
                                self().mVueVersion = 2;
                            }
                            if (jSONObject4.containsKey("useJSProcess")) {
                                if (AbsoluteConst.FALSE.equals(jSONObject4.getString("useJSProcess"))) {
                                    WXBridgeManager.getInstance().setUseSingleProcess(true);
                                } else {
                                    WXBridgeManager.getInstance().setUseSingleProcess(false);
                                }
                            }
                            if (jSONObject4.containsKey("webView") && (jSONObject2 = jSONObject4.getJSONObject("webView")) != null) {
                                try {
                                    if (jSONObject2.containsKey("minUserAgentVersion")) {
                                        BaseInfo.minUserAgentVersion = jSONObject2.getString("minUserAgentVersion");
                                    }
                                    if (jSONObject2.containsKey("x5")) {
                                        JSONObject jSONObject5 = jSONObject2.getJSONObject("x5");
                                        if (jSONObject5.containsKey("timeOut")) {
                                            BaseInfo.timeOut = jSONObject5.getIntValue("timeOut");
                                        }
                                        if (jSONObject5.containsKey("showTipsWithoutWifi")) {
                                            BaseInfo.showTipsWithoutWifi = jSONObject5.getBooleanValue("showTipsWithoutWifi");
                                        }
                                        if (jSONObject5.containsKey("allowDownloadWithoutWiFi")) {
                                            BaseInfo.allowDownloadWithoutWiFi = jSONObject5.getBooleanValue("allowDownloadWithoutWiFi");
                                        }
                                    }
                                } catch (Exception unused) {
                                }
                            }
                        }
                    }
                    if (jSONObject3 != null) {
                        if (jSONObject3.containsKey(IApp.ConfigProperty.CONFIG_UNISTATISTICS) && (jSONObject = jSONObject3.getJSONObject(IApp.ConfigProperty.CONFIG_UNISTATISTICS)) != null) {
                            JSONObject jSONObject6 = new JSONObject();
                            String str6 = str5;
                            try {
                                if (jSONObject.containsKey(str6)) {
                                    jSONObject6.put(str6, jSONObject.get(str6));
                                }
                                String str7 = str3;
                                if (jSONObject.containsKey(str7)) {
                                    jSONObject6.put(str7, jSONObject.get(str7));
                                }
                                String str8 = str2;
                                if (jSONObject.containsKey(str8)) {
                                    jSONObject6.put(str8, (Object) jSONObject.getJSONObject(str8));
                                }
                                AppRuntime.setUniStatistics(jSONObject6.toJSONString());
                            } catch (Exception unused2) {
                            }
                        }
                    }
                    if (jSONObject3 == null) {
                        return;
                    }
                    if (jSONObject3.containsKey("renderer")) {
                        BaseInfo.renderer = jSONObject3.getString("renderer");
                    }
                }
            } catch (Exception unused3) {
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.InputStream getAppFileStream(android.content.Context r5, java.lang.String r6, java.lang.String r7) {
        /*
            r4 = this;
            r0 = 0
            io.dcloud.common.adapter.util.AndroidResources.initAndroidResources(r5)     // Catch:{ Exception -> 0x00bb }
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x00bb }
            if (r1 == 0) goto L_0x000e
            io.dcloud.common.util.BaseInfo.parseControl()     // Catch:{ Exception -> 0x00bb }
            goto L_0x0010
        L_0x000e:
            io.dcloud.common.util.BaseInfo.sDefaultBootApp = r6     // Catch:{ Exception -> 0x00bb }
        L_0x0010:
            boolean r6 = io.dcloud.common.util.BaseInfo.ISDEBUG     // Catch:{ Exception -> 0x00bb }
            if (r6 != 0) goto L_0x001d
            boolean r6 = io.dcloud.common.adapter.io.DHFile.hasFile()     // Catch:{ Exception -> 0x00bb }
            if (r6 == 0) goto L_0x001b
            goto L_0x001d
        L_0x001b:
            r6 = 0
            goto L_0x001e
        L_0x001d:
            r6 = 1
        L_0x001e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bb }
            r1.<init>()     // Catch:{ Exception -> 0x00bb }
            java.lang.String r2 = "apps/"
            r1.append(r2)     // Catch:{ Exception -> 0x00bb }
            java.lang.String r2 = io.dcloud.common.util.BaseInfo.sDefaultBootApp     // Catch:{ Exception -> 0x00bb }
            r1.append(r2)     // Catch:{ Exception -> 0x00bb }
            java.lang.String r2 = "/www/"
            r1.append(r2)     // Catch:{ Exception -> 0x00bb }
            r1.append(r7)     // Catch:{ Exception -> 0x00bb }
            java.lang.String r7 = r1.toString()     // Catch:{ Exception -> 0x00bb }
            boolean r1 = r4.isAssetsRes     // Catch:{ Exception -> 0x00bb }
            if (r1 == 0) goto L_0x0054
            boolean r1 = io.dcloud.common.util.BaseInfo.SyncDebug     // Catch:{ Exception -> 0x00bb }
            if (r1 != 0) goto L_0x0054
            boolean r1 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x00bb }
            if (r1 != 0) goto L_0x0054
            android.content.res.Resources r1 = r5.getResources()     // Catch:{ Exception -> 0x0054 }
            android.content.res.AssetManager r1 = r1.getAssets()     // Catch:{ Exception -> 0x0054 }
            java.io.InputStream r1 = r1.open(r7)     // Catch:{ Exception -> 0x0054 }
            goto L_0x0055
        L_0x0054:
            r1 = r0
        L_0x0055:
            if (r1 != 0) goto L_0x00c0
            if (r6 == 0) goto L_0x007a
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b8 }
            r6.<init>()     // Catch:{ Exception -> 0x00b8 }
            java.io.File r2 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r2 = r2.getPath()     // Catch:{ Exception -> 0x00b8 }
            r6.append(r2)     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r2 = "/Android/data/"
            r6.append(r2)     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r2 = r5.getPackageName()     // Catch:{ Exception -> 0x00b8 }
            r6.append(r2)     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x00b8 }
            goto L_0x0082
        L_0x007a:
            java.io.File r6 = r5.getFilesDir()     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r6 = r6.getPath()     // Catch:{ Exception -> 0x00b8 }
        L_0x0082:
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x00b8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b8 }
            r3.<init>()     // Catch:{ Exception -> 0x00b8 }
            r3.append(r6)     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r6 = "/"
            r3.append(r6)     // Catch:{ Exception -> 0x00b8 }
            r3.append(r7)     // Catch:{ Exception -> 0x00b8 }
            java.lang.String r6 = r3.toString()     // Catch:{ Exception -> 0x00b8 }
            r2.<init>(r6)     // Catch:{ Exception -> 0x00b8 }
            boolean r6 = r2.exists()     // Catch:{ Exception -> 0x00b8 }
            if (r6 == 0) goto L_0x00a7
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00b8 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x00b8 }
            goto L_0x00b5
        L_0x00a7:
            android.content.res.Resources r5 = r5.getResources()     // Catch:{ Exception -> 0x00b4 }
            android.content.res.AssetManager r5 = r5.getAssets()     // Catch:{ Exception -> 0x00b4 }
            java.io.InputStream r0 = r5.open(r7)     // Catch:{ Exception -> 0x00b4 }
            goto L_0x00b5
        L_0x00b4:
        L_0x00b5:
            if (r0 == 0) goto L_0x00c0
            goto L_0x00bf
        L_0x00b8:
            r5 = move-exception
            r0 = r1
            goto L_0x00bc
        L_0x00bb:
            r5 = move-exception
        L_0x00bc:
            r5.printStackTrace()
        L_0x00bf:
            r1 = r0
        L_0x00c0:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.WeexInstanceMgr.getAppFileStream(android.content.Context, java.lang.String, java.lang.String):java.io.InputStream");
    }

    public void initAppForPath(Context context, String str) {
        this.isAssetsRes = AppRuntime.isAppResourcesInAssetsPath(context, str);
    }

    private String handleEncryptionInputStream(InputStream inputStream, Context context, boolean z) {
        System.currentTimeMillis();
        if (z) {
            inputStream = new UnicodeInputStream(inputStream, Charset.defaultCharset().name());
        }
        byte[] bArr = new byte[0];
        try {
            bArr = IOUtil.getBytes(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = null;
        IConfusionMgr iConfusionMgr = this.mConfusionMgr;
        if (iConfusionMgr != null) {
            str = iConfusionMgr.handleEncryption(context.getApplicationContext(), bArr);
        }
        return TextUtils.isEmpty(str) ? new String(bArr) : str;
    }

    public void registerUniappService(Context context, String str) {
        InputStream appFileStream;
        if (self().control.equals(AbsoluteConst.UNI_V3) && (appFileStream = self().getAppFileStream(context, str, "app-config-service.js")) != null) {
            try {
                WXSDKEngine.registerService(this.jsSACName, handleEncryptionInputStream(appFileStream, context, false), new HashMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isUniAppAssetsRes() {
        return this.isAssetsRes;
    }

    /* access modifiers changed from: private */
    public void unRegisterUniappService() {
        WXSDKEngine.unRegisterService(this.jsSACName);
    }

    private void registerReflexWeexPlugin(Context context) {
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_amap.AMapPluginImpl", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex.map.google.GoogleMapPluginImpl", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_scroller.DCScrollerPluginImpl", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_barcode.BarcodePlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.utsplugin.UTSPlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_livepusher.LivePusherPlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_media.VideoPlayerPlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_text.DCWXTextPlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_input.DCWXInputRegister", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.gcanvas.GCanvasRegister", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_switch.DCWXSwitchPlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        PlatformUtil.invokeMethod("io.dcloud.feature.weex_ad.DCWXAdPlugin", "initPlugin", (Object) null, new Class[]{Context.class}, new Object[]{context});
        Object invokeFieldValue = PlatformUtil.invokeFieldValue("com.taobao.weex.devtools.inspector.elements.android.WXComponentDescriptor", "sClassName", (Object) null);
        if (invokeFieldValue != null && (invokeFieldValue instanceof HashMap)) {
            HashMap hashMap = (HashMap) invokeFieldValue;
            hashMap.put(FrescoImageComponent.class, "image");
            hashMap.put(FrescoImageComponentU.class, "image");
            hashMap.put(WXDCWeb.class, "web-view");
            hashMap.put(DCWXView.class, WXBasicComponentType.VIEW);
            hashMap.put(DCCoverViewComponent.class, "cover-view");
            hashMap.put(DCWXSlider.class, WXBasicComponentType.SLIDER);
        }
    }

    public void addComponentByName(String str, Class cls) {
        try {
            Object invokeFieldValue = PlatformUtil.invokeFieldValue("com.taobao.weex.devtools.inspector.elements.android.WXComponentDescriptor", "sClassName", (Object) null);
            if (invokeFieldValue != null && (invokeFieldValue instanceof HashMap)) {
                ((HashMap) invokeFieldValue).put(cls, str);
            }
        } catch (Exception unused) {
        }
    }

    private String getConfigParam() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("__HtMl_Id__", (Object) "__uniapp_webview");
        jSONObject.put(RenderTypes.RENDER_TYPE_NATIVE, (Object) 1);
        jSONObject.put("debug", (Object) true);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(IApp.ConfigProperty.CONFIG_LANGUAGE, (Object) LanguageUtil.getDeviceDefLocalLanguage());
        jSONObject2.put("version", (Object) Build.VERSION.RELEASE);
        jSONObject2.put("name", (Object) TimeCalculator.PLATFORM_ANDROID);
        jSONObject2.put("vendor", (Object) "Google");
        jSONObject.put(WXConfig.os, (Object) jSONObject2);
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("CONNECTION_TYPE", (Object) 0);
        jSONObject.put("networkinfo", (Object) jSONObject3);
        return jSONObject.toJSONString();
    }

    public WXViewWrapper createWeexView(IWebview iWebview, ViewGroup viewGroup, org.json.JSONObject jSONObject, String str, int i) {
        WXViewWrapper makeWXViewWrapper = makeWXViewWrapper(iWebview, viewGroup, jSONObject, str, i);
        makeWXViewWrapper.loadTemplate(jSONObject);
        return makeWXViewWrapper;
    }

    private WXViewWrapper makeWXViewWrapper(IWebview iWebview, ViewGroup viewGroup, org.json.JSONObject jSONObject, String str, int i) {
        WXViewWrapper wXViewWrapper = new WXViewWrapper(iWebview, viewGroup, jSONObject, str, i, (!str.equals("__uniapp__service") || this.mPreUniAppid == null || WXSDKManager.getInstance().getSDKInstance(this.mPreInstanceId) == null) ? false : true);
        if (this.instanceHashMap.containsKey(str)) {
            ((WXViewWrapper) this.instanceHashMap.remove(str)).mWXSDKInstance.destroy();
        }
        this.instanceHashMap.put(str, wXViewWrapper);
        return wXViewWrapper;
    }

    public WXServiceWrapper createWeexService(IApp iApp, ViewGroup viewGroup, String str, org.json.JSONObject jSONObject) {
        WXServiceWrapper wXServiceWrapper = new WXServiceWrapper(iApp, viewGroup, str, jSONObject);
        this.serviceWrapperMapsCache.put(str, wXServiceWrapper);
        return wXServiceWrapper;
    }

    /* access modifiers changed from: package-private */
    public WXViewWrapper findWXViewWrapper(String str) {
        if (this.instanceHashMap.containsKey(str)) {
            return this.instanceHashMap.get(str);
        }
        return null;
    }

    public WXServiceWrapper findWXServiceWrapper(WXSDKInstance wXSDKInstance) {
        if (this.serviceWrapperMapsCache.size() <= 0) {
            return null;
        }
        for (String str : this.serviceWrapperMapsCache.keySet()) {
            WXServiceWrapper wXServiceWrapper = this.serviceWrapperMapsCache.get(str);
            if (wXServiceWrapper != null && wXServiceWrapper.mWXSDKInstance == wXSDKInstance) {
                return wXServiceWrapper;
            }
        }
        return null;
    }

    public void reloadWXServiceWrapper() {
        if (this.serviceWrapperMapsCache.size() > 0) {
            for (String str : this.serviceWrapperMapsCache.keySet()) {
                WXServiceWrapper wXServiceWrapper = this.serviceWrapperMapsCache.get(str);
                if (wXServiceWrapper != null) {
                    wXServiceWrapper.reload();
                }
            }
        }
    }

    public WXSDKInstance findWXSDKInstance(String str) {
        if (this.serviceWrapperMapsCache.containsKey(str)) {
            return this.serviceWrapperMapsCache.get(str).mWXSDKInstance;
        }
        if (this.instanceHashMap.containsKey(str)) {
            return this.instanceHashMap.get(str).mWXSDKInstance;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public WXBaseWrapper findWXBaseWrapper(String str) {
        if (this.serviceWrapperMapsCache.containsKey(str)) {
            return this.serviceWrapperMapsCache.get(str);
        }
        if (this.instanceHashMap.containsKey(str)) {
            return this.instanceHashMap.get(str);
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: io.dcloud.common.DHInterface.IWebview} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: io.dcloud.common.DHInterface.IWebview} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.dcloud.common.DHInterface.IWebview findWebview(io.dcloud.common.DHInterface.IWebview r6, io.dcloud.common.DHInterface.IApp r7, java.lang.String r8, java.lang.String r9) {
        /*
            r5 = this;
            io.dcloud.common.DHInterface.AbsMgr r0 = r5.featureMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            if (r6 == 0) goto L_0x000a
            goto L_0x000b
        L_0x000a:
            r6 = r7
        L_0x000b:
            r7 = 0
            r2[r7] = r6
            java.lang.String r6 = "ui"
            r3 = 1
            r2[r3] = r6
            java.lang.String r6 = "findWebview"
            r4 = 2
            r2[r4] = r6
            r6 = 3
            java.lang.String[] r4 = new java.lang.String[r4]
            r4[r7] = r8
            r4[r3] = r9
            r2[r6] = r4
            r6 = 10
            java.lang.Object r6 = r0.processEvent(r1, r6, r2)
            boolean r7 = r6 instanceof io.dcloud.common.DHInterface.IWebview
            if (r7 == 0) goto L_0x002e
            io.dcloud.common.DHInterface.IWebview r6 = (io.dcloud.common.DHInterface.IWebview) r6
            return r6
        L_0x002e:
            r6 = 0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.WeexInstanceMgr.findWebview(io.dcloud.common.DHInterface.IWebview, io.dcloud.common.DHInterface.IApp, java.lang.String, java.lang.String):io.dcloud.common.DHInterface.IWebview");
    }

    public Object doForFeature(IMgr.MgrType mgrType, int i, Object[] objArr) {
        AbsMgr absMgr = this.featureMgr;
        if (absMgr != null) {
            return absMgr.processEvent(mgrType, i, objArr);
        }
        return null;
    }

    public WXBaseWrapper findWXBaseWrapper(WXSDKInstance wXSDKInstance) {
        WXBaseWrapper wXBaseWrapper = null;
        for (String str : this.instanceHashMap.keySet()) {
            WXBaseWrapper wXBaseWrapper2 = this.instanceHashMap.get(str);
            if (wXBaseWrapper2 != null && wXBaseWrapper2.mWXSDKInstance == wXSDKInstance) {
                wXBaseWrapper = wXBaseWrapper2;
            }
        }
        return wXBaseWrapper;
    }

    public IWebview findWebview(WXSDKInstance wXSDKInstance) {
        for (String str : this.instanceHashMap.keySet()) {
            WXViewWrapper wXViewWrapper = this.instanceHashMap.get(str);
            if (wXViewWrapper != null && wXViewWrapper.mWXSDKInstance == wXSDKInstance) {
                return wXViewWrapper.mWebview;
            }
        }
        return null;
    }

    public IWebview findWebviewByInstanceId(String str) {
        for (String str2 : this.instanceHashMap.keySet()) {
            WXViewWrapper wXViewWrapper = this.instanceHashMap.get(str2);
            if (wXViewWrapper != null && wXViewWrapper.mWXSDKInstance.getInstanceId().equals(str)) {
                return wXViewWrapper.mWebview;
            }
        }
        return null;
    }

    public WXViewWrapper findPathByWrapper(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (String str2 : this.instanceHashMap.keySet()) {
            WXViewWrapper wXViewWrapper = this.instanceHashMap.get(str2);
            String initSrcPath = wXViewWrapper.initSrcPath(str);
            String srcPath = wXViewWrapper.getSrcPath();
            if (wXViewWrapper != null && !TextUtils.isEmpty(srcPath) && srcPath.equals(initSrcPath)) {
                return wXViewWrapper;
            }
        }
        return null;
    }

    public void removeWeexView(String str) {
        if (this.instanceHashMap.containsKey(str)) {
            ((WXViewWrapper) this.instanceHashMap.remove(str)).onDestroy();
        }
    }

    /* access modifiers changed from: package-private */
    public void onActivityResume() {
        forEach(new EachListener<WXBaseWrapper>() {
            public void onEach(WXBaseWrapper wXBaseWrapper) {
                wXBaseWrapper.onActivityResume();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onActivityPause() {
        forEach(new EachListener<WXBaseWrapper>() {
            public void onEach(WXBaseWrapper wXBaseWrapper) {
                wXBaseWrapper.onActivityPause();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onActivityResult(final int i, final int i2, final Intent intent) {
        forEach(new EachListener<WXBaseWrapper>() {
            public void onEach(WXBaseWrapper wXBaseWrapper) {
                wXBaseWrapper.onActivityResult(i, i2, intent);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onActivityDestroy() {
        onActivityDestroy(true);
    }

    /* access modifiers changed from: package-private */
    public void onActivityDestroy(boolean z) {
        this.mPreUniAppid = null;
        this.mPreInstanceId = null;
        if (this.instanceHashMap.size() > 0 || this.serviceWrapperMapsCache.size() > 0) {
            if (z) {
                getHandler().postDelayed(new Runnable() {
                    public void run() {
                        WeexInstanceMgr.this.wrapperDestroy();
                    }
                }, 200);
            } else {
                getHandler().post(new Runnable() {
                    public void run() {
                        WeexInstanceMgr.this.wrapperDestroy();
                    }
                });
            }
        }
        this.mPreUniMPCallBackMap.clear();
        this.mApplication = null;
    }

    /* access modifiers changed from: private */
    public void wrapperDestroy() {
        forEach(new EachListener<WXBaseWrapper>() {
            public void onEach(WXBaseWrapper wXBaseWrapper) {
                if (wXBaseWrapper != null) {
                    wXBaseWrapper.onDestroy();
                }
            }
        });
        this.instanceHashMap.clear();
        this.serviceWrapperMapsCache.clear();
        if (SDK.isUniMPSDK()) {
            reloadWeexEngine();
        }
    }

    private void reloadWeexEngine() {
        unRegisterUniappService();
        WXSDKEngine.reload();
    }

    private void forEach(EachListener eachListener) {
        try {
            Collection<WXViewWrapper> values = this.instanceHashMap.values();
            if (values != null) {
                for (WXViewWrapper next : values) {
                    if (next != null) {
                        eachListener.onEach(next);
                    }
                }
            }
            Collection<WXServiceWrapper> values2 = this.serviceWrapperMapsCache.values();
            if (values2 != null) {
                for (WXServiceWrapper next2 : values2) {
                    if (next2 != null) {
                        eachListener.onEach(next2);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e("forEach---" + e.getMessage());
        }
    }

    public void weexDebugReload() {
        LinkedHashMap<String, WXViewWrapper> linkedHashMap = this.instanceHashMap;
        if (linkedHashMap != null && linkedHashMap.size() > 0) {
            Iterator<String> it = this.instanceHashMap.keySet().iterator();
            if (it.hasNext()) {
                this.instanceHashMap.get(it.next()).mWebview.obtainFrameView().obtainWindowMgr().processEvent(IMgr.MgrType.AppMgr, 3, "snc:CID");
            }
        }
    }

    public void onRequestPermissionsResult(final int i, final String[] strArr, final int[] iArr) {
        forEach(new EachListener<WXBaseWrapper>() {
            public void onEach(WXBaseWrapper wXBaseWrapper) {
                if (wXBaseWrapper != null) {
                    wXBaseWrapper.onRequestPermissionsResult(i, strArr, iArr);
                }
            }
        });
    }

    public String getComplier() {
        return this.complier;
    }

    public String getControl() {
        return this.control;
    }

    public int getVueVersion() {
        return this.mVueVersion;
    }

    public boolean isWeexInitEnd() {
        return this.isWeexInitEnd;
    }

    public void setWeexInitEnd(boolean z) {
        this.isWeexInitEnd = z;
    }

    public void setJsFrameworkReady(boolean z) {
        this.isJsFrameworkReady = z;
    }

    public boolean isJsFrameworkReady() {
        return this.isJsFrameworkReady;
    }

    public void onJsFrameworkReady() {
        this.isJsFrameworkReady = true;
        if (this.mRestartReadyCall != null) {
            MessageHandler.sendMessage(new MessageHandler.IMessages() {
                public void execute(Object obj) {
                    WeexInstanceMgr.this.mRestartReadyCall.onCallBack(1, (Object) null);
                    ICallBack unused = WeexInstanceMgr.this.mRestartReadyCall = null;
                }
            }, (Object) null);
        }
        ArrayList<IWXStatisticsCallBack> arrayList = this.callBacks;
        if (arrayList != null) {
            Iterator<IWXStatisticsCallBack> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onJsFrameworkReady();
            }
        }
    }

    public void setWXStatisticsCallBack(IWXStatisticsCallBack iWXStatisticsCallBack) {
        if (!this.callBacks.contains(iWXStatisticsCallBack)) {
            this.callBacks.add(iWXStatisticsCallBack);
        }
    }

    public void unWXStatisticsCallBack(IWXStatisticsCallBack iWXStatisticsCallBack) {
        if (this.callBacks.contains(iWXStatisticsCallBack)) {
            this.callBacks.remove(iWXStatisticsCallBack);
        }
    }

    public boolean isUniServiceCreated(IApp iApp) {
        String obtainConfigProperty = iApp.obtainConfigProperty(AbsoluteConst.NVUE_LAUNCH_MODE);
        if (!TextUtils.isEmpty(obtainConfigProperty) && !obtainConfigProperty.equals("fast") && getControl().equals(AbsoluteConst.UNI_V3)) {
            return this.isUniServiceCreated;
        }
        if (!getControl().equals(Constants.CodeCache.SAVE_PATH)) {
            return true;
        }
        return this.isUniServiceCreated;
    }

    public String getPreInstanceId() {
        return this.mPreInstanceId;
    }

    public String getPreUniAppid() {
        return this.mPreUniAppid;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0010, code lost:
        if (android.text.TextUtils.isEmpty(r4) == false) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setUniServiceCreated(boolean r3, io.dcloud.common.DHInterface.IApp r4) {
        /*
            r2 = this;
            r2.isUniServiceCreated = r3
            java.lang.String r3 = "fast"
            if (r4 == 0) goto L_0x0013
            java.lang.String r0 = "nvueLaunchMode"
            java.lang.String r4 = r4.obtainConfigProperty(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 != 0) goto L_0x0013
            goto L_0x0014
        L_0x0013:
            r4 = r3
        L_0x0014:
            java.lang.String r0 = r2.getControl()
            java.lang.String r1 = "v8"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x0032
            boolean r3 = r4.equals(r3)
            if (r3 != 0) goto L_0x0062
            java.lang.String r3 = r2.getControl()
            java.lang.String r4 = "uni-v3"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L_0x0062
        L_0x0032:
            boolean r3 = r2.isUniServiceCreated
            if (r3 == 0) goto L_0x0062
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.weex.WXViewWrapper> r3 = r2.instanceHashMap
            java.util.Set r3 = r3.keySet()
            java.util.Iterator r3 = r3.iterator()
        L_0x0040:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0062
            java.lang.Object r4 = r3.next()
            java.lang.String r4 = (java.lang.String) r4
            java.util.LinkedHashMap<java.lang.String, io.dcloud.feature.weex.WXViewWrapper> r0 = r2.instanceHashMap
            java.lang.Object r4 = r0.get(r4)
            io.dcloud.feature.weex.WXViewWrapper r4 = (io.dcloud.feature.weex.WXViewWrapper) r4
            if (r4 == 0) goto L_0x0040
            boolean r0 = r4.isService
            if (r0 != 0) goto L_0x0040
            java.util.List r0 = r4.getWaitServiceRenderList()
            r4.runDelayedRenderCaches(r0)
            goto L_0x0040
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.WeexInstanceMgr.setUniServiceCreated(boolean, io.dcloud.common.DHInterface.IApp):void");
    }

    /* access modifiers changed from: private */
    public void preUniControlService(Application application, String str) {
        this.isUniServiceCreated = true;
        this.mPreUniAppid = str;
        UniSDKInstance uniSDKInstance = new UniSDKInstance(application);
        this.mPreInstanceId = uniSDKInstance.getInstanceId();
        String uniFileStr = getUniFileStr(application, str, "app-service.js");
        String uniFileStr2 = getUniFileStr(application, str, "app-config.js");
        HashMap hashMap = new HashMap();
        hashMap.put("plus_appid", str);
        hashMap.put("preload", true);
        hashMap.put("bundleUrl", "app-service.js");
        org.json.JSONObject jSONObject = new org.json.JSONObject();
        uniSDKInstance.render("__uniapp__service", (uniFileStr2 + uniFileStr + " plus.weexBridge.preloadReady('" + str + "');").replaceFirst(Pattern.quote("\"use weex:vue\""), Matcher.quoteReplacement("")), (Map<String, Object>) hashMap, jSONObject.toString(), WXRenderStrategy.APPEND_ASYNC);
        String str2 = this.TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("preUniControlService------");
        sb2.append(str);
        Logger.e(str2, sb2.toString());
    }

    private String getUniFileStr(Context context, String str, String str2) {
        try {
            return IOUtil.toString(getAppFileStream(context, str, str2));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ICallBack getPreUniMPCallBack(String str) {
        if (this.mPreUniMPCallBackMap.containsKey(str)) {
            return this.mPreUniMPCallBackMap.remove(str);
        }
        return null;
    }

    public void preUniMP(final Application application, final String str, ICallBack iCallBack) {
        if (TextUtils.isEmpty(this.mPreUniAppid) || iCallBack == null) {
            if (iCallBack != null) {
                this.mPreUniMPCallBackMap.put(str, iCallBack);
            }
            if (isWeexInitEnd()) {
                restartWeex(application, new ICallBack() {
                    public Object onCallBack(int i, Object obj) {
                        if (i != 1) {
                            return null;
                        }
                        WeexInstanceMgr.this.preUniControlService(application, str);
                        return null;
                    }
                }, str);
                return;
            }
            initWeexEnv(application);
            preUniControlService(application, str);
            return;
        }
        iCallBack.onCallBack(-101, "");
    }

    private Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        return this.mHandler;
    }

    private void clearHandler() {
        if (this.mHandler != null) {
            this.mHandler = null;
        }
    }

    public void initUniappPlugin(Application application) {
        UniMoudlesLoader.getInstance().onCreate(application);
    }

    public boolean isJSFKFileNotFound() {
        return this.isJSFKFileNotFound;
    }

    public void setJSFKFileNotFound(boolean z) {
        this.isJSFKFileNotFound = z;
    }

    public void loadDex(Application application) {
        if (BaseInfo.isBase(application)) {
            String path = application.getExternalFilesDir((String) null).getParentFile().getPath();
            ArrayList arrayList = new ArrayList();
            String str = path + File.separator + AbsoluteConst.XML_APPS + File.separator + BaseInfo.sDefaultBootApp + File.separator + CustomPath.CUSTOM_PATH_APP_WWW + File.separator + "uni_modules" + File.separator;
            List<String> listFilesWithSuffix = DHFile.listFilesWithSuffix(str, ".dex");
            List<String> listFilesWithSuffix2 = DHFile.listFilesWithSuffix(path + File.separator + AbsoluteConst.XML_APPS + File.separator + BaseInfo.sDefaultBootApp + File.separator + CustomPath.CUSTOM_PATH_APP_WWW + File.separator + "utssdk" + File.separator, ".dex");
            arrayList.addAll(listFilesWithSuffix);
            arrayList.addAll(listFilesWithSuffix2);
            if (arrayList.size() != 0) {
                PlatformUtil.invokeMethod("io.dcloud.debug.DexSwap", "loadDex", (Object) null, new Class[]{Context.class, List.class}, new Object[]{application, arrayList});
                PlatformUtil.invokeMethod("io.dcloud.debug.DexSwap", "loadPlugins", (Object) null, new Class[]{Context.class, String.class}, new Object[]{application, ""});
            }
        }
    }
}
