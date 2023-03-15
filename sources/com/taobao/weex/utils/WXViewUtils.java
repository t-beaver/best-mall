package com.taobao.weex.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.flat.widget.Widget;
import com.taobao.weex.ui.flat.widget.WidgetGroup;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.border.BorderDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WXViewUtils {
    public static final int DIMENSION_UNSET = -1;
    @Deprecated
    public static final int OPAQUE = -1;
    @Deprecated
    public static final int TRANSLUCENT = -3;
    @Deprecated
    public static final int TRANSPARENT = -2;
    private static int mScreenHeight = 0;
    private static int mScreenWidth = 0;
    private static final boolean mUseWebPx = false;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    @Retention(RetentionPolicy.SOURCE)
    public @interface Opacity {
    }

    public static int getOpacityFromColor(int i) {
        int i2 = i >>> 24;
        if (i2 == 255) {
            return -1;
        }
        return i2 == 0 ? -2 : -3;
    }

    public static int multiplyColorAlpha(int i, int i2) {
        if (i2 == 255) {
            return i;
        }
        if (i2 == 0) {
            return i & 16777215;
        }
        return (i & 16777215) | ((((i >>> 24) * (i2 + (i2 >> 7))) >> 8) << 24);
    }

    public static int generateViewId() {
        AtomicInteger atomicInteger;
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 17) {
            return View.generateViewId();
        }
        do {
            atomicInteger = sNextGeneratedId;
            i = atomicInteger.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!atomicInteger.compareAndSet(i, i2));
        return i;
    }

    public static int getWeexHeight(String str) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return -3;
        }
        int weexHeight = sDKInstance.getWeexHeight();
        return (weexHeight >= 0 || weexHeight == -2) ? weexHeight : getScreenHeight((Context) WXEnvironment.sApplication);
    }

    public static int getWeexWidth(String str) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return -3;
        }
        int weexWidth = sDKInstance.getWeexWidth();
        return (weexWidth >= 0 || weexWidth == -2) ? weexWidth : getScreenWidth(WXEnvironment.sApplication);
    }

    @Deprecated
    public static int getScreenWidth() {
        return getScreenWidth(WXEnvironment.sApplication);
    }

    @Deprecated
    public static int setScreenWidth(int i) {
        mScreenWidth = i;
        return i;
    }

    public static float getScreenDensity(Context context) {
        if (context == null) {
            return 3.0f;
        }
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            WXLogUtils.e("getScreenDensityDpi exception:" + e.getMessage());
            return 3.0f;
        }
    }

    public static void updateApplicationScreen(Context context) {
        if (context != null && WXEnvironment.sApplication != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            DisplayMetrics displayMetrics2 = WXEnvironment.sApplication.getResources().getDisplayMetrics();
            displayMetrics2.heightPixels = displayMetrics.heightPixels;
            displayMetrics2.widthPixels = displayMetrics.widthPixels;
            displayMetrics2.density = displayMetrics.density;
            displayMetrics2.densityDpi = displayMetrics.densityDpi;
            displayMetrics2.scaledDensity = displayMetrics.scaledDensity;
            displayMetrics2.xdpi = displayMetrics.xdpi;
        }
    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            mScreenWidth = resources.getDisplayMetrics().widthPixels;
            if (WXEnvironment.SETTING_FORCE_VERTICAL_SCREEN) {
                int i = resources.getDisplayMetrics().heightPixels;
                mScreenHeight = i;
                int i2 = mScreenWidth;
                if (i > i2) {
                    i = i2;
                }
                mScreenWidth = i;
            }
        } else if (WXEnvironment.isApkDebugable()) {
            throw new WXRuntimeException("Error Context is null When getScreenHeight");
        }
        return mScreenWidth;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", WXEnvironment.OS);
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return -1;
    }

    @Deprecated
    public static int getScreenHeight() {
        return getScreenHeight((Context) WXEnvironment.sApplication);
    }

    public static int getScreenHeight(String str) {
        return WXSDKManager.getInstance().getSDKInstance(str).isFullScreenHeightEnabled() ? getFullScreenHeight(WXEnvironment.sApplication) : getScreenHeight((Context) WXEnvironment.sApplication);
    }

    public static int getFullScreenHeight(Context context) {
        WindowManager windowManager;
        if (context != null) {
            Resources resources = context.getResources();
            if (Build.VERSION.SDK_INT < 17 || (windowManager = (WindowManager) context.getSystemService("window")) == null || windowManager.getDefaultDisplay() == null) {
                mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
            } else {
                Point point = new Point();
                windowManager.getDefaultDisplay().getRealSize(point);
                mScreenHeight = point.y;
            }
            if (WXEnvironment.SETTING_FORCE_VERTICAL_SCREEN) {
                int i = resources.getDisplayMetrics().widthPixels;
                mScreenWidth = i;
                int i2 = mScreenHeight;
                if (i2 > i) {
                    i = i2;
                }
                mScreenHeight = i;
            }
        } else if (WXEnvironment.isApkDebugable()) {
            throw new WXRuntimeException("Error Context is null When getScreenHeight");
        }
        return mScreenHeight;
    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            mScreenHeight = resources.getDisplayMetrics().heightPixels;
            if (WXEnvironment.SETTING_FORCE_VERTICAL_SCREEN) {
                int i = resources.getDisplayMetrics().widthPixels;
                mScreenWidth = i;
                int i2 = mScreenHeight;
                if (i2 > i) {
                    i = i2;
                }
                mScreenHeight = i;
            }
        } else if (WXEnvironment.isApkDebugable()) {
            throw new WXRuntimeException("Error Context is null When getScreenHeight");
        }
        return mScreenHeight;
    }

    @Deprecated
    public static float getRealPxByWidth(float f) {
        return realPxByWidth(f, 750.0f);
    }

    public static float getRealPxByWidth(float f, float f2) {
        return realPxByWidth(f, f2);
    }

    public static float getRealPxByWidth(float f, int i) {
        return realPxByWidth(f, (float) i);
    }

    private static float realPxByWidth(float f, float f2) {
        if (Float.isNaN(f)) {
            return f;
        }
        float screenWidth = (f * ((float) getScreenWidth())) / f2;
        double d = (double) screenWidth;
        if (d <= 0.005d || screenWidth >= 1.0f) {
            return (float) Math.rint(d);
        }
        return 1.0f;
    }

    @Deprecated
    public static float getRealSubPxByWidth(float f) {
        return realSubPxByWidth(f, 750.0f);
    }

    public static float getRealSubPxByWidth(float f, float f2) {
        return realSubPxByWidth(f, f2);
    }

    public static float getRealSubPxByWidth(float f, int i) {
        return realSubPxByWidth(f, (float) i);
    }

    private static float realSubPxByWidth(float f, float f2) {
        if (Float.isNaN(f)) {
            return f;
        }
        float screenWidth = (f * ((float) getScreenWidth())) / f2;
        if (((double) screenWidth) <= 0.005d || screenWidth >= 1.0f) {
            return screenWidth;
        }
        return 1.0f;
    }

    @Deprecated
    public static float getWeexPxByReal(float f) {
        return weexPxByReal(f, 750.0f);
    }

    public static float getWeexPxByReal(float f, float f2) {
        return weexPxByReal(f, f2);
    }

    public static float getWeexPxByReal(float f, int i) {
        return weexPxByReal(f, (float) i);
    }

    private static float weexPxByReal(float f, float f2) {
        if (Float.isNaN(f)) {
            return f;
        }
        return (f * f2) / ((float) getScreenWidth());
    }

    @Deprecated
    public static float getRealPxByWidth2(float f) {
        return (float) realPxByWidth2(f, 750.0f);
    }

    public static int getRealPxByWidth2(float f, float f2) {
        return realPxByWidth2(f, f2);
    }

    public static int getRealPxByWidth2(float f, int i) {
        return realPxByWidth2(f, (float) i);
    }

    private static int realPxByWidth2(float f, float f2) {
        float screenWidth = (f * ((float) getScreenWidth())) / f2;
        if (((double) screenWidth) <= 0.005d || screenWidth >= 1.0f) {
            return ((int) screenWidth) - 1;
        }
        return 1;
    }

    @Deprecated
    public static float getWebPxByWidth(float f) {
        return webPxByWidth(f, 750.0f);
    }

    public static float getWebPxByWidth(float f, float f2) {
        return webPxByWidth(f, f2);
    }

    public static float getWebPxByWidth(float f, int i) {
        return webPxByWidth(f, (float) i);
    }

    private static float webPxByWidth(float f, float f2) {
        double d = (double) f;
        if (d < -1.9999d && d > -2.005d) {
            return Float.NaN;
        }
        float screenWidth = (f * f2) / ((float) getScreenWidth());
        if (((double) screenWidth) <= 0.005d || screenWidth >= 1.0f) {
            return screenWidth;
        }
        return 1.0f;
    }

    public static int dip2px(float f) {
        float f2;
        try {
            f2 = WXEnvironment.getApplication().getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            WXLogUtils.e("[WXViewUtils] dip2px:", (Throwable) e);
            f2 = 2.0f;
        }
        float f3 = (f * f2) + 0.5f;
        if (f3 <= 0.0f || f3 >= 1.0f) {
            return (int) f3;
        }
        return 1;
    }

    public static boolean onScreenArea(View view) {
        int i;
        if (view == null || view.getVisibility() != 0) {
            return false;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            i = layoutParams.height;
        } else {
            i = view.getHeight();
        }
        if ((iArr[1] <= 0 || iArr[1] - getScreenHeight((Context) WXEnvironment.sApplication) >= 0) && (i + iArr[1] <= 0 || iArr[1] > 0)) {
            return false;
        }
        return true;
    }

    public static void setBackGround(View view, Drawable drawable, WXComponent wXComponent) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
            return;
        }
        try {
            view.setBackground(drawable);
        } catch (Exception unused) {
            if (wXComponent != null) {
                WXExceptionUtils.commitCriticalExceptionRT(wXComponent.getInstanceId(), WXErrorCode.WX_RENDER_ERR_TEXTURE_SETBACKGROUND, wXComponent.getComponentType() + " setBackGround for android view", WXErrorCode.WX_RENDER_ERR_TEXTURE_SETBACKGROUND.getErrorMsg() + ": TextureView doesn't support displaying a background drawable!", (Map<String, String>) null);
            }
        }
    }

    public static BorderDrawable getBorderDrawable(View view) {
        Drawable background = view.getBackground();
        if (background instanceof BorderDrawable) {
            return (BorderDrawable) background;
        }
        if (!(background instanceof LayerDrawable)) {
            return null;
        }
        LayerDrawable layerDrawable = (LayerDrawable) background;
        if (layerDrawable.getNumberOfLayers() <= 1) {
            return null;
        }
        for (int i = 0; i < layerDrawable.getNumberOfLayers(); i++) {
            Drawable drawable = layerDrawable.getDrawable(i);
            if (drawable instanceof BorderDrawable) {
                return (BorderDrawable) drawable;
            }
        }
        return null;
    }

    public static void clipCanvasWithinBorderBox(View view, Canvas canvas) {
        if (clipCanvasDueToAndroidVersion(canvas) && clipCanvasIfAnimationExist(view)) {
            BorderDrawable borderDrawable = getBorderDrawable(view);
            if (borderDrawable instanceof BorderDrawable) {
                BorderDrawable borderDrawable2 = borderDrawable;
                if (borderDrawable2.isRounded() && clipCanvasIfBackgroundImageExist(view, borderDrawable2)) {
                    RectF rectF = new RectF(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
                    Path contentPath = borderDrawable2.getContentPath(rectF);
                    if (view instanceof WXFrameLayout) {
                        WXDiv component = ((WXFrameLayout) view).getComponent();
                        if (component != null) {
                            Object obj = component.getAttrs().get(Constants.Name.ROTATE_FIX);
                            if (obj != null && obj.toString().equals(AbsoluteConst.TRUE)) {
                                canvas.clipRect(rectF);
                            } else if ((component.getStyles() != null && "hidden".equals(component.getStyles().getOverflow())) || Build.VERSION.SDK_INT < 24) {
                                canvas.clipPath(contentPath);
                            }
                        }
                    } else {
                        canvas.clipPath(contentPath);
                    }
                }
            }
        }
    }

    public static void clipCanvasWithinBorderBox(Widget widget, Canvas canvas) {
        BorderDrawable backgroundAndBorder;
        if (clipCanvasDueToAndroidVersion(canvas) && clipCanvasIfAnimationExist((View) null) && (backgroundAndBorder = widget.getBackgroundAndBorder()) != null) {
            if (!backgroundAndBorder.isRounded() || !clipCanvasIfBackgroundImageExist(widget, backgroundAndBorder)) {
                canvas.clipRect(widget.getBorderBox());
            } else {
                canvas.clipPath(backgroundAndBorder.getContentPath(new RectF(0.0f, 0.0f, (float) widget.getBorderBox().width(), (float) widget.getBorderBox().height())));
            }
        }
    }

    private static boolean clipCanvasDueToAndroidVersion(Canvas canvas) {
        return Build.VERSION.SDK_INT >= 18 || !canvas.isHardwareAccelerated();
    }

    private static boolean clipCanvasIfAnimationExist(View view) {
        return Build.VERSION.SDK_INT < 24;
    }

    private static boolean clipCanvasIfBackgroundImageExist(View view, BorderDrawable borderDrawable) {
        if (!(view instanceof ViewGroup)) {
            return true;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            BorderDrawable borderDrawable2 = getBorderDrawable(viewGroup.getChildAt(i));
            if (borderDrawable2 != null && borderDrawable2.hasImage() && Build.VERSION.SDK_INT < 21) {
                return false;
            }
        }
        return true;
    }

    private static boolean clipCanvasIfBackgroundImageExist(Widget widget, BorderDrawable borderDrawable) {
        if (!(widget instanceof WidgetGroup)) {
            return true;
        }
        for (Widget backgroundAndBorder : ((WidgetGroup) widget).getChildren()) {
            if (backgroundAndBorder.getBackgroundAndBorder().hasImage() && Build.VERSION.SDK_INT < 21) {
                return false;
            }
        }
        return true;
    }
}
