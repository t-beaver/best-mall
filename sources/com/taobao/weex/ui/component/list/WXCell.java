package com.taobao.weex.ui.component.list;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXHeader;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.flat.WidgetContainer;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

@Component(lazyload = false)
public class WXCell extends WidgetContainer<WXFrameLayout> {
    private CellAppendTreeListener cellAppendTreeListener;
    private boolean isAppendTreeDone;
    private boolean isSourceUsed = false;
    private boolean mFlatUIEnabled = false;
    private View mHeadView;
    private int mLastLocationY = 0;
    private ViewGroup mRealView;
    private int mScrollPosition = -1;
    private View mTempStickyView;
    private Object renderData;

    public interface CellAppendTreeListener {
        void onAppendTreeDone();
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXCell(wXSDKInstance, wXVContainer, true, basicComponentData);
        }
    }

    @Deprecated
    public WXCell(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXCell(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        lazy(true);
        setContentBoxMeasurement(new ContentBoxMeasurement() {
            public void layoutAfter(float f, float f2) {
            }

            public void layoutBefore() {
            }

            public void measureInternal(float f, float f2, int i, int i2) {
                if (i2 == 0) {
                    this.mMeasureHeight = 1.0f;
                }
            }
        });
        if (Build.VERSION.SDK_INT < 21) {
            try {
                WXAttr attrs = getAttrs();
                if (attrs.containsKey(Constants.Name.FLAT)) {
                    this.mFlatUIEnabled = WXUtils.getBoolean(attrs.get(Constants.Name.FLAT), false).booleanValue();
                }
            } catch (NullPointerException e) {
                WXLogUtils.e("Cell", WXLogUtils.getStackTrace(e));
            }
        }
        if (!canRecycled()) {
            wXSDKInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_CELL_DATA_UN_RECYCLE_NUM, 1.0d);
        }
        if (TextUtils.isEmpty(getAttrs().getScope())) {
            wXSDKInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_CELL_UN_RE_USE_NUM, 1.0d);
        }
    }

    public boolean isLazy() {
        return super.isLazy() && !isFixed();
    }

    public boolean isFlatUIEnabled() {
        return this.mFlatUIEnabled;
    }

    /* access modifiers changed from: protected */
    public WXFrameLayout initComponentHostView(Context context) {
        if (isSticky() || (this instanceof WXHeader)) {
            WXFrameLayout wXFrameLayout = new WXFrameLayout(context);
            WXFrameLayout wXFrameLayout2 = new WXFrameLayout(context);
            this.mRealView = wXFrameLayout2;
            wXFrameLayout.addView(wXFrameLayout2);
            if (isFlatUIEnabled()) {
                wXFrameLayout.setLayerType(2, (Paint) null);
            }
            return wXFrameLayout;
        }
        WXFrameLayout wXFrameLayout3 = new WXFrameLayout(context);
        this.mRealView = wXFrameLayout3;
        if (isFlatUIEnabled()) {
            wXFrameLayout3.setLayerType(2, (Paint) null);
        }
        return wXFrameLayout3;
    }

    public int getLocationFromStart() {
        return this.mLastLocationY;
    }

    public void setLocationFromStart(int i) {
        this.mLastLocationY = i;
    }

    /* access modifiers changed from: package-private */
    public void setScrollPositon(int i) {
        this.mScrollPosition = i;
    }

    public int getScrollPositon() {
        return this.mScrollPosition;
    }

    public ViewGroup getRealView() {
        return this.mRealView;
    }

    public void removeSticky() {
        if (((WXFrameLayout) getHostView()).getChildCount() > 0) {
            this.mHeadView = ((WXFrameLayout) getHostView()).getChildAt(0);
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            ((WXFrameLayout) getHostView()).getLocationOnScreen(iArr);
            getParentScroller().getView().getLocationOnScreen(iArr2);
            int i = iArr[0] - iArr2[0];
            int top = getParent().getHostView().getTop();
            ((WXFrameLayout) getHostView()).removeView(this.mHeadView);
            this.mRealView = (ViewGroup) this.mHeadView;
            this.mTempStickyView = new FrameLayout(getContext());
            ((WXFrameLayout) getHostView()).addView(this.mTempStickyView, new FrameLayout.LayoutParams((int) getLayoutWidth(), (int) getLayoutHeight()));
            this.mHeadView.setTranslationX((float) i);
            this.mHeadView.setTranslationY((float) top);
        }
    }

    public void recoverySticky() {
        View view = this.mHeadView;
        if (view != null) {
            if (view.getLayoutParams() != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mHeadView.getLayoutParams();
                if (marginLayoutParams.topMargin > 0) {
                    marginLayoutParams.topMargin = 0;
                }
            }
            if (this.mHeadView.getVisibility() != 0) {
                this.mHeadView.setVisibility(0);
            }
            if (this.mHeadView.getParent() != null) {
                ((ViewGroup) this.mHeadView.getParent()).removeView(this.mHeadView);
            }
            ((WXFrameLayout) getHostView()).removeView(this.mTempStickyView);
            ((WXFrameLayout) getHostView()).addView(this.mHeadView);
            this.mHeadView.setTranslationX(0.0f);
            this.mHeadView.setTranslationY(0.0f);
        }
    }

    /* access modifiers changed from: protected */
    public void mountFlatGUI() {
        if (getHostView() != null) {
            if (this.widgets == null) {
                this.widgets = new LinkedList();
            }
            ((WXFrameLayout) getHostView()).mountFlatGUI(this.widgets);
        }
    }

    public void unmountFlatGUI() {
        if (getHostView() != null) {
            ((WXFrameLayout) getHostView()).unmountFlatGUI();
        }
    }

    public boolean intendToBeFlatContainer() {
        return getInstance().getFlatUIContext().isFlatUIEnabled(this) && WXCell.class.equals(getClass()) && !isSticky();
    }

    public int getStickyOffset() {
        if (getAttrs().get(Constants.Name.STICKY_OFFSET) == null) {
            return 0;
        }
        return (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(getAttrs().get(Constants.Name.STICKY_OFFSET)), getViewPortWidthForFloat());
    }

    public Object getRenderData() {
        return this.renderData;
    }

    public void setRenderData(Object obj) {
        this.renderData = obj;
    }

    public boolean isSourceUsed() {
        return this.isSourceUsed;
    }

    public void setSourceUsed(boolean z) {
        this.isSourceUsed = z;
    }

    public boolean isAppendTreeDone() {
        return this.isAppendTreeDone;
    }

    public void appendTreeCreateFinish() {
        super.appendTreeCreateFinish();
        this.isAppendTreeDone = true;
        CellAppendTreeListener cellAppendTreeListener2 = this.cellAppendTreeListener;
        if (cellAppendTreeListener2 != null) {
            cellAppendTreeListener2.onAppendTreeDone();
        }
    }

    public void setCellAppendTreeListener(CellAppendTreeListener cellAppendTreeListener2) {
        this.cellAppendTreeListener = cellAppendTreeListener2;
        if (this.isAppendTreeDone) {
            cellAppendTreeListener2.onAppendTreeDone();
        }
    }

    public void createViewImpl() {
        if (getRealView() == null || getRealView().getParent() == null) {
            super.createViewImpl();
        }
    }
}
