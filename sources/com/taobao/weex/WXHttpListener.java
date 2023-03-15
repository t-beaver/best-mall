package com.taobao.weex;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.LogDetail;
import java.util.List;
import java.util.Map;

public class WXHttpListener implements IWXHttpAdapter.OnHttpListener {
    private WXRenderStrategy flag;
    private WXSDKInstance instance;
    private boolean isInstanceReady;
    public boolean isPreDownLoadMode;
    private boolean isResponseHasWait;
    private String jsonInitData;
    private WXInstanceApm mApmForInstance;
    private String mBundleUrl;
    private LogDetail mLogDetail;
    private WXResponse mResponse;
    private IWXUserTrackAdapter mUserTrackAdapter;
    private WXPerformance mWXPerformance;
    private Map<String, Object> options;
    private String pageName;
    private long startRequestTime;
    private int traceId;

    public void onFail(WXResponse wXResponse) {
    }

    public void onHttpUploadProgress(int i) {
    }

    public WXHttpListener(WXSDKInstance wXSDKInstance) {
        this.isPreDownLoadMode = false;
        this.isInstanceReady = false;
        this.isResponseHasWait = false;
        if (wXSDKInstance != null) {
            this.mLogDetail = wXSDKInstance.mTimeCalculator.createLogDetail("downloadBundleJS");
        }
        this.instance = wXSDKInstance;
        this.traceId = WXTracing.nextId();
        this.mWXPerformance = wXSDKInstance.getWXPerformance();
        this.mApmForInstance = wXSDKInstance.getApmForInstance();
        this.mUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
        if (WXTracing.isAvailable()) {
            WXTracing.TraceEvent newEvent = WXTracing.newEvent("downloadBundleJS", wXSDKInstance.getInstanceId(), -1);
            newEvent.iid = wXSDKInstance.getInstanceId();
            newEvent.tname = "Network";
            newEvent.ph = "B";
            newEvent.traceId = this.traceId;
            newEvent.submit();
        }
    }

    public WXHttpListener(WXSDKInstance wXSDKInstance, String str) {
        this(wXSDKInstance);
        this.startRequestTime = System.currentTimeMillis();
        this.mBundleUrl = str;
    }

    public WXHttpListener(WXSDKInstance wXSDKInstance, String str, Map<String, Object> map, String str2, WXRenderStrategy wXRenderStrategy, long j) {
        this(wXSDKInstance);
        this.pageName = str;
        this.options = map;
        this.jsonInitData = str2;
        this.flag = wXRenderStrategy;
        this.startRequestTime = j;
        this.mBundleUrl = wXSDKInstance.getBundleUrl();
    }

    public void setSDKInstance(WXSDKInstance wXSDKInstance) {
        this.instance = wXSDKInstance;
    }

    /* access modifiers changed from: protected */
    public WXSDKInstance getInstance() {
        return this.instance;
    }

    public void onHttpStart() {
        WXSDKInstance wXSDKInstance = this.instance;
        if (wXSDKInstance != null && wXSDKInstance.getWXStatisticsListener() != null) {
            this.instance.getWXStatisticsListener().onHttpStart();
            LogDetail logDetail = this.mLogDetail;
            if (logDetail != null) {
                logDetail.taskStart();
            }
        }
    }

    public void onHeadersReceived(int i, Map<String, List<String>> map) {
        WXSDKInstance wXSDKInstance = this.instance;
        if (!(wXSDKInstance == null || wXSDKInstance.getWXStatisticsListener() == null)) {
            this.instance.getWXStatisticsListener().onHeadersReceived();
            this.instance.onHttpStart();
        }
        WXSDKInstance wXSDKInstance2 = this.instance;
        if (wXSDKInstance2 != null && wXSDKInstance2.responseHeaders != null && map != null) {
            this.instance.responseHeaders.putAll(map);
        }
    }

