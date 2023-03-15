package com.dcloud.android.downloader.domain;

import java.io.Serializable;

public class DownloadThreadInfo implements Serializable {
    private int downloadInfoId;
    private long end;
    private int id;
    private long progress;
    private long start;
    private int threadId;
    private String uri;

    public DownloadThreadInfo(int i, int i2, String str, long j, long j2) {
        this.id = i2 + i;
        this.threadId = i;
        this.downloadInfoId = i2;
        this.uri = str;
        this.start = j;
        this.end = j2;
    }

    public int getDownloadInfoId() {
        return this.downloadInfoId;
    }

    public long getEnd() {
        return this.end;
    }

    public int getId() {
        return this.id;
    }

    public long getProgress() {
        return this.progress;
    }

    public long getStart() {
        return this.start;
    }

    public int getThreadId() {
        return this.threadId;
    }

    public String getUri() {
        return this.uri;
    }

    public boolean isThreadDownloadSuccess() {
        return this.progress >= this.end - this.start;
    }

    public void setDownloadInfoId(int i) {
        this.downloadInfoId = i;
    }

    public void setEnd(long j) {
        this.end = j;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setProgress(long j) {
        this.progress = j;
    }

    public void setStart(long j) {
        this.start = j;
    }

    public void setThreadId(int i) {
        this.threadId = i;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public DownloadThreadInfo() {
    }
}
