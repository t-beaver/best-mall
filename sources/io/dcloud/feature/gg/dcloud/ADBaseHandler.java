package io.dcloud.feature.gg.dcloud;

import android.content.Context;
import android.view.View;
import androidx.core.app.NotificationCompat;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.sdk.core.util.Const;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ADBaseHandler {
    public final int AD_REQUESTING = 0;
    public final int AD_REQUEST_FAIL = -1;
    public final int AD_REQUEST_SUCCESS = 1;
    public String AD_TAD = "";
    protected int currentStatus = 0;
    private JSONObject errorJson;
    protected String errorMsg = "";
    private boolean isBest = false;
    private boolean isShow = false;
    protected OnAdsRequestListener listeners;
    protected Context mContext;
    private int source;
    private long startLoadTime;
    private long totalLoadTime;

    public interface OnAdsRequestListener {
        void fail(ADBaseHandler aDBaseHandler);

        void success(ADBaseHandler aDBaseHandler);
    }

    public void addRequestListener(OnAdsRequestListener onAdsRequestListener) {
        if (this.listeners == null) {
            this.listeners = onAdsRequestListener;
        }
    }

    public void clearListener() {
        this.listeners = null;
    }

    public void endLoadAds() {
        this.totalLoadTime = System.currentTimeMillis() - this.startLoadTime;
    }

    public void execFail(int i, String str) {
        this.currentStatus = -1;
        if (!this.AD_TAD.equals(Const.TYPE_GDT) || i != 6000) {
            this.errorMsg = String.valueOf(i);
        } else {
            this.errorMsg = i + Operators.BRACKET_START_STR + str + Operators.BRACKET_END_STR;
        }
        if (i != -9999) {
            JSONObject jSONObject = new JSONObject();
            this.errorJson = jSONObject;
            try {
                jSONObject.put("code", i);
                this.errorJson.put(NotificationCompat.CATEGORY_MESSAGE, str);
            } catch (JSONException unused) {
            }
        }
        OnAdsRequestListener onAdsRequestListener = this.listeners;
        if (onAdsRequestListener != null) {
            onAdsRequestListener.fail(this);
        }
    }

    public void execSuccess() {
        this.currentStatus = 1;
        OnAdsRequestListener onAdsRequestListener = this.listeners;
        if (onAdsRequestListener != null) {
            onAdsRequestListener.success(this);
        }
    }

    public int getAdRequestStatus() {
        return this.currentStatus;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public JSONObject getResult() {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("provider", this.AD_TAD);
            int i = this.currentStatus;
            if (i == -1) {
                i = 0;
            }
            jSONObject2.put("ret", i);
            if (this.currentStatus == -1 && (jSONObject = this.errorJson) != null) {
                jSONObject2.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, jSONObject);
            }
            jSONObject2.put(Constants.Value.TIME, this.totalLoadTime);
        } catch (JSONException unused) {
        }
        return jSONObject2;
    }

    public int getSource() {
        return this.source;
    }

    public boolean isBest() {
        return this.isBest;
    }

    public boolean isHasContext() {
        return this.mContext != null;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public abstract void onBack();

    public abstract void onCreate(Context context);

    public abstract View onCreateSplash(Context context, String str, ICallBack iCallBack);

    public abstract boolean onSplashClose(View view);

    public void pullAds(Context context) {
        this.currentStatus = 0;
        this.errorMsg = "";
    }

    public void pullAds(Context context, String str, OnAdsRequestListener onAdsRequestListener) {
    }

    public abstract void setAdData(String str, String str2);

    public void setBest() {
        this.isBest = true;
    }

    public void setShowed() {
        this.isShow = true;
    }

    public void setSource(int i) {
        this.source = i;
    }

    public void startLoadAds() {
        this.startLoadTime = System.currentTimeMillis();
    }
}
