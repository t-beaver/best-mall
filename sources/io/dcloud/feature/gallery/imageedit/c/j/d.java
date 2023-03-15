package io.dcloud.feature.gallery.imageedit.c.j;

import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

public class d {
    private static final Matrix a = new Matrix();
    private View b;
    private float c;
    private float d;

    public d(View view) {
        this.b = view;
    }

    public boolean a(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.c = motionEvent.getX();
            this.d = motionEvent.getY();
            Matrix matrix = a;
            matrix.reset();
            matrix.setRotate(view.getRotation());
            return true;
        } else if (actionMasked != 2) {
            return false;
        } else {
            float[] fArr = {motionEvent.getX() - this.c, motionEvent.getY() - this.d};
            a.mapPoints(fArr);
            view.setTranslationX(this.b.getTranslationX() + fArr[0]);
            view.setTranslationY(this.b.getTranslationY() + fArr[1]);
            return true;
        }
    }
}
