package io.dcloud.feature.nativeObj.richtext.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

public class HrSpan extends ReplacementSpan {
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    int align;
    int color;
    int hrWidth;
    int size;
    int width;

    public HrSpan(int i, int i2, int i3, int i4, int i5) {
        this.align = i;
        this.size = i2;
        this.width = i3;
        this.color = i4;
        this.hrWidth = i5;
    }

    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        paint.setStrokeWidth((float) this.size);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.color);
        int i6 = this.align;
        if (i6 == 0) {
            int i7 = this.hrWidth;
            int i8 = this.width;
            float f2 = (float) ((i7 - i8) / 2);
            canvas.drawRect(f + f2, (float) i4, f + ((float) i8) + f2, (float) (i4 + this.size), paint);
        } else if (i6 == 2) {
            float f3 = f + ((float) this.hrWidth);
            canvas.drawRect(f3 - ((float) this.width), (float) i4, f3, (float) (i4 + this.size), paint);
        } else if (i6 == 1) {
            canvas.drawRect(f, (float) i4, f + ((float) this.width), (float) (i4 + this.size), paint);
        }
    }

    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        if (fontMetricsInt != null) {
            fontMetricsInt.ascent = 0;
            int i3 = this.size;
            fontMetricsInt.descent = i3;
            fontMetricsInt.bottom = i3;
            fontMetricsInt.top = 0;
        }
        return this.hrWidth;
    }
}
