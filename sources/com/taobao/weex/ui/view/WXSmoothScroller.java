package com.taobao.weex.ui.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class WXSmoothScroller extends Scroller {
    private double mScrollFactor = 1.0d;

    public WXSmoothScroller(Context context) {
        super(context);
    }

    public WXSmoothScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public WXSmoothScroller(Context context, Interpolator interpolator, boolean z) {
        super(context, interpolator, z);
    }

    public void setScrollDurationFactor(double d) {
        this.mScrollFactor = d;
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        double d = (double) i5;
        double d2 = this.mScrollFactor;
        Double.isNaN(d);
        super.startScroll(i, i2, i3, i4, (int) (d * d2));
    }
}
