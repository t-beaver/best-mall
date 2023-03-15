package io.dcloud.h.a.d.b;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import io.dcloud.h.c.c.a.a;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import java.lang.reflect.Method;

public class c {
    private static boolean a = false;
    private static String[] b = null;
    private static boolean c = false;
    private static String d = null;
    private static boolean e = false;
    private static String f = null;
    private static boolean g = false;
    private static String h = null;
    private static String i = "";
    private static boolean j = false;

    public static String a(Context context) {
        if (!PrivacyManager.getInstance().d()) {
            return f;
        }
        if (!a.a().a(context)) {
            return f;
        }
        if (PrivacyManager.getInstance().e()) {
            String c2 = PrivacyManager.getInstance().c();
            f = c2;
            return c2;
        }
        if (!e) {
            f = Settings.System.getString(context.getContentResolver(), "android_id");
            e = true;
        }
        return f;
    }

    private static String b(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String c(Context context) {
        if (context == null) {
            return i;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
            return i;
        }
        if (!PrivacyManager.getInstance().d()) {
            return i;
        }
        if (!a.a().a(context)) {
            return i;
        }
        if (j) {
            return i;
        }
        String str = null;
        if (a.a().a(context) && PrivacyManager.getInstance().d()) {
            try {
                str = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        i = str;
        j = true;
        return str;
    }

    public static String d(Context context) {
        String str;
        if (context == null) {
            return "";
        }
        try {
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") != 0) {
                return d;
            }
            if (!PrivacyManager.getInstance().d()) {
                return d;
            }
            if (!a.a().a(context)) {
                return d;
            }
            if (PrivacyManager.getInstance().e()) {
                String b2 = PrivacyManager.getInstance().b();
                d = b2;
                return b2;
            } else if (c) {
                return d;
            } else {
                int b3 = b(0, context);
                int b4 = b(1, context);
                if (b3 == -1 && b4 == -1) {
                    str = b(context);
                    c = true;
                    d = str;
                    return str;
                }
                String str2 = (String) a(b3, context);
                String str3 = (String) a(b4, context);
                if (!TextUtils.isEmpty(str2)) {
                    try {
                        if (!TextUtils.isEmpty(str3) && !str2.equals(str3)) {
                            str = str2 + "," + str3;
                        }
                    } catch (Exception unused) {
                    }
                    str = str2;
                } else {
                    str = !TextUtils.isEmpty(str3) ? str3 : b(context);
                }
                c = true;
                d = str;
                return str;
            }
        } catch (Exception unused2) {
            str = null;
        }
    }

    public static String e(Context context) {
        String str;
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_WIFI_STATE") != 0) {
            return h;
        }
        if (!PrivacyManager.getInstance().d()) {
            return h;
        }
        if (!a.a().a(context)) {
            return h;
        }
        if (g) {
            return h;
        }
        WifiInfo connectionInfo = ((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo();
        if (connectionInfo == null) {
            str = null;
        } else {
            str = connectionInfo.getMacAddress();
        }
        h = str;
        g = true;
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0062, code lost:
        if (a(r5) == false) goto L_0x0067;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] f(android.content.Context r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.String r1 = "android.permission.READ_PHONE_STATE"
            int r1 = androidx.core.content.ContextCompat.checkSelfPermission(r5, r1)
            if (r1 == 0) goto L_0x000f
            java.lang.String[] r5 = b
            return r5
        L_0x000f:
            io.dcloud.sdk.poly.base.utils.PrivacyManager r1 = io.dcloud.sdk.poly.base.utils.PrivacyManager.getInstance()
            boolean r1 = r1.d()
            if (r1 != 0) goto L_0x001c
            java.lang.String[] r5 = b
            return r5
        L_0x001c:
            io.dcloud.h.c.c.a.a r1 = io.dcloud.h.c.c.a.a.a()
            boolean r1 = r1.a((android.content.Context) r5)
            if (r1 != 0) goto L_0x0029
            java.lang.String[] r5 = b
            return r5
        L_0x0029:
            io.dcloud.sdk.poly.base.utils.PrivacyManager r1 = io.dcloud.sdk.poly.base.utils.PrivacyManager.getInstance()
            boolean r1 = r1.e()
            if (r1 == 0) goto L_0x003e
            io.dcloud.sdk.poly.base.utils.PrivacyManager r5 = io.dcloud.sdk.poly.base.utils.PrivacyManager.getInstance()
            java.lang.String[] r5 = r5.a()
            b = r5
            return r5
        L_0x003e:
            boolean r1 = a
            if (r1 == 0) goto L_0x0045
            java.lang.String[] r5 = b
            return r5
        L_0x0045:
            java.lang.String r1 = "phone"
            java.lang.Object r5 = r5.getSystemService(r1)
            android.telephony.TelephonyManager r5 = (android.telephony.TelephonyManager) r5
            r1 = 1
            java.lang.String r2 = r5.getDeviceId()     // Catch:{ Exception -> 0x0065 }
            java.lang.String r5 = r5.getDeviceId(r1)     // Catch:{ Exception -> 0x0065 }
            boolean r3 = a((java.lang.String) r2)     // Catch:{ Exception -> 0x0065 }
            if (r3 != 0) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r2 = r0
        L_0x005e:
            boolean r3 = a((java.lang.String) r5)     // Catch:{ Exception -> 0x0066 }
            if (r3 != 0) goto L_0x0066
            goto L_0x0067
        L_0x0065:
            r2 = r0
        L_0x0066:
            r5 = r0
        L_0x0067:
            boolean r3 = a((java.lang.String) r2)
            r4 = 0
            if (r3 != 0) goto L_0x007e
            boolean r3 = a((java.lang.String) r5)
            if (r3 != 0) goto L_0x007e
            r0 = 2
            java.lang.String[] r0 = new java.lang.String[r0]
            r0[r4] = r2
            r0[r1] = r5
            b = r0
            goto L_0x009a
        L_0x007e:
            boolean r3 = a((java.lang.String) r2)
            if (r3 != 0) goto L_0x008b
            java.lang.String[] r5 = new java.lang.String[r1]
            r5[r4] = r2
            b = r5
            goto L_0x009a
        L_0x008b:
            boolean r2 = a((java.lang.String) r5)
            if (r2 != 0) goto L_0x0098
            java.lang.String[] r0 = new java.lang.String[r1]
            r0[r4] = r5
            b = r0
            goto L_0x009a
        L_0x0098:
            b = r0
        L_0x009a:
            a = r1
            java.lang.String[] r5 = b
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.a.d.b.c.f(android.content.Context):java.lang.String[]");
    }

    private static int b(int i2, Context context) {
        Uri parse = Uri.parse("content://telephony/siminfo");
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            String[] strArr = {"_id", "sim_id"};
            cursor = contentResolver.query(parse, strArr, "sim_id = ?", new String[]{String.valueOf(i2)}, (String) null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor == null) {
                    return -1;
                }
                cursor.close();
                return -1;
            }
            int i3 = cursor.getInt(cursor.getColumnIndex("_id"));
            cursor.close();
            return i3;
        } catch (Exception unused) {
            if (cursor == null) {
                return -1;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private static Object a(int i2, Context context) {
        try {
            Object systemService = context.getSystemService("phone");
            int i3 = Build.VERSION.SDK_INT;
            if (i3 > 21) {
                return a(systemService.getClass().getName(), "getSubscriberId", systemService, new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i2)});
            }
            if (i3 == 21) {
                return a(systemService.getClass().getName(), "getSubscriberId", systemService, new Class[]{Long.TYPE}, new Object[]{Integer.valueOf(i2)});
            }
            return null;
        } catch (Exception unused) {
        }
    }

    private static Object a(String str, String str2, Object obj, Class[] clsArr, Object[] objArr) {
        try {
            Method method = Class.forName(str).getMethod(str2, clsArr);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(obj, objArr);
            }
        } catch (ClassNotFoundException | Exception | NoSuchMethodException unused) {
        }
        return null;
    }

    private static boolean a(String str) {
        return TextUtils.isEmpty(str) || str.contains("Unknown") || str.contains("00000000");
    }
}
