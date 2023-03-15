package com.alibaba.android.bindingx.core.internal;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Choreographer;

abstract class AnimationFrame {

    interface Callback {
        void doFrame();
    }

    /* access modifiers changed from: package-private */
    public abstract void clear();

    /* access modifiers changed from: package-private */
    public abstract void requestAnimationFrame(Callback callback);

    /* access modifiers changed from: package-private */
    public abstract void terminate();

    AnimationFrame() {
    }

    static AnimationFrame newInstance() {
        if (Build.VERSION.SDK_INT >= 16) {
            return new ChoreographerAnimationFrameImpl();
        }
        return new HandlerAnimationFrameImpl();
    }

    private static class ChoreographerAnimationFrameImpl extends AnimationFrame implements Choreographer.FrameCallback {
        private Callback callback;
        private Choreographer choreographer;
        private boolean isRunning;

        ChoreographerAnimationFrameImpl() {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            this.choreographer = Choreographer.getInstance();
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            Choreographer choreographer2 = this.choreographer;
            if (choreographer2 != null) {
                choreographer2.removeFrameCallback(this);
            }
            this.isRunning = false;
        }

        /* access modifiers changed from: package-private */
        public void terminate() {
            clear();
            this.choreographer = null;
        }

        /* access modifiers changed from: package-private */
        public void requestAnimationFrame(Callback callback2) {
            this.callback = callback2;
            this.isRunning = true;
            Choreographer choreographer2 = this.choreographer;
            if (choreographer2 != null) {
                choreographer2.postFrameCallback(this);
            }
        }

        public void doFrame(long j) {
            Callback callback2 = this.callback;
            if (callback2 != null) {
                callback2.doFrame();
            }
            Choreographer choreographer2 = this.choreographer;
            if (choreographer2 != null && this.isRunning) {
                choreographer2.postFrameCallback(this);
            }
        }
    }

    private static class HandlerAnimationFrameImpl extends AnimationFrame implements Handler.Callback {
        private static final long DEFAULT_DELAY_MILLIS = 16;
        private static final int MSG_FRAME_CALLBACK = 100;
        private Callback callback;
        private boolean isRunning;
        private Handler mInnerHandler;
        private HandlerThread mInnerHandlerThread;

        HandlerAnimationFrameImpl() {
            if (this.mInnerHandlerThread != null) {
                terminate();
            }
            HandlerThread handlerThread = new HandlerThread("expression-timing-thread");
            this.mInnerHandlerThread = handlerThread;
            handlerThread.start();
            this.mInnerHandler = new Handler(this.mInnerHandlerThread.getLooper(), this);
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            Handler handler = this.mInnerHandler;
            if (handler != null) {
                handler.removeCallbacksAndMessages((Object) null);
            }
            this.isRunning = false;
        }

        /* access modifiers changed from: package-private */
        public void terminate() {
            clear();
            if (Build.VERSION.SDK_INT >= 18) {
                this.mInnerHandlerThread.quitSafely();
            } else {
                this.mInnerHandlerThread.quit();
            }
            this.mInnerHandler = null;
            this.mInnerHandlerThread = null;
        }

        /* access modifiers changed from: package-private */
        public void requestAnimationFrame(Callback callback2) {
            this.callback = callback2;
            this.isRunning = true;
            Handler handler = this.mInnerHandler;
            if (handler != null) {
                handler.sendEmptyMessage(100);
            }
        }

        public boolean handleMessage(Message message) {
            if (message == null || message.what != 100 || this.mInnerHandler == null) {
                return false;
            }
            Callback callback2 = this.callback;
            if (callback2 != null) {
                callback2.doFrame();
            }
            if (!this.isRunning) {
                return true;
            }
            this.mInnerHandler.sendEmptyMessageDelayed(100, 16);
            return true;
        }
    }
}
