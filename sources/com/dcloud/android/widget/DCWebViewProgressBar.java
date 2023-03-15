package com.dcloud.android.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
import com.facebook.common.statfs.StatFsHelper;
import io.dcloud.feature.gg.dcloud.ADSim;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.animation.AnimatorListenerAdapter;
import io.dcloud.nineoldandroids.animation.ObjectAnimator;

public class DCWebViewProgressBar extends ProgressBar {
    int alpha = 255;
    public boolean isFinish = false;
    ObjectAnimator mCurrentAnmiator;

    public DCWebViewProgressBar(Context context) {
        super(context, (AttributeSet) null, 16842872);
        setMax(ADSim.INTISPLSH);
    }

    /* access modifiers changed from: private */
    public ObjectAnimator getProgressAnimation(int i, int i2, Interpolator interpolator, AnimatorListenerAdapter animatorListenerAdapter) {
        ObjectAnimator ofInt = ObjectAnimator.ofInt((Object) this, "progress", getProgress(), i * 100);
        ofInt.setDuration((long) i2);
        ofInt.setInterpolator(interpolator);
        if (animatorListenerAdapter != null) {
            ofInt.addListener(animatorListenerAdapter);
        }
        return ofInt;
    }

    /* access modifiers changed from: private */
    public void startDismissAnimation() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) this, "alpha", 1.0f, 0.0f);
        ofFloat.setDuration(1000);
        ofFloat.setInterpolator(new DecelerateInterpolator());
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                DCWebViewProgressBar.this.setProgress(0);
            }
        });
        ofFloat.start();
    }

    public void finishProgress() {
        if (!this.isFinish) {
            this.isFinish = true;
            ObjectAnimator objectAnimator = this.mCurrentAnmiator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator progressAnimation = getProgressAnimation(100, StatFsHelper.DEFAULT_DISK_YELLOW_LEVEL_IN_MB, new AccelerateInterpolator(), new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    DCWebViewProgressBar dCWebViewProgressBar = DCWebViewProgressBar.this;
                    if (dCWebViewProgressBar.isFinish) {
                        dCWebViewProgressBar.startDismissAnimation();
                    }
                }
            });
            this.mCurrentAnmiator = progressAnimation;
            progressAnimation.start();
        }
    }

    public void setAlphaInt(int i) {
        this.alpha = i;
    }

    public void setColorInt(int i) {
        int argb = Color.argb(this.alpha, Color.red(i), Color.green(i), Color.blue(i));
        ClipDrawable clipDrawable = new ClipDrawable(new ColorDrawable(0), 3, 1);
        clipDrawable.setLevel(ADSim.INTISPLSH);
        ClipDrawable clipDrawable2 = new ClipDrawable(new ColorDrawable(argb), 3, 1);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{clipDrawable, clipDrawable2, clipDrawable2});
        layerDrawable.setId(0, 16908288);
        layerDrawable.setId(1, 16908303);
        layerDrawable.setId(2, 16908301);
        setProgressDrawable(layerDrawable);
    }

    public void startProgress() {
        setProgress(0);
        setAlpha(1.0f);
        this.isFinish = false;
        final DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
        ObjectAnimator progressAnimation = getProgressAnimation(30, 2000, decelerateInterpolator, new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                DCWebViewProgressBar dCWebViewProgressBar = DCWebViewProgressBar.this;
                if (!dCWebViewProgressBar.isFinish) {
                    dCWebViewProgressBar.mCurrentAnmiator = dCWebViewProgressBar.getProgressAnimation(70, 2000, decelerateInterpolator, new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            AnonymousClass1 r5 = AnonymousClass1.this;
                            DCWebViewProgressBar dCWebViewProgressBar = DCWebViewProgressBar.this;
                            if (!dCWebViewProgressBar.isFinish) {
                                dCWebViewProgressBar.mCurrentAnmiator = dCWebViewProgressBar.getProgressAnimation(95, 50000, decelerateInterpolator, (AnimatorListenerAdapter) null);
                                DCWebViewProgressBar.this.mCurrentAnmiator.start();
                            }
                        }
                    });
                    DCWebViewProgressBar.this.mCurrentAnmiator.start();
                }
            }
        });
        this.mCurrentAnmiator = progressAnimation;
        progressAnimation.start();
    }
}
