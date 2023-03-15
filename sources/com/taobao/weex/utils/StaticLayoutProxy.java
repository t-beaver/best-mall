package com.taobao.weex.utils;

import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import java.lang.reflect.Constructor;

public class StaticLayoutProxy {
    private static Constructor<StaticLayout> layoutConstructor;

    public static StaticLayout create(CharSequence charSequence, TextPaint textPaint, int i, Layout.Alignment alignment, float f, float f2, boolean z, boolean z2) {
        if (Build.VERSION.SDK_INT < 18 || !z2) {
            return new StaticLayout(charSequence, textPaint, i, alignment, f, f2, z);
        }
        StaticLayout createInternal = createInternal(charSequence, textPaint, i, alignment, TextDirectionHeuristics.RTL, f, f2, z);
        if (createInternal != null) {
            return createInternal;
        }
        return new StaticLayout(charSequence, textPaint, i, alignment, f, f2, z);
    }

    private static StaticLayout createInternal(CharSequence charSequence, TextPaint textPaint, int i, Layout.Alignment alignment, TextDirectionHeuristic textDirectionHeuristic, float f, float f2, boolean z) {
        if (Build.VERSION.SDK_INT < 18) {
            return null;
        }
        try {
            if (layoutConstructor == null) {
                layoutConstructor = StaticLayout.class.getConstructor(new Class[]{CharSequence.class, TextPaint.class, Integer.TYPE, Layout.Alignment.class, TextDirectionHeuristic.class, Float.TYPE, Float.TYPE, Boolean.TYPE});
            }
            Constructor<StaticLayout> constructor = layoutConstructor;
            if (constructor != null) {
                return constructor.newInstance(new Object[]{charSequence, textPaint, Integer.valueOf(i), alignment, textDirectionHeuristic, Float.valueOf(f), Float.valueOf(f2), Boolean.valueOf(z)});
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}
