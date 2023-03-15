package io.dcloud.net;

import com.taobao.weex.http.WXStreamModule;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.common.util.net.RequestData;
import java.io.InputStream;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class XMLHttpRequest implements IReqListener, IResponseListener {
    private static final int ERROR_OTHER_CODE = 1;
    private static final int ERROR_TIME_OUT_CODE = 0;
    private static final int LOADED = 4;
    private static final int RECEIVING = 3;
    private String mCallbackId;
    private int mErrorCode = -1;
    private NetWork mNetWork;
    private int mReadyState;
    private RequestData mRequestData;
    private int mState;
    private String mStatusText;
    public String mUUID;
    IWebview mWebview;

    /* renamed from: io.dcloud.net.XMLHttpRequest$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                io.dcloud.common.DHInterface.IReqListener$NetState[] r0 = io.dcloud.common.DHInterface.IReqListener.NetState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState = r0
                io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_END     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_ERROR     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_TIMEOUT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.net.XMLHttpRequest.AnonymousClass1.<clinit>():void");
        }
    }

    public XMLHttpRequest(String str, String str2, String str3, IWebview iWebview) {
        this.mUUID = str;
        RequestData requestData = new RequestData(str2, str3);
        this.mRequestData = requestData;
        requestData.unTrustedCAType = iWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
        this.mRequestData.addHeader(IWebview.USER_AGENT, iWebview.getWebviewProperty(IWebview.USER_AGENT));
        this.mNetWork = new NetWork(3, this.mRequestData, this, this);
        this.mWebview = iWebview;
    }

    private JSONObject headersToJSON(Map<String, String> map) {
        JSONObject jSONObject = new JSONObject();
        for (String next : map.keySet()) {
            try {
                jSONObject.put(next, map.get(next));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    private String toJsResponseText(String str) {
        return JSONUtil.toJSONableString(str);
    }

    public NetWork getNetWork() {
        return this.mNetWork;
    }

    public RequestData getRequestData() {
        return this.mRequestData;
    }

    public void onNetStateChanged(IReqListener.NetState netState, boolean z) {
        if (z) {
            this.mReadyState = 4;
            return;
        }
        int i = AnonymousClass1.$SwitchMap$io$dcloud$common$DHInterface$IReqListener$NetState[netState.ordinal()];
        if (i == 1) {
            this.mReadyState = 4;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        } else if (i == 2) {
            this.mReadyState = 3;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        } else if (i == 3) {
            this.mReadyState = 4;
            this.mErrorCode = 1;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        } else if (i == 4) {
            this.mReadyState = 4;
            this.mErrorCode = 0;
            JSUtil.execCallback(this.mWebview, this.mCallbackId, toJSON(), JSUtil.OK, true);
        }
    }

    public int onReceiving(InputStream inputStream) {
        return 0;
    }

    public void onResponseState(int i, String str) {
        this.mState = i;
        this.mStatusText = str;
        Logger.d("xhr", "onResponseState pState=" + i + ";mCallbackId=" + this.mCallbackId);
    }

    public void onResponsing(InputStream inputStream) {
    }

    public void setCallbackId(String str) {
        this.mCallbackId = str;
    }

    public JSONObject toJSON() {
        JSONObject jSONObject = new JSONObject();
        String responseText = this.mNetWork.getResponseText();
        try {
            jSONObject.put("readyState", this.mReadyState);
            jSONObject.put("status", this.mState);
            jSONObject.put(WXStreamModule.STATUS_TEXT, this.mStatusText);
            jSONObject.put("responseText", responseText);
            jSONObject.put(WXBasicComponentType.HEADER, headersToJSON(this.mNetWork.getHeadersAndValues()));
            int i = this.mErrorCode;
            if (i > -1) {
                jSONObject.put("error", i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
