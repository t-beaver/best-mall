package io.dcloud.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.common.constant.StringConst;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class NetTool {
    private static final int DEFAULT_TIME_OUT_TIMES = 5000;
    private static String TAG = "NetTool";
    static HostnameVerifier sCustomeHostnameVerifier;

    public static HttpURLConnection createConnection(URL url, String str, int i, boolean z, boolean z2) {
        HttpURLConnection httpURLConnection;
        try {
            if (BaseInfo.isUniStatistics) {
                z2 = false;
            }
            if (!z2 || DHFile.hasFile()) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } else {
                httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            }
            if ((httpURLConnection instanceof HttpsURLConnection) && z) {
                SSLSocketFactory sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory();
                if (sSLSocketFactory != null) {
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLSocketFactory);
                }
                ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(getDefaultHostnameVerifier());
            }
            httpURLConnection.setConnectTimeout(i);
            httpURLConnection.setReadTimeout(i);
            httpURLConnection.setRequestMethod(str);
            httpURLConnection.setDoInput(true);
            return httpURLConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static HostnameVerifier getDefaultHostnameVerifier() {
        HostnameVerifier hostnameVerifier = sCustomeHostnameVerifier;
        return hostnameVerifier != null ? hostnameVerifier : DCloudTrustManager.getHostnameVerifier(true);
    }

    public static byte[] httpGet(String str, HashMap<String, String> hashMap) throws Exception {
        return request(str, (String) null, hashMap, "GET", 5000, true);
    }

    public static byte[] httpGetThrows(String str) throws Exception {
        return httpGet(str, (HashMap<String, String>) null);
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap) {
        return request(str, str2, hashMap, "POST", 5000, true);
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (!(connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null)) {
            for (int i = 0; i < allNetworkInfo.length; i++) {
                if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED || allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTING) {
                    return true;
                }
            }
        }
        return false;
    }

    private static byte[] read(InputStream inputStream) {
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

    private static byte[] request(String str, String str2, HashMap<String, String> hashMap, String str3, int i, boolean z) {
        return request(str, str2, hashMap, str3, i, z, false, (String[]) null);
    }

    private static void write(OutputStream outputStream, String str) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    outputStream.write(str.getBytes("UTF-8"));
                }
            } catch (IOException unused) {
            }
        }
    }

    public static byte[] httpGet(String str, HashMap<String, String> hashMap, boolean z) throws Exception {
        return request(str, (String) null, hashMap, "GET", 5000, z);
    }

    public static byte[] httpGetThrows(String str, boolean z) throws Exception {
        return httpGet(str, (HashMap<String, String>) null, z);
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap, boolean z) {
        return request(str, str2, hashMap, "POST", 5000, z);
    }

    private static byte[] request(String str, String str2, HashMap<String, String> hashMap, String str3, int i, boolean z, boolean z2) {
        return request(str, str2, hashMap, str3, i, z, z2, (String[]) null);
    }

    public static byte[] httpGet(String str, HashMap<String, String> hashMap, int i) throws Exception {
        return request(str, (String) null, hashMap, "GET", i, true);
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap, boolean z, boolean z2) {
        return request(str, str2, hashMap, "POST", 5000, z, z2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00c3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] request(java.lang.String r13, java.lang.String r14, java.util.HashMap<java.lang.String, java.lang.String> r15, java.lang.String r16, int r17, boolean r18, boolean r19, java.lang.String[] r20) {
        /*
            r1 = r13
            r3 = r15
            r4 = r16
            r2 = 0
            if (r1 == 0) goto L_0x00e6
            int r0 = r13.length()
            if (r0 != 0) goto L_0x000f
            goto L_0x00e6
        L_0x000f:
            r5 = 0
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x0098 }
            r0.<init>(r13)     // Catch:{ Exception -> 0x0098 }
            r6 = r17
            r7 = r18
            r8 = r19
            java.net.HttpURLConnection r0 = createConnection(r0, r4, r6, r7, r8)     // Catch:{ Exception -> 0x0095 }
            if (r3 == 0) goto L_0x0045
            boolean r9 = r15.isEmpty()     // Catch:{ Exception -> 0x0095 }
            if (r9 != 0) goto L_0x0045
            java.util.Set r9 = r15.keySet()     // Catch:{ Exception -> 0x0095 }
            java.util.Iterator r9 = r9.iterator()     // Catch:{ Exception -> 0x0095 }
        L_0x002f:
            boolean r10 = r9.hasNext()     // Catch:{ Exception -> 0x0095 }
            if (r10 == 0) goto L_0x0045
            java.lang.Object r10 = r9.next()     // Catch:{ Exception -> 0x0095 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Exception -> 0x0095 }
            java.lang.Object r11 = r15.get(r10)     // Catch:{ Exception -> 0x0095 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Exception -> 0x0095 }
            r0.setRequestProperty(r10, r11)     // Catch:{ Exception -> 0x0095 }
            goto L_0x002f
        L_0x0045:
            boolean r9 = android.text.TextUtils.isEmpty(r16)     // Catch:{ Exception -> 0x0095 }
            if (r9 != 0) goto L_0x0062
            java.util.Locale r9 = java.util.Locale.ENGLISH     // Catch:{ Exception -> 0x0095 }
            java.lang.String r9 = r4.toLowerCase(r9)     // Catch:{ Exception -> 0x0095 }
            java.lang.String r10 = "post"
            boolean r9 = android.text.TextUtils.equals(r9, r10)     // Catch:{ Exception -> 0x0095 }
            if (r9 == 0) goto L_0x0062
            java.io.OutputStream r9 = r0.getOutputStream()     // Catch:{ Exception -> 0x0095 }
            r10 = r14
            write(r9, r14)     // Catch:{ Exception -> 0x0093 }
            goto L_0x0063
        L_0x0062:
            r10 = r14
        L_0x0063:
            int r9 = r0.getResponseCode()     // Catch:{ Exception -> 0x0093 }
            r11 = 200(0xc8, float:2.8E-43)
            if (r9 == r11) goto L_0x008a
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x0093 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0093 }
            r11.<init>()     // Catch:{ Exception -> 0x0093 }
            java.lang.String r12 = "httpGet fail, status code = "
            r11.append(r12)     // Catch:{ Exception -> 0x0093 }
            r11.append(r9)     // Catch:{ Exception -> 0x0093 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x0093 }
            io.dcloud.common.adapter.util.Logger.p(r0, r11)     // Catch:{ Exception -> 0x0093 }
            if (r20 == 0) goto L_0x0089
            java.lang.String r0 = java.lang.String.valueOf(r9)     // Catch:{ Exception -> 0x0093 }
            r20[r5] = r0     // Catch:{ Exception -> 0x0093 }
        L_0x0089:
            return r2
        L_0x008a:
            java.io.InputStream r0 = r0.getInputStream()     // Catch:{ Exception -> 0x0093 }
            byte[] r0 = read(r0)     // Catch:{ Exception -> 0x0093 }
            return r0
        L_0x0093:
            r0 = move-exception
            goto L_0x00a0
        L_0x0095:
            r0 = move-exception
            r10 = r14
            goto L_0x00a0
        L_0x0098:
            r0 = move-exception
            r10 = r14
            r6 = r17
            r7 = r18
            r8 = r19
        L_0x00a0:
            boolean r9 = r0 instanceof java.net.SocketTimeoutException
            if (r9 != 0) goto L_0x00a8
            boolean r9 = r0 instanceof java.net.UnknownHostException
            if (r9 == 0) goto L_0x00c3
        L_0x00a8:
            boolean r9 = io.dcloud.common.constant.StringConst.canChangeHost(r13)
            if (r9 == 0) goto L_0x00c3
            java.lang.String r1 = io.dcloud.common.constant.StringConst.changeHost(r13)
            r2 = r14
            r3 = r15
            r4 = r16
            r5 = r17
            r6 = r18
            r7 = r19
            r8 = r20
            byte[] r0 = request(r1, r2, r3, r4, r5, r6, r7, r8)
            return r0
        L_0x00c3:
            java.lang.String r1 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "httpPost exception, e = "
            r3.append(r4)
            java.lang.String r4 = r0.getMessage()
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            io.dcloud.common.adapter.util.Logger.p(r1, r3)
            if (r20 == 0) goto L_0x00e5
            java.lang.String r0 = r0.getMessage()
            r20[r5] = r0
        L_0x00e5:
            return r2
        L_0x00e6:
            java.lang.String r0 = TAG
            java.lang.String r1 = "httpPost, url is null"
            io.dcloud.common.adapter.util.Logger.p(r0, r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.NetTool.request(java.lang.String, java.lang.String, java.util.HashMap, java.lang.String, int, boolean, boolean, java.lang.String[]):byte[]");
    }

    public static byte[] httpGet(String str, HashMap<String, String> hashMap, int i, boolean z) throws Exception {
        return request(str, (String) null, hashMap, "GET", i, z);
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap, boolean z, String[] strArr) {
        return request(str, str2, hashMap, "POST", 5000, z, false, strArr);
    }

    public static byte[] httpGet(String str) {
        return httpGet(str, true);
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap, boolean z, boolean z2, String[] strArr) {
        return request(str, str2, hashMap, "POST", 5000, z, z2, strArr);
    }

    public static byte[] httpGet(String str, boolean z) {
        try {
            return httpGet(str, (HashMap<String, String>) null, z);
        } catch (Exception e) {
            if (((e instanceof SocketTimeoutException) || (e instanceof UnknownHostException)) && StringConst.canChangeHost(str)) {
                return httpGet(StringConst.changeHost(str));
            }
            return null;
        }
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap, int i) {
        return request(str, str2, hashMap, "POST", i, true);
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap, int i, boolean z) {
        return request(str, str2, hashMap, "POST", i, z);
    }

    public static byte[] httpGet(String str, int i) {
        return httpGet(str, i, true);
    }

    public static byte[] httpGet(String str, int i, boolean z) {
        try {
            return httpGet(str, (HashMap<String, String>) null, i, z);
        } catch (Exception e) {
            if (((e instanceof SocketTimeoutException) || (e instanceof UnknownHostException)) && StringConst.canChangeHost(str)) {
                return httpGet(StringConst.changeHost(str), i);
            }
            return null;
        }
    }
}
