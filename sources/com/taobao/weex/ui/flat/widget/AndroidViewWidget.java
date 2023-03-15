package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.border.BorderDrawable;

public class AndroidViewWidget extends BaseWidget implements Destroyable {
    private View mView;

    public /* bridge */ /* synthetic */ void setBackgroundAndBorder(BorderDrawable borderDrawable) {
        super.setBackgroundAndBorder(borderDrawable);
    }

    public /* bridge */ /* synthetic */ void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        super.setLayout(i, i2, i3, i4, i5, i6, point);
    }

    public AndroidViewWidget(FlatGUIContext flatGUIContext) {
        super(flatGUIContext);
    }

    public void setContentView(View view) {
        this.mView = view;
    }

    public void setContentBox(int i, int i2, int i3, int i4) {
        View view = this.mView;
        if (view != null) {
            view.setPadding(i, i2, i3, i4);
            invalidate();
        }
    }

    public void onDraw(Canvas canvas) {
        View view = this.mView;
        if (view != null) {
            view.draw(canvas);
        }
    }

    public void invalidate() {
        super.invalidate();
        View view = this.mView;
        if (view != null) {
            view.invalidate();
        }
    }

    public View getView() {
        return this.mView;
    }

    public void destroy() {
        if (this.mView != null) {
            this.mView = null;
        }
    }
}
