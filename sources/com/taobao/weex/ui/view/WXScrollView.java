package com.taobao.weex.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.component.WXBaseScroller;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WXScrollView extends ScrollView implements Handler.Callback, IWXScroller, WXGestureObservable, NestedScrollingChild {
    private NestedScrollingChildHelper childHelper;
    private int[] consumed;
    private int mCheckTime;
    private View mCurrentStickyView;
    private boolean mHasNotDoneActionDown;
    private int mInitialPosition;
    private boolean mRedirectTouchToStickyView;
    private Rect mScrollRect;
    private List<WXScrollViewListener> mScrollViewListeners;
    int mScrollX;
    int mScrollY;
    private Handler mScrollerTask;
    private int mStickyOffset;
    private int[] mStickyP;
    private WXBaseScroller mWAScroller;
    private int[] offsetInWindow;
    private float ox;
    private float oy;
    private boolean scrollable;
    private int[] stickyScrollerP;
    private int[] stickyViewP;
    private WXGesture wxGesture;

    public interface WXScrollViewListener {
        void onScroll(WXScrollView wXScrollView, int i, int i2);

        void onScrollChanged(WXScrollView wXScrollView, int i, int i2, int i3, int i4);

        void onScrollStopped(WXScrollView wXScrollView, int i, int i2);

        void onScrollToBottom(WXScrollView wXScrollView, int i, int i2);
    }

    public WXScrollView(Context context) {
        super(context);
        this.consumed = new int[2];
        this.offsetInWindow = new int[2];
        this.mHasNotDoneActionDown = true;
        this.mCheckTime = 100;
        this.mStickyP = new int[2];
        this.stickyScrollerP = new int[2];
        this.stickyViewP = new int[2];
        this.scrollable = true;
        this.mScrollViewListeners = new ArrayList();
        init();
        try {
            WXReflectionUtils.setValue(this, "mMinimumVelocity", 5);
        } catch (Exception e) {
            WXLogUtils.e("[WXScrollView] WXScrollView: ", (Throwable) e);
        }
    }

    private void init() {
        setWillNotDraw(false);
        startScrollerTask();
        setOverScrollMode(2);
        NestedScrollingChildHelper nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        this.childHelper = nestedScrollingChildHelper;
        nestedScrollingChildHelper.setNestedScrollingEnabled(true);
    }

    public void startScrollerTask() {
        if (this.mScrollerTask == null) {
            this.mScrollerTask = new Handler(WXThread.secure((Handler.Callback) this));
        }
        this.mInitialPosition = getScrollY();
        this.mScrollerTask.sendEmptyMessageDelayed(0, (long) this.mCheckTime);
    }

    public WXScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.consumed = new int[2];
        this.offsetInWindow = new int[2];
        this.mHasNotDoneActionDown = true;
        this.mCheckTime = 100;
        this.mStickyP = new int[2];
        this.stickyScrollerP = new int[2];
        this.stickyViewP = new int[2];
        this.scrollable = true;
        init();
    }

    public WXScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.consumed = new int[2];
        this.offsetInWindow = new int[2];
        this.mHasNotDoneActionDown = true;
        this.mCheckTime = 100;
        this.mStickyP = new int[2];
        this.stickyScrollerP = new int[2];
        this.stickyViewP = new int[2];
        this.scrollable = true;
        setOverScrollMode(2);
    }

    public void addScrollViewListener(WXScrollViewListener wXScrollViewListener) {
        if (!this.mScrollViewListeners.contains(wXScrollViewListener)) {
            this.mScrollViewListeners.add(wXScrollViewListener);
        }
    }

    public void removeScrollViewListener(WXScrollViewListener wXScrollViewListener) {
        this.mScrollViewListeners.remove(wXScrollViewListener);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mRedirectTouchToStickyView = true;
        }
        if (this.mRedirectTouchToStickyView) {
            boolean z = false;
            boolean z2 = this.mCurrentStickyView != null;
            this.mRedirectTouchToStickyView = z2;
            if (z2) {
                if (motionEvent.getY() <= ((float) this.mCurrentStickyView.getHeight()) && motionEvent.getX() >= ((float) this.mCurrentStickyView.getLeft()) && motionEvent.getX() <= ((float) this.mCurrentStickyView.getRight())) {
                    z = true;
                }
                this.mRedirectTouchToStickyView = z;
            }
        }
        if (this.mRedirectTouchToStickyView) {
            if (this.mScrollRect == null) {
                Rect rect = new Rect();
                this.mScrollRect = rect;
                getGlobalVisibleRect(rect);
            }
            this.mCurrentStickyView.getLocationOnScreen(this.stickyViewP);
            motionEvent.offsetLocation(0.0f, (float) (this.stickyViewP[1] - this.mScrollRect.top));
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        WXGesture wXGesture = this.wxGesture;
        return wXGesture != null ? dispatchTouchEvent | wXGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mCurrentStickyView != null) {
            canvas.save();
            this.mCurrentStickyView.getLocationOnScreen(this.mStickyP);
            int i = this.mStickyOffset;
            if (i > 0) {
                i = 0;
            }
            canvas.translate((float) this.mStickyP[0], (float) (getScrollY() + i));
            canvas.clipRect(0, i, this.mCurrentStickyView.getWidth(), this.mCurrentStickyView.getHeight());
            this.mCurrentStickyView.draw(canvas);
            canvas.restore();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.scrollable) {
            return true;
        }
        if (this.mRedirectTouchToStickyView) {
            if (this.mScrollRect == null) {
                Rect rect = new Rect();
                this.mScrollRect = rect;
                getGlobalVisibleRect(rect);
            }
            this.mCurrentStickyView.getLocationOnScreen(this.stickyViewP);
            motionEvent.offsetLocation(0.0f, (float) (-(this.stickyViewP[1] - this.mScrollRect.top)));
        }
        if (motionEvent.getAction() == 0) {
            this.mHasNotDoneActionDown = false;
        }
        if (this.mHasNotDoneActionDown) {
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setAction(0);
            this.mHasNotDoneActionDown = false;
            obtain.recycle();
        }
        if (motionEvent.getAction() == 0) {
            this.ox = motionEvent.getX();
            this.oy = motionEvent.getY();
            startNestedScroll(3);
        }
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mHasNotDoneActionDown = true;
            stopNestedScroll();
        }
        if (motionEvent.getAction() == 2) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (dispatchNestedPreScroll((int) (this.ox - x), (int) (this.oy - y), this.consumed, this.offsetInWindow)) {
                int[] iArr = this.consumed;
                motionEvent.setLocation(x + ((float) iArr[0]), y + ((float) iArr[1]));
            }
            this.ox = motionEvent.getX();
            this.oy = motionEvent.getY();
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.childHelper.setNestedScrollingEnabled(z);
    }

    public boolean isNestedScrollingEnabled() {
        return this.childHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int i) {
        return this.childHelper.startNestedScroll(i);
    }

    public void stopNestedScroll() {
        this.childHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return this.childHelper.hasNestedScrollingParent();
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(boolean z) {
        this.scrollable = z;
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.childHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.childHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.childHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.childHelper.dispatchNestedPreFling(f, f2);
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        return dispatchNestedPreFling(f, f2);
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        return dispatchNestedFling(f, f2, z);
    }

    public void fling(int i) {
        super.fling(i);
        Handler handler = this.mScrollerTask;
        if (handler != null) {
            handler.removeMessages(0);
        }
        startScrollerTask();
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        this.mScrollX = getScrollX();
        int scrollY = getScrollY();
        this.mScrollY = scrollY;
        onScroll(this, this.mScrollX, scrollY);
        View childAt = getChildAt(getChildCount() - 1);
        if (childAt != null) {
            int bottom = childAt.getBottom();
            int height = getHeight();
            int i5 = this.mScrollY;
            if (bottom - (height + i5) == 0) {
                onScrollToBottom(this.mScrollX, i5);
            }
            List<WXScrollViewListener> list = this.mScrollViewListeners;
            int size = list == null ? 0 : list.size();
            for (int i6 = 0; i6 < size; i6++) {
                this.mScrollViewListeners.get(i6).onScrollChanged(this, i, i2, i3, i4);
            }
            showStickyView();
        }
    }

    /* access modifiers changed from: protected */
    public void onScroll(WXScrollView wXScrollView, int i, int i2) {
        List<WXScrollViewListener> list = this.mScrollViewListeners;
        int size = list == null ? 0 : list.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mScrollViewListeners.get(i3).onScroll(this, i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollToBottom(int i, int i2) {
        List<WXScrollViewListener> list = this.mScrollViewListeners;
        int size = list == null ? 0 : list.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mScrollViewListeners.get(i3).onScrollToBottom(this, i, i2);
        }
    }

    private void showStickyView() {
        WXBaseScroller wXBaseScroller = this.mWAScroller;
        if (wXBaseScroller != null) {
            View procSticky = procSticky(wXBaseScroller.getStickMap());
            if (procSticky != null) {
                this.mCurrentStickyView = procSticky;
            } else {
                this.mCurrentStickyView = null;
            }
        }
    }

    private View procSticky(Map<String, Map<String, WXComponent>> map) {
        Map map2;
        if (map == null || (map2 = map.get(this.mWAScroller.getRef())) == null) {
            return null;
        }
        for (Map.Entry value : map2.entrySet()) {
            WXComponent wXComponent = (WXComponent) value.getValue();
            getLocationOnScreen(this.stickyScrollerP);
            wXComponent.getHostView().getLocationOnScreen(this.stickyViewP);
            int height = (wXComponent.getParent() == null || wXComponent.getParent().getRealView() == null) ? 0 : wXComponent.getParent().getRealView().getHeight();
            int height2 = wXComponent.getHostView().getHeight();
            int[] iArr = this.stickyScrollerP;
            int i = iArr[1];
            int i2 = (-height) + iArr[1] + height2;
            int[] iArr2 = this.stickyViewP;
            if (iArr2[1] > i || iArr2[1] < i2 - height2) {
                wXComponent.setStickyOffset(0);
            } else {
                this.mStickyOffset = iArr2[1] - i2;
                wXComponent.setStickyOffset(iArr2[1] - iArr[1]);
                return wXComponent.getHostView();
            }
        }
        return null;
    }

    public boolean handleMessage(Message message) {
        if (message.what != 0) {
            return true;
        }
        Handler handler = this.mScrollerTask;
        if (handler != null) {
            handler.removeMessages(0);
        }
        if (this.mInitialPosition - getScrollY() == 0) {
            onScrollStopped(this, getScrollX(), getScrollY());
            return true;
        }
        onScroll(this, getScrollX(), getScrollY());
        this.mInitialPosition = getScrollY();
        Handler handler2 = this.mScrollerTask;
        if (handler2 == null) {
            return true;
        }
        handler2.sendEmptyMessageDelayed(0, (long) this.mCheckTime);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onScrollStopped(WXScrollView wXScrollView, int i, int i2) {
        List<WXScrollViewListener> list = this.mScrollViewListeners;
        int size = list == null ? 0 : list.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mScrollViewListeners.get(i3).onScrollStopped(this, i, i2);
        }
    }

    public void destroy() {
        Handler handler = this.mScrollerTask;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public Rect getContentFrame() {
        return new Rect(0, 0, computeHorizontalScrollRange(), computeVerticalScrollRange());
    }

    public void setWAScroller(WXBaseScroller wXBaseScroller) {
        this.mWAScroller = wXBaseScroller;
    }
}
