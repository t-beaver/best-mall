package com.taobao.weex.bridge;

import android.text.TextUtils;
import com.taobao.weex.common.WXJSService;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.StringUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WXServiceManager {
    /* access modifiers changed from: private */
    public static volatile ConcurrentHashMap<String, WXJSService> sInstanceJSServiceMap = new ConcurrentHashMap<>();

    public static boolean registerService(String str, String str2, Map<String, Object> map) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        String str3 = "serviceName: \"" + str + JSUtil.QUOTE;
        for (String next : map.keySet()) {
            Object obj = map.get(next);
            if (obj instanceof String) {
                str3 = str3 + ", '" + next + "': '" + obj + "'";
            } else {
                str3 = str3 + ", '" + next + "': " + obj;
            }
        }
        String format = StringUtil.format(";(function(service, options){ ;%s; })({ %s }, { %s });", str2, "register: global.registerService, unregister: global.unregisterService", str3);
        WXJSService wXJSService = new WXJSService();
        wXJSService.setName(str);
        wXJSService.setScript(str2);
        wXJSService.setOptions(map);
        sInstanceJSServiceMap.put(str, wXJSService);
        WXBridgeManager.getInstance().execJSService(format);
        return true;
    }

    public static boolean unRegisterService(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        sInstanceJSServiceMap.remove(str);
        WXBridgeManager.getInstance().execJSService(StringUtil.format("global.unregisterService( \"%s\" );", str));
        return true;
    }

    public static void execAllCacheJsService() {
        for (String str : sInstanceJSServiceMap.keySet()) {
            WXJSService wXJSService = sInstanceJSServiceMap.get(str);
            registerService(wXJSService.getName(), wXJSService.getScript(), wXJSService.getOptions());
        }
    }

    public static WXJSService getService(String str) {
        if (sInstanceJSServiceMap != null) {
            return sInstanceJSServiceMap.get(str);
        }
        return null;
    }

    public static void reload() {
        if (sInstanceJSServiceMap != null && sInstanceJSServiceMap.size() > 0) {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    for (Map.Entry value : WXServiceManager.sInstanceJSServiceMap.entrySet()) {
                        WXJSService wXJSService = (WXJSService) value.getValue();
                        WXServiceManager.registerService(wXJSService.getName(), wXJSService.getScript(), wXJSService.getOptions());
                    }
                }
            });
        }
    }
}
