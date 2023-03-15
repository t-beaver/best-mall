package com.taobao.weex;

import android.os.Build;
import android.util.Log;
import android.view.Choreographer;
import com.taobao.weex.common.WXErrorCode;
import java.lang.ref.WeakReference;

public class WeexFrameRateControl {
    private static final long VSYNC_FRAME = 16;
    /* access modifiers changed from: private */
    public final Choreographer mChoreographer;
    /* access modifiers changed from: private */
    public WeakReference<VSyncListener> mListener;
    /* access modifiers changed from: private */
    public final Choreographer.FrameCallback mVSyncFrameCallback;
    /* access modifiers changed from: private */
    public final Runnable runnable;

    public interface VSyncListener {
        void OnVSync();
    }

    public WeexFrameRateControl(VSyncListener vSyncListener) {
        this.mListener = new WeakReference<>(vSyncListener);
        if (Build.VERSION.SDK_INT > 15) {
            this.mChoreographer = Choreographer.getInstance();
            this.mVSyncFrameCallback = new Choreographer.FrameCallback() {
                public void doFrame(long j) {
                    VSyncListener vSyncListener;
                    if (WeexFrameRateControl.this.mListener != null && (vSyncListener = (VSyncListener) WeexFrameRateControl.this.mListener.get()) != null) {
                        try {
                            vSyncListener.OnVSync();
                            WeexFrameRateControl.this.mChoreographer.postFrameCallback(WeexFrameRateControl.this.mVSyncFrameCallback);
                        } catch (UnsatisfiedLinkError e) {
                            if (vSyncListener instanceof WXSDKInstance) {
                                ((WXSDKInstance) vSyncListener).onRenderError(WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode(), Log.getStackTraceString(e));
                            }
                        }
                    }
                }
            };
            this.runnable = null;
            return;
        }
        this.runnable = new Runnable() {
            public void run() {
                VSyncListener vSyncListener;
                if (WeexFrameRateControl.this.mListener != null && (vSyncListener = (VSyncListener) WeexFrameRateControl.this.mListener.get()) != null) {
                    try {
                        vSyncListener.OnVSync();
                        WXSDKManager.getInstance().getWXRenderManager().postOnUiThread(WeexFrameRateControl.this.runnable, 16);
                    } catch (UnsatisfiedLinkError e) {
                        if (vSyncListener instanceof WXSDKInstance) {
                            ((WXSDKInstance) vSyncListener).onRenderError(WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode(), Log.getStackTraceString(e));
                        }
                    }
                }
            }
        };
        this.mChoreographer = null;
        this.mVSyncFrameCallback = null;
    }

    public void start() {
        Choreographer choreographer = this.mChoreographer;
        if (choreographer != null) {
            choreographer.postFrameCallback(this.mVSyncFrameCallback);
        } else if (this.runnable != null) {
            WXSDKManager.getInstance().getWXRenderManager().postOnUiThread(this.runnable, 16);
        }
    }

    public void stop() {
        Choreographer choreographer = this.mChoreographer;
        if (choreographer != null) {
            choreographer.removeFrameCallback(this.mVSyncFrameCallback);
        } else if (this.runnable != null) {
            WXSDKManager.getInstance().getWXRenderManager().removeTask(this.runnable);
        }
    }
}
