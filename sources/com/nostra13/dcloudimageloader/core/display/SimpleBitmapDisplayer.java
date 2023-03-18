package com.nostra13.dcloudimageloader.core.display;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;

public final class SimpleBitmapDisplayer implements BitmapDisplayer {
    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(bitmap);
        return bitmap;
    }
}
