package com.taobao.weex.ui.component.list;

import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.ui.component.WXComponent;

interface DragHelper {
    boolean isDragExcluded(RecyclerView.ViewHolder viewHolder);

    boolean isDraggable();

    boolean isLongPressDragEnabled();

    void onDragEnd(WXComponent wXComponent, int i, int i2);

    void onDragStart(WXComponent wXComponent, int i);

    void onDragging(int i, int i2);

    void setDragExcluded(RecyclerView.ViewHolder viewHolder, boolean z);

    void setDraggable(boolean z);

    void setLongPressDragEnabled(boolean z);

    void startDrag(RecyclerView.ViewHolder viewHolder);
}
