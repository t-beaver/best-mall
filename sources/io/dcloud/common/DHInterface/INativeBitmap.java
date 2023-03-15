package io.dcloud.common.DHInterface;

import android.graphics.Bitmap;

public interface INativeBitmap {
    void clear();

    Bitmap getBitmap();

    void setBitmap(Bitmap bitmap);
}
