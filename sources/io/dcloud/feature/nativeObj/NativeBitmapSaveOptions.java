package io.dcloud.feature.nativeObj;

import com.taobao.weex.common.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class NativeBitmapSaveOptions {
    public int height;
    public JSONObject mClip;
    public String mFormat = "jpg";
    private JSONObject mJson = null;
    public boolean mOverwrite = false;
    public int mQuality = 50;
    public String path;
    public long size;
    public int width;

    public NativeBitmapSaveOptions(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.mJson = jSONObject;
            this.mOverwrite = jSONObject.optBoolean("overwrite", false);
            this.mFormat = this.mJson.optString(AbsoluteConst.JSON_KEY_FORMAT, "jpg");
            this.mQuality = this.mJson.optInt(Constants.Name.QUALITY, 50);
            this.mClip = this.mJson.optJSONObject("clip");
        } catch (JSONException unused) {
            this.mJson = new JSONObject();
        }
    }

    public String toJsString() {
        return this.mJson.toString();
    }
}
