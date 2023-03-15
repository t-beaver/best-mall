package com.alibaba.android.bindingx.core;

import java.lang.ref.WeakReference;

public class WeakRunnable implements Runnable {
    private final WeakReference<Runnable> mDelegateRunnable;

    public WeakRunnable(Runnable runnable) {
        this.mDelegateRunnable = new WeakReference<>(runnable);
    }

    public void run() {
        Runnable runnable = (Runnable) this.mDelegateRunnable.get();
        if (runnable != null) {
            runnable.run();
        }
    }
}
