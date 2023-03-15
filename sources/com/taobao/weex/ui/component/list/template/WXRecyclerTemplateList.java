package com.taobao.weex.ui.component.list.template;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.ICheckBindingScroller;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.el.parse.ArrayStack;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.AppearanceHelper;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXBaseRefresh;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.binding.Layouts;
import com.taobao.weex.ui.component.binding.Statements;
import com.taobao.weex.ui.component.helper.ScrollStartEndHelper;
import com.taobao.weex.ui.component.list.RecyclerTransform;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.listview.adapter.IOnLoadMoreListener;
import com.taobao.weex.ui.view.listview.adapter.IRecyclerAdapterListener;
import com.taobao.weex.ui.view.listview.adapter.RecyclerViewBaseAdapter;
import com.taobao.weex.ui.view.listview.adapter.WXRecyclerViewOnScrollListener;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component(lazyload = false)
public class WXRecyclerTemplateList extends WXVContainer<BounceRecyclerView> implements IRecyclerAdapterListener<TemplateViewHolder>, IOnLoadMoreListener, Scrollable {
    private static final long APPEAR_CHANGE_RUNNABLE_DELAY = 50;
    private static final String EMPTY_HOLDER_TEMPLATE_KEY = "";
    public static final boolean ENABLE_TRACE_LOG = false;
    private static final String NAME_HAS_FIXED_SIZE = "hasFixedSize";
    private static final String NAME_ITEM_VIEW_CACHE_SIZE = "itemViewCacheSize";
    private static final String NAME_TEMPLATE_CACHE_SIZE = "templateCacheSize";
    public static final String TAG = "WXRecyclerTemplateList";
    /* access modifiers changed from: private */
    public CellDataManager cellDataManager;
    private CellRenderContext cellRenderContext = new CellRenderContext();
    private WXCell defaultTemplateCell;
    private String defaultTemplateKey = "@default_template_cell";
    private boolean hasAppendTreeDone = false;
    private boolean hasLayoutDone = false;
    private boolean isScrollable = true;
    private String listDataIndexKey = null;
    private String listDataItemKey = null;
    private String listDataKey = Constants.Name.Recycler.LIST_DATA;
    private String listDataTemplateKey = Constants.Name.Recycler.SLOT_TEMPLATE_CASE;
    private Runnable listUpdateRunnable;
    /* access modifiers changed from: private */
    public Runnable mAppearChangeRunnable = null;
    private ArrayMap<Integer, List<AppearanceHelper>> mAppearHelpers = new ArrayMap<>();
    protected int mColumnCount = 1;
    protected float mColumnGap = 0.0f;
    protected float mColumnWidth = 0.0f;
    private ArrayMap<Integer, Map<String, Map<Integer, List<Object>>>> mDisAppearWatchList = new ArrayMap<>();
    private boolean mForceLoadmoreNextTime = false;
    private boolean mHasAddScrollEvent = false;
    private RecyclerView.ItemAnimator mItemAnimator;
    private Point mLastReport = new Point(-1, -1);
    protected int mLayoutType = 1;
    private int mListCellCount = 0;
    private int mOffsetAccuracy = 10;
    private float mPaddingLeft;
    private float mPaddingRight;
    private ScrollStartEndHelper mScrollStartEndHelper;
    /* access modifiers changed from: private */
    public TemplateStickyHelper mStickyHelper;
    private Map<String, WXCell> mTemplateSources;
    private ArrayMap<String, Integer> mTemplateViewTypes;
    private ConcurrentHashMap<String, TemplateCache> mTemplatesCache;
    /* access modifiers changed from: private */
    public WXRecyclerViewOnScrollListener mViewOnScrollListener = new WXRecyclerViewOnScrollListener(this);
    private int orientation = 1;
    private int templateCacheSize = 2;

    public void addSubView(View view, int i) {
    }

    /* access modifiers changed from: protected */
    public int getChildrenLayoutTopOffset() {
        return 0;
    }

    public boolean onFailedToRecycleView(TemplateViewHolder templateViewHolder) {
        return false;
    }

    public void onViewRecycled(TemplateViewHolder templateViewHolder) {
    }

    public WXRecyclerTemplateList(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        initRecyclerTemplateList(wXSDKInstance, basicComponentData, wXVContainer);
    }

    private void initRecyclerTemplateList(WXSDKInstance wXSDKInstance, BasicComponentData basicComponentData, WXVContainer wXVContainer) {
        updateRecyclerAttr();
        ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
        this.mTemplateViewTypes = arrayMap;
        arrayMap.put("", 0);
        this.mTemplateSources = new HashMap();
        this.mTemplatesCache = new ConcurrentHashMap<>();
        this.mStickyHelper = new TemplateStickyHelper(this);
        this.orientation = basicComponentData.getAttrs().getOrientation();
        this.listDataTemplateKey = WXUtils.getString(getAttrs().get(Constants.Name.Recycler.LIST_DATA_TEMPLATE_SWITCH_KEY), Constants.Name.Recycler.SLOT_TEMPLATE_CASE);
        this.listDataItemKey = WXUtils.getString(getAttrs().get(Constants.Name.Recycler.LIST_DATA_ITEM), this.listDataItemKey);
        this.listDataIndexKey = WXUtils.getString(getAttrs().get("index"), this.listDataIndexKey);
        CellDataManager cellDataManager2 = new CellDataManager(this);
        this.cellDataManager = cellDataManager2;
        cellDataManager2.listData = parseListDataToJSONArray(getAttrs().get(Constants.Name.Recycler.LIST_DATA));
    }

