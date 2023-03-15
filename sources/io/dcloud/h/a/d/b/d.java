package io.dcloud.h.a.d.b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.h.c.c.a.a;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Iterator;

public class d {
    private static String a = "";
    private static boolean b = false;

    public static String a(Context context) {
        if (context == null) {
            return a;
        }
        if (!PrivacyManager.getInstance().d()) {
            return a;
        }
        if (!a.a().a(context)) {
            return a;
        }
        if (b) {
            return a;
        }
        String str = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
        NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
        if (networkInfo.isConnected()) {
            str = a();
        } else if (networkInfo2.isConnected()) {
            str = a(((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress());
        }
        a = str;
        b = true;
        return str;
    }

    public static String a() {
        try {
            Iterator<T> it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                Iterator<T> it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
                while (true) {
                    if (it2.hasNext()) {
                        InetAddress inetAddress = (InetAddress) it2.next();
                        if (!inetAddress.isLoopbackAddress()) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
            return null;
        } catch (SocketException unused) {
            return null;
        }
    }

    public static String a(int i) {
        return (i & 255) + Operators.DOT_STR + ((i >> 8) & 255) + Operators.DOT_STR + ((i >> 16) & 255) + Operators.DOT_STR + ((i >> 24) & 255);
    }
}
