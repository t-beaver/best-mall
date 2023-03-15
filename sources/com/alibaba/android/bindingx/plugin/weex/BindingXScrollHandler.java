package com.alibaba.android.bindingx.plugin.weex;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.bindingx.core.BindingXCore;
import com.alibaba.android.bindingx.core.LogProxy;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.AbstractScrollEventHandler;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import com.google.android.material.appbar.AppBarLayout;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.ui.view.WXScrollView;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingXScrollHandler extends AbstractScrollEventHandler {
    /* access modifiers changed from: private */
    public static HashMap<String, ContentOffsetHolder> sOffsetHolderMap = new HashMap<>();
    private WXHorizontalScrollView.ScrollViewListener mHorizontalViewScrollListener;
    private RecyclerView.OnScrollListener mListOnScrollListener;
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private WXSwipeLayout.OnRefreshOffsetChangedListener mRefreshOffsetChangedListener;
    /* access modifiers changed from: private */
    public String mSourceRef;
    private WXScrollView.WXScrollViewListener mWxScrollViewListener;

    /* access modifiers changed from: private */
    public boolean isSameDirection(int i, int i2) {
        return (i > 0 && i2 > 0) || (i < 0 && i2 < 0);
    }

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public void onStart(String str, String str2) {
    }

    public BindingXScrollHandler(Context context, PlatformManager platformManager, Object... objArr) {
        super(context, platformManager, objArr);
    }

    public boolean onCreate(String str, String str2) {
        WXSwipeLayout swipeLayout;
        WXComponent findComponentByRef = WXModuleUtils.findComponentByRef(TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId, str);
        if (findComponentByRef == null) {
            LogProxy.e("[ExpressionScrollHandler]source component not found.");
            return false;
        }
        this.mSourceRef = str;
        if (findComponentByRef instanceof WXScroller) {
            WXScroller wXScroller = (WXScroller) findComponentByRef;
            ViewGroup viewGroup = (ViewGroup) wXScroller.getHostView();
            if (!(viewGroup == null || !(viewGroup instanceof BounceScrollerView) || (swipeLayout = ((BounceScrollerView) viewGroup).getSwipeLayout()) == null)) {
                InnerSwipeOffsetListener innerSwipeOffsetListener = new InnerSwipeOffsetListener();
                this.mRefreshOffsetChangedListener = innerSwipeOffsetListener;
                swipeLayout.addOnRefreshOffsetChangedListener(innerSwipeOffsetListener);
            }
            ViewGroup innerView = wXScroller.getInnerView();
            if (innerView != null && (innerView instanceof WXScrollView)) {
                InnerScrollViewListener innerScrollViewListener = new InnerScrollViewListener();
                this.mWxScrollViewListener = innerScrollViewListener;
                ((WXScrollView) innerView).addScrollViewListener(innerScrollViewListener);
                return true;
            } else if (innerView != null && (innerView instanceof WXHorizontalScrollView)) {
                InnerScrollViewListener innerScrollViewListener2 = new InnerScrollViewListener();
                this.mHorizontalViewScrollListener = innerScrollViewListener2;
                ((WXHorizontalScrollView) innerView).addScrollViewListener(innerScrollViewListener2);
                return true;
            }
        } else if (findComponentByRef instanceof WXListComponent) {
            WXListComponent wXListComponent = (WXListComponent) findComponentByRef;
            BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) wXListComponent.getHostView();
            if (bounceRecyclerView != null) {
                WXSwipeLayout swipeLayout2 = bounceRecyclerView.getSwipeLayout();
                if (swipeLayout2 != null) {
                    InnerSwipeOffsetListener innerSwipeOffsetListener2 = new InnerSwipeOffsetListener();
                    this.mRefreshOffsetChangedListener = innerSwipeOffsetListener2;
                    swipeLayout2.addOnRefreshOffsetChangedListener(innerSwipeOffsetListener2);
                }
                WXRecyclerView wXRecyclerView = (WXRecyclerView) bounceRecyclerView.getInnerView();
                boolean z = wXListComponent.getOrientation() == 1;
                if (wXRecyclerView != null) {
                    HashMap<String, ContentOffsetHolder> hashMap = sOffsetHolderMap;
                    if (hashMap != null && hashMap.get(str) == null) {
                        sOffsetHolderMap.put(str, new ContentOffsetHolder(0, 0));
                    }
                    InnerListScrollListener innerListScrollListener = new InnerListScrollListener(z, new WeakReference(wXListComponent));
                    this.mListOnScrollListener = innerListScrollListener;
                    wXRecyclerView.addOnScrollListener(innerListScrollListener);
                    return true;
                }
            }
        } else if (findComponentByRef.getHostView() != null && (findComponentByRef.getHostView() instanceof AppBarLayout)) {
            InnerAppBarOffsetChangedListener innerAppBarOffsetChangedListener = new InnerAppBarOffsetChangedListener();
            this.mOnOffsetChangedListener = innerAppBarOffsetChangedListener;
            findComponentByRef.getHostView().addOnOffsetChangedListener(innerAppBarOffsetChangedListener);
            return true;
        }
        return false;
    }

    public void onBindExpression(String str, Map<String, Object> map, ExpressionPair expressionPair, List<Map<String, Object>> list, BindingXCore.JavaScriptCallback javaScriptCallback) {
        super.onBindExpression(str, map, expressionPair, list, javaScriptCallback);
    }

    public boolean onDisable(String str, String str2) {
        BounceRecyclerView bounceRecyclerView;
        RecyclerView.OnScrollListener onScrollListener;
        WXHorizontalScrollView.ScrollViewListener scrollViewListener;
        WXScrollView.WXScrollViewListener wXScrollViewListener;
        WXSwipeLayout swipeLayout;
        WXSwipeLayout.OnRefreshOffsetChangedListener onRefreshOffsetChangedListener;
        ContentOffsetHolder contentOffsetHolder;
        super.onDisable(str, str2);
        if (!(sOffsetHolderMap == null || TextUtils.isEmpty(this.mSourceRef) || (contentOffsetHolder = sOffsetHolderMap.get(this.mSourceRef)) == null)) {
            contentOffsetHolder.x = this.mContentOffsetX;
            contentOffsetHolder.y = this.mContentOffsetY;
        }
        WXComponent findComponentByRef = WXModuleUtils.findComponentByRef(TextUtils.isEmpty(this.mAnchorInstanceId) ? this.mInstanceId : this.mAnchorInstanceId, str);
        if (findComponentByRef == null) {
            LogProxy.e("[ExpressionScrollHandler]source component not found.");
            return false;
        }
        if (findComponentByRef instanceof WXScroller) {
            WXScroller wXScroller = (WXScroller) findComponentByRef;
            ViewGroup viewGroup = (ViewGroup) wXScroller.getHostView();
            if (!(viewGroup == null || !(viewGroup instanceof BounceScrollerView) || (swipeLayout = ((BounceScrollerView) viewGroup).getSwipeLayout()) == null || (onRefreshOffsetChangedListener = this.mRefreshOffsetChangedListener) == null)) {
                swipeLayout.removeOnRefreshOffsetChangedListener(onRefreshOffsetChangedListener);
            }
            ViewGroup innerView = wXScroller.getInnerView();
            if (innerView != null && (innerView instanceof WXScrollView) && (wXScrollViewListener = this.mWxScrollViewListener) != null) {
                ((WXScrollView) innerView).removeScrollViewListener(wXScrollViewListener);
                return true;
            } else if (!(innerView == null || !(innerView instanceof WXHorizontalScrollView) || (scrollViewListener = this.mHorizontalViewScrollListener) == null)) {
                ((WXHorizontalScrollView) innerView).removeScrollViewListener(scrollViewListener);
                return true;
            }
        } else if ((findComponentByRef instanceof WXListComponent) && (bounceRecyclerView = (BounceRecyclerView) ((WXListComponent) findComponentByRef).getHostView()) != null) {
            if (!(bounceRecyclerView.getSwipeLayout() == null || this.mRefreshOffsetChangedListener == null)) {
                bounceRecyclerView.getSwipeLayout().removeOnRefreshOffsetChangedListener(this.mRefreshOffsetChangedListener);
            }
            WXRecyclerView wXRecyclerView = (WXRecyclerView) bounceRecyclerView.getInnerView();
            if (!(wXRecyclerView == null || (onScrollListener = this.mListOnScrollListener) == null)) {
                wXRecyclerView.removeOnScrollListener(onScrollListener);
                return true;
            }
        }
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mListOnScrollListener = null;
        this.mWxScrollViewListener = null;
        this.mOnOffsetChangedListener = null;
        HashMap<String, ContentOffsetHolder> hashMap = sOffsetHolderMap;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    private class InnerAppBarOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        /* access modifiers changed from: private */
        public int mContentOffsetY;
        private int mLastDy;
        private int mTy;

        private InnerAppBarOffsetChangedListener() {
            this.mContentOffsetY = 0;
            this.mTy = 0;
            this.mLastDy = 0;
        }

        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            boolean z;
            int i2 = -i;
            final int i3 = i2 - this.mContentOffsetY;
            this.mContentOffsetY = i2;
            if (i3 != 0) {
                if (!BindingXScrollHandler.this.isSameDirection(i3, this.mLastDy)) {
                    this.mTy = this.mContentOffsetY;
                    z = true;
                } else {
                    z = false;
                }
                int i4 = this.mContentOffsetY;
                final int i5 = i4 - this.mTy;
                this.mLastDy = i3;
                if (z) {
                    BindingXScrollHandler.super.fireEventByState(BindingXConstants.STATE_TURNING, 0.0d, (double) i4, 0.0d, (double) i3, 0.0d, (double) i5, new Object[0]);
                }
                WXBridgeManager.getInstance().post(new Runnable() {
                    public void run() {
                        InnerAppBarOffsetChangedListener.super.handleScrollEvent(0, InnerAppBarOffsetChangedListener.this.mContentOffsetY, 0, i3, 0, i5);
                    }
                }, BindingXScrollHandler.this.mInstanceId);
            }
        }
    }

    private class InnerScrollViewListener implements WXScrollView.WXScrollViewListener, WXHorizontalScrollView.ScrollViewListener {
        /* access modifiers changed from: private */
        public int mContentOffsetX;
        /* access modifiers changed from: private */
        public int mContentOffsetY;
        private int mLastDx;
        private int mLastDy;
        private int mTx;
        private int mTy;

        public void onScrollChanged(WXScrollView wXScrollView, int i, int i2, int i3, int i4) {
        }

        public void onScrollStopped(WXScrollView wXScrollView, int i, int i2) {
        }

        public void onScrollToBottom(WXScrollView wXScrollView, int i, int i2) {
        }

        private InnerScrollViewListener() {
            this.mContentOffsetX = 0;
            this.mContentOffsetY = 0;
            this.mTx = 0;
            this.mTy = 0;
            this.mLastDx = 0;
            this.mLastDy = 0;
        }

        public void onScroll(WXScrollView wXScrollView, int i, int i2) {
            onScrollInternal(i, i2);
        }

        public void onScrollChanged(WXHorizontalScrollView wXHorizontalScrollView, int i, int i2, int i3, int i4) {
            onScrollInternal(i, i2);
        }

        private void onScrollInternal(int i, int i2) {
            boolean z;
            int i3;
            int i4;
            int i5 = i;
            int i6 = i2;
            int i7 = i5 - this.mContentOffsetX;
            int i8 = i6 - this.mContentOffsetY;
            this.mContentOffsetX = i5;
            this.mContentOffsetY = i6;
            if (i7 != 0 || i8 != 0) {
                if (!BindingXScrollHandler.this.isSameDirection(i8, this.mLastDy)) {
                    this.mTy = this.mContentOffsetY;
                    z = true;
                } else {
                    z = false;
                }
                int i9 = this.mContentOffsetX;
                int i10 = i9 - this.mTx;
                int i11 = this.mContentOffsetY;
                int i12 = i11 - this.mTy;
                this.mLastDx = i7;
                this.mLastDy = i8;
                if (z) {
                    i4 = i8;
                    i3 = i7;
                    BindingXScrollHandler.super.fireEventByState(BindingXConstants.STATE_TURNING, (double) i9, (double) i11, (double) i7, (double) i8, (double) i10, (double) i12, new Object[0]);
                } else {
                    i3 = i7;
                    i4 = i8;
                }
                final int i13 = i3;
                final int i14 = i4;
                final int i15 = i10;
                final int i16 = i12;
                WXBridgeManager.getInstance().post(new Runnable() {
                    public void run() {
                        InnerScrollViewListener.super.handleScrollEvent(InnerScrollViewListener.this.mContentOffsetX, InnerScrollViewListener.this.mContentOffsetY, i13, i14, i15, i16);
                    }
                }, BindingXScrollHandler.this.mInstanceId);
            }
        }
    }

    private class InnerSwipeOffsetListener implements WXSwipeLayout.OnRefreshOffsetChangedListener {
        /* access modifiers changed from: private */
        public int mContentOffsetY;
        private int mLastDy;
        private int mTy;

        private InnerSwipeOffsetListener() {
            this.mContentOffsetY = 0;
            this.mTy = 0;
            this.mLastDy = 0;
        }

        public void onOffsetChanged(int i) {
            boolean z;
            int i2 = -i;
            final int i3 = i2 - this.mContentOffsetY;
            this.mContentOffsetY = i2;
            if (i3 != 0) {
                if (!BindingXScrollHandler.this.isSameDirection(i3, this.mLastDy)) {
                    this.mTy = this.mContentOffsetY;
                    z = true;
                } else {
                    z = false;
                }
                final int i4 = this.mContentOffsetY - this.mTy;
                this.mLastDy = i3;
                if (z) {
                    BindingXScrollHandler bindingXScrollHandler = BindingXScrollHandler.this;
                    BindingXScrollHandler.super.fireEventByState(BindingXConstants.STATE_TURNING, (double) bindingXScrollHandler.mContentOffsetX, (double) this.mContentOffsetY, 0.0d, (double) i3, 0.0d, (double) i4, new Object[0]);
                }
                WXBridgeManager.getInstance().post(new Runnable() {
                    public void run() {
                        InnerSwipeOffsetListener.super.handleScrollEvent(BindingXScrollHandler.this.mContentOffsetX, InnerSwipeOffsetListener.this.mContentOffsetY, 0, i3, 0, i4);
                    }
                }, BindingXScrollHandler.this.mInstanceId);
            }
        }
    }

    private class InnerListScrollListener extends RecyclerView.OnScrollListener {
        private boolean isVertical;
        private WeakReference<WXListComponent> mComponentRef;
        /* access modifiers changed from: private */
        public int mContentOffsetX = 0;
        /* access modifiers changed from: private */
        public int mContentOffsetY = 0;
        private int mLastDx = 0;
        private int mLastDy = 0;
        private int mTx = 0;
        private int mTy = 0;

        InnerListScrollListener(boolean z, WeakReference<WXListComponent> weakReference) {
            ContentOffsetHolder contentOffsetHolder;
            this.isVertical = z;
            this.mComponentRef = weakReference;
            if (!TextUtils.isEmpty(BindingXScrollHandler.this.mSourceRef) && BindingXScrollHandler.sOffsetHolderMap != null && (contentOffsetHolder = (ContentOffsetHolder) BindingXScrollHandler.sOffsetHolderMap.get(BindingXScrollHandler.this.mSourceRef)) != null) {
                this.mContentOffsetX = contentOffsetHolder.x;
                this.mContentOffsetY = contentOffsetHolder.y;
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            boolean z;
            WeakReference<WXListComponent> weakReference;
            int i3 = i;
            int i4 = i2;
            if (!ViewCompat.isInLayout(recyclerView) || (weakReference = this.mComponentRef) == null || weakReference.get() == null) {
                this.mContentOffsetY += i4;
            } else {
                this.mContentOffsetY = Math.abs(((WXListComponent) this.mComponentRef.get()).calcContentOffset(recyclerView));
            }
            this.mContentOffsetX += i3;
            boolean z2 = true;
            if (BindingXScrollHandler.this.isSameDirection(i3, this.mLastDx) || this.isVertical) {
                z = false;
            } else {
                this.mTx = this.mContentOffsetX;
                z = true;
            }
            if (BindingXScrollHandler.this.isSameDirection(i4, this.mLastDy) || !this.isVertical) {
                z2 = z;
            } else {
                this.mTy = this.mContentOffsetY;
            }
            int i5 = this.mContentOffsetX;
            int i6 = i5 - this.mTx;
            int i7 = this.mContentOffsetY;
            int i8 = i7 - this.mTy;
            this.mLastDx = i3;
            this.mLastDy = i4;
            if (z2) {
                BindingXScrollHandler.this.fireEventByState(BindingXConstants.STATE_TURNING, (double) i5, (double) i7, (double) i3, (double) i4, (double) i6, (double) i8, new Object[0]);
            }
            final int i9 = i;
            final int i10 = i2;
            final int i11 = i6;
            final int i12 = i8;
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    InnerListScrollListener.super.handleScrollEvent(InnerListScrollListener.this.mContentOffsetX, InnerListScrollListener.this.mContentOffsetY, i9, i10, i11, i12);
                }
            }, BindingXScrollHandler.this.mInstanceId);
        }
    }

    private static class ContentOffsetHolder {
        int x;
        int y;

        ContentOffsetHolder(int i, int i2) {
            this.x = i;
            this.y = i2;
        }
    }
}
