package io.dcloud.feature.gg.dcloud;

import org.json.JSONObject;

public interface IADReceiver {
    void onError(String str, String str2);

    void onReceiver(JSONObject jSONObject);
}
