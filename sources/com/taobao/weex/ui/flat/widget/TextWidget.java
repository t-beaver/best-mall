package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Layout;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.border.BorderDrawable;

public class TextWidget extends BaseWidget {
    private Layout mLayout;

    public /* bridge */ /* synthetic */ void setBackgroundAndBorder(BorderDrawable borderDrawable) {
        super.setBackgroundAndBorder(borderDrawable);
    }

    public /* bridge */ /* synthetic */ void setContentBox(int i, int i2, int i3, int i4) {
        super.setContentBox(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        super.setLayout(i, i2, i3, i4, i5, i6, point);
    }

    public TextWidget(FlatGUIContext flatGUIContext) {
        super(flatGUIContext);
    }

    public void onDraw(Canvas canvas) {
        Layout layout = this.mLayout;
        if (layout != null) {
            layout.draw(canvas);
        }
    }

    public void updateTextDrawable(Layout layout) {
        this.mLayout = layout;
        invalidate();
    }
}
