package com.taobao.weex.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class WXWorkThreadManager {
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void post(Runnable runnable) {
        ExecutorService executorService = this.singleThreadExecutor;
        if (executorService != null) {
            executorService.execute(runnable);
        }
    }

    public void destroy() {
        ExecutorService executorService = this.singleThreadExecutor;
        if (executorService != null) {
            executorService.shutdown();
        }
        this.singleThreadExecutor = null;
    }
}
