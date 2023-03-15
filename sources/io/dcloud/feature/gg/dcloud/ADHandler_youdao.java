package io.dcloud.feature.gg.dcloud;

import android.content.Context;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.gg.dcloud.ADHandler;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

class ADHandler_youdao extends ADHandler {
    ADHandler_youdao() {
    }

    static void click_youdao(Context context, ADHandler.AdData adData, String str) {
        JSONObject report = adData.report();
        if (report != null) {
            handleTrackers_youdao(report.optJSONArray("clktrackers"), "clktrackers", adData);
        }
        ADHandler.click_base(context, adData, str);
    }

    static void dplk_youdao(Context context, ADHandler.AdData adData, String str) {
        JSONObject report = adData.report();
        if (report != null) {
            handleTrackers_youdao(report.optJSONArray("dptrackers"), "dptrackers", adData);
        }
    }

    static void handleAdData_youdao(Context context, JSONObject jSONObject, long j) throws Exception {
        ADHandler.handleAdData_dcloud(context, jSONObject, j);
    }

    static void handleTrackers_youdao(JSONArray jSONArray, String str, ADHandler.AdData adData) {
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                final String optString = jSONArray.optString(i);
                ADHandler.log("ADHandler_youdao", str + ";url=" + optString);
                JSONObject full = adData.full();
                final HashMap hashMap = null;
                if (full != null && full.has("ua") && full.optString("ua").equalsIgnoreCase("webview")) {
                    hashMap = new HashMap();
                    hashMap.put(IWebview.USER_AGENT, ADHandler.get("ua-webview"));
                }
                ThreadPool.self().addThreadTask(new Runnable() {
                    public void run() {
                        try {
                            NetTool.httpGet(optString, (HashMap<String, String>) hashMap, true);
                        } catch (Exception unused) {
                        }
                    }
                });
            }
        }
    }

    static void view_youdao(Context context, ADHandler.AdData adData, String str) {
        JSONObject report = adData.report();
        if (report != null) {
            handleTrackers_youdao(report.optJSONArray("imptracker"), "imptracker", adData);
        }
    }
}
