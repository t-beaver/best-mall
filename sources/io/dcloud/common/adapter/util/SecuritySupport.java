package io.dcloud.common.adapter.util;

import android.content.Context;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.emulator.EmulatorCheckUtil;

public class SecuritySupport {
    public static String getAppId() {
        return BaseInfo.sDefaultBootApp;
    }

    public static String getDeviceId(Context context) {
        return AppRuntime.getDCloudDeviceID(context);
    }

    public static boolean isRoot() {
        return DeviceInfo.hasRootPrivilege();
    }

    public static boolean isSimulator(Context context) {
        return EmulatorCheckUtil.getSingleInstance().emulatorCheck(context);
    }
}
