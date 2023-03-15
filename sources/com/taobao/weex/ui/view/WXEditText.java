package com.taobao.weex.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.BaseMovementMethod;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewParent;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import io.dcloud.common.core.ui.keyboard.DCEditText;

public class WXEditText extends DCEditText implements WXGestureObservable {
    private boolean mAllowCopyPaste = true;
    private boolean mAllowDisableMovement = true;
    private int mLines = 1;
    private WXGesture wxGesture;

    public WXEditText(Context context, String str) {
        super(context, str);
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground((Drawable) null);
        } else {
            setBackgroundDrawable((Drawable) null);
        }
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public void setLines(int i) {
        super.setLines(i);
        this.mLines = i;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        WXGesture wXGesture = this.wxGesture;
        if (wXGesture != null) {
            onTouchEvent |= wXGesture.onTouch(this, motionEvent);
        }
        ViewParent parent = getParent();
        if (parent != null) {
            int action = motionEvent.getAction() & 255;
            if (action != 0) {
                if (action == 1 || action == 3) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            } else if (this.mLines < getLineCount()) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
        return onTouchEvent;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (getLayout() != null) {
            int height = getLayout().getHeight();
            if (!this.mAllowDisableMovement || i2 >= height) {
                setMovementMethod(getDefaultMovementMethod());
            } else {
                setMovementMethod(new BaseMovementMethod());
            }
        }
    }

    public void setAllowDisableMovement(boolean z) {
        this.mAllowDisableMovement = z;
    }

    public void setAllowCopyPaste(boolean z) {
        this.mAllowCopyPaste = z;
        if (z) {
            setLongClickable(true);
            setCustomSelectionActionModeCallback((ActionMode.Callback) null);
            if (Build.VERSION.SDK_INT >= 23) {
                setCustomInsertionActionModeCallback((ActionMode.Callback) null);
                return;
            }
            return;
        }
        setLongClickable(false);
        AnonymousClass1 r3 = new ActionMode.Callback() {
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
        };
        if (Build.VERSION.SDK_INT >= 23) {
            setCustomInsertionActionModeCallback(r3);
        }
        setCustomSelectionActionModeCallback(r3);
    }

    public boolean onTextContextMenuItem(int i) {
        return !this.mAllowCopyPaste || super.onTextContextMenuItem(i);
    }
}
