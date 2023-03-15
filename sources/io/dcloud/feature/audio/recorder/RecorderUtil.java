package io.dcloud.feature.audio.recorder;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.dcloud.android.widget.toast.ToastCompat;
import io.dcloud.common.adapter.util.PlatformUtil;

public class RecorderUtil {
    /* access modifiers changed from: private */
    public static Context context;
    private static boolean isDebug;
    private static Handler mainHandler;

    public static Context getContext() {
        return context;
    }

    static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    public static void init(Context context2, boolean z) {
        context = context2;
        isDebug = z;
        mainHandler = getMainHandler();
    }

    public static boolean isContainMp3() {
        return PlatformUtil.checkClass("io.dcloud.feature.audio.mp3.mp3Impl");
    }

    static boolean isDebug() {
        return isDebug;
    }

    static void postTaskSafely(Runnable runnable) {
        getMainHandler().post(runnable);
    }

    static void showDebugToast(final String str) {
        if (isDebug) {
            getMainHandler().post(new Runnable() {
                public void run() {
                    ToastCompat.makeText(RecorderUtil.context, (CharSequence) str, 0).show();
                }
            });
        }
    }
}
