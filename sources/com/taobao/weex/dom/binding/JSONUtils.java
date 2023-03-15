package com.taobao.weex.dom.binding;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.el.parse.Operators;

public class JSONUtils {
    public static boolean isJSON(Object obj) {
        if (obj instanceof JSONObject) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).startsWith(Operators.BLOCK_START_STR);
        }
        return false;
    }

    public static JSONObject toJSON(Object obj) {
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        return JSONObject.parseObject(obj.toString());
    }

    public static boolean isJSON(String str) {
        return str.startsWith(Operators.BLOCK_START_STR);
    }
}
