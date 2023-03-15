package io.dcloud.feature.barcode2.camera;

import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class FlashlightManager {
    private static final String TAG = "FlashlightManager";
    private static final Object iHardwareService;
    private static final Method setFlashEnabledMethod;

    static {
        String simpleName = FlashlightManager.class.getSimpleName();
        Object hardwareService = getHardwareService();
        iHardwareService = hardwareService;
        setFlashEnabledMethod = getSetFlashEnabledMethod(hardwareService);
        if (hardwareService == null) {
            Log.v(simpleName, "This device does supports control of a flashlight");
        } else {
            Log.v(simpleName, "This device does not support control of a flashlight");
        }
    }

    private FlashlightManager() {
    }

    static void disableFlashlight() {
        try {
            setFlashlight(false);
        } catch (Exception unused) {
        }
    }

    static void enableFlashlight() {
        try {
            setFlashlight(true);
        } catch (Exception unused) {
        }
    }

    private static Object getHardwareService() {
        Method maybeGetMethod;
        Object invoke;
        Class<?> maybeForName;
        Method maybeGetMethod2;
        Class<?> maybeForName2 = maybeForName("android.os.ServiceManager");
        if (maybeForName2 == null || (maybeGetMethod = maybeGetMethod(maybeForName2, "getService", String.class)) == null || (invoke = invoke(maybeGetMethod, (Object) null, "hardware")) == null || (maybeForName = maybeForName("android.os.IHardwareService$Stub")) == null || (maybeGetMethod2 = maybeGetMethod(maybeForName, "asInterface", IBinder.class)) == null) {
            return null;
        }
        return invoke(maybeGetMethod2, (Object) null, invoke);
    }

    private static Method getSetFlashEnabledMethod(Object obj) {
        if (obj == null) {
            return null;
        }
        return maybeGetMethod(obj.getClass(), "setFlashlightEnabled", Boolean.TYPE);
    }

    private static Object invoke(Method method, Object obj, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            String str = TAG;
            Log.w(str, "Unexpected error while invoking " + method, e);
            return null;
        } catch (InvocationTargetException e2) {
            String str2 = TAG;
            Log.w(str2, "Unexpected error while invoking " + method, e2.getCause());
            return null;
        } catch (RuntimeException e3) {
            String str3 = TAG;
            Log.w(str3, "Unexpected error while invoking " + method, e3);
            return null;
        }
    }

    private static Class<?> maybeForName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            return null;
        } catch (RuntimeException e) {
            String str2 = TAG;
            Log.w(str2, "Unexpected error while finding class " + str, e);
            return null;
        }
    }

    private static Method maybeGetMethod(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            return null;
        } catch (RuntimeException e) {
            String str2 = TAG;
            Log.w(str2, "Unexpected error while finding method " + str, e);
            return null;
        }
    }

    private static void setFlashlight(boolean z) {
        Camera cameraHandler = CameraManager.get().getCameraHandler();
        Camera.Parameters parameters = cameraHandler.getParameters();
        if (z) {
            parameters.setFlashMode("torch");
        } else {
            parameters.setFlashMode("off");
        }
        cameraHandler.setParameters(parameters);
        if (DeviceInfo.sBrand.equalsIgnoreCase(MobilePhoneModel.XIAOMI) && Build.VERSION.SDK_INT == 19) {
            cameraHandler.stopPreview();
            cameraHandler.startPreview();
            cameraHandler.autoFocus(CameraManager.get().getAutoFocusCallback());
        }
    }
}
