package io.dcloud.feature.gallery.imageedit.c.j;

import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.gallery.imageedit.view.IMGStickerView;

public class b implements View.OnTouchListener {
    private View a;
    private IMGStickerView b;
    private float c;
    private float d;
    private double e;
    private double f;
    private Matrix g = new Matrix();

    public b(IMGStickerView iMGStickerView, View view) {
        this.a = view;
        this.b = iMGStickerView;
        view.setOnTouchListener(this);
    }

    private static double a(float f2, float f3) {
        return Math.toDegrees(Math.atan2((double) f2, (double) f3));
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            this.d = 0.0f;
            this.c = 0.0f;
            float x2 = (this.a.getX() + x) - this.b.getPivotX();
            float y2 = (this.a.getY() + y) - this.b.getPivotY();
            Log.d("IMGStickerAdjustHelper", StringUtil.format("X=%f,Y=%f", Float.valueOf(x2), Float.valueOf(y2)));
            this.e = a(0.0f, 0.0f, x2, y2);
            this.f = a(y2, x2);
            this.g.setTranslate(x2 - x, y2 - y);
            Log.d("IMGStickerAdjustHelper", StringUtil.format("degrees=%f", Double.valueOf(a(y2, x2))));
            this.g.postRotate((float) (-a(y2, x2)), this.c, this.d);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            float[] fArr = {motionEvent.getX(), motionEvent.getY()};
            float x3 = (this.a.getX() + fArr[0]) - this.b.getPivotX();
            float y3 = (this.a.getY() + fArr[1]) - this.b.getPivotY();
            Log.d("IMGStickerAdjustHelper", StringUtil.format("X=%f,Y=%f", Float.valueOf(x3), Float.valueOf(y3)));
            double a2 = a(0.0f, 0.0f, x3, y3);
            double a3 = a(y3, x3);
            this.b.a((float) (a2 / this.e));
            Log.d("IMGStickerAdjustHelper", "    D   = " + (a3 - this.f));
            IMGStickerView iMGStickerView = this.b;
            double rotation = (double) iMGStickerView.getRotation();
            Double.isNaN(rotation);
            iMGStickerView.setRotation((float) ((rotation + a3) - this.f));
            this.e = a2;
            return true;
        }
    }

    private static double a(float f2, float f3, float f4, float f5) {
        float f6 = f2 - f4;
        float f7 = f3 - f5;
        return Math.sqrt((double) ((f6 * f6) + (f7 * f7)));
    }
}
