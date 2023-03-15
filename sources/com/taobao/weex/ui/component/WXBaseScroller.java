package com.taobao.weex.ui.component;

import android.view.ViewGroup;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXScrollView;
import java.util.Map;

@Component(lazyload = false)
public class WXBaseScroller extends WXVContainer<ViewGroup> implements WXScrollView.WXScrollViewListener, Scrollable {
    public void bindAppearEvent(WXComponent wXComponent) {
    }

    public void bindDisappearEvent(WXComponent wXComponent) {
    }

    public void bindStickStyle(WXComponent wXComponent) {
    }

    public ViewGroup getInnerView() {
        return null;
    }

    public int getOrientation() {
        return 0;
    }

    public Map<String, Object> getScrollEvent(int i, int i2) {
        return null;
    }

    public int getScrollX() {
        return 0;
    }

    public int getScrollY() {
        return 0;
    }

    public Map<String, Map<String, WXComponent>> getStickMap() {
        return null;
    }

    public boolean isScrollable() {
        return false;
    }

    public void onScroll(WXScrollView wXScrollView, int i, int i2) {
    }

    public void onScrollChanged(WXScrollView wXScrollView, int i, int i2, int i3, int i4) {
    }

    public void onScrollStopped(WXScrollView wXScrollView, int i, int i2) {
    }

    public void onScrollToBottom(WXScrollView wXScrollView, int i, int i2) {
    }

    public void scrollTo(WXComponent wXComponent, Map<String, Object> map) {
    }

    public void unbindAppearEvent(WXComponent wXComponent) {
    }

    public void unbindDisappearEvent(WXComponent wXComponent) {
    }

    public void unbindStickStyle(WXComponent wXComponent) {
    }

    public WXBaseScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, str, z, basicComponentData);
    }

    public WXBaseScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXBaseScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }
}
