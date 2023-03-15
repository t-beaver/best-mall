package io.dcloud.h.a.d;

import android.content.Context;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.h.a.c.a;
import io.dcloud.h.a.d.b.g;
import io.dcloud.h.a.e.d;
import io.dcloud.sdk.base.entry.AdData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends io.dcloud.h.a.c.a {
    ExecutorService e;
    /* access modifiers changed from: private */
    public String f;
    /* access modifiers changed from: private */
    public String g;
    /* access modifiers changed from: private */
    public String h;
    private String i;
    private String j;
    private JSONObject k;
    /* access modifiers changed from: private */
    public HashMap<String, String> l;
    /* access modifiers changed from: private */
    public String m;
    /* access modifiers changed from: private */
    public String n;

    /* renamed from: io.dcloud.h.a.d.a$a  reason: collision with other inner class name */
    class C0048a implements Runnable {
        final /* synthetic */ String a;

        C0048a(String str) {
            this.a = str;
        }

        /* renamed from: io.dcloud.h.a.d.a$a$a  reason: collision with other inner class name */
        class C0049a implements AdData.e {
            final /* synthetic */ AdData a;

            C0049a(AdData adData) {
                this.a = adData;
            }

            public void a() {
                AdData unused = a.this.d = this.a;
                a.this.a();
            }

            public void a(int i, String str) {
                try {
                    for (io.dcloud.sdk.base.entry.a next : g.a().b(a.this.b()).values()) {
                        if (!next.b()) {
                            AdData adData = new AdData();
                            next.a(a.this.b(), adData);
                            if (adData.n() && adData.m() && adData.c().equals(a.this.f) && adData.j().equals(a.this.h) && a.this.d == null) {
                                AdData unused = a.this.d = adData;
                                a.this.a();
                                return;
                            }
                        } else {
                            g.a().b(a.this.b(), next.a());
                        }
                    }
                } catch (Exception unused2) {
                }
                a.this.a(i, str);
            }
        }

        public void run() {
            String[] strArr = new String[1];
            byte[] a2 = d.a(a.this.g, this.a, (HashMap<String, String>) a.this.l, strArr);
            if (a2 != null) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(a2));
                    AdData adData = new AdData();
                    adData.c(a.this.b());
                    jSONObject.put("appid", a.this.f);
                    jSONObject.put("adpid", a.this.c);
                    jSONObject.put("tid", a.this.h);
                    jSONObject.put("did", a.this.m);
                    jSONObject.put("adid", a.this.n);
                    adData.a(jSONObject, new C0049a(adData), true);
                } catch (JSONException e) {
                    a.this.a(60006, e.getMessage());
                }
            } else {
                a aVar = a.this;
                StringBuilder sb = new StringBuilder();
                sb.append("网络请求失败：");
                sb.append(strArr[0] == null ? "data null" : strArr[0]);
                aVar.a(60003, sb.toString());
            }
        }
    }

    public a(a.c cVar, Context context, String str, String str2, String str3, String str4) {
        this(cVar, context, str2);
        this.i = str3;
        this.j = str4;
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.m = jSONObject.optString("did");
            this.n = jSONObject.optString("adid");
            this.g = jSONObject.optString("url");
            JSONObject optJSONObject = jSONObject.optJSONObject(AbsoluteConst.XML_APP);
            this.k = optJSONObject;
            this.f = optJSONObject.optString("app_id");
            this.h = jSONObject.optString("tid");
            JSONObject optJSONObject2 = jSONObject.optJSONObject(WXBasicComponentType.HEADER);
            if (optJSONObject2 != null && optJSONObject2.length() > 0) {
                this.l = new HashMap<>();
                Iterator<String> keys = optJSONObject2.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    this.l.put(next, optJSONObject2.optString(next));
                }
            }
        } catch (JSONException unused) {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:22|23|24|25|26|27|28) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0079 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void c() {
        /*
            r4 = this;
            java.lang.String r0 = r4.g
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0012
            r0 = 60001(0xea61, float:8.408E-41)
            java.lang.String r1 = "广告请求地址出错"
            r4.a(r0, r1)
            return
        L_0x0012:
            org.json.JSONObject r0 = r4.k
            if (r0 == 0) goto L_0x00b2
            int r0 = r0.length()
            if (r0 > 0) goto L_0x001e
            goto L_0x00b2
        L_0x001e:
            java.lang.String r0 = r4.c
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x002f
            org.json.JSONObject r0 = r4.k     // Catch:{ JSONException -> 0x002f }
            java.lang.String r1 = "adp_id"
            java.lang.String r2 = r4.c     // Catch:{ JSONException -> 0x002f }
            r0.put(r1, r2)     // Catch:{ JSONException -> 0x002f }
        L_0x002f:
            android.content.Context r0 = r4.b()
            org.json.JSONObject r0 = io.dcloud.h.a.d.b.h.a(r0)
            java.lang.String r1 = "app"
            org.json.JSONObject r2 = r4.k     // Catch:{ JSONException -> 0x003f }
            r0.put(r1, r2)     // Catch:{ JSONException -> 0x003f }
            goto L_0x0040
        L_0x003f:
        L_0x0040:
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = r4.i
            java.lang.String r2 = "1"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x008b
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r2 = r4.j     // Catch:{ Exception -> 0x00a7 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r2 = "method"
            r1.optString(r2)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r2 = "key"
            java.lang.String r2 = r1.optString(r2)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r3 = "iv"
            java.lang.String r1 = r1.optString(r3)     // Catch:{ Exception -> 0x00a7 }
            byte[] r3 = io.dcloud.h.a.d.b.i.a(r0)     // Catch:{ Exception -> 0x00a7 }
            byte[] r1 = io.dcloud.h.a.d.b.a.b(r3, r2, r1)     // Catch:{ Exception -> 0x00a7 }
            r2 = 2
            java.lang.String r1 = android.util.Base64.encodeToString(r1, r2)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r2 = "utf-8"
            java.lang.String r1 = java.net.URLEncoder.encode(r1, r2)     // Catch:{ UnsupportedEncodingException -> 0x0079 }
        L_0x0079:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a7 }
            r2.<init>()     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r3 = "edata="
            r2.append(r3)     // Catch:{ Exception -> 0x00a7 }
            r2.append(r1)     // Catch:{ Exception -> 0x00a7 }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x00a7 }
            goto L_0x00a7
        L_0x008b:
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r4.l
            if (r1 != 0) goto L_0x0096
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            r4.l = r1
        L_0x0096:
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r4.l
            java.lang.String r2 = "Content-Type"
            boolean r1 = r1.containsKey(r2)
            if (r1 != 0) goto L_0x00a7
            java.util.HashMap<java.lang.String, java.lang.String> r1 = r4.l
            java.lang.String r3 = "application/json"
            r1.put(r2, r3)
        L_0x00a7:
            java.util.concurrent.ExecutorService r1 = r4.e
            io.dcloud.h.a.d.a$a r2 = new io.dcloud.h.a.d.a$a
            r2.<init>(r0)
            r1.execute(r2)
            return
        L_0x00b2:
            r0 = 60002(0xea62, float:8.4081E-41)
            java.lang.String r1 = "广告配置异常"
            r4.a(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.a.d.a.c():void");
    }

    private a(a.c cVar, Context context, String str) {
        super(cVar, context, str);
        this.e = Executors.newSingleThreadExecutor();
        this.f = "";
        this.g = "";
        this.i = "";
        this.j = "";
    }
}
