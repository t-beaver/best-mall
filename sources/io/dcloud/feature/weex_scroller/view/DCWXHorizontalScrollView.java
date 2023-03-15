package io.dcloud.feature.weex_scroller.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import com.taobao.weex.ui.component.DCWXScroller;
import com.taobao.weex.ui.view.IWXScroller;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.ui.blur.AppEventForBlurManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DCWXHorizontalScrollView extends HorizontalScrollView implements IWXScroller, WXGestureObservable {
    private boolean isTouch = false;
    private float lowwer = 50.0f;
    private ScrollViewListener mScrollViewListener;
    private List<ScrollViewListener> mScrollViewListeners;
    int mScrollX;
    int mScrollY;
    private boolean scrollable = true;
    private Field scroller = null;
    private boolean shouldBeTriggerLower = true;
    private boolean shouldBeTriggerUpper = true;
    private float upper = 50.0f;
    private WXGesture wxGesture;

    public interface ScrollViewListener {
        void onScrollChanged(DCWXHorizontalScrollView dCWXHorizontalScrollView, int i, int i2, int i3, int i4);

        void onScrollToBottom();

        void onScrolltoTop();
    }

    public DCWXHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setOverScrollMode(2);
        try {
            Field declaredField = HorizontalScrollView.class.getDeclaredField("mScroller");
            this.scroller = declaredField;
            declaredField.setAccessible(true);
        } catch (Exception unused) {
        }
    }

    public DCWXHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        AppEventForBlurManager.onScrollChanged(i, i2);
        this.mScrollX = getScrollX();
        this.mScrollY = getScrollY();
        if (((float) getScrollX()) <= this.upper && !this.shouldBeTriggerUpper) {
            scrollToTop();
            this.shouldBeTriggerUpper = true;
        } else if (((float) getScrollX()) > this.upper) {
            this.shouldBeTriggerUpper = false;
        }
        View childAt = getChildAt(getChildCount() - 1);
        if (childAt != null) {
            float right = (float) (childAt.getRight() - (getWidth() + this.mScrollX));
            float f = this.lowwer;
            if (right <= f && !this.shouldBeTriggerLower) {
                scrollToBottom();
                this.shouldBeTriggerLower = true;
            } else if (right > f) {
                this.shouldBeTriggerLower = false;
            }
            ScrollViewListener scrollViewListener = this.mScrollViewListener;
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChanged(this, i, i2, i3 - i, i4 - i2);
            }
            List<ScrollViewListener> list = this.mScrollViewListeners;
            if (list != null) {
                for (ScrollViewListener onScrollChanged : list) {
                    onScrollChanged.onScrollChanged(this, i, i2, i3 - i, i4 - i2);
                }
            }
        }
    }

    private void scrollToTop() {
        ScrollViewListener scrollViewListener = this.mScrollViewListener;
        if (scrollViewListener != null) {
            scrollViewListener.onScrolltoTop();
        }
        List<ScrollViewListener> list = this.mScrollViewListeners;
        if (list != null) {
            for (ScrollViewListener onScrolltoTop : list) {
                onScrolltoTop.onScrolltoTop();
            }
        }
    }

    public void stopScroll() {
        Field field = this.scroller;
        if (field != null && !this.isTouch) {
            try {
                Method method = field.getType().getMethod("abortAnimation", new Class[0]);
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(this.scroller.get(this), new Object[0]);
                }
            } catch (Exception unused) {
            }
        }
    }

    public void setUpperLength(float f) {
        this.upper = f;
    }

    public void setLowwerLength(float f) {
        this.lowwer = f;
    }

    private void scrollToBottom() {
        ScrollViewListener scrollViewListener = this.mScrollViewListener;
        if (scrollViewListener != null) {
            scrollViewListener.onScrollToBottom();
        }
        List<ScrollViewListener> list = this.mScrollViewListeners;
        if (list != null) {
            for (ScrollViewListener onScrollToBottom : list) {
                onScrollToBottom.onScrollToBottom();
            }
        }
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    public void destroy() {
        List<ScrollViewListener> list = this.mScrollViewListeners;
        if (list != null) {
            list.clear();
        }
    }

    public void addScrollViewListener(ScrollViewListener scrollViewListener) {
        if (this.mScrollViewListeners == null) {
            this.mScrollViewListeners = new ArrayList();
        }
        if (!this.mScrollViewListeners.contains(scrollViewListener)) {
            this.mScrollViewListeners.add(scrollViewListener);
        }
    }

    public void removeScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListeners.remove(scrollViewListener);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        WXGesture wXGesture = this.wxGesture;
        return wXGesture != null ? dispatchTouchEvent | wXGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.isTouch = true;
        } else if (action == 1) {
            this.isTouch = false;
        } else if (action == 3) {
            this.isTouch = false;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(boolean z) {
        this.scrollable = z;
    }

    public Rect getContentFrame() {
        return new Rect(0, 0, computeHorizontalScrollRange(), computeVerticalScrollRange());
    }

    public void setWAScroller(DCWXScroller dCWXScroller) {
        this.upper = WXViewUtils.getRealPxByWidth(50.0f, dCWXScroller.getViewPortWidth());
        this.lowwer = WXViewUtils.getRealPxByWidth(50.0f, dCWXScroller.getViewPortWidth());
    }
}
