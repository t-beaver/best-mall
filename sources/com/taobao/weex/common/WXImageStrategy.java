package com.taobao.weex.common;

import android.widget.ImageView;
import java.util.Map;

public class WXImageStrategy {
    public int blurRadius;
    ImageListener imageListener;
    public String instanceId;
    boolean isAutoCompression = true;
    @Deprecated
    public boolean isClipping;
    public boolean isSharpen;
    public String placeHolder;

    public interface ImageListener {
        void onImageFinish(String str, ImageView imageView, boolean z, Map map);
    }

    public WXImageStrategy() {
    }

    public WXImageStrategy(String str) {
        this.instanceId = str;
    }

    public ImageListener getImageListener() {
        return this.imageListener;
    }

    public void setImageListener(ImageListener imageListener2) {
        this.imageListener = imageListener2;
    }

    public boolean isAutoCompression() {
        return this.isAutoCompression;
    }

    public void setAutoCompression(boolean z) {
        this.isAutoCompression = z;
    }
}
