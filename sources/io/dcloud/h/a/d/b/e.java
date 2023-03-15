package io.dcloud.h.a.d.b;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import io.dcloud.h.c.c.a.a;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class e {
    private static JSONObject a = new JSONObject();
    private static boolean b = false;
    private static Map<String, Integer> c = new HashMap();
    private static boolean d = false;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v8, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: android.location.Location} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.json.JSONObject a(android.content.Context r9) {
        /*
            if (r9 != 0) goto L_0x0005
            org.json.JSONObject r9 = a
            return r9
        L_0x0005:
            java.lang.String r0 = "android.permission.ACCESS_FINE_LOCATION"
            int r0 = androidx.core.content.ContextCompat.checkSelfPermission(r9, r0)
            if (r0 == 0) goto L_0x0018
            java.lang.String r0 = "android.permission.ACCESS_COARSE_LOCATION"
            int r0 = androidx.core.content.ContextCompat.checkSelfPermission(r9, r0)
            if (r0 == 0) goto L_0x0018
            org.json.JSONObject r9 = a
            return r9
        L_0x0018:
            io.dcloud.sdk.poly.base.utils.PrivacyManager r0 = io.dcloud.sdk.poly.base.utils.PrivacyManager.getInstance()
            boolean r0 = r0.d()
            if (r0 != 0) goto L_0x0025
            org.json.JSONObject r9 = a
            return r9
        L_0x0025:
            io.dcloud.h.c.c.a.a r0 = io.dcloud.h.c.c.a.a.a()
            boolean r0 = r0.a((android.content.Context) r9)
            if (r0 != 0) goto L_0x0032
            org.json.JSONObject r9 = a
            return r9
        L_0x0032:
            boolean r0 = b
            if (r0 == 0) goto L_0x0039
            org.json.JSONObject r9 = a
            return r9
        L_0x0039:
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>()
            java.lang.String r1 = "location"
            java.lang.Object r9 = r9.getSystemService(r1)
            android.location.LocationManager r9 = (android.location.LocationManager) r9
            r1 = 1
            if (r9 == 0) goto L_0x00f6
            java.lang.Class r2 = r9.getClass()     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r3 = "getLastKnownLocation"
            java.lang.Class[] r4 = new java.lang.Class[r1]     // Catch:{ Exception -> 0x00f6 }
            java.lang.Class<java.lang.String> r5 = java.lang.String.class
            r6 = 0
            r4[r6] = r5     // Catch:{ Exception -> 0x00f6 }
            java.lang.reflect.Method r2 = r2.getDeclaredMethod(r3, r4)     // Catch:{ Exception -> 0x00f6 }
            r2.setAccessible(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r4 = "gps"
            r3[r6] = r4     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r3 = r2.invoke(r9, r3)     // Catch:{ Exception -> 0x00f6 }
            android.location.Location r3 = (android.location.Location) r3     // Catch:{ Exception -> 0x00f6 }
            if (r3 != 0) goto L_0x0086
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r4 = "network"
            r3[r6] = r4     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r3 = r2.invoke(r9, r3)     // Catch:{ Exception -> 0x00f6 }
            android.location.Location r3 = (android.location.Location) r3     // Catch:{ Exception -> 0x00f6 }
            if (r3 != 0) goto L_0x0086
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r4 = "passive"
            r3[r6] = r4     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r9 = r2.invoke(r9, r3)     // Catch:{ Exception -> 0x00f6 }
            r3 = r9
            android.location.Location r3 = (android.location.Location) r3     // Catch:{ Exception -> 0x00f6 }
        L_0x0086:
            if (r3 == 0) goto L_0x00f6
            java.lang.String r9 = "android.location.Location"
            java.lang.Class r9 = java.lang.Class.forName(r9)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r2 = "getLongitude"
            java.lang.Class[] r4 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.reflect.Method r2 = r9.getMethod(r2, r4)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r4 = "getLatitude"
            java.lang.Class[] r5 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.reflect.Method r4 = r9.getMethod(r4, r5)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r5 = "getAccuracy"
            java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.reflect.Method r5 = r9.getMethod(r5, r7)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r7 = "getTime"
            java.lang.Class[] r8 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.reflect.Method r9 = r9.getMethod(r7, r8)     // Catch:{ Exception -> 0x00f6 }
            r2.setAccessible(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r7 = "lon"
            java.lang.Object[] r8 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r2 = r2.invoke(r3, r8)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x00f6 }
            r0.put(r7, r2)     // Catch:{ Exception -> 0x00f6 }
            r4.setAccessible(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r2 = "lat"
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r4 = r4.invoke(r3, r7)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x00f6 }
            r0.put(r2, r4)     // Catch:{ Exception -> 0x00f6 }
            r5.setAccessible(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r2 = "accuracy"
            java.lang.Object[] r4 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r4 = r5.invoke(r3, r4)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Exception -> 0x00f6 }
            r0.put(r2, r4)     // Catch:{ Exception -> 0x00f6 }
            r9.setAccessible(r1)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r2 = "ts"
            java.lang.Object[] r4 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00f6 }
            java.lang.Object r9 = r9.invoke(r3, r4)     // Catch:{ Exception -> 0x00f6 }
            java.lang.String r9 = java.lang.String.valueOf(r9)     // Catch:{ Exception -> 0x00f6 }
            r0.put(r2, r9)     // Catch:{ Exception -> 0x00f6 }
        L_0x00f6:
            a = r0
            b = r1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.a.d.b.e.a(android.content.Context):org.json.JSONObject");
    }

    public static Map<String, Integer> a(Context context, String str) {
        int i;
        int i2;
        if (context == null) {
            return c;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            return c;
        }
        if (!PrivacyManager.getInstance().d()) {
            return c;
        }
        if (!a.a().a(context)) {
            return c;
        }
        if (d) {
            return c;
        }
        int i3 = 0;
        if (TextUtils.isEmpty(str) || str.equals("10")) {
            i2 = 0;
            i = 0;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if ("03".equals(str) || "05".equals(str)) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
                i3 = cdmaCellLocation.getNetworkId();
                i2 = cdmaCellLocation.getBaseStationId() / 16;
                i = 2;
            } else {
                int i4 = ("01".equals(str) || "06".equals(str)) ? 3 : ("00".equals(str) || "02".equals(str) || "07".equals(str)) ? 1 : 4;
                GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                int lac = gsmCellLocation.getLac();
                i2 = gsmCellLocation.getCid();
                int i5 = i4;
                i3 = lac;
                i = i5;
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("carrier", Integer.valueOf(i));
        hashMap.put("cid", Integer.valueOf(i2));
        hashMap.put("lac", Integer.valueOf(i3));
        c = hashMap;
        d = true;
        return hashMap;
    }
}
