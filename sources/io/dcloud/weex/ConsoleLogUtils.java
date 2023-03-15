package io.dcloud.weex;

import android.text.TextUtils;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.AppConsoleLogUtil;
import io.dcloud.feature.uniapp.utils.AbsLogLevel;

public class ConsoleLogUtils {
    public static void consoleLog(String str, String str2, AbsLogLevel absLogLevel) {
        String str3;
        String str4;
        if (!TextUtils.isEmpty(str) && "jsLog".equals(str) && checkLog(str2)) {
            if (str2.endsWith("__ERROR")) {
                str4 = str2.replace("__ERROR", "");
                str3 = "ERROR";
            } else if (str2.endsWith("__LOG")) {
                str4 = str2.replace("__LOG", "");
                str3 = "LOG";
            } else if (str2.endsWith("__INFO")) {
                str4 = str2.replace("__INFO", "");
                str3 = "INFO";
            } else if (str2.endsWith("__WARN")) {
                str4 = str2.replace("__WARN", "");
                str3 = "WARN";
            } else {
                str4 = str2.replace("__DEBUG", "");
                str3 = "DEBUG";
            }
            str4.trim();
            if (str4.startsWith("v8performance")) {
                WXDotDataUtil.setValue("initJSEngineTime", str4.replace("v8performance:", ""));
            } else if (str4.startsWith("JSCPerformance")) {
                WXDotDataUtil.setValue("initJSEngineTime", str4.replace("JSCPerformance:", ""));
            } else {
                AppConsoleLogUtil.DCLog(str4, str3);
            }
        }
    }

    private static boolean checkLog(String str) {
        if (str.contains("Start windmill weex-vue-plugin")) {
            return false;
        }
        if (!str.contains("has been registered already!")) {
            return true;
        }
        Logger.i(str);
        return false;
    }
}
