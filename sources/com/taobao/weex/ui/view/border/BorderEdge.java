package com.taobao.weex.ui.view.border;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.taobao.weex.dom.CSSShorthand;

class BorderEdge {
    private float mBorderWidth;
    private CSSShorthand.EDGE mEdge;
    private BorderCorner mPostCorner;
    private BorderCorner mPreCorner;

    BorderEdge() {
    }

    /* access modifiers changed from: package-private */
    public BorderEdge set(BorderCorner borderCorner, BorderCorner borderCorner2, float f, CSSShorthand.EDGE edge) {
        this.mPreCorner = borderCorner;
        this.mPostCorner = borderCorner2;
        this.mBorderWidth = f;
        this.mEdge = edge;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void drawEdge(Canvas canvas, Paint paint) {
        paint.setStrokeWidth(this.mBorderWidth);
        BorderCorner borderCorner = this.mPreCorner;
        borderCorner.drawRoundedCorner(canvas, paint, borderCorner.getAngleBisectorDegree());
        paint.setStrokeWidth(this.mBorderWidth);
        canvas.drawLine(this.mPreCorner.getRoundCornerEndX(), this.mPreCorner.getRoundCornerEndY(), this.mPostCorner.getRoundCornerStartX(), this.mPostCorner.getRoundCornerStartY(), paint);
        BorderCorner borderCorner2 = this.mPostCorner;
        borderCorner2.drawRoundedCorner(canvas, paint, borderCorner2.getAngleBisectorDegree() - 45.0f);
    }

    public CSSShorthand.EDGE getEdge() {
        return this.mEdge;
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }
}
