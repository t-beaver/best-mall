package com.taobao.weex.ui.view.listview.adapter;

import android.graphics.Canvas;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class TransformItemDecoration extends RecyclerView.ItemDecoration {
    float mAlpha = -1.0f;
    boolean mIsVertical = true;
    int mRotation = 0;
    float mScaleX = 0.0f;
    float mScaleY = 0.0f;
    int mXTranslate = 0;
    int mYTranslate = 0;

    public TransformItemDecoration(boolean z, float f, int i, int i2, int i3, float f2, float f3) {
        this.mIsVertical = z;
        this.mAlpha = f;
        this.mXTranslate = i;
        this.mYTranslate = i2;
        this.mRotation = i3;
        this.mScaleX = f2;
        this.mScaleY = f3;
    }

    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        int width = recyclerView.getWidth();
        int height = recyclerView.getHeight();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            updateItem(recyclerView.getChildAt(i), width, height);
        }
    }

    private void updateItem(View view, int i, int i2) {
        int i3;
        int i4;
        if (this.mIsVertical) {
            int height = view.getHeight();
            i4 = view.getTop() + (height / 2);
            int i5 = i2;
            i3 = height;
            i = i5;
        } else {
            i3 = view.getWidth();
            i4 = view.getLeft() + (i3 / 2);
        }
        float min = Math.min(1.0f, Math.max(-1.0f, (1.0f / ((float) ((i3 + i) / 2))) * ((float) (i4 - (i / 2)))));
        float f = this.mAlpha;
        if (f > 0.0f) {
            view.setAlpha(1.0f - (f * Math.abs(min)));
        }
        float f2 = this.mScaleX;
        if (f2 > 0.0f || this.mScaleY > 0.0f) {
            view.setScaleX(1.0f - (f2 * Math.abs(min)));
            view.setScaleY(1.0f - (this.mScaleY * Math.abs(min)));
        }
        int i6 = this.mRotation;
        if (i6 != 0) {
            view.setRotation(((float) i6) * min);
        }
        int i7 = this.mXTranslate;
        if (i7 != 0) {
            view.setTranslationX(((float) i7) * Math.abs(min));
        }
        int i8 = this.mYTranslate;
        if (i8 != 0) {
            view.setTranslationY(((float) i8) * Math.abs(min));
        }
    }
}
