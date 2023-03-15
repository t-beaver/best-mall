package com.taobao.weex.performance;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class WXInstanceApm {
    public static final String KEY_PAGE_ANIM_BACK_NUM = "wxAnimationInBackCount";
    public static final String KEY_PAGE_PROPERTIES_BIZ_ID = "wxBizID";
    public static final String KEY_PAGE_PROPERTIES_BUBDLE_URL = "wxBundleUrl";
    public static final String KEY_PAGE_PROPERTIES_BUNDLE_TYPE = "wxBundleType";
    public static final String KEY_PAGE_PROPERTIES_CACHE_INFO = "wxZCacheInfo";
    public static final String KEY_PAGE_PROPERTIES_CACHE_TYPE = "wxCacheType";
    public static final String KEY_PAGE_PROPERTIES_CONTAINER_NAME = "wxContainerName";
    public static final String KEY_PAGE_PROPERTIES_INSTANCE_TYPE = "wxInstanceType";
    public static final String KEY_PAGE_PROPERTIES_JSLIB_VERSION = "wxJSLibVersion";
    public static final String KEY_PAGE_PROPERTIES_JS_FM_INI = "wxJsFrameworkInit";
    public static final String KEY_PAGE_PROPERTIES_PARENT_PAGE = "wxParentPage";
    public static final String KEY_PAGE_PROPERTIES_RENDER_TYPE = "wxRenderType";
    public static final String KEY_PAGE_PROPERTIES_REQUEST_TYPE = "wxRequestType";
    public static final String KEY_PAGE_PROPERTIES_UIKIT_TYPE = "wxUIKitType";
    public static final String KEY_PAGE_PROPERTIES_WEEX_VERSION = "wxSDKVersion";
    public static final String KEY_PAGE_STAGES_CONTAINER_READY = "wxContainerReady";
    public static final String KEY_PAGE_STAGES_CREATE_FINISH = "wxJSBundleCreateFinish";
    public static final String KEY_PAGE_STAGES_CUSTOM_PREPROCESS_END = "wxCustomPreprocessEnd";
    public static final String KEY_PAGE_STAGES_CUSTOM_PREPROCESS_START = "wxCustomPreprocessStart";
    public static final String KEY_PAGE_STAGES_DESTROY = "wxDestroy";
    public static final String KEY_PAGE_STAGES_DOWN_BUNDLE_END = "wxEndDownLoadBundle";
    public static final String KEY_PAGE_STAGES_DOWN_BUNDLE_START = "wxStartDownLoadBundle";
    public static final String KEY_PAGE_STAGES_END_EXCUTE_BUNDLE = "wxEndExecuteBundle";
    public static final String KEY_PAGE_STAGES_FIRST_INTERACTION_VIEW = "wxFirstInteractionView";
    public static final String KEY_PAGE_STAGES_FSRENDER = "wxFsRender";
    public static final String KEY_PAGE_STAGES_INTERACTION = "wxInteraction";
    public static final String KEY_PAGE_STAGES_LOAD_BUNDLE_END = "wxEndLoadBundle";
    public static final String KEY_PAGE_STAGES_LOAD_BUNDLE_START = "wxStartLoadBundle";
    public static final String KEY_PAGE_STAGES_NEW_FSRENDER = "wxNewFsRender";
    public static final String KEY_PAGE_STAGES_RENDER_ORGIGIN = "wxRenderTimeOrigin";
    public static final String KEY_PAGE_STATS_ACTUAL_DOWNLOAD_TIME = "wxActualNetworkTime";
    public static final String KEY_PAGE_STATS_BODY_RATIO = "wxBodyRatio";
    public static final String KEY_PAGE_STATS_BUNDLE_SIZE = "wxBundleSize";
    public static final String KEY_PAGE_STATS_CELL_DATA_UN_RECYCLE_NUM = "wxCellDataUnRecycleCount";
    public static final String KEY_PAGE_STATS_CELL_EXCEED_NUM = "wxCellExceedNum";
    public static final String KEY_PAGE_STATS_CELL_UN_RE_USE_NUM = "wxCellUnReUseCount";
    public static final String KEY_PAGE_STATS_COMPONENT_CREATE_COST = "wxComponentCost";
    public static final String KEY_PAGE_STATS_EMBED_COUNT = "wxEmbedCount";
    public static final String KEY_PAGE_STATS_EXECUTE_JS_CALLBACK_COST = "wxExecJsCallBack";
    public static final String KEY_PAGE_STATS_FS_CALL_EVENT_NUM = "wxFSCallEventTotalNum";
    public static final String KEY_PAGE_STATS_FS_CALL_JS_NUM = "wxFSCallJsTotalNum";
    public static final String KEY_PAGE_STATS_FS_CALL_JS_TIME = "wxFSCallJsTotalTime";
    public static final String KEY_PAGE_STATS_FS_CALL_NATIVE_NUM = "wxFSCallNativeTotalNum";
    public static final String KEY_PAGE_STATS_FS_CALL_NATIVE_TIME = "wxFSCallNativeTotalTime";
    public static final String KEY_PAGE_STATS_FS_REQUEST_NUM = "wxFSRequestNum";
    public static final String KEY_PAGE_STATS_FS_TIMER_NUM = "wxFSTimerCount";
    public static final String KEY_PAGE_STATS_IMG_LOAD_FAIL_NUM = "wxImgLoadFailCount";
    public static final String KEY_PAGE_STATS_IMG_LOAD_NUM = "wxImgLoadCount";
    public static final String KEY_PAGE_STATS_IMG_LOAD_SUCCESS_NUM = "wxImgLoadSuccessCount";
    public static final String KEY_PAGE_STATS_IMG_UN_RECYCLE_NUM = "wxImgUnRecycleCount";
    public static final String KEY_PAGE_STATS_I_ALL_VIEW_COUNT = "wxInteractionAllViewCount";
    public static final String KEY_PAGE_STATS_I_COMPONENT_CREATE_COUNT = "wxInteractionComponentCreateCount";
    public static final String KEY_PAGE_STATS_I_SCREEN_VIEW_COUNT = "wxInteractionScreenViewCount";
    public static final String KEY_PAGE_STATS_JSLIB_INIT_TIME = "wxJSLibInitTime";
    public static final String KEY_PAGE_STATS_LARGE_IMG_COUNT = "wxLargeImgMaxCount";
    public static final String KEY_PAGE_STATS_LAYOUT_TIME = "wxLayoutTime";
    public static final String KEY_PAGE_STATS_MAX_COMPONENT_NUM = "wxMaxComponentCount";
    public static final String KEY_PAGE_STATS_MAX_DEEP_DOM = "wxMaxDeepVDomLayer";
    public static final String KEY_PAGE_STATS_MAX_DEEP_VIEW = "wxMaxDeepViewLayer";
    public static final String KEY_PAGE_STATS_NET_FAIL_NUM = "wxNetworkRequestFailCount";
    public static final String KEY_PAGE_STATS_NET_NUM = "wxNetworkRequestCount";
    public static final String KEY_PAGE_STATS_NET_SUCCESS_NUM = "wxNetworkRequestSuccessCount";
    public static final String KEY_PAGE_STATS_SCROLLER_NUM = "wxScrollerCount";
    public static final String KEY_PAGE_STATS_VIEW_CREATE_COST = "wxViewCost";
    public static final String KEY_PAGE_STATS_WRONG_IMG_SIZE_COUNT = "wxWrongImgSizeCount";
    public static final String KEY_PAGE_TIMER_BACK_NUM = "wxTimerInBackCount";
    public static final String KEY_PROPERTIES_ERROR_CODE = "wxErrorCode";
    public static final String VALUE_BUNDLE_LOAD_LENGTH = "wxLoadedLength";
    public static final String VALUE_ERROR_CODE_DEFAULT = "0";
    public static final String WEEX_PAGE_TOPIC = "weex_page";
    private IWXApmMonitorAdapter apmInstance;
    public long componentCreateTime;
    private Runnable delayCollectDataTask = new Runnable() {
        public void run() {
            WXInstanceApm.this.recordPerformanceDetailData();
        }
    };
    public Set<String> exceptionRecord = new CopyOnWriteArraySet();
    public final Map<String, Object> extInfo;
    public boolean forceStopRecordInteraction = false;
    public boolean hasAddView;
    private boolean hasRecordFistInteractionView = false;
    public boolean hasReportLayerOverDraw = false;
    private boolean hasSendInteractionToJS = false;
    public Rect instanceRect;
    private long interactionComponentCreateTime;
    private long interactionJsCallBackTime;
    private double interactionLayoutTime;
    private long interactionViewCreateTime;
    private boolean isFSEnd;
    public volatile boolean isReady = true;
    private Runnable jsPerformanceCallBack = new Runnable() {
        public void run() {
            WXInstanceApm.this.sendPerformanceToJS();
        }
    };
    private boolean mEnd = false;
    private boolean mHasInit = false;
    private boolean mHasRecordDetailData = false;
    private String mInstanceId;
    private Map<String, Object> mPropertiesMap;
    private Handler mUIHandler;
    private long preUpdateTime = 0;
    private Map<String, Double> recordStatsMap;
    public String reportPageName;
    public final Map<String, Long> stageMap;
    public long viewCreateTime;
    private long wxExecJsCallBackTime;

    public WXInstanceApm(String str) {
        this.mInstanceId = str;
        this.extInfo = new ConcurrentHashMap();
        this.stageMap = new ConcurrentHashMap();
        this.mUIHandler = new Handler(Looper.getMainLooper());
        this.recordStatsMap = new ConcurrentHashMap();
        this.mPropertiesMap = new ConcurrentHashMap();
        IApmGenerator apmGenerater = WXSDKManager.getInstance().getApmGenerater();
        if (apmGenerater != null) {
            this.apmInstance = apmGenerater.generateApmInstance(WEEX_PAGE_TOPIC);
        }
    }

    public void onInstanceReady(boolean z) {
        this.isReady = true;
        if (z) {
            onStage(KEY_PAGE_STAGES_DOWN_BUNDLE_START);
        }
        doInit();
        for (Map.Entry next : this.stageMap.entrySet()) {
            sendStageInfo((String) next.getKey(), ((Long) next.getValue()).longValue());
        }
        for (Map.Entry next2 : this.recordStatsMap.entrySet()) {
            sendStats((String) next2.getKey(), ((Double) next2.getValue()).doubleValue());
        }
        for (Map.Entry next3 : this.mPropertiesMap.entrySet()) {
            sendProperty((String) next3.getKey(), next3.getValue());
        }
    }

    public void onEvent(String str, Object obj) {
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            iWXApmMonitorAdapter.onEvent(str, obj);
        }
    }

    public void onStage(String str) {
        onStageWithTime(str, WXUtils.getFixUnixTime());
    }

    public void onStageWithTime(String str, long j) {
        if (!this.mEnd && str != null) {
            this.stageMap.put(str, Long.valueOf(j));
            if (this.isReady) {
                sendStageInfo(str, j);
            }
        }
    }

    private void sendStageInfo(String str, long j) {
        if (WXAnalyzerDataTransfer.isOpenPerformance) {
            WXAnalyzerDataTransfer.transferPerformance(this.mInstanceId, "stage", str, Long.valueOf(j));
        }
        if (KEY_PAGE_STAGES_RENDER_ORGIGIN.equalsIgnoreCase(str)) {
            this.mUIHandler.postDelayed(this.jsPerformanceCallBack, 8000);
        }
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            iWXApmMonitorAdapter.onStage(str, j);
        }
    }

    public void addProperty(String str, Object obj) {
        if (!this.mEnd && str != null && obj != null) {
            this.mPropertiesMap.put(str, obj);
            if (this.isReady) {
                sendProperty(str, obj);
            }
        }
    }

    private void sendProperty(String str, Object obj) {
        if (WXAnalyzerDataTransfer.isOpenPerformance) {
            WXAnalyzerDataTransfer.transferPerformance(this.mInstanceId, "properties", str, obj);
        }
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            iWXApmMonitorAdapter.addProperty(str, obj);
        }
    }

    public void addStats(String str, double d) {
        if (!this.mEnd && str != null) {
            this.recordStatsMap.put(str, Double.valueOf(d));
            if (this.isReady) {
                sendStats(str, d);
            }
        }
    }

    private void sendStats(String str, double d) {
        if (WXAnalyzerDataTransfer.isOpenPerformance) {
            WXAnalyzerDataTransfer.transferPerformance(this.mInstanceId, "stats", str, Double.valueOf(d));
        }
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            iWXApmMonitorAdapter.addStats(str, d);
        }
    }

    public boolean hasInit() {
        return this.mHasInit;
    }

    public void doInit() {
        String str;
        if (this.isReady && !this.mHasInit) {
            this.mHasInit = true;
            IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
            if (iWXApmMonitorAdapter != null) {
                iWXApmMonitorAdapter.onStart(this.mInstanceId);
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(this.mInstanceId);
                if (wXSDKInstance == null) {
                    str = "unKnowUrl";
                } else {
                    str = wXSDKInstance.getBundleUrl();
                }
                addProperty(KEY_PAGE_PROPERTIES_BUBDLE_URL, str);
                addProperty(KEY_PROPERTIES_ERROR_CODE, VALUE_ERROR_CODE_DEFAULT);
                addProperty(KEY_PAGE_PROPERTIES_JSLIB_VERSION, WXEnvironment.JS_LIB_SDK_VERSION);
                addProperty(KEY_PAGE_PROPERTIES_WEEX_VERSION, WXEnvironment.WXSDK_VERSION);
                addProperty(KEY_PAGE_PROPERTIES_WEEX_VERSION, WXEnvironment.WXSDK_VERSION);
                addStats("wxReInitCount", (double) WXBridgeManager.reInitCount);
                if (wXSDKInstance != null) {
                    addProperty(KEY_PAGE_PROPERTIES_UIKIT_TYPE, wXSDKInstance.getRenderType());
                }
                addProperty("wxUseRuntimeApi", Boolean.valueOf(WXEnvironment.sUseRunTimeApi));
                if (wXSDKInstance != null && (wXSDKInstance.getRenderStrategy() == WXRenderStrategy.DATA_RENDER || wXSDKInstance.getRenderStrategy() == WXRenderStrategy.DATA_RENDER_BINARY)) {
                    addProperty(KEY_PAGE_PROPERTIES_RENDER_TYPE, WXEnvironment.EAGLE);
                }
                if (wXSDKInstance != null) {
                    for (Map.Entry next : wXSDKInstance.getContainerInfo().entrySet()) {
                        addProperty((String) next.getKey(), next.getValue());
                    }
                }
            }
        }
    }

    public void setPageName(String str) {
        WXSDKInstance wXSDKInstance;
        if (TextUtils.isEmpty(str) && (wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(this.mInstanceId)) != null) {
            str = wXSDKInstance.getContainerInfo().get(KEY_PAGE_PROPERTIES_CONTAINER_NAME);
        }
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            str = iWXApmMonitorAdapter.parseReportUrl(str);
        }
        this.reportPageName = str;
        String str2 = TextUtils.isEmpty(str) ? "emptyPageName" : this.reportPageName;
        this.reportPageName = str2;
        addProperty(KEY_PAGE_PROPERTIES_BIZ_ID, str2);
    }

    public void onAppear() {
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            iWXApmMonitorAdapter.onAppear();
        }
    }

    public void onDisAppear() {
        IWXApmMonitorAdapter iWXApmMonitorAdapter = this.apmInstance;
        if (iWXApmMonitorAdapter != null) {
            iWXApmMonitorAdapter.onDisappear();
        }
    }

    public void onEnd() {
        if (this.apmInstance != null && !this.mEnd) {
            new Handler(Looper.getMainLooper()).removeCallbacks(this.delayCollectDataTask);
            recordPerformanceDetailData();
            this.exceptionRecord.clear();
            this.mUIHandler.removeCallbacks(this.jsPerformanceCallBack);
            onStage(KEY_PAGE_STAGES_DESTROY);
            if (!this.mHasInit) {
                this.apmInstance.onEnd();
            }
            this.mEnd = true;
            if (WXEnvironment.isApkDebugable()) {
                printLog();
            }
        }
    }

    public void doDelayCollectData() {
        new Handler(Looper.getMainLooper()).postDelayed(this.delayCollectDataTask, 8000);
    }

    private void printLog() {
        Long l = this.stageMap.get(KEY_PAGE_STAGES_DOWN_BUNDLE_START);
        Long l2 = this.stageMap.get(KEY_PAGE_STAGES_DOWN_BUNDLE_END);
        Long l3 = this.stageMap.get(KEY_PAGE_STAGES_INTERACTION);
        Long l4 = this.stageMap.get(KEY_PAGE_STAGES_CONTAINER_READY);
        if (!(l2 == null || l == null)) {
            WXLogUtils.d("test->", "downLoadTime: " + (l2.longValue() - l.longValue()));
        }
        if (!(l2 == null || l3 == null)) {
            WXLogUtils.d("test->", "renderTime: " + (l3.longValue() - l2.longValue()));
        }
        if (l4 != null && l3 != null) {
            WXLogUtils.d("test->", "showTime: " + (l3.longValue() - l4.longValue()));
        }
    }

    public void arriveNewFsRenderTime() {
        if (this.apmInstance != null) {
            onStage(KEY_PAGE_STAGES_NEW_FSRENDER);
        }
    }

    public void arriveFSRenderTime() {
        if (this.apmInstance != null) {
            this.isFSEnd = true;
            onStage(KEY_PAGE_STAGES_FSRENDER);
        }
    }

    public void arriveInteraction(WXComponent wXComponent) {
        WXPerformance wXPerformance;
        double d;
        if (this.apmInstance != null && wXComponent != null && wXComponent.getInstance() != null) {
            if (WXAnalyzerDataTransfer.isOpenPerformance) {
                WXAnalyzerDataTransfer.transferInteractionInfo(wXComponent);
            }
            if (this.apmInstance != null && (wXPerformance = wXComponent.getInstance().getWXPerformance()) != null) {
                long fixUnixTime = WXUtils.getFixUnixTime();
                if (WXAnalyzerDataTransfer.isInteractionLogOpen()) {
                    Log.d(WXAnalyzerDataTransfer.INTERACTION_TAG, "[client][wxinteraction]" + wXComponent.getInstance().getInstanceId() + "," + wXComponent.getComponentType() + "," + wXComponent.getRef() + "," + wXComponent.getStyles() + "," + wXComponent.getAttrs());
                }
                if (!this.hasRecordFistInteractionView) {
                    onStage(KEY_PAGE_STAGES_FIRST_INTERACTION_VIEW);
                    this.hasRecordFistInteractionView = true;
                }
                if (!this.forceStopRecordInteraction) {
                    long fixUnixTime2 = WXUtils.getFixUnixTime();
                    if (fixUnixTime2 - this.preUpdateTime > 50) {
                        WXBridgeManager.getInstance().onInteractionTimeUpdate(this.mInstanceId);
                        this.preUpdateTime = fixUnixTime2;
                    }
                    this.interactionComponentCreateTime = this.componentCreateTime;
                    this.interactionViewCreateTime = this.viewCreateTime;
                    Double d2 = this.recordStatsMap.get(KEY_PAGE_STATS_LAYOUT_TIME);
                    if (d2 == null) {
                        d = 0.0d;
                    } else {
                        d = d2.doubleValue();
                    }
                    this.interactionLayoutTime = d;
                    wXPerformance.interactionTime = fixUnixTime - wXPerformance.renderUnixTimeOrigin;
                    wXPerformance.interactionRealUnixTime = System.currentTimeMillis();
                    onStageWithTime(KEY_PAGE_STAGES_INTERACTION, fixUnixTime);
                    updateDiffStats(KEY_PAGE_STATS_I_SCREEN_VIEW_COUNT, 1.0d);
                    updateMaxStats(KEY_PAGE_STATS_I_ALL_VIEW_COUNT, (double) wXPerformance.localInteractionViewAddCount);
                    WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.mInstanceId);
                    if (sDKInstance != null) {
                        updateMaxStats(KEY_PAGE_STATS_I_COMPONENT_CREATE_COUNT, (double) sDKInstance.getWXPerformance().componentCount);
                    }
                }
            }
        }
    }

    public void updateFSDiffStats(String str, double d) {
        if (this.apmInstance != null && !this.isFSEnd) {
            updateDiffStats(str, d);
        }
    }

    public void recordPerformanceDetailData() {
        if (!this.mHasRecordDetailData) {
            this.mHasRecordDetailData = true;
            addStats(KEY_PAGE_STATS_VIEW_CREATE_COST, (double) this.interactionViewCreateTime);
            addStats(KEY_PAGE_STATS_COMPONENT_CREATE_COST, (double) this.interactionComponentCreateTime);
            addStats(KEY_PAGE_STATS_EXECUTE_JS_CALLBACK_COST, (double) this.interactionJsCallBackTime);
            addStats(KEY_PAGE_STATS_LAYOUT_TIME, this.interactionLayoutTime);
        }
    }

    public void updateDiffStats(String str, double d) {
        if (this.apmInstance != null) {
            Double valueOf = Double.valueOf(this.recordStatsMap.containsKey(str) ? this.recordStatsMap.get(str).doubleValue() : 0.0d);
            if (valueOf == null) {
                WXErrorCode wXErrorCode = WXErrorCode.WX_ERR_HASH_MAP_TMP;
                WXExceptionUtils.commitCriticalExceptionRT("", wXErrorCode, "updateDiffStats", "key : " + str, (Map<String, String>) null);
                return;
            }
            addStats(str, valueOf.doubleValue() + d);
        }
    }

    public void updateMaxStats(String str, double d) {
        if (this.apmInstance != null) {
            Double valueOf = Double.valueOf(this.recordStatsMap.containsKey(str) ? this.recordStatsMap.get(str).doubleValue() : 0.0d);
            if (valueOf == null) {
                WXErrorCode wXErrorCode = WXErrorCode.WX_ERR_HASH_MAP_TMP;
                WXExceptionUtils.commitCriticalExceptionRT("", wXErrorCode, "updateMaxStats", "key : " + str, (Map<String, String>) null);
            } else if (valueOf.doubleValue() < d) {
                addStats(str, Double.valueOf(d).doubleValue());
            }
        }
    }

    public void updateRecordInfo(Map<String, Object> map) {
        if (this.apmInstance != null && map != null) {
            addPropeyFromExtParms(KEY_PAGE_PROPERTIES_REQUEST_TYPE, KEY_PAGE_PROPERTIES_REQUEST_TYPE, map);
            addPropeyFromExtParms(WXPerformance.CACHE_TYPE, KEY_PAGE_PROPERTIES_CACHE_TYPE, map);
            addPropeyFromExtParms("zCacheInfo", KEY_PAGE_PROPERTIES_CACHE_INFO, map);
            addStats(KEY_PAGE_STATS_JSLIB_INIT_TIME, (double) WXEnvironment.sJSLibInitTime);
            addProperty(KEY_PAGE_PROPERTIES_JS_FM_INI, Boolean.valueOf(WXEnvironment.JsFrameworkInit));
            Object obj = map.get("actualNetworkTime");
            if (obj instanceof Long) {
                updateDiffStats(KEY_PAGE_STATS_ACTUAL_DOWNLOAD_TIME, ((Long) obj).doubleValue());
            }
        }
    }

    private void addPropeyFromExtParms(String str, String str2, Map<String, Object> map) {
        Object obj = map.get(str);
        if (obj instanceof String) {
            addProperty(str2, obj);
        }
    }

    public void actionNetRequest() {
        if (!this.isFSEnd) {
            updateFSDiffStats(KEY_PAGE_STATS_FS_REQUEST_NUM, 1.0d);
        }
        updateDiffStats(KEY_PAGE_STATS_NET_NUM, 1.0d);
    }

    public void actionNetResult(boolean z, String str) {
        if (z) {
            updateDiffStats(KEY_PAGE_STATS_NET_SUCCESS_NUM, 1.0d);
        } else {
            updateDiffStats(KEY_PAGE_STATS_NET_FAIL_NUM, 1.0d);
        }
    }

    public void actionLoadImg() {
        updateDiffStats(KEY_PAGE_STATS_IMG_LOAD_NUM, 1.0d);
    }

    public void actionLoadImgResult(boolean z, String str) {
        if (z) {
            updateDiffStats(KEY_PAGE_STATS_IMG_LOAD_SUCCESS_NUM, 1.0d);
        } else {
            updateDiffStats(KEY_PAGE_STATS_IMG_LOAD_FAIL_NUM, 1.0d);
        }
    }

    public void sendPerformanceToJS() {
        if (!this.hasSendInteractionToJS) {
            this.hasSendInteractionToJS = true;
            WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(this.mInstanceId);
            if (wXSDKInstance != null) {
                HashMap hashMap = new HashMap(2);
                hashMap.put(KEY_PAGE_PROPERTIES_BIZ_ID, this.reportPageName);
                hashMap.put(KEY_PAGE_PROPERTIES_BUBDLE_URL, wXSDKInstance.getBundleUrl());
                HashMap hashMap2 = new HashMap(1);
                hashMap2.put(KEY_PAGE_STAGES_INTERACTION, Long.valueOf(wXSDKInstance.getWXPerformance().interactionRealUnixTime));
                HashMap hashMap3 = new HashMap(2);
                hashMap3.put("stage", hashMap2);
                hashMap3.put("properties", hashMap);
                wXSDKInstance.fireGlobalEventCallback("wx_apm", hashMap3);
            }
        }
    }

    public String toPerfString() {
        Long l = this.stageMap.get(KEY_PAGE_STAGES_RENDER_ORGIGIN);
        Long l2 = this.stageMap.get(KEY_PAGE_STAGES_INTERACTION);
        Long l3 = this.stageMap.get(KEY_PAGE_STAGES_NEW_FSRENDER);
        StringBuilder sb = new StringBuilder();
        if (!(l == null || l2 == null)) {
            sb.append("interactiveTime " + (l2.longValue() - l.longValue()) + "ms");
        }
        if (l3 != null) {
            sb.append(" wxNewFsRender " + l3 + "ms");
        }
        return sb.toString();
    }

    public void updateNativePerformanceData(Map<String, String> map) {
        double d;
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                try {
                    d = Double.valueOf((String) next.getValue()).doubleValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    d = -1.0d;
                }
                if (d != -1.0d) {
                    this.recordStatsMap.put((String) next.getKey(), Double.valueOf(d));
                }
            }
        }
    }
}
