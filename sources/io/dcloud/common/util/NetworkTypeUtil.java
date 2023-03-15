package io.dcloud.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;

public class NetworkTypeUtil {
    public static final int NETWORK_TYPE_2G = 4;
    public static final int NETWORK_TYPE_3G = 5;
    public static final int NETWORK_TYPE_4G = 6;
    public static final int NETWORK_TYPE_DISABLED = 1;
    public static final int NETWORK_TYPE_LINE = 2;
    public static final int NETWORK_TYPE_UNKONWN = 0;
    public static final int NETWORK_TYPE_WIFI = 3;

    public static String getCurrentAPN(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || activeNetworkInfo.getType() != 0) {
            return "";
        }
        return activeNetworkInfo.getExtraInfo();
    }

    public static int getNetworkType(Context context) {
        if (AppRuntime.hasPrivacyForNotShown(context)) {
            return 0;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return 1;
        }
        if (activeNetworkInfo.getType() == 0) {
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                    return 4;
                case 3:
                case 5:
                case 6:
                case 8:
                case 12:
                    return 5;
                case 9:
                case 10:
                case 11:
                case 13:
                case 15:
                case 17:
                    return 6;
                default:
                    return 0;
            }
        } else if (activeNetworkInfo.getType() == 1) {
            return 3;
        } else {
            return 0;
        }
    }

    public static boolean isWifiProxy(Context context) {
        String str;
        int i;
        if (Build.VERSION.SDK_INT >= 14) {
            str = System.getProperty("http.proxyHost");
            String property = System.getProperty("http.proxyPort");
            if (property == null) {
                property = "-1";
            }
            i = Integer.parseInt(property);
        } else {
            String host = Proxy.getHost(context);
            i = Proxy.getPort(context);
            str = host;
        }
        if (TextUtils.isEmpty(str) || i == -1) {
            return false;
        }
        return true;
    }
}
