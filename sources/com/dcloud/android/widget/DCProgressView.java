package com.dcloud.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

public class DCProgressView extends View {
    int alpha = 255;
    int curProgress = 0;
    float h;
    Paint p = new Paint();
    int w;
    int webviewProgress = 0;

    public DCProgressView(Context context) {
        super(context);
        this.w = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void fadeDisappear() {
        postDelayed(new Runnable() {
            public void run() {
                DCProgressView dCProgressView = DCProgressView.this;
                int i = dCProgressView.alpha - 5;
                dCProgressView.alpha = i;
                if (i > 0) {
                    dCProgressView.postDelayed(this, 5);
                } else {
                    ViewGroup viewGroup = (ViewGroup) dCProgressView.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(DCProgressView.this);
                    }
                }
                DCProgressView.this.invalidate();
            }
        }, 50);
    }

    public void finishProgress() {
        updateProgress(100);
    }

    public float getHeightInt() {
        return this.h;
    }

    public boolean isFinish() {
        return this.curProgress >= this.w;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.p.setAlpha(this.alpha);
        canvas.drawRect(0.0f, 0.0f, (float) this.curProgress, this.h, this.p);
    }

    public void setAlphaInt(int i) {
        this.alpha = i;
    }

    public void setColorInt(int i) {
        this.p.setColor(Color.argb(this.alpha, Color.red(i), Color.green(i), Color.blue(i)));
    }

    public void setCurProgress(int i) {
        this.curProgress = i;
    }

    public void setHeightInt(int i) {
        this.h = (float) i;
    }

    public void setMaxProgress(int i) {
        this.w = i;
    }

    public void setWebviewProgress(int i) {
        this.webviewProgress = i;
    }

    public void updateProgress(int i) {
        int i2 = (this.w * i) / 100;
        if (this.curProgress >= this.webviewProgress) {
            postDelayed(new Runnable() {
                public void run() {
                    DCProgressView dCProgressView = DCProgressView.this;
                    int i = dCProgressView.webviewProgress;
                    int i2 = dCProgressView.curProgress;
                    int i3 = 10;
                    int i4 = (i - i2) / 10;
                    if (i4 <= 10) {
                        i3 = i4 < 1 ? 1 : i4;
                    }
                    int i5 = i2 + i3;
                    dCProgressView.curProgress = i5;
                    if (i > i5) {
                        dCProgressView.postDelayed(this, 5);
                    } else if (i >= dCProgressView.w) {
                        dCProgressView.fadeDisappear();
                    }
                    DCProgressView.this.invalidate();
                }
            }, 5);
        }
        this.webviewProgress = i2;
    }
}
