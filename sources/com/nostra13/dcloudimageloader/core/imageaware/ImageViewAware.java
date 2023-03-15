package com.nostra13.dcloudimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.utils.L;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class ImageViewAware implements ImageAware {
    protected boolean checkActualViewSize;
    protected Reference<ImageView> imageViewRef;

    public ImageViewAware(ImageView imageView) {
        this(imageView, true);
    }

    public int getHeight() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int height = (!this.checkActualViewSize || layoutParams == null || layoutParams.height == -2) ? 0 : imageView.getHeight();
        if (height <= 0 && layoutParams != null) {
            height = layoutParams.height;
        }
        if (height <= 0) {
            height = imageView.getMaxHeight();
        }
        L.w("height = " + height, new Object[0]);
        return height;
    }

    public int getId() {
        ImageView imageView = this.imageViewRef.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    public ViewScaleType getScaleType() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return null;
    }

    public int getWidth() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int width = (!this.checkActualViewSize || layoutParams == null || layoutParams.width == -2) ? 0 : imageView.getWidth();
        if (width <= 0 && layoutParams != null) {
            width = layoutParams.width;
        }
        if (width <= 0) {
            width = imageView.getMaxWidth();
        }
        L.w("width = " + width, new Object[0]);
        return width;
    }

    public boolean isCollected() {
        return this.imageViewRef.get() == null;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return false;
        }
        imageView.setImageBitmap(bitmap);
        return true;
    }

    public boolean setImageDrawable(Drawable drawable) {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return false;
        }
        imageView.setImageDrawable(drawable);
        return true;
    }

    public ImageViewAware(ImageView imageView, boolean z) {
        this.imageViewRef = new WeakReference(imageView);
        this.checkActualViewSize = z;
    }

    public ImageView getWrappedView() {
        return this.imageViewRef.get();
    }
}
