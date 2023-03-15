package com.dcloud.android.downloader.callback;

import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.exception.DownloadException;

public interface DownloadListener {
    void onDownloadFailed(DownloadInfo downloadInfo, DownloadException downloadException);

    void onDownloadSuccess(DownloadInfo downloadInfo);

    void onDownloading(long j, long j2);

    void onPaused();

    void onRemoved();

    void onStart();

    void onWaited();
}
