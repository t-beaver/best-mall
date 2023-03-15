package io.dcloud.common.adapter.io;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import io.dcloud.common.util.ADUtils;
import io.dcloud.common.util.TestUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import org.json.JSONObject;

public class PushReceiver {
    private static void handle(final Context context, Intent intent) throws Exception {
        JSONObject jSONObject = new JSONObject(intent.getStringExtra("__json__"));
        final String stringExtra = intent.getStringExtra("appid");
        final String stringExtra2 = intent.getStringExtra("adid");
        final String optString = jSONObject.optString("tid");
        if (!TextUtils.isEmpty(optString)) {
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    TestUtil.PointTime.commitTid(context, stringExtra, optString, stringExtra2, 20);
                }
            });
        }
        if (!jSONObject.has("dplk") || !ADUtils.openDeepLink(context, jSONObject.optString("dplk"))) {
            String optString2 = jSONObject.optString("click_action");
            if (TextUtils.isEmpty(optString2)) {
                optString2 = (!jSONObject.has("appid") || TextUtils.isEmpty(jSONObject.optString("appid"))) ? "browser" : AbsoluteConst.XML_STREAMAPP;
            }
            if (TextUtils.equals("url", optString2)) {
                ADUtils.openUrl(context, jSONObject.optString("url"));
            } else if (TextUtils.equals(AbsoluteConst.SPNAME_DOWNLOAD, optString2)) {
                ADUtils.dwApp(context.getApplicationContext(), stringExtra, optString, stringExtra2, jSONObject.optString("url"), jSONObject.optString("downloadAppName"), jSONObject.optString(AbsURIAdapter.BUNDLE), 0, true, true, "");
            } else if (!TextUtils.equals(AbsoluteConst.XML_STREAMAPP, optString2) && TextUtils.equals("browser", optString2)) {
                ADUtils.openBrowser(context, jSONObject.optString("url"));
            }
        } else if (!TextUtils.isEmpty(optString)) {
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    TestUtil.PointTime.commitTid(context, stringExtra, optString, stringExtra2, 50);
                }
            });
        }
    }

    public static void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("dcloud.push.broswer")) {
            try {
                if (Boolean.valueOf(intent.getStringExtra("dcloud.push.broswer")).booleanValue()) {
                    handle(context, intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
