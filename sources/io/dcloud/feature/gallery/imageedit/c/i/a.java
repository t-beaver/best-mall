package io.dcloud.feature.gallery.imageedit.c.i;

import com.taobao.weex.el.parse.Operators;

public class a {
    public float a;
    public float b;
    public float c;
    public float d;

    public a(float f, float f2, float f3, float f4) {
        this.a = f;
        this.b = f2;
        this.c = f3;
        this.d = f4;
    }

    public void a(float f, float f2, float f3, float f4) {
        this.a = f;
        this.b = f2;
        this.c = f3;
        this.d = f4;
    }

    public String toString() {
        return "IMGHoming{x=" + this.a + ", y=" + this.b + ", scale=" + this.c + ", rotate=" + this.d + Operators.BLOCK_END;
    }

    public void a(a aVar) {
        this.c *= aVar.c;
        this.a -= aVar.a;
        this.b -= aVar.b;
    }

    public static boolean a(a aVar, a aVar2) {
        return Float.compare(aVar.d, aVar2.d) != 0;
    }
}
