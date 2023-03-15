package com.nostra13.dcloudimageloader.core;

import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

class ImageLoaderEngine {
    private final Map<Integer, String> cacheKeysForImageAwares = Collections.synchronizedMap(new HashMap());
    final ImageLoaderConfiguration configuration;
    private final AtomicBoolean networkDenied = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final AtomicBoolean slowNetwork = new AtomicBoolean(false);
    private ExecutorService taskDistributor;
    /* access modifiers changed from: private */
    public Executor taskExecutor;
    /* access modifiers changed from: private */
    public Executor taskExecutorForCachedImages;
    private final Map<String, ReentrantLock> uriLocks = new WeakHashMap();

    ImageLoaderEngine(ImageLoaderConfiguration imageLoaderConfiguration) {
        this.configuration = imageLoaderConfiguration;
        this.taskExecutor = imageLoaderConfiguration.taskExecutor;
        this.taskExecutorForCachedImages = imageLoaderConfiguration.taskExecutorForCachedImages;
        this.taskDistributor = Executors.newCachedThreadPool();
    }

    private Executor createTaskExecutor() {
        ImageLoaderConfiguration imageLoaderConfiguration = this.configuration;
        return DefaultConfigurationFactory.createExecutor(imageLoaderConfiguration.threadPoolSize, imageLoaderConfiguration.threadPriority, imageLoaderConfiguration.tasksProcessingType);
    }

    /* access modifiers changed from: private */
    public void initExecutorsIfNeed() {
        if (!this.configuration.customExecutor && ((ExecutorService) this.taskExecutor).isShutdown()) {
            this.taskExecutor = createTaskExecutor();
        }
        if (!this.configuration.customExecutorForCachedImages && ((ExecutorService) this.taskExecutorForCachedImages).isShutdown()) {
            this.taskExecutorForCachedImages = createTaskExecutor();
        }
    }

    /* access modifiers changed from: package-private */
    public void cancelDisplayTaskFor(ImageAware imageAware) {
        this.cacheKeysForImageAwares.remove(Integer.valueOf(imageAware.getId()));
    }

    /* access modifiers changed from: package-private */
    public void denyNetworkDownloads(boolean z) {
        this.networkDenied.set(z);
    }

    /* access modifiers changed from: package-private */
    public String getLoadingUriForView(ImageAware imageAware) {
        return this.cacheKeysForImageAwares.get(Integer.valueOf(imageAware.getId()));
    }

    /* access modifiers changed from: package-private */
    public ReentrantLock getLockForUri(String str) {
        ReentrantLock reentrantLock = this.uriLocks.get(str);
        if (reentrantLock != null) {
            return reentrantLock;
        }
        ReentrantLock reentrantLock2 = new ReentrantLock();
        this.uriLocks.put(str, reentrantLock2);
        return reentrantLock2;
    }

    /* access modifiers changed from: package-private */
    public AtomicBoolean getPause() {
        return this.paused;
    }

    /* access modifiers changed from: package-private */
    public void handleSlowNetwork(boolean z) {
        this.slowNetwork.set(z);
    }

    /* access modifiers changed from: package-private */
    public boolean isNetworkDenied() {
        return this.networkDenied.get();
    }

    /* access modifiers changed from: package-private */
    public boolean isSlowNetwork() {
        return this.slowNetwork.get();
    }

    /* access modifiers changed from: package-private */
    public void pause() {
        this.paused.set(true);
    }

    /* access modifiers changed from: package-private */
    public void prepareDisplayTaskFor(ImageAware imageAware, String str) {
        this.cacheKeysForImageAwares.put(Integer.valueOf(imageAware.getId()), str);
    }

    /* access modifiers changed from: package-private */
    public void resume() {
        synchronized (this.paused) {
            this.paused.set(false);
            this.paused.notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        if (!this.configuration.customExecutor) {
            ((ExecutorService) this.taskExecutor).shutdownNow();
        }
        if (!this.configuration.customExecutorForCachedImages) {
            ((ExecutorService) this.taskExecutorForCachedImages).shutdownNow();
        }
        this.cacheKeysForImageAwares.clear();
        this.uriLocks.clear();
    }

    /* access modifiers changed from: package-private */
    public void submit(final LoadAndDisplayImageTask loadAndDisplayImageTask) {
        this.taskDistributor.execute(new Runnable() {
            public void run() {
                boolean exists = ImageLoaderEngine.this.configuration.discCache.get(loadAndDisplayImageTask.getLoadingUri()).exists();
                ImageLoaderEngine.this.initExecutorsIfNeed();
                if (exists) {
                    ImageLoaderEngine.this.taskExecutorForCachedImages.execute(loadAndDisplayImageTask);
                } else {
                    ImageLoaderEngine.this.taskExecutor.execute(loadAndDisplayImageTask);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void submit(ProcessAndDisplayImageTask processAndDisplayImageTask) {
        initExecutorsIfNeed();
        this.taskExecutorForCachedImages.execute(processAndDisplayImageTask);
    }
}
