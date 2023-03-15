package io.dcloud.e.d;

import android.content.Context;
import com.bun.miitmdid.core.ErrorCode;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class a {
    /* access modifiers changed from: private */
    public b a;

    public interface b {
        void a(String str, boolean z);
    }

    private class c implements InvocationHandler {
        private c() {
        }

        public Object invoke(Object obj, Method method, Object[] objArr) {
            if (method.getName().equalsIgnoreCase("OnSupport") && objArr != null && objArr.length > 0) {
                boolean booleanValue = objArr[0].booleanValue();
                Object obj2 = objArr.length > 1 ? objArr[1] : null;
                StringBuilder sb = new StringBuilder();
                if (obj2 == null) {
                    sb.append(Operators.OR);
                } else {
                    Object invokeMethod = PlatformUtil.invokeMethod(obj2, "getOAID", (Class<?>[]) null, new Object[0]);
                    Object invokeMethod2 = PlatformUtil.invokeMethod(obj2, "getVAID", (Class<?>[]) null, new Object[0]);
                    Object invokeMethod3 = PlatformUtil.invokeMethod(obj2, "getAAID", (Class<?>[]) null, new Object[0]);
                    if (invokeMethod == null) {
                        invokeMethod = "";
                    }
                    sb.append(invokeMethod);
                    sb.append("|");
                    if (invokeMethod2 == null) {
                        invokeMethod2 = "";
                    }
                    sb.append(invokeMethod2);
                    sb.append("|");
                    if (invokeMethod3 == null) {
                        invokeMethod3 = "";
                    }
                    sb.append(invokeMethod3);
                }
                if (a.this.a != null) {
                    a.this.a.a(sb.toString(), booleanValue);
                }
            }
            return null;
        }
    }

    public a(b bVar) {
        this.a = bVar;
    }

    public boolean b(Context context) {
        int a2;
        if (PdrUtil.checkIntl() || (a2 = a(context)) == 1008612 || a2 == 1008613 || a2 == 1008611 || a2 != 1008614) {
            return false;
        }
        return true;
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
            Object obj = null;
            Object newProxyInstance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{cls}, new c());
            Method declaredMethod = cls2.getDeclaredMethod("InitSdk", new Class[]{Context.class, Boolean.TYPE, cls});
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                obj = declaredMethod.invoke((Object) null, new Object[]{context, Boolean.TRUE, newProxyInstance});
            }
            return obj instanceof Integer ? ((Integer) obj).intValue() : ErrorCode.INIT_HELPER_CALL_ERROR;
        } catch (ClassNotFoundException unused3) {
            if (PdrUtil.checkIntl() || BaseInfo.isChannelGooglePlay()) {
                return ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT;
            }
            throw new RuntimeException(context.getString(R.string.dcloud_common_app_not_oaid));
        }
    }
}