    /* access modifiers changed from: protected */
    public BounceRecyclerView initComponentHostView(Context context) {
        BounceRecyclerView bounceRecyclerView = new BounceRecyclerView(context, this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        WXAttr attrs = getAttrs();
        String str = (String) attrs.get("transform");
        if (str != null) {
            ((WXRecyclerView) bounceRecyclerView.getInnerView()).addItemDecoration(RecyclerTransform.parseTransforms(getOrientation(), str));
        }
        this.mItemAnimator = ((WXRecyclerView) bounceRecyclerView.getInnerView()).getItemAnimator();
        if (attrs.get(NAME_TEMPLATE_CACHE_SIZE) != null) {
            this.templateCacheSize = WXUtils.getInteger(attrs.get(NAME_TEMPLATE_CACHE_SIZE), Integer.valueOf(this.templateCacheSize)).intValue();
        }
        int numberInt = attrs.get(NAME_ITEM_VIEW_CACHE_SIZE) != null ? WXUtils.getNumberInt(getAttrs().get(NAME_ITEM_VIEW_CACHE_SIZE), 2) : 2;
        boolean booleanValue = attrs.get("hasFixedSize") != null ? WXUtils.getBoolean(attrs.get("hasFixedSize"), false).booleanValue() : false;
        RecyclerViewBaseAdapter recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(this);
        recyclerViewBaseAdapter.setHasStableIds(true);
        ((WXRecyclerView) bounceRecyclerView.getInnerView()).setItemAnimator((RecyclerView.ItemAnimator) null);
        if (numberInt != 2) {
            ((WXRecyclerView) bounceRecyclerView.getInnerView()).setItemViewCacheSize(numberInt);
        }
        if (bounceRecyclerView.getSwipeLayout() != null && WXUtils.getBoolean(getAttrs().get(Constants.Name.NEST_SCROLLING_ENABLED), false).booleanValue()) {
            bounceRecyclerView.getSwipeLayout().setNestedScrollingEnabled(true);
        }
        ((WXRecyclerView) bounceRecyclerView.getInnerView()).setHasFixedSize(booleanValue);
        bounceRecyclerView.setRecyclerViewBaseAdapter(recyclerViewBaseAdapter);
        bounceRecyclerView.setOverScrollMode(2);
        ((WXRecyclerView) bounceRecyclerView.getInnerView()).clearOnScrollListeners();
        ((WXRecyclerView) bounceRecyclerView.getInnerView()).addOnScrollListener(this.mViewOnScrollListener);
        ((WXRecyclerView) bounceRecyclerView.getInnerView()).addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                View childAt;
                super.onScrollStateChanged(recyclerView, i);
                WXRecyclerTemplateList.this.getScrollStartEndHelper().onScrollStateChanged(i);
                List<OnWXScrollListener> wXScrollListeners = WXRecyclerTemplateList.this.getInstance().getWXScrollListeners();
                if (wXScrollListeners != null && wXScrollListeners.size() > 0) {
                    for (OnWXScrollListener next : wXScrollListeners) {
                        if (!(next == null || (childAt = recyclerView.getChildAt(0)) == null)) {
                            next.onScrollStateChanged(recyclerView, 0, childAt.getTop(), i);
                        }
                    }
                }
            }

            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                List<OnWXScrollListener> wXScrollListeners = WXRecyclerTemplateList.this.getInstance().getWXScrollListeners();
                if (wXScrollListeners != null && wXScrollListeners.size() > 0) {
                    try {
                        for (OnWXScrollListener next : wXScrollListeners) {
                            if (next != null) {
                                if (!(next instanceof ICheckBindingScroller)) {
                                    next.onScrolled(recyclerView, i, i2);
                                } else if (((ICheckBindingScroller) next).isNeedScroller(WXRecyclerTemplateList.this.getRef(), (Object) null)) {
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
        bounceRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) WXRecyclerTemplateList.this.getHostView();
                if (bounceRecyclerView != null) {
                    WXRecyclerTemplateList.this.mViewOnScrollListener.onScrolled((RecyclerView) bounceRecyclerView.getInnerView(), 0, 0);
                    if (Build.VERSION.SDK_INT >= 16) {
                        bounceRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        bounceRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
        this.listUpdateRunnable = new Runnable() {
            public void run() {
                if (WXRecyclerTemplateList.this.mStickyHelper != null && WXRecyclerTemplateList.this.mStickyHelper.getStickyTypes().size() > 0) {
                    WXRecyclerTemplateList.this.mStickyHelper.getStickyPositions().clear();
                    if (WXRecyclerTemplateList.this.cellDataManager.listData != null) {
                        for (int i = 0; i < WXRecyclerTemplateList.this.cellDataManager.listData.size(); i++) {
                            WXCell sourceTemplate = WXRecyclerTemplateList.this.getSourceTemplate(i);
                            if (sourceTemplate != null && sourceTemplate.isSticky()) {
                                WXRecyclerTemplateList.this.mStickyHelper.getStickyPositions().add(Integer.valueOf(i));
                            }
                        }
                    }
                }
                if (!(WXRecyclerTemplateList.this.getHostView() == null || ((BounceRecyclerView) WXRecyclerTemplateList.this.getHostView()).getRecyclerViewBaseAdapter() == null)) {
                    ((BounceRecyclerView) WXRecyclerTemplateList.this.getHostView()).getRecyclerViewBaseAdapter().notifyDataSetChanged();
                }
                WXEnvironment.isOpenDebugLog();
            }
        };
        return bounceRecyclerView;
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(BounceRecyclerView bounceRecyclerView) {
        super.onHostViewInitialized(bounceRecyclerView);
        WXRecyclerView wXRecyclerView = (WXRecyclerView) bounceRecyclerView.getInnerView();
        if (wXRecyclerView == null || wXRecyclerView.getAdapter() == null) {
            WXLogUtils.e(TAG, "RecyclerView is not found or Adapter is not bound");
        }
    }

    /* access modifiers changed from: protected */
    public WXComponent.MeasureOutput measure(int i, int i2) {
        int screenHeight = WXViewUtils.getScreenHeight((Context) WXEnvironment.sApplication);
        int weexHeight = WXViewUtils.getWeexHeight(getInstanceId());
        if (weexHeight < screenHeight) {
            screenHeight = weexHeight;
        }
        if (i2 > screenHeight) {
            i2 = weexHeight - getAbsoluteY();
        }
        return super.measure(i, i2);
    }

    public void bindStickStyle(WXComponent wXComponent) {
        TemplateStickyHelper templateStickyHelper;
        WXComponent findParentType = findParentType(wXComponent, WXCell.class);
        if (findParentType != null && (templateStickyHelper = this.mStickyHelper) != null && !templateStickyHelper.getStickyTypes().contains(findParentType.getRef())) {
            this.mStickyHelper.getStickyTypes().add(findParentType.getRef());
            notifyUpdateList();
        }
    }

    public void unbindStickStyle(WXComponent wXComponent) {
        TemplateStickyHelper templateStickyHelper;
        WXComponent findParentType = findParentType(wXComponent, WXCell.class);
        if (findParentType != null && (templateStickyHelper = this.mStickyHelper) != null && templateStickyHelper.getStickyTypes().contains(findParentType.getRef())) {
            this.mStickyHelper.getStickyTypes().remove(findParentType.getRef());
            notifyUpdateList();
        }
    }

    private WXCell findCell(WXComponent wXComponent) {
        WXVContainer parent;
        if (wXComponent instanceof WXCell) {
            return (WXCell) wXComponent;
        }
        if (wXComponent == null || (parent = wXComponent.getParent()) == null) {
            return null;
        }
        return findCell(parent);
    }

    private void setAppearanceWatch(WXComponent wXComponent, int i, boolean z) {
        int cellTemplateItemType;
        if (this.cellDataManager.listData != null && this.mAppearHelpers != null && !TextUtils.isEmpty(wXComponent.getRef()) && (cellTemplateItemType = getCellTemplateItemType(findCell(wXComponent))) >= 0) {
            List list = this.mAppearHelpers.get(Integer.valueOf(cellTemplateItemType));
            if (list == null) {
                list = new ArrayList();
                this.mAppearHelpers.put(Integer.valueOf(cellTemplateItemType), list);
            }
            AppearanceHelper appearanceHelper = null;
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                AppearanceHelper appearanceHelper2 = (AppearanceHelper) it.next();
                if (wXComponent.getRef().equals(appearanceHelper2.getAwareChild().getRef())) {
                    appearanceHelper = appearanceHelper2;
                    break;
                }
            }
            if (appearanceHelper != null) {
                appearanceHelper.setWatchEvent(i, z);
                return;
            }
            AppearanceHelper appearanceHelper3 = new AppearanceHelper(wXComponent, cellTemplateItemType);
            appearanceHelper3.setWatchEvent(i, z);
            list.add(appearanceHelper3);
        }
    }

    public void bindAppearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 0, true);
        if (this.mAppearChangeRunnable == null) {
            this.mAppearChangeRunnable = new Runnable() {
                public void run() {
                    if (WXRecyclerTemplateList.this.mAppearChangeRunnable != null) {
                        WXRecyclerTemplateList.this.notifyAppearStateChange(0, 0, 0, 0);
                    }
                }
            };
        }
        if (getHostView() != null) {
            ((BounceRecyclerView) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            ((BounceRecyclerView) getHostView()).postDelayed(this.mAppearChangeRunnable, APPEAR_CHANGE_RUNNABLE_DELAY);
        }
    }

    public void bindDisappearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 1, true);
        if (this.mAppearChangeRunnable == null) {
            this.mAppearChangeRunnable = new Runnable() {
                public void run() {
                    if (WXRecyclerTemplateList.this.mAppearChangeRunnable != null) {
                        WXRecyclerTemplateList.this.notifyAppearStateChange(0, 0, 0, 0);
                    }
                }
            };
        }
        if (getHostView() != null) {
            ((BounceRecyclerView) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            ((BounceRecyclerView) getHostView()).postDelayed(this.mAppearChangeRunnable, APPEAR_CHANGE_RUNNABLE_DELAY);
        }
    }

    public void unbindAppearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 0, false);
    }

    public void unbindDisappearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 1, false);
    }

    @JSMethod(uiThread = true)
    public void queryElement(String str, String str2, JSCallback jSCallback) {
        try {
            String[] split = str.split("@");
            String str3 = split[0];
            int parseInt = Integer.parseInt(split[1]);
            WXComponent findVirtualComponentByVRef = TemplateDom.findVirtualComponentByVRef(getInstanceId(), str);
            if (findVirtualComponentByVRef != null && getHostView() != null) {
                if (((BounceRecyclerView) getHostView()).getInnerView() != null) {
                    ArrayList arrayList = new ArrayList(4);
                    Selector.queryElementAll(findVirtualComponentByVRef, str2, arrayList);
                    if (arrayList.size() > 0) {
                        jSCallback.invoke(TemplateDom.toMap(str3, parseInt, (WXComponent) arrayList.get(0)));
                    } else {
                        jSCallback.invoke(new HashMap(4));
                    }
                }
            }
        } catch (Exception e) {
            jSCallback.invoke(new HashMap(4));
            WXLogUtils.e(TAG, (Throwable) e);
        }
    }

