package com.dcloud.android.downloader.domain;

import android.content.Context;
import android.text.TextUtils;
import com.dcloud.android.downloader.callback.DownloadListener;
import com.dcloud.android.downloader.exception.DownloadException;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class DownloadInfo implements Serializable {
    public static final int STATUS_COMPLETED = 5;
    public static final int STATUS_DOWNLOADING = 2;
    public static final int STATUS_ERROR = 6;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_PREPARE_DOWNLOAD = 1;
    public static final int STATUS_REMOVED = 7;
    public static final int STATUS_WAIT = 3;
    private Context context;
    private long createAt;
    private transient DownloadListener downloadListener;
    private List<DownloadThreadInfo> downloadThreadInfos;
    private DownloadException exception;
    private int id;
    private String location;
    private String path;
    private long progress;
    private long size;
    private int status;
    private int supportRanges;
    private Object tag;
    private String uri;

    public static final class Builder {
        private static final String DEFAULT_ENCODE = "utf-8";
        private long createAt = -1;
        private String id;
        private String path;
        private String url;

        public DownloadInfo build(Context context) {
            DownloadInfo downloadInfo = new DownloadInfo(context);
            if (!TextUtils.isEmpty(this.url)) {
                downloadInfo.setUri(this.url);
                if (!TextUtils.isEmpty(this.path)) {
                    downloadInfo.setPath(this.path);
                    if (this.createAt == -1) {
                        setCreateAt(System.currentTimeMillis());
                    }
                    downloadInfo.setId(this.url.hashCode());
                    if (TextUtils.isEmpty(this.id)) {
                        downloadInfo.setId(this.url.hashCode());
                    }
                    return downloadInfo;
                }
                throw new DownloadException(1, "path cannot be null.");
            }
            throw new DownloadException(0, "uri cannot be null.");
        }

        public Builder setCreateAt(long j) {
            this.createAt = j;
            return this;
        }

        public void setId(String str) {
            this.id = str;
        }

        public Builder setPath(String str) {
            this.path = str;
            return this;
        }

        public Builder setUrl(String str) {
            this.url = str;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DownloadStatus {
    }

    public DownloadInfo(Context context2) {
        this.context = context2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass() && this.id == ((DownloadInfo) obj).id) {
            return true;
        }
        return false;
    }

    public Context getContext() {
        return this.context;
    }

    public long getCreateAt() {
        return this.createAt;
    }

    public DownloadListener getDownloadListener() {
        return this.downloadListener;
    }

    public List<DownloadThreadInfo> getDownloadThreadInfos() {
        return this.downloadThreadInfos;
    }

    public String getDownloadUrl() {
        if (TextUtils.isEmpty(this.location)) {
            return getUri();
        }
        return this.location;
    }

    public DownloadException getException() {
        return this.exception;
    }

    public int getId() {
        return this.id;
    }

    public String getLocation() {
        return this.location;
    }

    public String getPath() {
        return this.path;
    }

    public long getProgress() {
        return this.progress;
    }

    public long getSize() {
        return this.size;
    }

    public int getStatus() {
        return this.status;
    }

    public int getSupportRanges() {
        return this.supportRanges;
    }

    public Object getTag() {
        return this.tag;
    }

    public String getUri() {
        return this.uri;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean isPause() {
        int i = this.status;
        return i == 4 || i == 6 || i == 7;
    }

    public boolean isSupportRanges() {
        return this.supportRanges == 0;
    }

    public void setCreateAt(long j) {
        this.createAt = j;
    }

    public void setDownloadListener(DownloadListener downloadListener2) {
        this.downloadListener = downloadListener2;
    }

    public void setDownloadThreadInfos(List<DownloadThreadInfo> list) {
        this.downloadThreadInfos = list;
    }

    public void setException(DownloadException downloadException) {
        this.exception = downloadException;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setLocation(String str) {
        this.location = str;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public void setProgress(long j) {
        this.progress = j;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setSupportRanges(int i) {
        this.supportRanges = i;
    }

    public void setTag(Object obj) {
        this.tag = obj;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public void setSupportRanges(boolean z) {
        this.supportRanges = z ^ true ? 1 : 0;
    }
}
