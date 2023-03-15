package io.dcloud.sdk.base.dcloud;

import android.content.Context;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.h.a.e.d;
import io.dcloud.h.a.e.f;
import io.dcloud.sdk.base.dcloud.ADHandler;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class e extends ADHandler {

    class a implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ HashMap b;

        a(String str, HashMap hashMap) {
            this.a = str;
            this.b = hashMap;
        }

        public void run() {
            try {
                d.a(this.a, (HashMap<String, String>) this.b, true);
            } catch (Exception unused) {
            }
        }
    }

    static void a(JSONArray jSONArray, String str, ADHandler.g gVar) {
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                String optString = jSONArray.optJSONObject(i).optString("url");
                JSONObject c = gVar.c();
                HashMap hashMap = null;
                if (c != null && c.has("ua") && c.optString("ua").equalsIgnoreCase("webview")) {
                    hashMap = new HashMap();
                    hashMap.put(IWebview.USER_AGENT, ADHandler.a("ua-webview"));
                }
                f.a().a(new a(optString, hashMap));
            }
        }
    }

    static void d(Context context, ADHandler.g gVar, String str) {
        JSONObject f = gVar.f();
        if (f != null) {
            a(f.optJSONArray("clktracker"), "clktracker", gVar);
        }
        ADHandler.b(context, gVar, str);
    }

    static void a(Context context, ADHandler.g gVar, String str, String str2) {
        JSONObject f = gVar.f();
        if (f != null) {
            a(f.optJSONArray(str2), str2, gVar);
        }
    }
}
