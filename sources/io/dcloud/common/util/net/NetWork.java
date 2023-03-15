package io.dcloud.common.util.net;

import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.PdrUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class NetWork implements Runnable {
    public static long AUTO_RECONNECTTIME = 30000;
    public static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_COMMON = "text/plain;charset=utf-8";
    private static final String CONTENT_TYPE_UPLOAD = "application/x-www-form-urlencoded";
    private static final String DEFALUT_CHARSET = "utf-8";
    private static final String PARAM_CHARSET = ";charset=";
    public static final int WORK_COMMON = 3;
    public static final int WORK_DOWNLOAD = 2;
    public static final int WORK_UPLOAD = 1;
    public int MAX_TIMES = 3;
    protected boolean isAbort;
    protected Map<String, String> mHeaderList;
    protected String mMainBoundry;
    NetWorkLoop mNetWorkLoop = null;
    public int mPriority;
    protected IReqListener mReqListener;
    protected HttpURLConnection mRequest;
    protected RequestData mRequestData;
    protected InputStream mResponseInput;
    protected IResponseListener mResponseListener;
    protected String mResponseText;
    protected long mRetryIntervalTime = AUTO_RECONNECTTIME;
    public int mTimes = 1;
    private int mWorkType;

    public NetWork(int i, RequestData requestData, IReqListener iReqListener, IResponseListener iResponseListener) {
        this.mWorkType = i;
        this.mRequestData = requestData;
        this.mReqListener = iReqListener;
        this.mResponseListener = iResponseListener;
        this.mHeaderList = new HashMap();
        this.mMainBoundry = getBoundry();
    }

    public static String getBoundry() {
        StringBuffer stringBuffer = new StringBuffer("------");
        for (int i = 1; i < 7; i++) {
            long currentTimeMillis = System.currentTimeMillis() + ((long) i);
            long j = currentTimeMillis % 3;
            if (j == 0) {
                stringBuffer.append(((char) ((int) currentTimeMillis)) % 9);
            } else if (j == 1) {
                stringBuffer.append((char) ((int) ((currentTimeMillis % 26) + 65)));
            } else {
                stringBuffer.append((char) ((int) ((currentTimeMillis % 26) + 97)));
            }
        }
        return stringBuffer.toString();
    }

    private String getCharset(String str) {
        if (str != null) {
            String replace = str.replace(Operators.SPACE_STR, "");
            if (replace.contains(PARAM_CHARSET)) {
                return replace.substring(replace.indexOf(PARAM_CHARSET) + 9);
            }
        }
        return null;
    }

    private void setHeadersAndValues(Map<String, List<String>> map) {
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                if (!PdrUtil.isEmpty(next.getValue())) {
                    String str = "";
                    for (int i = 0; i < ((List) next.getValue()).size(); i++) {
                        if (i == 0) {
                            str = (String) ((List) next.getValue()).get(i);
                        } else {
                            str = str + "  " + ((String) ((List) next.getValue()).get(i));
                        }
                    }
                    if (!PdrUtil.isEmpty(next.getKey())) {
                        this.mHeaderList.put((String) next.getKey(), str);
                    }
                }
            }
        }
    }

    public void cancelWork() {
        this.isAbort = true;
        HttpURLConnection httpURLConnection = this.mRequest;
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
            this.mRequest = null;
        }
    }

    public void dispose() {
        this.mReqListener = null;
        this.mResponseListener = null;
    }

    public Map<String, String> getHeadersAndValues() {
        return this.mHeaderList;
    }

    public InputStream getResponseInput() {
        return this.mResponseInput;
    }

    public String getResponseText() {
        return this.mResponseText;
    }

    public void handleResponseText(InputStream inputStream) throws IOException {
        try {
            String charset = getCharset(this.mRequestData.mOverrideMimeType);
            if (charset == null) {
                charset = getCharset(this.mRequest.getContentType());
            }
            if (charset == null) {
                charset = DEFALUT_CHARSET;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int i = 10240;
            int min = Math.min(10240, inputStream.available());
            if (min > 0) {
                i = min;
            }
            byte[] bArr = new byte[i];
            while (true) {
                int read = inputStream.read(bArr);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    inputStream.close();
                    this.mResponseText = new String(byteArrayOutputStream.toByteArray(), charset);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.mResponseText = "";
        }
    }

    public void initHttpsURLConnectionVel() {
        HttpURLConnection httpURLConnection = this.mRequest;
        if (httpURLConnection != null) {
            if (httpURLConnection instanceof HttpsURLConnection) {
                try {
                    SSLSocketFactory sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory();
                    if (sSLSocketFactory != null) {
                        ((HttpsURLConnection) this.mRequest).setSSLSocketFactory(sSLSocketFactory);
                    }
                    ((HttpsURLConnection) this.mRequest).setHostnameVerifier(NetTool.getDefaultHostnameVerifier());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            this.mRequest.setConnectTimeout(this.mRequestData.mTimeout);
            this.mRequest.setReadTimeout(this.mRequestData.mTimeout);
            this.mRequest.setInstanceFollowRedirects(true);
            this.mRequest.setDoInput(true);
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x004e */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0057 A[Catch:{ Exception -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006d A[Catch:{ Exception -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0093 A[Catch:{ Exception -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:62:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            r0 = -1
            io.dcloud.common.util.net.RequestData r1 = r7.mRequestData     // Catch:{ IllegalArgumentException -> 0x019a }
            java.net.HttpURLConnection r1 = r1.getHttpRequest()     // Catch:{ IllegalArgumentException -> 0x019a }
            r7.mRequest = r1     // Catch:{ IllegalArgumentException -> 0x019a }
            if (r1 != 0) goto L_0x002c
            java.lang.String r1 = "url error"
            io.dcloud.common.DHInterface.IResponseListener r2 = r7.mResponseListener     // Catch:{ IllegalArgumentException -> 0x019a }
            r2.onResponseState(r0, r1)     // Catch:{ IllegalArgumentException -> 0x019a }
            io.dcloud.common.DHInterface.IReqListener r1 = r7.mReqListener     // Catch:{ IllegalArgumentException -> 0x019a }
            io.dcloud.common.DHInterface.IReqListener$NetState r2 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING     // Catch:{ IllegalArgumentException -> 0x019a }
            boolean r3 = r7.isAbort     // Catch:{ IllegalArgumentException -> 0x019a }
            r1.onNetStateChanged(r2, r3)     // Catch:{ IllegalArgumentException -> 0x019a }
            io.dcloud.common.DHInterface.IReqListener r1 = r7.mReqListener     // Catch:{ IllegalArgumentException -> 0x019a }
            io.dcloud.common.DHInterface.IReqListener$NetState r2 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_ERROR     // Catch:{ IllegalArgumentException -> 0x019a }
            boolean r3 = r7.isAbort     // Catch:{ IllegalArgumentException -> 0x019a }
            r1.onNetStateChanged(r2, r3)     // Catch:{ IllegalArgumentException -> 0x019a }
            io.dcloud.common.util.net.NetWorkLoop r1 = r7.mNetWorkLoop     // Catch:{ IllegalArgumentException -> 0x019a }
            if (r1 == 0) goto L_0x002b
            r1.removeNetWork(r7)     // Catch:{ IllegalArgumentException -> 0x019a }
        L_0x002b:
            return
        L_0x002c:
            io.dcloud.common.util.net.RequestData r0 = r7.mRequestData
            java.lang.String r1 = "Content-Type"
            boolean r0 = r0.containHeader(r1)
            r2 = 2
            if (r0 != 0) goto L_0x004e
            int r0 = r7.mWorkType     // Catch:{ Exception -> 0x004e }
            r3 = 1
            if (r0 != r3) goto L_0x0044
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x004e }
            java.lang.String r3 = "application/x-www-form-urlencoded"
            r0.setRequestProperty(r1, r3)     // Catch:{ Exception -> 0x004e }
            goto L_0x004e
        L_0x0044:
            if (r0 != r2) goto L_0x0047
            goto L_0x004e
        L_0x0047:
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x004e }
            java.lang.String r3 = "text/plain;charset=utf-8"
            r0.setRequestProperty(r1, r3)     // Catch:{ Exception -> 0x004e }
        L_0x004e:
            r7.initHttpsURLConnectionVel()     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.util.net.RequestData r0 = r7.mRequestData     // Catch:{ Exception -> 0x0164 }
            boolean r0 = r0.isRedirect     // Catch:{ Exception -> 0x0164 }
            if (r0 != 0) goto L_0x0060
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_REQUEST_BEGIN     // Catch:{ Exception -> 0x0164 }
            boolean r3 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r0.onNetStateChanged(r1, r3)     // Catch:{ Exception -> 0x0164 }
        L_0x0060:
            io.dcloud.common.util.net.RequestData r0 = r7.mRequestData     // Catch:{ Exception -> 0x0164 }
            java.net.HttpURLConnection r1 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            r0.addBody((java.net.HttpURLConnection) r1)     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.util.net.RequestData r0 = r7.mRequestData     // Catch:{ Exception -> 0x0164 }
            boolean r0 = r0.isRedirect     // Catch:{ Exception -> 0x0164 }
            if (r0 != 0) goto L_0x0076
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_BEGIN     // Catch:{ Exception -> 0x0164 }
            boolean r3 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r0.onNetStateChanged(r1, r3)     // Catch:{ Exception -> 0x0164 }
        L_0x0076:
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            int r0 = r0.getResponseCode()     // Catch:{ Exception -> 0x0164 }
            java.net.HttpURLConnection r1 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.lang.String r1 = r1.getResponseMessage()     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IResponseListener r3 = r7.mResponseListener     // Catch:{ Exception -> 0x0164 }
            r3.onResponseState(r0, r1)     // Catch:{ Exception -> 0x0164 }
            java.lang.String r3 = " "
            switch(r0) {
                case 200: goto L_0x0093;
                case 201: goto L_0x0093;
                case 202: goto L_0x0093;
                case 203: goto L_0x0093;
                case 204: goto L_0x0093;
                case 205: goto L_0x0093;
                case 206: goto L_0x0093;
                default: goto L_0x008c;
            }
        L_0x008c:
            switch(r0) {
                case 301: goto L_0x0101;
                case 302: goto L_0x0101;
                case 303: goto L_0x0101;
                default: goto L_0x008f;
            }
        L_0x008f:
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            goto L_0x013a
        L_0x0093:
            java.net.HttpURLConnection r4 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.util.Map r4 = r4.getHeaderFields()     // Catch:{ Exception -> 0x0164 }
            r7.setHeadersAndValues(r4)     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener r4 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener$NetState r5 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING     // Catch:{ Exception -> 0x0164 }
            boolean r6 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r4.onNetStateChanged(r5, r6)     // Catch:{ Exception -> 0x0164 }
            int r4 = r7.mWorkType     // Catch:{ Exception -> 0x0164 }
            if (r4 == r2) goto L_0x00ec
            java.net.HttpURLConnection r2 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.io.InputStream r2 = r2.getInputStream()     // Catch:{ Exception -> 0x0164 }
            if (r2 == 0) goto L_0x00d7
            java.net.HttpURLConnection r2 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.io.InputStream r2 = r2.getInputStream()     // Catch:{ Exception -> 0x0164 }
            r7.handleResponseText(r2)     // Catch:{ Exception -> 0x0164 }
            java.lang.String r2 = r7.mResponseText     // Catch:{ Exception -> 0x0164 }
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r2)     // Catch:{ Exception -> 0x0164 }
            if (r2 == 0) goto L_0x00f9
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0164 }
            r2.<init>()     // Catch:{ Exception -> 0x0164 }
            r2.append(r0)     // Catch:{ Exception -> 0x0164 }
            r2.append(r3)     // Catch:{ Exception -> 0x0164 }
            r2.append(r1)     // Catch:{ Exception -> 0x0164 }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x0164 }
            r7.mResponseText = r0     // Catch:{ Exception -> 0x0164 }
            goto L_0x00f9
        L_0x00d7:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0164 }
            r2.<init>()     // Catch:{ Exception -> 0x0164 }
            r2.append(r0)     // Catch:{ Exception -> 0x0164 }
            r2.append(r3)     // Catch:{ Exception -> 0x0164 }
            r2.append(r1)     // Catch:{ Exception -> 0x0164 }
            java.lang.String r0 = r2.toString()     // Catch:{ Exception -> 0x0164 }
            r7.mResponseText = r0     // Catch:{ Exception -> 0x0164 }
            goto L_0x00f9
        L_0x00ec:
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.io.InputStream r0 = r0.getInputStream()     // Catch:{ Exception -> 0x0164 }
            r7.mResponseInput = r0     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener r1 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            r1.onResponsing(r0)     // Catch:{ Exception -> 0x0164 }
        L_0x00f9:
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            boolean r1 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r0.onNetStateChanged(r5, r1)     // Catch:{ Exception -> 0x0164 }
            goto L_0x015a
        L_0x0101:
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.lang.String r1 = "Location"
            java.lang.String r0 = r0.getHeaderField(r1)     // Catch:{ Exception -> 0x0164 }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0164 }
            if (r1 != 0) goto L_0x015a
            java.io.PrintStream r1 = java.lang.System.out     // Catch:{ Exception -> 0x0164 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0164 }
            r2.<init>()     // Catch:{ Exception -> 0x0164 }
            java.lang.String r4 = "重定向的URL:"
            r2.append(r4)     // Catch:{ Exception -> 0x0164 }
            r2.append(r0)     // Catch:{ Exception -> 0x0164 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0164 }
            r1.println(r2)     // Catch:{ Exception -> 0x0164 }
            java.lang.String r1 = "%20"
            java.lang.String r0 = r0.replace(r3, r1)     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.util.net.RequestData r1 = r7.mRequestData     // Catch:{ Exception -> 0x0164 }
            r1.clearData()     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.util.net.RequestData r1 = r7.mRequestData     // Catch:{ Exception -> 0x0164 }
            r1.setUrl(r0)     // Catch:{ Exception -> 0x0164 }
            r7.run()     // Catch:{ Exception -> 0x0164 }
            return
        L_0x013a:
            java.util.Map r0 = r0.getHeaderFields()     // Catch:{ Exception -> 0x0164 }
            r7.setHeadersAndValues(r0)     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING     // Catch:{ Exception -> 0x0164 }
            boolean r2 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r0.onNetStateChanged(r1, r2)     // Catch:{ Exception -> 0x0164 }
            java.net.HttpURLConnection r0 = r7.mRequest     // Catch:{ Exception -> 0x0164 }
            java.io.InputStream r0 = r0.getErrorStream()     // Catch:{ Exception -> 0x0164 }
            r7.handleResponseText(r0)     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            boolean r2 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r0.onNetStateChanged(r1, r2)     // Catch:{ Exception -> 0x0164 }
        L_0x015a:
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener     // Catch:{ Exception -> 0x0164 }
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_END     // Catch:{ Exception -> 0x0164 }
            boolean r2 = r7.isAbort     // Catch:{ Exception -> 0x0164 }
            r0.onNetStateChanged(r1, r2)     // Catch:{ Exception -> 0x0164 }
            goto L_0x0192
        L_0x0164:
            r0 = move-exception
            r0.printStackTrace()
            java.lang.String r1 = r0.getMessage()
            io.dcloud.common.DHInterface.IResponseListener r2 = r7.mResponseListener
            r3 = 0
            r2.onResponseState(r3, r1)
            io.dcloud.common.DHInterface.IReqListener r1 = r7.mReqListener
            io.dcloud.common.DHInterface.IReqListener$NetState r2 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING
            boolean r3 = r7.isAbort
            r1.onNetStateChanged(r2, r3)
            boolean r0 = r0 instanceof java.net.SocketTimeoutException
            if (r0 == 0) goto L_0x0189
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_TIMEOUT
            boolean r2 = r7.isAbort
            r0.onNetStateChanged(r1, r2)
            goto L_0x0192
        L_0x0189:
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_ERROR
            boolean r2 = r7.isAbort
            r0.onNetStateChanged(r1, r2)
        L_0x0192:
            io.dcloud.common.util.net.NetWorkLoop r0 = r7.mNetWorkLoop
            if (r0 == 0) goto L_0x0199
            r0.removeNetWork(r7)
        L_0x0199:
            return
        L_0x019a:
            r1 = move-exception
            java.lang.String r1 = r1.getMessage()
            io.dcloud.common.DHInterface.IResponseListener r2 = r7.mResponseListener
            r2.onResponseState(r0, r1)
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING
            boolean r2 = r7.isAbort
            r0.onNetStateChanged(r1, r2)
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_ERROR
            boolean r2 = r7.isAbort
            r0.onNetStateChanged(r1, r2)
            io.dcloud.common.util.net.NetWorkLoop r0 = r7.mNetWorkLoop
            if (r0 == 0) goto L_0x01bd
            r0.removeNetWork(r7)
        L_0x01bd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.net.NetWork.run():void");
    }

    public void setRetryIntervalTime(long j) {
        if (j > 0) {
            this.mRetryIntervalTime = j;
        }
    }

    public void startWork() {
        Thread thread = new Thread(this);
        thread.setPriority(1);
        thread.start();
        this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_INIT, this.isAbort);
    }
}
