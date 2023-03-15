package com.taobao.weex.ui.view.border;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import com.taobao.weex.dom.CSSShorthand;

enum BorderStyle {
    SOLID,
    DASHED,
    DOTTED;

    /* renamed from: com.taobao.weex.ui.view.border.BorderStyle$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$weex$ui$view$border$BorderStyle = null;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.taobao.weex.ui.view.border.BorderStyle[] r0 = com.taobao.weex.ui.view.border.BorderStyle.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$taobao$weex$ui$view$border$BorderStyle = r0
                com.taobao.weex.ui.view.border.BorderStyle r1 = com.taobao.weex.ui.view.border.BorderStyle.DOTTED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$taobao$weex$ui$view$border$BorderStyle     // Catch:{ NoSuchFieldError -> 0x001d }
                com.taobao.weex.ui.view.border.BorderStyle r1 = com.taobao.weex.ui.view.border.BorderStyle.DASHED     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.border.BorderStyle.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public Shader getLineShader(float f, int i, CSSShorthand.EDGE edge) {
        CSSShorthand.EDGE edge2 = edge;
        int i2 = AnonymousClass1.$SwitchMap$com$taobao$weex$ui$view$border$BorderStyle[ordinal()];
        if (i2 != 1) {
            if (i2 != 2) {
                return null;
            }
        } else if (edge2 == CSSShorthand.EDGE.LEFT || edge2 == CSSShorthand.EDGE.RIGHT) {
            return new LinearGradient(0.0f, 0.0f, 0.0f, f * 2.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
        } else if (edge2 == CSSShorthand.EDGE.TOP || edge2 == CSSShorthand.EDGE.BOTTOM) {
            return new LinearGradient(0.0f, 0.0f, f * 2.0f, 0.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
        }
        if (edge2 == CSSShorthand.EDGE.LEFT || edge2 == CSSShorthand.EDGE.RIGHT) {
            return new LinearGradient(0.0f, 0.0f, 0.0f, f * 6.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
        } else if (edge2 != CSSShorthand.EDGE.TOP && edge2 != CSSShorthand.EDGE.BOTTOM) {
            return null;
        } else {
            return new LinearGradient(0.0f, 0.0f, 6.0f * f, 0.0f, new int[]{i, 0}, new float[]{0.5f, 0.5f}, Shader.TileMode.REPEAT);
        }
    }
}
