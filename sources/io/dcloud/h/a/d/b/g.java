package io.dcloud.h.a.d.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import io.dcloud.h.b.a;
import io.dcloud.sdk.base.entry.AdData;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class g {
    private static int a = 1;
    private static a b;
    private static SharedPreferences c;
    private static g d;
    private static Map<String, io.dcloud.sdk.base.entry.a> e = new ConcurrentHashMap();

    g() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:4|5|(2:7|8)|9|10) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0021 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private io.dcloud.h.b.a a(android.content.Context r6) {
        /*
            r5 = this;
            io.dcloud.h.b.a r0 = b
            if (r0 != 0) goto L_0x0026
            java.lang.Class<io.dcloud.h.a.d.b.g> r0 = io.dcloud.h.a.d.b.g.class
            monitor-enter(r0)
            io.dcloud.h.b.a r1 = b     // Catch:{ all -> 0x0023 }
            if (r1 != 0) goto L_0x0021
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x0021 }
            java.io.File r6 = r6.getFilesDir()     // Catch:{ IOException -> 0x0021 }
            java.lang.String r2 = "dcloudcache"
            r1.<init>(r6, r2)     // Catch:{ IOException -> 0x0021 }
            int r6 = a     // Catch:{ IOException -> 0x0021 }
            r2 = 1
            r3 = 10240(0x2800, double:5.059E-320)
            io.dcloud.h.b.a r6 = io.dcloud.h.b.a.a(r1, r6, r2, r3)     // Catch:{ IOException -> 0x0021 }
            b = r6     // Catch:{ IOException -> 0x0021 }
        L_0x0021:
            monitor-exit(r0)     // Catch:{ all -> 0x0023 }
            goto L_0x0026
        L_0x0023:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0023 }
            throw r6
        L_0x0026:
            io.dcloud.h.b.a r6 = b
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.a.d.b.g.a(android.content.Context):io.dcloud.h.b.a");
    }

    private static void c(Context context) {
        if (c == null) {
            synchronized (g.class) {
                if (c == null) {
                    c = context.getSharedPreferences("dcloudcache", 0);
                }
            }
        }
    }

    public Map<String, io.dcloud.sdk.base.entry.a> b(Context context) {
        c(context);
        Map<String, ?> all = c.getAll();
        if (all != null) {
            for (String next : all.keySet()) {
                if (!TextUtils.isEmpty(next) && !e.containsKey(next)) {
                    io.dcloud.sdk.base.entry.a aVar = new io.dcloud.sdk.base.entry.a(next);
                    aVar.a((String) all.get(next));
                    e.put(next, aVar);
                }
            }
        }
        return e;
    }

    public static g a() {
        if (d == null) {
            synchronized (g.class) {
                if (d == null) {
                    d = new g();
                    e.clear();
                }
            }
        }
        return d;
    }

    public void b(Context context, String str) {
        e.remove(str);
        c(context);
        SharedPreferences.Editor edit = c.edit();
        edit.remove(str);
        edit.apply();
        try {
            a(context).d(str);
        } catch (IOException unused) {
        }
    }

    private void a(Context context, String str, String str2) {
        c(context);
        SharedPreferences.Editor edit = c.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public void a(Context context, AdData adData, String str) {
        io.dcloud.sdk.base.entry.a aVar = new io.dcloud.sdk.base.entry.a(adData.k());
        aVar.a(adData.h());
        aVar.a(adData);
        e.put(adData.k(), aVar);
        try {
            a(context, adData.k(), adData.h());
            a.c a2 = a(context).a(adData.k());
            OutputStream a3 = a2.a(0);
            a3.write(str.getBytes());
            a3.close();
            a2.b();
            a(context).flush();
        } catch (Exception unused) {
        }
    }

    public String a(Context context, String str) {
        try {
            return a(context).b(str).b(0);
        } catch (Exception unused) {
            return null;
        }
    }
}
