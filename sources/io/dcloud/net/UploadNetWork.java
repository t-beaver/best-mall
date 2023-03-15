package io.dcloud.net;

import android.util.Log;
import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.common.util.net.RequestData;
import io.dcloud.common.util.net.UploadMgr;
import io.dcloud.net.JsUpload;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

public class UploadNetWork extends NetWork {
    public static String REQMETHOD_GET = "GET";
    public static String REQMETHOD_POST = "POST";
    public static final String TAG = "UploadNetWork";
    long RANGE_BUF = 102400;
    long mContentLength;
    public int mStatus = 0;
    private boolean mSupport = false;
    long mTotalSize;
    LinkedHashMap<String, JsUpload.UploadItem> mUploadItems = new LinkedHashMap<>(4);
    long mUploadedSize;
    StringBuffer mUploadingFile = new StringBuffer();
    String responseHeaders;

    public UploadNetWork(int i, RequestData requestData, IReqListener iReqListener, IResponseListener iResponseListener) {
        super(i, requestData, iReqListener, iResponseListener);
    }

    private void addCutoffLine(String str) {
        this.mContentLength = appendPostParemeter("--" + str + "\r\n", this.mContentLength);
    }

    private void addFileInputStream(String str, JsUpload.UploadFile uploadFile) {
        if (this.mSupport) {
            this.mContentLength = appendPostParemeter("Content-Disposition: attachments; name=\"" + str + "\"; filename=\"" + uploadFile.mFilename + "\"; range=\"0-777/777\"\r\n", this.mContentLength);
        } else {
            this.mContentLength = appendPostParemeter("Content-Disposition: form-data; name=\"" + str + "\"; filename=\"" + uploadFile.mFilename + "\"\r\n", this.mContentLength);
        }
        long appendPostParemeter = appendPostParemeter("Content-Type: " + uploadFile.mMimetype + "\r\n\r\n", this.mContentLength);
        this.mContentLength = appendPostParemeter;
        long appendPostParemeter2 = appendPostParemeter("\r\n", appendPostParemeter);
        this.mContentLength = appendPostParemeter2;
        this.mContentLength = appendPostParemeter2 + uploadFile.mFileSize;
    }

    private void addPropertyInputStream(String str, String str2) {
        this.mContentLength = appendPostParemeter("Content-Disposition: form-data; name=\"" + str + "\"\r\n\r\n", this.mContentLength);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("\r\n");
        this.mContentLength = appendPostParemeter(sb.toString(), this.mContentLength);
    }

