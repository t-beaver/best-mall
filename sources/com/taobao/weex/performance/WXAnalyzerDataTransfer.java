package com.taobao.weex.performance;

import android.util.Log;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXJSExceptionInfo;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.utils.WXUtils;
import java.util.List;
import org.json.JSONObject;

public class WXAnalyzerDataTransfer {
    private static final String GROUP = "WXAnalyzer";
    public static final String INTERACTION_TAG = "wxInteractionAnalyzer";
    private static final String MODULE_ERROR = "WXError";
    private static final String MODULE_WX_APM = "wxapm";
    public static boolean isOpenPerformance = false;
    private static boolean sOpenInteractionLog;

    public static void transferPerformance(String str, String str2, String str3, Object obj) {
        WXSDKInstance wXSDKInstance;
        if (isOpenPerformance) {
            if (sOpenInteractionLog && "stage".equals(str2)) {
                Log.d(INTERACTION_TAG, "[client][stage]" + str + "," + str3 + "," + obj);
            }
            List<IWXAnalyzer> wXAnalyzerList = WXSDKManager.getInstance().getWXAnalyzerList();
            if (wXAnalyzerList != null && wXAnalyzerList.size() != 0 && (wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str)) != null) {
                try {
                    String jSONObject = new JSONObject().put(str3, obj).toString();
                    for (IWXAnalyzer transfer : wXAnalyzerList) {
                        transfer.transfer(MODULE_WX_APM, wXSDKInstance.getInstanceId(), str2, jSONObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void transferInteractionInfo(WXComponent wXComponent) {
        List<IWXAnalyzer> wXAnalyzerList;
        if (isOpenPerformance && (wXAnalyzerList = WXSDKManager.getInstance().getWXAnalyzerList()) != null && wXAnalyzerList.size() != 0) {
            try {
                String jSONObject = new JSONObject().put("renderOriginDiffTime", WXUtils.getFixUnixTime() - wXComponent.getInstance().getWXPerformance().renderUnixTimeOrigin).put("type", wXComponent.getComponentType()).put("ref", wXComponent.getRef()).put("style", wXComponent.getStyles()).put(TemplateDom.KEY_ATTRS, wXComponent.getAttrs()).toString();
                for (IWXAnalyzer transfer : wXAnalyzerList) {
                    transfer.transfer(MODULE_WX_APM, wXComponent.getInstanceId(), "wxinteraction", jSONObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void transferError(WXJSExceptionInfo wXJSExceptionInfo, String str) {
        List<IWXAnalyzer> wXAnalyzerList;
        WXSDKInstance sDKInstance;
        String str2;
        if (WXEnvironment.isApkDebugable() && (wXAnalyzerList = WXSDKManager.getInstance().getWXAnalyzerList()) != null && wXAnalyzerList.size() != 0 && (sDKInstance = WXSDKManager.getInstance().getSDKInstance(str)) != null) {
            WXErrorCode errCode = wXJSExceptionInfo.getErrCode();
            try {
                str2 = new JSONObject().put("instanceId", str).put("url", sDKInstance.getBundleUrl()).put("errorCode", errCode.getErrorCode()).put("errorMsg", errCode.getErrorMsg()).put("errorGroup", errCode.getErrorGroup()).toString();
            } catch (Exception e) {
                e.printStackTrace();
                str2 = "";
            }
            for (IWXAnalyzer transfer : wXAnalyzerList) {
                transfer.transfer(GROUP, MODULE_ERROR, errCode.getErrorType().toString(), str2);
            }
        }
    }

    public static void switchInteractionLog(boolean z) {
        if (sOpenInteractionLog != z && WXEnvironment.JsFrameworkInit) {
            sOpenInteractionLog = z;
            WXBridgeManager.getInstance().registerCoreEnv("switchInteractionLog", String.valueOf(z));
        }
    }

    public static boolean isInteractionLogOpen() {
        return sOpenInteractionLog;
    }
}
