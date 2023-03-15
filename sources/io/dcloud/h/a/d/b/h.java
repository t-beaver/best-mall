package io.dcloud.h.a.d.b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import io.dcloud.h.a.e.e;
import io.dcloud.h.c.c.a.a;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import org.json.JSONObject;

public class h {
    private static String a = "0";
    private static String b = "1";
    private static String c = "3";
    private static String d = "4";
    private static String e = "5";
    private static String f = "6";
    private static String g = "7";
    private static boolean h = false;
    private static String i = "1";

    public static JSONObject a(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("device", b(context));
            jSONObject.put("net", c(context));
            jSONObject.put("gps", e.a(context));
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(55:1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|(1:25)|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|(1:45)(1:44)|46|47|48|(12:50|53|54|55|(1:57)|58|(1:60)(1:61)|62|63|64|65|68)|51|53|54|55|(0)|58|(0)(0)|62|63|64|65) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0054 */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ad A[Catch:{ Exception -> 0x00fe }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00b4 A[Catch:{ Exception -> 0x00fe }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c4 A[Catch:{ Exception -> 0x00cd }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00dd A[Catch:{ Exception -> 0x00fe }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00ed A[Catch:{ Exception -> 0x00fe }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ee A[Catch:{ Exception -> 0x00fe }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.json.JSONObject b(android.content.Context r7) {
        /*
            java.lang.String r0 = io.dcloud.h.a.d.b.c.c(r7)
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            java.lang.String r2 = "type"
            r3 = 1
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "os"
            java.lang.String r3 = "Android"
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "osv"
            java.lang.String r3 = android.os.Build.VERSION.RELEASE     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "vendor"
            java.lang.String r3 = android.os.Build.MANUFACTURER     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "model"
            java.lang.String r3 = android.os.Build.MODEL     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "w"
            android.content.res.Resources r3 = r7.getResources()     // Catch:{ Exception -> 0x00fe }
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()     // Catch:{ Exception -> 0x00fe }
            int r3 = r3.widthPixels     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "h"
            android.content.res.Resources r3 = r7.getResources()     // Catch:{ Exception -> 0x00fe }
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()     // Catch:{ Exception -> 0x00fe }
            int r3 = r3.heightPixels     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "ip"
            java.lang.String r3 = io.dcloud.h.a.d.b.d.a((android.content.Context) r7)     // Catch:{ Exception -> 0x0054 }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x0054 }
        L_0x0054:
            java.lang.String r2 = io.dcloud.h.a.d.b.c.e(r7)     // Catch:{ Exception -> 0x00fe }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r4 = ""
            if (r3 == 0) goto L_0x0061
            r2 = r4
        L_0x0061:
            java.lang.String r3 = "mac"
            r1.put(r3, r2)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "dpi"
            android.content.res.Resources r3 = r7.getResources()     // Catch:{ Exception -> 0x00fe }
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()     // Catch:{ Exception -> 0x00fe }
            int r3 = r3.densityDpi     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "density"
            android.content.res.Resources r3 = r7.getResources()     // Catch:{ Exception -> 0x00fe }
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()     // Catch:{ Exception -> 0x00fe }
            float r3 = r3.density     // Catch:{ Exception -> 0x00fe }
            double r5 = (double) r3     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r5)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "lan"
            java.util.Locale r3 = java.util.Locale.getDefault()     // Catch:{ Exception -> 0x00fe }
            java.lang.String r3 = r3.getLanguage()     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "country"
            java.util.Locale r3 = java.util.Locale.getDefault()     // Catch:{ Exception -> 0x00fe }
            java.lang.String r3 = r3.getCountry()     // Catch:{ Exception -> 0x00fe }
            r1.put(r2, r3)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r2 = "iccid"
            r1.put(r2, r0)     // Catch:{ Exception -> 0x00fe }
            java.lang.String[] r0 = io.dcloud.h.a.d.b.c.f(r7)     // Catch:{ Exception -> 0x00fe }
            if (r0 == 0) goto L_0x00b4
            int r2 = r0.length     // Catch:{ Exception -> 0x00fe }
            if (r2 <= 0) goto L_0x00b4
            java.lang.String r2 = ","
            java.lang.String r0 = android.text.TextUtils.join(r2, r0)     // Catch:{ Exception -> 0x00fe }
            goto L_0x00b5
        L_0x00b4:
            r0 = r4
        L_0x00b5:
            java.lang.String r2 = "imei"
            r1.put(r2, r0)     // Catch:{ Exception -> 0x00fe }
            io.dcloud.sdk.poly.base.utils.PrivacyManager r0 = io.dcloud.sdk.poly.base.utils.PrivacyManager.getInstance()     // Catch:{ Exception -> 0x00cd }
            boolean r0 = r0.d()     // Catch:{ Exception -> 0x00cd }
            if (r0 == 0) goto L_0x00cd
            io.dcloud.h.a.d.b.f r0 = io.dcloud.h.a.d.b.f.a()     // Catch:{ Exception -> 0x00cd }
            java.lang.String r0 = r0.c(r7)     // Catch:{ Exception -> 0x00cd }
            goto L_0x00ce
        L_0x00cd:
            r0 = r4
        L_0x00ce:
            java.lang.String r2 = "oaid"
            r1.put(r2, r0)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r0 = io.dcloud.h.a.d.b.c.a((android.content.Context) r7)     // Catch:{ Exception -> 0x00fe }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00fe }
            if (r2 == 0) goto L_0x00de
            r0 = r4
        L_0x00de:
            java.lang.String r2 = "aid"
            r1.put(r2, r0)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r0 = io.dcloud.h.a.d.b.c.d(r7)     // Catch:{ Exception -> 0x00fe }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x00fe }
            if (r2 == 0) goto L_0x00ee
            goto L_0x00ef
        L_0x00ee:
            r4 = r0
        L_0x00ef:
            java.lang.String r0 = "imsi"
            r1.put(r0, r4)     // Catch:{ Exception -> 0x00fe }
            java.lang.String r0 = "ua"
            java.lang.String r7 = e(r7)     // Catch:{ Exception -> 0x00fe }
            r1.put(r0, r7)     // Catch:{ Exception -> 0x00fe }
            goto L_0x0102
        L_0x00fe:
            r7 = move-exception
            r7.printStackTrace()
        L_0x0102:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.a.d.b.h.b(android.content.Context):org.json.JSONObject");
    }

    public static JSONObject c(Context context) {
        String str = "460";
        String str2 = "10";
        if (a.a().a(context) && PrivacyManager.getInstance().d()) {
            try {
                String networkOperator = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
                if (!TextUtils.isEmpty(networkOperator) && networkOperator.length() == 5) {
                    str = networkOperator.substring(0, 3);
                    str2 = networkOperator.substring(3);
                }
            } catch (Exception unused) {
            }
        }
        JSONObject jSONObject = new JSONObject(e.a(context, str2));
        try {
            jSONObject.put("type", d(context));
            jSONObject.put("mcc", str);
            jSONObject.put("mnc", str2);
        } catch (Exception unused2) {
        }
        return jSONObject;
    }

    public static String d(Context context) {
        if (!a.a().a(context)) {
            return i;
        }
        if (!PrivacyManager.getInstance().d()) {
            return i;
        }
        if (h) {
            return i;
        }
        String str = b;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (!(connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null)) {
            str = a;
            if (connectivityManager.getActiveNetworkInfo().getType() != 1) {
                if (connectivityManager.getActiveNetworkInfo().getType() == 0) {
                    int subtype = connectivityManager.getActiveNetworkInfo().getSubtype();
                    switch (subtype) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                            str = d;
                            break;
                        case 3:
                        case 8:
                            str = e;
                            break;
                        case 5:
                        case 6:
                        case 12:
                        case 14:
                            str = e;
                            break;
                        case 9:
                        case 10:
                        case 11:
                        case 13:
                        case 15:
                            str = f;
                            break;
                        case 17:
                        case 18:
                            str = e;
                            break;
                        case 20:
                            str = g;
                            break;
                        default:
                            str = "" + subtype;
                            break;
                    }
                }
            } else {
                str = c;
            }
        }
        i = str;
        h = true;
        return str;
    }

    public static String e(Context context) {
        String a2 = e.a(context, "dcloud-ads", "u-a");
        if (!TextUtils.isEmpty(a2) || !a.a().a(context) || !PrivacyManager.getInstance().d()) {
            return a2;
        }
        try {
            WebView webView = new WebView(context);
            WebSettings settings = webView.getSettings();
            settings.setSavePassword(false);
            a2 = settings.getUserAgentString();
            webView.destroy();
            e.a(context, "dcloud-ads", "u-a", a2);
            return a2;
        } catch (Throwable unused) {
            return a2;
        }
    }
}
