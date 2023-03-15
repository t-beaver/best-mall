package com.taobao.weex.dom;

import android.graphics.Paint;
import android.text.style.LineHeightSpan;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;

public class WXLineHeightSpan implements LineHeightSpan {
    private int lineHeight;

    public WXLineHeightSpan(int i) {
        this.lineHeight = i;
    }

    public void chooseHeight(CharSequence charSequence, int i, int i2, int i3, int i4, Paint.FontMetricsInt fontMetricsInt) {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("LineHeight", charSequence + " ; start " + i + "; end " + i2 + "; spanstartv " + i3 + "; v " + i4 + "; fm " + fontMetricsInt);
        }
        int i5 = this.lineHeight - (fontMetricsInt.descent - fontMetricsInt.ascent);
        int i6 = i5 / 2;
        int i7 = i5 - i6;
        fontMetricsInt.top -= i7;
        fontMetricsInt.bottom += i6;
        fontMetricsInt.ascent -= i7;
        fontMetricsInt.descent += i6;
    }
}
