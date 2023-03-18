package com.nostra13.dcloudimageloader.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.display.BitmapDisplayer;
import com.nostra13.dcloudimageloader.core.process.BitmapProcessor;

public final class DisplayImageOptions {
    /* access modifiers changed from: private */
    public final boolean cacheInMemory;
    /* access modifiers changed from: private */
    public final boolean cacheOnDisc;
    /* access modifiers changed from: private */
    public final boolean considerExifParams;
    /* access modifiers changed from: private */
    public final BitmapFactory.Options decodingOptions;
    /* access modifiers changed from: private */
    public final int delayBeforeLoading;
    /* access modifiers changed from: private */
    public final BitmapDisplayer displayer;
    /* access modifiers changed from: private */
    public final Object extraForDownloader;
    /* access modifiers changed from: private */
    public final Handler handler;
    /* access modifiers changed from: private */
    public final Drawable imageForEmptyUri;
    /* access modifiers changed from: private */
    public final Drawable imageOnFail;
    /* access modifiers changed from: private */
    public final Drawable imageOnLoading;
    /* access modifiers changed from: private */
    public final int imageResForEmptyUri;
    /* access modifiers changed from: private */
    public final int imageResOnFail;
    /* access modifiers changed from: private */
    public final int imageResOnLoading;
    /* access modifiers changed from: private */
    public final ImageScaleType imageScaleType;
    /* access modifiers changed from: private */
    public final boolean isSyncLoading;
    /* access modifiers changed from: private */
    public final BitmapProcessor postProcessor;
    /* access modifiers changed from: private */
    public final BitmapProcessor preProcessor;
    /* access modifiers changed from: private */
    public final boolean resetViewBeforeLoading;

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean cacheInMemory = false;
        /* access modifiers changed from: private */
        public boolean cacheOnDisc = false;
        /* access modifiers changed from: private */
        public boolean considerExifParams = false;
        /* access modifiers changed from: private */
        public BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
        /* access modifiers changed from: private */
        public int delayBeforeLoading = 0;
        /* access modifiers changed from: private */
        public BitmapDisplayer displayer = DefaultConfigurationFactory.createBitmapDisplayer();
        /* access modifiers changed from: private */
        public Object extraForDownloader = null;
        /* access modifiers changed from: private */
        public Handler handler = null;
        /* access modifiers changed from: private */
        public Drawable imageForEmptyUri = null;
        /* access modifiers changed from: private */
        public Drawable imageOnFail = null;
        /* access modifiers changed from: private */
        public Drawable imageOnLoading = null;
        /* access modifiers changed from: private */
        public int imageResForEmptyUri = 0;
        /* access modifiers changed from: private */
        public int imageResOnFail = 0;
        /* access modifiers changed from: private */
        public int imageResOnLoading = 0;
        /* access modifiers changed from: private */
        public ImageScaleType imageScaleType = ImageScaleType.IN_SAMPLE_POWER_OF_2;
        /* access modifiers changed from: private */
        public boolean isSyncLoading = false;
        /* access modifiers changed from: private */
        public BitmapProcessor postProcessor = null;
        /* access modifiers changed from: private */
        public BitmapProcessor preProcessor = null;
        /* access modifiers changed from: private */
        public boolean resetViewBeforeLoading = false;

        public Builder() {
            BitmapFactory.Options options = this.decodingOptions;
            options.inPurgeable = true;
            options.inInputShareable = true;
        }

        public Builder bitmapConfig(Bitmap.Config config) {
            if (config != null) {
                this.decodingOptions.inPreferredConfig = config;
                return this;
            }
            throw new IllegalArgumentException("bitmapConfig can't be null");
        }

        public DisplayImageOptions build() {
            return new DisplayImageOptions(this);
        }

        public Builder cacheInMemory() {
            this.cacheInMemory = true;
            return this;
        }

        public Builder cacheOnDisc() {
            this.cacheOnDisc = true;
            return this;
        }

