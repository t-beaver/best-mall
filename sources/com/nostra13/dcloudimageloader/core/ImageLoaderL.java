package com.nostra13.dcloudimageloader.core;

import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.SimpleImageLoadingListener;

public class ImageLoaderL extends ImageLoader {
    private static volatile ImageLoaderL instance;
    private ImageLoaderConfiguration configuration;
    private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();
    private ImageLoaderEngine engine;

    protected ImageLoaderL() {
    }

    private void checkConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException("ImageLoader must be init with configuration before using");
        }
    }

    public static ImageLoaderL getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderL.class) {
                if (instance == null) {
                    instance = new ImageLoaderL();
                }
            }
        }
        return instance;
    }
}
