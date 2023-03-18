package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.nineoldandroids.view.ViewHelper;

public class DHImageView extends FrameLayout {
    boolean isSlipping = false;
    boolean isTouchIntercept = true;
    public Bitmap mBitmap;
    public long mBitmapHeight = 0;
    public long mBitmapWidth = 0;
    private IFrameView mFrameView;
    public ImageView mImageView;
    public INativeView mView = null;

    public DHImageView(Context context) {
        super(context);
        ImageView imageView = new ImageView(context);
        this.mImageView = imageView;
        addView(imageView, new FrameLayout.LayoutParams(-1, -1));
    }

    public void addNativeView(IFrameView iFrameView, INativeView iNativeView) {
        this.mFrameView = iFrameView;
        INativeView iNativeView2 = this.mView;
        if (iNativeView2 != null) {
            removeView(iNativeView2.obtanMainView());
            this.mView.setNativeShowType(false);
            this.mView = null;
        }
        this.mView = iNativeView;
        if (iNativeView.obtanMainView().getParent() != null) {
            ((ViewGroup) iNativeView.obtanMainView().getParent()).removeView(iNativeView.obtanMainView());
        }
        iNativeView.obtanMainView().setVisibility(0);
        iNativeView.setNativeShowType(true);
        addView(iNativeView.obtanMainView());
    }

    public void clear() {
        setImageBitmap((Bitmap) null);
        removeNativeView();
        setVisibility(4);
        IFrameView iFrameView = this.mFrameView;
        if (iFrameView != null) {
            iFrameView.setSnapshotView((INativeView) null, "none");
        }
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public boolean isNativeView() {
        return this.mView != null;
    }

    public boolean isSlipping() {
        return this.isSlipping;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.isTouchIntercept) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void recycledBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public void refreshImagerView() {
        ViewHelper.setAlpha(this, 1.0f);
        ViewHelper.setScaleX(this, 1.0f);
        ViewHelper.setScaleY(this, 1.0f);
        ViewHelper.setRotationX(this, 0.0f);
        ViewHelper.setRotationY(this, 0.0f);
        ViewHelper.setTranslationX(this, 0.0f);
        ViewHelper.setTranslationY(this, 0.0f);
        ViewHelper.setX(this, 0.0f);
        ViewHelper.setY(this, 0.0f);
        ViewHelper.setScrollX(this, 0);
        ViewHelper.setScrollY(this, 0);
        setPadding(0, 0, 0, 0);
        this.isSlipping = false;
        setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        requestLayout();
    }

    public void removeNativeView() {
        INativeView iNativeView = this.mView;
        if (iNativeView != null) {
            removeView(iNativeView.obtanMainView());
            this.mView.setNativeShowType(false);
            this.mView = null;
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (this.isSlipping) {
            recycledBitmap(bitmap);
            return;
        }
        recycledBitmap(this.mBitmap);
        this.mBitmap = bitmap;
        if (bitmap != null) {
            this.mBitmapWidth = (long) bitmap.getWidth();
            this.mBitmapHeight = (long) bitmap.getHeight();
        } else {
            this.mBitmapWidth = 0;
            this.mBitmapHeight = 0;
        }
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void setIntercept(boolean z) {
        this.isTouchIntercept = z;
    }

    public void setNativeAnimationRuning(boolean z) {
        INativeView iNativeView = this.mView;
        if (iNativeView != null) {
            iNativeView.setWebAnimationRuning(z);
        }
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.mImageView.setScaleType(scaleType);
    }

    public void setSlipping(boolean z) {
        this.isSlipping = z;
    }
}