    @JSMethod(uiThread = true)
    public void queryElementAll(String str, String str2, JSCallback jSCallback) {
        ArrayList arrayList = new ArrayList();
        try {
            String[] split = str.split("@");
            String str3 = split[0];
            int parseInt = Integer.parseInt(split[1]);
            WXComponent findVirtualComponentByVRef = TemplateDom.findVirtualComponentByVRef(getInstanceId(), str);
            if (findVirtualComponentByVRef != null && getHostView() != null) {
                if (((BounceRecyclerView) getHostView()).getInnerView() != null) {
                    ArrayList<WXComponent> arrayList2 = new ArrayList<>(4);
                    Selector.queryElementAll(findVirtualComponentByVRef, str2, arrayList2);
                    for (WXComponent map : arrayList2) {
                        arrayList.add(TemplateDom.toMap(str3, parseInt, map));
                    }
                    jSCallback.invoke(arrayList);
                }
            }
        } catch (Exception e) {
            jSCallback.invoke(arrayList);
            WXLogUtils.e(TAG, (Throwable) e);
        }
    }

    @JSMethod(uiThread = true)
    public void closest(String str, String str2, JSCallback jSCallback) {
        try {
            String[] split = str.split("@");
            String str3 = split[0];
            int parseInt = Integer.parseInt(split[1]);
            WXComponent findVirtualComponentByVRef = TemplateDom.findVirtualComponentByVRef(getInstanceId(), str);
            if (findVirtualComponentByVRef != null && getHostView() != null) {
                if (((BounceRecyclerView) getHostView()).getInnerView() != null) {
                    ArrayList arrayList = new ArrayList(4);
                    Selector.closest(findVirtualComponentByVRef, str2, arrayList);
                    if (arrayList.size() > 0) {
                        jSCallback.invoke(TemplateDom.toMap(str3, parseInt, (WXComponent) arrayList.get(0)));
                    } else {
                        jSCallback.invoke(new HashMap(4));
                    }
                }
            }
        } catch (Exception e) {
            jSCallback.invoke(new HashMap(4));
            WXLogUtils.e(TAG, (Throwable) e);
        }
    }

    @JSMethod(uiThread = true)
    public void scrollToElement(String str, Map<String, Object> map) {
        scrollTo(str, map);
    }

    @JSMethod(uiThread = true)
    public void scrollTo(String str, Map<String, Object> map) {
        int i;
        try {
            if (str.indexOf(64) > 0) {
                i = Integer.parseInt(str.split("@")[0]);
            } else {
                i = (int) Float.parseFloat(str);
            }
            if (i >= 0) {
                float f = 0.0f;
                boolean z = true;
                if (map != null) {
                    WXUtils.getBoolean(map.get(Constants.Name.ANIMATED), true).booleanValue();
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
                int i2 = (int) f;
                BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) getHostView();
                if (bounceRecyclerView != null) {
                    ((WXRecyclerView) bounceRecyclerView.getInnerView()).scrollTo(z, i, i2, getOrientation());
                }
            }
        } catch (Exception e2) {
            WXLogUtils.e(TAG, (Throwable) e2);
        }
    }

