package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import com.taobao.weex.ui.view.border.BorderDrawable;

public interface Widget {
    public static final String TAG = "Widget";

    void draw(Canvas canvas);

    BorderDrawable getBackgroundAndBorder();

    Rect getBorderBox();

    Point getLocInFlatContainer();

    void onDraw(Canvas canvas);

    void setBackgroundAndBorder(BorderDrawable borderDrawable);

    void setContentBox(int i, int i2, int i3, int i4);

    void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point);
}
