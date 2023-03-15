package com.nostra13.dcloudimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;

public class ImageNonViewAware implements ImageAware {
    protected final ImageSize imageSize;
    protected final ViewScaleType scaleType;

    public ImageNonViewAware(ImageSize imageSize2, ViewScaleType viewScaleType) {
        this.imageSize = imageSize2;
        this.scaleType = viewScaleType;
    }

    public int getHeight() {
        return this.imageSize.getHeight();
    }

    public int getId() {
        return super.hashCode();
    }

    public ViewScaleType getScaleType() {
        return this.scaleType;
    }

    public int getWidth() {
        return this.imageSize.getWidth();
    }

    public View getWrappedView() {
        return null;
    }

    public boolean isCollected() {
        return false;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        return true;
    }

    public boolean setImageDrawable(Drawable drawable) {
        return true;
    }
}
