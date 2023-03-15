package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

public class PullToRefreshWebView extends PullToRefreshBase<View> {
    public PullToRefreshWebView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public boolean isReadyForPullDown() {
        return this.mRefreshableView.getScrollY() == 0;
    }

    /* access modifiers changed from: protected */
    public boolean isReadyForPullUp() {
        float f = this.mRefreshableView.getContext().getResources().getDisplayMetrics().density;
        int measuredHeight = this.mRefreshableView.getMeasuredHeight();
        T t = this.mRefreshableView;
        if (t instanceof WebView) {
            measuredHeight = ((WebView) t).getContentHeight();
        }
        double floor = Math.floor(Double.valueOf(String.valueOf(((float) measuredHeight) * f)).doubleValue());
        double height = (double) this.mRefreshableView.getHeight();
        Double.isNaN(height);
        return ((double) this.mRefreshableView.getScrollY()) >= floor - height;
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }
}