    public void scrollTo(WXComponent wXComponent, Map<String, Object> map) {
        boolean z;
        int i;
        int i2 = -1;
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
            i = WXUtils.getNumberInt(map.get(Constants.Name.Recycler.CELL_INDEX), -1);
            i2 = WXUtils.getNumberInt(map.get(Constants.Name.Recycler.TYPE_INDEX), -1);
        } else {
            i = -1;
            z = true;
        }
        WXCell findCell = findCell(wXComponent);
        if (!(i2 < 0 || this.cellDataManager.listData == null || wXComponent.getRef() == null)) {
            int i3 = 0;
            int i4 = 0;
            while (true) {
                if (i3 >= this.cellDataManager.listData.size()) {
                    break;
                }
                WXCell sourceTemplate = getSourceTemplate(i3);
                if (sourceTemplate != null) {
                    if (findCell.getRef().equals(sourceTemplate.getRef())) {
                        i4++;
                    }
                    if (i4 > i2) {
                        i = i3;
                        break;
                    }
                }
                i3++;
            }
            if (i < 0) {
                i = this.cellDataManager.listData.size() - 1;
            }
        }
        int i5 = (int) f;
        BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) getHostView();
        if (bounceRecyclerView != null && i >= 0) {
            ((WXRecyclerView) bounceRecyclerView.getInnerView()).scrollTo(z, i, i5, getOrientation());
        }
    }

    public int getScrollY() {
        BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) getHostView();
        if (bounceRecyclerView == null) {
            return 0;
        }
        return ((WXRecyclerView) bounceRecyclerView.getInnerView()).getScrollY();
    }

    public int getScrollX() {
        BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) getHostView();
        if (bounceRecyclerView == null) {
            return 0;
        }
        return ((WXRecyclerView) bounceRecyclerView.getInnerView()).getScrollX();
    }

    public int getOrientation() {
        return this.orientation;
    }

    public boolean isScrollable() {
        return this.isScrollable;
    }

    public void addChild(WXComponent wXComponent) {
        addChild(wXComponent, -1);
    }

    public void addChild(WXComponent wXComponent, int i) {
        boolean z = wXComponent instanceof WXCell;
        if (!z) {
            super.addChild(wXComponent, i);
        }
        if (!(wXComponent instanceof WXBaseRefresh) && z) {
            if (wXComponent.getAttrs() != null) {
                String string = WXUtils.getString(wXComponent.getAttrs().get(Constants.Name.Recycler.SLOT_TEMPLATE_CASE), (String) null);
                if (getAttrs().containsKey(Constants.Name.Recycler.LIST_DATA_TEMPLATE_SWITCH_KEY)) {
                    if (this.defaultTemplateCell == null) {
                        this.defaultTemplateCell = (WXCell) wXComponent;
                        if (!TextUtils.isEmpty(string)) {
                            this.defaultTemplateKey = string;
                        } else {
                            string = this.defaultTemplateKey;
                            wXComponent.getAttrs().put(Constants.Name.Recycler.SLOT_TEMPLATE_CASE, (Object) string);
                        }
                    }
                } else if (this.defaultTemplateCell == null || wXComponent.getAttrs().containsKey("default")) {
                    this.defaultTemplateCell = (WXCell) wXComponent;
                    if (!TextUtils.isEmpty(string)) {
                        this.defaultTemplateKey = string;
                    } else {
                        string = this.defaultTemplateKey;
                        wXComponent.getAttrs().put(Constants.Name.Recycler.SLOT_TEMPLATE_CASE, (Object) string);
                    }
                }
                if (string != null) {
                    this.mTemplateSources.put(string, (WXCell) wXComponent);
                    if (this.mTemplateViewTypes.get(string) == null) {
                        ArrayMap<String, Integer> arrayMap = this.mTemplateViewTypes;
                        arrayMap.put(string, Integer.valueOf(arrayMap.size()));
                    }
                }
            }
            ((WXCell) wXComponent).setCellAppendTreeListener(new WXCell.CellAppendTreeListener() {
                public void onAppendTreeDone() {
                    WXRecyclerTemplateList.this.checkAppendDone(false);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void checkAppendDone(boolean z) {
        if (this.mTemplateSources.size() != 0) {
            for (Map.Entry<String, WXCell> value : this.mTemplateSources.entrySet()) {
                if (!((WXCell) value.getValue()).isAppendTreeDone()) {
                    return;
                }
            }
            this.hasAppendTreeDone = true;
            if (this.hasLayoutDone) {
                notifyUpdateList();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(BounceRecyclerView bounceRecyclerView, int i, int i2, int i3, int i4, int i5, int i6) {
        super.setHostLayoutParams(bounceRecyclerView, i, i2, i3, i4, i5, i6);
        if (!this.hasLayoutDone) {
            this.hasLayoutDone = true;
            this.hasAppendTreeDone = true;
            notifyUpdateList();
        }
    }

    public void createChildViewAt(int i) {
        if (i >= 0 || childCount() - 1 >= 0) {
            WXComponent child = getChild(i);
            if (child instanceof WXBaseRefresh) {
                child.createView();
                setRefreshOrLoading(child);
            }
        }
    }

    public void remove(WXComponent wXComponent, boolean z) {
        removeFooterOrHeader(wXComponent);
        super.remove(wXComponent, z);
    }

    public void computeVisiblePointInViewCoordinate(PointF pointF) {
        RecyclerView recyclerView = (RecyclerView) ((BounceRecyclerView) getHostView()).getInnerView();
        pointF.set((float) recyclerView.computeHorizontalScrollOffset(), (float) recyclerView.computeVerticalScrollOffset());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00e5, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00f4, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r6, java.lang.Object r7) {
        /*
            r5 = this;
            r6.hashCode()
            int r0 = r6.hashCode()
            r1 = 10
            java.lang.String r2 = "case"
            r3 = 1
            r4 = -1
            switch(r0) {
                case -889473228: goto L_0x0094;
                case -713683669: goto L_0x0089;
                case -338674661: goto L_0x007e;
                case -223520855: goto L_0x0073;
                case -112563826: goto L_0x0068;
                case -5620052: goto L_0x005d;
                case 3046192: goto L_0x0054;
                case 66669991: goto L_0x0049;
                case 92902992: goto L_0x003c;
                case 100346066: goto L_0x002e;
                case 1345164648: goto L_0x0020;
                case 1614714674: goto L_0x0012;
                default: goto L_0x0010;
            }
        L_0x0010:
            goto L_0x009e
        L_0x0012:
            java.lang.String r0 = "scrollDirection"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x001c
            goto L_0x009e
        L_0x001c:
            r4 = 11
            goto L_0x009e
        L_0x0020:
            java.lang.String r0 = "listData"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x002a
            goto L_0x009e
        L_0x002a:
            r4 = 10
            goto L_0x009e
        L_0x002e:
            java.lang.String r0 = "index"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0038
            goto L_0x009e
        L_0x0038:
            r4 = 9
            goto L_0x009e
        L_0x003c:
            java.lang.String r0 = "alias"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0046
            goto L_0x009e
        L_0x0046:
            r4 = 8
            goto L_0x009e
        L_0x0049:
            java.lang.String r0 = "scrollable"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0052
            goto L_0x009e
        L_0x0052:
            r4 = 7
            goto L_0x009e
        L_0x0054:
            boolean r0 = r6.equals(r2)
            if (r0 != 0) goto L_0x005b
            goto L_0x009e
        L_0x005b:
            r4 = 6
            goto L_0x009e
        L_0x005d:
            java.lang.String r0 = "offsetAccuracy"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0066
            goto L_0x009e
        L_0x0066:
            r4 = 5
            goto L_0x009e
        L_0x0068:
            java.lang.String r0 = "loadmoreoffset"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0071
            goto L_0x009e
        L_0x0071:
            r4 = 4
            goto L_0x009e
        L_0x0073:
            java.lang.String r0 = "showScrollbar"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x007c
            goto L_0x009e
        L_0x007c:
            r4 = 3
            goto L_0x009e
        L_0x007e:
            java.lang.String r0 = "hasFixedSize"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0087
            goto L_0x009e
        L_0x0087:
            r4 = 2
            goto L_0x009e
        L_0x0089:
            java.lang.String r0 = "itemViewCacheSize"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x0092
            goto L_0x009e
        L_0x0092:
            r4 = 1
            goto L_0x009e
        L_0x0094:
            java.lang.String r0 = "switch"
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L_0x009d
            goto L_0x009e
        L_0x009d:
            r4 = 0
        L_0x009e:
            switch(r4) {
                case 0: goto L_0x00f5;
                case 1: goto L_0x00f4;
                case 2: goto L_0x00f4;
                case 3: goto L_0x00e6;
                case 4: goto L_0x00e5;
                case 5: goto L_0x00d6;
                case 6: goto L_0x00f5;
                case 7: goto L_0x00c6;
                case 8: goto L_0x00bd;
                case 9: goto L_0x00b4;
                case 10: goto L_0x00b0;
                case 11: goto L_0x00a6;
                default: goto L_0x00a1;
            }
        L_0x00a1:
            boolean r6 = super.setProperty(r6, r7)
            return r6
        L_0x00a6:
            if (r7 == 0) goto L_0x00af
            java.lang.String r6 = r7.toString()
            r5.setScrollDirection(r6)
        L_0x00af:
            return r3
        L_0x00b0:
            r5.setListData(r7)
            return r3
        L_0x00b4:
            java.lang.String r6 = r5.listDataIndexKey
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r6)
            r5.listDataIndexKey = r6
            return r3
        L_0x00bd:
            java.lang.String r6 = r5.listDataItemKey
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r6)
            r5.listDataItemKey = r6
            return r3
        L_0x00c6:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r6)
            boolean r6 = r6.booleanValue()
            r5.setScrollable(r6)
            return r3
        L_0x00d6:
            java.lang.Integer r6 = java.lang.Integer.valueOf(r1)
            java.lang.Integer r6 = com.taobao.weex.utils.WXUtils.getInteger(r7, r6)
            int r6 = r6.intValue()
            r5.setOffsetAccuracy(r6)
        L_0x00e5:
            return r3
        L_0x00e6:
            r6 = 0
            java.lang.Boolean r6 = com.taobao.weex.utils.WXUtils.getBoolean(r7, r6)
            if (r6 == 0) goto L_0x00f4
            boolean r6 = r6.booleanValue()
            r5.setShowScrollbar(r6)
        L_0x00f4:
            return r3
        L_0x00f5:
            java.lang.String r6 = com.taobao.weex.utils.WXUtils.getString(r7, r2)
            r5.listDataTemplateKey = r6
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "offsetAccuracy")
    public void setOffsetAccuracy(int i) {
        this.mOffsetAccuracy = (int) WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidthWithFloat());
    }

    private void updateRecyclerAttr() {
        this.mLayoutType = getAttrs().getLayoutType();
        int columnCount = getAttrs().getColumnCount();
        this.mColumnCount = columnCount;
        if (columnCount <= 0 && this.mLayoutType != 1) {
            ArrayMap arrayMap = new ArrayMap();
            arrayMap.put("componentType", getComponentType());
            arrayMap.put("attribute", getAttrs().toString());
            arrayMap.put("stackTrace", Arrays.toString(Thread.currentThread().getStackTrace()));
            WXExceptionUtils.commitCriticalExceptionRT(getInstanceId(), WXErrorCode.WX_RENDER_ERR_LIST_INVALID_COLUMN_COUNT, Constants.Name.COLUMN_COUNT, String.format(Locale.ENGLISH, "You are trying to set the list/recycler/vlist/waterfall's column to %d, which is illegal. The column count should be a positive integer", new Object[]{Integer.valueOf(this.mColumnCount)}), arrayMap);
            this.mColumnCount = 1;
        }
        this.mColumnGap = getAttrs().getColumnGap();
        this.mColumnWidth = getAttrs().getColumnWidth();
        this.mPaddingLeft = getPadding().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT);
        this.mPaddingRight = getPadding().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT);
    }

    @WXComponentProp(name = "scrollDirection")
    public void setScrollDirection(String str) {
        if (this.orientation != getAttrs().getOrientation()) {
            this.orientation = getAttrs().getOrientation();
            updateRecyclerAttr();
            WXRecyclerView wXRecyclerView = (WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView();
            if (this.orientation == 0) {
                wXRecyclerView.setHorizontalScrollBarEnabled(true);
            } else {
                wXRecyclerView.setHorizontalScrollBarEnabled(false);
            }
            wXRecyclerView.initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "columnWidth")
    public void setColumnWidth(int i) {
        if (getAttrs().getColumnWidth() != this.mColumnWidth) {
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "showScrollbar")
    public void setShowScrollbar(boolean z) {
        if (getHostView() != null && ((BounceRecyclerView) getHostView()).getInnerView() != null) {
            if (getOrientation() == 1) {
                ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).setVerticalScrollBarEnabled(z);
            } else {
                ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).setHorizontalScrollBarEnabled(z);
            }
        }
    }

    @WXComponentProp(name = "columnCount")
    public void setColumnCount(int i) {
        if (getAttrs().getColumnCount() != this.mColumnCount) {
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "columnGap")
    public void setColumnGap(float f) throws InterruptedException {
        if (getAttrs().getColumnGap() != this.mColumnGap) {
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).setScrollable(z);
    }

    @JSMethod
    public void setListData(Object obj) {
        JSONArray parseListDataToJSONArray = parseListDataToJSONArray(obj);
        boolean z = this.cellDataManager.listData != parseListDataToJSONArray;
        if ((parseListDataToJSONArray instanceof JSONArray) && z) {
            this.cellDataManager.setListData(parseListDataToJSONArray);
            notifyUpdateList();
        }
    }

    @JSMethod
    public void appendData(JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.size() != 0) {
            if (this.cellDataManager.listData == null) {
                this.cellDataManager.listData = new JSONArray();
            }
            int size = this.cellDataManager.listData.size();
            if (size < 0) {
                size = 0;
            }
            if (jSONArray instanceof JSONArray) {
                this.cellDataManager.listData.addAll(jSONArray);
            }
            ((BounceRecyclerView) getHostView()).getRecyclerViewBaseAdapter().notifyItemRangeInserted(size, jSONArray.size());
        }
    }

    @JSMethod
    public void insertData(int i, Object obj) {
        if (obj != null && this.cellDataManager.listData != null && i <= this.cellDataManager.listData.size()) {
            if (this.cellDataManager.insertData(i, obj)) {
                notifyUpdateList();
            } else {
                ((BounceRecyclerView) getHostView()).getRecyclerViewBaseAdapter().notifyItemInserted(i);
            }
        }
    }

    @JSMethod
    public void appendRange(int i, JSONArray jSONArray) {
        insertRange(i, jSONArray);
    }

    @JSMethod
    public void insertRange(int i, JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.size() != 0 && this.cellDataManager.listData != null && i <= this.cellDataManager.listData.size()) {
            if (this.cellDataManager.insertRange(i, jSONArray)) {
                notifyUpdateList();
            } else {
                ((BounceRecyclerView) getHostView()).getRecyclerViewBaseAdapter().notifyItemRangeInserted(i, jSONArray.size());
            }
        }
    }

    @JSMethod
    public void updateData(int i, Object obj) {
        if (obj != null && this.cellDataManager.listData != null && i < this.cellDataManager.listData.size()) {
            if (this.cellDataManager.updateData(obj, i)) {
                ((BounceRecyclerView) getHostView()).getRecyclerViewBaseAdapter().notifyItemChanged(i, obj);
            } else {
                notifyUpdateList();
            }
        }
    }

    @JSMethod
    public void removeData(int i, int i2) {
        if (this.cellDataManager.listData != null && i < this.cellDataManager.listData.size()) {
            if (i2 <= 0) {
                i2 = 1;
            }
            int i3 = 0;
            while (i2 > 0 && i < this.cellDataManager.listData.size()) {
                this.cellDataManager.removeData(Integer.valueOf(i));
                i2--;
                i3++;
            }
            if (i3 > 0) {
                notifyUpdateList();
            }
        }
    }

    @JSMethod
    public void resetLoadmore() {
        this.mForceLoadmoreNextTime = true;
        this.mListCellCount = 0;
    }

    public void updateProperties(Map<String, Object> map) {
        super.updateProperties(map);
        if (!map.containsKey("padding") && !map.containsKey(Constants.Name.PADDING_LEFT) && !map.containsKey(Constants.Name.PADDING_RIGHT)) {
            return;
        }
        if (this.mPaddingLeft != getPadding().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT) || this.mPaddingRight != getPadding().get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT)) {
            updateRecyclerAttr();
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).initView(getContext(), this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        }
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (ScrollStartEndHelper.isScrollEvent(str) && getHostView() != null && ((BounceRecyclerView) getHostView()).getInnerView() != null && !this.mHasAddScrollEvent) {
            this.mHasAddScrollEvent = true;
            ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean mFirstEvent = true;
                private int offsetXCorrection;
                private int offsetYCorrection;

                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    int i3;
                    int i4;
                    super.onScrolled(recyclerView, i, i2);
                    if (recyclerView.getLayoutManager().canScrollVertically()) {
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
                        WXRecyclerTemplateList.this.getScrollStartEndHelper().onScrolled(i4, i3);
                        if (WXRecyclerTemplateList.this.getEvents().contains("scroll")) {
                            if (this.mFirstEvent) {
                                this.mFirstEvent = false;
                            } else if (WXRecyclerTemplateList.this.shouldReport(i4, i3)) {
                                WXRecyclerTemplateList.this.fireScrollEvent(recyclerView, i4, i3);
                            }
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fireScrollEvent(RecyclerView recyclerView, int i, int i2) {
        fireEvent("scroll", getScrollEvent(recyclerView, i, i2));
    }

    public Map<String, Object> getScrollEvent(RecyclerView recyclerView, int i, int i2) {
        int measuredWidth = recyclerView.getMeasuredWidth() + recyclerView.computeHorizontalScrollRange();
        int calcContentSize = calcContentSize();
        HashMap hashMap = new HashMap(3);
        HashMap hashMap2 = new HashMap(3);
        HashMap hashMap3 = new HashMap(3);
        hashMap2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) measuredWidth, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) calcContentSize, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap3.put(Constants.Name.X, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i, getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap3.put(Constants.Name.Y, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) (-calcContentOffset(recyclerView)), getInstance().getInstanceViewPortWidthWithFloat())));
        hashMap.put(Constants.Name.CONTENT_SIZE, hashMap2);
        hashMap.put(Constants.Name.CONTENT_OFFSET, hashMap3);
        boolean z = true;
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

    private boolean setRefreshOrLoading(final WXComponent wXComponent) {
        if ((wXComponent instanceof WXRefresh) && getHostView() != null) {
            ((BounceRecyclerView) getHostView()).setOnRefreshListener((WXRefresh) wXComponent);
            ((BounceRecyclerView) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    ((BounceRecyclerView) WXRecyclerTemplateList.this.getHostView()).setHeaderView(wXComponent);
                }
            }), 100);
            return true;
        } else if (!(wXComponent instanceof WXLoading) || getHostView() == null) {
            return false;
        } else {
            ((BounceRecyclerView) getHostView()).setOnLoadingListener((WXLoading) wXComponent);
            ((BounceRecyclerView) getHostView()).postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    ((BounceRecyclerView) WXRecyclerTemplateList.this.getHostView()).setFooterView(wXComponent);
                }
            }), 100);
            return true;
        }
    }

    private void removeFooterOrHeader(WXComponent wXComponent) {
        if (wXComponent instanceof WXLoading) {
            ((BounceRecyclerView) getHostView()).removeFooterView(wXComponent);
        } else if (wXComponent instanceof WXRefresh) {
            ((BounceRecyclerView) getHostView()).removeHeaderView(wXComponent);
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

    public void destroy() {
        synchronized (this) {
            if (getHostView() != null) {
                if (this.mAppearChangeRunnable != null) {
                    ((BounceRecyclerView) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
                    this.mAppearChangeRunnable = null;
                }
                ((BounceRecyclerView) getHostView()).removeCallbacks(this.listUpdateRunnable);
                if (((BounceRecyclerView) getHostView()).getInnerView() != null) {
                    ((WXRecyclerView) ((BounceRecyclerView) getHostView()).getInnerView()).setAdapter((RecyclerView.Adapter) null);
                }
            }
            if (this.cellDataManager.listData != null) {
                this.cellDataManager.setListData((JSONArray) null);
            }
            if (this.mStickyHelper != null) {
                this.mStickyHelper = null;
            }
            ArrayMap<String, Integer> arrayMap = this.mTemplateViewTypes;
            if (arrayMap != null) {
                arrayMap.clear();
            }
            Map<String, WXCell> map = this.mTemplateSources;
            if (map != null) {
                map.clear();
            }
            ArrayMap<Integer, List<AppearanceHelper>> arrayMap2 = this.mAppearHelpers;
            if (arrayMap2 != null) {
                arrayMap2.clear();
            }
            ArrayMap<Integer, Map<String, Map<Integer, List<Object>>>> arrayMap3 = this.mDisAppearWatchList;
            if (arrayMap3 != null) {
                arrayMap3.clear();
            }
            super.destroy();
        }
    }

    public void onBindViewHolder(TemplateViewHolder templateViewHolder, int i) {
        WXCell template;
        if (templateViewHolder != null && (template = templateViewHolder.getTemplate()) != null) {
            if (templateViewHolder.getHolderPosition() >= 0) {
                fireEvent(TemplateDom.DETACH_CELL_SLOT, TemplateDom.findAllComponentRefs(getRef(), i, template));
            }
            System.currentTimeMillis();
            templateViewHolder.setHolderPosition(i);
            Object obj = this.cellDataManager.listData.get(i);
            CellRenderState renderState = this.cellDataManager.getRenderState(i);
            if (template.getRenderData() != obj || (renderState != null && renderState.isDirty())) {
                Statements.doInitCompontent(doRenderTemplate(template, i));
                template.setRenderData(obj);
                Layouts.doLayoutAsync(templateViewHolder, true);
                WXEnvironment.isOpenDebugLog();
                return;
            }
            WXEnvironment.isOpenDebugLog();
            fireEvent(TemplateDom.ATTACH_CELL_SLOT, TemplateDom.findAllComponentRefs(getRef(), i, template));
        }
    }

    /* JADX WARNING: type inference failed for: r0v5, types: [com.taobao.weex.ui.component.WXComponent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.weex.ui.component.list.template.TemplateViewHolder onCreateViewHolder(android.view.ViewGroup r5, int r6) {
        /*
            r4 = this;
            androidx.collection.ArrayMap<java.lang.String, java.lang.Integer> r5 = r4.mTemplateViewTypes
            java.lang.Object r5 = r5.keyAt(r6)
            java.lang.String r5 = (java.lang.String) r5
            java.util.Map<java.lang.String, com.taobao.weex.ui.component.list.WXCell> r0 = r4.mTemplateSources
            java.lang.Object r0 = r0.get(r5)
            com.taobao.weex.ui.component.list.WXCell r0 = (com.taobao.weex.ui.component.list.WXCell) r0
            r1 = 0
            if (r0 != 0) goto L_0x002a
            android.widget.FrameLayout r5 = new android.widget.FrameLayout
            android.content.Context r0 = r4.getContext()
            r5.<init>(r0)
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            r0.<init>(r1, r1)
            r5.setLayoutParams(r0)
            com.taobao.weex.ui.component.list.template.TemplateViewHolder r0 = new com.taobao.weex.ui.component.list.template.TemplateViewHolder
            r0.<init>((com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList) r4, (android.view.View) r5, (int) r6)
            return r0
        L_0x002a:
            com.taobao.weex.ui.component.list.WXCell r2 = r4.getCellTemplateFromCache(r5)
            if (r2 != 0) goto L_0x0041
            boolean r3 = r0.isSourceUsed()
            if (r3 != 0) goto L_0x0041
            r2 = 1
            r0.setSourceUsed(r2)
            r4.renderTemplateCellWithData(r0)
            com.taobao.weex.WXEnvironment.isOpenDebugLog()
            r2 = r0
        L_0x0041:
            if (r2 != 0) goto L_0x0050
            java.lang.System.currentTimeMillis()
            com.taobao.weex.ui.component.WXComponent r0 = r4.copyComponentFromSourceCell(r0)
            r2 = r0
            com.taobao.weex.ui.component.list.WXCell r2 = (com.taobao.weex.ui.component.list.WXCell) r2
            com.taobao.weex.WXEnvironment.isOpenDebugLog()
        L_0x0050:
            boolean r0 = r2.isLazy()
            if (r0 != 0) goto L_0x0061
            android.view.View r0 = r2.getHostView()
            if (r0 != 0) goto L_0x005d
            goto L_0x0061
        L_0x005d:
            com.taobao.weex.WXEnvironment.isOpenDebugLog()
            goto L_0x0068
        L_0x0061:
            doCreateCellViewBindData(r2, r5, r1)
            boolean r5 = com.taobao.weex.WXEnvironment.isOpenDebugLog()
        L_0x0068:
            com.taobao.weex.ui.component.list.template.TemplateViewHolder r5 = new com.taobao.weex.ui.component.list.template.TemplateViewHolder
            r5.<init>((com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList) r4, (com.taobao.weex.ui.component.list.WXCell) r2, (int) r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList.onCreateViewHolder(android.view.ViewGroup, int):com.taobao.weex.ui.component.list.template.TemplateViewHolder");
    }

    public int getItemViewType(int i) {
        int indexOfKey = this.mTemplateViewTypes.indexOfKey(getTemplateKey(i));
        return indexOfKey < 0 ? this.mTemplateViewTypes.indexOfKey("") : indexOfKey;
    }

    private List<WXComponent> doRenderTemplate(WXCell wXCell, int i) {
        this.cellRenderContext.clear();
        Object obj = this.cellDataManager.listData.get(i);
        CellRenderState renderState = this.cellDataManager.getRenderState(i);
        this.cellRenderContext.renderState = renderState;
        this.cellRenderContext.templateList = this;
        this.cellRenderContext.position = i;
        ArrayStack arrayStack = this.cellRenderContext.stack;
        Map map = this.cellRenderContext.map;
        if (this.cellDataManager.listData != null) {
            arrayStack.push(map);
            map.put(this.listDataKey, this.cellDataManager.listData);
            if (!TextUtils.isEmpty(this.listDataIndexKey)) {
                map.put(this.listDataIndexKey, new PositionRef(renderState));
            }
            if (!TextUtils.isEmpty(this.listDataItemKey)) {
                map.put(this.listDataItemKey, obj);
            } else {
                arrayStack.push(obj);
            }
        }
        if (renderState.itemId <= 0) {
            getItemId(i);
        }
        List<WXComponent> doRender = Statements.doRender(wXCell, this.cellRenderContext);
        if (renderState.isDirty()) {
            renderState.resetDirty();
        }
        return doRender;
    }

    public ArrayStack copyStack(CellRenderContext cellRenderContext2, ArrayStack arrayStack) {
        ArrayStack arrayStack2 = new ArrayStack();
        for (int i = 0; i < arrayStack.size(); i++) {
            Object obj = arrayStack.get(i);
            if (obj instanceof Map) {
                obj = new HashMap((Map) obj);
            }
            arrayStack2.push(obj);
        }
        return arrayStack2;
    }

    public String getTemplateKey(int i) {
        return getTemplateKey(safeGetListData(i));
    }

    public String getTemplateKey(Object obj) {
        String string = obj instanceof JSONObject ? ((JSONObject) obj).getString(this.listDataTemplateKey) : null;
        if (TextUtils.isEmpty(string)) {
            return this.defaultTemplateCell != null ? this.defaultTemplateKey : "";
        }
        return string;
    }

    public WXCell getSourceTemplate(int i) {
        return this.mTemplateSources.get(getTemplateKey(i));
    }

    private int getCellTemplateItemType(WXCell wXCell) {
        if (wXCell == null) {
            return -1;
        }
        if (wXCell.getAttrs() == null) {
            return 0;
        }
        String string = WXUtils.getString(wXCell.getAttrs().get(Constants.Name.Recycler.SLOT_TEMPLATE_CASE), (String) null);
        if (wXCell == this.defaultTemplateCell) {
            string = this.defaultTemplateKey;
        }
        int indexOfKey = this.mTemplateViewTypes.indexOfKey(string);
        if (indexOfKey < 0) {
            return -1;
        }
        return indexOfKey;
    }

    public int getItemCount() {
        ArrayMap<String, Integer> arrayMap;
        Map<String, WXCell> map;
        if (!this.hasLayoutDone || !this.hasAppendTreeDone || this.cellDataManager.listData == null || (arrayMap = this.mTemplateViewTypes) == null || arrayMap.size() <= 1 || (map = this.mTemplateSources) == null || map.size() == 0) {
            return 0;
        }
        return this.cellDataManager.listData.size();
    }

    public long getItemId(int i) {
        CellRenderState renderState = this.cellDataManager.getRenderState(i);
        if (renderState.itemId <= 0) {
            if (TextUtils.isEmpty(getTemplateKey(i))) {
                return -1;
            }
            Object safeGetListData = safeGetListData(i);
            if (safeGetListData instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) safeGetListData;
                if (jSONObject.containsKey("keyItemId")) {
                    renderState.itemId = jSONObject.getLongValue("keyItemId");
                }
            }
            renderState.itemId = (((long) Math.abs(safeGetListData.hashCode())) << 24) + ((long) i);
        }
        return renderState.itemId;
    }

    public void onBeforeScroll(int i, int i2) {
        TemplateStickyHelper templateStickyHelper = this.mStickyHelper;
        if (templateStickyHelper != null) {
            templateStickyHelper.onBeforeScroll(i, i2);
        }
    }

    public void onLoadMore(int i) {
        try {
            String loadMoreOffset = getAttrs().getLoadMoreOffset();
            if (TextUtils.isEmpty(loadMoreOffset)) {
                loadMoreOffset = WXInstanceApm.VALUE_ERROR_CODE_DEFAULT;
            }
            if (((float) i) <= WXViewUtils.getRealPxByWidth((float) Integer.parseInt(loadMoreOffset), getInstance().getInstanceViewPortWidthWithFloat()) && this.cellDataManager.listData != null) {
                if (this.mListCellCount != this.cellDataManager.listData.size() || this.mForceLoadmoreNextTime) {
                    fireEvent(Constants.Event.LOADMORE);
                    this.mListCellCount = this.cellDataManager.listData.size();
                    this.mForceLoadmoreNextTime = false;
                }
            }
        } catch (Exception e) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.d("WXRecyclerTemplateList onLoadMore : ", (Throwable) e);
            }
        }
    }

    public void notifyAppearStateChange(int i, int i2, int i3, int i4) {
        int i5;
        Map map;
        List<WXComponent> findChildListByRef;
        int i6 = i2;
        ArrayMap<Integer, List<AppearanceHelper>> arrayMap = this.mAppearHelpers;
        if (arrayMap != null && arrayMap.size() > 0) {
            if (this.mAppearChangeRunnable != null) {
                ((BounceRecyclerView) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
                this.mAppearChangeRunnable = null;
            }
            String str = i4 > 0 ? "up" : i4 < 0 ? "down" : null;
            if (getOrientation() == 0 && i3 != 0) {
                str = i3 > 0 ? "left" : "right";
            }
            RecyclerView recyclerView = (RecyclerView) ((BounceRecyclerView) getHostView()).getInnerView();
            int i7 = i;
            while (true) {
                i5 = 0;
                if (i7 > i6) {
                    break;
                }
                List<AppearanceHelper> list = this.mAppearHelpers.get(Integer.valueOf(getItemViewType(i7)));
                if (list != null) {
                    for (AppearanceHelper appearanceHelper : list) {
                        if (appearanceHelper.isWatch()) {
                            TemplateViewHolder templateViewHolder = (TemplateViewHolder) recyclerView.findViewHolderForAdapterPosition(i7);
                            if (templateViewHolder == null || templateViewHolder.getComponent() == null || (findChildListByRef = findChildListByRef(templateViewHolder.getComponent(), appearanceHelper.getAwareChild().getRef())) == null || findChildListByRef.size() == 0) {
                                break;
                            }
                            Map map2 = this.mDisAppearWatchList.get(Integer.valueOf(i7));
                            if (map2 == null) {
                                map2 = new ArrayMap();
                                this.mDisAppearWatchList.put(Integer.valueOf(i7), map2);
                            }
                            Map map3 = (Map) map2.get(appearanceHelper.getAwareChild().getRef());
                            if (map3 == null) {
                                map3 = new ArrayMap();
                                map2.put(appearanceHelper.getAwareChild().getRef(), map3);
                            }
                            for (int i8 = 0; i8 < findChildListByRef.size(); i8++) {
                                WXComponent wXComponent = findChildListByRef.get(i8);
                                if (wXComponent.getHostView() != null) {
                                    boolean isViewVisible = appearanceHelper.isViewVisible(wXComponent.getHostView());
                                    int hashCode = wXComponent.getHostView().hashCode();
                                    if (isViewVisible) {
                                        if (!map3.containsKey(Integer.valueOf(hashCode))) {
                                            wXComponent.notifyAppearStateChange(Constants.Event.APPEAR, str);
                                            map3.put(Integer.valueOf(hashCode), (wXComponent.getEvents() == null || wXComponent.getEvents().getEventBindingArgsValues() == null || wXComponent.getEvents().getEventBindingArgsValues().get(Constants.Event.DISAPPEAR) == null) ? null : wXComponent.getEvents().getEventBindingArgsValues().get(Constants.Event.DISAPPEAR));
                                        }
                                    } else if (map3.containsKey(Integer.valueOf(hashCode))) {
                                        wXComponent.notifyAppearStateChange(Constants.Event.DISAPPEAR, str);
                                        map3.remove(Integer.valueOf(hashCode));
                                    }
                                }
                            }
                        }
                    }
                }
                i7++;
            }
            int itemCount = getItemCount();
            while (i5 < itemCount) {
                if (i5 < i || i5 > i6) {
                    Map map4 = this.mDisAppearWatchList.get(Integer.valueOf(i5));
                    if (map4 != null) {
                        WXCell wXCell = this.mTemplateSources.get(getTemplateKey(i5));
                        if (wXCell != null) {
                            for (Map.Entry entry : map4.entrySet()) {
                                WXComponent findChildByRef = findChildByRef(wXCell, (String) entry.getKey());
                                if (!(findChildByRef == null || (map = (Map) entry.getValue()) == null || map.size() == 0)) {
                                    WXEvent events = findChildByRef.getEvents();
                                    for (Map.Entry value : map.entrySet()) {
                                        events.putEventBindingArgsValue(Constants.Event.DISAPPEAR, (List) value.getValue());
                                        findChildByRef.notifyAppearStateChange(Constants.Event.DISAPPEAR, str);
                                    }
                                    map.clear();
                                }
                            }
                            this.mDisAppearWatchList.remove(Integer.valueOf(i5));
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                } else {
                    i5 = i6 + 1;
                }
                i5++;
            }
        }
    }

    private Object safeGetListData(int i) {
        try {
            return this.cellDataManager.listData.get(i);
        } catch (Exception unused) {
            return JSONObject.parseObject("{}");
        }
    }

    public void notifyUpdateList() {
        if (getHostView() != null && ((BounceRecyclerView) getHostView()).getInnerView() != null && this.listUpdateRunnable != null) {
            if (Looper.getMainLooper().getThread().getId() != Thread.currentThread().getId()) {
                ((BounceRecyclerView) getHostView()).removeCallbacks(this.listUpdateRunnable);
                ((BounceRecyclerView) getHostView()).post(this.listUpdateRunnable);
                return;
            }
            this.listUpdateRunnable.run();
        }
    }

    private int calcContentSize() {
        if (this.cellDataManager.listData == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.cellDataManager.listData.size(); i2++) {
            WXCell sourceTemplate = getSourceTemplate(i2);
            if (sourceTemplate != null) {
                i = (int) (((float) i) + sourceTemplate.getLayoutHeight());
            }
        }
        return i;
    }

    public int calcContentOffset(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int i = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            int findFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int i2 = 0;
            while (i < findFirstVisibleItemPosition) {
                WXCell sourceTemplate = getSourceTemplate(i);
                if (sourceTemplate != null) {
                    i2 = (int) (((float) i2) - sourceTemplate.getLayoutHeight());
                }
                i++;
            }
            if (layoutManager instanceof GridLayoutManager) {
                i2 /= ((GridLayoutManager) layoutManager).getSpanCount();
            }
            View findViewByPosition = layoutManager.findViewByPosition(findFirstVisibleItemPosition);
            return findViewByPosition != null ? i2 + findViewByPosition.getTop() : i2;
        } else if (!(layoutManager instanceof StaggeredGridLayoutManager)) {
            return -1;
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            int i3 = staggeredGridLayoutManager.findFirstVisibleItemPositions((int[]) null)[0];
            int i4 = 0;
            while (i < i3) {
                WXCell sourceTemplate2 = getSourceTemplate(i);
                if (sourceTemplate2 != null) {
                    i4 = (int) (((float) i4) - sourceTemplate2.getLayoutHeight());
                }
                i++;
            }
            int i5 = i4 / spanCount;
            View findViewByPosition2 = layoutManager.findViewByPosition(i3);
            return findViewByPosition2 != null ? i5 + findViewByPosition2.getTop() : i5;
        }
    }

    public WXComponent findParentType(WXComponent wXComponent, Class cls) {
        if (cls.isAssignableFrom(wXComponent.getClass())) {
            return wXComponent;
        }
        if (wXComponent.getParent() == null) {
            return null;
        }
        findTypeParent(wXComponent.getParent(), cls);
        return null;
    }

    public WXComponent findChildByRef(WXComponent wXComponent, String str) {
        if (str.equals(wXComponent.getRef())) {
            return wXComponent;
        }
        if (!(wXComponent instanceof WXVContainer)) {
            return null;
        }
        WXVContainer wXVContainer = (WXVContainer) wXComponent;
        for (int i = 0; i < wXVContainer.getChildCount(); i++) {
            WXComponent findChildByRef = findChildByRef(wXVContainer.getChild(i), str);
            if (findChildByRef != null) {
                return findChildByRef;
            }
        }
        return null;
    }

    public List<WXComponent> findChildListByRef(WXComponent wXComponent, String str) {
        WXComponent findChildByRef = findChildByRef(wXComponent, str);
        if (findChildByRef == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        WXVContainer parent = findChildByRef.getParent();
        if (parent == null || (parent instanceof WXRecyclerTemplateList)) {
            arrayList.add(findChildByRef);
        } else {
            for (int i = 0; i < parent.getChildCount(); i++) {
                WXComponent child = parent.getChild(i);
                if (str.equals(child.getRef())) {
                    arrayList.add(child);
                }
            }
        }
        return arrayList;
    }

    public WXComponent findChildByAttrsRef(WXComponent wXComponent, String str) {
        if (wXComponent.getAttrs() != null && str.equals(wXComponent.getAttrs().get("ref"))) {
            return wXComponent;
        }
        if (!(wXComponent instanceof WXVContainer)) {
            return null;
        }
        WXVContainer wXVContainer = (WXVContainer) wXComponent;
        for (int i = 0; i < wXVContainer.getChildCount(); i++) {
            WXComponent findChildByAttrsRef = findChildByAttrsRef(wXVContainer.getChild(i), str);
            if (findChildByAttrsRef != null) {
                return findChildByAttrsRef;
            }
        }
        return null;
    }

    private WXCell getCellTemplateFromCache(String str) {
        TemplateCache templateCache = this.mTemplatesCache.get(str);
        WXCell poll = (templateCache == null || templateCache.cells == null || templateCache.cells.size() <= 0) ? null : templateCache.cells.poll();
        if (templateCache == null || !templateCache.isLoadIng) {
            if (templateCache == null) {
                templateCache = new TemplateCache();
                this.mTemplatesCache.put(str, templateCache);
            }
            templateCache.isLoadIng = true;
            WXCell wXCell = this.mTemplateSources.get(str);
            if (wXCell != null && WXUtils.getBoolean(wXCell.getAttrs().get("preload"), true).booleanValue()) {
                new AsyncCellLoadTask(str, wXCell, this).startTask();
            }
        }
        return poll;
    }

    public WXComponent copyComponentFromSourceCell(WXCell wXCell) {
        renderTemplateCellWithData(wXCell);
        return (WXCell) Statements.copyComponentTree(wXCell);
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:659)
        	at java.util.ArrayList.get(ArrayList.java:435)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    private synchronized void renderTemplateCellWithData(com.taobao.weex.ui.component.list.WXCell r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.lang.Object r0 = r4.getRenderData()     // Catch:{ all -> 0x0056 }
            if (r0 != 0) goto L_0x0054
            com.taobao.weex.ui.component.list.template.CellDataManager r0 = r3.cellDataManager     // Catch:{ all -> 0x0056 }
            com.alibaba.fastjson.JSONArray r0 = r0.listData     // Catch:{ all -> 0x0056 }
            if (r0 == 0) goto L_0x0054
            com.taobao.weex.ui.component.list.template.CellDataManager r0 = r3.cellDataManager     // Catch:{ all -> 0x0056 }
            com.alibaba.fastjson.JSONArray r0 = r0.listData     // Catch:{ all -> 0x0056 }
            int r0 = r0.size()     // Catch:{ all -> 0x0056 }
            if (r0 <= 0) goto L_0x0054
            monitor-enter(r3)     // Catch:{ all -> 0x0056 }
            java.lang.Object r0 = r4.getRenderData()     // Catch:{ all -> 0x0051 }
            if (r0 != 0) goto L_0x004f
            com.taobao.weex.ui.component.binding.Statements.parseStatementsToken(r4)     // Catch:{ all -> 0x0051 }
            r0 = 0
        L_0x0022:
            com.taobao.weex.ui.component.list.template.CellDataManager r1 = r3.cellDataManager     // Catch:{ all -> 0x0051 }
            com.alibaba.fastjson.JSONArray r1 = r1.listData     // Catch:{ all -> 0x0051 }
            int r1 = r1.size()     // Catch:{ all -> 0x0051 }
            if (r0 >= r1) goto L_0x004f
            com.taobao.weex.ui.component.list.WXCell r1 = r3.getSourceTemplate(r0)     // Catch:{ all -> 0x0051 }
            if (r4 != r1) goto L_0x004c
            com.taobao.weex.ui.component.list.template.CellDataManager r1 = r3.cellDataManager     // Catch:{ all -> 0x0051 }
            com.alibaba.fastjson.JSONArray r1 = r1.listData     // Catch:{ all -> 0x0051 }
            java.lang.Object r1 = r1.get(r0)     // Catch:{ all -> 0x0051 }
            r3.doRenderTemplate(r4, r0)     // Catch:{ all -> 0x0051 }
            float r0 = r3.getLayoutWidth()     // Catch:{ all -> 0x0051 }
            float r2 = r3.getLayoutHeight()     // Catch:{ all -> 0x0051 }
            com.taobao.weex.ui.component.binding.Layouts.doLayoutSync(r4, r0, r2)     // Catch:{ all -> 0x0051 }
            r4.setRenderData(r1)     // Catch:{ all -> 0x0051 }
            goto L_0x004f
        L_0x004c:
            int r0 = r0 + 1
            goto L_0x0022
        L_0x004f:
            monitor-exit(r3)     // Catch:{ all -> 0x0051 }
            goto L_0x0054
        L_0x0051:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0051 }
            throw r4     // Catch:{ all -> 0x0056 }
        L_0x0054:
            monitor-exit(r3)
            return
        L_0x0056:
            r4 = move-exception
            monitor-exit(r3)
            goto L_0x005a
        L_0x0059:
            throw r4
        L_0x005a:
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.template.WXRecyclerTemplateList.renderTemplateCellWithData(com.taobao.weex.ui.component.list.WXCell):void");
    }

    public static void doCreateCellViewBindData(WXCell wXCell, String str, boolean z) {
        if (wXCell.isLazy() || wXCell.getHostView() == null) {
            System.currentTimeMillis();
            Statements.initLazyComponent(wXCell, (WXVContainer) null);
            WXEnvironment.isOpenDebugLog();
        }
    }

    public ScrollStartEndHelper getScrollStartEndHelper() {
        if (this.mScrollStartEndHelper == null) {
            this.mScrollStartEndHelper = new ScrollStartEndHelper(this);
        }
        return this.mScrollStartEndHelper;
    }

    public int getTemplateCacheSize() {
        return this.templateCacheSize;
    }

    public ConcurrentHashMap<String, TemplateCache> getTemplatesCache() {
        if (this.mTemplatesCache == null) {
            this.mTemplatesCache = new ConcurrentHashMap<>();
        }
        return this.mTemplatesCache;
    }

    public CellDataManager getCellDataManager() {
        return this.cellDataManager;
    }

    private JSONArray parseListDataToJSONArray(Object obj) {
        try {
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
            if (obj instanceof String) {
                return JSONArray.parseArray(getAttrs().get(Constants.Name.Recycler.LIST_DATA).toString());
            }
            return new JSONArray();
        } catch (Exception e) {
            WXLogUtils.e(TAG, "parseListDataException" + e.getMessage());
        }
    }

    @WXComponentProp(name = "scrollTop")
    public void setScrollTop(String str) {
        BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) getHostView();
        if (bounceRecyclerView != null) {
            ((WXRecyclerView) bounceRecyclerView.getInnerView()).scrollTo(0, (int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(str), getInstance().getInstanceViewPortWidthWithFloat()), getAttrs().getOrientation());
        }
    }

    @WXComponentProp(name = "scrollLeft")
    public void setScrollLeft(String str) {
        BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) getHostView();
        if (bounceRecyclerView != null) {
            ((WXRecyclerView) bounceRecyclerView.getInnerView()).scrollTo((int) WXViewUtils.getRealPxByWidth(WXUtils.getFloat(str), getInstance().getInstanceViewPortWidthWithFloat()), 0, getAttrs().getOrientation());
        }
    }

    public void setIsLayoutRTL(boolean z) {
        super.setIsLayoutRTL(z);
        this.mViewOnScrollListener.setLayoutRTL(isLayoutRTL());
    }
}
