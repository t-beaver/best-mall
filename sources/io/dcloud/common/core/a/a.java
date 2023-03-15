package io.dcloud.common.core.a;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.URLUtil;
import com.taobao.weex.ui.module.WXModalUIModule;
import io.dcloud.EntryProxy;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetTool;
import io.dcloud.e.c.h.b;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    private static IApp a() {
        return (IApp) EntryProxy.getInstnace().getCoreHandler().dispatchEvent(IMgr.MgrType.AppMgr, 28, BaseInfo.sDefaultBootApp);
    }

    public static void bc(String str) {
        SP.setBundleData("pdr", SP.REPORT_UNI_VERIFY_GYUID, "");
        SharedPreferences orCreateBundle = SP.getOrCreateBundle(AbsoluteConst.START_STATISTICS_DATA);
        if ((System.currentTimeMillis() - orCreateBundle.getLong(AbsoluteConst.COMMIT_APP_LIST_TIME, 0)) / 100000 >= 26000 && !BaseInfo.isChannelGooglePlay() && ((!Build.MANUFACTURER.equalsIgnoreCase(MobilePhoneModel.HUAWEI) || Build.VERSION.SDK_INT < 23 || PlatformUtil.checkGTAndYoumeng()) && (TextUtils.isEmpty(BaseInfo.sChannel) || !BaseInfo.sChannel.endsWith("|xiaomi")))) {
            orCreateBundle.edit().putLong(AbsoluteConst.COMMIT_APP_LIST_TIME, System.currentTimeMillis()).commit();
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.isNull("ret") && jSONObject.optInt("ret") == 0) {
                if (WXModalUIModule.OK.equals(jSONObject.opt("desc")) && !jSONObject.isNull("did")) {
                    SP.setBundleData(SP.getOrCreateBundle("pdr"), SP.STARTUP_DEVICE_ID, jSONObject.optString("did"));
                }
            }
            if (!BaseInfo.ISDEBUG && jSONObject.has("urd")) {
                String optString = jSONObject.optString("urd");
                if (URLUtil.isNetworkUrl(optString)) {
                    DHFile.writeFile(NetTool.httpGet(optString, false), 0, BaseInfo.sURDFilePath);
                }
            }
        } catch (JSONException e) {
            Logger.p("IDBridge", e.getMessage());
        }
    }

    public static String gd() {
        IApp a = a();
        if (a == null) {
            return "";
        }
        HashMap hashMap = new HashMap(b.a(a, SP.getOrCreateBundle((Context) a.getActivity(), AbsoluteConst.START_STATISTICS_DATA)));
        try {
            hashMap.put("ps", Integer.valueOf(BaseInfo.existsStreamEnv() ? 1 : 0));
            hashMap.put("psd", Integer.valueOf(BaseInfo.ISDEBUG ? 1 : 0));
            hashMap.put("paid", a.obtainConfigProperty("adid"));
            JSONObject obtainThridInfo = a.obtainThridInfo(IApp.ConfigProperty.ThridInfo.URDJsonData);
            hashMap.put("urv", obtainThridInfo != null ? obtainThridInfo.optString("version") : "0.1");
            if (!TextUtils.isEmpty(AppRuntime.getUniStatistics())) {
                hashMap.put("us", AppRuntime.getUniStatistics());
            }
        } catch (Exception unused) {
        }
        do {
        } while (hashMap.values().remove((Object) null));
        do {
        } while (hashMap.values().remove("null"));
        return new JSONObject(hashMap).toString();
    }
}
