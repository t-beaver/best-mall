package io.dcloud.feature.weex_scroller.view;

import android.content.Context;
import com.taobao.weex.ui.component.DCWXScroller;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;

public class DCBounceScrollerView extends BaseBounceView<DCWXScrollView> {
    public void onLoadmoreComplete() {
    }

    public void onRefreshingComplete() {
    }

    public DCBounceScrollerView(Context context, int i, DCWXScroller dCWXScroller) {
        super(context, i);
        init(context);
        if (getInnerView() != null) {
            ((DCWXScrollView) getInnerView()).setWAScroller(dCWXScroller);
        }
    }

    public DCWXScrollView setInnerView(Context context) {
        return new DCWXScrollView(context);
    }
}
