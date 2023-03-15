package io.dcloud.common.util.net;

import android.text.TextUtils;
import io.dcloud.common.util.PdrUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class RequestData {
    public static final String URL_HTTP = "http";
    public static final String URL_HTTPS = "https";
    public String URL_METHOD = "http";
    public boolean isRedirect = false;
    private String mBody;
    public int mChunkSize;
    private long mContentLength;
    private HashMap<String, String> mHeads;
    private HttpURLConnection mHttpRequest;
    private String mIp;
    private HashMap<String, String> mNameValue;
    public String mOverrideMimeType = null;
    private String mPort;
    private String mReqmethod;
    public int mTimeout = 120000;
    private String mUrl;
    public String unTrustedCAType = "accept";

    /* renamed from: io.dcloud.common.util.net.RequestData$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                io.dcloud.common.util.net.RequestData$HttpOption[] r0 = io.dcloud.common.util.net.RequestData.HttpOption.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption = r0
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.POST     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.PUT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.DELETE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.HEAD     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.TRACE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption     // Catch:{ NoSuchFieldError -> 0x0049 }
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.OPTIONS     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption     // Catch:{ NoSuchFieldError -> 0x0054 }
                io.dcloud.common.util.net.RequestData$HttpOption r1 = io.dcloud.common.util.net.RequestData.HttpOption.GET     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.net.RequestData.AnonymousClass1.<clinit>():void");
        }
    }

    enum HttpOption {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        TRACE,
        OPTIONS
    }

    public RequestData(String str, String str2) {
        this.mUrl = str;
        this.mReqmethod = str2;
        if (str != null && str.startsWith("https")) {
            this.URL_METHOD = "https";
        }
        this.mNameValue = new HashMap<>();
        this.mHeads = new HashMap<>();
    }

    public boolean addBody(String str) {
        if (PdrUtil.isEmpty(str)) {
            return false;
        }
        this.mBody = str;
        return true;
    }

    public boolean addHeader(String str, String str2) {
        if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2) || this.mHeads.containsKey(str)) {
            return false;
        }
        this.mHeads.put(str, str2);
        return true;
    }

    public boolean addParemeter(String str, String str2) {
        if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2) || this.mNameValue.containsKey(str)) {
            return false;
        }
        this.mNameValue.put(str, str2);
        return true;
    }

    public void clearData() {
        this.mHttpRequest.disconnect();
        this.mHttpRequest = null;
    }

    public boolean containHeader(String str) {
        if (!(str == null || this.mHeads == null || !str.equals(NetWork.CONTENT_TYPE))) {
            for (String equalsIgnoreCase : this.mHeads.keySet()) {
                if (equalsIgnoreCase.equalsIgnoreCase(NetWork.CONTENT_TYPE)) {
                    return true;
                }
            }
        }
        HashMap<String, String> hashMap = this.mHeads;
        if (hashMap != null) {
            return hashMap.containsKey(str);
        }
        return false;
    }

    public HttpURLConnection getHttpRequest() throws IllegalArgumentException {
        if (this.mHttpRequest == null) {
            HttpOption valueOf = HttpOption.valueOf(this.mReqmethod.toUpperCase());
            try {
                URLConnection openConnection = new URL(this.mUrl).openConnection();
                if (!(openConnection instanceof HttpURLConnection)) {
                    return null;
                }
                this.mHttpRequest = (HttpURLConnection) openConnection;
                switch (AnonymousClass1.$SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[valueOf.ordinal()]) {
                    case 1:
                        this.mHttpRequest.setRequestMethod("POST");
                        break;
                    case 2:
                        this.mHttpRequest.setRequestMethod("PUT");
                        break;
                    case 3:
                        this.mHttpRequest.setRequestMethod("DELETE");
                        break;
                    case 4:
                        this.mHttpRequest.setRequestMethod("HEAD");
                        break;
                    case 5:
                        this.mHttpRequest.setRequestMethod("TRACE");
                        break;
                    case 6:
                        this.mHttpRequest.setRequestMethod("OPTIONS");
                        break;
                    default:
                        this.mHttpRequest.setRequestMethod("GET");
                        break;
                }
                addHeader(this.mHttpRequest);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        return this.mHttpRequest;
    }

    public String getIP() {
        return this.mIp;
    }

    public String getPort() {
        return this.mPort;
    }

    public String getReqmethod() {
        return this.mReqmethod;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setReqmethod(String str) {
        this.mReqmethod = str;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }

    public void addBody(HttpURLConnection httpURLConnection) {
        if (!TextUtils.isEmpty(this.mBody)) {
            try {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                String str = this.mBody;
                if (str != null && str.length() > 0) {
                    outputStream.write(this.mBody.getBytes("UTF-8"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addHeader(HttpURLConnection httpURLConnection) {
        if (httpURLConnection != null) {
            for (String next : this.mHeads.keySet()) {
                httpURLConnection.addRequestProperty(next, this.mHeads.get(next));
            }
        }
    }
}
