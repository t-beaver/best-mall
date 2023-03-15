package io.dcloud.sdk.core.util;

import com.taobao.weex.common.RenderTypes;
import io.dcloud.sdk.poly.base.utils.e;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    public static Object invokeField(String str, String str2) {
        try {
            Class<?> cls = Class.forName(str);
            Field declaredField = cls.getDeclaredField(str2);
            declaredField.setAccessible(true);
            return declaredField.get(cls);
        } catch (Exception unused) {
            return null;
        }
    }

    public static Object invokeMethod(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        if (obj == null) {
            return null;
        }
        try {
            Method method = obj.getClass().getMethod(str, clsArr);
            method.setAccessible(true);
            if (objArr == null || objArr.length == 0) {
                objArr = null;
            }
            return method.invoke(obj, objArr);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static Object newInstance(String str, Class[] clsArr, Object[] objArr) {
        try {
            return Class.forName(str).getConstructor(clsArr).newInstance(objArr);
        } catch (Throwable th) {
            e.a(RenderTypes.RENDER_TYPE_NATIVE, th.toString());
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0026  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object invokeMethod(java.lang.String r2, java.lang.String r3, java.lang.Object r4, java.lang.Class[] r5, java.lang.Object[] r6) {
        /*
            r0 = 0
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x001f, NoSuchMethodException -> 0x001c, Exception -> 0x0016 }
            java.lang.reflect.Method r2 = r2.getMethod(r3, r5)     // Catch:{ ClassNotFoundException -> 0x001f, NoSuchMethodException -> 0x001c, Exception -> 0x0016 }
            if (r2 == 0) goto L_0x0014
            r5 = 1
            r2.setAccessible(r5)     // Catch:{ ClassNotFoundException -> 0x001f, NoSuchMethodException -> 0x001c, Exception -> 0x0016 }
            java.lang.Object r2 = r2.invoke(r4, r6)     // Catch:{ ClassNotFoundException -> 0x001f, NoSuchMethodException -> 0x001c, Exception -> 0x0016 }
            goto L_0x0024
        L_0x0014:
            r2 = r0
            goto L_0x0024
        L_0x0016:
            r2 = move-exception
            java.lang.String r2 = r2.getMessage()
            goto L_0x0021
        L_0x001c:
            java.lang.String r2 = "NoSuchMethodException"
            goto L_0x0021
        L_0x001f:
            java.lang.String r2 = "ClassNotFoundException"
        L_0x0021:
            r1 = r0
            r0 = r2
            r2 = r1
        L_0x0024:
            if (r0 == 0) goto L_0x002b
            java.lang.String r4 = "getJsContent"
            r4.equals(r3)
        L_0x002b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.sdk.core.util.ReflectUtil.invokeMethod(java.lang.String, java.lang.String, java.lang.Object, java.lang.Class[], java.lang.Object[]):java.lang.Object");
    }

    public static Object invokeMethod(String str, String str2, Object obj) {
        return invokeMethod(str, str2, obj, new Class[0], new Object[0]);
    }
}
