package io.dcloud.common.util.net.http;

import android.content.Context;
import android.webkit.CookieSyncManager;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieStore;

public class CookieManager {
    public static void initCookieConfig(Context context) {
        CookieSyncManager.createInstance(context);
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
        CookieHandler.setDefault(new WebkitCookieManagerProxy((CookieStore) null, CookiePolicy.ACCEPT_ALL));
    }
}
