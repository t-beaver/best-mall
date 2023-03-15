package com.dcloud.android.downloader.core;

import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.exception.DownloadException;

public interface DownloadResponse {
    void handleException(DownloadException downloadException);

    void onStatusChanged(DownloadInfo downloadInfo);
}
