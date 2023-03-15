package com.taobao.weex.ui.view.gesture;

public interface WXGestureObservable {
    WXGesture getGestureListener();

    void registerGestureListener(WXGesture wXGesture);
}
