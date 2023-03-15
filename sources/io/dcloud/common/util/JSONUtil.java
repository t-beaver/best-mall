package io.dcloud.common.util;

import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.adapter.util.Logger;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    public static JSONObject combinJSONObject(JSONObject jSONObject, JSONObject jSONObject2) {
        Iterator<String> keys;
        if (jSONObject == null) {
            return jSONObject2;
        }
        if (!(jSONObject == jSONObject2 || jSONObject2 == null || (keys = jSONObject2.keys()) == null)) {
            while (keys.hasNext()) {
                try {
                    String valueOf = String.valueOf(keys.next());
                    Object opt = jSONObject.opt(valueOf);
                    Object opt2 = jSONObject2.opt(valueOf);
                    if (opt2 != null) {
                        if (opt == null) {
                            jSONObject.putOpt(valueOf, opt2);
                        } else if (!(opt2 instanceof JSONObject)) {
                            jSONObject.putOpt(valueOf, opt2);
                        } else if (opt instanceof JSONObject) {
                            combinJSONObject((JSONObject) opt, (JSONObject) opt2);
                        } else {
                            jSONObject.putOpt(valueOf, opt2);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONObject;
    }

    public static JSONObject combinSitemapHtmlJSONObject(JSONObject jSONObject, JSONObject jSONObject2) {
        Iterator<String> keys;
        if (jSONObject == null) {
            return jSONObject2;
        }
        if (!(jSONObject == jSONObject2 || (keys = jSONObject2.keys()) == null)) {
            while (keys.hasNext()) {
                try {
                    String valueOf = String.valueOf(keys.next());
                    Object opt = jSONObject.opt(valueOf);
                    Object opt2 = jSONObject2.opt(valueOf);
                    if (opt2 != null) {
                        if (opt == null) {
                            jSONObject.putOpt(valueOf, opt2);
                        } else if (opt2 instanceof JSONObject) {
                            if (opt instanceof JSONObject) {
                                combinSitemapHtmlJSONObject((JSONObject) opt, (JSONObject) opt2);
                            } else {
                                jSONObject.putOpt(valueOf, opt2);
                            }
                        } else if (!(opt instanceof JSONObject) || !(opt2 instanceof Boolean)) {
                            jSONObject.putOpt(valueOf, opt2);
                        } else {
                            jSONObject.remove(valueOf);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONObject;
    }

    public static JSONArray createJSONArray(String str) {
        if (!str.startsWith(Operators.ARRAY_START_STR)) {
            str = Operators.ARRAY_START_STR + str;
        }
        if (!str.endsWith(Operators.ARRAY_END_STR)) {
            str = str + Operators.ARRAY_END_STR;
        }
        try {
            return new JSONArray(str);
        } catch (JSONException unused) {
            return null;
        }
    }

    public static JSONObject createJSONObject(String str) {
        if (str == null) {
            return null;
        }
        try {
            return new JSONObject(str);
        } catch (Exception unused) {
            Logger.d("jsonutil", "JSONException pJson=" + str);
            return null;
        }
    }

    public static boolean getBoolean(JSONObject jSONObject, String str) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str)) {
                    return jSONObject.getBoolean(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getInt(JSONObject jSONObject, String str) {
        try {
            Integer valueOf = Integer.valueOf(jSONObject.getInt(str));
            if (valueOf instanceof Integer) {
                return valueOf.intValue();
            }
            return 0;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static JSONArray getJSONArray(JSONArray jSONArray, int i) {
        try {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject jSONObject, String str) {
        try {
            Object opt = jSONObject.opt(str);
            if (opt instanceof JSONObject) {
                return (JSONObject) opt;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static long getLong(JSONObject jSONObject, String str) {
        if (jSONObject != null) {
            try {
                if (jSONObject.has(str)) {
                    return jSONObject.getLong(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static String getString(JSONObject jSONObject, String str) {
        if (jSONObject == null) {
            return null;
        }
        try {
            return jSONObject.getString(str);
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean isNull(JSONObject jSONObject, String str) {
        if (jSONObject == null) {
            return true;
        }
        try {
            return jSONObject.isNull(str);
        } catch (Exception unused) {
            return true;
        }
    }

    public static String toJSONableString(String str) {
        return str != null ? JSONObject.quote(str) : "''";
    }

    public static HashMap<String, String> toMap(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        Iterator<String> keys = jSONObject.keys();
        HashMap<String, String> hashMap = new HashMap<>(jSONObject.length());
        while (keys.hasNext()) {
            String next = keys.next();
            hashMap.put(next, jSONObject.optString(next));
        }
        return hashMap;
    }

    public static String getString(JSONArray jSONArray, int i) {
        try {
            Object obj = jSONArray.get(i);
            if (!PdrUtil.isEmpty(obj)) {
                return String.valueOf(obj);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONArray getJSONArray(JSONObject jSONObject, String str) {
        try {
            Object obj = jSONObject.get(str);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONArray jSONArray, int i) {
        try {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONObject) {
                return (JSONObject) obj;
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }
}
