package io.dcloud.feature.gallery.imageedit.c.i;

import android.animation.TypeEvaluator;

public class b implements TypeEvaluator<a> {
    private a a;

    /* renamed from: a */
    public a evaluate(float f, a aVar, a aVar2) {
        float f2 = aVar.a;
        float f3 = f2 + ((aVar2.a - f2) * f);
        float f4 = aVar.b;
        float f5 = f4 + ((aVar2.b - f4) * f);
        float f6 = aVar.c;
        float f7 = f6 + ((aVar2.c - f6) * f);
        float f8 = aVar.d;
        float f9 = f8 + (f * (aVar2.d - f8));
        a aVar3 = this.a;
        if (aVar3 == null) {
            this.a = new a(f3, f5, f7, f9);
        } else {
            aVar3.a(f3, f5, f7, f9);
        }
        return this.a;
    }
}
