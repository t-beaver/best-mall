package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import io.dcloud.common.adapter.util.Logger;

public class AdaRootView extends AdaContainerFrameItem {
    FrameLayout mMyRootView = null;

    public static class GestureListenerImpl implements GestureOverlayView.OnGestureListener {
        public void onGesture(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        public void onGestureCancelled(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }

        public void onGestureEnded(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
            gestureOverlayView.getGesture().getStrokes();
        }

        public void onGestureStarted(GestureOverlayView gestureOverlayView, MotionEvent motionEvent) {
        }
    }

    class MyRootView extends FrameLayout {
        AdaRootView mProxy = null;

        public MyRootView(Context context, AdaRootView adaRootView) {
            super(context);
            this.mProxy = adaRootView;
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            AdaRootView.this.mViewOptions.onScreenChanged();
        }

        /* access modifiers changed from: protected */
        public void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            AdaRootView.this.mViewOptions.onScreenChanged(i, i2);
            Logger.d(Logger.LAYOUT_TAG, "AdaRootView onSizeChanged", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
        }
    }

    protected AdaRootView(Context context, FrameLayout frameLayout) {
        super(context);
        if (frameLayout != null) {
            this.mMyRootView = frameLayout;
        } else {
            this.mMyRootView = new MyRootView(context, this);
        }
        setMainView(this.mMyRootView);
    }
}
