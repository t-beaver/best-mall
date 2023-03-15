package com.taobao.weex.ui.animation;

import android.view.View;
import android.view.ViewGroup;

public class WidthProperty extends LayoutParamsProperty {
    public /* bridge */ /* synthetic */ Integer get(View view) {
        return super.get(view);
    }

    public /* bridge */ /* synthetic */ void set(View view, Integer num) {
        super.set(view, num);
    }

    /* access modifiers changed from: protected */
    public Integer getProperty(ViewGroup.LayoutParams layoutParams) {
        return Integer.valueOf(layoutParams.width);
    }

    /* access modifiers changed from: protected */
    public void setProperty(ViewGroup.LayoutParams layoutParams, Integer num) {
        layoutParams.width = num.intValue();
    }
}
