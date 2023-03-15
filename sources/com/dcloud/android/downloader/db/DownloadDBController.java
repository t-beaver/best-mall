package com.dcloud.android.downloader.db;

import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.domain.DownloadThreadInfo;
import java.util.List;

public interface DownloadDBController {
    void createOrUpdate(DownloadInfo downloadInfo);

    void createOrUpdate(DownloadThreadInfo downloadThreadInfo);

    void delete(DownloadInfo downloadInfo);

    void delete(DownloadThreadInfo downloadThreadInfo);

    List<DownloadInfo> findAllDownloaded();

    List<DownloadInfo> findAllDownloading();

    DownloadInfo findDownloadedInfoById(int i);

    void pauseAllDownloading();
}
