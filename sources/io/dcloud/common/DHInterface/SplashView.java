package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import io.dcloud.PdrR;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.internal.splash.ISplash;

public class SplashView extends RelativeLayout {
    public static int STYLE_BLACK = 1;
    public static int STYLE_DEFAULT = 0;
    public static int STYLE_WHITE = 2;
    final String TAG = "SplashView";
    private boolean hasAdSplash = false;
    protected boolean mShowSplashScreen = false;
    protected boolean mShowSplashWaiting = false;
    int screenHeight;
    int screenWidth;
    boolean showBitmap = false;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: android.graphics.drawable.BitmapDrawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: android.graphics.drawable.NinePatchDrawable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: android.graphics.drawable.NinePatchDrawable} */
    /* JADX WARNING: type inference failed for: r0v7, types: [android.graphics.drawable.Drawable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SplashView(android.content.Context r8, android.graphics.Bitmap r9) {
        /*
            r7 = this;
            r7.<init>(r8)
            r0 = 0
            r7.mShowSplashScreen = r0
            r7.mShowSplashWaiting = r0
            java.lang.String r1 = "SplashView"
            r7.TAG = r1
            r7.showBitmap = r0
            r7.hasAdSplash = r0
            r0 = -1
            r7.setBackgroundColor(r0)
            android.content.res.Resources r0 = r8.getResources()
            android.util.DisplayMetrics r0 = r0.getDisplayMetrics()
            int r1 = r0.widthPixels
            r7.screenWidth = r1
            int r0 = r0.heightPixels
            r7.screenHeight = r0
            byte[] r0 = r9.getNinePatchChunk()
            boolean r0 = android.graphics.NinePatch.isNinePatchChunk(r0)
            if (r0 == 0) goto L_0x0044
            android.graphics.drawable.NinePatchDrawable r0 = new android.graphics.drawable.NinePatchDrawable
            android.content.res.Resources r2 = r8.getResources()
            byte[] r4 = r9.getNinePatchChunk()
            android.graphics.Rect r5 = new android.graphics.Rect
            r5.<init>()
            r6 = 0
            r1 = r0
            r3 = r9
            r1.<init>(r2, r3, r4, r5, r6)
            goto L_0x004d
        L_0x0044:
            android.graphics.drawable.BitmapDrawable r0 = new android.graphics.drawable.BitmapDrawable
            android.content.res.Resources r8 = r8.getResources()
            r0.<init>(r8, r9)
        L_0x004d:
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 16
            if (r8 >= r9) goto L_0x0057
            r7.setBackgroundDrawable(r0)
            goto L_0x005a
        L_0x0057:
            r7.setBackground(r0)
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.DHInterface.SplashView.<init>(android.content.Context, android.graphics.Bitmap):void");
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        if (view instanceof ISplash) {
            this.hasAdSplash = true;
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (DeviceInfo.sStatusBarHeight <= 0) {
            Logger.d("SplashView", "paint() before DeviceInfo.updateScreenInfo()");
            DeviceInfo.updateStatusBarHeight((Activity) getContext());
        }
        Logger.d("SplashView", "dispatchDraw.....");
        super.dispatchDraw(canvas);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.hasAdSplash) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onShowProgressBar(ViewGroup viewGroup, int i) {
        if (!this.hasAdSplash) {
            Logger.d(Logger.MAIN_TAG, "showWaiting style=" + i);
            ProgressBar progressBar = new ProgressBar(getContext());
            int parseInt = PdrUtil.parseInt("7%", this.screenWidth, -1);
            setGravity(17);
            Drawable drawable = null;
            if (i == STYLE_BLACK) {
                drawable = getContext().getResources().getDrawable(PdrR.DRAWBLE_PROGRESSBAR_BLACK_SNOW);
            } else if (i == STYLE_WHITE) {
                drawable = getContext().getResources().getDrawable(PdrR.DRAWBLE_PROGRESSBAR_WHITE_SNOW);
            }
            if (drawable != null) {
                progressBar.setIndeterminateDrawable(drawable);
            }
            viewGroup.addView(progressBar, new RelativeLayout.LayoutParams(parseInt, parseInt));
            Logger.d(Logger.MAIN_TAG, "onShowProgressBar");
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public void showWaiting() {
        showWaiting(STYLE_DEFAULT);
    }

    public void showWaiting(final int i) {
        post(new Runnable() {
            public void run() {
                SplashView splashView = SplashView.this;
                splashView.onShowProgressBar(splashView, i);
            }
        });
    }
}
