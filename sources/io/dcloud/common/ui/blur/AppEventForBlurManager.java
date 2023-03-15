package io.dcloud.common.ui.blur;

import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.util.ThreadPool;
import java.util.ArrayList;
import java.util.Iterator;

public class AppEventForBlurManager {
    private static final String TAG = "AppScrollManager";
    /* access modifiers changed from: private */
    public static ArrayList<OnAppChangedCallBack> callBacks = new ArrayList<>();
    public static final boolean isBlur = true;
    /* access modifiers changed from: private */
    public static boolean mLoop = false;
    /* access modifiers changed from: private */
    public static long sLastChangedTime = 0;
    /* access modifiers changed from: private */
    public static boolean sScrollStart = false;

    public interface OnAppChangedCallBack {
        void onContentScrollEnd();

        void onContentScrollStart();

        void onSplashclosed();
    }

    public static synchronized void addEventChangedCallBack(OnAppChangedCallBack onAppChangedCallBack) {
        synchronized (AppEventForBlurManager.class) {
            if (!callBacks.contains(onAppChangedCallBack)) {
                callBacks.add(onAppChangedCallBack);
            }
        }
    }

    public static void onScrollChanged(int i, int i2) {
        sScrollStart = true;
        sLastChangedTime = System.currentTimeMillis();
        if (!mLoop) {
            onScrollStart();
        }
        startLoop();
    }

    /* access modifiers changed from: private */
    public static void onScrollEnd() {
        MessageHandler.post(new Runnable() {
            public void run() {
                Iterator it = AppEventForBlurManager.callBacks.iterator();
                while (it.hasNext()) {
                    ((OnAppChangedCallBack) it.next()).onContentScrollEnd();
                }
            }
        });
    }

    private static void onScrollStart() {
        MessageHandler.post(new Runnable() {
            public void run() {
                Iterator it = AppEventForBlurManager.callBacks.iterator();
                while (it.hasNext()) {
                    ((OnAppChangedCallBack) it.next()).onContentScrollStart();
                }
            }
        });
    }

    public static void onSplashclosed() {
        MessageHandler.post(new Runnable() {
            public void run() {
                Iterator it = AppEventForBlurManager.callBacks.iterator();
                while (it.hasNext()) {
                    ((OnAppChangedCallBack) it.next()).onSplashclosed();
                }
            }
        });
    }

    public static synchronized void removeEventChangedCallBack(OnAppChangedCallBack onAppChangedCallBack) {
        synchronized (AppEventForBlurManager.class) {
            if (callBacks.contains(onAppChangedCallBack)) {
                callBacks.remove(onAppChangedCallBack);
            }
        }
    }

    private static void startLoop() {
        if (!mLoop) {
            mLoop = true;
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    while (AppEventForBlurManager.sScrollStart) {
                        boolean unused = AppEventForBlurManager.mLoop = true;
                        if (System.currentTimeMillis() - AppEventForBlurManager.sLastChangedTime > 500) {
                            boolean unused2 = AppEventForBlurManager.sScrollStart = false;
                            boolean unused3 = AppEventForBlurManager.mLoop = false;
                            long unused4 = AppEventForBlurManager.sLastChangedTime = 0;
                            AppEventForBlurManager.onScrollEnd();
                        } else {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, true);
        }
    }
}
