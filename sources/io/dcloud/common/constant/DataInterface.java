package io.dcloud.common.constant;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CreateShortResultReceiver;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TelephonyUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class DataInterface {
    public static String getBaseUrl() {
        return StringConst.STREAMAPP_KEY_BASESERVICEURL();
    }

    public static String getRomVersion() {
        String str;
        if (!Build.MANUFACTURER.equals(MobilePhoneModel.XIAOMI)) {
            return Build.VERSION.INCREMENTAL;
        }
        String buildValue = DeviceInfo.getBuildValue("ro.miui.ui.version.name");
        StringBuilder sb = new StringBuilder();
        sb.append(Build.VERSION.INCREMENTAL);
        if (TextUtils.isEmpty(buildValue)) {
            str = "";
        } else {
            str = Operators.SUB + buildValue;
        }
        sb.append(str);
        return sb.toString();
    }

    public static HashMap getStartupUrlBaseData(Context context, String str, String str2, String str3, boolean z) {
        String bundleData = SP.getBundleData(context, "pdr", SP.STARTUP_DEVICE_ID);
        if (PdrUtil.isEmpty(bundleData)) {
            bundleData = TelephonyUtil.getSBBS(context, true, true, false);
        }
        int networkType = NetworkTypeUtil.getNetworkType(context);
        String str4 = Build.MODEL;
        int i = Build.VERSION.SDK_INT;
        String str5 = DeviceInfo.sPackageName;
        if (str5 == null) {
            str5 = AndroidResources.packageName;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("appid", str);
        if (!z) {
            hashMap.put("net", Integer.valueOf(networkType));
        }
        hashMap.put("imei", bundleData);
        hashMap.put("md", str4);
        hashMap.put(WXConfig.os, Integer.valueOf(i));
        hashMap.put("vb", "1.9.9.81676");
        hashMap.put(CreateShortResultReceiver.KEY_SF, Integer.valueOf(StringConst.getIntSF(str2)));
        hashMap.put(ContextChain.TAG_PRODUCT, "a");
        hashMap.put("d1", Long.valueOf(System.currentTimeMillis()));
        hashMap.put(CreateShortResultReceiver.KEY_SFD, str3);
        hashMap.put("vd", Build.MANUFACTURER);
        hashMap.put("pn", str5);
        return hashMap;
    }

    public static String getStreamappFrom(Intent intent) {
        if (intent == null || !intent.hasExtra(IntentConst.RUNING_STREAPP_LAUNCHER)) {
            return null;
        }
        String stringExtra = intent.getStringExtra(IntentConst.RUNING_STREAPP_LAUNCHER);
        if (stringExtra.indexOf("third:") == 0) {
            return stringExtra.substring(6, stringExtra.length());
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x006c A[SYNTHETIC, Splitter:B:17:0x006c] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0088 A[SYNTHETIC, Splitter:B:25:0x0088] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getSystemProperty() {
        /*
            java.lang.String r0 = "Exception while closing InputStream"
            java.lang.String r1 = "ro.miui.ui.version.name"
            r2 = 0
            java.lang.Runtime r3 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            r4.<init>()     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.lang.String r5 = "getprop "
            r4.append(r5)     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            r4.append(r1)     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.lang.String r4 = r4.toString()     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.lang.Process r3 = r3.exec(r4)     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.io.InputStream r3 = r3.getInputStream()     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            r5.<init>(r3)     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            r3 = 1024(0x400, float:1.435E-42)
            r4.<init>(r5, r3)     // Catch:{ IOException -> 0x0051, all -> 0x004f }
            java.lang.String r3 = r4.readLine()     // Catch:{ IOException -> 0x004d }
            r4.close()     // Catch:{ IOException -> 0x004d }
            r4.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x004c
        L_0x0039:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            r2.append(r1)
            java.lang.String r0 = r2.toString()
            io.dcloud.common.adapter.util.Logger.i(r0)
        L_0x004c:
            return r3
        L_0x004d:
            r3 = move-exception
            goto L_0x0053
        L_0x004f:
            r1 = move-exception
            goto L_0x0086
        L_0x0051:
            r3 = move-exception
            r4 = r2
        L_0x0053:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0084 }
            r5.<init>()     // Catch:{ all -> 0x0084 }
            java.lang.String r6 = "Unable to read sysprop "
            r5.append(r6)     // Catch:{ all -> 0x0084 }
            r5.append(r1)     // Catch:{ all -> 0x0084 }
            r5.append(r3)     // Catch:{ all -> 0x0084 }
            java.lang.String r1 = r5.toString()     // Catch:{ all -> 0x0084 }
            io.dcloud.common.adapter.util.Logger.i(r1)     // Catch:{ all -> 0x0084 }
            if (r4 == 0) goto L_0x0083
            r4.close()     // Catch:{ IOException -> 0x0070 }
            goto L_0x0083
        L_0x0070:
            r1 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = r3.toString()
            io.dcloud.common.adapter.util.Logger.i(r0)
        L_0x0083:
            return r2
        L_0x0084:
            r1 = move-exception
            r2 = r4
        L_0x0086:
            if (r2 == 0) goto L_0x009f
            r2.close()     // Catch:{ IOException -> 0x008c }
            goto L_0x009f
        L_0x008c:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r0)
            r3.append(r2)
            java.lang.String r0 = r3.toString()
            io.dcloud.common.adapter.util.Logger.i(r0)
        L_0x009f:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.constant.DataInterface.getSystemProperty():java.lang.String");
    }

    public static String getTestParam(String str) {
        String str2 = (TextUtils.isEmpty(str) || !BaseInfo.isTest(str)) ? "r" : "t";
        return "&__am=" + str2;
    }

    public static String getUrlBaseData(Context context, String str, String str2, String str3) {
        String str4;
        String imei = TelephonyUtil.getIMEI(context);
        int networkType = NetworkTypeUtil.getNetworkType(context);
        try {
            str4 = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str4 = null;
        }
        int i = Build.VERSION.SDK_INT;
        String str5 = DeviceInfo.sPackageName;
        if (str5 == null) {
            str5 = AndroidResources.packageName;
        }
        return StringUtil.format(StringConst.T_URL_BASE_DATA, str, imei, Integer.valueOf(networkType), str4, Integer.valueOf(i), "1.9.9.81676", Integer.valueOf(StringConst.getIntSF(str2)), Long.valueOf(System.currentTimeMillis()), str3, PdrUtil.encodeURL(Build.MANUFACTURER), str5) + getTestParam(str) + "&mc=" + BaseInfo.sChannel;
    }
}
