package com.taobao.weex.ui.animation;

import android.os.Build;
import android.util.Property;
import android.view.View;

class CameraDistanceProperty extends Property<View, Float> {
    private static final String TAG = "CameraDistance";
    private static CameraDistanceProperty instance;

    static Property<View, Float> getInstance() {
        return instance;
    }

    private CameraDistanceProperty() {
        super(Float.class, TAG);
    }

    public Float get(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Float.valueOf(view.getCameraDistance());
        }
        return Float.valueOf(Float.NaN);
    }

    public void set(View view, Float f) {
        view.setCameraDistance(f.floatValue());
    }
}
