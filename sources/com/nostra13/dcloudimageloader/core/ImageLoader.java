package com.nostra13.dcloudimageloader.core;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware;
import com.nostra13.dcloudimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.dcloudimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.SyncImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import com.nostra13.dcloudimageloader.core.imageaware.ImageNonViewAware;
import com.nostra13.dcloudimageloader.core.imageaware.ImageViewAware;
import com.nostra13.dcloudimageloader.utils.ImageSizeUtils;
import com.nostra13.dcloudimageloader.utils.L;

public class ImageLoader {
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
    static final String LOG_DESTROY = "Destroy ImageLoader";
    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
    static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";
    public static final String TAG = "ImageLoader";
    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
    private static volatile ImageLoader instance;
    private ImageLoaderConfiguration configuration;
    private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();
    private ImageLoaderEngine engine;

    protected ImageLoader() {
    }

    private void checkConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public void cancelDisplayTask(ImageAware imageAware) {
        this.engine.cancelDisplayTaskFor(imageAware);
    }

    public void clearDiscCache() {
        checkConfiguration();
        this.configuration.discCache.clear();
    }

    public void clearMemoryCache() {
        checkConfiguration();
        this.configuration.memoryCache.clear();
    }

    public void denyNetworkDownloads(boolean z) {
        this.engine.denyNetworkDownloads(z);
    }

    public void destroy() {
        ImageLoaderConfiguration imageLoaderConfiguration = this.configuration;
        if (imageLoaderConfiguration != null && imageLoaderConfiguration.writeLogs) {
            L.d(LOG_DESTROY, new Object[0]);
        }
        stop();
        this.engine = null;
        this.configuration = null;
    }

    public void displayImage(String str, ImageAware imageAware) {
        displayImage(str, imageAware, (DisplayImageOptions) null, (ImageLoadingListener) null);
    }

    public DiscCacheAware getDiscCache() {
        checkConfiguration();
        return this.configuration.discCache;
    }

    public String getLoadingUriForView(ImageAware imageAware) {
        return this.engine.getLoadingUriForView(imageAware);
    }

    public MemoryCacheAware getMemoryCache() {
        checkConfiguration();
        return this.configuration.memoryCache;
    }

    public void handleSlowNetwork(boolean z) {
        this.engine.handleSlowNetwork(z);
    }

    public synchronized void init(ImageLoaderConfiguration imageLoaderConfiguration) {
        if (imageLoaderConfiguration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        } else if (this.configuration == null) {
            if (imageLoaderConfiguration.writeLogs) {
                L.d(LOG_INIT_CONFIG, new Object[0]);
            }
            this.engine = new ImageLoaderEngine(imageLoaderConfiguration);
            this.configuration = imageLoaderConfiguration;
        } else {
            L.w(WARNING_RE_INIT_CONFIG, new Object[0]);
        }
    }

    public boolean isInited() {
        return this.configuration != null;
    }

    public void loadImage(String str, ImageLoadingListener imageLoadingListener) {
        loadImage(str, (ImageSize) null, (DisplayImageOptions) null, imageLoadingListener);
    }

    public Bitmap loadImageSync(String str) {
        return loadImageSync(str, (ImageSize) null, (DisplayImageOptions) null);
    }

    public void pause() {
        this.engine.pause();
    }

    public void resume() {
        this.engine.resume();
    }

    public void stop() {
        this.engine.stop();
    }

    public void cancelDisplayTask(ImageView imageView) {
        this.engine.cancelDisplayTaskFor(new ImageViewAware(imageView));
    }

    public void displayImage(String str, ImageAware imageAware, ImageLoadingListener imageLoadingListener) {
        displayImage(str, imageAware, (DisplayImageOptions) null, imageLoadingListener);
    }

    public String getLoadingUriForView(ImageView imageView) {
        return this.engine.getLoadingUriForView(new ImageViewAware(imageView));
    }

    public void loadImage(String str, ImageSize imageSize, ImageLoadingListener imageLoadingListener) {
        loadImage(str, imageSize, (DisplayImageOptions) null, imageLoadingListener);
    }

    public Bitmap loadImageSync(String str, DisplayImageOptions displayImageOptions) {
        return loadImageSync(str, (ImageSize) null, displayImageOptions);
    }

    public void displayImage(String str, ImageAware imageAware, DisplayImageOptions displayImageOptions) {
        displayImage(str, imageAware, displayImageOptions, (ImageLoadingListener) null);
    }

    public void loadImage(String str, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener) {
        loadImage(str, (ImageSize) null, displayImageOptions, imageLoadingListener);
    }

    public Bitmap loadImageSync(String str, ImageSize imageSize) {
        return loadImageSync(str, imageSize, (DisplayImageOptions) null);
    }

