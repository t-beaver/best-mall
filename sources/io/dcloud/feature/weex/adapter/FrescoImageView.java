package io.dcloud.feature.weex.adapter;

import android.content.Context;
import android.view.MotionEvent;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import io.dcloud.feature.weex.adapter.Fresco.DCGenericDraweeHierarchy;
import io.dcloud.feature.weex.adapter.Fresco.DCGenericDraweeView;

public class FrescoImageView extends DCGenericDraweeView implements WXGestureObservable {
    private WXGesture wxGesture;

    public FrescoImageView(Context context) {
        super(context);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        WXGesture wXGesture = this.wxGesture;
        return wXGesture != null ? onTouchEvent | wXGesture.onTouch(this, motionEvent) : onTouchEvent;
    }

    public void setFadeShow(boolean z) {
        if (getHierarchy() == null) {
            return;
        }
        if (z) {
            ((DCGenericDraweeHierarchy) getHierarchy()).setFadeDuration(300);
            ((DCGenericDraweeHierarchy) getHierarchy()).getTopLevelDrawable().setRefresh(true);
            return;
        }
        ((DCGenericDraweeHierarchy) getHierarchy()).setFadeDuration(0);
        ((DCGenericDraweeHierarchy) getHierarchy()).getTopLevelDrawable().setRefresh(false);
    }
}
