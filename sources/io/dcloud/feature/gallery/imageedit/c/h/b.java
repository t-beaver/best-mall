package io.dcloud.feature.gallery.imageedit.c.h;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public abstract class b {
    private Uri a;

    public b(Uri uri) {
        this.a = uri;
    }

    public abstract Bitmap a(BitmapFactory.Options options);

    public Uri a() {
        return this.a;
    }
}
