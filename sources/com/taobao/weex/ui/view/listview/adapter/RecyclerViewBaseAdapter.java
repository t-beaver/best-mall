package com.taobao.weex.ui.view.listview.adapter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.taobao.weex.ui.view.listview.adapter.ListBaseViewHolder;

public class RecyclerViewBaseAdapter<T extends ListBaseViewHolder> extends RecyclerView.Adapter<T> {
    private IRecyclerAdapterListener iRecyclerAdapterListener;

    public RecyclerViewBaseAdapter(IRecyclerAdapterListener iRecyclerAdapterListener2) {
        this.iRecyclerAdapterListener = iRecyclerAdapterListener2;
    }

    public T onCreateViewHolder(ViewGroup viewGroup, int i) {
        IRecyclerAdapterListener iRecyclerAdapterListener2 = this.iRecyclerAdapterListener;
        if (iRecyclerAdapterListener2 != null) {
            return (ListBaseViewHolder) iRecyclerAdapterListener2.onCreateViewHolder(viewGroup, i);
        }
        return null;
    }

    public void onViewAttachedToWindow(T t) {
        ViewGroup.LayoutParams layoutParams;
        super.onViewAttachedToWindow(t);
        if (t != null && t.isFullSpan() && (layoutParams = t.itemView.getLayoutParams()) != null && (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    public void onViewDetachedFromWindow(T t) {
        super.onViewDetachedFromWindow(t);
        if (t != null) {
            t.setComponentUsing(false);
        }
    }

    public void onBindViewHolder(T t, int i) {
        IRecyclerAdapterListener iRecyclerAdapterListener2 = this.iRecyclerAdapterListener;
        if (iRecyclerAdapterListener2 != null) {
            iRecyclerAdapterListener2.onBindViewHolder(t, i);
        }
    }

    public int getItemViewType(int i) {
        IRecyclerAdapterListener iRecyclerAdapterListener2 = this.iRecyclerAdapterListener;
        return iRecyclerAdapterListener2 != null ? iRecyclerAdapterListener2.getItemViewType(i) : i;
    }

    public long getItemId(int i) {
        return this.iRecyclerAdapterListener.getItemId(i);
    }

    public int getItemCount() {
        IRecyclerAdapterListener iRecyclerAdapterListener2 = this.iRecyclerAdapterListener;
        if (iRecyclerAdapterListener2 != null) {
            return iRecyclerAdapterListener2.getItemCount();
        }
        return 0;
    }

    public void onViewRecycled(T t) {
        IRecyclerAdapterListener iRecyclerAdapterListener2 = this.iRecyclerAdapterListener;
        if (iRecyclerAdapterListener2 != null) {
            iRecyclerAdapterListener2.onViewRecycled(t);
        }
        super.onViewRecycled(t);
    }

    public boolean onFailedToRecycleView(T t) {
        IRecyclerAdapterListener iRecyclerAdapterListener2 = this.iRecyclerAdapterListener;
        if (iRecyclerAdapterListener2 != null) {
            return iRecyclerAdapterListener2.onFailedToRecycleView(t);
        }
        return super.onFailedToRecycleView(t);
    }
}
