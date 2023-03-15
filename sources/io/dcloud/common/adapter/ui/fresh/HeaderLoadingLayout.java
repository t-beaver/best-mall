package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import io.dcloud.common.adapter.ui.fresh.ILoadingLayout;

public class HeaderLoadingLayout extends LoadingLayout {
    private static final int ROTATE_ANIM_DURATION = 150;

    public HeaderLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
    }

    /* access modifiers changed from: protected */
    public View createLoadingView(Context context, AttributeSet attributeSet) {
        return new TextView(context);
    }

    public int getContentSize() {
        return (int) (getResources().getDisplayMetrics().density * 100.0f);
    }

    /* access modifiers changed from: protected */
    public void onPullToRefresh() {
    }

    /* access modifiers changed from: protected */
    public void onRefreshing() {
    }

    /* access modifiers changed from: protected */
    public void onReleaseToRefresh() {
    }

    /* access modifiers changed from: protected */
    public void onReset() {
    }

    /* access modifiers changed from: protected */
    public void onStateChanged(ILoadingLayout.State state, ILoadingLayout.State state2) {
        super.onStateChanged(state, state2);
    }

    public void setLastUpdatedLabel(CharSequence charSequence) {
    }

    public HeaderLoadingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }
}