    private void initUploadData() throws Exception {
        DataOutputStream dataOutputStream;
        String str = this.mMainBoundry;
        HttpURLConnection httpRequest = this.mRequestData.getHttpRequest();
        this.mRequest = httpRequest;
        if (httpRequest == null) {
            new Exception("url error");
        }
        initHttpsURLConnectionVel();
        this.mRequest.setDoOutput(true);
        this.mRequest.setDoInput(true);
        this.mRequest.setRequestProperty("Connection", "Keep-Alive");
        this.mRequest.setRequestProperty("Charset", "UTF-8");
        HttpURLConnection httpURLConnection = this.mRequest;
        httpURLConnection.setRequestProperty(NetWork.CONTENT_TYPE, "multipart/form-data; boundary=" + str);
        int i = 0;
        this.mRequest.setUseCaches(false);
        initContentLength();
        int i2 = 10240;
        int i3 = this.mRequestData.mChunkSize;
        if (i3 > 0) {
            i2 = i3 * 1024;
            this.mRequest.setChunkedStreamingMode(i2);
        } else {
            this.mRequest.setFixedLengthStreamingMode(this.mContentLength);
        }
        DataOutputStream dataOutputStream2 = new DataOutputStream(this.mRequest.getOutputStream());
        this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_REQUEST_BEGIN, this.isAbort);
        LinkedHashMap<String, JsUpload.UploadItem> linkedHashMap = this.mUploadItems;
        if (linkedHashMap != null && linkedHashMap.size() > 0) {
            Set<String> keySet = this.mUploadItems.keySet();
            this.mTotalSize = this.mContentLength;
            for (String next : keySet) {
                JsUpload.UploadItem uploadItem = this.mUploadItems.get(next);
                dataOutputStream2.writeBytes("--" + str + "\r\n");
                if (uploadItem instanceof JsUpload.UploadFile) {
                    JsUpload.UploadFile uploadFile = (JsUpload.UploadFile) uploadItem;
                    InputStream inputStream = uploadFile.mFileInputS;
                    if (inputStream != null) {
                        this.mUploadingFile.append(next);
                        dataOutputStream2.write(("Content-Disposition: form-data; name=\"" + next + "\"; filename=\"" + uploadFile.mFilename + JSUtil.QUOTE + "\r\n").getBytes());
                        StringBuilder sb = new StringBuilder();
                        sb.append("Content-Type: ");
                        sb.append(uploadFile.mMimetype);
                        sb.append("\r\n");
                        dataOutputStream2.write(sb.toString().getBytes());
                        dataOutputStream2.writeBytes("\r\n");
                        int min = Math.min(inputStream.available(), i2);
                        byte[] bArr = new byte[min];
                        long j = 0;
                        int read = inputStream.read(bArr, i, min);
                        while (read > 0) {
                            j += (long) read;
                            this.mUploadedSize = j;
                            Log.e("UploadNetWort", "initUploadData: mUploadedSize==" + this.mUploadedSize + " ===== length =" + read + " ====== buffer =" + min);
                            DataOutputStream dataOutputStream3 = dataOutputStream2;
                            dataOutputStream3.write(bArr, 0, read);
                            this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                            read = inputStream.read(bArr, 0, Math.min(inputStream.available(), i2));
                            dataOutputStream2 = dataOutputStream3;
                        }
                        dataOutputStream = dataOutputStream2;
                        dataOutputStream.writeBytes("\r\n");
                        inputStream.close();
                    }
                } else {
                    dataOutputStream = dataOutputStream2;
                    if (uploadItem instanceof JsUpload.UploadString) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Content-Disposition: form-data; name=\"" + next + JSUtil.QUOTE + "\r\n");
                        sb2.append("\r\n");
                        sb2.append(((JsUpload.UploadString) uploadItem).mData);
                        sb2.append("\r\n");
                        dataOutputStream.write(sb2.toString().getBytes());
                        sb2.delete(0, sb2.length());
                        this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                        dataOutputStream2 = dataOutputStream;
                        i = 0;
                    }
                }
                this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                dataOutputStream2 = dataOutputStream;
                i = 0;
            }
        }
        DataOutputStream dataOutputStream4 = dataOutputStream2;
        dataOutputStream4.writeBytes("--" + str + "--" + "\r\n");
        this.mUploadedSize = (long) dataOutputStream4.size();
        dataOutputStream4.flush();
        responseUpload();
        dataOutputStream4.close();
    }

    private static boolean isRightRequest(int i) {
        return i >= 200 && i < 300;
    }

    private void uploadContent() {
        this.mTimes = 1;
        connet(true);
        dispose();
    }

    public boolean addFile(String str, JsUpload.UploadFile uploadFile) {
        if (this.mUploadItems.containsKey(uploadFile)) {
            return false;
        }
        this.mUploadItems.put(str, uploadFile);
        return true;
    }

    public boolean addParemeter(String str, String str2) {
        if (this.mUploadItems.containsKey(str)) {
            return false;
        }
        this.mUploadItems.put(str, new JsUpload.UploadString(str2));
        return true;
    }

    public long appendPostParemeter(String str, long j) {
        try {
            return ((long) new ByteArrayInputStream(str.getBytes("utf-8")).available()) + j;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void connet(boolean z) {
        if (z) {
            try {
                this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_CONNECTED, this.isAbort);
            } catch (Exception e) {
                Logger.d("upload is ERROR:" + e.getLocalizedMessage());
                this.mResponseText = e.getMessage();
                this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_ERROR, this.isAbort);
                e.printStackTrace();
                long currentTimeMillis = System.currentTimeMillis();
                long j = this.mRetryIntervalTime;
                int i = this.mTimes;
                long j2 = currentTimeMillis + ((j * ((long) (1 << i))) / 2);
                if (i < this.MAX_TIMES) {
                    this.mTimes = i + 1;
                    do {
                    } while (System.currentTimeMillis() <= j2);
                    connet(z);
                    return;
                }
                return;
            }
        }
        initUploadData();
        this.responseHeaders = getResponseHeaders();
    }

    public void dispose() {
        this.mHeaderList = null;
        this.mRequest = null;
        this.mUploadedSize = 0;
        this.mTotalSize = 0;
        UploadMgr.getUploadMgr().removeNetWork(this);
    }

    public String getResponseHeaders() {
        try {
            HttpURLConnection httpURLConnection = this.mRequest;
            if (httpURLConnection == null) {
                return "";
            }
            Map headerFields = httpURLConnection.getHeaderFields();
            HashMap hashMap = new HashMap();
            for (Map.Entry entry : headerFields.entrySet()) {
                String str = "";
                for (String str2 : (List) entry.getValue()) {
                    str = str + "  " + str2;
                }
                if (!PdrUtil.isEmpty(entry.getKey())) {
                    hashMap.put((String) entry.getKey(), str);
                }
            }
            return new JSONObject(hashMap).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public void initContentLength() {
        this.mContentLength = 0;
        LinkedHashMap<String, JsUpload.UploadItem> linkedHashMap = this.mUploadItems;
        if (linkedHashMap != null && linkedHashMap.size() > 0) {
            for (String next : this.mUploadItems.keySet()) {
                JsUpload.UploadItem uploadItem = this.mUploadItems.get(next);
                if (uploadItem instanceof JsUpload.UploadFile) {
                    this.mUploadingFile.append(next);
                    addCutoffLine(this.mMainBoundry);
                    addFileInputStream(next, (JsUpload.UploadFile) uploadItem);
                } else if (uploadItem instanceof JsUpload.UploadString) {
                    addCutoffLine(this.mMainBoundry);
                    addPropertyInputStream(next, ((JsUpload.UploadString) uploadItem).mData);
                }
            }
        }
        this.mContentLength = appendPostParemeter("--" + this.mMainBoundry + "--\r\n", this.mContentLength);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(18:1|2|(1:4)|5|(1:7)|8|(1:10)(1:11)|12|(2:13|(1:15)(1:28))|16|17|18|19|20|21|(1:23)(1:24)|25|30) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0072 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void responseUpload() {
        /*
            r7 = this;
            java.lang.String r0 = "uploadnetwork"
            java.lang.String r1 = ";url="
            java.net.HttpURLConnection r2 = r7.mRequest     // Catch:{ Exception -> 0x00bd }
            if (r2 == 0) goto L_0x000e
            int r2 = r2.getResponseCode()     // Catch:{ Exception -> 0x00bd }
            r7.mStatus = r2     // Catch:{ Exception -> 0x00bd }
        L_0x000e:
            java.net.HttpURLConnection r2 = r7.mRequest     // Catch:{ Exception -> 0x00bd }
            java.lang.String r3 = "Set-Cookie"
            java.lang.String r2 = r2.getHeaderField(r3)     // Catch:{ Exception -> 0x00bd }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00bd }
            if (r3 != 0) goto L_0x0029
            android.webkit.CookieManager r3 = android.webkit.CookieManager.getInstance()     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.util.net.RequestData r4 = r7.mRequestData     // Catch:{ Exception -> 0x00bd }
            java.lang.String r4 = r4.getUrl()     // Catch:{ Exception -> 0x00bd }
            r3.setCookie(r4, r2)     // Catch:{ Exception -> 0x00bd }
        L_0x0029:
            int r2 = r7.mStatus     // Catch:{ Exception -> 0x00bd }
            r3 = 400(0x190, float:5.6E-43)
            if (r2 <= r3) goto L_0x0036
            java.net.HttpURLConnection r2 = r7.mRequest     // Catch:{ Exception -> 0x00bd }
            java.io.InputStream r2 = r2.getErrorStream()     // Catch:{ Exception -> 0x00bd }
            goto L_0x003c
        L_0x0036:
            java.net.HttpURLConnection r2 = r7.mRequest     // Catch:{ Exception -> 0x00bd }
            java.io.InputStream r2 = r2.getInputStream()     // Catch:{ Exception -> 0x00bd }
        L_0x003c:
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x00bd }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x00bd }
            r4.<init>()     // Catch:{ Exception -> 0x00bd }
        L_0x0045:
            int r5 = r2.read(r3)     // Catch:{ Exception -> 0x00bd }
            if (r5 <= 0) goto L_0x0050
            r6 = 0
            r4.write(r3, r6, r5)     // Catch:{ Exception -> 0x00bd }
            goto L_0x0045
        L_0x0050:
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x00bd }
            byte[] r5 = r4.toByteArray()     // Catch:{ Exception -> 0x00bd }
            java.lang.String r6 = "utf-8"
            r3.<init>(r5, r6)     // Catch:{ Exception -> 0x00bd }
            r7.mResponseText = r3     // Catch:{ Exception -> 0x00bd }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x0072 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r6 = "result"
            io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r5, (java.lang.String) r6)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r6 = "code"
            io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r5, (java.lang.String) r6)     // Catch:{ Exception -> 0x0072 }
            java.lang.String r6 = "message"
            io.dcloud.common.util.JSONUtil.getString((org.json.JSONObject) r5, (java.lang.String) r6)     // Catch:{ Exception -> 0x0072 }
            goto L_0x0092
        L_0x0072:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00bd }
            r5.<init>()     // Catch:{ Exception -> 0x00bd }
            java.lang.String r6 = "responseUpload JSONObject _data="
            r5.append(r6)     // Catch:{ Exception -> 0x00bd }
            r5.append(r3)     // Catch:{ Exception -> 0x00bd }
            r5.append(r1)     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.util.net.RequestData r3 = r7.mRequestData     // Catch:{ Exception -> 0x00bd }
            java.lang.String r3 = r3.getUrl()     // Catch:{ Exception -> 0x00bd }
            r5.append(r3)     // Catch:{ Exception -> 0x00bd }
            java.lang.String r3 = r5.toString()     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.adapter.util.Logger.e(r0, r3)     // Catch:{ Exception -> 0x00bd }
        L_0x0092:
            int r3 = r7.mStatus     // Catch:{ Exception -> 0x00bd }
            boolean r3 = isRightRequest(r3)     // Catch:{ Exception -> 0x00bd }
            if (r3 == 0) goto L_0x00ad
            io.dcloud.common.DHInterface.IReqListener r3 = r7.mReqListener     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.DHInterface.IReqListener$NetState r5 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_ING     // Catch:{ Exception -> 0x00bd }
            boolean r6 = r7.isAbort     // Catch:{ Exception -> 0x00bd }
            r3.onNetStateChanged(r5, r6)     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.DHInterface.IReqListener r3 = r7.mReqListener     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.DHInterface.IReqListener$NetState r5 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_HANDLE_END     // Catch:{ Exception -> 0x00bd }
            boolean r6 = r7.isAbort     // Catch:{ Exception -> 0x00bd }
            r3.onNetStateChanged(r5, r6)     // Catch:{ Exception -> 0x00bd }
            goto L_0x00b6
        L_0x00ad:
            io.dcloud.common.DHInterface.IReqListener r3 = r7.mReqListener     // Catch:{ Exception -> 0x00bd }
            io.dcloud.common.DHInterface.IReqListener$NetState r5 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_ERROR     // Catch:{ Exception -> 0x00bd }
            boolean r6 = r7.isAbort     // Catch:{ Exception -> 0x00bd }
            r3.onNetStateChanged(r5, r6)     // Catch:{ Exception -> 0x00bd }
        L_0x00b6:
            r4.close()     // Catch:{ Exception -> 0x00bd }
            r2.close()     // Catch:{ Exception -> 0x00bd }
            goto L_0x00f1
        L_0x00bd:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "responseUpload "
            r3.append(r4)
            java.lang.String r4 = r2.getLocalizedMessage()
            r3.append(r4)
            r3.append(r1)
            io.dcloud.common.util.net.RequestData r1 = r7.mRequestData
            java.lang.String r1 = r1.getUrl()
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            io.dcloud.common.adapter.util.Logger.e(r0, r1)
            java.lang.String r0 = r2.getMessage()
            r7.mResponseText = r0
            io.dcloud.common.DHInterface.IReqListener r0 = r7.mReqListener
            io.dcloud.common.DHInterface.IReqListener$NetState r1 = io.dcloud.common.DHInterface.IReqListener.NetState.NET_ERROR
            boolean r2 = r7.isAbort
            r0.onNetStateChanged(r1, r2)
        L_0x00f1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.net.UploadNetWork.responseUpload():void");
    }

    public void run() {
        uploadContent();
    }
}
