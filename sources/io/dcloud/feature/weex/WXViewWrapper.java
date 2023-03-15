package io.dcloud.feature.weex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.taobao.weex.IWXInstanceContainerOnSizeListener;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.R;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXParams;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import com.taobao.weex.utils.tools.LogDetail;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaUniWebView;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.webview.WebResUtil;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ErrorDialogUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.weex.WeexInstanceMgr;
import io.dcloud.weex.WXDotDataUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

class WXViewWrapper extends WXBaseWrapper implements IWXRenderListener, IEventCallback, WeexInstanceMgr.IWXStatisticsCallBack {
    static final int LOAD_JS = 1000;
    String TAG;
    private boolean hasScrollListener;
    private boolean isChlid;
    private boolean isCompilerWithUniapp;
    boolean isDelayRender;
    /* access modifiers changed from: private */
    public boolean isFrameShow;
    private boolean isPre;
    boolean isReady;
    boolean isService;
    JSONObject jsonObject;
    long lastTime;
    List<FireEvent> mFireCaches;
    int mFontSize;
    Handler mHandler;
    IWXInstanceContainerOnSizeListener mInstanceOnSizeListener;
    private float mLastScreenWidth;
    JSONObject mNvueCfgData;
    /* access modifiers changed from: private */
    public List<Message> mRenderCaches;
    LogDetail mServiceLogDetail;
    private String mUniPagePath;
    float mViewPort;
    View mWXSDKView;
    /* access modifiers changed from: private */
    public List<Message> mWaitServiceRenderList;
    private String readyJs;
    long time;

    public String getType() {
        return WXBasicComponentType.VIEW;
    }

