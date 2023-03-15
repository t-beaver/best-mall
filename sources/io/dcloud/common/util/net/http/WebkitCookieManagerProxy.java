package io.dcloud.common.util.net.http;

import android.text.TextUtils;
import dc.squareup.cookie.CookieCenter;
import io.dcloud.common.DHInterface.IWebview;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebkitCookieManagerProxy extends CookieManager {
    private android.webkit.CookieManager webkitCookieManager;

    public WebkitCookieManagerProxy() {
        this((CookieStore) null, (CookiePolicy) null);
    }

    public Map<String, List<String>> get(URI uri, Map<String, List<String>> map) throws IOException {
        if (uri == null || map == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        String uri2 = uri.toString();
        HashMap hashMap = new HashMap();
        String cookie = this.webkitCookieManager.getCookie(uri2);
        String cookies = CookieCenter.getCookies(uri2);
        if (!TextUtils.isEmpty(cookies)) {
            cookie = cookie + "; " + cookies;
        }
        if (cookie != null) {
            hashMap.put(IWebview.COOKIE, Arrays.asList(new String[]{cookie}));
        }
        return hashMap;
    }

    public CookieStore getCookieStore() {
        throw new UnsupportedOperationException();
    }

    public void put(URI uri, Map<String, List<String>> map) throws IOException {
        if (uri != null && map != null) {
            String uri2 = uri.toString();
            for (String next : map.keySet()) {
                if (next != null && (next.equalsIgnoreCase("Set-Cookie2") || next.equalsIgnoreCase(IWebview.SET_COOKIE))) {
                    for (String str : map.get(next)) {
                        this.webkitCookieManager.setCookie(uri2, str);
                        CookieCenter.putCookies(uri2, str);
                    }
                }
            }
        }
    }

    public boolean removeAllCookie() {
        this.webkitCookieManager.removeAllCookie();
        CookieCenter.removeAllCookie();
        return true;
    }

    public boolean removeSessionCookie() {
        this.webkitCookieManager.removeSessionCookie();
        CookieCenter.removeSessionCookie();
        return true;
    }

    public WebkitCookieManagerProxy(CookieStore cookieStore, CookiePolicy cookiePolicy) {
        super((CookieStore) null, cookiePolicy);
        this.webkitCookieManager = android.webkit.CookieManager.getInstance();
    }
}
