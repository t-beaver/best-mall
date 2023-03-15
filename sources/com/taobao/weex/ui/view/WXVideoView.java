package com.taobao.weex.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXResourceUtils;

public class WXVideoView extends VideoView implements WXGestureObservable {
    private VideoPlayListener mVideoPauseListener;
    private WXGesture wxGesture;

    public interface VideoPlayListener {
        void onPause();

        void onStart();
    }

    public WXVideoView(Context context) {
        super(context);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public void setOnVideoPauseListener(VideoPlayListener videoPlayListener) {
        this.mVideoPauseListener = videoPlayListener;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        WXGesture wXGesture = this.wxGesture;
        return wXGesture != null ? onTouchEvent | wXGesture.onTouch(this, motionEvent) : onTouchEvent;
    }

    public void start() {
        super.start();
        VideoPlayListener videoPlayListener = this.mVideoPauseListener;
        if (videoPlayListener != null) {
            videoPlayListener.onStart();
        }
    }

    public void pause() {
        super.pause();
        VideoPlayListener videoPlayListener = this.mVideoPauseListener;
        if (videoPlayListener != null) {
            videoPlayListener.onPause();
        }
    }

    public static class Wrapper extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {
        private boolean mControls = true;
        private MediaController mMediaController;
        private MediaPlayer.OnCompletionListener mOnCompletionListener;
        private MediaPlayer.OnErrorListener mOnErrorListener;
        private MediaPlayer.OnPreparedListener mOnPreparedListener;
        private ProgressBar mProgressBar;
        private Uri mUri;
        private VideoPlayListener mVideoPlayListener;
        private WXVideoView mVideoView;

        public Wrapper(Context context) {
            super(context);
            init(context);
        }

        public Wrapper(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init(context);
        }

        public Wrapper(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            init(context);
        }

        private void init(Context context) {
            setBackgroundColor(WXResourceUtils.getColor("#ee000000"));
            this.mProgressBar = new ProgressBar(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            this.mProgressBar.setLayoutParams(layoutParams);
            layoutParams.gravity = 17;
            addView(this.mProgressBar);
            getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        public ProgressBar getProgressBar() {
            return this.mProgressBar;
        }

        public WXVideoView getVideoView() {
            return this.mVideoView;
        }

        public WXVideoView createIfNotExist() {
            if (this.mVideoView == null) {
                createVideoView();
            }
            return this.mVideoView;
        }

        public MediaController getMediaController() {
            return this.mMediaController;
        }

        public void setVideoURI(Uri uri) {
            this.mUri = uri;
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.setVideoURI(uri);
            }
        }

        public void start() {
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.start();
            }
        }

        public void pause() {
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.pause();
            }
        }

        public void stopPlayback() {
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.stopPlayback();
            }
        }

