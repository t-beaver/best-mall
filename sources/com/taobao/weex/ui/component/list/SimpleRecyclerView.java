package com.taobao.weex.ui.component.list;

import android.content.Context;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.listview.adapter.RecyclerViewBaseAdapter;

class SimpleRecyclerView extends WXRecyclerView implements ListComponentView {
    private RecyclerViewBaseAdapter mAdapter = null;

    public WXRecyclerView getInnerView() {
        return this;
    }

    public void notifyStickyRemove(WXCell wXCell) {
    }

    public void notifyStickyShow(WXCell wXCell) {
    }

    public void updateStickyView(int i) {
    }

    public SimpleRecyclerView(Context context) {
        super(context);
    }

    public void setRecyclerViewBaseAdapter(RecyclerViewBaseAdapter recyclerViewBaseAdapter) {
        setAdapter(recyclerViewBaseAdapter);
        this.mAdapter = recyclerViewBaseAdapter;
    }

    public RecyclerViewBaseAdapter getRecyclerViewBaseAdapter() {
        return this.mAdapter;
    }
}
