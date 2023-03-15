package io.dcloud.feature.gg.dcloud;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.ADUtils;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.LoadAppUtils;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.feature.gg.dcloud.ADHandler;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ADHandler_wanka extends ADHandler {
    ADHandler_wanka() {
    }

    /* access modifiers changed from: private */
    public static void checkRunLoad(final Context context, final JSONObject jSONObject, int i) {
        final int i2 = i + 1;
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (ADHandler_wanka.isRun(context, jSONObject.optString(AbsURIAdapter.BUNDLE))) {
                    JSONArray optJSONArray = jSONObject.optJSONObject("report").optJSONArray("actvtrackers");
                    if (optJSONArray != null) {
                        ADHandler_wanka.handleTrackers_wanka(optJSONArray, jSONObject, "actvtrackers");
                    } else {
                        ADHandler_wanka.log("no actvtrackers");
                    }
                } else {
                    int i = i2;
                    if (i <= 3) {
                        ADHandler_wanka.checkRunLoad(context, jSONObject, i);
                    }
                }
            }
        }, (long) (i * 60 * 1000));
    }

    static void click_wanka(final Context context, final ADHandler.AdData adData, final String str) {
        int optInt = adData.data().optInt("template", 0);
        String optString = adData.data().optString("action");
        if (optInt != 1) {
            JSONArray optJSONArray = adData.report().optJSONArray("clktrackers");
            if (optJSONArray != null) {
                handleTrackers_wanka(optJSONArray, adData.full(), "clktrackers");
            }
            if (AbsoluteConst.SPNAME_DOWNLOAD.equals(optString)) {
                String optString2 = adData.data().optString(AbsURIAdapter.BUNDLE);
                if (TextUtils.isEmpty(optString2) || !LoadAppUtils.isAppLoad(context, optString2)) {
                    ThreadPool.self().addThreadTask(new Runnable() {
                        public void run() {
                            String optString = ADHandler.AdData.this.data().optString("url");
                            String optString2 = ADHandler.AdData.this.data().optString("tid");
                            Context context = context;
                            ADHandler.AdData adData = ADHandler.AdData.this;
                            ADHandler_wanka.click_wanka_other(context, adData, optString, adData.mOriginalAppid, optString2, str, adData.full());
                        }
                    });
                    if (!adData.isEClick()) {
                        ADUtils.loadAppTip(context);
                    }
                } else if (!adData.isEClick()) {
                    try {
                        Intent parseUri = Intent.parseUri(adData.data().optString("dplk"), 1);
                        parseUri.setFlags(268435456);
                        if (BaseInfo.isDefense) {
                            parseUri.setSelector((Intent) null);
                            parseUri.setComponent((ComponentName) null);
                            parseUri.addCategory("android.intent.category.BROWSABLE");
                        }
                        if (context.getPackageManager().resolveActivity(parseUri, 65536) != null) {
                            context.startActivity(parseUri);
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ADHandler.click_base(context, adData, str);
            }
        } else if (!adData.isEClick()) {
            JSONArray optJSONArray2 = adData.report().optJSONArray("clktrackers");
            if (AbsoluteConst.SPNAME_DOWNLOAD.equals(optString)) {
                handleTrackers_wanka(optJSONArray2, adData.full(), new ICallBack() {
                    public Object onCallBack(int i, Object obj) {
                        JSONObject optJSONObject = ((JSONObject) obj).optJSONObject("data");
                        String optString = optJSONObject.optString("dstlink");
                        String optString2 = optJSONObject.optString("clickid");
                        String optString3 = ADHandler.AdData.this.data().optString("tid");
                        try {
                            ADHandler.AdData.this.full().put("click_id", optString2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Context context = context;
                        ADHandler.AdData adData = ADHandler.AdData.this;
                        ADHandler_wanka.click_wanka_other(context, adData, optString, adData.mOriginalAppid, optString3, str, adData.full());
                        if (ADHandler.AdData.this.isEClick()) {
                            return null;
                        }
                        ADUtils.loadAppTip(context);
                        return null;
                    }
                }, "clktrackers");
            } else if ("url".equals(optString)) {
                click_wanka_url(context, adData, optJSONArray2, adData.mOriginalAppid, str);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void click_wanka_other(Context context, ADHandler.AdData adData, String str, String str2, String str3, String str4, JSONObject jSONObject) {
        String str5;
        Context context2 = context;
        String str6 = str;
        final JSONObject jSONObject2 = jSONObject;
        final JSONObject optJSONObject = jSONObject2.optJSONObject("report");
        JSONArray optJSONArray = optJSONObject.optJSONArray("dwnlsts");
        if (optJSONArray != null) {
            handleTrackers_wanka(optJSONArray, jSONObject2, "dwnlsts");
        }
        String optString = jSONObject2.optJSONObject("data").optString(AbsURIAdapter.BUNDLE);
        if (ADUtils.getDdDataForUrl(str) == null) {
            String str7 = Operators.DOT_STR + context.getString(R.string.in_package);
            if (TextUtils.isEmpty(optString)) {
                str5 = null;
            } else {
                str5 = optString + Operators.DOT_STR + context.getString(R.string.in_package);
            }
            String fileNameByUrl = PdrUtil.getFileNameByUrl(str6, str7, str5);
            String optString2 = adData.full() != null ? adData.full().optString("ua") : "";
            if (adData.isEClick()) {
                ADSim.dwApp(context, str2, str3, str4, str, optString, new ILoadCallBack() {
                    public Object onCallBack(int i, Context context, Object obj) {
                        if (i != 0) {
                            return null;
                        }
                        JSONArray optJSONArray = optJSONObject.optJSONArray("dwnltrackers");
                        if (optJSONArray != null) {
                            ADHandler_wanka.handleTrackers_wanka(optJSONArray, jSONObject2, "dwnltrackers");
                        }
                        return Boolean.TRUE;
                    }
                }, optString2);
                return;
            }
            long loadADFile = ADUtils.loadADFile(context, str2, str3, str4, fileNameByUrl, optString, str, "application/vnd.android", new ILoadCallBack() {
                public Object onCallBack(int i, Context context, Object obj) {
                    if (i != 0) {
                        return null;
                    }
                    JSONArray optJSONArray = optJSONObject.optJSONArray("dwnltrackers");
                    if (optJSONArray != null) {
                        ADHandler_wanka.handleTrackers_wanka(optJSONArray, jSONObject2, "dwnltrackers");
                    }
                    ADHandler_wanka.runApp(context, jSONObject2, (Intent) obj);
                    return Boolean.TRUE;
                }
            }, true, optString2);
            ADUtils.ADLoadData aDLoadData = new ADUtils.ADLoadData();
            aDLoadData.name = AdSplashUtil.getApplicationName(context);
            aDLoadData.pname = optString;
            aDLoadData.url = str6;
            aDLoadData.appid = str2;
            aDLoadData.tid = str3;
            aDLoadData.adid = str4;
            aDLoadData.type = "wanka";
            aDLoadData.id = loadADFile;
            aDLoadData.ua = optString2;
            try {
                ADUtils.saveLoadData(aDLoadData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void click_wanka_url(final Context context, final ADHandler.AdData adData, final JSONArray jSONArray, String str, String str2) {
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                if (jSONArray != null) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        try {
                            JSONObject optJSONObject = jSONArray.optJSONObject(i);
                            int optInt = optJSONObject.optInt("template_type");
                            JSONObject full = adData.full();
                            boolean z = full.has("ua") && full.optString("ua").equalsIgnoreCase("webview");
                            full.put("u-a", ADHandler.get("ua-webview"));
                            String formatUrl = ADHandler.formatUrl(optJSONObject.optString("url"), full);
                            int optInt2 = optJSONObject.optInt("http_method");
                            String optString = optJSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME);
                            if (optInt != 1) {
                                ADHandler_wanka.handleTrackers_wanka(formatUrl, optString, optInt2, 2, false, (ICallBack) null, "clktrackers", z);
                            } else if (adData.isEClick()) {
                                ADSim.openUrl(context, formatUrl);
                            } else {
                                ADUtils.openUrl(context, formatUrl);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    static void dplk_wanka(Context context, ADHandler.AdData adData, String str) {
        JSONArray optJSONArray = adData.report().optJSONArray("dplktrackers");
        if (optJSONArray != null) {
            handleTrackers_wanka(optJSONArray, adData.full(), "dplktrackers");
        }
    }

    static void handleAdData_wanka(Context context, JSONObject jSONObject, long j) throws Exception {
        ADHandler.handleAdData_dcloud(context, jSONObject, j);
    }

    /* access modifiers changed from: private */
    public static void handleTrackers_wanka(JSONArray jSONArray, JSONObject jSONObject, String str) {
        handleTrackers_wanka(jSONArray, jSONObject, (ICallBack) null, str);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x0018  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isRun(android.content.Context r2, java.lang.String r3) {
        /*
            java.lang.String r0 = "activity"
            java.lang.Object r2 = r2.getSystemService(r0)
            android.app.ActivityManager r2 = (android.app.ActivityManager) r2
            r0 = 100
            java.util.List r2 = r2.getRunningTasks(r0)
            java.util.Iterator r2 = r2.iterator()
        L_0x0012:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x0038
            java.lang.Object r0 = r2.next()
            android.app.ActivityManager$RunningTaskInfo r0 = (android.app.ActivityManager.RunningTaskInfo) r0
            android.content.ComponentName r1 = r0.topActivity
            java.lang.String r1 = r1.getPackageName()
            boolean r1 = r1.equals(r3)
            if (r1 != 0) goto L_0x0036
            android.content.ComponentName r0 = r0.baseActivity
            java.lang.String r0 = r0.getPackageName()
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0012
        L_0x0036:
            r2 = 1
            goto L_0x0039
        L_0x0038:
            r2 = 0
        L_0x0039:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gg.dcloud.ADHandler_wanka.isRun(android.content.Context, java.lang.String):boolean");
    }

    static void log(String str) {
        ADHandler.log("wanka", str);
    }

    /* access modifiers changed from: private */
    public static void runApp(Context context, JSONObject jSONObject, Intent intent) {
        try {
            JSONArray optJSONArray = jSONObject.optJSONObject("report").optJSONArray("intltrackers");
            if (optJSONArray != null) {
                handleTrackers_wanka(optJSONArray, jSONObject, "intltrackers");
            }
            checkRunLoad(context, jSONObject, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RuningAcitvityUtil.StartActivity(context, intent);
    }

    static void view_wanka(Context context, ADHandler.AdData adData, String str) {
        JSONObject report = adData.report();
        if (report != null) {
            handleTrackers_wanka(report.optJSONArray("imptrackers"), adData.full(), "imptrackers");
        }
    }

    /* access modifiers changed from: private */
    public static void handleTrackers_wanka(String str, String str2, int i, int i2, boolean z, ICallBack iCallBack, String str3, boolean z2) {
        final int i3 = i2 - 1;
        log("handleTrackers_wanka template = " + (z ? 1 : 0) + "; t_count=" + i3 + "; tagMsg " + str3 + ";  url=" + str);
        final boolean z3 = z2;
        final int i4 = i;
        final String str4 = str;
        final String str5 = str2;
        final boolean z4 = z;
        final ICallBack iCallBack2 = iCallBack;
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                HashMap hashMap;
                byte[] bArr = null;
                if (z3) {
                    hashMap = new HashMap();
                    hashMap.put(IWebview.USER_AGENT, ADHandler.get("ua-webview"));
                } else {
                    hashMap = null;
                }
                int i = i4;
                if (i == 0) {
                    try {
                        bArr = NetTool.httpGet(str4, (HashMap<String, String>) hashMap, true);
                    } catch (Exception unused) {
                    }
                } else if (i == 1) {
                    bArr = NetTool.httpPost(str4, str5, hashMap);
                }
                if (z4 && bArr != null) {
                    try {
                        JSONObject jSONObject = new JSONObject(new String(bArr));
                        if (jSONObject.optInt("ret") != 0) {
                            String optString = jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE);
                            ADHandler_wanka.log("handleTrackers_wanka Runnable Error url=" + str4 + ";msg=" + optString);
                            int i2 = i3;
                            if (i2 > 0) {
                                ADHandler_wanka.handleTrackers_wanka(str4, optString, i4, i2, z4, iCallBack2, (String) null, z3);
                            }
                            NetTool.httpGet(str4, (HashMap<String, String>) hashMap, true);
                            return;
                        }
                        ICallBack iCallBack = iCallBack2;
                        if (iCallBack != null) {
                            iCallBack.onCallBack(1, jSONObject);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static void handleTrackers_wanka(JSONArray jSONArray, JSONObject jSONObject, ICallBack iCallBack, String str) {
        JSONObject jSONObject2 = jSONObject;
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject optJSONObject = jSONArray.optJSONObject(i);
            int optInt = optJSONObject.optInt("template_type");
            boolean z = jSONObject2.has("ua") && jSONObject2.optString("ua").equalsIgnoreCase("webview");
            try {
                jSONObject2.put("u-a", ADHandler.get("ua-webview"));
            } catch (JSONException unused) {
            }
            handleTrackers_wanka(ADHandler.formatUrl(optJSONObject.optString("url"), jSONObject2), optJSONObject.optString(UriUtil.LOCAL_CONTENT_SCHEME), optJSONObject.optInt("http_method"), 2, optInt == 1, iCallBack, str, z);
        }
    }
}
