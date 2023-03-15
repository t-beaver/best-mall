package com.taobao.weex.ui.view.refresh.wrapper;

import android.content.Context;
import com.taobao.weex.ui.component.WXBaseScroller;
import com.taobao.weex.ui.view.WXScrollView;

public class BounceScrollerView extends BaseBounceView<WXScrollView> {
    public void onLoadmoreComplete() {
    }

    public void onRefreshingComplete() {
    }

    public BounceScrollerView(Context context, int i, WXBaseScroller wXBaseScroller) {
        super(context, i);
        init(context);
        if (getInnerView() != null) {
            ((WXScrollView) getInnerView()).setWAScroller(wXBaseScroller);
        }
    }

    public WXScrollView setInnerView(Context context) {
        return new WXScrollView(context);
    }
}
