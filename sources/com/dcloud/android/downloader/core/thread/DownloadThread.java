package com.dcloud.android.downloader.core.thread;

import android.os.Process;
import com.dcloud.android.downloader.config.Config;
import com.dcloud.android.downloader.core.DownloadResponse;
import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.domain.DownloadThreadInfo;
import com.dcloud.android.downloader.exception.DownloadException;
import com.dcloud.android.downloader.exception.DownloadPauseException;
import java.io.InputStream;

public class DownloadThread implements Runnable {
    public static final String TAG = "DownloadThread";
    private final Config config;
    private final DownloadInfo downloadInfo;
    private final DownloadProgressListener downloadProgressListener;
    private final DownloadResponse downloadResponse;
    private final DownloadThreadInfo downloadThreadInfo;
    private InputStream inputStream;
    private long lastProgress;
    private int retryDownloadCount = 0;

    public interface DownloadProgressListener {
        void onDownloadSuccess();

        void onProgress();
    }

    public DownloadThread(DownloadThreadInfo downloadThreadInfo2, DownloadResponse downloadResponse2, Config config2, DownloadInfo downloadInfo2, DownloadProgressListener downloadProgressListener2) {
        this.downloadThreadInfo = downloadThreadInfo2;
        this.downloadResponse = downloadResponse2;
        this.config = config2;
        this.downloadInfo = downloadInfo2;
        this.lastProgress = downloadThreadInfo2.getProgress();
        this.downloadProgressListener = downloadProgressListener2;
    }

