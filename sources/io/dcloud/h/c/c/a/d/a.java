package io.dcloud.h.c.c.a.d;

import io.dcloud.h.c.c.a.e.d;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends JSONObject {
    public a() {
    }

    /* renamed from: a */
    public a optJSONObject(String str) {
        Object opt = opt(str);
        if (opt == null) {
            return null;
        }
        try {
            return new a(opt.toString());
        } catch (JSONException unused) {
            return null;
        }
    }

    public JSONObject b(String str) {
        Object opt = super.opt(str);
        if (opt instanceof JSONObject) {
            return (JSONObject) opt;
        }
        return null;
    }

    public JSONObject c(String str) {
        return b(d.b(str));
    }

    public Object get(String str) throws JSONException {
        return super.get(d.b(str));
    }

    public boolean getBoolean(String str) throws JSONException {
        return super.getBoolean(str);
    }

    public double getDouble(String str) throws JSONException {
        return super.getDouble(str);
    }

    public int getInt(String str) throws JSONException {
        return super.getInt(str);
    }

    public JSONArray getJSONArray(String str) throws JSONException {
        return super.getJSONArray(str);
    }

    public JSONObject getJSONObject(String str) throws JSONException {
        Object obj = get(str);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw a(str, obj, "JSONObject");
    }

    public long getLong(String str) throws JSONException {
        return super.getLong(str);
    }

    public String getString(String str) throws JSONException {
        return super.getString(str);
    }

    public boolean has(String str) {
        return super.has(d.b(str));
    }

    public boolean isNull(String str) {
        return super.isNull(d.b(str));
    }

    public Object opt(String str) {
        return super.opt(d.b(str));
    }

    public boolean optBoolean(String str) {
        return super.optBoolean(str);
    }

    public double optDouble(String str) {
        return super.optDouble(str);
    }

    public int optInt(String str) {
        return super.optInt(str);
    }

    public JSONArray optJSONArray(String str) {
        return super.optJSONArray(str);
    }

    public long optLong(String str) {
        return super.optLong(str);
    }

    public String optString(String str) {
        return super.optString(str);
    }

    public JSONObject put(String str, boolean z) throws JSONException {
        return super.put(d.b(str), z);
    }

    public Object remove(String str) {
        return super.remove(d.b(str));
    }

    public JSONArray toJSONArray(JSONArray jSONArray) throws JSONException {
        return super.toJSONArray(jSONArray);
    }

    public a(Map map) {
        super(map);
    }

    public boolean optBoolean(String str, boolean z) {
        return super.optBoolean(str, z);
    }

    public double optDouble(String str, double d) {
        return super.optDouble(str, d);
    }

    public int optInt(String str, int i) {
        return super.optInt(str, i);
    }

    public long optLong(String str, long j) {
        return super.optLong(str, j);
    }

    public String optString(String str, String str2) {
        return super.optString(str, str2);
    }

    public JSONObject put(String str, double d) throws JSONException {
        return super.put(d.b(str), d);
    }

    public a(String str) throws JSONException {
        super(str);
    }

    public JSONObject put(String str, int i) throws JSONException {
        return super.put(d.b(str), i);
    }

    public a(JSONObject jSONObject, String[] strArr) throws JSONException {
        super(jSONObject, strArr);
    }

    public JSONObject put(String str, long j) throws JSONException {
        return super.put(d.b(str), j);
    }

    public static JSONException a(Object obj, Object obj2, String str) throws JSONException {
        if (obj2 == null) {
            throw new JSONException("Value at " + obj + " is null.");
        }
        throw new JSONException("Value " + obj2 + " at " + obj + " of type " + obj2.getClass().getName() + " cannot be converted to " + str);
    }

    public JSONObject put(String str, Object obj) throws JSONException {
        return super.put(d.b(str), obj);
    }
}
