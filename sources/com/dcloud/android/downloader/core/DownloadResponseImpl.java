package com.dcloud.android.downloader.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.dcloud.android.downloader.db.DownloadDBController;
import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.domain.DownloadThreadInfo;
import com.dcloud.android.downloader.exception.DownloadException;

public class DownloadResponseImpl implements DownloadResponse {
    private static final String TAG = "DownloadResponseImpl";
    private final DownloadDBController downloadDBController;
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            DownloadInfo downloadInfo = (DownloadInfo) message.obj;
            switch (downloadInfo.getStatus()) {
                case 1:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onStart();
                        return;
                    }
                    return;
                case 2:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onDownloading(downloadInfo.getProgress(), downloadInfo.getSize());
                        return;
                    }
                    return;
                case 3:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onWaited();
                        return;
                    }
                    return;
                case 4:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onPaused();
                        return;
                    }
                    return;
                case 5:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onDownloadSuccess(downloadInfo);
                        return;
                    }
                    return;
                case 6:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onDownloadFailed(downloadInfo, downloadInfo.getException());
                        return;
                    }
                    return;
                case 7:
                    if (downloadInfo.getDownloadListener() != null) {
                        downloadInfo.getDownloadListener().onRemoved();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    public DownloadResponseImpl(DownloadDBController downloadDBController2) {
        this.downloadDBController = downloadDBController2;
    }

    public void handleException(DownloadException downloadException) {
    }

    public void onStatusChanged(DownloadInfo downloadInfo) {
        if (downloadInfo.getStatus() != 7) {
            this.downloadDBController.createOrUpdate(downloadInfo);
            if (downloadInfo.getDownloadThreadInfos() != null) {
                for (DownloadThreadInfo createOrUpdate : downloadInfo.getDownloadThreadInfos()) {
                    this.downloadDBController.createOrUpdate(createOrUpdate);
                }
            }
        }
        Message obtainMessage = this.handler.obtainMessage(downloadInfo.getId());
        obtainMessage.obj = downloadInfo;
        obtainMessage.sendToTarget();
        Log.d(TAG, "progress:" + downloadInfo.getProgress() + ",size:" + downloadInfo.getSize());
    }
}
