package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import io.dcloud.common.adapter.ui.fresh.ILoadingLayout;

public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {
    private View mContainer;
    private ILoadingLayout.State mCurState;
    private ILoadingLayout.State mPreState;

    /* renamed from: io.dcloud.common.adapter.ui.fresh.LoadingLayout$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State[] r0 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State = r0
                io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r1 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.RESET     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r1 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.RELEASE_TO_REFRESH     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State     // Catch:{ NoSuchFieldError -> 0x0028 }
                io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r1 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.PULL_TO_REFRESH     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State     // Catch:{ NoSuchFieldError -> 0x0033 }
                io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r1 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.REFRESHING     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State     // Catch:{ NoSuchFieldError -> 0x003e }
                io.dcloud.common.adapter.ui.fresh.ILoadingLayout$State r1 = io.dcloud.common.adapter.ui.fresh.ILoadingLayout.State.NO_MORE_DATA     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.fresh.LoadingLayout.AnonymousClass1.<clinit>():void");
        }
    }

    public LoadingLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    /* access modifiers changed from: protected */
    public abstract View createLoadingView(Context context, AttributeSet attributeSet);

    public abstract int getContentSize();

    /* access modifiers changed from: protected */
    public ILoadingLayout.State getPreState() {
        return this.mPreState;
    }

    public ILoadingLayout.State getState() {
        return this.mCurState;
    }

    /* access modifiers changed from: protected */
    public void init(Context context, AttributeSet attributeSet) {
    }

    /* access modifiers changed from: protected */
    public void onNoMoreData() {
    }

    public void onPull(float f) {
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
        int i = AnonymousClass1.$SwitchMap$io$dcloud$common$adapter$ui$fresh$ILoadingLayout$State[state.ordinal()];
        if (i == 1) {
            onReset();
        } else if (i == 2) {
            onReleaseToRefresh();
        } else if (i == 3) {
            onPullToRefresh();
        } else if (i == 4) {
            onRefreshing();
        } else if (i == 5) {
            onNoMoreData();
        }
    }

    public void setLastUpdatedLabel(CharSequence charSequence) {
    }

    public void setLoadingDrawable(Drawable drawable) {
    }

    public void setPullLabel(CharSequence charSequence) {
    }

    public void setRefreshingLabel(CharSequence charSequence) {
    }

    public void setReleaseLabel(CharSequence charSequence) {
    }

    public void setState(ILoadingLayout.State state) {
        ILoadingLayout.State state2 = this.mCurState;
        if (state2 != state) {
            this.mPreState = state2;
            this.mCurState = state;
            onStateChanged(state, state2);
        }
    }

    public void show(boolean z) {
        ViewGroup.LayoutParams layoutParams;
        int i = 0;
        if (z != (getVisibility() == 0) && (layoutParams = this.mContainer.getLayoutParams()) != null) {
            if (z) {
                layoutParams.height = -2;
            } else {
                layoutParams.height = 0;
            }
            if (!z) {
                i = 4;
            }
            setVisibility(i);
        }
    }

    public LoadingLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LoadingLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        ILoadingLayout.State state = ILoadingLayout.State.NONE;
        this.mCurState = state;
        this.mPreState = state;
        init(context, attributeSet);
    }
}
