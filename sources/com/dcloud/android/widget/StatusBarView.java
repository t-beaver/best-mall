package com.dcloud.android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class StatusBarView extends View {
    private int mStatusBarHeight = 0;

    public StatusBarView(Context context) {
        super(context);
    }

    public void setStatusBarHeight(int i) {
        this.mStatusBarHeight = i;
        setMeasuredDimension(-1, i);
        if (getLayoutParams() == null) {
            setLayoutParams(new ViewGroup.MarginLayoutParams(-1, this.mStatusBarHeight));
        }
    }
}
