package com.nostra13.dcloudimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.core.decode.ImageDecoder;
import com.nostra13.dcloudimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import com.nostra13.dcloudimageloader.utils.IoUtils;
import com.nostra13.dcloudimageloader.utils.L;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

final class LoadAndDisplayImageTask implements Runnable {
    private static final int BUFFER_SIZE = 32768;
    private static final String ERROR_POST_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISC_CACHE_NULL = "Bitmap processor for disc cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC = "Process image before cache on disc [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    /* access modifiers changed from: private */
    public final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private boolean imageAwareCollected = false;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom = LoadedFrom.NETWORK;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    private final ImageDownloader slowNetworkDownloader;
    private final ImageSize targetSize;
    final String uri;
    private final boolean writeLogs;

    public LoadAndDisplayImageTask(ImageLoaderEngine imageLoaderEngine, ImageLoadingInfo imageLoadingInfo2, Handler handler2) {
        this.engine = imageLoaderEngine;
        this.imageLoadingInfo = imageLoadingInfo2;
        this.handler = handler2;
        ImageLoaderConfiguration imageLoaderConfiguration = imageLoaderEngine.configuration;
        this.configuration = imageLoaderConfiguration;
        this.downloader = imageLoaderConfiguration.downloader;
        this.networkDeniedDownloader = imageLoaderConfiguration.networkDeniedDownloader;
        this.slowNetworkDownloader = imageLoaderConfiguration.slowNetworkDownloader;
        this.decoder = imageLoaderConfiguration.decoder;
        this.writeLogs = imageLoaderConfiguration.writeLogs;
        this.uri = imageLoadingInfo2.uri;
        this.memoryCacheKey = imageLoadingInfo2.memoryCacheKey;
        this.imageAware = imageLoadingInfo2.imageAware;
        this.targetSize = imageLoadingInfo2.targetSize;
        this.options = imageLoadingInfo2.options;
        this.listener = imageLoadingInfo2.listener;
    }

    private boolean checkTaskIsInterrupted() {
        boolean interrupted = Thread.interrupted();
        if (interrupted) {
            log(LOG_TASK_INTERRUPTED);
        }
        return interrupted;
    }

    private boolean checkTaskIsNotActual() {
        return checkViewCollected() || checkViewReused();
    }

    private boolean checkViewCollected() {
        if (!this.imageAware.isCollected()) {
            return false;
        }
        this.imageAwareCollected = true;
        log(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED);
        fireCancelEvent();
        return true;
    }

    private boolean checkViewReused() {
        boolean z = !this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware));
        if (z) {
            log(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED);
            fireCancelEvent();
        }
        return z;
    }

    private Bitmap decodeImage(String str) throws IOException {
        ViewScaleType scaleType;
        if (checkViewCollected() || (scaleType = this.imageAware.getScaleType()) == null) {
            return null;
        }
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str, this.targetSize, scaleType, getDownloader(), this.options));
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        log(LOG_DELAY_BEFORE_LOADING, Integer.valueOf(this.options.getDelayBeforeLoading()), this.memoryCacheKey);
        try {
            Thread.sleep((long) this.options.getDelayBeforeLoading());
            return checkTaskIsNotActual();
        } catch (InterruptedException unused) {
            L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
            return true;
        }
    }

    private void downloadImage(File file) throws IOException {
        BufferedOutputStream stream = getDownloader().getStream(this.uri, this.options.getExtraForDownloader());
        try {
            stream = new BufferedOutputStream(new FileOutputStream(file), 1195);
            IoUtils.copyStream(stream, stream);
        } catch (Throwable th) {
            throw th;
        } finally {
            IoUtils.closeSilently(stream);
        }
    }

    /* JADX INFO: finally extract failed */
    private boolean downloadSizedImage(File file, int i, int i2) throws IOException {
        Bitmap decode = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, this.uri, new ImageSize(i, i2), ViewScaleType.FIT_INSIDE, getDownloader(), new DisplayImageOptions.Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
        if (decode == null) {
            return false;
        }
        if (this.configuration.processorForDiscCache != null) {
            log(LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC);
            decode = this.configuration.processorForDiscCache.process(decode);
            if (decode == null) {
                L.e(ERROR_PROCESSOR_FOR_DISC_CACHE_NULL, this.memoryCacheKey);
                return false;
            }
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 1195);
        try {
            ImageLoaderConfiguration imageLoaderConfiguration = this.configuration;
            boolean compress = decode.compress(imageLoaderConfiguration.imageCompressFormatForDiscCache, imageLoaderConfiguration.imageQualityForDiscCache, bufferedOutputStream);
            IoUtils.closeSilently(bufferedOutputStream);
            decode.recycle();
            return compress;
        } catch (Throwable th) {
            IoUtils.closeSilently(bufferedOutputStream);
            throw th;
        }
    }

    private void fireCancelEvent() {
        if (Thread.interrupted()) {
            return;
        }
        if (this.options.isSyncLoading()) {
            this.listener.onLoadingCancelled(this.uri, this.imageAware.getWrappedView());
        } else {
            this.handler.post(new Runnable() {
                public void run() {
                    LoadAndDisplayImageTask loadAndDisplayImageTask = LoadAndDisplayImageTask.this;
                    loadAndDisplayImageTask.listener.onLoadingCancelled(loadAndDisplayImageTask.uri, loadAndDisplayImageTask.imageAware.getWrappedView());
                }
            });
        }
    }

    private void fireFailEvent(final FailReason.FailType failType, final Throwable th) {
        if (Thread.interrupted()) {
            return;
        }
        if (this.options.isSyncLoading()) {
            this.listener.onLoadingFailed(this.uri, this.imageAware.getWrappedView(), new FailReason(failType, th));
        } else {
            this.handler.post(new Runnable() {
                public void run() {
                    if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail()) {
                        LoadAndDisplayImageTask loadAndDisplayImageTask = LoadAndDisplayImageTask.this;
                        loadAndDisplayImageTask.imageAware.setImageDrawable(loadAndDisplayImageTask.options.getImageOnFail(loadAndDisplayImageTask.configuration.resources));
                    }
                    LoadAndDisplayImageTask loadAndDisplayImageTask2 = LoadAndDisplayImageTask.this;
                    loadAndDisplayImageTask2.listener.onLoadingFailed(loadAndDisplayImageTask2.uri, loadAndDisplayImageTask2.imageAware.getWrappedView(), new FailReason(failType, th));
                }
            });
        }
    }

    private ImageDownloader getDownloader() {
        if (this.engine.isNetworkDenied()) {
            return this.networkDeniedDownloader;
        }
        if (this.engine.isSlowNetwork()) {
            return this.slowNetworkDownloader;
        }
        return this.downloader;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001c, code lost:
        r0 = r3.configuration.reserveDiscCache.get(r3.uri);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File getImageFileInDiscCache() {
        /*
            r3 = this;
            com.nostra13.dcloudimageloader.core.ImageLoaderConfiguration r0 = r3.configuration
            com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware r0 = r0.discCache
            java.lang.String r1 = r3.uri
            java.io.File r0 = r0.get(r1)
            java.io.File r1 = r0.getParentFile()
            if (r1 == 0) goto L_0x001c
            boolean r2 = r1.exists()
            if (r2 != 0) goto L_0x0035
            boolean r1 = r1.mkdirs()
            if (r1 != 0) goto L_0x0035
        L_0x001c:
            com.nostra13.dcloudimageloader.core.ImageLoaderConfiguration r0 = r3.configuration
            com.nostra13.dcloudimageloader.cache.disc.DiscCacheAware r0 = r0.reserveDiscCache
            java.lang.String r1 = r3.uri
            java.io.File r0 = r0.get(r1)
            java.io.File r1 = r0.getParentFile()
            if (r1 == 0) goto L_0x0035
            boolean r2 = r1.exists()
            if (r2 != 0) goto L_0x0035
            r1.mkdirs()
        L_0x0035:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.dcloudimageloader.core.LoadAndDisplayImageTask.getImageFileInDiscCache():java.io.File");
    }

    private void log(String str) {
        if (this.writeLogs) {
            L.d(str, this.memoryCacheKey);
        }
    }

    private String tryCacheImageOnDisc(File file) {
        log(LOG_CACHE_IMAGE_ON_DISC);
        try {
            ImageLoaderConfiguration imageLoaderConfiguration = this.configuration;
            int i = imageLoaderConfiguration.maxImageWidthForDiscCache;
            int i2 = imageLoaderConfiguration.maxImageHeightForDiscCache;
            boolean z = false;
            if (i > 0 || i2 > 0) {
                z = downloadSizedImage(file, i, i2);
            }
            if (!z) {
                downloadImage(file);
            }
            this.configuration.discCache.put(this.uri, file);
            return ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath());
        } catch (IOException e) {
            L.e(e);
            if (file.exists()) {
                file.delete();
            }
            return this.uri;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap tryLoadBitmap() {
        /*
            r6 = this;
            java.io.File r0 = r6.getImageFileInDiscCache()
            r1 = 0
            boolean r2 = r0.exists()     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            if (r2 == 0) goto L_0x0027
            java.lang.String r2 = "Load image from disc cache [%s]"
            r6.log(r2)     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            com.nostra13.dcloudimageloader.core.assist.LoadedFrom r2 = com.nostra13.dcloudimageloader.core.assist.LoadedFrom.DISC_CACHE     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            r6.loadedFrom = r2     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r2 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            java.lang.String r3 = r0.getAbsolutePath()     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            java.lang.String r2 = r2.wrap(r3)     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            android.graphics.Bitmap r2 = r6.decodeImage(r2)     // Catch:{ IllegalStateException -> 0x00a4, IOException -> 0x008e, OutOfMemoryError -> 0x0083, all -> 0x0079 }
            boolean r3 = r6.imageAwareCollected     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 == 0) goto L_0x0028
            return r1
        L_0x0027:
            r2 = r1
        L_0x0028:
            if (r2 == 0) goto L_0x0036
            int r3 = r2.getWidth()     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 <= 0) goto L_0x0036
            int r3 = r2.getHeight()     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 > 0) goto L_0x00aa
        L_0x0036:
            java.lang.String r3 = "Load image from network [%s]"
            r6.log(r3)     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            com.nostra13.dcloudimageloader.core.assist.LoadedFrom r3 = com.nostra13.dcloudimageloader.core.assist.LoadedFrom.NETWORK     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            r6.loadedFrom = r3     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            com.nostra13.dcloudimageloader.core.DisplayImageOptions r3 = r6.options     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            boolean r3 = r3.isCacheOnDisc()     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 == 0) goto L_0x004c
            java.lang.String r3 = r6.tryCacheImageOnDisc(r0)     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            goto L_0x004e
        L_0x004c:
            java.lang.String r3 = r6.uri     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
        L_0x004e:
            boolean r4 = r6.checkTaskIsNotActual()     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r4 != 0) goto L_0x00aa
            android.graphics.Bitmap r2 = r6.decodeImage(r3)     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            boolean r3 = r6.imageAwareCollected     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 == 0) goto L_0x005d
            return r1
        L_0x005d:
            if (r2 == 0) goto L_0x006b
            int r3 = r2.getWidth()     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 <= 0) goto L_0x006b
            int r3 = r2.getHeight()     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            if (r3 > 0) goto L_0x00aa
        L_0x006b:
            com.nostra13.dcloudimageloader.core.assist.FailReason$FailType r3 = com.nostra13.dcloudimageloader.core.assist.FailReason.FailType.DECODING_ERROR     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            r6.fireFailEvent(r3, r1)     // Catch:{ IllegalStateException -> 0x00a5, IOException -> 0x0077, OutOfMemoryError -> 0x0074, all -> 0x0071 }
            goto L_0x00aa
        L_0x0071:
            r0 = move-exception
            r1 = r2
            goto L_0x007a
        L_0x0074:
            r0 = move-exception
            r1 = r2
            goto L_0x0084
        L_0x0077:
            r1 = move-exception
            goto L_0x0092
        L_0x0079:
            r0 = move-exception
        L_0x007a:
            com.nostra13.dcloudimageloader.utils.L.e(r0)
            com.nostra13.dcloudimageloader.core.assist.FailReason$FailType r2 = com.nostra13.dcloudimageloader.core.assist.FailReason.FailType.UNKNOWN
            r6.fireFailEvent(r2, r0)
            goto L_0x008c
        L_0x0083:
            r0 = move-exception
        L_0x0084:
            com.nostra13.dcloudimageloader.utils.L.e(r0)
            com.nostra13.dcloudimageloader.core.assist.FailReason$FailType r2 = com.nostra13.dcloudimageloader.core.assist.FailReason.FailType.OUT_OF_MEMORY
            r6.fireFailEvent(r2, r0)
        L_0x008c:
            r2 = r1
            goto L_0x00aa
        L_0x008e:
            r2 = move-exception
            r5 = r2
            r2 = r1
            r1 = r5
        L_0x0092:
            com.nostra13.dcloudimageloader.utils.L.e(r1)
            com.nostra13.dcloudimageloader.core.assist.FailReason$FailType r3 = com.nostra13.dcloudimageloader.core.assist.FailReason.FailType.IO_ERROR
            r6.fireFailEvent(r3, r1)
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x00aa
            r0.delete()
            goto L_0x00aa
        L_0x00a4:
            r2 = r1
        L_0x00a5:
            com.nostra13.dcloudimageloader.core.assist.FailReason$FailType r0 = com.nostra13.dcloudimageloader.core.assist.FailReason.FailType.NETWORK_DENIED
            r6.fireFailEvent(r0, r1)
        L_0x00aa:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.dcloudimageloader.core.LoadAndDisplayImageTask.tryLoadBitmap():android.graphics.Bitmap");
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        synchronized (pause) {
            if (pause.get()) {
                log(LOG_WAITING_FOR_RESUME);
                try {
                    pause.wait();
                    log(LOG_RESUME_AFTER_PAUSE);
                } catch (InterruptedException unused) {
                    L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
                    return true;
                }
            }
        }
        return checkTaskIsNotActual();
    }

    /* access modifiers changed from: package-private */
    public String getLoadingUri() {
        return this.uri;
    }

    public void run() {
        if (!waitIfPaused() && !delayIfNeed()) {
            ReentrantLock reentrantLock = this.imageLoadingInfo.loadFromUriLock;
            log(LOG_START_DISPLAY_IMAGE_TASK);
            if (reentrantLock.isLocked()) {
                log(LOG_WAITING_FOR_IMAGE_LOADED);
            }
            reentrantLock.lock();
            try {
                if (!checkTaskIsNotActual()) {
                    Bitmap bitmap = this.configuration.memoryCache.get(this.memoryCacheKey);
                    if (bitmap == null) {
                        bitmap = tryLoadBitmap();
                        if (this.imageAwareCollected) {
                            reentrantLock.unlock();
                            return;
                        } else if (bitmap == null) {
                            reentrantLock.unlock();
                            return;
                        } else {
                            if (!checkTaskIsNotActual()) {
                                if (!checkTaskIsInterrupted()) {
                                    if (this.options.shouldPreProcess()) {
                                        log(LOG_PREPROCESS_IMAGE);
                                        bitmap = this.options.getPreProcessor().process(bitmap);
                                        if (bitmap == null) {
                                            L.e("Pre-processor returned null [%s]", new Object[0]);
                                        }
                                    }
                                    if (bitmap != null && this.options.isCacheInMemory()) {
                                        log(LOG_CACHE_IMAGE_IN_MEMORY);
                                        this.configuration.memoryCache.put(this.memoryCacheKey, bitmap);
                                    }
                                }
                            }
                            reentrantLock.unlock();
                            return;
                        }
                    } else {
                        this.loadedFrom = LoadedFrom.MEMORY_CACHE;
                        log(LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING);
                    }
                    if (bitmap != null && this.options.shouldPostProcess()) {
                        log(LOG_POSTPROCESS_IMAGE);
                        bitmap = this.options.getPostProcessor().process(bitmap);
                        if (bitmap == null) {
                            L.e("Pre-processor returned null [%s]", this.memoryCacheKey);
                        }
                    }
                    reentrantLock.unlock();
                    if (!checkTaskIsNotActual() && !checkTaskIsInterrupted()) {
                        DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bitmap, this.imageLoadingInfo, this.engine, this.loadedFrom);
                        displayBitmapTask.setLoggingEnabled(this.writeLogs);
                        if (this.options.isSyncLoading()) {
                            displayBitmapTask.run();
                        } else {
                            this.handler.post(displayBitmapTask);
                        }
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    private void log(String str, Object... objArr) {
        if (this.writeLogs) {
            L.d(str, objArr);
        }
    }
}