        public Builder cloneFrom(DisplayImageOptions displayImageOptions) {
            this.imageResOnLoading = displayImageOptions.imageResOnLoading;
            this.imageResForEmptyUri = displayImageOptions.imageResForEmptyUri;
            this.imageResOnFail = displayImageOptions.imageResOnFail;
            this.imageOnLoading = displayImageOptions.imageOnLoading;
            this.imageForEmptyUri = displayImageOptions.imageForEmptyUri;
            this.imageOnFail = displayImageOptions.imageOnFail;
            this.resetViewBeforeLoading = displayImageOptions.resetViewBeforeLoading;
            this.cacheInMemory = displayImageOptions.cacheInMemory;
            this.cacheOnDisc = displayImageOptions.cacheOnDisc;
            this.imageScaleType = displayImageOptions.imageScaleType;
            this.decodingOptions = displayImageOptions.decodingOptions;
            this.delayBeforeLoading = displayImageOptions.delayBeforeLoading;
            this.considerExifParams = displayImageOptions.considerExifParams;
            this.extraForDownloader = displayImageOptions.extraForDownloader;
            this.preProcessor = displayImageOptions.preProcessor;
            this.postProcessor = displayImageOptions.postProcessor;
            this.displayer = displayImageOptions.displayer;
            this.handler = displayImageOptions.handler;
            this.isSyncLoading = displayImageOptions.isSyncLoading;
            return this;
        }

        public Builder considerExifParams(boolean z) {
            this.considerExifParams = z;
            return this;
        }

        public Builder decodingOptions(BitmapFactory.Options options) {
            if (options != null) {
                this.decodingOptions = options;
                return this;
            }
            throw new IllegalArgumentException("decodingOptions can't be null");
        }

        public Builder delayBeforeLoading(int i) {
            this.delayBeforeLoading = i;
            return this;
        }

        public Builder displayer(BitmapDisplayer bitmapDisplayer) {
            if (bitmapDisplayer != null) {
                this.displayer = bitmapDisplayer;
                return this;
            }
            throw new IllegalArgumentException("displayer can't be null");
        }

        public Builder extraForDownloader(Object obj) {
            this.extraForDownloader = obj;
            return this;
        }

        public Builder handler(Handler handler2) {
            this.handler = handler2;
            return this;
        }

        public Builder imageScaleType(ImageScaleType imageScaleType2) {
            this.imageScaleType = imageScaleType2;
            return this;
        }

        public Builder postProcessor(BitmapProcessor bitmapProcessor) {
            this.postProcessor = bitmapProcessor;
            return this;
        }

        public Builder preProcessor(BitmapProcessor bitmapProcessor) {
            this.preProcessor = bitmapProcessor;
            return this;
        }

        public Builder resetViewBeforeLoading() {
            this.resetViewBeforeLoading = true;
            return this;
        }

        public Builder showImageForEmptyUri(int i) {
            this.imageResForEmptyUri = i;
            return this;
        }

        public Builder showImageOnFail(int i) {
            this.imageResOnFail = i;
            return this;
        }

        public Builder showImageOnLoading(int i) {
            this.imageResOnLoading = i;
            return this;
        }

        @Deprecated
        public Builder showStubImage(int i) {
            this.imageResOnLoading = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder syncLoading(boolean z) {
            this.isSyncLoading = z;
            return this;
        }

        public Builder cacheInMemory(boolean z) {
            this.cacheInMemory = z;
            return this;
        }

        public Builder cacheOnDisc(boolean z) {
            this.cacheOnDisc = z;
            return this;
        }

        public Builder resetViewBeforeLoading(boolean z) {
            this.resetViewBeforeLoading = z;
            return this;
        }

        public Builder showImageForEmptyUri(Drawable drawable) {
            this.imageForEmptyUri = drawable;
            return this;
        }

        public Builder showImageOnFail(Drawable drawable) {
            this.imageOnFail = drawable;
            return this;
        }

        public Builder showImageOnLoading(Drawable drawable) {
            this.imageOnLoading = drawable;
            return this;
        }
    }

