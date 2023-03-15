package com.nostra13.dcloudimageloader.core.display;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;

public class FadeInBitmapDisplayer implements BitmapDisplayer {
    private final boolean animateFromDisc;
    private final boolean animateFromMemory;
    private final boolean animateFromNetwork;
    private final int durationMillis;

    public FadeInBitmapDisplayer(int i) {
        this(i, true, true, true);
    }

    public static void animate(View view, int i) {
        if (view != null) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration((long) i);
            alphaAnimation.setInterpolator(new DecelerateInterpolator());
            view.startAnimation(alphaAnimation);
        }
    }

    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(bitmap);
        if ((this.animateFromNetwork && loadedFrom == LoadedFrom.NETWORK) || ((this.animateFromDisc && loadedFrom == LoadedFrom.DISC_CACHE) || (this.animateFromMemory && loadedFrom == LoadedFrom.MEMORY_CACHE))) {
            animate(imageAware.getWrappedView(), this.durationMillis);
        }
        return bitmap;
    }

    public FadeInBitmapDisplayer(int i, boolean z, boolean z2, boolean z3) {
        this.durationMillis = i;
        this.animateFromNetwork = z;
        this.animateFromDisc = z2;
        this.animateFromMemory = z3;
    }
}
