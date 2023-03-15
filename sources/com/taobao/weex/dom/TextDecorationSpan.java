package com.taobao.weex.dom;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import com.taobao.weex.ui.component.WXTextDecoration;

public class TextDecorationSpan extends CharacterStyle implements UpdateAppearance {
    private final WXTextDecoration mTextDecoration;

    public TextDecorationSpan(WXTextDecoration wXTextDecoration) {
        this.mTextDecoration = wXTextDecoration;
    }

    /* renamed from: com.taobao.weex.dom.TextDecorationSpan$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$weex$ui$component$WXTextDecoration;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.taobao.weex.ui.component.WXTextDecoration[] r0 = com.taobao.weex.ui.component.WXTextDecoration.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$taobao$weex$ui$component$WXTextDecoration = r0
                com.taobao.weex.ui.component.WXTextDecoration r1 = com.taobao.weex.ui.component.WXTextDecoration.LINETHROUGH     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$taobao$weex$ui$component$WXTextDecoration     // Catch:{ NoSuchFieldError -> 0x001d }
                com.taobao.weex.ui.component.WXTextDecoration r1 = com.taobao.weex.ui.component.WXTextDecoration.UNDERLINE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$taobao$weex$ui$component$WXTextDecoration     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.taobao.weex.ui.component.WXTextDecoration r1 = com.taobao.weex.ui.component.WXTextDecoration.NONE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.dom.TextDecorationSpan.AnonymousClass1.<clinit>():void");
        }
    }

    public void updateDrawState(TextPaint textPaint) {
        int i = AnonymousClass1.$SwitchMap$com$taobao$weex$ui$component$WXTextDecoration[this.mTextDecoration.ordinal()];
        if (i == 1) {
            textPaint.setUnderlineText(false);
            textPaint.setStrikeThruText(true);
        } else if (i == 2) {
            textPaint.setUnderlineText(true);
            textPaint.setStrikeThruText(false);
        } else if (i == 3) {
            textPaint.setUnderlineText(false);
            textPaint.setStrikeThruText(false);
        }
    }
}
