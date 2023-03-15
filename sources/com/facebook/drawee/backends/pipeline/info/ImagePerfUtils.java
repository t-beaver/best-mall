package com.facebook.drawee.backends.pipeline.info;

import com.taobao.weex.ui.component.WXImage;

public class ImagePerfUtils {
    public static String toString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "unknown" : "error" : "canceled" : WXImage.SUCCEED : "intermediate_available" : "origin_available" : "requested";
    }

    private ImagePerfUtils() {
    }
}
