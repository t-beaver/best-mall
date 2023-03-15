package io.dcloud.weex;

import android.text.TextUtils;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.feature.weex.WeexInstanceMgr;

public class DCFileUtils {
    /* JADX WARNING: Can't wrap try/catch for region: R(2:11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        io.dcloud.feature.weex.WeexInstanceMgr.self().setJSFKFileNotFound(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0097, code lost:
        r4.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0025 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.InputStream loadWeexAsset(java.lang.String r4, android.content.Context r5) {
        /*
            java.io.InputStream r0 = io.dcloud.common.util.AppRuntime.loadWeexAsset(r4, r5)
            if (r0 == 0) goto L_0x0007
            return r0
        L_0x0007:
            java.lang.String r1 = "uni-jsframework"
            boolean r1 = r4.startsWith(r1)
            if (r1 == 0) goto L_0x009a
            java.lang.String r1 = ".js"
            boolean r1 = r4.endsWith(r1)
            if (r1 == 0) goto L_0x009a
            r1 = 0
            r2 = 1
            android.content.res.AssetManager r5 = r5.getAssets()     // Catch:{ FileNotFoundException -> 0x0025 }
            java.io.InputStream r1 = r5.open(r4)     // Catch:{ FileNotFoundException -> 0x0025 }
            goto L_0x002c
        L_0x0022:
            r4 = move-exception
            goto L_0x0097
        L_0x0025:
            io.dcloud.feature.weex.WeexInstanceMgr r4 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x0022 }
            r4.setJSFKFileNotFound(r2)     // Catch:{ Exception -> 0x0022 }
        L_0x002c:
            if (r1 == 0) goto L_0x008f
            io.dcloud.feature.weex.WeexInstanceMgr r4 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x0022 }
            r5 = 0
            r4.setJSFKFileNotFound(r5)     // Catch:{ Exception -> 0x0022 }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0022 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0022 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0022 }
            r4.<init>(r2)     // Catch:{ Exception -> 0x0022 }
            java.lang.String r1 = r4.readLine()     // Catch:{ Exception -> 0x0022 }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Exception -> 0x0022 }
            r3 = 2
            java.lang.String r1 = r1.substring(r3)     // Catch:{ Exception -> 0x0022 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0022 }
            java.lang.String r1 = "version"
            java.lang.String r1 = r2.optString(r1)     // Catch:{ Exception -> 0x0022 }
            io.dcloud.common.util.BaseInfo.uniVersionV3 = r1     // Catch:{ Exception -> 0x0022 }
            java.lang.String r1 = "encode"
            java.lang.String r1 = r2.optString(r1)     // Catch:{ Exception -> 0x0022 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0022 }
            if (r2 != 0) goto L_0x009a
            java.lang.String r2 = "base64"
            boolean r1 = r1.equals(r2)     // Catch:{ Exception -> 0x0022 }
            if (r1 == 0) goto L_0x009a
            java.lang.String r1 = r4.readLine()     // Catch:{ Exception -> 0x0022 }
        L_0x006e:
            java.lang.String r2 = r4.readLine()     // Catch:{ Exception -> 0x0022 }
            if (r2 == 0) goto L_0x0084
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0022 }
            r3.<init>()     // Catch:{ Exception -> 0x0022 }
            r3.append(r1)     // Catch:{ Exception -> 0x0022 }
            r3.append(r2)     // Catch:{ Exception -> 0x0022 }
            java.lang.String r1 = r3.toString()     // Catch:{ Exception -> 0x0022 }
            goto L_0x006e
        L_0x0084:
            byte[] r4 = android.util.Base64.decode(r1, r5)     // Catch:{ Exception -> 0x0022 }
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0022 }
            r5.<init>(r4)     // Catch:{ Exception -> 0x0022 }
            r0 = r5
            goto L_0x009a
        L_0x008f:
            io.dcloud.feature.weex.WeexInstanceMgr r4 = io.dcloud.feature.weex.WeexInstanceMgr.self()     // Catch:{ Exception -> 0x0022 }
            r4.setJSFKFileNotFound(r2)     // Catch:{ Exception -> 0x0022 }
            goto L_0x009a
        L_0x0097:
            r4.printStackTrace()
        L_0x009a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.weex.DCFileUtils.loadWeexAsset(java.lang.String, android.content.Context):java.io.InputStream");
    }

    public static String getAssetPath(String str) {
        String str2;
        if (!TextUtils.isEmpty(str) && str.equals("weex-main-jsfm.js")) {
            String str3 = WeexInstanceMgr.self().getVueVersion() == 3 ? "uni-jsframework-vue3" : "uni-jsframework";
            if (BaseInfo.SyncDebug) {
                str2 = str3 + "-dev.js";
            } else {
                str2 = str3 + ".js";
            }
            if (PlatformUtil.getResInputStream(str2) != null || !BaseInfo.SyncDebug) {
                str = str2;
            } else {
                str = str3 + ".js";
            }
            Logger.i("DCFileUtils", "getAssetPath---------" + str);
        }
        return str;
    }
}
