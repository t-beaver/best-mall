package io.dcloud.feature.weex_switch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SwitchGroup extends LinearLayout {
    private static final int DEFAULT_HEIGHT = dp2pxInt(36.0f);
    private static final int DEFAULT_WIDTH = dp2pxInt(58.0f);
    int i = 0;

    public SwitchGroup(Context context) {
        super(context);
        addView(new SwitchButton(context), new LinearLayout.LayoutParams(-1, -1));
    }

    public SwitchGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SwitchGroup(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        super.onLayout(z, i2, i3, i4, i5);
        int childCount = getChildCount();
        for (int i6 = 0; i6 < childCount; i6++) {
            SwitchButton switchButton = (SwitchButton) getChildAt(i6);
            switchButton.layout(0, 0, switchButton.getMeasuredWidth(), ((int) (((float) switchButton.getMeasuredHeight()) + switchButton.getShadowBottomSize())) + 0);
        }
        disableClipOnParents(this);
    }

    public void disableClipOnParents(View view) {
        int i2;
        if (view.getParent() != null) {
            if (view instanceof ViewGroup) {
                ((ViewGroup) view).setClipChildren(false);
            }
            if ((view.getParent() instanceof View) && (i2 = this.i) < 2) {
                this.i = i2 + 1;
                disableClipOnParents((View) view.getParent());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i2, int i3) {
        int mode = View.MeasureSpec.getMode(i2);
        int mode2 = View.MeasureSpec.getMode(i3);
        if (mode == 0 || mode == Integer.MIN_VALUE) {
            i2 = View.MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, 1073741824);
        }
        if (mode2 == 0 || mode2 == Integer.MIN_VALUE) {
            i3 = View.MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, 1073741824);
        }
        super.onMeasure(i2, i3);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i2, int i3, int i4, int i5) {
        super.onSizeChanged(i2, i3, i4, i5);
    }

    private static int dp2pxInt(float f) {
        return (int) dp2px(f);
    }

    private static float dp2px(float f) {
        return TypedValue.applyDimension(1, f, Resources.getSystem().getDisplayMetrics());
    }
}
