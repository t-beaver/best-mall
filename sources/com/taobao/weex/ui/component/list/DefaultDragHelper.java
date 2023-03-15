package com.taobao.weex.ui.component.list;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.common.DHInterface.IApp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultDragHelper implements DragHelper {
    private static final String EVENT_END_DRAG = "dragend";
    private static final String EVENT_START_DRAG = "dragstart";
    private static final String TAG = "WXListExComponent";
    private static final String TAG_EXCLUDED = "drag_excluded";
    private boolean isDraggable = false;
    private final List<WXComponent> mDataSource;
    private final EventTrigger mEventTrigger;
    private ItemTouchHelper mItemTouchHelper;
    private boolean mLongPressEnabled;
    private final RecyclerView mRecyclerView;

    DefaultDragHelper(List<WXComponent> list, RecyclerView recyclerView, EventTrigger eventTrigger) {
        this.mDataSource = list;
        this.mEventTrigger = eventTrigger;
        this.mRecyclerView = recyclerView;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DragSupportCallback(this, true));
        this.mItemTouchHelper = itemTouchHelper;
        try {
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } catch (Throwable unused) {
        }
    }

    public void onDragStart(WXComponent wXComponent, int i) {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d(TAG, "list on drag start : from index " + i);
        }
        this.mEventTrigger.triggerEvent(EVENT_START_DRAG, buildEvent(wXComponent.getRef(), i, -1));
    }

    public void onDragEnd(WXComponent wXComponent, int i, int i2) {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d(TAG, "list on drag end : from index " + i + " to index " + i2);
        }
        this.mEventTrigger.triggerEvent(EVENT_END_DRAG, buildEvent(wXComponent.getRef(), i, i2));
    }

    public void onDragging(int i, int i2) {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d(TAG, "list on dragging : from index " + i + " to index " + i2);
        }
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        if (adapter == null) {
            WXLogUtils.e(TAG, "drag failed because of RecyclerView#Adapter is not bound");
        } else if (i >= 0 && i <= this.mDataSource.size() - 1 && i2 >= 0 && i2 <= this.mDataSource.size() - 1) {
            Collections.swap(this.mDataSource, i, i2);
            adapter.notifyItemMoved(i, i2);
        }
    }

    public boolean isLongPressDragEnabled() {
        return this.mLongPressEnabled;
    }

    public void setLongPressDragEnabled(boolean z) {
        this.mLongPressEnabled = z;
    }

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if (this.isDraggable) {
            this.mItemTouchHelper.startDrag(viewHolder);
        }
    }

    public boolean isDraggable() {
        return this.isDraggable;
    }

    public void setDraggable(boolean z) {
        this.isDraggable = z;
    }

    public void setDragExcluded(RecyclerView.ViewHolder viewHolder, boolean z) {
        if (viewHolder.itemView == null) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "[error] viewHolder.itemView is null");
            }
        } else if (z) {
            viewHolder.itemView.setTag(TAG_EXCLUDED);
        } else {
            viewHolder.itemView.setTag((Object) null);
        }
    }

    public boolean isDragExcluded(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.itemView == null) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "[error] viewHolder.itemView is null");
            }
            return false;
        } else if (viewHolder.itemView.getTag() == null || !TAG_EXCLUDED.equals(viewHolder.itemView.getTag())) {
            return false;
        } else {
            return true;
        }
    }

    private Map<String, Object> buildEvent(String str, int i, int i2) {
        HashMap hashMap = new HashMap(4);
        if (str == null) {
            str = "unknown";
        }
        hashMap.put(IApp.ConfigProperty.CONFIG_TARGET, str);
        hashMap.put("fromIndex", Integer.valueOf(i));
        hashMap.put("toIndex", Integer.valueOf(i2));
        hashMap.put("timestamp", Long.valueOf(System.currentTimeMillis()));
        return hashMap;
    }
}
