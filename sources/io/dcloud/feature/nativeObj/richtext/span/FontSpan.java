package io.dcloud.feature.nativeObj.richtext.span;

import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;

public class FontSpan extends AbsoluteSizeSpan {
    public static final int DECORATION_LINE_THROUGH = 2;
    public static final int DECORATION_NONE = 0;
    public static final int DECORATION_UNDERLINE = 1;
    public static float DEF_FONT_SIZE = 12.0f;
    public static final int STYLE_ITALIC = 1;
    public static final int STYLE_NORMAL = 0;
    public static final int WEIGHT_BOLD = 1;
    public static final int WEIGHT_NORMAL = 0;
    int color = -16777216;
    int decoration = 0;
    int style = 0;
    int weight = 0;

    public FontSpan(float f, int i, int i2, int i3, int i4) {
        super((int) f, true);
        this.color = i;
        this.weight = i2;
        this.style = i3;
        this.decoration = i4;
    }

    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(this.color);
        int i = this.decoration;
        if (i == 2) {
            textPaint.setStrikeThruText(true);
        } else if (i == 1) {
            textPaint.setUnderlineText(true);
        }
        if (this.style == 1) {
            textPaint.setTextSkewX(-0.3f);
        }
        if (this.weight == 1) {
            textPaint.setFakeBoldText(true);
        } else {
            textPaint.setFakeBoldText(false);
        }
        super.updateDrawState(textPaint);
    }
}
