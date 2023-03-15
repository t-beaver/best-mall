package io.dcloud.h.c.c.d;

import android.app.Activity;
import android.content.Context;
import io.dcloud.h.c.c.a.a;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.poly.base.config.DCloudSlotConfig;
import org.json.JSONObject;

public abstract class c extends b implements Runnable {
    protected DCloudAdSlot b;
    /* access modifiers changed from: protected */
    public final Activity c;
    protected int d;
    private a.C0055a e;

    class a extends a.C0055a {
        a(String str) {
            super(str);
        }

        public void a(JSONObject jSONObject) {
            c.this.a(jSONObject);
        }

        public void a(int i, String str) {
            c.this.b(i, str);
        }
    }

    public c(Activity activity) {
        this.c = activity;
    }

    private void e() {
        if (this.e == null) {
            this.e = new a(this.b.getAdpid());
        }
        io.dcloud.h.c.c.a.a.a().a((Context) this.c, d(), this.e);
    }

    /* access modifiers changed from: protected */
    public abstract void a(int i, String str);

    /* access modifiers changed from: protected */
    public final void a(DCloudAdSlot dCloudAdSlot) {
        this.b = dCloudAdSlot;
        dCloudAdSlot.setType(this.d);
    }

    /* access modifiers changed from: protected */
    public abstract void a(DCloudSlotConfig dCloudSlotConfig);

    /* access modifiers changed from: protected */
    public void b(int i, String str) {
        a(i, str);
    }

    /* access modifiers changed from: protected */
    public int d() {
        return 2;
    }

    public void run() {
        e();
    }

    /* access modifiers changed from: protected */
    public void a(JSONObject jSONObject) {
        if (jSONObject == null || jSONObject.length() <= 0) {
            a(-5001, AdErrorUtil.getErrorMsg(-5001));
        } else {
            a(new DCloudSlotConfig().a(jSONObject));
        }
    }

    /* access modifiers changed from: protected */
    public void a(a.C0055a aVar) {
        this.e = aVar;
    }
}
