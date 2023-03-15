package io.dcloud.sdk.base.dcloud;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.facebook.common.util.UriUtil;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import io.dcloud.h.a.e.f;
import io.dcloud.sdk.base.dcloud.ADHandler;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class a extends ADHandler {

    /* renamed from: io.dcloud.sdk.base.dcloud.a$a  reason: collision with other inner class name */
    class C0073a implements io.dcloud.sdk.base.dcloud.k.c {
        final /* synthetic */ ADHandler.g a;
        final /* synthetic */ Context b;
        final /* synthetic */ String c;

        C0073a(ADHandler.g gVar, Context context, String str) {
            this.a = gVar;
            this.b = context;
            this.c = str;
        }

        public Object onCallBack(int i, Object obj) {
            JSONObject optJSONObject = ((JSONObject) obj).optJSONObject("data");
            String optString = optJSONObject.optString("dstlink");
            String optString2 = optJSONObject.optString("clickid");
            String optString3 = this.a.b().optString("tid");
            try {
                this.a.c().put("click_id", optString2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Context context = this.b;
            ADHandler.g gVar = this.a;
            a.b(context, gVar, optString, gVar.h, optString3, this.c, gVar.c());
            if (this.a.d()) {
                return null;
            }
            io.dcloud.h.a.e.b.a(this.b);
            return null;
        }
    }

    class b implements Runnable {
        final /* synthetic */ ADHandler.g a;
        final /* synthetic */ Context b;
        final /* synthetic */ String c;

        b(ADHandler.g gVar, Context context, String str) {
            this.a = gVar;
            this.b = context;
            this.c = str;
        }

        public void run() {
            String optString = this.a.b().optString("url");
            String optString2 = this.a.b().optString("tid");
            Context context = this.b;
            ADHandler.g gVar = this.a;
            a.b(context, gVar, optString, gVar.h, optString2, this.c, gVar.c());
        }
    }

    class c implements Runnable {
        final /* synthetic */ JSONArray a;
        final /* synthetic */ ADHandler.g b;
        final /* synthetic */ Context c;

        c(JSONArray jSONArray, ADHandler.g gVar, Context context) {
            this.a = jSONArray;
            this.b = gVar;
            this.c = context;
        }

        public void run() {
            if (this.a != null) {
                for (int i = 0; i < this.a.length(); i++) {
                    try {
                        JSONObject optJSONObject = this.a.optJSONObject(i);
                        int optInt = optJSONObject.optInt("template_type");
                        JSONObject c2 = this.b.c();
                        boolean z = c2.has("ua") && c2.optString("ua").equalsIgnoreCase("webview");
                        c2.put("u-a", ADHandler.a("ua-webview"));
                        String a2 = ADHandler.a(optJSONObject.optString("url"), c2);
                        int optInt2 = optJSONObject.optInt("http_method");
                        String optString = optJSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME);
                        if (optInt != 1) {
                            a.b(a2, optString, optInt2, 2, false, (io.dcloud.sdk.base.dcloud.k.c) null, "clktrackers", z);
                        } else if (this.b.d()) {
                            c.a(this.c, a2);
                        } else {
                            io.dcloud.h.a.e.b.e(this.c, a2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class d implements Runnable {
        final /* synthetic */ boolean a;
        final /* synthetic */ int b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;
        final /* synthetic */ boolean e;
        final /* synthetic */ int f;
        final /* synthetic */ io.dcloud.sdk.base.dcloud.k.c g;

        d(boolean z, int i, String str, String str2, boolean z2, int i2, io.dcloud.sdk.base.dcloud.k.c cVar) {
            this.a = z;
            this.b = i;
            this.c = str;
            this.d = str2;
            this.e = z2;
            this.f = i2;
            this.g = cVar;
        }

        public void run() {
            HashMap hashMap;
            byte[] bArr = null;
            if (this.a) {
                hashMap = new HashMap();
                hashMap.put(IWebview.USER_AGENT, ADHandler.a("ua-webview"));
            } else {
                hashMap = null;
            }
            int i = this.b;
            if (i == 0) {
                try {
                    bArr = io.dcloud.h.a.e.d.a(this.c, (HashMap<String, String>) hashMap, true);
                } catch (Exception unused) {
                }
            } else if (i == 1) {
                bArr = io.dcloud.h.a.e.d.a(this.c, this.d, (HashMap<String, String>) hashMap);
            }
            if (this.e && bArr != null) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(bArr));
                    if (jSONObject.optInt("ret") != 0) {
                        String optString = jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE);
                        a.b("handleTrackers_wanka Runnable Error url=" + this.c + ";msg=" + optString);
                        int i2 = this.f;
                        if (i2 > 0) {
                            a.b(this.c, optString, this.b, i2, this.e, this.g, (String) null, this.a);
                        }
                        io.dcloud.h.a.e.d.a(this.c, (HashMap<String, String>) hashMap, true);
                        return;
                    }
                    io.dcloud.sdk.base.dcloud.k.c cVar = this.g;
                    if (cVar != null) {
                        cVar.onCallBack(1, jSONObject);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void b(Context context, ADHandler.g gVar, String str, String str2, String str3, String str4, JSONObject jSONObject) {
    }

    static void b(String str) {
        ADHandler.a("wanka", str);
    }

    static void d(Context context, ADHandler.g gVar, String str) {
        int optInt = gVar.b().optInt("template", 0);
        String optString = gVar.b().optString("action");
        if (optInt != 1) {
            JSONArray optJSONArray = gVar.f().optJSONArray("clktrackers");
            if (optJSONArray != null) {
                a(optJSONArray, gVar.c(), "clktrackers");
            }
            if (AbsoluteConst.SPNAME_DOWNLOAD.equals(optString)) {
                String optString2 = gVar.b().optString(AbsURIAdapter.BUNDLE);
                if (TextUtils.isEmpty(optString2) || !io.dcloud.h.a.e.b.a(context, optString2)) {
                    f.a().a(new b(gVar, context, str));
                    if (!gVar.d()) {
                        io.dcloud.h.a.e.b.a(context);
                    }
                } else if (!gVar.d()) {
                    try {
                        Intent parseUri = Intent.parseUri(gVar.b().optString("dplk"), 1);
                        parseUri.setFlags(268435456);
                        parseUri.setSelector((Intent) null);
                        parseUri.setComponent((ComponentName) null);
                        parseUri.addCategory("android.intent.category.BROWSABLE");
                        if (context.getPackageManager().resolveActivity(parseUri, 65536) != null) {
                            context.startActivity(parseUri);
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ADHandler.b(context, gVar, str);
            }
        } else if (!gVar.d()) {
            JSONArray optJSONArray2 = gVar.f().optJSONArray("clktrackers");
            if (AbsoluteConst.SPNAME_DOWNLOAD.equals(optString)) {
                a(optJSONArray2, gVar.c(), new C0073a(gVar, context, str), "clktrackers");
            } else if ("url".equals(optString)) {
                a(context, gVar, optJSONArray2, gVar.h, str);
            }
        }
    }

    static void e(Context context, ADHandler.g gVar, String str) {
        JSONArray optJSONArray = gVar.f().optJSONArray("dplktrackers");
        if (optJSONArray != null) {
            a(optJSONArray, gVar.c(), "dplktrackers");
        }
    }

    static void f(Context context, ADHandler.g gVar, String str) {
        JSONObject f = gVar.f();
        if (f != null) {
            a(f.optJSONArray("imptrackers"), gVar.c(), "imptrackers");
        }
    }

    /* access modifiers changed from: private */
    public static void b(String str, String str2, int i, int i2, boolean z, io.dcloud.sdk.base.dcloud.k.c cVar, String str3, boolean z2) {
        int i3 = i2 - 1;
        b("handleTrackers_wanka template = " + (z ? 1 : 0) + "; t_count=" + i3 + "; tagMsg " + str3 + ";  url=" + str);
        f.a().a(new d(z2, i, str, str2, z, i3, cVar));
    }

    private static void a(Context context, ADHandler.g gVar, JSONArray jSONArray, String str, String str2) {
        f.a().a(new c(jSONArray, gVar, context));
    }

    private static void a(JSONArray jSONArray, JSONObject jSONObject, String str) {
        a(jSONArray, jSONObject, (io.dcloud.sdk.base.dcloud.k.c) null, str);
    }

    private static void a(JSONArray jSONArray, JSONObject jSONObject, io.dcloud.sdk.base.dcloud.k.c cVar, String str) {
        JSONObject jSONObject2 = jSONObject;
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            int optInt = optJSONObject.optInt("template_type");
            boolean z = jSONObject2.has("ua") && jSONObject2.optString("ua").equalsIgnoreCase("webview");
            try {
                jSONObject2.put("u-a", ADHandler.a("ua-webview"));
            } catch (JSONException unused) {
            }
            b(ADHandler.a(optJSONObject.optString("url"), jSONObject2), optJSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME), optJSONObject.optInt("http_method"), 2, optInt == 1, cVar, str, z);
        }
    }
}
