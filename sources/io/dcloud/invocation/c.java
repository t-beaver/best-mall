package io.dcloud.invocation;

import android.util.Log;
import io.dcloud.common.DHInterface.IWebview;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;

class c {
    a a = null;
    Class b = null;
    Object c = null;
    String d = null;

    public c(IWebview iWebview, a aVar, Class cls, String str, JSONArray jSONArray) throws Exception {
        this.a = aVar;
        this.b = cls;
        this.d = str;
        this.c = a(iWebview, aVar, cls, jSONArray);
    }

    public static Object a(IWebview iWebview, a aVar, Class cls, JSONArray jSONArray) throws Exception {
        Constructor constructor;
        if (jSONArray == null) {
            return cls.newInstance();
        }
        Object[] a2 = a(iWebview, aVar, jSONArray);
        int i = 0;
        Class[] clsArr = (Class[]) a2[0];
        Object[] objArr = (Object[]) a2[1];
        try {
            constructor = cls.getConstructor(clsArr);
        } catch (NoSuchMethodException unused) {
            Constructor[] constructors = cls.getConstructors();
            while (true) {
                if (i >= constructors.length) {
                    constructor = null;
                    break;
                }
                Class[] parameterTypes = constructors[i].getParameterTypes();
                if (parameterTypes.length == clsArr.length && a(parameterTypes, clsArr, objArr)) {
                    constructor = constructors[i];
                    break;
                }
                i++;
            }
        }
        if (constructor != null) {
            return constructor.newInstance(objArr);
        }
        return null;
    }

