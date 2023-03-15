package com.nostra13.dcloudimageloader.core.display;

import android.graphics.Bitmap;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;

public interface BitmapDisplayer {
    Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom);
}
