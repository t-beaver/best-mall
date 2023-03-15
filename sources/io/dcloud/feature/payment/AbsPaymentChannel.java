package io.dcloud.feature.payment;

import android.content.Context;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.JSUtil;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbsPaymentChannel implements IReflectAble {
    /* access modifiers changed from: private */
    public String a;
    protected String description;
    protected String featureName;
    protected String id;
    protected Context mContext;
    protected final IPaymentListener mListener = new a();
    protected IWebview mWebview;
    protected String name;
    protected boolean serviceReady;

    class a implements IPaymentListener {
        a() {
        }

        public void onError(int i, String str) {
            String json = DOMException.toJSON(i, str);
            AbsPaymentChannel absPaymentChannel = AbsPaymentChannel.this;
            Deprecated_JSUtil.execCallback(absPaymentChannel.mWebview, absPaymentChannel.a, json, JSUtil.ERROR, true, false);
        }

        public void onSuccess(PaymentResult paymentResult) {
            AbsPaymentChannel absPaymentChannel = AbsPaymentChannel.this;
            JSUtil.execCallback(absPaymentChannel.mWebview, absPaymentChannel.a, paymentResult.toJSONObject(), JSUtil.OK, false);
        }
    }

    public String getFullDescription() {
        return this.featureName + this.description;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public abstract void installService();

    /* access modifiers changed from: protected */
    public void isReadyToPay(String str, String str2) {
    }

    /* access modifiers changed from: protected */
    public abstract void request(String str);

    public JSONObject toJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", this.id);
            jSONObject.put("description", this.description);
            jSONObject.put("serviceReady", this.serviceReady);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public void updateWebview(IWebview iWebview) {
        this.mWebview = iWebview;
    }

    /* access modifiers changed from: package-private */
    public final void a(String str, String str2) {
        this.a = str2;
        request(str);
    }
}
