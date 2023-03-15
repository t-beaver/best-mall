package com.dcloud.android.downloader.callback;

import com.dcloud.android.downloader.db.DownloadDBController;
import com.dcloud.android.downloader.domain.DownloadInfo;
import java.util.List;

public interface DCDownloadManager {
    void download(DownloadInfo downloadInfo);

    List<DownloadInfo> findAllDownloaded();

    List<DownloadInfo> findAllDownloading();

    DownloadInfo getDownloadById(int i);

    DownloadDBController getDownloadDBController();

    void onDestroy();

    void pause(DownloadInfo downloadInfo);

    void remove(DownloadInfo downloadInfo);

    void resume(DownloadInfo downloadInfo);
}
