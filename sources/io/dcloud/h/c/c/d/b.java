package io.dcloud.h.c.c.d;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import io.dcloud.h.c.a;
import io.dcloud.h.c.c.a.b.b;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.poly.base.utils.d;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.HashMap;
import org.json.JSONArray;

public abstract class b {
    private boolean a = false;

    /* access modifiers changed from: protected */
    public abstract Activity a();

    /* access modifiers changed from: protected */
    public void a(int i, String str, String str2, String str3, JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.length() != 0 && i != 14) {
            this.a = true;
            if (c()) {
                HashMap hashMap = new HashMap();
                hashMap.put("type", i != 1 ? i != 4 ? i != 7 ? i != 15 ? i != 9 ? i != 10 ? "" : "draw_flow" : "rewarded" : "interstitial" : "full_screen_video" : "template" : "splash");
                hashMap.put("adpid", str);
                hashMap.put("ord", str2);
                if (!TextUtils.isEmpty(str3)) {
                    hashMap.put("ext", str3);
                }
                hashMap.put("rsp", jSONArray);
                hashMap.put("tid", 60);
                d.a().post(new Runnable(str, hashMap) {
                    public final /* synthetic */ String f$1;
                    public final /* synthetic */ HashMap f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    public final void run() {
                        b.this.a(this.f$1, this.f$2);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public void b(Activity activity, DCBaseAOL dCBaseAOL) {
        e.a("on ad show");
        a(activity, 40, dCBaseAOL);
    }

    /* access modifiers changed from: protected */
    public abstract boolean c();

    /* access modifiers changed from: private */
    public /* synthetic */ void a(String str, HashMap hashMap) {
        io.dcloud.h.c.c.a.b.b.a((Context) a(), a.d().b().getAppId(), a.d().b().getAdId(), 60, str, (HashMap<String, Object>) hashMap);
    }

    private void a(Context context, int i, DCBaseAOL dCBaseAOL) {
        if (i == 40) {
            io.dcloud.sdk.poly.base.utils.a.a(context, dCBaseAOL.h(), dCBaseAOL.getType());
        }
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(dCBaseAOL.i())) {
            hashMap.put("ext", dCBaseAOL.i());
        }
        d.a().post(new Runnable(context, dCBaseAOL, i, hashMap) {
            public final /* synthetic */ Context f$0;
            public final /* synthetic */ DCBaseAOL f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ HashMap f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                b.a(this.f$0, this.f$1.e(), this.f$1.getSlotId(), this.f$1.getTid(), a.d().b().getAppId(), a.d().b().getAdId(), this.f$2, this.f$1.h(), this.f$3);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void a(Activity activity, DCBaseAOL dCBaseAOL) {
        e.a("on ad click");
        a(activity, 41, dCBaseAOL);
    }
}
