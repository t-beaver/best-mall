package io.dcloud.feature.gallery.imageedit.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;

public class WindowInsertLinearLayout extends LinearLayout {

    class a implements ViewGroup.OnHierarchyChangeListener {
        a() {
        }

        public void onChildViewAdded(View view, View view2) {
            if (Build.VERSION.SDK_INT >= 20) {
                WindowInsertLinearLayout.this.requestApplyInsets();
            }
        }

        public void onChildViewRemoved(View view, View view2) {
        }
    }

    public WindowInsertLinearLayout(Context context) {
        super(context);
    }

    public WindowInsets dispatchApplyWindowInsets(WindowInsets windowInsets) {
        if (Build.VERSION.SDK_INT >= 20) {
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).dispatchApplyWindowInsets(windowInsets);
            }
        }
        return windowInsets;
    }

    public WindowInsertLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnHierarchyChangeListener(new a());
    }
}
