package com.taobao.weex.ui.flat.widget;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.utils.WXViewUtils;

abstract class BaseWidget implements Widget {
    private BorderDrawable backgroundBorder;
    private Rect borderBox = new Rect();
    private int bottomOffset;
    private final FlatGUIContext context;
    private int leftOffset;
    private Point offsetOfContainer = new Point();
    private int rightOffset;
    private int topOffset;

    BaseWidget(FlatGUIContext flatGUIContext) {
        this.context = flatGUIContext;
    }

    public void setLayout(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        this.offsetOfContainer = point;
        this.borderBox.set(i3, i5, i + i3, i2 + i5);
        BorderDrawable borderDrawable = this.backgroundBorder;
        if (borderDrawable != null) {
            setBackgroundAndBorder(borderDrawable);
        }
        invalidate();
    }

    public void setContentBox(int i, int i2, int i3, int i4) {
        this.leftOffset = i;
        this.topOffset = i2;
        this.rightOffset = i3;
        this.bottomOffset = i4;
        invalidate();
    }

    public void setBackgroundAndBorder(BorderDrawable borderDrawable) {
        this.backgroundBorder = borderDrawable;
        Rect rect = new Rect(this.borderBox);
        rect.offset(-this.borderBox.left, -this.borderBox.top);
        borderDrawable.setBounds(rect);
        setCallback(borderDrawable);
        invalidate();
    }

    public final Point getLocInFlatContainer() {
        return this.offsetOfContainer;
    }

    public final BorderDrawable getBackgroundAndBorder() {
        return this.backgroundBorder;
    }

    public final Rect getBorderBox() {
        return this.borderBox;
    }

    public final void draw(Canvas canvas) {
        canvas.save();
        WXViewUtils.clipCanvasWithinBorderBox((Widget) this, canvas);
        canvas.translate((float) this.borderBox.left, (float) this.borderBox.top);
        BorderDrawable borderDrawable = this.backgroundBorder;
        if (borderDrawable != null) {
            borderDrawable.draw(canvas);
        }
        canvas.clipRect(this.leftOffset, this.topOffset, this.borderBox.width() - this.rightOffset, this.borderBox.height() - this.bottomOffset);
        canvas.translate((float) this.leftOffset, (float) this.topOffset);
        onDraw(canvas);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void invalidate() {
        View widgetContainerView;
        Rect rect = new Rect(this.borderBox);
        rect.offset(this.offsetOfContainer.x, this.offsetOfContainer.y);
        FlatGUIContext flatGUIContext = this.context;
        if (flatGUIContext != null && (widgetContainerView = flatGUIContext.getWidgetContainerView(this)) != null) {
            widgetContainerView.invalidate(rect);
        }
    }

    /* access modifiers changed from: protected */
    public void setCallback(Drawable drawable) {
        View widgetContainerView;
        FlatGUIContext flatGUIContext = this.context;
        if (flatGUIContext != null && (widgetContainerView = flatGUIContext.getWidgetContainerView(this)) != null) {
            drawable.setCallback(widgetContainerView);
        }
    }
}
