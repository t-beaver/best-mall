package io.dcloud.sdk.poly.base.config;

import androidx.core.app.NotificationCompat;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    private long a = 0;
    private String b;
    private String c;
    private String d;
    private String e;
    private int f;
    private int g;
    private String h;
    private JSONObject i;
    private boolean j = true;

    public b a(long j2) {
        this.a = j2;
        return this;
    }

    public b b(String str) {
        this.b = str;
        return this;
    }

    public b c(String str) {
        this.d = str;
        return this;
    }

    public b d(String str) {
        this.e = str;
        return this;
    }

    public JSONObject e() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Constants.Value.TIME, this.a);
            jSONObject.put("ret", this.f);
            if (this.f == 0) {
                jSONObject.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, this.i);
            }
            jSONObject.put("tid", this.e);
            jSONObject.put("mediaId", this.c);
            jSONObject.put("slotId", this.d);
            jSONObject.put("provider", this.b);
        } catch (JSONException unused) {
        }
        return jSONObject;
    }

    public String toString() {
        return e().toString();
    }

    public b a(String str) {
        this.c = str;
        return this;
    }

    public JSONObject b() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ContextChain.TAG_PRODUCT, this.b);
            jSONObject.put("id", this.d);
            jSONObject.put("code", this.g);
            jSONObject.put(NotificationCompat.CATEGORY_MESSAGE, this.h);
        } catch (JSONException unused) {
        }
        return jSONObject;
    }

    public int c() {
        return this.f;
    }

    public boolean d() {
        return this.j;
    }

    public b a(int i2) {
        this.f = i2;
        return this;
    }

    public b a(int i2, String str) {
        this.j = i2 != -9999;
        this.g = i2;
        this.h = str;
        JSONObject jSONObject = new JSONObject();
        this.i = jSONObject;
        try {
            jSONObject.put("code", i2);
            this.i.put(NotificationCompat.CATEGORY_MESSAGE, str);
        } catch (JSONException unused) {
        }
        return this;
    }

    public int a() {
        return this.g;
    }
}
