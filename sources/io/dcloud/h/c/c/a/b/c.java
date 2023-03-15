package io.dcloud.h.c.c.a.b;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.h.a.d.b.i;
import io.dcloud.h.a.e.d;
import io.dcloud.h.a.e.f;
import io.dcloud.h.c.c.a.e.b;
import io.dcloud.h.c.c.b.a;
import io.dcloud.h.c.c.b.c;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class c implements io.dcloud.h.c.c.b.c {

    class a implements a.b {
        String[] a = new String[1];
        final /* synthetic */ c.a[] b;
        final /* synthetic */ String c;
        final /* synthetic */ HashMap d;
        final /* synthetic */ Context e;

        a(c.a[] aVarArr, String str, HashMap hashMap, Context context) {
            this.b = aVarArr;
            this.c = str;
            this.d = hashMap;
            this.e = context;
        }

        public void a(a.C0057a aVar) {
        }

        public boolean b(a.C0057a aVar) {
            byte[] a2 = d.a(aVar.b(), this.c, (HashMap<String, String>) this.d, this.a);
            if (a2 == null) {
                return false;
            }
            try {
                io.dcloud.h.c.c.a.d.a aVar2 = new io.dcloud.h.c.c.a.d.a(new String(a2));
                if (aVar2.has("7C61656D")) {
                    c.this.a(aVar2, this.e);
                }
                if (aVar2.has("6C697C69")) {
                    aVar2.put("6C697C69", (Object) new io.dcloud.h.c.c.a.d.a(new String(io.dcloud.h.a.d.b.a.a(Base64.decode(aVar2.getString("6C697C69"), 2), b.b(), b.a()))));
                    for (c.a a3 : this.b) {
                        a3.a(aVar2);
                    }
                    return true;
                }
                for (c.a a4 : this.b) {
                    a4.a(-1, AdErrorUtil.getErrorMsg(-5007));
                }
                return true;
            } catch (Exception e2) {
                e2.printStackTrace();
                for (c.a a5 : this.b) {
                    a5.a(-1, AdErrorUtil.getErrorMsg(-5007));
                }
                return true;
            }
        }

        public void onNoOnePicked() {
            for (c.a a2 : this.b) {
                a2.a(-1, this.a[0]);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void b(Context context, int i, c.a[] aVarArr) {
        String str;
        try {
            str = URLEncoder.encode(Base64.encodeToString(io.dcloud.h.a.d.b.a.b(i.a(new io.dcloud.h.c.c.a.d.a((Map) e.a(context)).toString()), b.b(), b.a()), 2), "utf-8");
        } catch (UnsupportedEncodingException unused) {
            str = null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put(NetWork.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        f.a().a(new Runnable(context, i, aVarArr, "edata=" + str, hashMap) {
            public final /* synthetic */ Context f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ c.a[] f$3;
            public final /* synthetic */ String f$4;
            public final /* synthetic */ HashMap f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            public final void run() {
                c.this.a(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
    }

    public void a(Context context, int i, c.a... aVarArr) {
        if (io.dcloud.h.c.a.d().b() == null || TextUtils.isEmpty(io.dcloud.h.c.a.d().b().getAdId()) || TextUtils.isEmpty(io.dcloud.h.c.a.d().b().getAppId())) {
            for (c.a a2 : aVarArr) {
                a2.a(-5016, AdErrorUtil.getErrorMsg(-5016));
            }
            return;
        }
        MainHandlerUtil.getMainHandler().post(new Runnable(context, i, aVarArr) {
            public final /* synthetic */ Context f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ c.a[] f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                c.this.b(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void a(Context context, int i, c.a[] aVarArr, String str, HashMap hashMap) {
        io.dcloud.h.c.c.b.a.a().a(context, i != 1 ? io.dcloud.h.c.c.b.b.e : io.dcloud.h.c.c.b.b.d, i != 1 ? "ThirdConfig" : "Splash", new a(aVarArr, str, hashMap, context));
    }

    /* access modifiers changed from: protected */
    public void a(io.dcloud.h.c.c.a.d.a aVar, Context context) {
        long j;
        try {
            j = Long.parseLong(io.dcloud.sdk.poly.base.utils.a.c(context));
        } catch (Exception unused) {
            j = 0;
        }
        long optLong = aVar.optLong("7C61656D");
        if (j > 0 && optLong > 0 && !io.dcloud.sdk.poly.base.utils.a.a(j * 1000, 1000 * optLong)) {
            io.dcloud.sdk.poly.base.utils.a.a(context);
        }
        io.dcloud.sdk.poly.base.utils.a.a(context, String.valueOf(optLong));
    }
}
