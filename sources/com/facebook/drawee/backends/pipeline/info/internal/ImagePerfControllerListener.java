package com.facebook.drawee.backends.pipeline.info.internal;

import android.graphics.drawable.Animatable;
import com.facebook.common.time.MonotonicClock;
import com.facebook.drawee.backends.pipeline.info.ImagePerfMonitor;
import com.facebook.drawee.backends.pipeline.info.ImagePerfState;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.fresco.ui.common.DimensionsInfo;
import com.facebook.fresco.ui.common.OnDrawControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import javax.annotation.Nullable;

public class ImagePerfControllerListener extends BaseControllerListener<ImageInfo> implements OnDrawControllerListener<ImageInfo> {
    private final MonotonicClock mClock;
    private final ImagePerfMonitor mImagePerfMonitor;
    private final ImagePerfState mImagePerfState;

    public ImagePerfControllerListener(MonotonicClock monotonicClock, ImagePerfState imagePerfState, ImagePerfMonitor imagePerfMonitor) {
        this.mClock = monotonicClock;
        this.mImagePerfState = imagePerfState;
        this.mImagePerfMonitor = imagePerfMonitor;
    }

    public void onSubmit(String str, Object obj) {
        long now = this.mClock.now();
        this.mImagePerfState.resetPointsTimestamps();
        this.mImagePerfState.setControllerSubmitTimeMs(now);
        this.mImagePerfState.setControllerId(str);
        this.mImagePerfState.setCallerContext(obj);
        this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 0);
        reportViewVisible(now);
    }

    public void onIntermediateImageSet(String str, @Nullable ImageInfo imageInfo) {
        this.mImagePerfState.setControllerIntermediateImageSetTimeMs(this.mClock.now());
        this.mImagePerfState.setControllerId(str);
        this.mImagePerfState.setImageInfo(imageInfo);
        this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 2);
    }

    public void onFinalImageSet(String str, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
        long now = this.mClock.now();
        this.mImagePerfState.setControllerFinalImageSetTimeMs(now);
        this.mImagePerfState.setImageRequestEndTimeMs(now);
        this.mImagePerfState.setControllerId(str);
        this.mImagePerfState.setImageInfo(imageInfo);
        this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 3);
    }

    public void onFailure(String str, Throwable th) {
        long now = this.mClock.now();
        this.mImagePerfState.setControllerFailureTimeMs(now);
        this.mImagePerfState.setControllerId(str);
        this.mImagePerfState.setErrorThrowable(th);
        this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 5);
        reportViewInvisible(now);
    }

    public void onRelease(String str) {
        super.onRelease(str);
        long now = this.mClock.now();
        int imageLoadStatus = this.mImagePerfState.getImageLoadStatus();
        if (!(imageLoadStatus == 3 || imageLoadStatus == 5 || imageLoadStatus == 6)) {
            this.mImagePerfState.setControllerCancelTimeMs(now);
            this.mImagePerfState.setControllerId(str);
            this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 4);
        }
        reportViewInvisible(now);
    }

    public void onImageDrawn(String str, ImageInfo imageInfo, DimensionsInfo dimensionsInfo) {
        this.mImagePerfState.setImageDrawTimeMs(this.mClock.now());
        this.mImagePerfState.setDimensionsInfo(dimensionsInfo);
        this.mImagePerfMonitor.notifyStatusUpdated(this.mImagePerfState, 6);
    }

    public void reportViewVisible(long j) {
        this.mImagePerfState.setVisible(true);
        this.mImagePerfState.setVisibilityEventTimeMs(j);
        this.mImagePerfMonitor.notifyListenersOfVisibilityStateUpdate(this.mImagePerfState, 1);
    }

    private void reportViewInvisible(long j) {
        this.mImagePerfState.setVisible(false);
        this.mImagePerfState.setInvisibilityEventTimeMs(j);
        this.mImagePerfMonitor.notifyListenersOfVisibilityStateUpdate(this.mImagePerfState, 2);
    }
}