    public void onHttpResponseProgress(int i) {
        this.instance.getApmForInstance().extInfo.put(WXInstanceApm.VALUE_BUNDLE_LOAD_LENGTH, Integer.valueOf(i));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v27, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onHttpFinish(com.taobao.weex.common.WXResponse r9) {
        /*
            r8 = this;
            com.taobao.weex.utils.tools.LogDetail r0 = r8.mLogDetail
            if (r0 == 0) goto L_0x0007
            r0.taskEnd()
        L_0x0007:
            com.taobao.weex.WXSDKInstance r0 = r8.instance
            if (r0 == 0) goto L_0x001a
            com.taobao.weex.IWXStatisticsListener r0 = r0.getWXStatisticsListener()
            if (r0 == 0) goto L_0x001a
            com.taobao.weex.WXSDKInstance r0 = r8.instance
            com.taobao.weex.IWXStatisticsListener r0 = r0.getWXStatisticsListener()
            r0.onHttpFinish()
        L_0x001a:
            boolean r0 = com.taobao.weex.tracing.WXTracing.isAvailable()
            if (r0 == 0) goto L_0x0057
            com.taobao.weex.WXSDKInstance r0 = r8.instance
            java.lang.String r0 = r0.getInstanceId()
            r1 = -1
            java.lang.String r2 = "downloadBundleJS"
            com.taobao.weex.tracing.WXTracing$TraceEvent r0 = com.taobao.weex.tracing.WXTracing.newEvent(r2, r0, r1)
            int r1 = r8.traceId
            r0.traceId = r1
            java.lang.String r1 = "Network"
            r0.tname = r1
            java.lang.String r1 = "E"
            r0.ph = r1
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            r0.extParams = r1
            if (r9 == 0) goto L_0x0054
            byte[] r1 = r9.originalData
            if (r1 == 0) goto L_0x0054
            java.util.Map<java.lang.String, java.lang.Object> r1 = r0.extParams
            byte[] r2 = r9.originalData
            int r2 = r2.length
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            java.lang.String r3 = "BundleSize"
            r1.put(r3, r2)
        L_0x0054:
            r0.submit()
        L_0x0057:
            com.taobao.weex.common.WXPerformance r0 = r8.mWXPerformance
            long r1 = java.lang.System.currentTimeMillis()
            long r3 = r8.startRequestTime
            long r1 = r1 - r3
            r0.networkTime = r1
            if (r9 == 0) goto L_0x01bf
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            if (r0 == 0) goto L_0x01bf
            com.taobao.weex.performance.WXInstanceApm r0 = r8.mApmForInstance
            java.util.Map<java.lang.String, java.lang.Object> r1 = r9.extendParams
            r0.updateRecordInfo(r1)
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "actualNetworkTime"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.Long
            r3 = 0
            if (r2 == 0) goto L_0x0086
            java.lang.Long r0 = (java.lang.Long) r0
            long r5 = r0.longValue()
            goto L_0x0087
        L_0x0086:
            r5 = r3
        L_0x0087:
            r1.actualNetworkTime = r5
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "pureNetworkTime"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.Long
            if (r2 == 0) goto L_0x009e
            java.lang.Long r0 = (java.lang.Long) r0
            long r5 = r0.longValue()
            goto L_0x009f
        L_0x009e:
            r5 = r3
        L_0x009f:
            r1.pureNetworkTime = r5
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "connectionType"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.String
            java.lang.String r5 = ""
            if (r2 == 0) goto L_0x00b4
            java.lang.String r0 = (java.lang.String) r0
            goto L_0x00b5
        L_0x00b4:
            r0 = r5
        L_0x00b5:
            r1.connectionType = r0
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "packageSpendTime"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.Long
            if (r2 == 0) goto L_0x00cc
            java.lang.Long r0 = (java.lang.Long) r0
            long r6 = r0.longValue()
            goto L_0x00cd
        L_0x00cc:
            r6 = r3
        L_0x00cd:
            r1.packageSpendTime = r6
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "syncTaskTime"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.Long
            if (r2 == 0) goto L_0x00e3
            java.lang.Long r0 = (java.lang.Long) r0
            long r3 = r0.longValue()
        L_0x00e3:
            r1.syncTaskTime = r3
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "requestType"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.String
            if (r2 == 0) goto L_0x00f6
            java.lang.String r0 = (java.lang.String) r0
            goto L_0x00f8
        L_0x00f6:
            java.lang.String r0 = "none"
        L_0x00f8:
            r1.requestType = r0
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            com.taobao.weex.common.WXPerformance$Dimension r1 = com.taobao.weex.common.WXPerformance.Dimension.cacheType
            java.lang.String r1 = r1.toString()
            java.lang.Object r0 = r0.get(r1)
            boolean r1 = r0 instanceof java.lang.String
            if (r1 == 0) goto L_0x0110
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            java.lang.String r0 = (java.lang.String) r0
            r1.cacheType = r0
        L_0x0110:
            java.util.Map<java.lang.String, java.lang.Object> r0 = r9.extendParams
            java.lang.String r1 = "zCacheInfo"
            java.lang.Object r0 = r0.get(r1)
            com.taobao.weex.common.WXPerformance r1 = r8.mWXPerformance
            boolean r2 = r0 instanceof java.lang.String
            if (r2 == 0) goto L_0x0122
            r5 = r0
            java.lang.String r5 = (java.lang.String) r5
        L_0x0122:
            r1.zCacheInfo = r5
            com.taobao.weex.common.WXPerformance r0 = r8.mWXPerformance
            java.lang.String r0 = r0.requestType
            boolean r0 = r8.isNet(r0)
            if (r0 == 0) goto L_0x01bf
            com.taobao.weex.adapter.IWXUserTrackAdapter r0 = r8.mUserTrackAdapter
            if (r0 == 0) goto L_0x01bf
            com.taobao.weex.common.WXPerformance r5 = new com.taobao.weex.common.WXPerformance
            com.taobao.weex.WXSDKInstance r0 = r8.instance
            java.lang.String r0 = r0.getInstanceId()
            r5.<init>(r0)
            java.lang.String r0 = r8.mBundleUrl
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x015e
            java.lang.String r0 = r8.mBundleUrl     // Catch:{ Exception -> 0x015a }
            android.net.Uri r0 = android.net.Uri.parse(r0)     // Catch:{ Exception -> 0x015a }
            android.net.Uri$Builder r0 = r0.buildUpon()     // Catch:{ Exception -> 0x015a }
            android.net.Uri$Builder r0 = r0.clearQuery()     // Catch:{ Exception -> 0x015a }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x015a }
            r5.args = r0     // Catch:{ Exception -> 0x015a }
            goto L_0x015e
        L_0x015a:
            java.lang.String r0 = r8.pageName
            r5.args = r0
        L_0x015e:
            java.lang.String r0 = r9.statusCode
            java.lang.String r1 = "200"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0181
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_ERR_JSBUNDLE_DOWNLOAD
            java.lang.String r0 = r0.getErrorCode()
            r5.errCode = r0
            java.lang.String r0 = r9.errorCode
            r5.appendErrMsg(r0)
            java.lang.String r0 = "|"
            r5.appendErrMsg(r0)
            java.lang.String r0 = r9.errorMsg
            r5.appendErrMsg(r0)
            goto L_0x01ae
        L_0x0181:
            java.lang.String r0 = r9.statusCode
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x01a6
            byte[] r0 = r9.originalData
            if (r0 == 0) goto L_0x0192
            byte[] r0 = r9.originalData
            int r0 = r0.length
            if (r0 > 0) goto L_0x01a6
        L_0x0192:
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_ERR_JSBUNDLE_DOWNLOAD
            java.lang.String r0 = r0.getErrorCode()
            r5.errCode = r0
            java.lang.String r0 = r9.statusCode
            r5.appendErrMsg(r0)
            java.lang.String r0 = "|template is null!"
            r5.appendErrMsg(r0)
            goto L_0x01ae
        L_0x01a6:
            com.taobao.weex.common.WXErrorCode r0 = com.taobao.weex.common.WXErrorCode.WX_SUCCESS
            java.lang.String r0 = r0.getErrorCode()
            r5.errCode = r0
        L_0x01ae:
            com.taobao.weex.adapter.IWXUserTrackAdapter r1 = r8.mUserTrackAdapter
            if (r1 == 0) goto L_0x01bf
            com.taobao.weex.WXSDKInstance r0 = r8.instance
            android.content.Context r2 = r0.getContext()
            r3 = 0
            r6 = 0
            java.lang.String r4 = "jsDownload"
            r1.commit(r2, r3, r4, r5, r6)
        L_0x01bf:
            boolean r0 = r8.isPreDownLoadMode
            if (r0 == 0) goto L_0x01dd
            boolean r0 = r8.isInstanceReady
            java.lang.String r1 = "test->"
            if (r0 == 0) goto L_0x01d2
            java.lang.String r0 = "DownLoad didHttpFinish on http"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.String) r0)
            r8.didHttpFinish(r9)
            goto L_0x01e0
        L_0x01d2:
            java.lang.String r0 = "DownLoad end before activity created"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.String) r0)
            r8.mResponse = r9
            r9 = 1
            r8.isResponseHasWait = r9
            goto L_0x01e0
        L_0x01dd:
            r8.didHttpFinish(r9)
        L_0x01e0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXHttpListener.onHttpFinish(com.taobao.weex.common.WXResponse):void");
    }

    public void onInstanceReady() {
        if (this.isPreDownLoadMode) {
            this.isInstanceReady = true;
            if (this.isResponseHasWait) {
                WXLogUtils.e("test->", "preDownLoad didHttpFinish on ready");
                didHttpFinish(this.mResponse);
            }
        }
    }

    private void didHttpFinish(WXResponse wXResponse) {
        String str;
        if (wXResponse != null && wXResponse.originalData != null && TextUtils.equals("200", wXResponse.statusCode)) {
            this.mApmForInstance.onStage(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_END);
            onSuccess(wXResponse);
            str = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
        } else if (TextUtils.equals(WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode(), wXResponse.statusCode)) {
            WXLogUtils.e("user intercept: WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR");
            str = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
            WXSDKInstance wXSDKInstance = this.instance;
            wXSDKInstance.onRenderError(str, "|response.errorMsg==" + wXResponse.errorMsg + "|instance bundleUrl = \n" + this.instance.getBundleUrl() + "|instance requestUrl = \n" + Uri.decode(WXSDKInstance.requestUrl));
            onFail(wXResponse);
        } else if (wXResponse == null || wXResponse.originalData == null || !TextUtils.equals("-206", wXResponse.statusCode)) {
            str = WXErrorCode.WX_DEGRAD_ERR_NETWORK_BUNDLE_DOWNLOAD_FAILED.getErrorCode();
            this.instance.onRenderError(str, wXResponse.errorMsg);
            onFail(wXResponse);
        } else {
            WXLogUtils.e("user intercept: WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED");
            str = WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED.getErrorCode();
            WXSDKInstance wXSDKInstance2 = this.instance;
            wXSDKInstance2.onRenderError(str, WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED.getErrorCode() + "|response.errorMsg==" + wXResponse.errorMsg);
            onFail(wXResponse);
        }
        if (!WXInstanceApm.VALUE_ERROR_CODE_DEFAULT.equals(str)) {
            this.mApmForInstance.addProperty(WXInstanceApm.KEY_PROPERTIES_ERROR_CODE, str);
        }
    }

    private boolean isNet(String str) {
        return "network".equals(str) || "2g".equals(str) || "3g".equals(str) || "4g".equals(str) || "wifi".equals(str) || "other".equals(str) || "unknown".equals(str);
    }

    public void onSuccess(WXResponse wXResponse) {
        if (this.flag == WXRenderStrategy.DATA_RENDER_BINARY) {
            this.instance.render(this.pageName, wXResponse.originalData, this.options, this.jsonInitData);
            return;
        }
        this.instance.render(this.pageName, new String(wXResponse.originalData), this.options, this.jsonInitData, this.flag);
    }
}
