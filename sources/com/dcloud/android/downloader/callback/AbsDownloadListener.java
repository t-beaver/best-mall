package com.dcloud.android.downloader.callback;

import java.lang.ref.SoftReference;

public abstract class AbsDownloadListener implements DownloadListener {
    private SoftReference<Object> userTag;

    public AbsDownloadListener() {
    }

    public SoftReference<Object> getUserTag() {
        return this.userTag;
    }

    public void setUserTag(SoftReference<Object> softReference) {
        this.userTag = softReference;
    }

    public AbsDownloadListener(SoftReference<Object> softReference) {
        this.userTag = softReference;
    }
}