    public void displayImage(String str, ImageAware imageAware, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener) {
        checkConfiguration();
        if (imageAware != null) {
            if (imageLoadingListener == null) {
                imageLoadingListener = this.emptyListener;
            }
            ImageLoadingListener imageLoadingListener2 = imageLoadingListener;
            if (displayImageOptions == null) {
                displayImageOptions = this.configuration.defaultDisplayImageOptions;
            }
            if (TextUtils.isEmpty(str)) {
                this.engine.cancelDisplayTaskFor(imageAware);
                imageLoadingListener2.onLoadingStarted(str, imageAware.getWrappedView());
                if (displayImageOptions.shouldShowImageForEmptyUri()) {
                    imageAware.setImageDrawable(displayImageOptions.getImageForEmptyUri(this.configuration.resources));
                } else {
                    imageAware.setImageDrawable((Drawable) null);
                }
                imageLoadingListener2.onLoadingComplete(str, imageAware.getWrappedView(), (Bitmap) null);
                return;
            }
            ImageSize defineTargetSizeForView = ImageSizeUtils.defineTargetSizeForView(imageAware, this.configuration.getMaxImageSize());
            String generateKey = MemoryCacheUtil.generateKey(str, defineTargetSizeForView);
            this.engine.prepareDisplayTaskFor(imageAware, generateKey);
            imageLoadingListener2.onLoadingStarted(str, imageAware.getWrappedView());
            Bitmap bitmap = this.configuration.memoryCache.get(generateKey);
            if (bitmap == null || bitmap.isRecycled()) {
                if (displayImageOptions.shouldShowImageOnLoading()) {
                    imageAware.setImageDrawable(displayImageOptions.getImageOnLoading(this.configuration.resources));
                } else if (displayImageOptions.isResetViewBeforeLoading()) {
                    imageAware.setImageDrawable((Drawable) null);
                }
                LoadAndDisplayImageTask loadAndDisplayImageTask = new LoadAndDisplayImageTask(this.engine, new ImageLoadingInfo(str, imageAware, defineTargetSizeForView, generateKey, displayImageOptions, imageLoadingListener2, this.engine.getLockForUri(str)), displayImageOptions.getHandler());
                if (displayImageOptions.isSyncLoading()) {
                    loadAndDisplayImageTask.run();
                } else {
                    this.engine.submit(loadAndDisplayImageTask);
                }
            } else {
                if (this.configuration.writeLogs) {
                    L.d(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, generateKey);
                }
                if (displayImageOptions.shouldPostProcess()) {
                    ProcessAndDisplayImageTask processAndDisplayImageTask = new ProcessAndDisplayImageTask(this.engine, bitmap, new ImageLoadingInfo(str, imageAware, defineTargetSizeForView, generateKey, displayImageOptions, imageLoadingListener2, this.engine.getLockForUri(str)), displayImageOptions.getHandler());
                    if (displayImageOptions.isSyncLoading()) {
                        processAndDisplayImageTask.run();
                    } else {
                        this.engine.submit(processAndDisplayImageTask);
                    }
                } else {
                    imageLoadingListener2.onLoadingComplete(str, imageAware.getWrappedView(), displayImageOptions.getDisplayer().display(bitmap, imageAware, LoadedFrom.MEMORY_CACHE));
                }
            }
        } else {
            throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
        }
    }

    public void loadImage(String str, ImageSize imageSize, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener) {
        checkConfiguration();
        if (imageSize == null) {
            imageSize = this.configuration.getMaxImageSize();
        }
        if (displayImageOptions == null) {
            displayImageOptions = this.configuration.defaultDisplayImageOptions;
        }
        displayImage(str, (ImageAware) new ImageNonViewAware(imageSize, ViewScaleType.CROP), displayImageOptions, imageLoadingListener);
    }

    public Bitmap loadImageSync(String str, ImageSize imageSize, DisplayImageOptions displayImageOptions) {
        if (displayImageOptions == null) {
            displayImageOptions = this.configuration.defaultDisplayImageOptions;
        }
        DisplayImageOptions build = new DisplayImageOptions.Builder().cloneFrom(displayImageOptions).syncLoading(true).build();
        SyncImageLoadingListener syncImageLoadingListener = new SyncImageLoadingListener();
        loadImage(str, imageSize, build, syncImageLoadingListener);
        return syncImageLoadingListener.getLoadedBitmap();
    }

    public void displayImage(String str, ImageView imageView) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), (DisplayImageOptions) null, (ImageLoadingListener) null);
    }

    public void displayImage(String str, ImageView imageView, DisplayImageOptions displayImageOptions) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), displayImageOptions, (ImageLoadingListener) null);
    }

    public void displayImage(String str, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), (DisplayImageOptions) null, imageLoadingListener);
    }

    public void displayImage(String str, ImageView imageView, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), displayImageOptions, imageLoadingListener);
    }
}
