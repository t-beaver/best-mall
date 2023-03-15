package com.alibaba.android.bindingx.core.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import com.alibaba.android.bindingx.core.LogProxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Utils {
    public static float normalizeRotation(float f) {
        float f2 = f % 360.0f;
        return f2 >= 0.0f ? (f2 < 0.0f || f2 > 180.0f) ? (f2 % 180.0f) - 180.0f : f2 : (f2 <= -180.0f || f2 >= 0.0f) ? (f2 % 180.0f) + 180.0f : f2;
    }

    private Utils() {
    }

    public static Map<String, Object> toMap(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return Collections.emptyMap();
        }
        HashMap hashMap = new HashMap();
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            hashMap.put(next, fromJson(jSONObject.get(next)));
        }
        return hashMap;
    }

    public static List toList(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(fromJson(jSONArray.get(i)));
        }
        return arrayList;
    }

    private static Object fromJson(Object obj) throws JSONException {
        if (obj == JSONObject.NULL) {
            return null;
        }
        if (obj instanceof JSONObject) {
            return toMap((JSONObject) obj);
        }
        return obj instanceof JSONArray ? toList((JSONArray) obj) : obj;
    }

    public static String getStringValue(Map<String, Object> map, String str) {
        Object obj = map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

    public static List<Map<String, Object>> getRuntimeProps(Map<String, Object> map) {
        Object obj = map.get(BindingXConstants.KEY_RUNTIME_PROPS);
        if (obj == null) {
            return null;
        }
        try {
            return (List) obj;
        } catch (Exception unused) {
            return null;
        }
    }

    public static Map<String, ExpressionPair> getCustomInterceptors(Map<String, Object> map) {
        ExpressionPair expressionPair;
        Object obj = map.get(BindingXConstants.KEY_INTERCEPTORS);
        if (obj == null || !(obj instanceof Map)) {
            return null;
        }
        HashMap hashMap = new HashMap(8);
        for (Map.Entry entry : ((Map) obj).entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if ((key instanceof String) && (value instanceof Map)) {
                try {
                    expressionPair = getExpressionPair((Map) value, BindingXConstants.KEY_EXPRESSION);
                } catch (Exception unused) {
                    expressionPair = null;
                }
                if (expressionPair != null) {
                    hashMap.put((String) key, expressionPair);
                }
            }
        }
        return hashMap;
    }

    public static ExpressionPair getExpressionPair(Map<String, Object> map, String str) {
        JSONObject jSONObject;
        Object obj = map.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return ExpressionPair.create((String) null, (String) obj);
        }
        if (!(obj instanceof Map)) {
            return null;
        }
        try {
            jSONObject = new JSONObject((Map) obj);
        } catch (Throwable th) {
            LogProxy.e("unexpected json parse error.", th);
            jSONObject = null;
        }
        if (jSONObject == null) {
            return ExpressionPair.create((String) null, (String) null);
        }
        String optString = jSONObject.optString("origin", (String) null);
        String optString2 = jSONObject.optString(BindingXConstants.KEY_TRANSFORMED, (String) null);
        if (!TextUtils.isEmpty(optString) || !TextUtils.isEmpty(optString2)) {
            return ExpressionPair.create(optString, optString2);
        }
        return ExpressionPair.create((String) null, (String) null);
    }

    @SafeVarargs
    public static <E> HashSet<E> newHashSet(E... eArr) {
        HashSet<E> hashSet = new HashSet<>(eArr.length);
        Collections.addAll(hashSet, eArr);
        return hashSet;
    }

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... eArr) {
        ArrayList<E> arrayList = new ArrayList<>(eArr.length);
        Collections.addAll(arrayList, eArr);
        return arrayList;
    }

    public static int normalizedPerspectiveValue(Context context, int i) {
        return (int) (context.getApplicationContext().getResources().getDisplayMetrics().density * ((float) i) * 5.0f);
    }

    public static Pair<Float, Float> parseTransformOrigin(String str, View view) {
        int indexOf;
        float f;
        int height;
        int width;
        if (!TextUtils.isEmpty(str) && (indexOf = str.indexOf(32)) != -1) {
            int i = indexOf;
            while (i < str.length() && str.charAt(i) == ' ') {
                i++;
            }
            if (i < str.length() && str.charAt(i) != ' ') {
                String trim = str.substring(0, indexOf).trim();
                String trim2 = str.substring(i, str.length()).trim();
                float f2 = 0.0f;
                if ("left".equals(trim)) {
                    f = 0.0f;
                } else {
                    if ("right".equals(trim)) {
                        width = view.getWidth();
                    } else if ("center".equals(trim)) {
                        width = view.getWidth() / 2;
                    } else {
                        width = view.getWidth() / 2;
                    }
                    f = (float) width;
                }
                if (!"top".equals(trim2)) {
                    if ("bottom".equals(trim2)) {
                        height = view.getHeight();
                    } else if ("center".equals(trim2)) {
                        height = view.getHeight() / 2;
                    } else {
                        height = view.getHeight() / 2;
                    }
                    f2 = (float) height;
                }
                return new Pair<>(Float.valueOf(f), Float.valueOf(f2));
            }
        }
        return null;
    }
}
