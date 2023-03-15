package com.bun.miitmdid.core;

import android.content.Context;
import android.util.Log;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.a;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MdidSdkHelper {
    public static String TAG = "MdidSdkHelper";
    public static boolean _OuterIsOk = true;
    private String sdk_date = "2020011018";

    public static int InitSdk(Context context, boolean z, IIdentifierListener iIdentifierListener) {
        String str;
        try {
            if (_OuterIsOk) {
                Class<?> cls = Class.forName("com.bun.miitmdid.core.MdidSdk");
                if (cls == null) {
                    str = "not found class:" + "com.bun.miitmdid.core.MdidSdk";
                } else {
                    Constructor<?> constructor = cls.getConstructor(new Class[]{Boolean.TYPE});
                    if (constructor == null) {
                        str = "not found MdidSdk Constructor";
                    } else {
                        Object newInstance = constructor.newInstance(new Object[]{Boolean.valueOf(z)});
                        if (newInstance == null) {
                            str = "Create MdidSdk Instance failed";
                        } else {
                            Method declaredMethod = cls.getDeclaredMethod("InitSdk", new Class[]{Context.class, IIdentifierListener.class});
                            if (declaredMethod == null) {
                                str = "not found MdidSdk " + "InitSdk" + " function";
                            } else {
                                int intValue = ((Integer) declaredMethod.invoke(newInstance, new Object[]{context, iIdentifierListener})).intValue();
                                logd(z, "call and retvalue:" + intValue);
                                return intValue;
                            }
                        }
                    }
                }
                logd(z, str);
                return ErrorCode.INIT_HELPER_CALL_ERROR;
            } else if (iIdentifierListener == null) {
                return ErrorCode.INIT_HELPER_CALL_ERROR;
            } else {
                iIdentifierListener.OnSupport(false, new a());
                return ErrorCode.INIT_HELPER_CALL_ERROR;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            loge(z, e);
            logd(z, "exception exit");
            return ErrorCode.INIT_HELPER_CALL_ERROR;
        }
    }

    public static void logd(boolean z, String str) {
        if (z) {
            Log.d(TAG, str);
        }
    }

    public static void loge(boolean z, Exception exc) {
        if (z) {
            Log.e(TAG, exc.getClass().getSimpleName(), exc);
        }
    }
}
