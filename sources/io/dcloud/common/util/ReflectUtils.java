package io.dcloud.common.util;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static final String CLASSNAME_APPLICATIONINFO = "android.content.pm.ApplicationInfo";
    public static final String CLASSNAME_AUDIOSYSTEM = "android.media.AudioSystem";
    public static final String CLASSNAME_ICONTENTPROVIDER = "android.content.IContentProvider";
    public static final String CLASSNAME_IMOUNTSERVICE_STUB = "android.os.storage.IMountService$Stub";
    public static final String CLASSNAME_IPACKAGEDATAOBSERVER = "android.content.pm.IPackageDataObserver";
    public static final String CLASSNAME_IPACKAGEDELETEOBSERVER = "android.content.pm.IPackageDeleteObserver";
    public static final String CLASSNAME_IPACKAGEINSTALLOBERVER = "android.content.pm.IPackageInstallObserver";
    public static final String CLASSNAME_IPACKAGEMANAGER = "android.content.pm.IPackageManager";
    public static final String CLASSNAME_IPACKAGEMANAGER_STUB = "android.content.pm.IPackageManager$Stub";
    public static final String CLASSNAME_IPACKAGESTATSOBSERVER = "android.content.pm.IPackageStatsObserver";
    public static final String CLASSNAME_PACKAGEINFO = "android.content.pm.PackageInfo";
    public static final String CLASSNAME_PACKAGEMANAGER = "android.content.pm.PackageManager";
    public static final String CLASSNAME_PAGEAGEPARSE = "android.content.pm.PackageParser";
    public static final String CLASSNAME_PAGEAGEPARSE_PACKAGE = "android.content.pm.PackageParser$Package";
    public static final String CLASSNAME_PROCESS = "android.os.Process";
    public static final String CLASSNAME_THREADS = "android.provider.Telephony$Threads";
    public static final String CLASSNAME_THUMBNAILUTILS = "android.media.ThumbnailUtils";

    public static Class classForName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            throw new RuntimeException(str);
        }
    }

    public static Context getApplicationContext() {
        try {
            return (Context) Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getDeclaredField(Object obj, String str) {
        Class cls = obj.getClass();
        while (cls != Object.class) {
            try {
                return cls.getDeclaredField(str);
            } catch (Exception unused) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    public static Method getDeclaredMethod(Object obj, String str, Class<?>... clsArr) {
        Class cls = obj.getClass();
        while (cls != Object.class) {
            try {
                return cls.getDeclaredMethod(str, clsArr);
            } catch (Exception unused) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    public static Object getField(Object obj, String str) throws NoSuchFieldException, IllegalAccessException {
        return prepareField(obj.getClass(), str).get(obj);
    }

    public static Object getFieldValue(Object obj, String str) {
        Field declaredField = getDeclaredField(obj, str);
        declaredField.setAccessible(true);
        try {
            return declaredField.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getIntField(Object obj, String str) {
        try {
            return obj.getClass().getDeclaredField(str).getInt(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Method getMethod(String str, String str2, Class<?>... clsArr) {
        try {
            return Class.forName(str).getDeclaredMethod(str2, clsArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor getObjectConstructor(String str, Class... clsArr) {
        try {
            return Class.forName(str).getConstructor(clsArr);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Object getObjectField(Object obj, String str) {
        try {
            return obj.getClass().getDeclaredField(str).get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Object getObjectFieldNoDeclared(Object obj, String str) {
        try {
            return obj.getClass().getField(str).get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static int getStaticIntField(String str, String str2) {
        try {
            return Class.forName(str).getDeclaredField(str2).getInt((Object) null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        } catch (ClassNotFoundException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static Object getStaticObjectField(String str, String str2) {
        try {
            return Class.forName(str).getDeclaredField(str2).get((Object) null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        } catch (ClassNotFoundException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static String getStaticStringField(String str, String str2) {
        return (String) getStaticObjectField(str, str2);
    }

    public static String getSystemProperties(String str, String str2) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class, String.class}).invoke((Object) null, new Object[]{str, str2});
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static Object invoke(Object obj, Method method, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Object obj, String str, Class<?>[] clsArr, Object[] objArr) {
        Method declaredMethod = getDeclaredMethod(obj, str, clsArr);
        declaredMethod.setAccessible(true);
        try {
            return declaredMethod.invoke(obj, objArr);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused) {
            return null;
        }
    }

    public static void modifyPushBigContentView(Object obj, String str, Object obj2) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static void modifyPushPriority(Object obj, String str, Object obj2) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    private static Field prepareField(Class<?> cls, String str) throws NoSuchFieldException {
        if (cls != null) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField;
            } finally {
                cls.getSuperclass();
            }
        } else {
            throw new NoSuchFieldException();
        }
    }

    public static void setField(Object obj, String str, Object obj2) throws NoSuchFieldException, IllegalAccessException {
        prepareField(obj.getClass(), str).set(obj, obj2);
    }

    public static void setFieldValue(Object obj, String str, Object obj2) {
        Field declaredField = getDeclaredField(obj, str);
        declaredField.setAccessible(true);
        try {
            declaredField.set(obj, obj2);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }

    public static Object stubAsInterface(String str, IBinder iBinder) {
        return stubAsInterface(classForName(str), iBinder);
    }

    public static void windowDismissed(InputMethodManager inputMethodManager, IBinder iBinder) {
        try {
            inputMethodManager.getClass().getMethod("windowDismissed", new Class[]{IBinder.class}).invoke(inputMethodManager, new Object[]{iBinder});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static Object stubAsInterface(Class cls, IBinder iBinder) {
        try {
            return cls.getDeclaredMethod("asInterface", new Class[]{IBinder.class}).invoke((Object) null, new Object[]{iBinder});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e2) {
            throw new RuntimeException(e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException(e3);
        }
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        if (str != null && str.length() > 0) {
            try {
                return cls.getMethod(str, clsArr);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
