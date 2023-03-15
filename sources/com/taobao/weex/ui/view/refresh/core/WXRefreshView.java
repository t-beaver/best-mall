package com.taobao.weex.ui.view.refresh.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.view.refresh.circlebar.CircleProgressBar;

public class WXRefreshView extends FrameLayout {
    /* access modifiers changed from: private */
    public CircleProgressBar circleProgressBar;
    /* access modifiers changed from: private */
    public LinearLayout linearLayout;

    public WXRefreshView(Context context) {
        super(context);
        setupViews();
    }

    public WXRefreshView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupViews();
    }

    public WXRefreshView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setupViews();
    }

    private void setupViews() {
        this.linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        this.linearLayout.setOrientation(1);
        this.linearLayout.setGravity(17);
        addView(this.linearLayout, layoutParams);
    }

    public void setContentGravity(int i) {
        LinearLayout linearLayout2 = this.linearLayout;
        if (linearLayout2 != null) {
            linearLayout2.setGravity(i);
        }
    }

    public void setRefreshView(final View view) {
        if (view != null) {
            post(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    View view = view;
                    if (view.getParent() != null) {
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                    WXRefreshView.this.linearLayout.removeAllViews();
                    int i = 0;
                    while (true) {
                        ViewGroup viewGroup = (ViewGroup) view;
                        if (i < viewGroup.getChildCount()) {
                            View childAt = viewGroup.getChildAt(i);
                            if (childAt instanceof CircleProgressBar) {
                                CircleProgressBar unused = WXRefreshView.this.circleProgressBar = (CircleProgressBar) childAt;
                            }
                            i++;
                        } else {
                            WXRefreshView.this.linearLayout.addView(view);
                            return;
                        }
                    }
                }
            }));
        }
    }

    public void setProgressBgColor(int i) {
        CircleProgressBar circleProgressBar2 = this.circleProgressBar;
        if (circleProgressBar2 != null) {
            circleProgressBar2.setBackgroundColor(i);
        }
    }

    public void setProgressColor(int i) {
        CircleProgressBar circleProgressBar2 = this.circleProgressBar;
        if (circleProgressBar2 != null) {
            circleProgressBar2.setColorSchemeColors(i);
        }
    }

    /* access modifiers changed from: protected */
    public void startAnimation() {
        CircleProgressBar circleProgressBar2 = this.circleProgressBar;
        if (circleProgressBar2 != null) {
            circleProgressBar2.start();
        }
    }

    public void setStartEndTrim(float f, float f2) {
        CircleProgressBar circleProgressBar2 = this.circleProgressBar;
        if (circleProgressBar2 != null) {
            circleProgressBar2.setStartEndTrim(f, f2);
        }
    }

    /* access modifiers changed from: protected */
    public void stopAnimation() {
        CircleProgressBar circleProgressBar2 = this.circleProgressBar;
        if (circleProgressBar2 != null) {
            circleProgressBar2.stop();
        }
    }

    public void setProgressRotation(float f) {
        CircleProgressBar circleProgressBar2 = this.circleProgressBar;
        if (circleProgressBar2 != null) {
            circleProgressBar2.setProgressRotation(f);
        }
    }
}
