package com.dcloud.android.downloader.config;

import com.dcloud.android.downloader.db.DownloadDBController;
import io.dcloud.feature.gg.dcloud.ADSim;

public class Config {
    private int connectTimeout = ADSim.INTISPLSH;
    private String databaseName = "download_info.db";
    private int databaseVersion = 2;
    private DownloadDBController downloadDBController;
    private int downloadThread = 2;
    private int eachDownloadThread = 1;
    private final String method = "GET";
    private int readTimeout = ADSim.INTISPLSH;
    private int retryDownloadCount = 2;

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public int getDatabaseVersion() {
        return this.databaseVersion;
    }

    public DownloadDBController getDownloadDBController() {
        return this.downloadDBController;
    }

    public int getDownloadThread() {
        return this.downloadThread;
    }

    public int getEachDownloadThread() {
        return this.eachDownloadThread;
    }

    public String getMethod() {
        return "GET";
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public int getRetryDownloadCount() {
        return this.retryDownloadCount;
    }

    public void setConnectTimeout(int i) {
        this.connectTimeout = i;
    }

    public void setDatabaseName(String str) {
        this.databaseName = str;
    }

    public void setDatabaseVersion(int i) {
        this.databaseVersion = i;
    }

    public void setDownloadDBController(DownloadDBController downloadDBController2) {
        this.downloadDBController = downloadDBController2;
    }

    public void setDownloadThread(int i) {
        this.downloadThread = i;
    }

    public void setEachDownloadThread(int i) {
        this.eachDownloadThread = i;
    }

    public void setReadTimeout(int i) {
        this.readTimeout = i;
    }

    public void setRetryDownloadCount(int i) {
        this.retryDownloadCount = i;
    }
}
