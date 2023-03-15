package io.dcloud.invocation;

import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.StringUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class d {
    static HashMap<String, Class> a = new HashMap<>(2);
    static HashMap<Class, String> b = new HashMap<>(2);
    private static String c = "cache/invCache/";
    private static String d = "cache/invCache_temp/";

    public static Class a(String str) {
        Class<?> cls = a.get(str);
        if (cls != null) {
            return cls;
        }
        try {
            cls = Class.forName(str);
            a.put(str, cls);
            return cls;
        } catch (ClassNotFoundException unused) {
            return cls;
        }
    }

    public static String b(String str) {
        return c(a(str));
    }

    public static String c(Class cls) {
        String str = b.get(cls);
        if (str != null) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(Operators.ARRAY_START_STR);
        stringBuffer.append(JSUtil.QUOTE);
        stringBuffer.append(cls.getName());
        stringBuffer.append(JSUtil.QUOTE);
        stringBuffer.append(",");
        for (Class<? super Object> superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            stringBuffer.append(JSUtil.QUOTE);
            stringBuffer.append(superclass.getName());
            stringBuffer.append(JSUtil.QUOTE);
            stringBuffer.append(",");
            if (superclass == Object.class) {
                break;
            }
        }
        a(stringBuffer);
        stringBuffer.append(Operators.ARRAY_END_STR);
        String stringBuffer2 = stringBuffer.toString();
        b.put(cls, stringBuffer2);
        return Deprecated_JSUtil.wrapJsVar(stringBuffer2, false);
    }

    public static String b(Class cls) {
        return cls.getName();
    }

    public static void a(StringBuffer stringBuffer, ArrayList arrayList, StringBuffer stringBuffer2, ArrayList arrayList2, Class cls) {
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                if (Modifier.isStatic(modifiers) && !arrayList2.contains(name) && !name.contains(Operators.SUB) && !"toString".equals(name) && !"valueOf".equals(name) && !"propertyIsEnumerable".equals(name) && !"hasOwnProperty".equals(name) && !"isPrototypeOf".equals(name) && !"constructor".equals(name) && !"toLocaleString".equals(name)) {
                    arrayList2.add(name);
                    stringBuffer2.append(JSUtil.QUOTE + name + JSUtil.QUOTE);
                    stringBuffer2.append(",");
                }
                if (!arrayList.contains(name) && !name.contains(Operators.SUB)) {
                    arrayList.add(name);
                    stringBuffer.append(JSUtil.QUOTE + name + JSUtil.QUOTE);
                    stringBuffer.append(",");
                }
            }
        }
    }

    public static void a(IWebview iWebview, a aVar, Object obj, Field field, boolean z, StringBuffer stringBuffer, StringBuffer stringBuffer2) {
        String name = field.getName();
        int modifiers = field.getModifiers();
        if ((z && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) || (!z && !Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))) {
            try {
                a(iWebview, aVar, field.getType(), field.get(obj), name, z, stringBuffer, stringBuffer2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void a(IWebview iWebview, a aVar, Class cls, Object obj, String str, boolean z, StringBuffer stringBuffer, StringBuffer stringBuffer2) {
        if (z) {
            stringBuffer.append(JSUtil.QUOTE + str + JSUtil.QUOTE);
            a(iWebview, aVar, obj, cls, stringBuffer2);
            stringBuffer.append(",");
            stringBuffer2.append(",");
            return;
        }
        stringBuffer.append("self.");
        stringBuffer.append(str);
        stringBuffer.append("=");
        a(iWebview, aVar, obj, cls, stringBuffer2);
        stringBuffer.append(stringBuffer2.toString());
        stringBuffer.append(";");
        stringBuffer2.delete(0, stringBuffer2.length());
    }

    public static void a(IWebview iWebview, a aVar, Object obj, Class cls, StringBuffer stringBuffer) {
        if (obj == null) {
            stringBuffer.append("null");
        } else if (a(cls)) {
            stringBuffer.append(String.valueOf(obj));
        } else if (cls.equals(String.class) || cls.equals(Character.class)) {
            String valueOf = String.valueOf(obj);
            String jsResponseText = JSUtil.toJsResponseText(JSUtil.QUOTE + valueOf + JSUtil.QUOTE);
            stringBuffer.append(JSUtil.QUOTE + jsResponseText + JSUtil.QUOTE);
        } else {
            int a2 = a(obj);
            if (a2 > -1) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append(Operators.ARRAY_START_STR);
                for (int i = 0; i < a2; i++) {
                    Object a3 = a(Array.get(obj, i), obj.getClass());
                    a(iWebview, aVar, a3, (Class) a3.getClass(), stringBuffer2);
                    stringBuffer2.append(",");
                }
                a(stringBuffer2);
                stringBuffer2.append(Operators.ARRAY_END_STR);
                stringBuffer.append(stringBuffer2);
                return;
            }
            stringBuffer.append(JSUtil.QUOTE + JSUtil.toJsResponseText(a.b(iWebview, obj)) + JSUtil.QUOTE);
        }
    }

    public static int a(Object obj) {
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }
        if (obj instanceof boolean[]) {
            return ((boolean[]) obj).length;
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj).length;
        }
        if (obj instanceof char[]) {
            return ((char[]) obj).length;
        }
        if (obj instanceof short[]) {
            return ((short[]) obj).length;
        }
        if (obj instanceof int[]) {
            return ((int[]) obj).length;
        }
        if (obj instanceof long[]) {
            return ((long[]) obj).length;
        }
        if (obj instanceof float[]) {
            return ((float[]) obj).length;
        }
        if (obj instanceof double[]) {
            return ((double[]) obj).length;
        }
        return -1;
    }

    public static String a(IWebview iWebview, a aVar, String str) {
        Class a2 = a(str);
        System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = new StringBuffer();
        StringBuffer stringBuffer4 = new StringBuffer();
        if (a2 != null) {
            try {
                a(stringBuffer, new ArrayList(), stringBuffer2, new ArrayList(), a2);
                a(stringBuffer);
                a(stringBuffer2);
                Field[] fields = a2.getFields();
                for (Field a3 : fields) {
                    a(iWebview, aVar, (Object) null, a3, true, stringBuffer3, stringBuffer4);
                }
                a(iWebview, aVar, Class.class, a2, "class", true, stringBuffer3, stringBuffer4);
                a(a2, str, stringBuffer3, stringBuffer4);
                a(stringBuffer3);
                a(stringBuffer4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StringUtil.format("{InstanceMethod:[%s],ClassMethod:[%s],ClassConstKeys:[%s],ClassConstValues:[%s]}", stringBuffer.toString(), stringBuffer2.toString(), stringBuffer3.toString(), stringBuffer4.toString());
    }

    private static void a(Class cls, String str, StringBuffer stringBuffer, StringBuffer stringBuffer2) {
        Class[] classes = cls.getClasses();
        if (classes != null) {
            for (Class cls2 : classes) {
                String simpleName = cls2.getSimpleName();
                String name = cls2.getName();
                stringBuffer.append(JSUtil.QUOTE + simpleName + JSUtil.QUOTE);
                stringBuffer2.append(JSUtil.QUOTE + JSUtil.toJsResponseText(StringUtil.format("plus.android.importClass('%s')", name)) + JSUtil.QUOTE);
                stringBuffer.append(",");
                stringBuffer2.append(",");
            }
        }
    }

    public static void a(StringBuffer stringBuffer) {
        if (stringBuffer.length() > 0 && stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        }
    }

    public static boolean a(Class cls) {
        return cls == Boolean.class || cls == Boolean.TYPE || cls == Double.class || cls == Double.TYPE || cls == Integer.class || cls == Integer.TYPE || cls == Float.class || cls == Float.TYPE || cls == Long.class || cls == Long.TYPE || cls == Byte.class || cls == Byte.TYPE;
    }

    public static Object a(Object obj, Class cls) {
        if (obj != null) {
            return obj;
        }
        String cls2 = cls.toString();
        if (cls2.contains("java.lang.Byte") || cls2.contains("java.lang.Long") || cls2.contains("java.lang.Float") || cls2.contains("java.lang.Integer") || cls2.contains("java.lang.Double")) {
            return 0;
        }
        if (cls2.contains("java.lang.Boolean")) {
            return Boolean.FALSE;
        }
        return null;
    }
}
