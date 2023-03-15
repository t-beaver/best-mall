package io.dcloud.h.a.e;

import android.text.TextUtils;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.sdk.poly.base.utils.b;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class d {
    public static byte[] a(String str, HashMap<String, String> hashMap, boolean z) throws Exception {
        return a(str, (String) null, hashMap, "GET", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, z);
    }

    public static byte[] a(String str, HashMap<String, String> hashMap, boolean z, String[] strArr) {
        return a(str, (String) null, hashMap, "GET", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, z, false, strArr);
    }

    private static byte[] a(String str, String str2, HashMap<String, String> hashMap, String str3, int i, boolean z) {
        return a(str, str2, hashMap, str3, i, z, false, (String[]) null);
    }

    private static byte[] a(String str, String str2, HashMap<String, String> hashMap, String str3, int i, boolean z, boolean z2, String[] strArr) {
        if (!(str == null || str.length() == 0)) {
            if (hashMap == null) {
                try {
                    hashMap = new HashMap<>();
                } catch (Exception e) {
                    if (strArr != null) {
                        strArr[0] = e.getMessage();
                    }
                }
            }
            HttpURLConnection a = a(new URL(str), str3, i, z, z2);
            if (!hashMap.isEmpty()) {
                for (String next : hashMap.keySet()) {
                    a.setRequestProperty(next, hashMap.get(next));
                }
            }
            if (!TextUtils.isEmpty(str3) && TextUtils.equals(str3.toLowerCase(Locale.ENGLISH), "post")) {
                a(a.getOutputStream(), str2);
            }
            int responseCode = a.getResponseCode();
            if (responseCode == 200) {
                return a(a.getInputStream());
            }
            if (strArr != null) {
                strArr[0] = String.valueOf(responseCode);
            }
            return null;
        }
        return null;
    }

    public static InputStream a(String str, int i, boolean z, String[] strArr) {
        return a(str, (String) null, "GET", i, z, false, strArr);
    }

    private static InputStream a(String str, String str2, String str3, int i, boolean z, boolean z2, String[] strArr) {
        if (!(str == null || str.length() == 0)) {
            try {
                HashMap hashMap = new HashMap();
                HttpURLConnection a = a(new URL(str), str3, i, z, z2);
                if (!hashMap.isEmpty()) {
                    for (String str4 : hashMap.keySet()) {
                        a.setRequestProperty(str4, (String) hashMap.get(str4));
                    }
                }
                if (!TextUtils.isEmpty(str3) && TextUtils.equals(str3.toLowerCase(Locale.ENGLISH), "post")) {
                    a(a.getOutputStream(), str2);
                }
                int responseCode = a.getResponseCode();
                if (responseCode == 200) {
                    return a.getInputStream();
                }
                if (strArr != null) {
                    strArr[0] = String.valueOf(responseCode);
                }
                return null;
            } catch (Exception e) {
                if (strArr != null) {
                    strArr[0] = e.getMessage();
                }
            }
        }
        return null;
    }

    private static void a(OutputStream outputStream, String str) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    outputStream.write(str.getBytes("UTF-8"));
                }
            } catch (IOException unused) {
            }
        }
    }

    private static byte[] a(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int i = 10240;
            int min = Math.min(10240, inputStream.available());
            if (min > 0) {
                i = min;
            }
            byte[] bArr = new byte[i];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static HttpURLConnection a(URL url, String str, int i, boolean z, boolean z2) {
        HttpURLConnection httpURLConnection;
        try {
            if (b.a) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            }
            httpURLConnection.setConnectTimeout(i);
            httpURLConnection.setReadTimeout(i);
            httpURLConnection.setRequestMethod(str);
            httpURLConnection.setDoInput(true);
            return httpURLConnection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] a(String str, String str2, HashMap<String, String> hashMap) {
        return a(str, str2, hashMap, "POST", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, true);
    }

    public static byte[] a(String str, String str2, HashMap<String, String> hashMap, String[] strArr) {
        return a(str, str2, hashMap, "POST", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, true, false, strArr);
    }
}
