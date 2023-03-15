package io.dcloud.feature.weex.adapter.webview.video;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class FullscreenHolder extends FrameLayout {
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public FullscreenHolder(Context context) {
        super(context);
        setBackgroundColor(context.getResources().getColor(17170444));
    }
}
