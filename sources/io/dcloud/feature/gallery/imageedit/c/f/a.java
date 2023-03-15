package io.dcloud.feature.gallery.imageedit.c.f;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import io.dcloud.feature.gallery.imageedit.c.i.b;

public class a extends ValueAnimator {
    private boolean a = false;
    private b b;

    public a() {
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void a(io.dcloud.feature.gallery.imageedit.c.i.a aVar, io.dcloud.feature.gallery.imageedit.c.i.a aVar2) {
        setObjectValues(aVar, aVar2);
        this.a = io.dcloud.feature.gallery.imageedit.c.i.a.a(aVar, aVar2);
    }

    public void setObjectValues(Object... objArr) {
        super.setObjectValues(objArr);
        if (this.b == null) {
            this.b = new b();
        }
        setEvaluator(this.b);
    }

    public boolean a() {
        return this.a;
    }
}
