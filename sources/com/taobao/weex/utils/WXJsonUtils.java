package com.taobao.weex.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.WXRuntimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXJsonUtils {
    public static <T> List<T> getList(String str, Class<T> cls) {
        try {
            return JSONObject.parseArray(str, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    public static String fromObjectToJSONString(Object obj, boolean z) {
        if (!z) {
            return JSON.toJSONString(obj);
        }
        try {
            return JSON.toJSONString(obj, SerializerFeature.WriteNonStringKeyAsString);
        } catch (Exception e) {
            if (!WXEnvironment.isApkDebugable()) {
                WXLogUtils.e("fromObjectToJSONString error:", (Throwable) e);
                return "{}";
            }
            throw new WXRuntimeException("fromObjectToJSONString parse error!");
        }
    }

    public static String fromObjectToJSONString(Object obj) {
        return fromObjectToJSONString(obj, false);
    }

    public static void putAll(Map<String, Object> map, JSONObject jSONObject) {
        for (Map.Entry next : jSONObject.entrySet()) {
            String str = (String) next.getKey();
            Object value = next.getValue();
            if (!(str == null || value == null)) {
                map.put(str, value);
            }
        }
    }
}
