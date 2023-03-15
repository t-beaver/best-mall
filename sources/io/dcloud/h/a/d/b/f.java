package io.dcloud.h.a.d.b;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.bun.miitmdid.core.ErrorCode;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.h.a.e.e;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class f {
    private static f a;
    /* access modifiers changed from: private */
    public String b = "";

    private class a implements InvocationHandler {
        Context a;

        public a(Context context) {
            this.a = context;
        }

        public Object invoke(Object obj, Method method, Object[] objArr) {
            if (method.getName().equalsIgnoreCase("OnSupport") && objArr != null && objArr.length > 0) {
                objArr[0].booleanValue();
                Object obj2 = objArr.length > 1 ? objArr[1] : null;
                StringBuilder sb = new StringBuilder();
                if (obj2 == null) {
                    sb.append(Operators.OR);
                } else {
                    Object a2 = f.a(obj2, "getOAID", (Class<?>[]) null, new Object[0]);
                    Object a3 = f.a(obj2, "getVAID", (Class<?>[]) null, new Object[0]);
                    Object a4 = f.a(obj2, "getAAID", (Class<?>[]) null, new Object[0]);
                    sb.append(a2 == null ? "" : a2);
                    sb.append("|");
                    if (a3 == null) {
                        a3 = "";
                    }
                    sb.append(a3);
                    sb.append("|");
                    if (a4 == null) {
                        a4 = "";
                    }
                    sb.append(a4);
                    String unused = f.this.b = String.valueOf(a2);
                    Context context = this.a;
                    if (context != null) {
                        e.a(context, "dcloud-ads", "oaid", String.valueOf(a2));
                    }
                }
            }
            return null;
        }
    }

    private f() {
    }

    private boolean b(Context context) {
        int a2 = a(context);
        return (a2 == 1008612 || a2 == 1008613 || a2 == 1008611 || a2 != 1008614) ? false : true;
    }

    public String c(Context context) {
        if (TextUtils.isEmpty(this.b)) {
            if (!b(context)) {
                return "";
            }
            if (context != null) {
                this.b = e.a(context, "dcloud-ads", "oaid");
            }
        }
        return this.b;
    }

    public static f a() {
        if (a == null) {
            synchronized (f.class) {
                if (a == null) {
                    a = new f();
                }
            }
        }
        return a;
    }

    private int a(Context context) {
        Class<?> cls;
        try {
            Class<?> cls2 = Class.forName("com.bun.miitmdid.core.MdidSdkHelper");
            try {
                cls = Class.forName("com.bun.supplier.IIdentifierListener");
            } catch (Exception unused) {
                try {
                    cls = Class.forName("com.bun.miitmdid.interfaces.IIdentifierListener");
                } catch (Exception unused2) {
                    return ErrorCode.INIT_HELPER_CALL_ERROR;
                }
            }
            Object newProxyInstance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{cls}, new a(context));
            Method declaredMethod = cls2.getDeclaredMethod("InitSdk", new Class[]{Context.class, Boolean.TYPE, cls});
            Object obj = null;
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                obj = declaredMethod.invoke((Object) null, new Object[]{context, Boolean.TRUE, newProxyInstance});
            }
            return obj instanceof Integer ? ((Integer) obj).intValue() : ErrorCode.INIT_HELPER_CALL_ERROR;
        } catch (ClassNotFoundException unused3) {
            throw new RuntimeException("not support");
        }
    }

    public static Object a(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        Method method;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> cls = obj.getClass();
            if (Build.VERSION.SDK_INT > 10) {
                method = cls.getMethod(str, clsArr);
            } else {
                method = cls.getDeclaredMethod(str, clsArr);
            }
            method.setAccessible(true);
            if (objArr.length == 0) {
                objArr = null;
            }
            return method.invoke(obj, objArr);
        } catch (Throwable unused) {
            return null;
        }
    }
}
