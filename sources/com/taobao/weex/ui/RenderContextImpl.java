package com.taobao.weex.ui;

import android.text.TextUtils;
import android.view.ViewGroup;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.RenderContext;
import com.taobao.weex.ui.component.DCWXScroller;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.feature.weex_scroller.view.DCWXScrollView;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

class RenderContextImpl implements RenderContext {
    private Map<String, WXComponent> mIdComponent = new ConcurrentHashMap();
    private Map<String, WXComponent> mRegistry = new ConcurrentHashMap();
    private List<DCWXScroller> mScrollers = new CopyOnWriteArrayList();
    private WXSDKInstance mWXSDKInstance;

    public RenderContextImpl(WXSDKInstance wXSDKInstance) {
        this.mWXSDKInstance = wXSDKInstance;
    }

    public void destroy() {
        this.mWXSDKInstance = null;
        try {
            this.mRegistry.clear();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public WXSDKInstance getWXSDKInstance() {
        return this.mWXSDKInstance;
    }

    public WXSDKInstance getInstance() {
        return this.mWXSDKInstance;
    }

    public WXComponent getComponent(String str) {
        return this.mRegistry.get(str);
    }

    public WXComponent getComponentById(String str) {
        return this.mIdComponent.get(str);
    }

    public void registerComponent(String str, WXComponent wXComponent) {
        if (wXComponent instanceof DCWXScroller) {
            this.mScrollers.add((DCWXScroller) wXComponent);
        }
        if (wXComponent.getAttrs().containsKey("id")) {
            String str2 = (String) wXComponent.getAttrs().get("id");
            if (!TextUtils.isEmpty(str2)) {
                this.mIdComponent.put(str2, wXComponent);
            }
        }
        this.mRegistry.put(str, wXComponent);
    }

    public WXComponent unregisterComponent(String str) {
        return this.mRegistry.remove(str);
    }

    public void setAllScrollerScrollable(boolean z, String str) {
        if (this.mScrollers.size() > 0) {
            Scrollable parentScroller = getComponent(str).getParentScroller();
            while (parentScroller != null) {
                if (parentScroller instanceof DCWXScroller) {
                    DCWXScroller dCWXScroller = (DCWXScroller) parentScroller;
                    ViewGroup innerView = dCWXScroller.getInnerView();
                    if (innerView instanceof DCWXScrollView) {
                        ((DCWXScrollView) innerView).setScrollable(z);
                    }
                    parentScroller = dCWXScroller.getParentScroller();
                } else {
                    parentScroller = ((WXComponent) parentScroller).getParentScroller();
                }
            }
        }
    }

    public int getComponentCount() {
        return this.mRegistry.size();
    }
}
