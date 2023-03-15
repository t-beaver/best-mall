package com.taobao.weex.ui.view.listview;

import android.content.Context;
import android.graphics.PointF;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class ExtendedLinearLayoutManager extends LinearLayoutManager {
    /* access modifiers changed from: private */
    public OnSmoothScrollEndListener onScrollEndListener;
    private RecyclerView.SmoothScroller smoothScroller;

    public interface OnSmoothScrollEndListener {
        void onStop();
    }

    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public ExtendedLinearLayoutManager(Context context) {
        super(context, 1, false);
    }

    public ExtendedLinearLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollVerticallyBy(i, recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        if (this.smoothScroller == null) {
            this.smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
        }
        this.smoothScroller.setTargetPosition(i);
        startSmoothScroll(this.smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
        /* access modifiers changed from: protected */
        public int getVerticalSnapPreference() {
            return -1;
        }

        public TopSnappedSmoothScroller(Context context) {
            super(context);
        }

        public PointF computeScrollVectorForPosition(int i) {
            return ExtendedLinearLayoutManager.this.computeScrollVectorForPosition(i);
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            super.onStop();
            if (ExtendedLinearLayoutManager.this.onScrollEndListener != null) {
                ExtendedLinearLayoutManager.this.onScrollEndListener.onStop();
                OnSmoothScrollEndListener unused = ExtendedLinearLayoutManager.this.onScrollEndListener = null;
            }
        }
    }

    public void setOnScrollEndListener(OnSmoothScrollEndListener onSmoothScrollEndListener) {
        this.onScrollEndListener = onSmoothScrollEndListener;
    }
}
