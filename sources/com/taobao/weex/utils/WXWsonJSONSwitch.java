package com.taobao.weex.utils;

import com.alibaba.fastjson.JSON;
import com.taobao.weex.bridge.WXJSObject;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.wson.Wson;
import com.taobao.weex.wson.WsonUtils;

public class WXWsonJSONSwitch {
    private static final String TAG = "WXSwitch";
    public static boolean USE_WSON = true;
    public static final String WSON_OFF = "wson_off";

    public static final byte[] convertJSONToWsonIfUseWson(byte[] bArr) {
        if (!USE_WSON) {
            return bArr;
        }
        if (bArr == null) {
            return null;
        }
        String str = new String(bArr);
        if (str.startsWith(Operators.ARRAY_START_STR)) {
            return WsonUtils.toWson(JSON.parseArray(str));
        }
        return WsonUtils.toWson(JSON.parse(str));
    }

    public static final Object parseWsonOrJSON(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            if (USE_WSON) {
                return Wson.parse(bArr);
            }
            return JSON.parse(new String(bArr, "UTF-8"));
        } catch (Exception e) {
            WXLogUtils.e(TAG, (Throwable) e);
            if (USE_WSON) {
                return JSON.parse(new String(bArr));
            }
            return Wson.parse(bArr);
        }
    }

    public static final WXJSObject toWsonOrJsonWXJSObject(Object obj) {
        if (obj == null) {
            return new WXJSObject((Object) null);
        }
        if (obj.getClass() == WXJSObject.class) {
            return (WXJSObject) obj;
        }
        if (USE_WSON) {
            return new WXJSObject(4, Wson.toWson(obj));
        }
        return new WXJSObject(3, WXJsonUtils.fromObjectToJSONString(obj));
    }

    public static final Object convertWXJSObjectDataToJSON(WXJSObject wXJSObject) {
        if (wXJSObject.type == 4) {
            return JSON.parse(Wson.parse((byte[]) wXJSObject.data).toString());
        }
        return JSON.parse(wXJSObject.data.toString());
    }

    public static String fromObjectToJSONString(WXJSObject wXJSObject) {
        Object parse;
        if (wXJSObject == null || wXJSObject.type != 4 || (parse = Wson.parse((byte[]) wXJSObject.data)) == null) {
            return WXJsonUtils.fromObjectToJSONString(wXJSObject, false);
        }
        return parse.toString();
    }
}
