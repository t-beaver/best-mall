package io.dcloud.feature.gallery.imageedit.c;

import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;

public class d {
    private String a;
    private int b = -1;
    private int c = 0;

    public d(String str, int i, int i2) {
        this.a = str;
        this.b = i;
        this.c = i2;
    }

    public int a() {
        return this.c;
    }

    public int b() {
        return this.b;
    }

    public String c() {
        return this.a;
    }

    public boolean d() {
        return TextUtils.isEmpty(this.a);
    }

    public String toString() {
        return "IMGText{text='" + this.a + Operators.SINGLE_QUOTE + ", color=" + this.b + Operators.BLOCK_END;
    }
}
