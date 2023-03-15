package com.nostra13.dcloudimageloader.core;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.display.BitmapDisplayer;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import com.nostra13.dcloudimageloader.utils.L;

final class DisplayBitmapTask implements Runnable {
    private static final String LOG_DISPLAY_IMAGE_IN_IMAGEAWARE = "Display image in ImageAware (loaded from %1$s) [%2$s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private final Bitmap bitmap;
    private final BitmapDisplayer displayer;
    private final ImageLoaderEngine engine;
    private final ImageAware imageAware;
    private final String imageUri;
    private final ImageLoadingListener listener;
    private final LoadedFrom loadedFrom;
    private boolean loggingEnabled;
    private final String memoryCacheKey;

    public DisplayBitmapTask(Bitmap bitmap2, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine imageLoaderEngine, LoadedFrom loadedFrom2) {
        this.bitmap = bitmap2;
        this.imageUri = imageLoadingInfo.uri;
        this.imageAware = imageLoadingInfo.imageAware;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.displayer = imageLoadingInfo.options.getDisplayer();
        this.listener = imageLoadingInfo.listener;
        this.engine = imageLoaderEngine;
        this.loadedFrom = loadedFrom2;
    }

    private boolean isViewWasReused() {
        return !this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware));
    }

    public void run() {
        if (this.imageAware.isCollected()) {
            if (this.loggingEnabled) {
                L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, this.memoryCacheKey);
            }
            this.listener.onLoadingCancelled(this.imageUri, this.imageAware.getWrappedView());
        } else if (isViewWasReused()) {
            if (this.loggingEnabled) {
                L.d(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, this.memoryCacheKey);
            }
            this.listener.onLoadingCancelled(this.imageUri, this.imageAware.getWrappedView());
        } else {
            if (this.loggingEnabled) {
                L.d(LOG_DISPLAY_IMAGE_IN_IMAGEAWARE, this.loadedFrom, this.memoryCacheKey);
            }
            this.listener.onLoadingComplete(this.imageUri, this.imageAware.getWrappedView(), this.displayer.display(this.bitmap, this.imageAware, this.loadedFrom));
            this.engine.cancelDisplayTaskFor(this.imageAware);
        }
    }

    /* access modifiers changed from: package-private */
    public void setLoggingEnabled(boolean z) {
        this.loggingEnabled = z;
    }
}
