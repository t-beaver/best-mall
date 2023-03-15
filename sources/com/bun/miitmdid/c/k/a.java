package com.bun.miitmdid.c.k;

import android.content.Context;
import java.lang.reflect.Method;

public class a {
    private static Object a;
    private static Class<?> b;
    private static Method c;
    private static Method d;
    private static Method e;

    static {
        try {
            Class<?> cls = Class.forName("com.android.id.impl.IdProviderImpl");
            b = cls;
            a = cls.newInstance();
        } catch (Exception e2) {
            com.bun.lib.a.a("IdentifierManager", "reflect exception!", e2);
        }
        try {
            Class<?> cls2 = b;
            if (cls2 != null) {
                c = cls2.getMethod("getOAID", new Class[]{Context.class});
            }
        } catch (Exception e3) {
            com.bun.lib.a.a("IdentifierManager", "reflect exception!", e3);
        }
        try {
            Class<?> cls3 = b;
            if (cls3 != null) {
                d = cls3.getMethod("getVAID", new Class[]{Context.class});
            }
        } catch (Exception e4) {
            com.bun.lib.a.a("IdentifierManager", "reflect exception!", e4);
        }
        try {
            Class<?> cls4 = b;
            if (cls4 != null) {
                e = cls4.getMethod("getAAID", new Class[]{Context.class});
            }
        } catch (Exception e5) {
            com.bun.lib.a.a("IdentifierManager", "reflect exception!", e5);
        }
    }

    public static native String a(Context context);

    private static native String a(Context context, Method method);

    public static native boolean a();

    public static native String b(Context context);

    public static native String c(Context context);
}
