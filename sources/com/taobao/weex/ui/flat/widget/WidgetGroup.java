package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.border.BorderDrawable;
import java.util.LinkedList;
import java.util.List;

public class WidgetGroup extends BaseWidget {
    private List<Widget> mChildren = new LinkedList();

    public /* bridge */ /* synthetic */ void setBackgroundAndBorder(BorderDrawable borderDrawable) {
        super.setBackgroundAndBorder(borderDrawable);
    }

    public /* bridge */ /* synthetic */ void setContentBox(int i, int i2, int i3, int i4) {
        super.setContentBox(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        super.setLayout(i, i2, i3, i4, i5, i6, point);
    }

    public WidgetGroup(FlatGUIContext flatGUIContext) {
        super(flatGUIContext);
    }

    public void replaceAll(List<Widget> list) {
        this.mChildren = list;
        invalidate();
    }

    public List<Widget> getChildren() {
        return this.mChildren;
    }

    public void onDraw(Canvas canvas) {
        for (Widget draw : this.mChildren) {
            draw.draw(canvas);
        }
    }
}
