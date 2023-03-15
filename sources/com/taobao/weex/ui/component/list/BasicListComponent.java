package com.taobao.weex.ui.component.list;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.collection.ArrayMap;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.ICheckBindingScroller;
import com.taobao.weex.common.IWXObject;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.AppearanceHelper;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXBaseRefresh;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXHeader;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.helper.ScrollStartEndHelper;
import com.taobao.weex.ui.component.helper.WXStickyHelper;
import com.taobao.weex.ui.component.list.ListComponentView;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.listview.adapter.IOnLoadMoreListener;
import com.taobao.weex.ui.view.listview.adapter.IRecyclerAdapterListener;
import com.taobao.weex.ui.view.listview.adapter.ListBaseViewHolder;
import com.taobao.weex.ui.view.listview.adapter.RecyclerViewBaseAdapter;
import com.taobao.weex.ui.view.listview.adapter.WXRecyclerViewOnScrollListener;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class BasicListComponent<T extends ViewGroup & ListComponentView> extends WXVContainer<T> implements IRecyclerAdapterListener<ListBaseViewHolder>, IOnLoadMoreListener, Scrollable {
    private static final boolean DEFAULT_EXCLUDED = false;
    private static final String DEFAULT_TRIGGER_TYPE = "longpress";
    private static final String DRAG_ANCHOR = "dragAnchor";
    private static final String DRAG_TRIGGER_TYPE = "dragTriggerType";
    private static final String EXCLUDED = "dragExcluded";
    public static final String LOADMOREOFFSET = "loadmoreoffset";
    private static final int MAX_VIEWTYPE_ALLOW_CACHE = 9;
    public static final String TRANSFORM = "transform";
    private static boolean mAllowCacheViewHolder = true;
    private static boolean mDownForBidCacheViewHolder = false;
    private static final Pattern transformPattern = Pattern.compile("([a-z]+)\\(([0-9\\.]+),?([0-9\\.]+)?\\)");
    private String TAG = "BasicListComponent";
    private boolean isScrollable = true;
    boolean isThisGroupFinished = false;
    /* access modifiers changed from: private */
    public WXComponent keepPositionCell = null;
    /* access modifiers changed from: private */
    public Runnable keepPositionCellRunnable = null;
    private long keepPositionLayoutDelay = 150;
    ListStanceCell listStanceObject;
    /* access modifiers changed from: private */
    public Runnable mAppearChangeRunnable = null;
    private long mAppearChangeRunnableDelay = 50;
    private Map<String, AppearanceHelper> mAppearComponents = new HashMap();
    protected int mColumnCount = 1;
    protected float mColumnGap = 0.0f;
    protected float mColumnWidth = 0.0f;
    /* access modifiers changed from: private */
    public DragHelper mDragHelper;
    private boolean mForceLoadmoreNextTime = false;
    private boolean mHasAddScrollEvent = false;
    private RecyclerView.ItemAnimator mItemAnimator;
    private Point mLastReport = new Point(-1, -1);
    protected int mLayoutType = 1;
    protected float mLeftGap = 0.0f;
    private int mListCellCount = 0;
    private int mOffsetAccuracy = 10;
    private ArrayMap<String, Long> mRefToViewType;
    protected float mRightGap = 0.0f;
    private ScrollStartEndHelper mScrollStartEndHelper;
    private Map<String, Map<String, WXComponent>> mStickyMap = new HashMap();
    private String mTriggerType;
    /* access modifiers changed from: private */
    public WXRecyclerViewOnScrollListener mViewOnScrollListener = new WXRecyclerViewOnScrollListener(this);
    private SparseArray<ArrayList<WXComponent>> mViewTypes;
    private WXStickyHelper stickyHelper = new WXStickyHelper(this);

    interface DragTriggerType {
        public static final String LONG_PRESS = "longpress";
        public static final String PAN = "pan";
    }

    public void addSubView(View view, int i) {
    }

    /* access modifiers changed from: package-private */
    public abstract T generateListView(Context context, int i);

    /* access modifiers changed from: protected */
    public int getChildrenLayoutTopOffset() {
        return 0;
    }

    public int getOrientation() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public boolean isRenderFromBottom() {
        return false;
    }

    public BasicListComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void setMarginsSupportRTL(ViewGroup.MarginLayoutParams marginLayoutParams, int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT >= 17) {
            marginLayoutParams.setMargins(i, i2, i3, i4);
            marginLayoutParams.setMarginStart(i);
            marginLayoutParams.setMarginEnd(i3);
        } else if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) marginLayoutParams;
            if (isLayoutRTL()) {
                layoutParams.gravity = 53;
                marginLayoutParams.setMargins(i3, i2, i, i4);
                return;
            }
            layoutParams.gravity = 51;
            marginLayoutParams.setMargins(i, i2, i3, i4);
        } else {
            marginLayoutParams.setMargins(i, i2, i3, i4);
        }
    }

    public void setLayout(WXComponent wXComponent) {
        if (wXComponent.getHostView() != null) {
            ViewCompat.setLayoutDirection(wXComponent.getHostView(), wXComponent.isLayoutRTL() ? 1 : 0);
        }
        super.setLayout(wXComponent);
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(T t) {
        super.onHostViewInitialized(t);
        WXRecyclerView innerView = ((ListComponentView) t).getInnerView();
        if (innerView == null || innerView.getAdapter() == null) {
            WXLogUtils.e(this.TAG, "RecyclerView is not found or Adapter is not bound");
            return;
        }
        if (WXUtils.getBoolean(getAttrs().get("prefetchGapDisable"), false).booleanValue() && innerView.getLayoutManager() != null) {
            innerView.getLayoutManager().setItemPrefetchEnabled(false);
        }
        if (this.mChildren == null) {
            WXLogUtils.e(this.TAG, "children is null");
            return;
        }
        this.mDragHelper = new DefaultDragHelper(this.mChildren, innerView, new EventTrigger() {
            public void triggerEvent(String str, Map<String, Object> map) {
                BasicListComponent.this.fireEvent(str, map);
            }
        });
        this.mTriggerType = getTriggerType(this);
    }

    /* access modifiers changed from: protected */
    public WXComponent.MeasureOutput measure(int i, int i2) {
        return super.measure(i, i2);
    }

    public void destroy() {
        if (!(this.mAppearChangeRunnable == null || getHostView() == null)) {
            ((ViewGroup) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            this.mAppearChangeRunnable = null;
        }
        if (!(getHostView() == null || ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() == null)) {
            ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().clearOnScrollListeners();
        }
        super.destroy();
        Map<String, Map<String, WXComponent>> map = this.mStickyMap;
        if (map != null) {
            map.clear();
        }
        SparseArray<ArrayList<WXComponent>> sparseArray = this.mViewTypes;
        if (sparseArray != null) {
            sparseArray.clear();
        }
        ArrayMap<String, Long> arrayMap = this.mRefToViewType;
        if (arrayMap != null) {
            arrayMap.clear();
        }
    }

    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent wXComponent, View view, int i, int i2, int i3, int i4, int i5, int i6) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if ((wXComponent instanceof WXBaseRefresh) && marginLayoutParams == null) {
            return new LinearLayout.LayoutParams(i, i2);
        }
        if (marginLayoutParams == null) {
            return new RecyclerView.LayoutParams(i, i2);
        }
        marginLayoutParams.width = i;
        marginLayoutParams.height = i2;
        setMarginsSupportRTL(marginLayoutParams, i3, 0, i4, 0);
        return marginLayoutParams;
    }

    /* access modifiers changed from: protected */
    public T initComponentHostView(Context context) {
        T generateListView = generateListView(context, getOrientation());
        String attrByKey = getAttrByKey("transform");
        if (attrByKey != null) {
            ((ListComponentView) generateListView).getInnerView().addItemDecoration(RecyclerTransform.parseTransforms(getOrientation(), attrByKey));
        }
        if (getAttrs().get(Constants.Name.KEEP_POSITION_LAYOUT_DELAY) != null) {
            this.keepPositionLayoutDelay = (long) WXUtils.getNumberInt(getAttrs().get(Constants.Name.KEEP_POSITION_LAYOUT_DELAY), (int) this.keepPositionLayoutDelay);
        }
        if (getAttrs().get("appearActionDelay") != null) {
            this.mAppearChangeRunnableDelay = (long) WXUtils.getNumberInt(getAttrs().get("appearActionDelay"), (int) this.mAppearChangeRunnableDelay);
        }
        ListComponentView listComponentView = (ListComponentView) generateListView;
        this.mItemAnimator = listComponentView.getInnerView().getItemAnimator();
        RecyclerViewBaseAdapter recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(this);
        recyclerViewBaseAdapter.setHasStableIds(true);
        listComponentView.setRecyclerViewBaseAdapter(recyclerViewBaseAdapter);
        listComponentView.getInnerView().addOnScrollListener(this.mViewOnScrollListener);
        if (getAttrs().get(Constants.Name.HAS_FIXED_SIZE) != null) {
            listComponentView.getInnerView().setHasFixedSize(WXUtils.getBoolean(getAttrs().get(Constants.Name.HAS_FIXED_SIZE), false).booleanValue());
        }
        listComponentView.getInnerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                int size;
                View childAt;
                super.onScrollStateChanged(recyclerView, i);
                BasicListComponent.this.getScrollStartEndHelper().onScrollStateChanged(i);
                List<OnWXScrollListener> wXScrollListeners = BasicListComponent.this.getInstance().getWXScrollListeners();
                if (wXScrollListeners != null && (size = wXScrollListeners.size()) > 0) {
                    int i2 = 0;
                    while (i2 < size && i2 < wXScrollListeners.size()) {
                        OnWXScrollListener onWXScrollListener = wXScrollListeners.get(i2);
                        if (!(onWXScrollListener == null || (childAt = recyclerView.getChildAt(0)) == null)) {
                            onWXScrollListener.onScrollStateChanged(recyclerView, 0, childAt.getTop(), i);
                        }
                        i2++;
                    }
                }
            }

            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                List<OnWXScrollListener> wXScrollListeners = BasicListComponent.this.getInstance().getWXScrollListeners();
                if (wXScrollListeners != null && wXScrollListeners.size() > 0) {
                    try {
                        for (OnWXScrollListener next : wXScrollListeners) {
                            if (next != null) {
                                if (!(next instanceof ICheckBindingScroller)) {
                                    next.onScrolled(recyclerView, i, i2);
                                } else if (((ICheckBindingScroller) next).isNeedScroller(BasicListComponent.this.getRef(), (Object) null)) {
                                    next.onScrolled(recyclerView, i, i2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        generateListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ViewGroup viewGroup = (ViewGroup) BasicListComponent.this.getHostView();
                if (viewGroup != null) {
                    BasicListComponent.this.mViewOnScrollListener.onScrolled(((ListComponentView) viewGroup).getInnerView(), 0, 0);
                    if (Build.VERSION.SDK_INT >= 16) {
                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
        return generateListView;
    }

    public void bindStickStyle(WXComponent wXComponent) {
        this.stickyHelper.bindStickStyle(wXComponent, this.mStickyMap);
    }

    public void unbindStickStyle(WXComponent wXComponent) {
        this.stickyHelper.unbindStickStyle(wXComponent, this.mStickyMap);
        WXHeader wXHeader = (WXHeader) findTypeParent(wXComponent, WXHeader.class);
        if (wXHeader != null && getHostView() != null) {
            ((ListComponentView) ((ViewGroup) getHostView())).notifyStickyRemove(wXHeader);
        }
    }

    private WXComponent findDirectListChild(WXComponent wXComponent) {
        WXVContainer parent;
        if (wXComponent == null || (parent = wXComponent.getParent()) == null) {
            return null;
        }
        if (parent instanceof BasicListComponent) {
            return wXComponent;
        }
        return findDirectListChild(parent);
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -304480883:
                if (str.equals(Constants.Name.DRAGGABLE)) {
                    c = 0;
                    break;
                }
                break;
            case -223520855:
                if (str.equals(Constants.Name.SHOW_SCROLLBAR)) {
                    c = 1;
                    break;
                }
                break;
            case -112563826:
                if (str.equals("loadmoreoffset")) {
                    c = 2;
                    break;
                }
                break;
            case -5620052:
                if (str.equals(Constants.Name.OFFSET_ACCURACY)) {
                    c = 3;
                    break;
                }
                break;
            case 66669991:
                if (str.equals(Constants.Name.SCROLLABLE)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                setDraggable(WXUtils.getBoolean(obj, false).booleanValue());
                return true;
            case 1:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool != null) {
                    setShowScrollbar(bool.booleanValue());
                }
                return true;
            case 2:
                break;
            case 3:
                setOffsetAccuracy(WXUtils.getInteger(obj, 10).intValue());
                break;
            case 4:
                setScrollable(WXUtils.getBoolean(obj, true).booleanValue());
                return true;
            default:
                return super.setProperty(str, obj);
        }
        return true;
    }

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        this.isScrollable = z;
        WXRecyclerView innerView = ((ListComponentView) ((ViewGroup) getHostView())).getInnerView();
        if (innerView != null) {
            innerView.setScrollable(z);
        }
    }

    @WXComponentProp(name = "offsetAccuracy")
    public void setOffsetAccuracy(int i) {
        this.mOffsetAccuracy = (int) WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidthWithFloat());
    }

    @WXComponentProp(name = "draggable")
    public void setDraggable(boolean z) {
        DragHelper dragHelper = this.mDragHelper;
        if (dragHelper != null) {
            dragHelper.setDraggable(z);
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("set draggable : " + z);
        }
    }

    @WXComponentProp(name = "showScrollbar")
    public void setShowScrollbar(boolean z) {
        if (getHostView() != null && ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() != null) {
            if (getOrientation() == 1) {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().setVerticalScrollBarEnabled(z);
            } else {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().setHorizontalScrollBarEnabled(z);
            }
        }
    }

    public boolean isScrollable() {
        return this.isScrollable;
    }

    private void setAppearanceWatch(WXComponent wXComponent, int i, boolean z) {
        int indexOf;
        AppearanceHelper appearanceHelper = this.mAppearComponents.get(wXComponent.getRef());
        if (appearanceHelper != null) {
            appearanceHelper.setWatchEvent(i, z);
        } else if (z && (indexOf = this.mChildren.indexOf(findDirectListChild(wXComponent))) != -1) {
            AppearanceHelper appearanceHelper2 = new AppearanceHelper(wXComponent, indexOf);
            appearanceHelper2.setWatchEvent(i, true);
            this.mAppearComponents.put(wXComponent.getRef(), appearanceHelper2);
        }
    }

    public void bindAppearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 0, true);
        if (this.mAppearChangeRunnable == null) {
            this.mAppearChangeRunnable = new Runnable() {
                public void run() {
                    if (BasicListComponent.this.mAppearChangeRunnable != null) {
                        BasicListComponent.this.notifyAppearStateChange(0, 0, 0, 0);
                    }
                }
            };
        }
        if (getHostView() != null) {
            ((ViewGroup) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            ((ViewGroup) getHostView()).postDelayed(this.mAppearChangeRunnable, this.mAppearChangeRunnableDelay);
        }
    }

    public void bindDisappearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 1, true);
    }

    public void unbindAppearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 0, false);
    }

    public void unbindDisappearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 1, false);
    }

    public void scrollTo(WXComponent wXComponent, Map<String, Object> map) {
        int indexOf;
        boolean z = true;
        float f = 0.0f;
        if (map != null) {
            String obj = map.get("offset") == null ? WXInstanceApm.VALUE_ERROR_CODE_DEFAULT : map.get("offset").toString();
            z = WXUtils.getBoolean(map.get(Constants.Name.ANIMATED), true).booleanValue();
            if (obj != null) {
                try {
                    f = WXViewUtils.getRealPxByWidth(Float.parseFloat(obj), getInstance().getInstanceViewPortWidthWithFloat());
                } catch (Exception e) {
                    WXLogUtils.e("Float parseFloat error :" + e.getMessage());
                }
            }
        }
        int i = (int) f;
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            WXCell wXCell = null;
            while (true) {
                if (wXComponent == null) {
                    break;
                } else if (wXComponent instanceof WXCell) {
                    wXCell = (WXCell) wXComponent;
                    break;
                } else {
                    wXComponent = wXComponent.getParent();
                }
            }
            if (wXCell != null && (indexOf = this.mChildren.indexOf(wXCell)) != -1) {
                ((ListComponentView) viewGroup).getInnerView().scrollTo(z, indexOf, i, getOrientation());
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:61:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x010f A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBeforeScroll(int r11, int r12) {
        /*
            r10 = this;
            android.view.View r11 = r10.getHostView()
            android.view.ViewGroup r11 = (android.view.ViewGroup) r11
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.taobao.weex.ui.component.WXComponent>> r0 = r10.mStickyMap
            if (r0 == 0) goto L_0x0133
            if (r11 != 0) goto L_0x000e
            goto L_0x0133
        L_0x000e:
            java.lang.String r1 = r10.getRef()
            java.lang.Object r0 = r0.get(r1)
            java.util.Map r0 = (java.util.Map) r0
            if (r0 != 0) goto L_0x001b
            return
        L_0x001b:
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
            r1 = -1
        L_0x0024:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L_0x011e
            java.lang.Object r2 = r0.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r2 = r2.getValue()
            com.taobao.weex.ui.component.WXComponent r2 = (com.taobao.weex.ui.component.WXComponent) r2
            if (r2 == 0) goto L_0x0024
            boolean r3 = r2 instanceof com.taobao.weex.ui.component.list.WXCell
            if (r3 == 0) goto L_0x0024
            r3 = r2
            com.taobao.weex.ui.component.list.WXCell r3 = (com.taobao.weex.ui.component.list.WXCell) r3
            android.view.View r4 = r3.getHostView()
            if (r4 != 0) goto L_0x0046
            return
        L_0x0046:
            r4 = 2
            int[] r5 = new int[r4]
            android.view.View r6 = r2.getHostView()
            r6.getLocationOnScreen(r5)
            int[] r4 = new int[r4]
            com.taobao.weex.ui.component.Scrollable r2 = r2.getParentScroller()
            android.view.ViewGroup r2 = r2.getView()
            r2.getLocationOnScreen(r4)
            r2 = 1
            r5 = r5[r2]
            r4 = r4[r2]
            int r5 = r5 - r4
            android.view.View r4 = r10.getHostView()
            android.view.ViewGroup r4 = (android.view.ViewGroup) r4
            com.taobao.weex.ui.component.list.ListComponentView r4 = (com.taobao.weex.ui.component.list.ListComponentView) r4
            com.taobao.weex.ui.view.listview.WXRecyclerView r4 = r4.getInnerView()
            androidx.recyclerview.widget.RecyclerView$LayoutManager r4 = r4.getLayoutManager()
            boolean r6 = r4 instanceof androidx.recyclerview.widget.LinearLayoutManager
            r7 = 0
            if (r6 != 0) goto L_0x00b6
            boolean r6 = r4 instanceof androidx.recyclerview.widget.GridLayoutManager
            if (r6 == 0) goto L_0x007d
            goto L_0x00b6
        L_0x007d:
            boolean r6 = r4 instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager
            if (r6 == 0) goto L_0x00b3
            r6 = 3
            int[] r6 = new int[r6]
            androidx.recyclerview.widget.StaggeredGridLayoutManager r4 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r4
            int[] r8 = r4.findFirstVisibleItemPositions(r6)
            r8 = r8[r7]
            int[] r4 = r4.findLastVisibleItemPositions(r6)
            r4 = r4[r7]
            java.util.ArrayList r6 = r10.mChildren
            int r6 = r6.indexOf(r3)
            if (r6 <= r8) goto L_0x00ae
            int r9 = r3.getStickyOffset()
            if (r9 <= 0) goto L_0x00ab
            if (r8 >= r6) goto L_0x00ab
            if (r6 > r4) goto L_0x00ab
            int r4 = r3.getStickyOffset()
            if (r5 > r4) goto L_0x00ab
            goto L_0x00ae
        L_0x00ab:
            r4 = 0
            r6 = 1
            goto L_0x00df
        L_0x00ae:
            if (r6 <= r1) goto L_0x00b1
            r1 = r6
        L_0x00b1:
            r4 = 1
            goto L_0x00b4
        L_0x00b3:
            r4 = 0
        L_0x00b4:
            r6 = 0
            goto L_0x00df
        L_0x00b6:
            androidx.recyclerview.widget.LinearLayoutManager r4 = (androidx.recyclerview.widget.LinearLayoutManager) r4
            int r6 = r4.findFirstVisibleItemPosition()
            int r4 = r4.findLastVisibleItemPosition()
            java.util.ArrayList r8 = r10.mChildren
            int r8 = r8.indexOf(r3)
            r3.setScrollPositon(r8)
            if (r8 <= r6) goto L_0x00db
            int r9 = r3.getStickyOffset()
            if (r9 <= 0) goto L_0x00ab
            if (r6 >= r8) goto L_0x00ab
            if (r8 > r4) goto L_0x00ab
            int r4 = r3.getStickyOffset()
            if (r5 > r4) goto L_0x00ab
        L_0x00db:
            if (r8 <= r1) goto L_0x00b1
            r1 = r8
            goto L_0x00b1
        L_0x00df:
            if (r4 == 0) goto L_0x00f1
            int r4 = r3.getLocationFromStart()
            if (r4 < 0) goto L_0x00f1
            int r4 = r3.getStickyOffset()
            if (r5 > r4) goto L_0x00f1
            if (r12 < 0) goto L_0x00f1
            r4 = 1
            goto L_0x00f2
        L_0x00f1:
            r4 = 0
        L_0x00f2:
            int r8 = r3.getLocationFromStart()
            int r9 = r3.getStickyOffset()
            if (r8 > r9) goto L_0x0105
            int r8 = r3.getStickyOffset()
            if (r5 <= r8) goto L_0x0105
            if (r12 > 0) goto L_0x0105
            goto L_0x0106
        L_0x0105:
            r2 = 0
        L_0x0106:
            if (r4 == 0) goto L_0x010f
            r2 = r11
            com.taobao.weex.ui.component.list.ListComponentView r2 = (com.taobao.weex.ui.component.list.ListComponentView) r2
            r2.notifyStickyShow(r3)
            goto L_0x0119
        L_0x010f:
            if (r2 != 0) goto L_0x0113
            if (r6 == 0) goto L_0x0119
        L_0x0113:
            r2 = r11
            com.taobao.weex.ui.component.list.ListComponentView r2 = (com.taobao.weex.ui.component.list.ListComponentView) r2
            r2.notifyStickyRemove(r3)
        L_0x0119:
            r3.setLocationFromStart(r5)
            goto L_0x0024
        L_0x011e:
            if (r1 < 0) goto L_0x0126
            com.taobao.weex.ui.component.list.ListComponentView r11 = (com.taobao.weex.ui.component.list.ListComponentView) r11
            r11.updateStickyView(r1)
            goto L_0x0133
        L_0x0126:
            boolean r12 = r11 instanceof com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView
            if (r12 == 0) goto L_0x0133
            com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView r11 = (com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView) r11
            com.taobao.weex.ui.component.list.StickyHeaderHelper r11 = r11.getStickyHeaderHelper()
            r11.clearStickyHeaders()
        L_0x0133:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.BasicListComponent.onBeforeScroll(int, int):void");
    }

    public int getScrollY() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup == null) {
            return 0;
        }
        return ((ListComponentView) viewGroup).getInnerView().getScrollY();
    }

    public int getScrollX() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup == null) {
            return 0;
        }
        return ((ListComponentView) viewGroup).getInnerView().getScrollX();
    }

    public void addChild(WXComponent wXComponent) {
        addChild(wXComponent, -1);
    }

    public void addChild(WXComponent wXComponent, int i) {
        boolean z;
        super.addChild(wXComponent, i);
        if (wXComponent != null && i >= -1) {
            if (i >= this.mChildren.size()) {
                i = -1;
            }
            bindViewType(wXComponent);
            boolean z2 = false;
            if (this.isThisGroupFinished || !isRenderFromBottom()) {
                z = false;
            } else if (wXComponent.getAttrs().containsKey("renderReversePosition")) {
                z = true;
            } else {
                return;
            }
            this.isThisGroupFinished = true;
            int size = i == -1 ? this.mChildren.size() - 1 : i;
            final ViewGroup viewGroup = (ViewGroup) getHostView();
            if (viewGroup != null) {
                if (getBasicComponentData() != null && "default".equals(getAttrs().get(Constants.Name.INSERT_CELL_ANIMATION))) {
                    ((ListComponentView) viewGroup).getInnerView().setItemAnimator(this.mItemAnimator);
                } else {
                    ((ListComponentView) viewGroup).getInnerView().setItemAnimator((RecyclerView.ItemAnimator) null);
                }
                if (wXComponent.getBasicComponentData() != null && WXUtils.getBoolean(wXComponent.getAttrs().get(Constants.Name.KEEP_SCROLL_POSITION), false).booleanValue() && i <= getChildCount() && i > -1) {
                    z2 = true;
                }
                if (!z2 && z) {
                    z2 = true;
                }
                if (z2) {
                    ListComponentView listComponentView = (ListComponentView) viewGroup;
                    if (listComponentView.getInnerView().getLayoutManager() instanceof LinearLayoutManager) {
                        if (this.keepPositionCell == null) {
                            ListBaseViewHolder listBaseViewHolder = (ListBaseViewHolder) listComponentView.getInnerView().findViewHolderForAdapterPosition(((LinearLayoutManager) listComponentView.getInnerView().getLayoutManager()).findLastCompletelyVisibleItemPosition());
                            if (listBaseViewHolder != null) {
                                this.keepPositionCell = listBaseViewHolder.getComponent();
                            }
                            if (this.keepPositionCell != null) {
                                if (!listComponentView.getInnerView().isLayoutFrozen()) {
                                    listComponentView.getInnerView().setLayoutFrozen(true);
                                }
                                Runnable runnable = this.keepPositionCellRunnable;
                                if (runnable != null) {
                                    viewGroup.removeCallbacks(runnable);
                                }
                                this.keepPositionCellRunnable = new Runnable() {
                                    public void run() {
                                        if (BasicListComponent.this.keepPositionCell != null) {
                                            BasicListComponent basicListComponent = BasicListComponent.this;
                                            int indexOf = basicListComponent.indexOf(basicListComponent.keepPositionCell);
                                            int top = BasicListComponent.this.keepPositionCell.getHostView() != null ? BasicListComponent.this.keepPositionCell.getHostView().getTop() : 0;
                                            if (top > 0) {
                                                ((LinearLayoutManager) ((ListComponentView) viewGroup).getInnerView().getLayoutManager()).scrollToPositionWithOffset(indexOf, top);
                                            } else {
                                                ((ListComponentView) viewGroup).getInnerView().getLayoutManager().scrollToPosition(indexOf);
                                            }
                                            ((ListComponentView) viewGroup).getInnerView().setLayoutFrozen(false);
                                            WXComponent unused = BasicListComponent.this.keepPositionCell = null;
                                            Runnable unused2 = BasicListComponent.this.keepPositionCellRunnable = null;
                                        }
                                    }
                                };
                            }
                        }
                        if (this.keepPositionCellRunnable == null) {
                            int findLastVisibleItemPosition = ((LinearLayoutManager) listComponentView.getInnerView().getLayoutManager()).findLastVisibleItemPosition();
                            if (!z) {
                                i = findLastVisibleItemPosition;
                            }
                            listComponentView.getInnerView().scrollToPosition(i);
                        }
                    }
                    listComponentView.getRecyclerViewBaseAdapter().notifyItemRangeInserted(size, 20);
                    Runnable runnable2 = this.keepPositionCellRunnable;
                    if (runnable2 != null) {
                        viewGroup.removeCallbacks(runnable2);
                        viewGroup.postDelayed(this.keepPositionCellRunnable, this.keepPositionLayoutDelay);
                    }
                } else {
                    ((ListComponentView) viewGroup).getRecyclerViewBaseAdapter().notifyDataSetChanged();
                }
            }
            relocateAppearanceHelper();
        }
    }

    private void relocateAppearanceHelper() {
        for (Map.Entry<String, AppearanceHelper> value : this.mAppearComponents.entrySet()) {
            AppearanceHelper appearanceHelper = (AppearanceHelper) value.getValue();
            appearanceHelper.setCellPosition(this.mChildren.indexOf(findDirectListChild(appearanceHelper.getAwareChild())));
        }
    }

    public void remove(WXComponent wXComponent, boolean z) {
        int indexOf = this.mChildren.indexOf(wXComponent);
        if (z) {
            wXComponent.detachViewAndClearPreInfo();
        }
        unBindViewType(wXComponent);
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            if ("default".equals(wXComponent.getAttrs().get(Constants.Name.DELETE_CELL_ANIMATION))) {
                ((ListComponentView) viewGroup).getInnerView().setItemAnimator(this.mItemAnimator);
            } else {
                ((ListComponentView) viewGroup).getInnerView().setItemAnimator((RecyclerView.ItemAnimator) null);
            }
            ((ListComponentView) viewGroup).getRecyclerViewBaseAdapter().notifyItemRemoved(indexOf);
            if (WXEnvironment.isApkDebugable()) {
                String str = this.TAG;
                WXLogUtils.d(str, "removeChild child at " + indexOf);
            }
            super.remove(wXComponent, z);
        }
    }

    public void computeVisiblePointInViewCoordinate(PointF pointF) {
        WXRecyclerView innerView = ((ListComponentView) ((ViewGroup) getHostView())).getInnerView();
        pointF.set((float) innerView.computeHorizontalScrollOffset(), (float) innerView.computeVerticalScrollOffset());
    }

    public void onViewRecycled(ListBaseViewHolder listBaseViewHolder) {
        long currentTimeMillis = System.currentTimeMillis();
        listBaseViewHolder.setComponentUsing(false);
        if (listBaseViewHolder == null || !listBaseViewHolder.canRecycled() || listBaseViewHolder.getComponent() == null || listBaseViewHolder.getComponent().isUsing()) {
            WXLogUtils.w(this.TAG, "this holder can not be allowed to  recycled");
        } else {
            listBaseViewHolder.recycled();
        }
        if (WXEnvironment.isApkDebugable()) {
            String str = this.TAG;
            WXLogUtils.d(str, "Recycle holder " + (System.currentTimeMillis() - currentTimeMillis) + "  Thread:" + Thread.currentThread().getName());
        }
    }

    public void onBindViewHolder(final ListBaseViewHolder listBaseViewHolder, int i) {
        int i2;
        if (listBaseViewHolder != null) {
            listBaseViewHolder.setComponentUsing(true);
            IWXObject listChild = getListChild(i);
            if (listChild instanceof ListStanceCell) {
                listBaseViewHolder.getView().setBackgroundColor(WXResourceUtils.getColor(((ListStanceCell) listChild).getBackgroundColor(), -1));
                listBaseViewHolder.getView().setVisibility(0);
                listBaseViewHolder.getView().postInvalidate();
            } else if (listChild instanceof WXComponent) {
                WXComponent wXComponent = (WXComponent) listChild;
                if (wXComponent == null || (wXComponent instanceof WXRefresh) || (wXComponent instanceof WXLoading) || wXComponent.isFixed()) {
                    if (WXEnvironment.isApkDebugable()) {
                        String str = this.TAG;
                        WXLogUtils.d(str, "Bind WXRefresh & WXLoading " + listBaseViewHolder);
                    }
                    if ((wXComponent instanceof WXBaseRefresh) && listBaseViewHolder.getView() != null) {
                        if (wXComponent.getAttrs().get("holderBackground") != null || wXComponent.getStyles().containsKey("backgroundColor")) {
                            Object obj = wXComponent.getAttrs().get("holderBackground");
                            if (obj != null) {
                                i2 = WXResourceUtils.getColor(obj.toString(), -1);
                            } else {
                                i2 = WXResourceUtils.getColor(wXComponent.getStyles().getBackgroundColor(), -1);
                            }
                            listBaseViewHolder.getView().setBackgroundColor(i2);
                            listBaseViewHolder.getView().setVisibility(0);
                            listBaseViewHolder.getView().postInvalidate();
                        }
                    }
                } else if (listBaseViewHolder.getComponent() != null && (listBaseViewHolder.getComponent() instanceof WXCell)) {
                    if (listBaseViewHolder.isRecycled()) {
                        listBaseViewHolder.bindData(wXComponent);
                        wXComponent.onRenderFinish(1);
                    }
                    DragHelper dragHelper = this.mDragHelper;
                    if (dragHelper != null && dragHelper.isDraggable()) {
                        String str2 = this.mTriggerType;
                        if (str2 == null) {
                            str2 = "longpress";
                        }
                        this.mTriggerType = str2;
                        WXCell wXCell = (WXCell) listBaseViewHolder.getComponent();
                        boolean booleanValue = WXUtils.getBoolean(wXCell.getAttrs().get(EXCLUDED), false).booleanValue();
                        this.mDragHelper.setDragExcluded(listBaseViewHolder, booleanValue);
                        if ("pan".equals(this.mTriggerType)) {
                            this.mDragHelper.setLongPressDragEnabled(false);
                            WXComponent findComponentByAnchorName = findComponentByAnchorName(wXCell, DRAG_ANCHOR);
                            if (findComponentByAnchorName != null && findComponentByAnchorName.getHostView() != null && !booleanValue) {
                                findComponentByAnchorName.getHostView().setOnTouchListener(new View.OnTouchListener() {
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if (MotionEventCompat.getActionMasked(motionEvent) != 0) {
                                            return true;
                                        }
                                        BasicListComponent.this.mDragHelper.startDrag(listBaseViewHolder);
                                        return true;
                                    }
                                });
                            } else if (!WXEnvironment.isApkDebugable()) {
                            } else {
                                if (!booleanValue) {
                                    WXLogUtils.e(this.TAG, "[error] onBindViewHolder: the anchor component or view is not found");
                                    return;
                                }
                                String str3 = this.TAG;
                                WXLogUtils.d(str3, "onBindViewHolder: position " + i + " is drag excluded");
                            }
                        } else if ("longpress".equals(this.mTriggerType)) {
                            this.mDragHelper.setLongPressDragEnabled(true);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void markComponentUsable() {
        Iterator it = this.mChildren.iterator();
        while (it.hasNext()) {
            ((WXComponent) it.next()).setUsing(false);
        }
    }

    public ListBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (((long) i) == -1) {
            return createVHForRefreshComponent(i);
        }
        if (this.mChildren != null) {
            SparseArray<ArrayList<WXComponent>> sparseArray = this.mViewTypes;
            if (sparseArray == null) {
                return createVHForFakeComponent(i);
            }
            ArrayList arrayList = sparseArray.get(i);
            checkRecycledViewPool(i);
            if (arrayList == null) {
                return createVHForFakeComponent(i);
            }
            int i2 = 0;
            while (i2 < arrayList.size()) {
                WXComponent wXComponent = (WXComponent) arrayList.get(i2);
                if (wXComponent == null || wXComponent.isUsing()) {
                    i2++;
                } else if (wXComponent.isFixed()) {
                    return createVHForFakeComponent(i);
                } else {
                    if (wXComponent instanceof WXCell) {
                        if (wXComponent.getRealView() != null) {
                            return new ListBaseViewHolder(wXComponent, i);
                        }
                        wXComponent.lazy(false);
                        wXComponent.createView();
                        wXComponent.applyLayoutAndEvent(wXComponent);
                        return new ListBaseViewHolder(wXComponent, i, true);
                    } else if (wXComponent instanceof WXBaseRefresh) {
                        return createVHForRefreshComponent(i);
                    } else {
                        WXLogUtils.e(this.TAG, "List cannot include element except cell、header、fixed、refresh and loading");
                        return createVHForFakeComponent(i);
                    }
                }
            }
        }
        if (WXEnvironment.isApkDebugable()) {
            String str = this.TAG;
            WXLogUtils.e(str, "Cannot find request viewType: " + i);
        }
        return createVHForFakeComponent(i);
    }

    private void checkRecycledViewPool(int i) {
        try {
            if (this.mViewTypes.size() > 9) {
                mAllowCacheViewHolder = false;
            }
            if (!(!mDownForBidCacheViewHolder || getHostView() == null || ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() == null)) {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().getRecycledViewPool().setMaxRecycledViews(i, 0);
            }
            if (!mDownForBidCacheViewHolder && !mAllowCacheViewHolder && getHostView() != null && ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() != null) {
                for (int i2 = 0; i2 < this.mViewTypes.size(); i2++) {
                    ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().getRecycledViewPool().setMaxRecycledViews(this.mViewTypes.keyAt(i2), 0);
                }
                mDownForBidCacheViewHolder = true;
            }
        } catch (Exception unused) {
            WXLogUtils.e(this.TAG, "Clear recycledViewPool error!");
        }
    }

    public int getItemViewType(int i) {
        return generateViewType(getListChild(i));
    }

    private WXComponent findComponentByAnchorName(WXComponent wXComponent, String str) {
        String string;
        long currentTimeMillis = WXEnvironment.isApkDebugable() ? System.currentTimeMillis() : 0;
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(wXComponent);
        while (!arrayDeque.isEmpty()) {
            WXComponent wXComponent2 = (WXComponent) arrayDeque.removeFirst();
            if (wXComponent2 != null && (string = WXUtils.getString(wXComponent2.getAttrs().get(str), (String) null)) != null && string.equals(AbsoluteConst.TRUE)) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("dragPerf", "findComponentByAnchorName time: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
                }
                return wXComponent2;
            } else if (wXComponent2 instanceof WXVContainer) {
                WXVContainer wXVContainer = (WXVContainer) wXComponent2;
                int childCount = wXVContainer.childCount();
                for (int i = 0; i < childCount; i++) {
                    arrayDeque.add(wXVContainer.getChild(i));
                }
            }
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("dragPerf", "findComponentByAnchorName elapsed time: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
        return null;
    }

    private String getTriggerType(WXComponent wXComponent) {
        String str = "longpress";
        if (wXComponent == null) {
            return str;
        }
        String string = WXUtils.getString(wXComponent.getAttrs().get(DRAG_TRIGGER_TYPE), str);
        if (str.equals(string) || "pan".equals(string)) {
            str = string;
        }
        if (WXEnvironment.isApkDebugable()) {
            String str2 = this.TAG;
            WXLogUtils.d(str2, "trigger type is " + str);
        }
        return str;
    }

    private void bindViewType(WXComponent wXComponent) {
        int generateViewType = generateViewType(wXComponent);
        if (this.mViewTypes == null) {
            this.mViewTypes = new SparseArray<>();
        }
        ArrayList arrayList = this.mViewTypes.get(generateViewType);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mViewTypes.put(generateViewType, arrayList);
        }
        arrayList.add(wXComponent);
    }

    private void unBindViewType(WXComponent wXComponent) {
        ArrayList arrayList;
        int generateViewType = generateViewType(wXComponent);
        SparseArray<ArrayList<WXComponent>> sparseArray = this.mViewTypes;
        if (sparseArray != null && (arrayList = sparseArray.get(generateViewType)) != null) {
            arrayList.remove(wXComponent);
        }
    }

    private int generateViewType(IWXObject iWXObject) {
        long j = -1;
        try {
            if (!(iWXObject instanceof ListStanceCell)) {
                if (iWXObject instanceof WXComponent) {
                    WXComponent wXComponent = (WXComponent) iWXObject;
                    long parseInt = (long) Integer.parseInt(wXComponent.getRef());
                    String scope = wXComponent.getAttrs().getScope();
                    if (!TextUtils.isEmpty(scope)) {
                        if (this.mRefToViewType == null) {
                            this.mRefToViewType = new ArrayMap<>();
                        }
                        if (!this.mRefToViewType.containsKey(scope)) {
                            this.mRefToViewType.put(scope, Long.valueOf(parseInt));
                        }
                        j = this.mRefToViewType.get(scope).longValue();
                    } else {
                        j = parseInt;
                    }
                }
            }
        } catch (RuntimeException e) {
            WXLogUtils.eTag(this.TAG, e);
            WXLogUtils.e(this.TAG, "getItemViewType: NO ID, this will crash the whole render system of WXListRecyclerView");
        }
        return (int) j;
    }

    public int getItemCount() {
        if (this.isThisGroupFinished) {
            return getChildCount();
        }
        return 0;
    }

    public boolean onFailedToRecycleView(ListBaseViewHolder listBaseViewHolder) {
        if (!WXEnvironment.isApkDebugable()) {
            return false;
        }
        String str = this.TAG;
        WXLogUtils.d(str, "Failed to recycle " + listBaseViewHolder);
        return false;
    }

    public long getItemId(int i) {
        try {
            IWXObject listChild = getListChild(i);
            if (listChild instanceof ListStanceCell) {
                return -1;
            }
            if (listChild instanceof WXComponent) {
                return Long.parseLong(((WXComponent) listChild).getRef());
            }
            return -1;
        } catch (RuntimeException e) {
            WXLogUtils.e(this.TAG, WXLogUtils.getStackTrace(e));
            return -1;
        }
    }

    public void onLoadMore(int i) {
        try {
            String loadMoreOffset = getAttrs().getLoadMoreOffset();
            if (TextUtils.isEmpty(loadMoreOffset)) {
                loadMoreOffset = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
            }
            if (((float) i) <= WXViewUtils.getRealPxByWidth((float) WXUtils.getInt(loadMoreOffset), getInstance().getInstanceViewPortWidthWithFloat()) && getEvents().contains(Constants.Event.LOADMORE)) {
                if (this.mListCellCount != this.mChildren.size() || this.mForceLoadmoreNextTime) {
                    fireEvent(Constants.Event.LOADMORE);
                    this.mListCellCount = this.mChildren.size();
                    this.mForceLoadmoreNextTime = false;
                }
            }
        } catch (Exception e) {
            WXLogUtils.d(this.TAG + "onLoadMore :", (Throwable) e);
        }
    }

    public void notifyAppearStateChange(int i, int i2, int i3, int i4) {
        View hostView;
        String str = null;
        if (this.mAppearChangeRunnable != null) {
            ((ViewGroup) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            this.mAppearChangeRunnable = null;
        }
        if (i4 > 0) {
            str = "up";
        } else if (i4 < 0) {
            str = "down";
        }
        if (getOrientation() == 0 && i3 != 0) {
            str = i3 > 0 ? "left" : "right";
        }
        for (AppearanceHelper next : this.mAppearComponents.values()) {
            WXComponent awareChild = next.getAwareChild();
            if (next.isWatch() && (hostView = awareChild.getHostView()) != null) {
                int appearStatus = next.setAppearStatus(!(ViewCompat.isAttachedToWindow(hostView) ^ true) && next.isViewVisible(true));
                if (appearStatus != 0) {
                    boolean isApkDebugable = WXEnvironment.isApkDebugable();
                    String str2 = Constants.Event.APPEAR;
                    if (isApkDebugable) {
                        WXLogUtils.d(str2, "item " + next.getCellPositionINScollable() + " result " + appearStatus);
                    }
                    if (appearStatus != 1) {
                        str2 = Constants.Event.DISAPPEAR;
                    }
                    awareChild.notifyAppearStateChange(str2, str);
                }
            }
        }
    }

    private ListBaseViewHolder createVHForFakeComponent(int i) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundColor(-1);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
        return new ListBaseViewHolder((View) frameLayout, i);
    }

    private ListBaseViewHolder createVHForRefreshComponent(int i) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, 1));
        return new ListBaseViewHolder((View) frameLayout, i);
    }

    @JSMethod
    public void resetLoadmore() {
        this.mForceLoadmoreNextTime = true;
        this.mListCellCount = 0;
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (ScrollStartEndHelper.isScrollEvent(str) && getHostView() != null && ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() != null && !this.mHasAddScrollEvent) {
            this.mHasAddScrollEvent = true;
            ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean mFirstEvent = true;
                private int offsetXCorrection;
                private int offsetYCorrection;

                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    int i3;
                    int i4;
                    super.onScrolled(recyclerView, i, i2);
                    int computeHorizontalScrollOffset = recyclerView.computeHorizontalScrollOffset();
                    int computeVerticalScrollOffset = recyclerView.computeVerticalScrollOffset();
                    if (i == 0 && i2 == 0) {
                        this.offsetXCorrection = computeHorizontalScrollOffset;
                        this.offsetYCorrection = computeVerticalScrollOffset;
                        i4 = 0;
                        i3 = 0;
                    } else {
                        i4 = computeHorizontalScrollOffset - this.offsetXCorrection;
                        i3 = computeVerticalScrollOffset - this.offsetYCorrection;
                    }
                    BasicListComponent.this.getScrollStartEndHelper().onScrolled(i4, i3);
                    if (BasicListComponent.this.getEvents().contains("scroll")) {
                        if (this.mFirstEvent) {
                            this.mFirstEvent = false;
                            return;
                        }
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (BasicListComponent.this.getOrientation() == 1) {
                            if (!layoutManager.canScrollVertically()) {
                                return;
                            }
                        } else if (BasicListComponent.this.getOrientation() == 0 && !layoutManager.canScrollHorizontally()) {
                            return;
                        }
                        if (BasicListComponent.this.shouldReport(i4, i3)) {
                            BasicListComponent.this.fireScrollEvent(recyclerView, i4, i3);
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fireScrollEvent(RecyclerView recyclerView, int i, int i2) {
        if (getInstance() != null) {
            fireEvent("scroll", getScrollEvent(recyclerView, i, i2));
        }
    }

    public Map<String, Object> getScrollEvent(RecyclerView recyclerView, int i, int i2) {
        boolean z = true;
        if (getOrientation() == 1) {
            i2 = -calcContentOffset(recyclerView);
        }
        int measuredWidth = recyclerView.getMeasuredWidth() + recyclerView.computeHorizontalScrollRange();
        int i3 = 0;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            i3 = (int) (((float) i3) + getListChildLayoutHeight(i4));
        }
        HashMap hashMap = new HashMap(3);
        HashMap hashMap2 = new HashMap(3);
        HashMap hashMap3 = new HashMap(3);
        hashMap2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) measuredWidth, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) i3, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap3.put(Constants.Name.X, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap3.put(Constants.Name.Y, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i2, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap.put(Constants.Name.CONTENT_SIZE, hashMap2);
        hashMap.put(Constants.Name.CONTENT_OFFSET, hashMap3);
        if (recyclerView.getScrollState() != 1) {
            z = false;
        }
        hashMap.put(Constants.Name.ISDRAGGING, Boolean.valueOf(z));
        return hashMap;
    }

    /* access modifiers changed from: private */
    public boolean shouldReport(int i, int i2) {
        if (this.mLastReport.x == -1 && this.mLastReport.y == -1) {
            this.mLastReport.x = i;
            this.mLastReport.y = i2;
            return true;
        }
        int abs = Math.abs(this.mLastReport.x - i);
        int abs2 = Math.abs(this.mLastReport.y - i2);
        int i3 = this.mOffsetAccuracy;
        if (abs < i3 && abs2 < i3) {
            return false;
        }
        this.mLastReport.x = i;
        this.mLastReport.y = i2;
        return true;
    }

    public int calcContentOffset(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int i = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            int findFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (findFirstVisibleItemPosition == -1) {
                return 0;
            }
            View findViewByPosition = layoutManager.findViewByPosition(findFirstVisibleItemPosition);
            int top = findViewByPosition != null ? findViewByPosition.getTop() : 0;
            int i2 = 0;
            while (i < findFirstVisibleItemPosition) {
                i2 = (int) (((float) i2) - getListChildLayoutHeight(i));
                i++;
            }
            if (layoutManager instanceof GridLayoutManager) {
                i2 /= ((GridLayoutManager) layoutManager).getSpanCount();
            }
            return i2 + top;
        } else if (!(layoutManager instanceof StaggeredGridLayoutManager)) {
            return -1;
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            int i3 = staggeredGridLayoutManager.findFirstVisibleItemPositions((int[]) null)[0];
            if (i3 == -1) {
                return 0;
            }
            View findViewByPosition2 = layoutManager.findViewByPosition(i3);
            int top2 = findViewByPosition2 != null ? findViewByPosition2.getTop() : 0;
            int i4 = 0;
            while (i < i3) {
                i4 = (int) (((float) i4) - getListChildLayoutHeight(i));
                i++;
            }
            return (i4 / spanCount) + top2;
        }
    }

    public ScrollStartEndHelper getScrollStartEndHelper() {
        if (this.mScrollStartEndHelper == null) {
            this.mScrollStartEndHelper = new ScrollStartEndHelper(this);
        }
        return this.mScrollStartEndHelper;
    }

    @JSMethod
    public void setSpecialEffects(JSONObject jSONObject) {
        if (getHostView() != null && (getHostView() instanceof BounceRecyclerView)) {
            if (jSONObject == null || !jSONObject.containsKey("id")) {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().setNestInfo((JSONObject) null);
                return;
            }
            String string = jSONObject.getString("id");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("isNestParent", (Object) false);
            jSONObject2.put("instanceId", (Object) getInstance().getInstanceId());
            jSONObject2.put("listParentId", (Object) string);
            ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().setNestInfo(jSONObject2);
            ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().callBackNestParent(getRef(), getInstance().getInstanceId(), WXViewUtils.getRealPxByWidth(WXUtils.getFloat(jSONObject.getString("headerHeight")), getInstance().getInstanceViewPortWidthWithFloat()));
        }
    }

    public int getChildCount() {
        WXComponent child = super.getChild(0);
        if (child == null || !(child instanceof WXRefresh) || this.mColumnCount <= 1) {
            return super.getChildCount();
        }
        return super.getChildCount() + (this.mColumnCount - 1);
    }

    public IWXObject getListChild(int i) {
        int i2;
        WXComponent child = super.getChild(0);
        if (i == 0) {
            return child;
        }
        if (child == null || !(child instanceof WXRefresh) || (i2 = this.mColumnCount) <= 1) {
            return super.getChild(i);
        }
        if (i - i2 < 0) {
            return getListStanceCell(child.getStyles().getBackgroundColor());
        }
        return super.getChild(i - (i2 - 1));
    }

    private IWXObject getListStanceCell(String str) {
        if (this.listStanceObject == null) {
            this.listStanceObject = new ListStanceCell(str);
        }
        return this.listStanceObject;
    }

    private float getListChildLayoutHeight(int i) {
        WXComponent wXComponent;
        IWXObject listChild = getListChild(i);
        if (listChild != null) {
            if (listChild instanceof ListStanceCell) {
                return 1.0f;
            }
            if ((listChild instanceof WXComponent) && (wXComponent = (WXComponent) listChild) != null) {
                return wXComponent.getLayoutHeight();
            }
        }
        return 0.0f;
    }

    @WXComponentProp(name = "bounce")
    public void setBounce(String str) {
        WXRecyclerView innerView = ((ListComponentView) ((ViewGroup) getHostView())).getInnerView();
        if (Boolean.valueOf(str).booleanValue()) {
            innerView.setOverScrollMode(0);
        } else {
            innerView.setOverScrollMode(2);
        }
    }

    @JSMethod
    public void scrollTo(String str, JSCallback jSCallback) {
        float realPxByWidth = WXViewUtils.getRealPxByWidth(WXUtils.getFloat(JSON.parseObject(str).getString(Constants.Name.SCROLL_TOP)), getInstance().getInstanceViewPortWidthWithFloat());
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            WXRecyclerView innerView = ((ListComponentView) viewGroup).getInnerView();
            if (getOrientation() == 1) {
                innerView.scrollTo(0, (int) realPxByWidth, getOrientation());
            } else {
                innerView.scrollTo((int) realPxByWidth, 0, getOrientation());
            }
            if (jSCallback != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("type", WXImage.SUCCEED);
                jSCallback.invoke(hashMap);
            }
        }
    }

    @WXComponentProp(name = "scrollTop")
    public void setScrollTop(String str) {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            ((ListComponentView) viewGroup).getInnerView().scrollTo(0, (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(str), getInstance().getInstanceViewPortWidthWithFloat()), getOrientation());
        }
    }

    @WXComponentProp(name = "scrollLeft")
    public void setScrollLeft(String str) {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            ((ListComponentView) viewGroup).getInnerView().scrollTo((int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(str), getInstance().getInstanceViewPortWidthWithFloat()), 0, getOrientation());
        }
    }

    public void setIsLayoutRTL(boolean z) {
        super.setIsLayoutRTL(z);
        this.mViewOnScrollListener.setLayoutRTL(z);
    }
}
