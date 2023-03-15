package io.dcloud.common.util;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;

public class AppConsoleLogUtil {
    private static int LOG_MAXLENGTH = 1000;
    private static String TAG = "console";
    private static DCAPPLogWatcher mWatcher;

    public interface DCAPPLogWatcher {
        void i(String str, String str2);
    }

    public static void DCLog(String str, String str2) {
        if (BaseInfo.SyncDebug) {
            if (TextUtils.isEmpty(str)) {
                Log.i(TAG, str);
                return;
            }
            int length = str.length();
            String str3 = Operators.ARRAY_START_STR + str2 + Operators.ARRAY_END_STR;
            if (length > LOG_MAXLENGTH) {
                int hashCode = str.hashCode();
                String str4 = "[___Connect_" + hashCode + "___]";
                String str5 = "[___Connect_" + hashCode + "___END]";
                int i = 0;
                while (i < length) {
                    int i2 = length - i;
                    int i3 = LOG_MAXLENGTH;
                    int i4 = i2 >= i3 ? i3 + i : length;
                    String substring = str.substring(i, i4);
                    if (i4 == length) {
                        Log.i(TAG, str3 + str4 + substring + str5);
                        StringBuilder sb = new StringBuilder();
                        sb.append(str4);
                        sb.append(substring);
                        sb.append(str5);
                        notifyLog(str3, sb.toString());
                    } else {
                        Log.i(TAG, str3 + str4 + substring);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str4);
                        sb2.append(substring);
                        notifyLog(str3, sb2.toString());
                    }
                    i = i4;
                }
                return;
            }
            Log.i(TAG, str3 + str);
            notifyLog(str3, str);
        }
    }

    public static void log(String str) {
        Log.i(TAG, str);
    }

    private static void notifyLog(String str, String str2) {
        DCAPPLogWatcher dCAPPLogWatcher = mWatcher;
        if (dCAPPLogWatcher != null) {
            dCAPPLogWatcher.i(str, str2);
        }
    }

    public static void setWatcher(DCAPPLogWatcher dCAPPLogWatcher) {
        mWatcher = dCAPPLogWatcher;
    }
}
