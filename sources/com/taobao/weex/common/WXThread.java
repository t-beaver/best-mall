package com.taobao.weex.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.LogDetail;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.lang.ref.WeakReference;

public class WXThread extends HandlerThread {
    private Handler mHandler;

    static class SafeRunnable implements Runnable {
        static final String TAG = "SafeRunnable";
        private WeakReference<WXSDKInstance> mInstance;
        final Runnable mTask;
        private LogDetail mTimelineLog;

        SafeRunnable(Runnable runnable) {
            this(runnable, (String) null);
        }

        SafeRunnable(Runnable runnable, String str) {
            this(runnable, (WXSDKInstance) null, str);
        }

        SafeRunnable(Runnable runnable, WXSDKInstance wXSDKInstance, String str) {
            this.mTimelineLog = null;
            this.mTask = runnable;
            if (str != null) {
                LogDetail logDetail = new LogDetail();
                this.mTimelineLog = logDetail;
                logDetail.info.platform = TimeCalculator.PLATFORM_ANDROID;
                this.mTimelineLog.name(str);
                this.mInstance = new WeakReference<>(wXSDKInstance);
            }
        }

        public void run() {
            WeakReference<WXSDKInstance> weakReference;
            WXSDKInstance wXSDKInstance;
            try {
                if (this.mTask != null) {
                    LogDetail logDetail = this.mTimelineLog;
                    if (logDetail != null) {
                        logDetail.taskStart();
                    }
                    this.mTask.run();
                    LogDetail logDetail2 = this.mTimelineLog;
                    if (logDetail2 != null) {
                        logDetail2.taskEnd();
                    }
                }
            } catch (Throwable th) {
                if (!WXEnvironment.isApkDebugable()) {
                    WXLogUtils.w(TAG, th);
                } else {
                    WXLogUtils.e(TAG, "SafeRunnable run throw expection:" + th.getMessage());
                    throw th;
                }
            }
            if (this.mTimelineLog != null && (weakReference = this.mInstance) != null && (wXSDKInstance = (WXSDKInstance) weakReference.get()) != null && wXSDKInstance.mTimeCalculator != null) {
                wXSDKInstance.mTimeCalculator.addLog(this.mTimelineLog);
            }
        }
    }

    static class SafeCallback implements Handler.Callback {
        static final String TAG = "SafeCallback";
        final Handler.Callback mCallback;

        SafeCallback(Handler.Callback callback) {
            this.mCallback = callback;
        }

        public boolean handleMessage(Message message) {
            try {
                Handler.Callback callback = this.mCallback;
                if (callback != null) {
                    return callback.handleMessage(message);
                }
                return false;
            } catch (Throwable th) {
                if (!WXEnvironment.isApkDebugable()) {
                    return false;
                }
                WXLogUtils.e(TAG, "SafeCallback handleMessage throw expection:" + th.getMessage());
                throw th;
            }
        }
    }

    public static Runnable secure(Runnable runnable) {
        return secure(runnable, (WXSDKInstance) null, (String) null);
    }

    public static Runnable secure(Runnable runnable, WXSDKInstance wXSDKInstance, String str) {
        return (runnable == null || (runnable instanceof SafeRunnable)) ? runnable : new SafeRunnable(runnable, wXSDKInstance, str);
    }

    public static Handler.Callback secure(Handler.Callback callback) {
        return (callback == null || (callback instanceof SafeCallback)) ? callback : new SafeCallback(callback);
    }

    public WXThread(String str) {
        super(str);
        start();
        this.mHandler = new Handler(getLooper());
    }

    public WXThread(String str, Handler.Callback callback) {
        super(str);
        start();
        this.mHandler = new Handler(getLooper(), secure(callback));
    }

    public WXThread(String str, int i, Handler.Callback callback) {
        super(str, i);
        start();
        this.mHandler = new Handler(getLooper(), secure(callback));
    }

    public WXThread(String str, int i) {
        super(str, i);
        start();
        this.mHandler = new Handler(getLooper());
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean isWXThreadAlive() {
        return (this.mHandler == null || getLooper() == null || !isAlive()) ? false : true;
    }

    public boolean quit() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages((Object) null);
        }
        return super.quit();
    }
}
