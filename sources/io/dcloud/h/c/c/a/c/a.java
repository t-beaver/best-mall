package io.dcloud.h.c.c.a.c;

import org.json.JSONObject;

public class a {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;

    public void a(String str) {
        this.a = str;
    }

    public String b() {
        return this.d;
    }

    public String c() {
        return this.c;
    }

    public String d() {
        return this.b;
    }

    public String e() {
        return this.e;
    }

    public String f() {
        return this.a;
    }

    public void a(JSONObject jSONObject) {
        this.c = jSONObject.optString("er");
        this.d = jSONObject.optString("ec");
        JSONObject optJSONObject = jSONObject.optJSONObject("splash");
        if (optJSONObject != null) {
            this.b = optJSONObject.toString();
            this.e = optJSONObject.optString("tid");
            JSONObject optJSONObject2 = optJSONObject.optJSONObject(AbsoluteConst.XML_APP);
            if (optJSONObject2 != null) {
                this.f = optJSONObject2.optString("app_id");
            }
        }
    }

    public String a() {
        return this.f;
    }
}
