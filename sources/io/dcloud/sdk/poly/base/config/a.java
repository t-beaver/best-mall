package io.dcloud.sdk.poly.base.config;

import org.json.JSONArray;
import org.json.JSONObject;

public class a {
    private String a;
    private String b;
    private JSONObject c;
    private JSONArray d = new JSONArray();
    private io.dcloud.h.c.c.a.c.a e;
    private String f;

    public String a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public void c(String str) {
        this.f = str;
    }

    public io.dcloud.h.c.c.a.c.a d() {
        return this.e;
    }

    public void a(String str) {
        this.a = str;
    }

    public void b(String str) {
        this.b = str;
    }

    public String c() {
        return this.f;
    }

    public void a(io.dcloud.h.c.c.a.c.a aVar) {
        this.e = aVar;
    }

    public boolean a(JSONObject jSONObject) {
        if (jSONObject == null || jSONObject.length() <= 0) {
            return false;
        }
        this.a = jSONObject.optString("appid");
        this.d = jSONObject.optJSONArray("appidh");
        this.b = jSONObject.optString("appkey");
        JSONObject optJSONObject = jSONObject.optJSONObject("ext");
        this.c = optJSONObject;
        if (optJSONObject == null) {
            return true;
        }
        if (this.e == null) {
            this.e = new io.dcloud.h.c.c.a.c.a();
        }
        this.e.a(this.f);
        this.e.a(this.c);
        return true;
    }
}
