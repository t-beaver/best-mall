package io.dcloud.feature.gallery.imageedit.c.h;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;

public class a extends b {
    private Context b;

    public a(Context context, Uri uri) {
        super(uri);
        this.b = context;
    }

    public Bitmap a(BitmapFactory.Options options) {
        Uri a = a();
        if (a == null) {
            return null;
        }
        String path = a.getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            InputStream open = this.b.getAssets().open(path.substring(1));
            if (options.inJustDecodeBounds) {
                return BitmapFactory.decodeStream(open, (Rect) null, options);
            }
            return io.dcloud.feature.gallery.imageedit.c.k.a.a(open, options);
        } catch (IOException unused) {
            return null;
        }
    }
}
