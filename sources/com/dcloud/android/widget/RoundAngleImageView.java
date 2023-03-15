package com.dcloud.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import io.dcloud.common.adapter.util.CanvasHelper;

public class RoundAngleImageView extends ImageView {
    Path path = new Path();

    public RoundAngleImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.save();
        this.path.addRoundRect(new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight()), (float) CanvasHelper.dip2px(getContext(), 8.0f), (float) CanvasHelper.dip2px(getContext(), 8.0f), Path.Direction.CCW);
        canvas.clipPath(this.path);
        if (getDrawable() == null) {
            canvas.drawColor(-3355444);
        }
        super.onDraw(canvas);
        canvas.restore();
    }
}
