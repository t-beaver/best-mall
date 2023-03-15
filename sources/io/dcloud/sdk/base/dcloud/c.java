package io.dcloud.sdk.base.dcloud;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.feature.gg.dcloud.ADSim;
import io.dcloud.h.a.e.f;
import io.dcloud.h.c.c.a.b.b;
import io.dcloud.sdk.base.dcloud.ADHandler;
import io.dcloud.sdk.base.dcloud.k.a;
import java.io.File;
import java.util.HashMap;
import org.json.JSONObject;

public class c {
    ADHandler.g a = null;
    private Context b;
    Handler c = null;
    JSONObject d;

    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 10000) {
                c.this.c();
            }
        }
    }

    class b implements Runnable {
        b() {
        }

        public void run() {
            c cVar = c.this;
            cVar.a = cVar.a(cVar.d);
            ADHandler.g gVar = c.this.a;
            if (gVar != null) {
                String optString = gVar.c() != null ? c.this.a.c().optString("ua") : "";
                c cVar2 = c.this;
                cVar2.a(cVar2.a.g, optString);
            }
        }
    }

    /* renamed from: io.dcloud.sdk.base.dcloud.c$c  reason: collision with other inner class name */
    class C0074c implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ String b;

        C0074c(String str, String str2) {
            this.a = str;
            this.b = str2;
        }

        public void run() {
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(this.a) && this.a.equalsIgnoreCase("webview")) {
                hashMap.put(IWebview.USER_AGENT, ADHandler.a("ua-webview"));
            }
            try {
                if (io.dcloud.h.a.e.d.a(this.b, (HashMap<String, String>) hashMap, true) != null) {
                    c.this.c.sendEmptyMessage(ADSim.INTISPLSH);
                }
            } catch (Exception unused) {
            }
        }
    }

    class d implements Runnable {
        d() {
        }

        public void run() {
            c.this.a();
        }
    }

    class e implements a.C0075a {
        final /* synthetic */ io.dcloud.sdk.base.dcloud.k.d a;
        final /* synthetic */ Context b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;
        final /* synthetic */ String e;
        final /* synthetic */ String f;
        final /* synthetic */ io.dcloud.sdk.base.dcloud.k.b g;

        e(io.dcloud.sdk.base.dcloud.k.d dVar, Context context, String str, String str2, String str3, String str4, io.dcloud.sdk.base.dcloud.k.b bVar) {
            this.a = dVar;
            this.b = context;
            this.c = str;
            this.d = str2;
            this.e = str3;
            this.f = str4;
            this.g = bVar;
        }

        public void a(io.dcloud.sdk.base.dcloud.k.a aVar) {
            c.b(this.b, this.c, this.d, this.e, this.f, 32);
            File file = new File(aVar.b());
            if (file.exists()) {
                file.delete();
            }
            this.g.b(aVar);
        }

        public void b(io.dcloud.sdk.base.dcloud.k.a aVar) {
            this.a.onCallBack(0, aVar.a(), (Object) null);
            c.b(this.b, this.c, this.d, this.e, this.f, 30);
            File file = new File(aVar.b());
            if (file.exists()) {
                file.delete();
            }
            this.g.b(aVar);
        }
    }

    public c(Context context, JSONObject jSONObject) {
        this.d = jSONObject;
        this.b = context;
        this.c = new a(context.getMainLooper());
    }

    /* access modifiers changed from: private */
    public void c() {
        ADHandler.a("shutao", "ADSim---view");
        Context context = this.b;
        ADHandler.c(context, this.a, ADHandler.a(context, "adid"));
        b();
    }

    public void d() {
        this.c.postDelayed(new b(), (long) a(250, 350));
    }

    private void b() {
        if (this.a.d()) {
            this.c.postDelayed(new d(), (long) a(800, 2000));
        }
    }

    /* access modifiers changed from: private */
    public void a(String str, String str2) {
        f.a().a(new C0074c(str2, str));
    }

    /* access modifiers changed from: private */
    public static void b(Context context, String str, String str2, String str3, String str4, int i) {
        f.a().a(new Runnable(context, str, str2, str3, i, str4) {
            public final /* synthetic */ Context f$0;
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ int f$4;
            public final /* synthetic */ String f$5;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            public final void run() {
                b.a(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
    }

    /* access modifiers changed from: private */
    public ADHandler.g a(JSONObject jSONObject) {
        ADHandler.g gVar = new ADHandler.g();
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        if (optJSONObject != null) {
            gVar.c = jSONObject.optString("provider");
            gVar.d = jSONObject;
            gVar.i = jSONObject.optInt("es", 0);
            gVar.j = jSONObject.optInt("ec", 0);
            gVar.g = optJSONObject.optString("src");
            gVar.e = "000";
            gVar.h = ADHandler.a("appid");
        }
        return gVar;
    }

    /* access modifiers changed from: private */
    public void a() {
        ADHandler.a("shutao", "ADSim---click");
        Context context = this.b;
        ADHandler.a(context, this.a, ADHandler.a(context, "adid"));
    }

    public static int a(int i, int i2) {
        double d2 = (double) i;
        double random = Math.random();
        double d3 = (double) ((i2 - i) + 1);
        Double.isNaN(d3);
        Double.isNaN(d2);
        return (int) (d2 + (random * d3));
    }

    public static void a(Context context, String str) {
        ADHandler.a("shutao", "ADSim---openUrl");
        new d(context).a(str);
    }

    public static void a(Context context, String str, String str2, String str3, String str4, String str5, io.dcloud.sdk.base.dcloud.k.d dVar, String str6, String str7) {
        Context context2 = context;
        String str8 = str4;
        b(context, str, str2, str3, str7, 29);
        io.dcloud.sdk.base.dcloud.k.b a2 = io.dcloud.sdk.base.dcloud.k.b.a(context.getApplicationContext());
        String absolutePath = context.getExternalFilesDir((String) null).getAbsolutePath();
        StringBuilder sb = new StringBuilder();
        sb.append(absolutePath);
        String str9 = "/";
        if (absolutePath.endsWith(str9)) {
            str9 = "";
        }
        sb.append(str9);
        String str10 = sb.toString() + "/Download/" + "ADSIM-INFO.io";
        File file = new File(str10);
        if (file.exists()) {
            file.delete();
        }
        for (io.dcloud.sdk.base.dcloud.k.a next : a2.a()) {
            if (next.c().equals(str4)) {
                a2.b(next);
            }
        }
        io.dcloud.sdk.base.dcloud.k.a aVar = new io.dcloud.sdk.base.dcloud.k.a();
        aVar.a(context, str4, str10);
        aVar.a(new e(dVar, context, str, str2, str3, str7, a2));
        a2.a(aVar);
    }
}
