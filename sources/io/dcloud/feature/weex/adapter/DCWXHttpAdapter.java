package io.dcloud.feature.weex.adapter;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import dc.squareup.okhttp3.ConnectionPool;
import dc.squareup.okhttp3.OkHttpClient;
import dc.squareup.okhttp3.Response;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class DCWXHttpAdapter implements IWXHttpAdapter {
    private static ConnectionPool mConnectPool;
    private static SSLSocketFactory sslSocketFactory;
    private ExecutorService mExecutorService;

    private void execute(Runnable runnable) {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newFixedThreadPool(10);
        }
        this.mExecutorService.execute(runnable);
    }

    public void sendRequest(final WXRequest wXRequest, final IWXHttpAdapter.OnHttpListener onHttpListener) {
        if (onHttpListener != null) {
            onHttpListener.onHttpStart();
        }
        execute(new Runnable() {
            public void run() {
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(wXRequest.instanceId);
                if (wXSDKInstance != null && !wXSDKInstance.isDestroy()) {
                    wXSDKInstance.getApmForInstance().actionNetRequest();
                }
                boolean z = true;
                WXResponse wXResponse = new WXResponse();
                boolean z2 = false;
                try {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    Response execute = builder.build().newCall(DCWXHttpAdapter.this.getOKRequest(builder, wXRequest, onHttpListener)).execute();
                    Map<String, List<String>> multimap = execute.headers().toMultimap();
                    int code = execute.code();
                    wXResponse.statusCode = String.valueOf(code);
                    IWXHttpAdapter.OnHttpListener onHttpListener = onHttpListener;
                    if (onHttpListener != null) {
                        onHttpListener.onHeadersReceived(code, multimap);
                    }
                    if (execute.isSuccessful()) {
                        wXResponse.originalData = DCWXHttpAdapter.this.readInputStreamAsBytes(execute.body().byteStream(), onHttpListener);
                    } else {
                        wXResponse.errorMsg = DCWXHttpAdapter.this.readInputStream(execute.body().byteStream(), onHttpListener);
                        z = false;
                    }
                    IWXHttpAdapter.OnHttpListener onHttpListener2 = onHttpListener;
                    if (onHttpListener2 != null) {
                        onHttpListener2.onHttpFinish(wXResponse);
                    }
                    z2 = z;
                } catch (Exception e) {
                    e.printStackTrace();
                    wXResponse.statusCode = "-1";
                    wXResponse.errorCode = "-1";
                    wXResponse.errorMsg = e.getMessage();
                    IWXHttpAdapter.OnHttpListener onHttpListener3 = onHttpListener;
                    if (onHttpListener3 != null) {
                        onHttpListener3.onHttpFinish(wXResponse);
                    }
                }
                if (wXSDKInstance != null && !wXSDKInstance.isDestroy()) {
                    wXSDKInstance.getApmForInstance().actionNetResult(z2, (String) null);
                }
            }
        });
    }

    /* JADX WARNING: type inference failed for: r1v45, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public dc.squareup.okhttp3.Request getOKRequest(dc.squareup.okhttp3.OkHttpClient.Builder r6, com.taobao.weex.common.WXRequest r7, com.taobao.weex.adapter.IWXHttpAdapter.OnHttpListener r8) {
        /*
            r5 = this;
            int r0 = r7.timeoutMs
            long r0 = (long) r0
            java.util.concurrent.TimeUnit r2 = java.util.concurrent.TimeUnit.MILLISECONDS
            dc.squareup.okhttp3.OkHttpClient$Builder r0 = r6.connectTimeout(r0, r2)
            int r1 = r7.timeoutMs
            long r1 = (long) r1
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS
            dc.squareup.okhttp3.OkHttpClient$Builder r0 = r0.readTimeout(r1, r3)
            int r1 = r7.timeoutMs
            long r1 = (long) r1
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS
            dc.squareup.okhttp3.OkHttpClient$Builder r0 = r0.writeTimeout(r1, r3)
            int r1 = r7.timeoutMs
            long r1 = (long) r1
            java.util.concurrent.TimeUnit r3 = java.util.concurrent.TimeUnit.MILLISECONDS
            dc.squareup.okhttp3.OkHttpClient$Builder r0 = r0.callTimeout(r1, r3)
            dc.squareup.okhttp3.Protocol r1 = dc.squareup.okhttp3.Protocol.HTTP_1_1
            java.util.List r1 = java.util.Collections.singletonList(r1)
            r0.protocols(r1)
            boolean r0 = r7.isFirstIpv4
            if (r0 == 0) goto L_0x0039
            io.dcloud.common.adapter.util.DCOKDns r0 = new io.dcloud.common.adapter.util.DCOKDns
            r0.<init>()
            r6.dns(r0)
        L_0x0039:
            java.lang.String r0 = r7.url
            android.net.Uri r0 = android.net.Uri.parse(r0)
            java.lang.String r0 = r0.getHost()
            com.alibaba.fastjson.JSONObject r1 = r7.tls
            r2 = 0
            if (r1 == 0) goto L_0x0097
            io.dcloud.feature.weex.config.AndroidTlsConfig r0 = new io.dcloud.feature.weex.config.AndroidTlsConfig
            r0.<init>()
            com.alibaba.fastjson.JSONObject r1 = r7.tls
            java.lang.String r3 = "keystore"
            java.lang.String r1 = r1.getString(r3)
            r0.setKeystore(r1)
            com.alibaba.fastjson.JSONObject r1 = r7.tls
            java.lang.String r3 = "storePass"
            java.lang.String r1 = r1.getString(r3)
            r0.setStorePass(r1)
            com.alibaba.fastjson.JSONObject r1 = r7.tls
            java.lang.String r3 = "ca"
            com.alibaba.fastjson.JSONArray r1 = r1.getJSONArray(r3)
            r3 = 0
            if (r1 == 0) goto L_0x0077
            java.lang.String[] r3 = new java.lang.String[r2]
            java.lang.Object[] r1 = r1.toArray(r3)
            r3 = r1
            java.lang.String[] r3 = (java.lang.String[]) r3
        L_0x0077:
            r0.setCa(r3)
            com.taobao.weex.WXSDKManager r1 = com.taobao.weex.WXSDKManager.getInstance()
            java.util.Map r1 = r1.getAllInstanceMap()
            java.lang.String r3 = r7.instanceId
            java.lang.Object r1 = r1.get(r3)
            com.taobao.weex.WXSDKInstance r1 = (com.taobao.weex.WXSDKInstance) r1
            javax.net.ssl.SSLSocketFactory r0 = io.dcloud.feature.weex.config.UserCustomTrustManager.getSSLSocketFactory((io.dcloud.feature.weex.config.AndroidTlsConfig) r0, (com.taobao.weex.WXSDKInstance) r1)
            r6.sslSocketFactory(r0)
            dc.squareup.okhttp3.internal.tls.OkHostnameVerifier r0 = dc.squareup.okhttp3.internal.tls.OkHostnameVerifier.INSTANCE
            r6.hostnameVerifier(r0)
            goto L_0x00f0
        L_0x0097:
            java.util.HashMap<java.lang.String, com.taobao.weex.http.CertDTO> r1 = com.taobao.weex.http.WXStreamModule.certMap
            if (r1 == 0) goto L_0x00c8
            java.util.HashMap<java.lang.String, com.taobao.weex.http.CertDTO> r1 = com.taobao.weex.http.WXStreamModule.certMap
            boolean r1 = r1.containsKey(r0)
            if (r1 == 0) goto L_0x00c8
            java.util.HashMap<java.lang.String, com.taobao.weex.http.CertDTO> r1 = com.taobao.weex.http.WXStreamModule.certMap
            java.lang.Object r0 = r1.get(r0)
            com.taobao.weex.http.CertDTO r0 = (com.taobao.weex.http.CertDTO) r0
            com.taobao.weex.WXSDKManager r1 = com.taobao.weex.WXSDKManager.getInstance()
            java.util.Map r1 = r1.getAllInstanceMap()
            java.lang.String r3 = r7.instanceId
            java.lang.Object r1 = r1.get(r3)
            com.taobao.weex.WXSDKInstance r1 = (com.taobao.weex.WXSDKInstance) r1
            javax.net.ssl.SSLSocketFactory r0 = io.dcloud.feature.weex.config.UserCustomTrustManager.getSSLSocketFactory((com.taobao.weex.http.CertDTO) r0, (com.taobao.weex.WXSDKInstance) r1)
            r6.sslSocketFactory(r0)
            dc.squareup.okhttp3.internal.tls.OkHostnameVerifier r0 = dc.squareup.okhttp3.internal.tls.OkHostnameVerifier.INSTANCE
            r6.hostnameVerifier(r0)
            goto L_0x00f0
        L_0x00c8:
            boolean r0 = r7.sslVerify
            if (r0 == 0) goto L_0x00f0
            javax.net.ssl.SSLSocketFactory r0 = sslSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x00e3, KeyManagementException -> 0x00de }
            if (r0 != 0) goto L_0x00d6
            javax.net.ssl.SSLSocketFactory r0 = io.dcloud.common.adapter.util.DCloudTrustManager.getSSLSocketFactory()     // Catch:{ NoSuchAlgorithmException -> 0x00e3, KeyManagementException -> 0x00de }
            sslSocketFactory = r0     // Catch:{ NoSuchAlgorithmException -> 0x00e3, KeyManagementException -> 0x00de }
        L_0x00d6:
            javax.net.ssl.SSLSocketFactory r0 = sslSocketFactory     // Catch:{ NoSuchAlgorithmException -> 0x00e3, KeyManagementException -> 0x00de }
            if (r0 == 0) goto L_0x00e7
            r6.sslSocketFactory(r0)     // Catch:{ NoSuchAlgorithmException -> 0x00e3, KeyManagementException -> 0x00de }
            goto L_0x00e7
        L_0x00de:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00e7
        L_0x00e3:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00e7:
            boolean r0 = r7.sslVerify
            org.apache.http.conn.ssl.X509HostnameVerifier r0 = r5.getHostnameVerifier(r0)
            r6.hostnameVerifier(r0)
        L_0x00f0:
            dc.squareup.okhttp3.ConnectionPool r0 = mConnectPool
            if (r0 != 0) goto L_0x00fb
            dc.squareup.okhttp3.ConnectionPool r0 = new dc.squareup.okhttp3.ConnectionPool
            r0.<init>()
            mConnectPool = r0
        L_0x00fb:
            dc.squareup.okhttp3.ConnectionPool r0 = mConnectPool
            r6.connectionPool(r0)
            r6.build()
            dc.squareup.okhttp3.Request$Builder r6 = new dc.squareup.okhttp3.Request$Builder
            r6.<init>()
            java.lang.String r0 = r7.url
            r6.url((java.lang.String) r0)
            java.lang.String r0 = "application/x-www-form-urlencoded"
            java.util.Map<java.lang.String, java.lang.String> r1 = r7.paramMap
            if (r1 == 0) goto L_0x0145
            java.util.Map<java.lang.String, java.lang.String> r1 = r7.paramMap
            java.util.Set r1 = r1.keySet()
            java.util.Iterator r1 = r1.iterator()
        L_0x011d:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x0145
            java.lang.Object r3 = r1.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "Content-Type"
            boolean r4 = r3.equalsIgnoreCase(r4)
            if (r4 == 0) goto L_0x0139
            java.util.Map<java.lang.String, java.lang.String> r0 = r7.paramMap
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
        L_0x0139:
            java.util.Map<java.lang.String, java.lang.String> r4 = r7.paramMap
            java.lang.Object r4 = r4.get(r3)
            java.lang.String r4 = (java.lang.String) r4
            r6.addHeader(r3, r4)
            goto L_0x011d
        L_0x0145:
            java.lang.String r1 = r7.method
            java.lang.String r3 = "POST"
            boolean r1 = r3.equals(r1)
            if (r1 != 0) goto L_0x017c
            java.lang.String r1 = r7.method
            java.lang.String r3 = "PUT"
            boolean r1 = r3.equals(r1)
            if (r1 != 0) goto L_0x017c
            java.lang.String r1 = r7.method
            java.lang.String r3 = "PATCH"
            boolean r1 = r3.equals(r1)
            if (r1 != 0) goto L_0x017c
            java.lang.String r1 = r7.method
            java.lang.String r3 = "DELETE"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L_0x016e
            goto L_0x017c
        L_0x016e:
            java.lang.String r7 = r7.method
            java.lang.String r8 = "HEAD"
            boolean r7 = r8.equals(r7)
            if (r7 == 0) goto L_0x01bf
            r6.head()
            goto L_0x01bf
        L_0x017c:
            java.lang.String r1 = r7.body
            if (r1 == 0) goto L_0x0185
            if (r8 == 0) goto L_0x0185
            r8.onHttpUploadProgress(r2)
        L_0x0185:
            java.lang.String r1 = r7.inputType
            java.lang.String r2 = "BASE64"
            boolean r1 = r2.equalsIgnoreCase(r1)
            r2 = 100
            if (r1 == 0) goto L_0x01ab
            java.lang.String r1 = r7.body
            r3 = 2
            byte[] r1 = android.util.Base64.decode(r1, r3)
            dc.squareup.okhttp3.MediaType r0 = dc.squareup.okhttp3.MediaType.parse(r0)
            dc.squareup.okhttp3.RequestBody r0 = dc.squareup.okhttp3.RequestBody.createWithBytes(r0, r1)
            java.lang.String r7 = r7.method
            r6.method(r7, r0)
            if (r8 == 0) goto L_0x01bf
            r8.onHttpUploadProgress(r2)
            goto L_0x01bf
        L_0x01ab:
            dc.squareup.okhttp3.MediaType r0 = dc.squareup.okhttp3.MediaType.parse(r0)
            java.lang.String r1 = r7.body
            dc.squareup.okhttp3.RequestBody r0 = dc.squareup.okhttp3.RequestBody.create((dc.squareup.okhttp3.MediaType) r0, (java.lang.String) r1)
            java.lang.String r7 = r7.method
            r6.method(r7, r0)
            if (r8 == 0) goto L_0x01bf
            r8.onHttpUploadProgress(r2)
        L_0x01bf:
            dc.squareup.okhttp3.Request r6 = r6.build()
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.adapter.DCWXHttpAdapter.getOKRequest(dc.squareup.okhttp3.OkHttpClient$Builder, com.taobao.weex.common.WXRequest, com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener):dc.squareup.okhttp3.Request");
    }

    /* access modifiers changed from: private */
    public byte[] readInputStreamAsBytes(InputStream inputStream, IWXHttpAdapter.OnHttpListener onHttpListener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr, 0, 2048);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
                i += read;
                if (onHttpListener != null) {
                    onHttpListener.onHttpResponseProgress(i);
                }
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    /* access modifiers changed from: private */
    public String readInputStream(InputStream inputStream, IWXHttpAdapter.OnHttpListener onHttpListener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] cArr = new char[2048];
        while (true) {
            int read = bufferedReader.read(cArr);
            if (read != -1) {
                sb.append(cArr, 0, read);
                if (onHttpListener != null) {
                    onHttpListener.onHttpResponseProgress(sb.length());
                }
            } else {
                bufferedReader.close();
                return sb.toString();
            }
        }
    }

    public X509HostnameVerifier getHostnameVerifier(boolean z) {
        if (!z) {
            return org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        }
        return org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    }
}
