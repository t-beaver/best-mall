package io.dcloud.common.DHInterface;

import org.json.JSONException;
import org.json.JSONObject;

public interface ISmartUpdate {

    public interface OnSmartUpdateCallback {
        void onCallback(SmartUpdateCallbackParams smartUpdateCallbackParams);
    }

    public static class SmartUpdateCallbackParams {
        public String icon_url;
        public int mode;
        public boolean needUpdate = false;
        public String oldVersion;
        public JSONObject sitemap;
        public String splash_url;
        public int type = 1;
        public String updateVersion;
        public String url;
        public String wap2app_url;

        public SmartUpdateCallbackParams(String str) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                int optInt = jSONObject.optInt("ret", jSONObject.optInt("status", 0) == 1 ? 0 : 1);
                this.url = jSONObject.optString("url");
                this.icon_url = jSONObject.optString("icon_url");
                this.splash_url = jSONObject.optString("splash_url");
                this.wap2app_url = jSONObject.optString("wap2app_url");
                this.sitemap = jSONObject.optJSONObject("sitemap");
                if (optInt == 0) {
                    String optString = jSONObject.optString("type");
                    this.updateVersion = jSONObject.optString("updateVersion");
                    this.needUpdate = true;
                    if (optString.equals("wgt")) {
                        this.type = 0;
                    }
                }
                this.mode = jSONObject.optInt("up_mode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void checkUpdate();

    void reload();

    void update(SmartUpdateCallbackParams smartUpdateCallbackParams);
}
