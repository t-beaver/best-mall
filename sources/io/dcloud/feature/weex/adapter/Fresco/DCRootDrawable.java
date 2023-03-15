package io.dcloud.feature.weex.adapter.Fresco;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.facebook.drawee.drawable.VisibilityCallback;
import com.facebook.drawee.generic.RootDrawable;

public class DCRootDrawable extends RootDrawable {
    private boolean isRefresh = true;
    private VisibilityCallback mVisibilityCallback;

    public DCRootDrawable(Drawable drawable) {
        super(drawable);
    }

    public void setRefresh(boolean z) {
        this.isRefresh = z;
    }

    public void setVisibilityCallback(VisibilityCallback visibilityCallback) {
        this.mVisibilityCallback = visibilityCallback;
    }

    public boolean setVisible(boolean z, boolean z2) {
        VisibilityCallback visibilityCallback = this.mVisibilityCallback;
        if (visibilityCallback != null && this.isRefresh) {
            try {
                visibilityCallback.onVisibilityChange(z);
            } catch (Exception unused) {
            }
        }
        return super.setVisible(z, z2);
    }

    public void draw(Canvas canvas) {
        if (isVisible()) {
            VisibilityCallback visibilityCallback = this.mVisibilityCallback;
            if (visibilityCallback != null) {
                visibilityCallback.onDraw();
            }
            super.draw(canvas);
        }
    }
}