    public void onRefreshSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
    }

    public List<Message> getWaitServiceRenderList() {
        return this.mWaitServiceRenderList;
    }

    public void onReady() {
        this.isReady = true;
        runFireCache();
        if (this.isService) {
            if (!SDK.isUniMPSDK()) {
                WeexInstanceMgr.self().doForFeature(IMgr.MgrType.FeatureMgr, 10, new Object[]{this.mWebview.obtainApp(), WeexInstanceMgr.self().getUniMPFeature(), "onUniMPInit", new Object[0]});
            }
            LogDetail logDetail = this.mServiceLogDetail;
            if (logDetail != null) {
                logDetail.taskEnd();
                WXDotDataUtil.setValue(this.mServiceLogDetail.info.taskName, Long.valueOf(this.mServiceLogDetail.time.execTime));
            }
        }
    }

    private synchronized void runFireCache() {
        if (!this.mFireCaches.isEmpty()) {
            for (int i = 0; i < this.mFireCaches.size(); i++) {
                FireEvent fireEvent = this.mFireCaches.get(i);
                fireGlobalEvent(fireEvent.key, fireEvent.params);
            }
            this.mFireCaches.clear();
        }
    }

    WXViewWrapper(IWebview iWebview, ViewGroup viewGroup, JSONObject jSONObject, String str, int i, boolean z) {
        super(viewGroup.getContext());
        this.TAG = "WXViewWrapper";
        this.lastTime = 0;
        this.isService = false;
        this.readyJs = ";var plusModule = weex.requireModule('plus'); plusModule.uniReady();";
        this.isReady = false;
        this.mFontSize = -1;
        this.mNvueCfgData = null;
        this.isDelayRender = false;
        this.mRenderCaches = new ArrayList();
        this.mWaitServiceRenderList = new ArrayList();
        this.isFrameShow = false;
        this.isChlid = false;
        this.isPre = false;
        this.isCompilerWithUniapp = true;
        this.mHandler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 1000) {
                    if (!WXViewWrapper.this.isService) {
                        WXViewWrapper.this.delayedRender(message.obj, (long) 10);
                    } else if (WXViewWrapper.this.mWebview != null) {
                        String obtainConfigProperty = WXViewWrapper.this.mWebview.obtainApp().obtainConfigProperty(AbsoluteConst.NVUE_LAUNCH_MODE);
                        if (TextUtils.isEmpty(obtainConfigProperty) || !obtainConfigProperty.equals("fast") || !WeexInstanceMgr.self().getControl().equals(AbsoluteConst.UNI_V3) || WXEnvironment.sRemoteDebugMode) {
                            WXViewWrapper.this.delayedRender(message.obj, 10);
                            return;
                        }
                        JSONObject obtainThridInfo = WXViewWrapper.this.mWebview.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.LaunchWebviewJsonData);
                        if (obtainThridInfo == null || !obtainThridInfo.has(AbsoluteConst.JSON_KEY_UNINVIEW)) {
                            WXViewWrapper.this.delayedRender(message.obj, 10);
                        } else {
                            WXViewWrapper.this.delayedRender(message.obj, 100);
                        }
                    }
                }
            }
        };
        this.mInstanceOnSizeListener = new IWXInstanceContainerOnSizeListener() {
            public void onSizeChanged(String str, float f, float f2, boolean z, boolean z2) {
                WXViewWrapper.this.updateDeviceDisplay(f, f2, z, z2);
            }
        };
        this.hasScrollListener = false;
        this.time = 0;
        this.mLastScreenWidth = 0.0f;
        this.lastTime = System.currentTimeMillis();
        String str2 = this.TAG;
        Logger.e(str2, "WXViewWrapper----------isPre=" + z + "  wxid=" + str);
        if (!WeexInstanceMgr.self().isJsFrameworkReady()) {
            WeexInstanceMgr.self().setWXStatisticsCallBack(this);
        }
        this.isCompilerWithUniapp = !WeexInstanceMgr.self().getComplier().equalsIgnoreCase("weex");
        this.mWebview = iWebview;
        this.isPre = z;
        this.mFireCaches = new ArrayList();
        if (this.mWebview instanceof AdaUniWebView) {
            this.isService = ((AdaUniWebView) this.mWebview).isUniService();
        }
        if (WeexInstanceMgr.self().isJSFKFileNotFound()) {
            boolean z2 = WeexInstanceMgr.self().getVueVersion() == 3;
            String string = iWebview.getContext().getString(R.string.dcloud_feature_weex_jsfk_not_found_tips);
            Object[] objArr = new Object[1];
            objArr[0] = z2 ? "VUE3" : "VUE2";
            ErrorDialogUtil.showErrorTipsAlert(iWebview.getActivity(), StringUtil.format(string, objArr), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Process.killProcess(Process.myPid());
                }
            });
            return;
        }
        viewGroup.addView(this, i, new ViewGroup.LayoutParams(-1, -1));
        ((AdaFrameView) this.mWebview.obtainFrameView()).addFrameViewListener(this);
        this.jsonObject = jSONObject;
        this.lastTime = System.currentTimeMillis();
        this.mWxId = str;
        this.mNvueCfgData = JSONUtil.createJSONObject(this.mWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.UNI_NVUE_DATA));
    }

    /* access modifiers changed from: private */
    public Map<String, Object> getInitOptions() {
        int indexOf;
        HashMap hashMap = new HashMap();
        if (this.mWebview != null) {
            hashMap.put("plus_appid", this.mWebview.obtainApp().obtainAppId());
        }
        if (this.mWebview != null) {
            hashMap.put("plus_web_id", this.mWebview.obtainFrameId());
        }
        String str = this.mSrcPath;
        if (!TextUtils.isEmpty(this.mPath) && str.indexOf(Operators.CONDITION_IF_STRING) == -1 && (indexOf = this.mPath.indexOf(Operators.CONDITION_IF_STRING)) > 0) {
            str = str + this.mPath.substring(indexOf);
        }
        hashMap.put("deviceLanguage", LanguageUtil.getDeviceDefLocalLanguage());
        hashMap.put("deviceCountry", LanguageUtil.getDeviceDefCountry());
        hashMap.put("isInternational", Boolean.valueOf(PdrUtil.checkIntl()));
        hashMap.put("bundleUrl", str);
        if (getWxId().equals("__uniapp__service")) {
            hashMap.put("plus_weex_id", "__uniapp__service");
        }
        return hashMap;
    }

    public String getSrcPath() {
        return this.mSrcPath;
    }

    public String getWxId() {
        return this.mWxId;
    }

    /* access modifiers changed from: private */
    public String getInitStringJsonData() {
        JSONObject jSONObject = new JSONObject();
        try {
            String originalUrl = this.mWebview.getOriginalUrl();
            if (originalUrl.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                originalUrl = originalUrl.substring(7);
            }
            jSONObject.put("Plus_InitURL", this.mWebview.obtainApp().convert2RelPath(originalUrl));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public String evalJs(String str, int i) {
        return this.mWXSDKInstance != null ? WXBridgeManager.getInstance().syncExecJsOnInstanceWithResult(this.mWXSDKInstance.getInstanceId(), str, i) : "";
    }

    /* access modifiers changed from: private */
    public void render(Object obj, Map<String, Object> map, String str) {
        if (this.mWXSDKInstance != null) {
            recoveryInstance();
        }
        if (this.mWebview != null && this.mWebview.obtainFrameView() != null) {
            if (this.isService && BaseInfo.SyncDebug) {
                if (this.mServiceLogDetail == null) {
                    LogDetail logDetail = new LogDetail();
                    this.mServiceLogDetail = logDetail;
                    logDetail.name("initV3Service");
                }
                this.mServiceLogDetail.taskStart();
            }
            this.isChlid = ((AdaFrameView) this.mWebview.obtainFrameView()).isChildOfFrameView;
            this.isReady = false;
            if (!this.isPre) {
                String str2 = (String) obj;
                if (!WXEnvironment.sRemoteDebugMode) {
                    str2 = str2.replaceFirst(Pattern.quote("\"use weex:vue\""), Matcher.quoteReplacement(""));
                }
                obj = str2 + this.readyJs;
            } else if (WeexInstanceMgr.self().getPreInstanceId() != null) {
                this.mWXSDKInstance = WXSDKManager.getInstance().getSDKInstance(WeexInstanceMgr.self().getPreInstanceId());
                if (this.mWXSDKInstance != null) {
                    this.mWXSDKInstance.init(this.mWebview.getContext());
                    onReady();
                    fireGlobalEvent("launchApp", new HashMap());
                }
            }
            if (!this.isPre) {
                if (this.mWebview.getActivity() instanceof IActivityHandler) {
                    this.mWXSDKInstance = new UniSDKInstance(this.mWebview.getContext(), ((IActivityHandler) this.mWebview.getActivity()).getOriginalContext());
                } else {
                    this.mWXSDKInstance = new UniSDKInstance(this.mWebview.getContext());
                }
            }
            if (this.mFontSize > 0) {
                this.mWXSDKInstance.setDefaultFontSize(this.mFontSize);
            }
            this.mWXSDKInstance.setPageKeepRawCssStyles();
            this.mWXSDKInstance.setImmersive(this.mWebview.obtainApp().obtainStatusBarMgr().isImmersive);
            this.mWXAnaly = new WXAnalyzerDelegate(this.mWebview.getContext());
            this.mWXSDKInstance.registerRenderListener(this);
            this.mWXSDKInstance.setBundleUrl(this.mSrcPath);
            this.mWXSDKInstance.setUniPagePath(this.isService ? "app-service.js" : this.mUniPagePath);
            this.mWXSDKInstance.setWXInstanceContainerOnSizeListener(this.mInstanceOnSizeListener);
            ((UniSDKInstance) this.mWXSDKInstance).setCompilerWithUniapp(this.isCompilerWithUniapp);
            int frameType = this.mWebview.obtainFrameView().getFrameType();
            if ((this.isFrameShow || frameType == 2 || frameType == 4) && this.mWXSDKInstance != null) {
                this.mWXSDKInstance.onShowAnimationEnd();
            }
            if (!this.isPre) {
                this.mWXSDKInstance.render(this.mWxId, String.valueOf(obj), map, str, WXRenderStrategy.APPEND_ASYNC);
            }
            if (!this.isService && (this.mWebview instanceof AdaWebview)) {
                ((AdaWebview) this.mWebview).dispatchWebviewStateEvent(0, this.mPath);
            }
            initViewPortWidth(false);
            initFlexDirection();
            initTitleNView();
            if (this.isService) {
                WeexInstanceMgr.self().setUniServiceCreated(true, this.mWebview.obtainApp());
            }
        }
    }

    private void initTitleNView() {
        AdaFrameView adaFrameView = (AdaFrameView) this.mWebview.obtainFrameView();
        JSONObject jSONObject = adaFrameView.obtainFrameOptions().titleNView;
        if (jSONObject != null && !adaFrameView.isChildOfFrameView && jSONObject.has("type") && "transparent".equals(jSONObject.optString("type"))) {
            this.mWXSDKInstance.addOnInstanceVisibleListener(new WXSDKInstance.OnInstanceVisibleListener() {
                public void onDisappear() {
                }

                public void onAppear() {
                    WXViewWrapper.this.addScrollListener(this);
                }
            });
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005e, code lost:
        if ("tab".equals(r4.obtainWebView().obtainFrameId()) != false) goto L_0x0062;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addScrollListener(com.taobao.weex.WXSDKInstance.OnInstanceVisibleListener r7) {
        /*
            r6 = this;
            com.taobao.weex.WXSDKInstance r0 = r6.mWXSDKInstance
            if (r0 == 0) goto L_0x00c2
            com.taobao.weex.WXSDKInstance r0 = r6.mWXSDKInstance
            android.view.View r0 = r0.getRootView()
            if (r0 != 0) goto L_0x000e
            goto L_0x00c2
        L_0x000e:
            boolean r0 = r6.hasScrollListener
            if (r0 == 0) goto L_0x0013
            return
        L_0x0013:
            r0 = 1
            r6.hasScrollListener = r0
            io.dcloud.common.DHInterface.IWebview r1 = r6.mWebview
            io.dcloud.common.DHInterface.IFrameView r1 = r1.obtainFrameView()
            io.dcloud.common.adapter.ui.AdaFrameView r1 = (io.dcloud.common.adapter.ui.AdaFrameView) r1
            io.dcloud.common.adapter.util.ViewOptions r2 = r1.obtainFrameOptions()
            io.dcloud.common.adapter.ui.AdaWebViewParent r3 = r1.obtainWebviewParent()
            io.dcloud.common.adapter.util.ViewOptions r3 = r3.obtainFrameOptions()
            org.json.JSONObject r2 = r2.titleNView
            if (r2 != 0) goto L_0x0034
            org.json.JSONObject r4 = r3.titleNView
            if (r4 == 0) goto L_0x0034
            org.json.JSONObject r2 = r3.titleNView
        L_0x0034:
            if (r2 == 0) goto L_0x00c2
            java.lang.String r3 = "type"
            boolean r4 = r2.has(r3)
            if (r4 != 0) goto L_0x0040
            goto L_0x00c2
        L_0x0040:
            io.dcloud.common.adapter.ui.AdaContainerFrameItem r4 = r1.getParentFrameItem()
            boolean r5 = r4 instanceof io.dcloud.common.adapter.ui.AdaFrameView
            if (r5 == 0) goto L_0x0061
            io.dcloud.common.adapter.ui.AdaFrameView r4 = (io.dcloud.common.adapter.ui.AdaFrameView) r4
            io.dcloud.common.DHInterface.IWebview r5 = r4.obtainWebView()
            if (r5 == 0) goto L_0x0061
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()
            java.lang.String r4 = r4.obtainFrameId()
            java.lang.String r5 = "tab"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x0061
            goto L_0x0062
        L_0x0061:
            r0 = 0
        L_0x0062:
            if (r2 == 0) goto L_0x00c2
            boolean r4 = r1.isChildOfFrameView
            if (r4 == 0) goto L_0x006a
            if (r0 == 0) goto L_0x00c2
        L_0x006a:
            boolean r0 = r2.has(r3)
            if (r0 == 0) goto L_0x00c2
            java.lang.String r0 = r2.optString(r3)
            java.lang.String r3 = "transparent"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x00c2
            com.taobao.weex.WXSDKInstance r0 = r6.mWXSDKInstance
            if (r0 != 0) goto L_0x0081
            return
        L_0x0081:
            com.taobao.weex.WXSDKInstance r0 = r6.mWXSDKInstance
            android.view.View r0 = r0.getRootView()
            if (r0 == 0) goto L_0x0090
            if (r7 == 0) goto L_0x0090
            com.taobao.weex.WXSDKInstance r3 = r6.mWXSDKInstance
            r3.removeOnInstanceVisibleListener(r7)
        L_0x0090:
            boolean r7 = r0 instanceof io.dcloud.feature.weex_scroller.view.DCWXScrollView
            if (r7 == 0) goto L_0x009f
            io.dcloud.feature.weex_scroller.view.DCWXScrollView r0 = (io.dcloud.feature.weex_scroller.view.DCWXScrollView) r0
            io.dcloud.feature.weex.WXViewWrapper$5 r7 = new io.dcloud.feature.weex.WXViewWrapper$5
            r7.<init>(r1, r2)
            r0.addScrollViewListener(r7)
            goto L_0x00c2
        L_0x009f:
            boolean r7 = r0 instanceof com.taobao.weex.ui.view.WXScrollView
            if (r7 == 0) goto L_0x00ae
            com.taobao.weex.ui.view.WXScrollView r0 = (com.taobao.weex.ui.view.WXScrollView) r0
            io.dcloud.feature.weex.WXViewWrapper$6 r7 = new io.dcloud.feature.weex.WXViewWrapper$6
            r7.<init>(r1, r2)
            r0.addScrollViewListener(r7)
            goto L_0x00c2
        L_0x00ae:
            boolean r7 = r0 instanceof com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView
            if (r7 == 0) goto L_0x00c2
            com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView r0 = (com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView) r0
            android.view.View r7 = r0.getInnerView()
            com.taobao.weex.ui.view.listview.WXRecyclerView r7 = (com.taobao.weex.ui.view.listview.WXRecyclerView) r7
            io.dcloud.feature.weex.WXViewWrapper$7 r0 = new io.dcloud.feature.weex.WXViewWrapper$7
            r0.<init>(r1, r2)
            r7.addOnScrollListener(r0)
        L_0x00c2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.WXViewWrapper.addScrollListener(com.taobao.weex.WXSDKInstance$OnInstanceVisibleListener):void");
    }

    /* access modifiers changed from: protected */
    public void onRefresh() {
        if (this.mWebview != null) {
            ((AdaFrameView) this.mWebview.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENTS_PULL_DOWN_EVENT, 3);
            ((AdaFrameView) this.mWebview.obtainFrameView()).dispatchFrameViewEvents(AbsoluteConst.EVENTS_PULL_TO_REFRESH, 3);
        }
    }

    public void loadTemplate(JSONObject jSONObject) {
        JSONObject optJSONObject;
        try {
            this.jsonObject = jSONObject;
            this.mPath = jSONObject.optString("js");
            JSONObject jSONObject2 = this.jsonObject;
            if (!(jSONObject2 == null || !jSONObject2.has("data") || (optJSONObject = this.jsonObject.optJSONObject("data")) == null)) {
                this.mFontSize = optJSONObject.optInt("defaultFontSize");
                if (optJSONObject.has("delayRender")) {
                    this.isDelayRender = optJSONObject.optBoolean("delayRender", this.isDelayRender);
                }
                if (optJSONObject.has(AbsoluteConst.XML_PATH)) {
                    this.mUniPagePath = optJSONObject.optString(AbsoluteConst.XML_PATH, "") + ".nvue";
                }
            }
            if (!PdrUtil.isNetPath(this.mPath)) {
                this.mSrcPath = initSrcPath(this.mPath);
                if (this.isPre) {
                    render((Object) null, getInitOptions(), getInitStringJsonData());
                } else {
                    ThreadPool.self().addThreadTask(new Runnable() {
                        public void run() {
                            InputStream encryptionInputStream;
                            if (WXViewWrapper.this.mWebview != null && (encryptionInputStream = WebResUtil.getEncryptionInputStream(WXViewWrapper.this.mSrcPath, WXViewWrapper.this.mWebview.obtainApp())) != null) {
                                try {
                                    String str = new String(IOUtil.toString(encryptionInputStream));
                                    if (WXViewWrapper.this.isService) {
                                        str = WXViewWrapper.this.getAllUniService(str);
                                    }
                                    Message message = new Message();
                                    message.obj = str;
                                    message.what = 1000;
                                    if (WXViewWrapper.this.isService || WeexInstanceMgr.self().isUniServiceCreated(WXViewWrapper.this.mWebview.obtainApp())) {
                                        if (!WeexInstanceMgr.self().isJsFrameworkReady()) {
                                            if (!WXViewWrapper.this.mRenderCaches.contains(message)) {
                                                WXViewWrapper.this.mRenderCaches.add(message);
                                            }
                                        } else if (!WXViewWrapper.this.isDelayRender || WXViewWrapper.this.isService || WXViewWrapper.this.isFrameShow) {
                                            WXViewWrapper.this.mHandler.sendMessage(message);
                                        } else if (!WXViewWrapper.this.mRenderCaches.contains(message)) {
                                            WXViewWrapper.this.mRenderCaches.add(message);
                                        }
                                        IOUtil.close(encryptionInputStream);
                                    }
                                    if (!WXViewWrapper.this.mWaitServiceRenderList.contains(message)) {
                                        WXViewWrapper.this.mWaitServiceRenderList.add(message);
                                    }
                                    IOUtil.close(encryptionInputStream);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void titleNViewRefresh() {
        addScrollListener((WXSDKInstance.OnInstanceVisibleListener) null);
    }

    /* access modifiers changed from: private */
    public String getAllUniService(String str) {
        if (this.mWebview == null || this.mWebview.obtainApp() == null) {
            return "";
        }
        IApp obtainApp = this.mWebview.obtainApp();
        String initSrcPath = initSrcPath("_www/app-config.js");
        String initSrcPath2 = initSrcPath("_www/app-confusion.js");
        InputStream encryptionInputStream = WebResUtil.getEncryptionInputStream(initSrcPath, obtainApp);
        InputStream encryptionInputStream2 = WebResUtil.getEncryptionInputStream(initSrcPath2, obtainApp);
        try {
            String iOUtil = IOUtil.toString(encryptionInputStream);
            String iOUtil2 = IOUtil.toString(encryptionInputStream2);
            return iOUtil + iOUtil2 + str;
        } catch (IOException e) {
            e.printStackTrace();
            return str;
        }
    }

    /* access modifiers changed from: private */
    public void delayedRender(final Object obj, long j) {
        postDelayed(new Runnable() {
            public void run() {
                if (WXViewWrapper.this.mWebview != null) {
                    WXViewWrapper wXViewWrapper = WXViewWrapper.this;
                    wXViewWrapper.render(obj, wXViewWrapper.getInitOptions(), WXViewWrapper.this.getInitStringJsonData());
                }
            }
        }, j);
    }

    public String initSrcPath(String str) {
        if (this.mWebview == null) {
            return str;
        }
        int indexOf = str.indexOf(Operators.CONDITION_IF_STRING);
        String substring = indexOf > 1 ? str.substring(0, indexOf) : str;
        if (substring.startsWith("/")) {
            substring = substring.substring(1);
        }
        String[] split = str.split("\\.");
        if (split.length == 1) {
            substring = substring + ".js";
        } else if (split.length == 2) {
            substring = split[0] + ".js";
        }
        File file = new File(substring);
        if (file.exists()) {
            return Uri.fromFile(file).toString();
        }
        byte obtainRunningAppMode = this.mWebview.obtainApp().obtainRunningAppMode();
        String str2 = null;
        if (!(this.mWebview instanceof AdaUniWebView)) {
            str2 = this.mWebview.obtainFullUrl();
        }
        if (substring.startsWith("/storage") || obtainRunningAppMode != 1) {
            return this.mWebview.obtainApp().convert2WebviewFullPath(str2, substring);
        }
        String convert2AbsFullPath = this.mWebview.obtainApp().convert2AbsFullPath(str2, substring);
        if (convert2AbsFullPath.startsWith("/")) {
            return convert2AbsFullPath.substring(1, convert2AbsFullPath.length());
        }
        return convert2AbsFullPath;
    }

    public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
        if (this.mWXAnaly != null) {
            this.mWXAnaly.onWeexViewCreated(wXSDKInstance, view);
        }
        this.mWXSDKView = view;
        addView(this.mWXSDKView, new LinearLayout.LayoutParams(-1, -1));
        this.mWXSDKView.layout(0, 0, getWidth(), getHeight());
        if (isFocusableInTouchMode()) {
            this.mWXSDKView.setFocusable(true);
            this.mWXSDKView.setFocusableInTouchMode(true);
        }
        addScrollListener((WXSDKInstance.OnInstanceVisibleListener) null);
    }

    public void setFocusable(int i) {
        super.setFocusable(i);
        View view = this.mWXSDKView;
        if (view != null) {
            view.setFocusable(i);
        }
    }

    public void setFocusableInTouchMode(boolean z) {
        super.setFocusableInTouchMode(z);
        View view = this.mWXSDKView;
        if (view != null) {
            view.setFocusableInTouchMode(z);
        }
    }

    public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        if (this.mWXAnaly != null) {
            this.mWXAnaly.onWeexRenderSuccess(wXSDKInstance);
        }
        if (!this.isService && this.mWebview != null) {
            if (this.mWebview instanceof AdaWebview) {
                ((AdaWebview) this.mWebview).dispatchWebviewStateEvent(1, this.mPath);
            }
            String obtainConfigProperty = this.mWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_AUTOCLOSE);
            if (PdrUtil.isEmpty(obtainConfigProperty)) {
                obtainConfigProperty = AbsoluteConst.TRUE;
            }
            if (Boolean.parseBoolean(obtainConfigProperty)) {
                this.mWebview.obtainFrameView().obtainWindowMgr().processEvent(IMgr.MgrType.WindowMgr, 11, this.mWebview.obtainFrameView());
            }
        }
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        String str3 = this.TAG;
        Logger.e(str3, "onException--errCode=" + str + "    msg=" + str2);
        if (this.mWXAnaly != null) {
            this.mWXAnaly.onException(wXSDKInstance, str, str2);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        WeexInstanceMgr.self().unWXStatisticsCallBack(this);
        this.mWebview = null;
    }

    public void reload() {
        if (this.time == 0 || System.currentTimeMillis() - this.time >= 600) {
            this.time = System.currentTimeMillis();
            recoveryInstance();
            this.isFrameShow = true;
            if (!TextUtils.isEmpty(this.mPath)) {
                loadTemplate(this.jsonObject);
            }
        }
    }

    public Object onCallBack(String str, Object obj) {
        if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE) && (obj instanceof IWebview)) {
            ((AdaFrameView) ((IWebview) obj).obtainFrameView()).removeFrameViewListener(this);
            WeexInstanceMgr.self().removeWeexView(this.mWxId);
            onDestroy();
            return null;
        } else if ((!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_SHOW_ANIMATION_END) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CHILD_INITIALIZE_SHOW)) || this.isFrameShow) {
            return null;
        } else {
            this.isFrameShow = true;
            if (this.mWXSDKInstance != null) {
                this.mWXSDKInstance.onShowAnimationEnd();
            }
            if (!this.isDelayRender) {
                return null;
            }
            runDelayedRenderCaches(this.mRenderCaches);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        String str;
        int i;
        super.onConfigurationChanged(configuration);
        if (this.mWXSDKInstance != null && this.mWebview.getActivity() != null) {
            int rotation = this.mWebview.getActivity().getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == 1) {
                str = "landscape";
                i = 90;
            } else if (rotation == 2) {
                str = "portraitReverse";
                i = 180;
            } else if (rotation != 3) {
                str = "portrait";
                i = 0;
            } else {
                str = "landscapeReverse";
                i = -90;
            }
            WXUtils.getCache().evictAll();
            HashMap hashMap = new HashMap();
            hashMap.put("value", str);
            hashMap.put("orientation", Integer.valueOf(i));
            this.mWXSDKInstance.fireGlobalEventCallback("orientationchange", hashMap);
            if (this.isCompilerWithUniapp) {
                this.mWXSDKInstance.setInstanceViewPortWidth(((float) this.mWebview.obtainApp().getInt(0)) / this.mWebview.getScale(), true);
            }
        }
    }

    private void updateInitDeviceParams(Context context) {
        WXParams initParams = WXBridgeManager.getInstance().getInitParams();
        if (initParams != null && !TextUtils.equals(initParams.getDeviceWidth(), String.valueOf(WXViewUtils.getScreenWidth(context)))) {
            initParams.setDeviceWidth(String.valueOf(WXViewUtils.getScreenWidth(context)));
            initParams.setDeviceHeight(String.valueOf(WXViewUtils.getScreenHeight(context)));
            float f = WXEnvironment.sApplication.getResources().getDisplayMetrics().density;
            WXEnvironment.addCustomOptions("scale", Float.toString(f));
            String str = null;
            if (WXViewUtils.getStatusBarHeight(context) > 0) {
                str = String.valueOf(WXViewUtils.getStatusBarHeight(context));
            }
            WXBridgeManager.getInstance().updateInitDeviceParams(initParams.getDeviceWidth(), initParams.getDeviceHeight(), Float.toString(f), str);
            WXBridgeManager.getInstance().setDeviceDisplay(this.mWXSDKInstance.getInstanceId(), (float) WXViewUtils.getScreenWidth(context), (float) WXViewUtils.getScreenHeight(context), WXViewUtils.getScreenDensity(context));
        }
    }

    public synchronized boolean fireGlobalEvent(String str, Map<String, Object> map) {
        if (this.isReady) {
            return super.fireGlobalEvent(str, map);
        }
        this.mFireCaches.add(new FireEvent(str, map));
        return true;
    }

    public void onJsFrameworkReady() {
        runDelayedRenderCaches(this.mRenderCaches);
        if (this.mWXSDKInstance != null) {
            initViewPortWidth(true);
            initFlexDirection();
        }
    }

    public void runDelayedRenderCaches(List<Message> list) {
        if (list.size() > 0) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mHandler.sendMessage(list.get(size));
            }
            list.clear();
        }
    }

    private void initViewPortWidth(boolean z) {
        if (this.isCompilerWithUniapp && this.mWXSDKInstance != null) {
            int i = this.mWebview.obtainApp().getInt(0);
            if (z) {
                i = WXViewUtils.getScreenWidth(getContext());
            }
            this.mViewPort = ((float) i) / this.mWebview.getScale();
            this.mWXSDKInstance.setInstanceViewPortWidth(this.mViewPort, z);
        }
    }

    private void initFlexDirection() {
        JSONObject jSONObject = this.mNvueCfgData;
        if (jSONObject != null && jSONObject.has(AbsoluteConst.UNI_NVUE_FLEX_DIRECTION)) {
            WXBridgeManager.getInstance().setFlexDirectionDef(this.mNvueCfgData.optString(AbsoluteConst.UNI_NVUE_FLEX_DIRECTION));
        }
    }

    class FireEvent {
        String key;
        Map<String, Object> params;

        public FireEvent(String str, Map<String, Object> map) {
            this.key = str;
            this.params = map;
        }
    }

    public void recoveryInstance() {
        View currentFocus;
        if (this.mWXSDKView != null) {
            if (!this.isChlid && (currentFocus = this.mWebview.getActivity().getCurrentFocus()) != null) {
                DeviceInfo.hideIME(currentFocus);
            }
            this.mWXSDKView.clearFocus();
            clearFocus();
            removeView(this.mWXSDKView);
            this.mWXSDKView = null;
            this.isPre = false;
        }
        super.recoveryInstance();
        this.isFrameShow = false;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mWebview != null && (this.mWebview instanceof AdaUniWebView)) {
            try {
                ((AdaUniWebView) this.mWebview).updateScreenAndDisplay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateDeviceDisplay(float f, float f2, boolean z, boolean z2) {
        if (this.mWXSDKInstance != null && this.mWebview != null && this.mWebview.getActivity() != null) {
            int i = this.mWebview.obtainApp().getInt(2);
            int i2 = this.mWebview.obtainApp().getInt(0);
            int i3 = this.mWebview.obtainApp().getInt(1);
            if (this.mWXSDKInstance.isOnSizeChangedRender() && this.mWXSDKInstance.isOnSizeChangedRender()) {
                HashMap hashMap = new HashMap();
                float scale = this.mWebview.getScale();
                hashMap.put("resolutionHeight", Integer.valueOf((int) (((float) i) / scale)));
                float f3 = (float) i2;
                int i4 = (int) (f3 / scale);
                hashMap.put("resolutionWidth", Integer.valueOf(i4));
                hashMap.put("dpiX", Float.valueOf(DeviceInfo.dpiX));
                hashMap.put("dpiY", Float.valueOf(DeviceInfo.dpiY));
                HashMap hashMap2 = new HashMap();
                hashMap2.put("resolutionHeight", Integer.valueOf((int) (((float) i3) / scale)));
                hashMap2.put("resolutionWidth", Integer.valueOf(i4));
                StringBuilder sb = new StringBuilder();
                for (String str : hashMap.keySet()) {
                    sb.append("plus.screen.");
                    sb.append(str);
                    sb.append("=");
                    sb.append(hashMap.get(str));
                    sb.append(";");
                }
                for (String str2 : hashMap2.keySet()) {
                    sb.append("plus.display.");
                    sb.append(str2);
                    sb.append("=");
                    sb.append(hashMap2.get(str2));
                    sb.append(";");
                }
                this.mWebview.evalJS(sb.toString());
                if (this.isCompilerWithUniapp) {
                    this.mWXSDKInstance.setInstanceViewPortWidth(f3 / this.mWebview.getScale(), true);
                }
                if (!this.isService) {
                    this.mWXSDKInstance.setDeviceDisplayOfPage(i2, i3);
                    this.mWXSDKInstance.reloadPageLayout();
                    WXBridgeManager.getInstance().setDefaultRootSize(this.mWXSDKInstance.getInstanceId(), f, f2, z, z2);
                }
            }
        }
    }
}