        public void resume() {
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.resume();
            }
        }

        public void setOnErrorListener(MediaPlayer.OnErrorListener onErrorListener) {
            this.mOnErrorListener = onErrorListener;
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.setOnErrorListener(onErrorListener);
            }
        }

        public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
            this.mOnPreparedListener = onPreparedListener;
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.setOnPreparedListener(onPreparedListener);
            }
        }

        public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
            this.mOnCompletionListener = onCompletionListener;
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.setOnCompletionListener(onCompletionListener);
            }
        }

        public void setOnVideoPauseListener(VideoPlayListener videoPlayListener) {
            this.mVideoPlayListener = videoPlayListener;
            WXVideoView wXVideoView = this.mVideoView;
            if (wXVideoView != null) {
                wXVideoView.setOnVideoPauseListener(videoPlayListener);
            }
        }

        public void setControls(boolean z) {
            MediaController mediaController;
            this.mControls = z;
            if (this.mVideoView != null && (mediaController = this.mMediaController) != null) {
                if (!z) {
                    mediaController.setVisibility(8);
                } else {
                    mediaController.setVisibility(0);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0060, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private synchronized void createVideoView() {
            /*
                r4 = this;
                monitor-enter(r4)
                com.taobao.weex.ui.view.WXVideoView r0 = r4.mVideoView     // Catch:{ all -> 0x0061 }
                if (r0 == 0) goto L_0x0007
                monitor-exit(r4)
                return
            L_0x0007:
                android.content.Context r0 = r4.getContext()     // Catch:{ all -> 0x0061 }
                com.taobao.weex.ui.view.WXVideoView r1 = new com.taobao.weex.ui.view.WXVideoView     // Catch:{ all -> 0x0061 }
                r1.<init>(r0)     // Catch:{ all -> 0x0061 }
                android.widget.FrameLayout$LayoutParams r2 = new android.widget.FrameLayout$LayoutParams     // Catch:{ all -> 0x0061 }
                r3 = -1
                r2.<init>(r3, r3)     // Catch:{ all -> 0x0061 }
                r3 = 17
                r2.gravity = r3     // Catch:{ all -> 0x0061 }
                r1.setLayoutParams(r2)     // Catch:{ all -> 0x0061 }
                r2 = 0
                r4.addView(r1, r2)     // Catch:{ all -> 0x0061 }
                android.media.MediaPlayer$OnErrorListener r3 = r4.mOnErrorListener     // Catch:{ all -> 0x0061 }
                r1.setOnErrorListener(r3)     // Catch:{ all -> 0x0061 }
                android.media.MediaPlayer$OnPreparedListener r3 = r4.mOnPreparedListener     // Catch:{ all -> 0x0061 }
                r1.setOnPreparedListener(r3)     // Catch:{ all -> 0x0061 }
                android.media.MediaPlayer$OnCompletionListener r3 = r4.mOnCompletionListener     // Catch:{ all -> 0x0061 }
                r1.setOnCompletionListener(r3)     // Catch:{ all -> 0x0061 }
                com.taobao.weex.ui.view.WXVideoView$VideoPlayListener r3 = r4.mVideoPlayListener     // Catch:{ all -> 0x0061 }
                r1.setOnVideoPauseListener(r3)     // Catch:{ all -> 0x0061 }
                android.widget.MediaController r3 = new android.widget.MediaController     // Catch:{ all -> 0x0061 }
                r3.<init>(r0)     // Catch:{ all -> 0x0061 }
                r3.setAnchorView(r4)     // Catch:{ all -> 0x0061 }
                r1.setMediaController(r3)     // Catch:{ all -> 0x0061 }
                r3.setMediaPlayer(r1)     // Catch:{ all -> 0x0061 }
                boolean r0 = r4.mControls     // Catch:{ all -> 0x0061 }
                if (r0 != 0) goto L_0x004d
                r0 = 8
                r3.setVisibility(r0)     // Catch:{ all -> 0x0061 }
                goto L_0x0050
            L_0x004d:
                r3.setVisibility(r2)     // Catch:{ all -> 0x0061 }
            L_0x0050:
                r4.mMediaController = r3     // Catch:{ all -> 0x0061 }
                r4.mVideoView = r1     // Catch:{ all -> 0x0061 }
                r0 = 1
                r1.setZOrderOnTop(r0)     // Catch:{ all -> 0x0061 }
                android.net.Uri r0 = r4.mUri     // Catch:{ all -> 0x0061 }
                if (r0 == 0) goto L_0x005f
                r4.setVideoURI(r0)     // Catch:{ all -> 0x0061 }
            L_0x005f:
                monitor-exit(r4)
                return
            L_0x0061:
                r0 = move-exception
                monitor-exit(r4)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.WXVideoView.Wrapper.createVideoView():void");
        }

        private void removeSelfFromViewTreeObserver() {
            if (Build.VERSION.SDK_INT >= 16) {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }

        public boolean createVideoViewIfVisible() {
            Rect rect = new Rect();
            if (this.mVideoView != null) {
                return true;
            }
            if (!getGlobalVisibleRect(rect) || rect.isEmpty()) {
                return false;
            }
            createVideoView();
            return true;
        }

        public void onGlobalLayout() {
            if (createVideoViewIfVisible()) {
                removeSelfFromViewTreeObserver();
            }
        }
    }
}
