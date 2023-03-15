package com.taobao.weex.ui.component.list;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.taobao.weex.ui.view.listview.adapter.ListBaseViewHolder;
import com.taobao.weex.utils.WXLogUtils;

class DragSupportCallback extends ItemTouchHelper.Callback {
    private static final String TAG = "WXListExComponent";
    private int dragFrom = -1;
    private int dragTo = -1;
    private final DragHelper mDragHelper;
    private boolean mEnableDifferentViewTypeDrag = false;

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    DragSupportCallback(DragHelper dragHelper) {
        this.mDragHelper = dragHelper;
        this.mEnableDifferentViewTypeDrag = false;
    }

    DragSupportCallback(DragHelper dragHelper, boolean z) {
        this.mDragHelper = dragHelper;
        this.mEnableDifferentViewTypeDrag = z;
    }

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if ((recyclerView.getLayoutManager() instanceof GridLayoutManager) || (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager)) {
            return makeMovementFlags(15, 0);
        }
        return makeMovementFlags(3, 48);
    }

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        if (!(viewHolder == null || viewHolder2 == null)) {
            if ((!this.mEnableDifferentViewTypeDrag && viewHolder.getItemViewType() != viewHolder2.getItemViewType()) || this.mDragHelper.isDragExcluded(viewHolder)) {
                return false;
            }
            try {
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                if (this.dragFrom == -1) {
                    this.dragFrom = adapterPosition;
                }
                this.dragTo = adapterPosition2;
                this.mDragHelper.onDragging(adapterPosition, adapterPosition2);
                return true;
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
        return false;
    }

    public boolean isLongPressDragEnabled() {
        return this.mDragHelper.isDraggable() && this.mDragHelper.isLongPressDragEnabled();
    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
        if (i != 0 && (viewHolder instanceof ListBaseViewHolder)) {
            ListBaseViewHolder listBaseViewHolder = (ListBaseViewHolder) viewHolder;
            if (listBaseViewHolder.getComponent() != null) {
                this.mDragHelper.onDragStart(listBaseViewHolder.getComponent(), listBaseViewHolder.getAdapterPosition());
            }
        }
        super.onSelectedChanged(viewHolder, i);
    }

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ListBaseViewHolder) {
            ListBaseViewHolder listBaseViewHolder = (ListBaseViewHolder) viewHolder;
            if (!(listBaseViewHolder.getComponent() == null || this.dragFrom == -1 || this.dragTo == -1)) {
                this.mDragHelper.onDragEnd(listBaseViewHolder.getComponent(), this.dragFrom, this.dragTo);
            }
        }
        this.dragTo = -1;
        this.dragFrom = -1;
    }
}
