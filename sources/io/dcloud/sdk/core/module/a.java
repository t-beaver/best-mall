package io.dcloud.sdk.core.module;

import android.app.Activity;
import androidx.core.app.NotificationCompat;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.Const;
import io.dcloud.sdk.poly.base.utils.e;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class a extends DCBaseAOL {
    private long n = 0;
    private int o = -1;
    private JSONObject p;
    private String q = "";
    private boolean r = true;
    private long s = 0;

    public a(DCloudAdSlot dCloudAdSlot, Activity activity) {
        super(dCloudAdSlot, activity);
    }

    /* access modifiers changed from: protected */
    public void a(int i) {
        this.o = i;
    }

    public void b(int i, String str) {
        this.r = i != -9999;
        e.b("uniAD", h() + ":" + getType() + ":" + i + ":" + str + ";id:" + getSlotId());
        this.o = 0;
        this.n = System.currentTimeMillis() - this.s;
        JSONObject jSONObject = new JSONObject();
        this.p = jSONObject;
        try {
            jSONObject.put("code", i);
            this.p.put(NotificationCompat.CATEGORY_MESSAGE, str);
        } catch (JSONException unused) {
        }
        if (getType().equals(Const.TYPE_GDT) && i == 6000) {
            this.q = getType() + ":" + i + Operators.BRACKET_START_STR + str + Operators.BRACKET_END_STR;
        } else if (!getType().equals(Const.TYPE_BD) || i != -1) {
            this.q = getType() + ":" + i;
        } else {
            this.q = getType() + ":" + str;
        }
    }

    public int getAdStatus() {
        return this.o;
    }

    /* access modifiers changed from: protected */
    public final long l() {
        return this.n;
    }

    /* access modifiers changed from: protected */
    public void m() {
        this.n = System.currentTimeMillis() - this.s;
    }

    public void n() {
        e.b("uniAD", h() + ":" + getType() + ":success;id:" + getSlotId());
        this.o = 1;
        this.n = System.currentTimeMillis() - this.s;
    }

    public void startLoadTime() {
        this.s = System.currentTimeMillis();
    }
}
