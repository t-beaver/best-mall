package com.taobao.weex.appfram.storage;

import com.taobao.weex.bridge.JSCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageResultHandler {
    private static final String DATA = "data";
    private static final String RESULT = "result";
    private static final String RESULT_FAILED = "failed";
    private static final String RESULT_FAILED_INVALID_PARAM = "invalid_param";
    private static final String RESULT_FAILED_NO_HANDLER = "no_handler";
    private static final String RESULT_OK = "success";
    private static final String UNDEFINED = "undefined";

    private StorageResultHandler() {
    }

    public static Map<String, Object> getItemResult(String str) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("result", str != null ? "success" : "failed");
        if (str == null) {
            str = "undefined";
        }
        hashMap.put("data", str);
        return hashMap;
    }

    public static Map<String, Object> setItemResult(boolean z) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("result", z ? "success" : "failed");
        hashMap.put("data", "undefined");
        return hashMap;
    }

    public static Map<String, Object> removeItemResult(boolean z) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("result", z ? "success" : "failed");
        hashMap.put("data", "undefined");
        return hashMap;
    }

    public static Map<String, Object> getLengthResult(long j) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("result", "success");
        hashMap.put("data", Long.valueOf(j));
        return hashMap;
    }

    public static Map<String, Object> getAllkeysResult(List<String> list) {
        if (list == null) {
            list = new ArrayList<>(1);
        }
        HashMap hashMap = new HashMap(4);
        hashMap.put("result", "success");
        hashMap.put("data", list);
        return hashMap;
    }

    private static void handleResult(JSCallback jSCallback, String str, Object obj) {
        if (jSCallback != null) {
            HashMap hashMap = new HashMap(4);
            hashMap.put("result", str);
            hashMap.put("data", obj);
            jSCallback.invoke(hashMap);
        }
    }

    public static void handleNoHandlerError(JSCallback jSCallback) {
        handleResult(jSCallback, "failed", RESULT_FAILED_NO_HANDLER);
    }

    public static void handleInvalidParam(JSCallback jSCallback) {
        handleResult(jSCallback, "failed", RESULT_FAILED_INVALID_PARAM);
    }
}