    public static DisplayImageOptions createSimple() {
        return new Builder().build();
    }

    public BitmapFactory.Options getDecodingOptions() {
        return this.decodingOptions;
    }

    public int getDelayBeforeLoading() {
        return this.delayBeforeLoading;
    }

    public BitmapDisplayer getDisplayer() {
        return this.displayer;
    }

    public Object getExtraForDownloader() {
        return this.extraForDownloader;
    }

    public Handler getHandler() {
        if (this.isSyncLoading) {
            return null;
        }
        Handler handler2 = this.handler;
        if (handler2 != null) {
            return handler2;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return new Handler();
        }
        throw new IllegalStateException("ImageLoader.displayImage(...) must be invoked from the main thread or from Looper thread");
    }

    public Drawable getImageForEmptyUri(Resources resources) {
        int i = this.imageResForEmptyUri;
        return i != 0 ? resources.getDrawable(i) : this.imageForEmptyUri;
    }

    public Drawable getImageOnFail(Resources resources) {
        int i = this.imageResOnFail;
        return i != 0 ? resources.getDrawable(i) : this.imageOnFail;
    }

    public Drawable getImageOnLoading(Resources resources) {
        int i = this.imageResOnLoading;
        return i != 0 ? resources.getDrawable(i) : this.imageOnLoading;
    }

    public ImageScaleType getImageScaleType() {
        return this.imageScaleType;
    }

    public BitmapProcessor getPostProcessor() {
        return this.postProcessor;
    }

    public BitmapProcessor getPreProcessor() {
        return this.preProcessor;
    }

    public boolean isCacheInMemory() {
        return this.cacheInMemory;
    }

    public boolean isCacheOnDisc() {
        return this.cacheOnDisc;
    }

    public boolean isConsiderExifParams() {
        return this.considerExifParams;
    }

    public boolean isResetViewBeforeLoading() {
        return this.resetViewBeforeLoading;
    }

    /* access modifiers changed from: package-private */
    public boolean isSyncLoading() {
        return this.isSyncLoading;
    }

    public boolean shouldDelayBeforeLoading() {
        return this.delayBeforeLoading > 0;
    }

    public boolean shouldPostProcess() {
        return this.postProcessor != null;
    }

    public boolean shouldPreProcess() {
        return this.preProcessor != null;
    }

    public boolean shouldShowImageForEmptyUri() {
        return (this.imageForEmptyUri == null && this.imageResForEmptyUri == 0) ? false : true;
    }

    public boolean shouldShowImageOnFail() {
        return (this.imageOnFail == null && this.imageResOnFail == 0) ? false : true;
    }

    public boolean shouldShowImageOnLoading() {
        return (this.imageOnLoading == null && this.imageResOnLoading == 0) ? false : true;
    }

    private DisplayImageOptions(Builder builder) {
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResForEmptyUri = builder.imageResForEmptyUri;
        this.imageResOnFail = builder.imageResOnFail;
        this.imageOnLoading = builder.imageOnLoading;
        this.imageForEmptyUri = builder.imageForEmptyUri;
        this.imageOnFail = builder.imageOnFail;
        this.resetViewBeforeLoading = builder.resetViewBeforeLoading;
        this.cacheInMemory = builder.cacheInMemory;
        this.cacheOnDisc = builder.cacheOnDisc;
        this.imageScaleType = builder.imageScaleType;
        this.decodingOptions = builder.decodingOptions;
        this.delayBeforeLoading = builder.delayBeforeLoading;
        this.considerExifParams = builder.considerExifParams;
        this.extraForDownloader = builder.extraForDownloader;
        this.preProcessor = builder.preProcessor;
        this.postProcessor = builder.postProcessor;
        this.displayer = builder.displayer;
        this.handler = builder.handler;
        this.isSyncLoading = builder.isSyncLoading;
    }
}
