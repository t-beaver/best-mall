package com.dcloud.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TextViewButton extends TextView {
    public TextViewButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            setBackgroundColor(-3355444);
        } else if (action == 1 || action == 4) {
            setBackgroundColor(-1118482);
        }
        return super.onTouchEvent(motionEvent);
    }
}
