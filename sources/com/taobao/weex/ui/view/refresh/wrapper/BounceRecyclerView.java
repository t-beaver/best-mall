package com.taobao.weex.ui.view.refresh.wrapper;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import com.taobao.weex.R;
import com.taobao.weex.ui.component.list.ListComponentView;
import com.taobao.weex.ui.component.list.StickyHeaderHelper;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.listview.adapter.RecyclerViewBaseAdapter;
import java.lang.reflect.Method;

public class BounceRecyclerView extends BaseBounceView<WXRecyclerView> implements ListComponentView, WXGestureObservable {
    public static final int DEFAULT_COLUMN_COUNT = 1;
    public static final int DEFAULT_COLUMN_GAP = 1;
    private RecyclerViewBaseAdapter adapter;
    private int mColumnCount;
    private float mColumnGap;
    private int mLayoutType;
    private int mOrientation;
    private StickyHeaderHelper mStickyHeaderHelper;

    public /* bridge */ /* synthetic */ WXRecyclerView getInnerView() {
        return (WXRecyclerView) super.getInnerView();
    }

    public BounceRecyclerView(Context context, int i, int i2, float f, int i3) {
        super(context, i3);
        this.adapter = null;
        this.mLayoutType = 1;
        this.mColumnCount = 1;
        this.mColumnGap = 1.0f;
        this.mOrientation = 0;
        this.mLayoutType = i;
        this.mColumnCount = i2;
        this.mColumnGap = f;
        this.mOrientation = i3;
        init(context);
        this.mStickyHeaderHelper = new StickyHeaderHelper(this);
    }

    public BounceRecyclerView(Context context, int i, int i2) {
        this(context, i, 1, 1.0f, i2);
    }

    public void setRecyclerViewBaseAdapter(RecyclerViewBaseAdapter recyclerViewBaseAdapter) {
        this.adapter = recyclerViewBaseAdapter;
        if (getInnerView() != null) {
            ((WXRecyclerView) getInnerView()).setAdapter(recyclerViewBaseAdapter);
        }
    }

    public RecyclerViewBaseAdapter getRecyclerViewBaseAdapter() {
        return this.adapter;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public WXRecyclerView setInnerView(Context context) {
        WXRecyclerView wXRecyclerView;
        int next;
        if (R.layout.weex_recycler_layout != 0) {
            XmlResourceParser xml = getResources().getXml(R.layout.weex_recycler_layout);
            AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
            do {
                try {
                    next = xml.next();
                    if (next == 2) {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (next != 1);
            wXRecyclerView = new WXRecyclerView(context, asAttributeSet);
            wXRecyclerView.setScrollBarStyle(33554432);
            try {
                Method declaredMethod = View.class.getDeclaredMethod("initializeScrollbars", new Class[]{TypedArray.class});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(wXRecyclerView, new Object[]{null});
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (this.mOrientation == 1) {
                wXRecyclerView.setVerticalScrollBarEnabled(true);
            } else {
                wXRecyclerView.setHorizontalScrollBarEnabled(true);
            }
        } else {
            wXRecyclerView = new WXRecyclerView(context);
        }
        wXRecyclerView.setOverScrollMode(0);
        wXRecyclerView.initView(context, this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        return wXRecyclerView;
    }

    public void onRefreshingComplete() {
        RecyclerViewBaseAdapter recyclerViewBaseAdapter = this.adapter;
        if (recyclerViewBaseAdapter != null) {
            recyclerViewBaseAdapter.notifyDataSetChanged();
        }
    }

    public void onLoadmoreComplete() {
        RecyclerViewBaseAdapter recyclerViewBaseAdapter = this.adapter;
        if (recyclerViewBaseAdapter != null) {
            recyclerViewBaseAdapter.notifyDataSetChanged();
        }
    }

    public void notifyStickyShow(WXCell wXCell) {
        this.mStickyHeaderHelper.notifyStickyShow(wXCell);
    }

    public void updateStickyView(int i) {
        this.mStickyHeaderHelper.updateStickyView(i);
    }

    public void notifyStickyRemove(WXCell wXCell) {
        this.mStickyHeaderHelper.notifyStickyRemove(wXCell);
    }

    public StickyHeaderHelper getStickyHeaderHelper() {
        return this.mStickyHeaderHelper;
    }

    public void registerGestureListener(WXGesture wXGesture) {
        ((WXRecyclerView) getInnerView()).registerGestureListener(wXGesture);
    }

    public WXGesture getGestureListener() {
        return ((WXRecyclerView) getInnerView()).getGestureListener();
    }
}
