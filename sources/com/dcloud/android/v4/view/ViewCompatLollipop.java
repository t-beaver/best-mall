package com.dcloud.android.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.WindowInsets;

class ViewCompatLollipop {
    ViewCompatLollipop() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = ((com.dcloud.android.v4.view.WindowInsetsCompatApi21) r2).unwrap();
        r1 = r1.dispatchApplyWindowInsets(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.dcloud.android.v4.view.WindowInsetsCompat dispatchApplyWindowInsets(android.view.View r1, com.dcloud.android.v4.view.WindowInsetsCompat r2) {
        /*
            boolean r0 = r2 instanceof com.dcloud.android.v4.view.WindowInsetsCompatApi21
            if (r0 == 0) goto L_0x0016
            r0 = r2
            com.dcloud.android.v4.view.WindowInsetsCompatApi21 r0 = (com.dcloud.android.v4.view.WindowInsetsCompatApi21) r0
            android.view.WindowInsets r0 = r0.unwrap()
            android.view.WindowInsets r1 = r1.dispatchApplyWindowInsets(r0)
            if (r1 == r0) goto L_0x0016
            com.dcloud.android.v4.view.WindowInsetsCompatApi21 r2 = new com.dcloud.android.v4.view.WindowInsetsCompatApi21
            r2.<init>(r1)
        L_0x0016:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.v4.view.ViewCompatLollipop.dispatchApplyWindowInsets(android.view.View, com.dcloud.android.v4.view.WindowInsetsCompat):com.dcloud.android.v4.view.WindowInsetsCompat");
    }

    public static boolean dispatchNestedFling(View view, float f, float f2, boolean z) {
        return view.dispatchNestedFling(f, f2, z);
    }

    public static boolean dispatchNestedPreFling(View view, float f, float f2) {
        return view.dispatchNestedPreFling(f, f2);
    }

    public static boolean dispatchNestedPreScroll(View view, int i, int i2, int[] iArr, int[] iArr2) {
        return view.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public static boolean dispatchNestedScroll(View view, int i, int i2, int i3, int i4, int[] iArr) {
        return view.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    static ColorStateList getBackgroundTintList(View view) {
        return view.getBackgroundTintList();
    }

    static PorterDuff.Mode getBackgroundTintMode(View view) {
        return view.getBackgroundTintMode();
    }

    public static float getElevation(View view) {
        return view.getElevation();
    }

    public static String getTransitionName(View view) {
        return view.getTransitionName();
    }

    public static float getTranslationZ(View view) {
        return view.getTranslationZ();
    }

    public static float getZ(View view) {
        return view.getZ();
    }

    public static boolean hasNestedScrollingParent(View view) {
        return view.hasNestedScrollingParent();
    }

    public static boolean isImportantForAccessibility(View view) {
        return view.isImportantForAccessibility();
    }

    public static boolean isNestedScrollingEnabled(View view) {
        return view.isNestedScrollingEnabled();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = ((com.dcloud.android.v4.view.WindowInsetsCompatApi21) r2).unwrap();
        r1 = r1.onApplyWindowInsets(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.dcloud.android.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View r1, com.dcloud.android.v4.view.WindowInsetsCompat r2) {
        /*
            boolean r0 = r2 instanceof com.dcloud.android.v4.view.WindowInsetsCompatApi21
            if (r0 == 0) goto L_0x0016
            r0 = r2
            com.dcloud.android.v4.view.WindowInsetsCompatApi21 r0 = (com.dcloud.android.v4.view.WindowInsetsCompatApi21) r0
            android.view.WindowInsets r0 = r0.unwrap()
            android.view.WindowInsets r1 = r1.onApplyWindowInsets(r0)
            if (r1 == r0) goto L_0x0016
            com.dcloud.android.v4.view.WindowInsetsCompatApi21 r2 = new com.dcloud.android.v4.view.WindowInsetsCompatApi21
            r2.<init>(r1)
        L_0x0016:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.v4.view.ViewCompatLollipop.onApplyWindowInsets(android.view.View, com.dcloud.android.v4.view.WindowInsetsCompat):com.dcloud.android.v4.view.WindowInsetsCompat");
    }

    public static void requestApplyInsets(View view) {
        view.requestApplyInsets();
    }

    static void setBackgroundTintList(View view, ColorStateList colorStateList) {
        view.setBackgroundTintList(colorStateList);
    }

    static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        view.setBackgroundTintMode(mode);
    }

    public static void setElevation(View view, float f) {
        view.setElevation(f);
    }

    public static void setNestedScrollingEnabled(View view, boolean z) {
        view.setNestedScrollingEnabled(z);
    }

    public static void setOnApplyWindowInsetsListener(View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        view.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return ((WindowInsetsCompatApi21) OnApplyWindowInsetsListener.this.onApplyWindowInsets(view, new WindowInsetsCompatApi21(windowInsets))).unwrap();
            }
        });
    }

    public static void setTransitionName(View view, String str) {
        view.setTransitionName(str);
    }

    public static void setTranslationZ(View view, float f) {
        view.setTranslationZ(f);
    }

    public static boolean startNestedScroll(View view, int i) {
        return view.startNestedScroll(i);
    }

    public static void stopNestedScroll(View view) {
        view.stopNestedScroll();
    }
}
