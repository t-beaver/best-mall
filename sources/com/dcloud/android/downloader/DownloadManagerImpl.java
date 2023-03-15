package com.dcloud.android.downloader;

import android.content.Context;
import com.dcloud.android.downloader.callback.DCDownloadManager;
import com.dcloud.android.downloader.config.Config;
import com.dcloud.android.downloader.core.DownloadResponse;
import com.dcloud.android.downloader.core.DownloadResponseImpl;
import com.dcloud.android.downloader.core.DownloadTaskImpl;
import com.dcloud.android.downloader.core.task.DownloadTask;
import com.dcloud.android.downloader.db.DefaultDownloadDBController;
import com.dcloud.android.downloader.db.DownloadDBController;
import com.dcloud.android.downloader.domain.DownloadInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DownloadManagerImpl implements DCDownloadManager, DownloadTaskImpl.DownloadTaskListener {
    private static final int MIN_EXECUTE_INTERVAL = 500;
    private static DownloadManagerImpl instance;
    private final ConcurrentHashMap<Integer, DownloadTask> cacheDownloadTask;
    private final Config config;
    private final Context context;
    private final DownloadDBController downloadDBController;
    private final DownloadResponse downloadResponse;
    private final List<DownloadInfo> downloadingCaches;
    private final ExecutorService executorService;
    private long lastExecuteTime;

    private DownloadManagerImpl(Context context2, Config config2) {
        this.context = context2;
        config2 = config2 == null ? new Config() : config2;
        this.config = config2;
        if (config2.getDownloadDBController() == null) {
            this.downloadDBController = new DefaultDownloadDBController(context2, config2);
        } else {
            this.downloadDBController = config2.getDownloadDBController();
        }
        if (this.downloadDBController.findAllDownloading() == null) {
            this.downloadingCaches = new ArrayList();
        } else {
            this.downloadingCaches = this.downloadDBController.findAllDownloading();
        }
        this.cacheDownloadTask = new ConcurrentHashMap<>();
        this.downloadDBController.pauseAllDownloading();
        this.executorService = Executors.newFixedThreadPool(config2.getDownloadThread());
        this.downloadResponse = new DownloadResponseImpl(this.downloadDBController);
    }

    public static DCDownloadManager getInstance(Context context2, Config config2) {
        synchronized (DownloadManagerImpl.class) {
            if (instance == null) {
                instance = new DownloadManagerImpl(context2, config2);
            }
        }
        return instance;
    }

    private void prepareDownload(DownloadInfo downloadInfo) {
        if (this.cacheDownloadTask.size() >= this.config.getDownloadThread()) {
            downloadInfo.setStatus(3);
            this.downloadResponse.onStatusChanged(downloadInfo);
            return;
        }
        DownloadTaskImpl downloadTaskImpl = new DownloadTaskImpl(this.executorService, this.downloadResponse, downloadInfo, this.config, this);
        this.cacheDownloadTask.put(Integer.valueOf(downloadInfo.getId()), downloadTaskImpl);
        downloadInfo.setStatus(1);
        this.downloadResponse.onStatusChanged(downloadInfo);
        downloadTaskImpl.start();
    }

    private void prepareDownloadNextTask() {
        for (DownloadInfo next : this.downloadingCaches) {
            if (next.getStatus() == 3) {
                prepareDownload(next);
                return;
            }
        }
    }

    public void download(DownloadInfo downloadInfo) {
        this.downloadingCaches.add(downloadInfo);
        prepareDownload(downloadInfo);
    }

    public List<DownloadInfo> findAllDownloaded() {
        return this.downloadDBController.findAllDownloaded();
    }

    public List<DownloadInfo> findAllDownloading() {
        return this.downloadingCaches;
    }

    public DownloadInfo getDownloadById(int i) {
        DownloadInfo downloadInfo;
        Iterator<DownloadInfo> it = this.downloadingCaches.iterator();
        while (true) {
            if (!it.hasNext()) {
                downloadInfo = null;
                break;
            }
            downloadInfo = it.next();
            if (downloadInfo.getId() == i) {
                break;
            }
        }
        return downloadInfo == null ? this.downloadDBController.findDownloadedInfoById(i) : downloadInfo;
    }

    public DownloadDBController getDownloadDBController() {
        return this.downloadDBController;
    }

    public boolean isExecute() {
        if (System.currentTimeMillis() - this.lastExecuteTime <= 500) {
            return false;
        }
        this.lastExecuteTime = System.currentTimeMillis();
        return true;
    }

    public void onDestroy() {
    }

    public void onDownloadSuccess(DownloadInfo downloadInfo) {
        this.cacheDownloadTask.remove(Integer.valueOf(downloadInfo.getId()));
        this.downloadingCaches.remove(downloadInfo);
        prepareDownloadNextTask();
    }

    public void pause(DownloadInfo downloadInfo) {
        if (isExecute()) {
            downloadInfo.setStatus(4);
            this.cacheDownloadTask.remove(Integer.valueOf(downloadInfo.getId()));
            this.downloadResponse.onStatusChanged(downloadInfo);
            prepareDownloadNextTask();
        }
    }

    public void remove(DownloadInfo downloadInfo) {
        downloadInfo.setStatus(7);
        this.cacheDownloadTask.remove(Integer.valueOf(downloadInfo.getId()));
        this.downloadingCaches.remove(downloadInfo);
        this.downloadDBController.delete(downloadInfo);
        this.downloadResponse.onStatusChanged(downloadInfo);
    }

    public void resume(DownloadInfo downloadInfo) {
        if (isExecute()) {
            this.cacheDownloadTask.remove(Integer.valueOf(downloadInfo.getId()));
            prepareDownload(downloadInfo);
        }
    }
}
