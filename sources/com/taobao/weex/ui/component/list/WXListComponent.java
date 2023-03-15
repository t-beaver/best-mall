package com.taobao.weex.ui.component.list;

import android.content.Context;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.IWXObject;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXBaseRefresh;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component(lazyload = false)
public class WXListComponent extends BasicListComponent<BounceRecyclerView> {
    private String TAG;
    private boolean hasSetGapItemDecoration;
    private float mPaddingLeft;
    private float mPaddingRight;
    private Float[] mSpanOffsets;
    private String mSpanOffsetsStr;
    private boolean renderReverse;

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXListComponent(wXSDKInstance, wXVContainer, true, basicComponentData);
        }
    }

    @Deprecated
    public WXListComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXListComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.TAG = "WXListComponent";
        this.hasSetGapItemDecoration = false;
        this.renderReverse = false;
    }

    /* access modifiers changed from: protected */
    public BounceRecyclerView generateListView(Context context, int i) {
        PagerSnapHelper pagerSnapHelper;
        BounceRecyclerView bounceRecyclerView = new BounceRecyclerView(context, this.mLayoutType, this.mColumnCount, this.mColumnGap, i);
        if (bounceRecyclerView.getSwipeLayout() != null && WXUtils.getBoolean(getAttrs().get(Constants.Name.NEST_SCROLLING_ENABLED), false).booleanValue()) {
            bounceRecyclerView.getSwipeLayout().setNestedScrollingEnabled(true);
        }
        if (WXUtils.getBoolean(getAttrs().get(Constants.Name.PAGE_ENABLED), false).booleanValue()) {
            if (TextUtils.isEmpty(WXUtils.getString(getAttrs().get(Constants.Name.PAGE_SIZE), (String) null))) {
                pagerSnapHelper = new PagerSnapHelper();
            } else {
                pagerSnapHelper = new WXPagerSnapHelper();
            }
            pagerSnapHelper.attachToRecyclerView((RecyclerView) bounceRecyclerView.getInnerView());
        }
        return bounceRecyclerView;
    }

    public void addChild(WXComponent wXComponent, int i) {
        super.addChild(wXComponent, i);
        if (wXComponent != null && i >= -1) {
            setRefreshOrLoading(wXComponent);
            if (getHostView() != null && hasColumnPros()) {
                updateRecyclerAttr();
                ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
            }
        }
    }

    private boolean hasColumnPros() {
        return (getAttrs().containsKey(Constants.Name.COLUMN_WIDTH) && this.mColumnWidth != WXUtils.parseFloat(getAttrs().get(Constants.Name.COLUMN_WIDTH))) || (getAttrs().containsKey(Constants.Name.COLUMN_COUNT) && this.mColumnCount != WXUtils.parseInt(getAttrs().get(Constants.Name.COLUMN_COUNT))) || (getAttrs().containsKey(Constants.Name.COLUMN_GAP) && this.mColumnGap != WXUtils.parseFloat(getAttrs().get(Constants.Name.COLUMN_GAP)));
    }

    private boolean setRefreshOrLoading(final WXComponent wXComponent) {
        if (getHostView() == null) {
            WXLogUtils.e(this.TAG, "setRefreshOrLoading: HostView == null !!!!!! check list attr has append =tree");
            return true;
        } else if (wXComponent instanceof WXRefresh) {
            ((BounceRecyclerView) getHostView()).setOnRefreshListener((WXRefresh) wXComponent);
            ((BounceRecyclerView) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    ((BounceRecyclerView) WXListComponent.this.getHostView()).setHeaderView(wXComponent);
                }
            }), 100);
            return true;
        } else if (!(wXComponent instanceof WXLoading)) {
            return false;
        } else {
            ((BounceRecyclerView) getHostView()).setOnLoadingListener((WXLoading) wXComponent);
            ((BounceRecyclerView) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    ((BounceRecyclerView) WXListComponent.this.getHostView()).setFooterView(wXComponent);
                }
            }), 100);
            return true;
        }
    }

    private void updateRecyclerAttr() {
        this.mColumnCount = WXUtils.parseInt(getAttrs().get(Constants.Name.COLUMN_COUNT));
        if (this.mColumnCount <= 0 && this.mLayoutType != 1) {
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.put("componentType", getComponentType());
            arrayMap.put("attribute", getAttrs().toString());
            arrayMap.put("stackTrace", Arrays.toString(Thread.currentThread().getStackTrace()));
            WXExceptionUtils.commitCriticalExceptionRT(getInstanceId(), WXErrorCode.WX_RENDER_ERR_LIST_INVALID_COLUMN_COUNT, Constants.Name.COLUMN_COUNT, String.format(Locale.ENGLISH, "You are trying to set the list/recycler/vlist/waterfall's column to %d, which is illegal. The column count should be a positive integer", new Object[]{Integer.valueOf(this.mColumnCount)}), arrayMap);
            this.mColumnCount = 1;
        }
        this.mColumnGap = WXUtils.parseFloat(getAttrs().get(Constants.Name.COLUMN_GAP));
        this.mColumnWidth = WXUtils.parseFloat(getAttrs().get(Constants.Name.COLUMN_WIDTH));
        this.mPaddingLeft = WXUtils.parseFloat(getAttrs().get(Constants.Name.PADDING_LEFT));
        this.mPaddingRight = WXUtils.parseFloat(getAttrs().get(Constants.Name.PADDING_RIGHT));
        String str = (String) getAttrs().get(Constants.Name.SPAN_OFFSETS);
        this.mSpanOffsetsStr = str;
        try {
            if (!TextUtils.isEmpty(str)) {
                List<Float> parseArray = JSON.parseArray(this.mSpanOffsetsStr, Float.class);
                int size = parseArray.size();
                Float[] fArr = this.mSpanOffsets;
                if (fArr == null || fArr.length != size) {
                    this.mSpanOffsets = new Float[size];
                }
                parseArray.toArray(this.mSpanOffsets);
            } else {
                this.mSpanOffsets = null;
            }
        } catch (Throwable th) {
            WXLogUtils.w("Parser SpanOffsets error ", th);
        }
        if (!this.hasSetGapItemDecoration && getSpanOffsets() != null && getHostView() != null && ((BounceRecyclerView) getHostView()).getInnerView() != null) {
            this.hasSetGapItemDecoration = true;
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).addItemDecoration(new GapItemDecoration(this));
        }
    }

    @WXComponentProp(name = "spanOffsets")
    public void setSpanOffsets(String str) {
        if (!TextUtils.equals(str, this.mSpanOffsetsStr)) {
            markComponentUsable();
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "columnWidth")
    public void setColumnWidth(float f) {
        if (f != this.mColumnWidth) {
            markComponentUsable();
            updateRecyclerAttr();
            WXRecyclerView wXRecyclerView = (WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView();
            if (wXRecyclerView.getLayoutManager() != null) {
                wXRecyclerView.getAdapter().notifyDataSetChanged();
            } else {
                wXRecyclerView.initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
            }
        }
    }

    @WXComponentProp(name = "columnCount")
    public void setColumnCount(int i) {
        if (i != this.mColumnCount) {
            markComponentUsable();
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "columnGap")
    public void setColumnGap(float f) throws InterruptedException {
        if (f != this.mColumnGap) {
            markComponentUsable();
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).setScrollable(z);
    }

    @WXComponentProp(name = "renderReverse")
    public void setRenderReverse(boolean z) {
        this.renderReverse = z;
    }

    public void updateProperties(Map<String, Object> map) {
        super.updateProperties(map);
        if (isRecycler(this)) {
            if (WXBasicComponentType.WATERFALL.equals(getComponentType())) {
                this.mLayoutType = 3;
            } else {
                this.mLayoutType = getAttrs().getLayoutType();
            }
        }
        if (!map.containsKey("padding") && !map.containsKey(Constants.Name.PADDING_LEFT) && !map.containsKey(Constants.Name.PADDING_RIGHT)) {
            return;
        }
        if (this.mPaddingLeft != WXUtils.parseFloat(map.get(Constants.Name.PADDING_LEFT)) || this.mPaddingRight != WXUtils.parseFloat(map.get(Constants.Name.PADDING_RIGHT))) {
            markComponentUsable();
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    public void createChildViewAt(int i) {
        if (i >= 0 || childCount() - 1 >= 0) {
            IWXObject listChild = getListChild(i);
            if (listChild instanceof WXBaseRefresh) {
                final WXComponent wXComponent = (WXComponent) listChild;
                wXComponent.createView();
                if (listChild instanceof WXRefresh) {
                    ((BounceRecyclerView) getHostView()).setOnRefreshListener((WXRefresh) listChild);
                    ((BounceRecyclerView) getHostView()).postDelayed(new Runnable() {
                        public void run() {
                            ((BounceRecyclerView) WXListComponent.this.getHostView()).setHeaderView(wXComponent);
                        }
                    }, 100);
                } else if (listChild instanceof WXLoading) {
                    ((BounceRecyclerView) getHostView()).setOnLoadingListener((WXLoading) listChild);
                    ((BounceRecyclerView) getHostView()).postDelayed(new Runnable() {
                        public void run() {
                            ((BounceRecyclerView) WXListComponent.this.getHostView()).setFooterView(wXComponent);
                        }
                    }, 100);
                }
            } else {
                super.createChildViewAt(i);
            }
        }
    }

    public void remove(WXComponent wXComponent, boolean z) {
        super.remove(wXComponent, z);
        removeFooterOrHeader(wXComponent);
    }

    private void removeFooterOrHeader(WXComponent wXComponent) {
        if (wXComponent instanceof WXLoading) {
            ((BounceRecyclerView) getHostView()).removeFooterView(wXComponent);
        } else if (wXComponent instanceof WXRefresh) {
            ((BounceRecyclerView) getHostView()).removeHeaderView(wXComponent);
        }
    }

    private boolean isRecycler(WXComponent wXComponent) {
        return WXBasicComponentType.WATERFALL.equals(wXComponent.getComponentType()) || WXBasicComponentType.RECYCLE_LIST.equals(wXComponent.getComponentType()) || WXBasicComponentType.RECYCLER.equals(wXComponent.getComponentType());
    }

    public Float[] getSpanOffsets() {
        return this.mSpanOffsets;
    }

    public boolean isRenderFromBottom() {
        return this.renderReverse;
    }
}
