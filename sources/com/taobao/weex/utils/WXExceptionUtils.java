package com.taobao.weex.utils;

import android.text.TextUtils;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXJSExceptionInfo;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.performance.WXStateRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WXExceptionUtils {
    public static String degradeUrl = "BundleUrlDefaultDegradeUrl";
    private static Set<String> sGlobalExceptionRecord = new CopyOnWriteArraySet();

    private static boolean checkNeedReportCauseRepeat(String str, WXErrorCode wXErrorCode, String str2) {
        Set<String> set;
        if (TextUtils.isEmpty(str2)) {
            return true;
        }
        if (wXErrorCode != null && wXErrorCode.getErrorGroup() != WXErrorCode.ErrorGroup.JS) {
            return true;
        }
        if (TextUtils.isEmpty(str)) {
            str = "instanceIdNull";
        }
        if (str2.length() > 200) {
            str2 = str2.substring(0, 200);
        }
        WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str);
        if (wXSDKInstance == null) {
            set = sGlobalExceptionRecord;
        } else {
            set = wXSDKInstance.getApmForInstance().exceptionRecord;
        }
        if (set == null) {
            return true;
        }
        if (set.contains(str2)) {
            return false;
        }
        set.add(str2);
        return true;
    }

    public static void commitCriticalExceptionRT(String str, WXErrorCode wXErrorCode, String str2, String str3, Map<String, String> map) {
        try {
            WXLogUtils.e("weex", "commitCriticalExceptionRT :" + wXErrorCode + "exception" + str3);
            WXStateRecord.getInstance().recordException(str, str3);
            IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
            boolean z = true;
            if (wxConfigAdapter != null ? AbsoluteConst.TRUE.equalsIgnoreCase(wxConfigAdapter.getConfig("wxapm", "check_repeat_report", AbsoluteConst.TRUE)) : true) {
                z = checkNeedReportCauseRepeat(str, wXErrorCode, str3);
            }
            if (!z) {
                return;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        commitCriticalExceptionWithDefaultUrl("BundleUrlDefault", str, wXErrorCode, str2, str3, map);
    }

    public static void commitCriticalExceptionWithDefaultUrl(String str, String str2, WXErrorCode wXErrorCode, String str3, String str4, Map<String, String> map) {
        String str5;
        String str6;
        String str7;
        IWXJSExceptionAdapter iWXJSExceptionAdapter = WXSDKManager.getInstance().getIWXJSExceptionAdapter();
        if (TextUtils.isEmpty(str)) {
            str = "BundleUrlDefault";
        }
        if (map == null) {
            map = new HashMap<>();
        }
        Map<String, String> map2 = map;
        map2.put("wxSdkInitStartTime", String.valueOf(WXEnvironment.sSDKInitStart));
        map2.put("wxSDKInitCostTime", String.valueOf(WXEnvironment.sSDKInitTime));
        map2.put("wxSDKCurExceptionTime", String.valueOf(System.currentTimeMillis()));
        map2.put("wxUseRuntimeApi", String.valueOf(WXEnvironment.sUseRunTimeApi));
        if (!TextUtils.isEmpty(str2)) {
            WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str2);
            if (wXSDKInstance != null) {
                r8 = wXSDKInstance.getApmForInstance().reportPageName;
                Object obj = wXSDKInstance.getApmForInstance().extInfo.get(WXInstanceApm.VALUE_BUNDLE_LOAD_LENGTH);
                map2.put(WXInstanceApm.VALUE_BUNDLE_LOAD_LENGTH, obj instanceof Integer ? String.valueOf(obj) : "unknownLength");
                map2.put("templateInfo", wXSDKInstance.getTemplateInfo());
                if (TextUtils.isEmpty(r8) || r8.equals("default")) {
                    if (!TextUtils.equals(degradeUrl, "BundleUrlDefaultDegradeUrl")) {
                        r8 = degradeUrl;
                    } else {
                        r8 = WXSDKInstance.requestUrl;
                    }
                }
                for (Map.Entry next : wXSDKInstance.getContainerInfo().entrySet()) {
                    map2.put((String) next.getKey(), (String) next.getValue());
                }
                map2.put("wxStageList", convertStageToStr(wXSDKInstance));
                String template = wXSDKInstance.getTemplate();
                if (template == null) {
                    str7 = "has recycle by gc";
                } else {
                    str7 = template.substring(0, Math.min(template.length(), 300));
                }
                map2.put("wxTemplateOfBundle", str7);
                Long l = wXSDKInstance.getApmForInstance().stageMap.get(WXInstanceApm.KEY_PAGE_STAGES_DOWN_BUNDLE_START);
                if (l == null) {
                    l = wXSDKInstance.getApmForInstance().stageMap.get(WXInstanceApm.KEY_PAGE_STAGES_RENDER_ORGIGIN);
                }
                if (l != null) {
                    map2.put("wxUseTime", String.valueOf(WXUtils.getFixUnixTime() - l.longValue()));
                }
            }
            str5 = r8;
            str6 = str2;
        } else {
            if (map2.size() > 0) {
                if (TextUtils.isEmpty(map2.get("weexUrl"))) {
                    r8 = map2.get("weexUrl");
                } else {
                    r8 = map2.get("bundleUrl");
                }
            }
            str5 = r8;
            str6 = "InstanceIdDefalut";
        }
        String str8 = map2.get("errorCode");
        if (str8 != null && str8.length() > 200) {
            map2.remove("errorCode");
        }
        WXJSExceptionInfo wXJSExceptionInfo = new WXJSExceptionInfo(str6, str5, wXErrorCode, str3, str4, map2);
        if (iWXJSExceptionAdapter != null) {
            iWXJSExceptionAdapter.onJSException(wXJSExceptionInfo);
        }
        WXAnalyzerDataTransfer.transferError(wXJSExceptionInfo, str2);
    }

    private static String convertStageToStr(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance == null || wXSDKInstance.getApmForInstance() == null || wXSDKInstance.getApmForInstance().stageMap.isEmpty()) {
            return "noStageRecord";
        }
        ArrayList<Map.Entry> arrayList = new ArrayList<>(wXSDKInstance.getApmForInstance().stageMap.entrySet());
        Collections.sort(arrayList, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> entry, Map.Entry<String, Long> entry2) {
                return (int) (entry.getValue().longValue() - entry2.getValue().longValue());
            }
        });
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : arrayList) {
            sb.append((String) entry.getKey());
            sb.append(Operators.CONDITION_IF_MIDDLE);
            sb.append(entry.getValue());
            sb.append("->");
        }
        return sb.toString();
    }
}
