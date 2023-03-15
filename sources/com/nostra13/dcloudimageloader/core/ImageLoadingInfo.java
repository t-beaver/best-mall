package com.nostra13.dcloudimageloader.core;

import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import java.util.concurrent.locks.ReentrantLock;

final class ImageLoadingInfo {
    final ImageAware imageAware;
    final ImageLoadingListener listener;
    final ReentrantLock loadFromUriLock;
    final String memoryCacheKey;
    final DisplayImageOptions options;
    final ImageSize targetSize;
    final String uri;

    public ImageLoadingInfo(String str, ImageAware imageAware2, ImageSize imageSize, String str2, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, ReentrantLock reentrantLock) {
        this.uri = str;
        this.imageAware = imageAware2;
        this.targetSize = imageSize;
        this.options = displayImageOptions;
        this.listener = imageLoadingListener;
        this.loadFromUriLock = reentrantLock;
        this.memoryCacheKey = str2;
    }
}
