package io.dcloud.feature.gallery.imageedit.c.h;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import io.dcloud.feature.gallery.imageedit.c.k.a;
import java.io.File;

public class c extends b {
    public c(Uri uri) {
        super(uri);
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
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        if (options.inJustDecodeBounds) {
            return BitmapFactory.decodeFile(path, options);
        }
        return a.a(file, options);
    }
}
