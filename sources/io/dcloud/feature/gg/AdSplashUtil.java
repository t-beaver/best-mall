package io.dcloud.feature.gg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.e.f.b;
import io.dcloud.feature.gg.dcloud.ADHandler;
import io.dcloud.sdk.core.util.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdSplashUtil {
    private static String DC_AD_TYPE_KEY = "dc_ad_type_key";
    static final float Main_View_Weight = 0.8f;
    public static final String SP_AD_LIST_TYPE = "ad_list_order";
    static String[] adTypes = {"adpid", Const.TYPE_CSJ, Const.TYPE_GDT};
    private static boolean isShowInterstitialAd = false;
    public static final String showCountADReward = "_s_c_a_r";

    public static String da(String str, String str2) {
        String str3;
        JSONObject optJSONObject;
        b bVar = SP.getsOrCreateBundle(ADHandler.AdTag);
        if (bVar.a("cgk")) {
            str3 = bVar.a("cgk", "");
        } else {
            str3 = bVar.a("uniad", "");
        }
        if (TextUtils.isEmpty(str3)) {
            String metaValue = AndroidResources.getMetaValue(str);
            if (PdrUtil.isEmpty(metaValue) || !metaValue.contains("_")) {
                return metaValue;
            }
            if (Arrays.binarySearch(adTypes, metaValue.substring(0, metaValue.indexOf("_"))) != -1) {
                return metaValue.substring(metaValue.indexOf("_") + 1);
            }
            return metaValue;
        }
        try {
            JSONObject jSONObject = new JSONObject(str3);
            if (!jSONObject.has("appid") || (optJSONObject = jSONObject.optJSONObject("appid")) == null || !optJSONObject.has(str2)) {
                return null;
            }
            return optJSONObject.optString(str2);
        } catch (Exception unused) {
            return null;
        }
    }

    public static JSONArray dah(String str) {
        JSONObject optJSONObject;
        String str2 = SP.getsBundleData(ADHandler.AdTag, "uniad");
        if (!TextUtils.isEmpty(str2)) {
            try {
                JSONObject jSONObject = new JSONObject(str2);
                if (jSONObject.has("appid_h") && (optJSONObject = jSONObject.optJSONObject("appid_h")) != null && optJSONObject.has(str)) {
                    return optJSONObject.getJSONArray(str);
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public static void decodeChannel(JSONObject jSONObject) {
        JSONObject jSONObject2;
        if (jSONObject != null) {
            JSONObject jSONObject3 = new JSONObject();
            JSONObject jSONObject4 = new JSONObject();
            JSONObject jSONObject5 = null;
            if (jSONObject.has("uniad")) {
                jSONObject2 = jSONObject.optJSONObject("uniad");
                if (jSONObject2 != null) {
                    jSONObject5 = jSONObject2.optJSONObject(AbsoluteConst.XML_CHANNEL);
                }
            } else {
                jSONObject2 = null;
            }
            if (jSONObject5 != null) {
                Iterator<String> keys = jSONObject5.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    JSONObject optJSONObject = jSONObject5.optJSONObject(next);
                    try {
                        jSONObject3.put(next, optJSONObject.optString("appid"));
                        jSONObject4.put(next, optJSONObject.optJSONArray("appidh"));
                    } catch (Exception unused) {
                    }
                }
                try {
                    jSONObject2.put("appid", jSONObject3);
                    jSONObject2.put("appid_h", jSONObject4);
                } catch (JSONException unused2) {
                }
            }
        }
    }

    public static int dh(Context context) {
        return (int) (((float) context.getResources().getDisplayMetrics().heightPixels) * Main_View_Weight);
    }

    public static int dw(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String getAL(Context context) {
        String str = SP.getsBundleData(context, ADHandler.AdTag, "al");
        if (PdrUtil.isEmpty(str)) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.length() == 0) {
                return "";
            }
            JSONObject jSONObject2 = new JSONObject();
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next);
                try {
                    if (PdrUtil.isEmpty(optString)) {
                        jSONObject2.put(next, 0);
                    } else if (context.getPackageManager().getLaunchIntentForPackage(optString) != null) {
                        jSONObject2.put(next, 1);
                    } else {
                        jSONObject2.put(next, 0);
                    }
                } catch (Exception unused) {
                }
            }
            if (jSONObject2.length() > 0) {
                return jSONObject2.toString();
            }
            return "";
        } catch (Exception unused2) {
            return "";
        }
    }

    public static List<String> getAdOrder() {
        ArrayList arrayList = new ArrayList(Arrays.asList(SP.getBundleData(ADHandler.AdTag, SP_AD_LIST_TYPE).split(",")));
        arrayList.remove("");
        return arrayList;
    }

    public static String getAdpId(IApp iApp, String str) {
        JSONObject optJSONObject;
        String str2 = SP.getsBundleData(ADHandler.AdTag, "uniad");
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str2);
            if (!jSONObject.has("adpids") || (optJSONObject = jSONObject.optJSONObject("adpids")) == null || !optJSONObject.has(str)) {
                return null;
            }
            return optJSONObject.optString(str);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getAppKey(String str, String str2) {
        JSONObject optJSONObject;
        String str3 = SP.getsBundleData(DeviceInfo.sApplicationContext, ADHandler.AdTag, "uniad");
        if (TextUtils.isEmpty(str3)) {
            String metaValue = AndroidResources.getMetaValue(str);
            if (PdrUtil.isEmpty(metaValue) || !metaValue.contains("_")) {
                return metaValue;
            }
            if (Arrays.binarySearch(adTypes, metaValue.substring(0, metaValue.indexOf("_"))) != -1) {
                return metaValue.substring(metaValue.indexOf("_") + 1);
            }
            return metaValue;
        }
        try {
            JSONObject jSONObject = new JSONObject(str3);
            if (jSONObject.has("appid") && (optJSONObject = jSONObject.optJSONObject("appid")) != null && optJSONObject.has(str2)) {
                return optJSONObject.optString(str2);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static String getAppKey2(String str, String str2) {
        JSONObject optJSONObject;
        String str3 = SP.getsBundleData(DeviceInfo.sApplicationContext, ADHandler.AdTag, "uniad");
        if (TextUtils.isEmpty(str3)) {
            String metaValue = AndroidResources.getMetaValue(str);
            if (PdrUtil.isEmpty(metaValue) || !metaValue.contains("_")) {
                return metaValue;
            }
            if (Arrays.binarySearch(adTypes, metaValue.substring(0, metaValue.indexOf("_"))) != -1) {
                return metaValue.substring(metaValue.indexOf("_") + 1);
            }
            return metaValue;
        }
        try {
            JSONObject jSONObject = new JSONObject(str3);
            if (jSONObject.has("appKey") && (optJSONObject = jSONObject.optJSONObject("appKey")) != null && optJSONObject.has(str2)) {
                return optJSONObject.optString(str2);
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static Drawable getApplicationIcon(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationIcon(packageManager.getApplicationInfo(context.getPackageName(), 0));
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getApplicationName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(context.getPackageName(), 0));
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getPlashType() {
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(DC_AD_TYPE_KEY);
        if (orCreateBundle != null) {
            return orCreateBundle.getString("type", "dcloud");
        }
        return "dcloud";
    }

    public static String getSplashAdpId(String str, String str2) {
        JSONObject optJSONObject;
        String str3 = SP.getsBundleData(ADHandler.AdTag, "uniad");
        if (!TextUtils.isEmpty(str3)) {
            try {
                JSONObject jSONObject = new JSONObject(str3);
                if (!jSONObject.has("splash") || (optJSONObject = jSONObject.optJSONObject("splash")) == null || !optJSONObject.has(str)) {
                    return null;
                }
                return optJSONObject.optString(str);
            } catch (Exception unused) {
                return null;
            }
        } else if (TextUtils.isEmpty(str2)) {
            return null;
        } else {
            String metaValue = AndroidResources.getMetaValue(str2);
            if (!PdrUtil.isEmpty(metaValue) && metaValue.contains("_")) {
                if (Arrays.binarySearch(adTypes, metaValue.substring(0, metaValue.indexOf("_"))) != -1) {
                    metaValue = metaValue.substring(metaValue.indexOf("_") + 1);
                }
            }
            if ("UNIAD_FULL_SPLASH".equals(str2)) {
                return Boolean.parseBoolean(metaValue) ? "1" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
            }
            return metaValue;
        }
    }

    public static JSONObject getUniad() {
        String str = SP.getsBundleData(ADHandler.AdTag, "uniad");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("adpids")) {
                return jSONObject.optJSONObject("adpids");
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean isShowingInterstitialAd() {
        return isShowInterstitialAd;
    }

    public static void saveADShowCount(String str, String str2) {
        JSONObject jSONObject;
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(showCountADReward);
        int i = 0;
        try {
            jSONObject = new JSONObject(SP.getBundleData(orCreateBundle, str));
            try {
                i = jSONObject.optInt(str2) + 1;
                jSONObject.put(str2, i);
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            jSONObject = null;
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
            try {
                jSONObject.put(str2, i + 1);
            } catch (Exception unused3) {
            }
        }
        SP.setBundleData(orCreateBundle, str, jSONObject.toString());
    }

    public static void saveOperate(Context context, String str, HashMap<String, String> hashMap) {
        try {
            SharedPreferences.Editor edit = SP.getOrCreateBundle(context, ADHandler.AdTag).edit();
            for (String next : hashMap.keySet()) {
                edit.putString(next, hashMap.get(next));
            }
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setShowInterstitialAd(boolean z) {
        isShowInterstitialAd = z;
    }

    public static void saveOperate(HashMap<String, String> hashMap) {
        saveOperate((Context) null, (String) null, hashMap);
    }
}
