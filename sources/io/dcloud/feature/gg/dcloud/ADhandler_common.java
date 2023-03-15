package io.dcloud.feature.gg.dcloud;

import android.content.Context;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.gg.dcloud.ADHandler;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class ADhandler_common extends ADHandler {
    static void click_common(Context context, ADHandler.AdData adData, String str) {
        JSONObject report = adData.report();
        if (report != null) {
            handleTrackers_common(report.optJSONArray("clktracker"), "clktracker", adData);
        }
        ADHandler.click_base(context, adData, str);
    }

    static void handleAdData_common(Context context, JSONObject jSONObject, long j) throws Exception {
        ADHandler.handleAdData_dcloud(context, jSONObject, j);
    }

    static void handleTrackers_common(JSONArray jSONArray, String str, ADHandler.AdData adData) {
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                final String optString = jSONArray.optJSONObject(i).optString("url");
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

    static void handletask_common(Context context, ADHandler.AdData adData, String str, String str2) {
        JSONObject report = adData.report();
        if (report != null) {
            handleTrackers_common(report.optJSONArray(str2), str2, adData);
        }
    }
}