    private void checkPause() {
        if (this.downloadInfo.isPause()) {
            throw new DownloadPauseException(7);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:73:0x01ea  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0209  */
    /* JADX WARNING: Removed duplicated region for block: B:91:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:76:0x01f2=Splitter:B:76:0x01f2, B:64:0x01d3=Splitter:B:64:0x01d3} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executeDownload() {
        /*
            r13 = this;
            r0 = 5
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch:{ ProtocolException -> 0x01fd, IOException -> 0x01ee, DownloadPauseException -> 0x01e7, NoSuchAlgorithmException -> 0x01db, KeyManagementException -> 0x01cf }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r3 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01fd, IOException -> 0x01ee, DownloadPauseException -> 0x01e7, NoSuchAlgorithmException -> 0x01db, KeyManagementException -> 0x01cf }
            java.lang.String r3 = r3.getUri()     // Catch:{ ProtocolException -> 0x01fd, IOException -> 0x01ee, DownloadPauseException -> 0x01e7, NoSuchAlgorithmException -> 0x01db, KeyManagementException -> 0x01cf }
            r2.<init>(r3)     // Catch:{ ProtocolException -> 0x01fd, IOException -> 0x01ee, DownloadPauseException -> 0x01e7, NoSuchAlgorithmException -> 0x01db, KeyManagementException -> 0x01cf }
            java.net.URLConnection r2 = r2.openConnection()     // Catch:{ ProtocolException -> 0x01fd, IOException -> 0x01ee, DownloadPauseException -> 0x01e7, NoSuchAlgorithmException -> 0x01db, KeyManagementException -> 0x01cf }
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ ProtocolException -> 0x01fd, IOException -> 0x01ee, DownloadPauseException -> 0x01e7, NoSuchAlgorithmException -> 0x01db, KeyManagementException -> 0x01cf }
            boolean r1 = r2 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r3 = 0
            if (r1 == 0) goto L_0x002e
            javax.net.ssl.SSLSocketFactory r1 = io.dcloud.common.adapter.util.DCloudTrustManager.getSSLSocketFactory()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            if (r1 == 0) goto L_0x0024
            r4 = r2
            javax.net.ssl.HttpsURLConnection r4 = (javax.net.ssl.HttpsURLConnection) r4     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r4.setSSLSocketFactory(r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
        L_0x0024:
            r1 = r2
            javax.net.ssl.HttpsURLConnection r1 = (javax.net.ssl.HttpsURLConnection) r1     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            org.apache.http.conn.ssl.X509HostnameVerifier r4 = io.dcloud.common.adapter.util.DCloudTrustManager.getHostnameVerifier(r3)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.setHostnameVerifier(r4)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
        L_0x002e:
            com.dcloud.android.downloader.config.Config r1 = r13.config     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r1 = r1.getConnectTimeout()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r2.setConnectTimeout(r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.config.Config r1 = r13.config     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r1 = r1.getReadTimeout()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r2.setReadTimeout(r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.config.Config r1 = r13.config     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r1 = r1.getMethod()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r2.setRequestMethod(r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r1 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r4 = r1.getStart()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r6 = r13.lastProgress     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r4 = r4 + r6
            com.dcloud.android.downloader.domain.DownloadInfo r1 = r13.downloadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            boolean r1 = r1.isSupportRanges()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r6 = 1
            if (r1 == 0) goto L_0x00ac
            com.dcloud.android.downloader.domain.DownloadThreadInfo r1 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r1.getEnd()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r9 = 0
            int r1 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r1 <= 0) goto L_0x006a
            r13.lastProgress = r9     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r4 = r9
        L_0x006a:
            com.dcloud.android.downloader.config.Config r1 = r13.config     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r1 = r1.getEachDownloadThread()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r7 = "-"
            java.lang.String r8 = "bytes="
            java.lang.String r9 = "Range"
            if (r1 != r6) goto L_0x008e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.<init>()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r4)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r1 = r1.toString()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r2.setRequestProperty(r9, r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            goto L_0x00ac
        L_0x008e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.<init>()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r4)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r7 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r7.getEnd()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.append(r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r1 = r1.toString()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r2.setRequestProperty(r9, r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
        L_0x00ac:
            int r1 = r2.getResponseCode()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r7 = "Content-Length"
            java.lang.String r7 = r2.getHeaderField(r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r7 = java.lang.Integer.parseInt(r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = (long) r7     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r7 + r4
            com.dcloud.android.downloader.config.Config r9 = r13.config     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r9 = r9.getEachDownloadThread()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            if (r9 != r6) goto L_0x00ea
            com.dcloud.android.downloader.domain.DownloadThreadInfo r9 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r9 = r9.getEnd()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 == 0) goto L_0x00ea
            com.dcloud.android.downloader.domain.DownloadThreadInfo r9 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r9 = r9.getEnd()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r7 - r9
            r9 = 1
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 != 0) goto L_0x00e2
            long r4 = r4 - r9
            long r7 = r13.lastProgress     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r7 - r9
            r13.lastProgress = r7     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            goto L_0x00ea
        L_0x00e2:
            com.dcloud.android.downloader.exception.DownloadException r1 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r3 = "IO error Data source change"
            r1.<init>((int) r0, (java.lang.String) r3)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            throw r1     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
        L_0x00ea:
            r7 = 206(0xce, float:2.89E-43)
            if (r1 == r7) goto L_0x010c
            r7 = 200(0xc8, float:2.8E-43)
            if (r1 != r7) goto L_0x00f3
            goto L_0x010c
        L_0x00f3:
            com.dcloud.android.downloader.exception.DownloadException r3 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r4 = 8
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r5.<init>()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r6 = "UnSupported response code:"
            r5.append(r6)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r5.append(r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r1 = r5.toString()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r3.<init>((int) r4, (java.lang.String) r1)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            throw r3     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
        L_0x010c:
            java.io.InputStream r1 = r2.getInputStream()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r13.inputStream = r1     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadInfo r7 = r13.downloadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r7 = r7.getPath()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r8 = "rwd"
            r1.<init>(r7, r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.config.Config r7 = r13.config     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r7 = r7.getEachDownloadThread()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            if (r7 != r6) goto L_0x013a
            long r6 = r1.length()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r8 = r13.lastProgress     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 < 0) goto L_0x0132
            goto L_0x013a
        L_0x0132:
            com.dcloud.android.downloader.exception.DownloadException r1 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r3 = "IO error Have small download size"
            r1.<init>((int) r0, (java.lang.String) r3)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            throw r1     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
        L_0x013a:
            r1.seek(r4)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r4 = 4096(0x1000, float:5.74E-42)
            byte[] r4 = new byte[r4]     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r5 = 0
        L_0x0142:
            r13.checkPause()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.io.InputStream r6 = r13.inputStream     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r6 = r6.read(r4)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7 = -1
            if (r6 != r7) goto L_0x015b
            com.dcloud.android.downloader.core.thread.DownloadThread$DownloadProgressListener r1 = r13.downloadProgressListener     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r1.onDownloadSuccess()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r13.checkPause()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r2.disconnect()
            goto L_0x01ed
        L_0x015b:
            r1.write(r4, r3, r6)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r5 = r5 + r6
            com.dcloud.android.downloader.domain.DownloadThreadInfo r6 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r13.lastProgress     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r9 = (long) r5     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r7 = r7 + r9
            r6.setProgress(r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.core.thread.DownloadThread$DownloadProgressListener r6 = r13.downloadProgressListener     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r6.onProgress()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r6 = "DownloadThread"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7.<init>()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r8 = "downloadInfo:"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadInfo r8 = r13.downloadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r8 = r8.getId()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r8 = " thread:"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r8 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            int r8 = r8.getThreadId()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r8 = " progress:"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r8 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r8 = r8.getProgress()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r8 = ",start:"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r8 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r8 = r8.getStart()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r8 = ",end:"
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            com.dcloud.android.downloader.domain.DownloadThreadInfo r8 = r13.downloadThreadInfo     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            long r8 = r8.getEnd()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            r7.append(r8)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            java.lang.String r7 = r7.toString()     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            android.util.Log.d(r6, r7)     // Catch:{ ProtocolException -> 0x01ca, IOException -> 0x01c8, DownloadPauseException -> 0x01c6, NoSuchAlgorithmException -> 0x01c4, KeyManagementException -> 0x01c2 }
            goto L_0x0142
        L_0x01c2:
            r1 = move-exception
            goto L_0x01d3
        L_0x01c4:
            r1 = move-exception
            goto L_0x01df
        L_0x01c6:
            r1 = r2
            goto L_0x01e8
        L_0x01c8:
            r1 = move-exception
            goto L_0x01f2
        L_0x01ca:
            r0 = move-exception
            r1 = r2
            goto L_0x01fe
        L_0x01cd:
            r0 = move-exception
            goto L_0x0207
        L_0x01cf:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L_0x01d3:
            com.dcloud.android.downloader.exception.DownloadException r3 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ all -> 0x01fa }
            java.lang.String r4 = "Key management"
            r3.<init>(r0, r4, r1)     // Catch:{ all -> 0x01fa }
            throw r3     // Catch:{ all -> 0x01fa }
        L_0x01db:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L_0x01df:
            com.dcloud.android.downloader.exception.DownloadException r3 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ all -> 0x01fa }
            java.lang.String r4 = "NO such"
            r3.<init>(r0, r4, r1)     // Catch:{ all -> 0x01fa }
            throw r3     // Catch:{ all -> 0x01fa }
        L_0x01e7:
        L_0x01e8:
            if (r1 == 0) goto L_0x01ed
            r1.disconnect()
        L_0x01ed:
            return
        L_0x01ee:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L_0x01f2:
            com.dcloud.android.downloader.exception.DownloadException r3 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ all -> 0x01fa }
            java.lang.String r4 = "IO error"
            r3.<init>(r0, r4, r1)     // Catch:{ all -> 0x01fa }
            throw r3     // Catch:{ all -> 0x01fa }
        L_0x01fa:
            r0 = move-exception
            r1 = r2
            goto L_0x0207
        L_0x01fd:
            r0 = move-exception
        L_0x01fe:
            com.dcloud.android.downloader.exception.DownloadException r2 = new com.dcloud.android.downloader.exception.DownloadException     // Catch:{ all -> 0x01cd }
            r3 = 4
            java.lang.String r4 = "Protocol error"
            r2.<init>(r3, r4, r0)     // Catch:{ all -> 0x01cd }
            throw r2     // Catch:{ all -> 0x01cd }
        L_0x0207:
            if (r1 == 0) goto L_0x020c
            r1.disconnect()
        L_0x020c:
            goto L_0x020e
        L_0x020d:
            throw r0
        L_0x020e:
            goto L_0x020d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.downloader.core.thread.DownloadThread.executeDownload():void");
    }

    public void run() {
        Process.setThreadPriority(10);
        checkPause();
        try {
            executeDownload();
        } catch (DownloadException e) {
            this.downloadInfo.setStatus(6);
            this.downloadInfo.setException(e);
            this.downloadResponse.onStatusChanged(this.downloadInfo);
            this.downloadResponse.handleException(e);
        } catch (Exception e2) {
            DownloadException downloadException = new DownloadException(9, "other error", e2);
            this.downloadInfo.setStatus(6);
            this.downloadInfo.setException(downloadException);
            this.downloadResponse.onStatusChanged(this.downloadInfo);
            this.downloadResponse.handleException(downloadException);
        }
    }
}
