package io.dcloud.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final int MAX_COUNT = 3;
    ExecutorService newFixedThreadPool;
    ExecutorService singleThreadPool;
    ThreadPoolExecutor threadPool;

    static class ThreadPoolHolder {
        static ThreadPool mInstance = new ThreadPool();

        ThreadPoolHolder() {
        }
    }

    public static ThreadPool self() {
        return ThreadPoolHolder.mInstance;
    }

    public synchronized void addSingleThreadTask(Runnable runnable) {
        ExecutorService executorService = this.singleThreadPool;
        if (executorService != null) {
            executorService.execute(runnable);
        }
    }

    public synchronized void addThreadTask(Runnable runnable) {
        addThreadTask(runnable, false);
    }

    private ThreadPool() {
        this.threadPool = null;
        this.singleThreadPool = null;
        this.newFixedThreadPool = null;
        this.threadPool = new ThreadPoolExecutor(3, 3, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
        this.newFixedThreadPool = new ThreadPoolExecutor(3, 50, 300, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this.singleThreadPool = Executors.newSingleThreadExecutor();
    }

    public synchronized void addThreadTask(Runnable runnable, boolean z) {
        if (z) {
            this.newFixedThreadPool.execute(runnable);
        } else {
            this.threadPool.execute(runnable);
        }
    }
}
