package com.dcloud.android.downloader.core;

import com.dcloud.android.downloader.config.Config;
import com.dcloud.android.downloader.core.task.DownloadTask;
import com.dcloud.android.downloader.core.task.GetFileInfoTask;
import com.dcloud.android.downloader.core.thread.DownloadThread;
import com.dcloud.android.downloader.domain.DownloadInfo;
import com.dcloud.android.downloader.domain.DownloadThreadInfo;
import com.dcloud.android.downloader.exception.DownloadException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadTaskImpl implements DownloadTask, GetFileInfoTask.OnGetFileInfoListener, DownloadThread.DownloadProgressListener {
    private final Config config;
    private final DownloadInfo downloadInfo;
    private final DownloadResponse downloadResponse;
    private final DownloadTaskListener downloadTaskListener;
    private final List<DownloadThread> downloadThreads;
    private final ExecutorService executorService;
    private volatile AtomicBoolean isComputerDownload = new AtomicBoolean(false);
    private long lastRefreshTime = System.currentTimeMillis();
    private long progress;

    public interface DownloadTaskListener {
        void onDownloadSuccess(DownloadInfo downloadInfo);
    }

    public DownloadTaskImpl(ExecutorService executorService2, DownloadResponse downloadResponse2, DownloadInfo downloadInfo2, Config config2, DownloadTaskListener downloadTaskListener2) {
        this.executorService = executorService2;
        this.downloadResponse = downloadResponse2;
        this.downloadInfo = downloadInfo2;
        this.config = config2;
        this.downloadTaskListener = downloadTaskListener2;
        this.downloadThreads = new ArrayList();
    }

    private void computerDownloadProgress() {
        this.progress = 0;
        for (DownloadThreadInfo progress2 : this.downloadInfo.getDownloadThreadInfos()) {
            this.progress += progress2.getProgress();
        }
        this.downloadInfo.setProgress(this.progress);
    }

    private void getFileInfo() {
        this.executorService.submit(new GetFileInfoTask(this.downloadResponse, this.downloadInfo, this));
    }

    private void removeDownlaodFile() {
        File file = new File(this.downloadInfo.getPath());
        if (file.exists()) {
            file.delete();
        }
    }

    public void onDownloadSuccess() {
        computerDownloadProgress();
        if (this.downloadInfo.getProgress() == this.downloadInfo.getSize()) {
            this.downloadInfo.setStatus(5);
            this.downloadResponse.onStatusChanged(this.downloadInfo);
            DownloadTaskListener downloadTaskListener2 = this.downloadTaskListener;
            if (downloadTaskListener2 != null) {
                downloadTaskListener2.onDownloadSuccess(this.downloadInfo);
            }
        }
    }

    public void onFailed(DownloadException downloadException) {
    }

    public void onProgress() {
        if (!this.isComputerDownload.get()) {
            synchronized (this) {
                if (!this.isComputerDownload.get()) {
                    this.isComputerDownload.set(true);
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastRefreshTime > 1000) {
                        computerDownloadProgress();
                        this.downloadResponse.onStatusChanged(this.downloadInfo);
                        this.lastRefreshTime = currentTimeMillis;
                    }
                    this.isComputerDownload.set(false);
                }
            }
        }
    }

    public void onSuccess(long j, boolean z) {
        boolean z2 = z;
        this.downloadInfo.setSupportRanges(z2);
        this.downloadInfo.setSize(j);
        removeDownlaodFile();
        ArrayList arrayList = new ArrayList();
        if (z2) {
            long size = this.downloadInfo.getSize();
            int eachDownloadThread = this.config.getEachDownloadThread();
            long j2 = size / ((long) eachDownloadThread);
            int i = 0;
            while (i < eachDownloadThread) {
                long j3 = j2 * ((long) i);
                int i2 = i;
                DownloadThreadInfo downloadThreadInfo = new DownloadThreadInfo(i2, this.downloadInfo.getId(), this.downloadInfo.getDownloadUrl(), j3, i == eachDownloadThread + -1 ? size : (j3 + j2) - 1);
                arrayList.add(downloadThreadInfo);
                DownloadThread downloadThread = new DownloadThread(downloadThreadInfo, this.downloadResponse, this.config, this.downloadInfo, this);
                this.executorService.submit(downloadThread);
                this.downloadThreads.add(downloadThread);
                i = i2 + 1;
            }
        } else {
            DownloadThreadInfo downloadThreadInfo2 = new DownloadThreadInfo(0, this.downloadInfo.getId(), this.downloadInfo.getDownloadUrl(), 0, this.downloadInfo.getSize());
            arrayList.add(downloadThreadInfo2);
            DownloadThread downloadThread2 = new DownloadThread(downloadThreadInfo2, this.downloadResponse, this.config, this.downloadInfo, this);
            this.executorService.submit(downloadThread2);
            this.downloadThreads.add(downloadThread2);
        }
        this.downloadInfo.setDownloadThreadInfos(arrayList);
        this.downloadInfo.setStatus(2);
        this.downloadResponse.onStatusChanged(this.downloadInfo);
    }

    public void start() {
        if (this.downloadInfo.getSize() <= 0) {
            getFileInfo();
            return;
        }
        for (DownloadThreadInfo downloadThread : this.downloadInfo.getDownloadThreadInfos()) {
            DownloadThread downloadThread2 = new DownloadThread(downloadThread, this.downloadResponse, this.config, this.downloadInfo, this);
            this.executorService.submit(downloadThread2);
            this.downloadThreads.add(downloadThread2);
        }
        this.downloadInfo.setStatus(2);
        this.downloadResponse.onStatusChanged(this.downloadInfo);
    }
}
