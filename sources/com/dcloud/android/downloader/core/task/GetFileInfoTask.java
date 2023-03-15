package com.dcloud.android.downloader.core.task;

import android.os.Process;
import android.text.TextUtils;
import com.dcloud.android.downloader.core.DownloadResponse;
import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.exception.DownloadException;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.feature.gg.dcloud.ADSim;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class GetFileInfoTask implements Runnable {
    private final DownloadInfo downloadInfo;
    private final DownloadResponse downloadResponse;
    private final OnGetFileInfoListener onGetFileInfoListener;

    public interface OnGetFileInfoListener {
        void onFailed(DownloadException downloadException);

        void onSuccess(long j, boolean z);
    }

    public GetFileInfoTask(DownloadResponse downloadResponse2, DownloadInfo downloadInfo2, OnGetFileInfoListener onGetFileInfoListener2) {
        this.downloadResponse = downloadResponse2;
        this.downloadInfo = downloadInfo2;
        this.onGetFileInfoListener = onGetFileInfoListener2;
    }

    private void checkIfPause() {
        if (this.downloadInfo.isPause()) {
            throw new DownloadException(7);
        }
    }

    private void executeConnection() throws DownloadException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.downloadInfo.getDownloadUrl()).openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                SSLSocketFactory sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory();
                if (sSLSocketFactory != null) {
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLSocketFactory);
                }
                ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(DCloudTrustManager.getHostnameVerifier(false));
            }
            httpURLConnection.setConnectTimeout(ADSim.INTISPLSH);
            httpURLConnection.setReadTimeout(ADSim.INTISPLSH);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Range", "bytes=0-");
            httpURLConnection.setInstanceFollowRedirects(false);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                parseHttpResponse(httpURLConnection, false);
            } else if (responseCode == 206) {
                parseHttpResponse(httpURLConnection, true);
            } else {
                if (responseCode != 302) {
                    if (301 != responseCode) {
                        throw new DownloadException(3, "UnSupported response code:" + responseCode);
                    }
                }
                this.downloadInfo.setLocation(httpURLConnection.getHeaderField("Location"));
                executeConnection();
            }
        } catch (MalformedURLException e) {
            throw new DownloadException(2, "Bad url.", e);
        } catch (ProtocolException e2) {
            throw new DownloadException(4, "Protocol error", e2);
        } catch (IOException e3) {
            throw new DownloadException(5, "IO error", e3);
        } catch (Exception e4) {
            throw new DownloadException(5, "Unknown error", e4);
        }
    }

    private void parseHttpResponse(HttpURLConnection httpURLConnection, boolean z) throws DownloadException {
        long j;
        String headerField = httpURLConnection.getHeaderField("Content-Length");
        if (TextUtils.isEmpty(headerField) || headerField.equals(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT) || headerField.equals("-1")) {
            j = (long) httpURLConnection.getContentLength();
        } else {
            j = Long.parseLong(headerField);
        }
        if (j > 0) {
            checkIfPause();
            this.onGetFileInfoListener.onSuccess(j, z);
            return;
        }
        throw new DownloadException(6, "length <= 0");
    }

    public void run() {
        Process.setThreadPriority(10);
        try {
            executeConnection();
        } catch (DownloadException e) {
            this.downloadResponse.handleException(e);
        } catch (Exception e2) {
            this.downloadResponse.handleException(new DownloadException(9, (Throwable) e2));
        }
    }
}
