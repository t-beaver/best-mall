package com.taobao.weex.ui.component.richtext.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;
import android.view.View;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IDrawableLoader;
import com.taobao.weex.ui.component.WXComponent;

public class ImgSpan extends ReplacementSpan implements IDrawableLoader.StaticTarget {
    private int height;
    private String instanceId;
    private Drawable mDrawable;
    private View mView;
    private String ref;
    private int width;

    public ImgSpan(int i, int i2, String str, String str2) {
        this.width = i;
        this.height = i2;
        this.instanceId = str;
        this.ref = str2;
    }

    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        WXComponent wXComponent;
        if (this.mView == null && (wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.instanceId, this.ref)) != null && wXComponent.getHostView() != null && this.width > wXComponent.getHostView().getWidth() && wXComponent.getHostView().getWidth() > 0) {
            int width2 = wXComponent.getHostView().getWidth();
            int i3 = (this.height * width2) / this.width;
            this.height = i3;
            this.width = width2;
            Drawable drawable = this.mDrawable;
            if (drawable != null) {
                drawable.setBounds(0, 0, width2, i3);
            }
        }
        if (fontMetricsInt != null) {
            fontMetricsInt.ascent = -this.height;
            fontMetricsInt.descent = 0;
            fontMetricsInt.top = fontMetricsInt.ascent;
            fontMetricsInt.bottom = 0;
        }
        return this.width;
    }

    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        if (this.mDrawable != null) {
            canvas.save();
            canvas.translate(f, (float) ((i5 - this.mDrawable.getBounds().bottom) - paint.getFontMetricsInt().descent));
            this.mDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public void setDrawable(Drawable drawable, boolean z) {
        this.mDrawable = drawable;
        if (z) {
            drawable.setBounds(0, 0, this.width, this.height);
        }
        setCallback();
        this.mDrawable.invalidateSelf();
    }

    public void setView(View view) {
        this.mView = view;
        setCallback();
    }

    private void setCallback() {
        View view;
        Drawable drawable = this.mDrawable;
        if (drawable != null && (view = this.mView) != null) {
            drawable.setCallback(view);
        }
    }
}