    public static void b(IWebview iWebview, a aVar, Class cls, Object obj, String str, JSONArray jSONArray) {
        Object[] a2 = a(iWebview, aVar, jSONArray);
        try {
            Field field = cls.getField(str);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, ((Object[]) a2[1])[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean c(Class cls, Class[] clsArr, int i) {
        boolean c2 = c(cls, clsArr[i]);
        if (c2) {
            clsArr[i] = cls;
        }
        return c2;
    }

    static boolean d(Class cls) {
        return Character.TYPE.equals(cls) || Character.class.equals(cls);
    }

    static boolean e(Class cls) {
        return cls.equals(Byte.TYPE) || cls.equals(Byte.class) || cls.equals(Integer.TYPE) || cls.equals(Integer.class) || cls.equals(Short.TYPE) || cls.equals(Short.class) || cls.equals(Float.TYPE) || cls.equals(Float.class) || cls.equals(Long.TYPE) || cls.equals(Long.class) || cls.equals(Double.TYPE) || cls.equals(Double.class);
    }

    public boolean equals(Object obj) {
        return super.equals(obj) || this.c.equals(obj);
    }

    static class a {
        private int[] a = null;

        a(int i) {
            this.a = new int[i];
        }

        /* access modifiers changed from: package-private */
        public int[] a(int i) {
            int[] iArr = this.a;
            if (i > iArr.length) {
                return null;
            }
            return Arrays.copyOfRange(iArr, iArr.length - i, iArr.length);
        }

        /* access modifiers changed from: package-private */
        public void a(int i, int i2) {
            int[] iArr = this.a;
            if (i <= iArr.length) {
                iArr[iArr.length - i] = i2;
            }
        }
    }

    static boolean c(Class cls, Class cls2) {
        return (Number.class.isAssignableFrom(cls) && Number.class.isAssignableFrom(cls2)) || (e(cls) && e(cls2));
    }

    static boolean c(Class cls) {
        return Boolean.TYPE.equals(cls) || Boolean.class.equals(cls);
    }

    private static Method b(Class cls, String str, Class[] clsArr, Object[] objArr) {
        int length;
        try {
            Method[] declaredMethods = cls.getDeclaredMethods();
            for (int i = 0; i < declaredMethods.length; i++) {
                Method method = declaredMethods[i];
                if (method.getName().equals(str) && (length = method.getParameterTypes().length) == clsArr.length && (length == 0 || a(method.getParameterTypes(), clsArr, objArr))) {
                    return method;
                }
            }
            if (cls != Object.class) {
                return b(cls.getSuperclass(), str, clsArr, objArr);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public c(a aVar, Class cls, String str, Object obj) {
        this.a = aVar;
        this.b = cls;
        this.d = str;
        this.c = obj;
    }

    public static Object a(Class cls, Object obj, String str) {
        try {
            Field field = cls.getField(str);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static Object[] b(Class cls) {
        Class<?> cls2;
        String name = cls.getName();
        Object[] objArr = new Object[2];
        Class<?> cls3 = null;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= name.length()) {
                break;
            }
            char charAt = name.charAt(i);
            if (charAt == '[') {
                i2++;
            } else if (charAt == 'B') {
                cls3 = Byte.TYPE;
            } else if (charAt == 'I') {
                cls3 = Integer.TYPE;
            } else if (charAt == 'F') {
                cls3 = Float.TYPE;
            } else if (charAt == 'D') {
                cls3 = Double.TYPE;
            } else if (charAt == 'Z') {
                cls3 = Boolean.TYPE;
            } else if (charAt == 'J') {
                cls3 = Long.TYPE;
            } else if (charAt == 'S') {
                cls3 = Short.TYPE;
            } else {
                if (charAt == 'L') {
                    try {
                        cls2 = Class.forName(name.substring(i + 1));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        cls2 = Class.forName(name);
                    } catch (ClassNotFoundException e2) {
                        e2.printStackTrace();
                    }
                }
                cls3 = cls2;
            }
            i++;
        }
        objArr[0] = Integer.valueOf(i2);
        objArr[1] = cls3;
        return objArr;
    }

    /* access modifiers changed from: package-private */
    public String a(IWebview iWebview, a aVar) {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Field[] fields = this.b.getFields();
        for (Field a2 : fields) {
            d.a(iWebview, aVar, this.c, a2, false, stringBuffer, stringBuffer2);
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: package-private */
    public Object a(IWebview iWebview, String str, JSONArray jSONArray) throws Exception {
        return a(iWebview, this.a, this.b, this.c, str, jSONArray);
    }

    public static final Object a(IWebview iWebview, a aVar, Class cls, Object obj, String str, JSONArray jSONArray) throws Exception {
        Object[] a2 = a(iWebview, aVar, jSONArray);
        Object[] objArr = (Object[]) a2[1];
        return a(obj, a(cls, str, (Class[]) a2[0], objArr), objArr, true);
    }

    private static final Object a(Object obj, Method method, Object[] objArr, boolean z) throws Exception {
        try {
            if (!method.getReturnType().equals(Void.class)) {
                return method.invoke(obj, objArr);
            }
            method.invoke(obj, objArr);
            return null;
        } catch (IllegalArgumentException e) {
            if (z) {
                Class[] parameterTypes = method.getParameterTypes();
                if (!(parameterTypes == null || objArr == null || parameterTypes.length != objArr.length)) {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (objArr[i] != null) {
                            Class<?> cls = objArr[i].getClass();
                            Class cls2 = parameterTypes[i];
                            if (cls.isAssignableFrom(cls2)) {
                                continue;
                            } else if (!e(cls) || !e(cls2)) {
                                throw e;
                            } else {
                                objArr[i] = a(objArr[i], cls2);
                            }
                        }
                    }
                    return a(obj, method, objArr, false);
                }
            } else {
                throw e;
            }
        }
    }

    static Method a(Class cls, String str, Class[] clsArr, Object[] objArr) {
        boolean equals = "getClass".equals(str);
        Class cls2 = cls;
        while (true) {
            if (!equals && cls2 == Object.class) {
                return b(cls, str, clsArr, objArr);
            }
            try {
                Method method = cls.getMethod(str, clsArr);
                if (method != null) {
                    method.setAccessible(true);
                    return method;
                }
                cls2 = cls2.getSuperclass();
            } catch (NoSuchMethodException unused) {
            }
        }
    }

    static boolean b(Class cls, Class cls2) {
        return d(cls) && d(cls2);
    }

    static boolean b(Class cls, Class[] clsArr, int i) {
        boolean b2 = b(cls, clsArr[i]);
        if (b2) {
            clsArr[i] = cls;
        }
        return b2;
    }

    public static boolean a(Class[] clsArr, Class[] clsArr2, Object[] objArr) {
        boolean z = false;
        for (int i = 0; i < clsArr.length; i++) {
            Class cls = clsArr2[i];
            Class cls2 = clsArr[i];
            z = cls == null ? !(e(cls2) || Boolean.TYPE.equals(cls2) || Boolean.class.equals(cls2) || Character.TYPE.equals(cls2) || Character.class.equals(cls2)) : !(!cls2.isAssignableFrom(cls) && !c(cls2, clsArr2, i) && !a(cls2, clsArr2, i) && !b(cls2, clsArr2, i) && (!cls2.isArray() || !cls2.isArray() || !a(cls2, clsArr2, objArr, i)));
            if (!z) {
                return z;
            }
        }
        return z;
    }

    static boolean a(Class cls, Class[] clsArr, Object[] objArr, int i) {
        if (cls == clsArr[i]) {
            return true;
        }
        Object[] b2 = b(cls);
        Object[] b3 = b(clsArr[i]);
        int parseInt = Integer.parseInt(String.valueOf(b2[0]));
        Class cls2 = (Class) b2[1];
        int parseInt2 = Integer.parseInt(String.valueOf(b3[0]));
        Class cls3 = (Class) b3[1];
        if (parseInt == parseInt2 && (a(cls2, cls3) || b(cls2, cls3) || c(cls2, cls3) || cls3.isAssignableFrom(cls2))) {
            try {
                Object a2 = a(objArr[i], cls2, parseInt, new a(parseInt));
                if (a2 != null) {
                    clsArr[i] = cls;
                    objArr[i] = a2;
                    return true;
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    static Object a(Object obj, Class cls, int i, a aVar) throws Exception {
        if (!obj.getClass().isArray()) {
            return a(obj, cls);
        }
        int length = Array.getLength(obj);
        Object obj2 = null;
        for (int i2 = 0; i2 < length; i2++) {
            try {
                Object a2 = a(Array.get(obj, i2), cls, i - 1, aVar);
                if (obj2 == null) {
                    aVar.a(i, length);
                    if (i == 1) {
                        obj2 = Array.newInstance(cls, length);
                    } else {
                        obj2 = Array.newInstance(cls, aVar.a(i));
                    }
                }
                Array.set(obj2, i2, a2);
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("test", "i=" + i2 + ";mAw=" + i);
            }
        }
        return obj2;
    }

    static boolean a(Class cls, Class cls2) {
        return c(cls) && c(cls2);
    }

    static boolean a(Class cls, Class[] clsArr, int i) {
        boolean a2 = a(cls, clsArr[i]);
        if (a2) {
            clsArr[i] = cls;
        }
        return a2;
    }

    static Object a(Object obj, Class cls) {
        if (cls == obj.getClass()) {
            return obj;
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return Byte.valueOf(Byte.parseByte(String.valueOf(obj)));
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return Integer.valueOf(Integer.parseInt(String.valueOf(obj)));
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return Boolean.valueOf(Boolean.parseBoolean(String.valueOf(obj)));
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return Short.valueOf(Short.parseShort(String.valueOf(obj)));
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return Long.valueOf(Long.parseLong(String.valueOf(obj)));
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return Double.valueOf(Double.parseDouble(String.valueOf(obj)));
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return Float.valueOf(Float.parseFloat(String.valueOf(obj)));
        }
        if (cls == Character.TYPE || cls == Character.class) {
            return String.valueOf(obj);
        }
        return cls.cast(obj);
    }

    static Class a(Class cls) {
        if (cls == Integer.class) {
            return Integer.TYPE;
        }
        return cls == Boolean.class ? Boolean.TYPE : cls;
    }

    static Object[] a(IWebview iWebview, a aVar, Object obj) {
        Object[] objArr = new Object[2];
        if (obj instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) obj;
            int length = jSONArray.length();
            Object obj2 = null;
            for (int i = 0; i < length; i++) {
                Object[] a2 = a(iWebview, aVar, jSONArray.opt(i));
                Class<?> cls = a2[1].getClass();
                if (i == 0) {
                    obj2 = Array.newInstance(a(a2[1].getClass()), length);
                }
                if (cls == Boolean.class) {
                    Array.setBoolean(obj2, i, ((Boolean) a2[1]).booleanValue());
                } else if (cls == Byte.class) {
                    Array.setByte(obj2, i, ((Byte) a2[1]).byteValue());
                } else if (cls == Double.class) {
                    Array.setDouble(obj2, i, ((Double) a2[1]).doubleValue());
                } else if (cls == Float.class) {
                    Array.setFloat(obj2, i, ((Float) a2[1]).floatValue());
                } else if (cls == Integer.class) {
                    Array.setInt(obj2, i, ((Integer) a2[1]).intValue());
                } else if (cls == Long.class) {
                    Array.setLong(obj2, i, ((Long) a2[1]).longValue());
                } else if (cls instanceof Object) {
                    Array.set(obj2, i, a2[1]);
                }
            }
            if (obj2 != null) {
                objArr[0] = obj2.getClass();
                objArr[1] = obj2;
            }
        } else if (obj instanceof JSONObject) {
            JSONObject jSONObject = (JSONObject) obj;
            String optString = jSONObject.optString("type");
            Object opt = jSONObject.opt("value");
            if (optString.equals("object")) {
                c a3 = aVar.a(iWebview, String.valueOf(opt));
                objArr[0] = a3.b;
                objArr[1] = a3.c;
            } else if (optString.equals("string")) {
                objArr[0] = String.class;
                objArr[1] = opt;
            } else if (optString.equals("number")) {
                if (opt instanceof Integer) {
                    objArr[0] = Integer.TYPE;
                    objArr[1] = Integer.valueOf(((Integer) opt).intValue());
                } else if (opt instanceof Double) {
                    objArr[0] = Double.class;
                    objArr[1] = (Double) opt;
                } else if (opt instanceof Float) {
                    objArr[0] = Float.class;
                    objArr[1] = (Float) opt;
                } else if (opt instanceof Long) {
                    objArr[0] = Long.class;
                    objArr[1] = (Long) opt;
                }
            } else if (optString.equals("boolean")) {
                objArr[0] = Boolean.class;
                objArr[1] = (Boolean) opt;
            } else if ("JSBObject".equals(jSONObject.optString("__TYPE__"))) {
                c a4 = aVar.a(iWebview, jSONObject.optString(AbsoluteConst.JSON_KEY_UUID));
                objArr[0] = a4.b;
                objArr[1] = a4.c;
            }
        } else {
            objArr[0] = obj.getClass();
            objArr[1] = obj;
        }
        return objArr;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.Object[] a(io.dcloud.common.DHInterface.IWebview r9, io.dcloud.invocation.a r10, org.json.JSONArray r11) {
        /*
            r0 = 2
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1 = 0
            if (r11 == 0) goto L_0x000b
            int r2 = r11.length()     // Catch:{ JSONException -> 0x002e }
            goto L_0x000c
        L_0x000b:
            r2 = 0
        L_0x000c:
            java.lang.Class[] r3 = new java.lang.Class[r2]     // Catch:{ JSONException -> 0x002e }
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ JSONException -> 0x002e }
            r5 = 0
        L_0x0011:
            r6 = 1
            if (r5 >= r2) goto L_0x0029
            java.lang.Object r7 = r11.get(r5)     // Catch:{ JSONException -> 0x002e }
            java.lang.Object[] r7 = a((io.dcloud.common.DHInterface.IWebview) r9, (io.dcloud.invocation.a) r10, (java.lang.Object) r7)     // Catch:{ JSONException -> 0x002e }
            r8 = r7[r1]     // Catch:{ JSONException -> 0x002e }
            java.lang.Class r8 = (java.lang.Class) r8     // Catch:{ JSONException -> 0x002e }
            r3[r5] = r8     // Catch:{ JSONException -> 0x002e }
            r6 = r7[r6]     // Catch:{ JSONException -> 0x002e }
            r4[r5] = r6     // Catch:{ JSONException -> 0x002e }
            int r5 = r5 + 1
            goto L_0x0011
        L_0x0029:
            r0[r1] = r3     // Catch:{ JSONException -> 0x002e }
            r0[r6] = r4     // Catch:{ JSONException -> 0x002e }
            goto L_0x0032
        L_0x002e:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0032:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.invocation.c.a(io.dcloud.common.DHInterface.IWebview, io.dcloud.invocation.a, org.json.JSONArray):java.lang.Object[]");
    }

    public void a() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
    }
}
