package com.dcloud.android.v4.widget;

import android.widget.OverScroller;

class ScrollerCompatIcs {
    ScrollerCompatIcs() {
    }

    public static float getCurrVelocity(Object obj) {
        return ((OverScroller) obj).getCurrVelocity();
    }
}
