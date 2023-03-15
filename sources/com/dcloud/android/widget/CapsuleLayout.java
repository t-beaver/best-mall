package com.dcloud.android.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class CapsuleLayout extends LinearLayout {
    public static final int STYLE_DARK = 2;
    public static final int STYLE_LIGHT = 1;
    public boolean isDiy = false;
    public float mAngle;
    public int mBackgroundColor;
    CapsuleDrawable mDrawable;
    private List<View> mIntervals;
    public Paint mPaint;
    private int mSelectColor;
    public int mStrokeColor;
    public int mStrokeWidth;
    private int mStyle = 1;

    /* renamed from: com.dcloud.android.widget.CapsuleLayout$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$dcloud$android$widget$CapsuleLayout$ButtonType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.dcloud.android.widget.CapsuleLayout$ButtonType[] r0 = com.dcloud.android.widget.CapsuleLayout.ButtonType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$dcloud$android$widget$CapsuleLayout$ButtonType = r0
                com.dcloud.android.widget.CapsuleLayout$ButtonType r1 = com.dcloud.android.widget.CapsuleLayout.ButtonType.LIFT     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$dcloud$android$widget$CapsuleLayout$ButtonType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.dcloud.android.widget.CapsuleLayout$ButtonType r1 = com.dcloud.android.widget.CapsuleLayout.ButtonType.RIGHT     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$dcloud$android$widget$CapsuleLayout$ButtonType     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.dcloud.android.widget.CapsuleLayout$ButtonType r1 = com.dcloud.android.widget.CapsuleLayout.ButtonType.MIDDLE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.widget.CapsuleLayout.AnonymousClass1.<clinit>():void");
        }
    }

    public enum ButtonType {
        LIFT,
        MIDDLE,
        RIGHT
    }

    private class CapsuleDrawable extends GradientDrawable {
        private CapsuleDrawable() {
        }

        /* synthetic */ CapsuleDrawable(CapsuleLayout capsuleLayout, AnonymousClass1 r2) {
            this();
        }
    }

    public CapsuleLayout(Context context, float f) {
        super(context);
        this.mAngle = f;
        this.mIntervals = new ArrayList();
        setRoundColor(Color.parseColor("#ffffffff"), Color.parseColor("#ffe5e5e5"), 1);
        this.mSelectColor = Color.parseColor("#CBCCCD");
    }

    private void initButtonBackground(View view, ButtonType buttonType) {
        View view2 = view;
        StateListDrawable stateListDrawable = new StateListDrawable();
        CapsuleDrawable capsuleDrawable = new CapsuleDrawable(this, (AnonymousClass1) null);
        CapsuleDrawable capsuleDrawable2 = new CapsuleDrawable(this, (AnonymousClass1) null);
        float[] fArr = new float[0];
        int i = AnonymousClass1.$SwitchMap$com$dcloud$android$widget$CapsuleLayout$ButtonType[buttonType.ordinal()];
        if (i == 1) {
            float f = this.mAngle;
            fArr = new float[]{f, f, 0.0f, 0.0f, 0.0f, 0.0f, f, f};
            capsuleDrawable2.setStroke(this.mStrokeWidth, 0);
        } else if (i == 2) {
            float f2 = this.mAngle;
            fArr = new float[]{0.0f, 0.0f, f2, f2, f2, f2, 0.0f, 0.0f};
            capsuleDrawable2.setStroke(this.mStrokeWidth, 0);
        } else if (i == 3) {
            fArr = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        }
        capsuleDrawable2.setCornerRadii(fArr);
        capsuleDrawable.setCornerRadii(fArr);
        capsuleDrawable.setColor(0);
        capsuleDrawable2.setColor(this.mSelectColor);
        stateListDrawable.addState(new int[]{16842919, 16842910}, capsuleDrawable2);
        stateListDrawable.addState(new int[]{16842910, 16842908}, capsuleDrawable2);
        stateListDrawable.addState(new int[]{16842910}, capsuleDrawable);
        stateListDrawable.addState(new int[]{16842908}, capsuleDrawable2);
        stateListDrawable.addState(new int[]{16842909}, capsuleDrawable2);
        stateListDrawable.addState(new int[0], capsuleDrawable);
        if (Build.VERSION.SDK_INT < 16) {
            view2.setBackgroundDrawable(stateListDrawable);
        } else {
            view2.setBackground(stateListDrawable);
        }
    }

    private void updateBackground() {
        if (this.mDrawable == null) {
            CapsuleDrawable capsuleDrawable = new CapsuleDrawable(this, (AnonymousClass1) null);
            this.mDrawable = capsuleDrawable;
            if (Build.VERSION.SDK_INT < 16) {
                setBackgroundDrawable(capsuleDrawable);
            } else {
                setBackground(capsuleDrawable);
            }
        }
        this.mDrawable.setCornerRadius(this.mAngle);
        this.mDrawable.setStroke(this.mStrokeWidth, this.mStrokeColor);
        this.mDrawable.setColor(this.mBackgroundColor);
        this.mDrawable.invalidateSelf();
    }

    private void updateIntervalColor() {
        for (View backgroundColor : this.mIntervals) {
            backgroundColor.setBackgroundColor(this.mStrokeColor);
        }
    }

    public void addButtonView(View view, LinearLayout.LayoutParams layoutParams, ButtonType buttonType, View.OnClickListener onClickListener) {
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -1);
        layoutParams2.weight = 1.0f;
        layoutParams2.gravity = 17;
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.addView(view, layoutParams);
        addView(linearLayout, layoutParams2);
        linearLayout.setOnClickListener(onClickListener);
        initButtonBackground(linearLayout, buttonType);
    }

    public void addIntervalView(float f) {
        View view = new View(getContext());
        view.setBackgroundColor(this.mStrokeColor);
        addView(view, new LinearLayout.LayoutParams(this.mStrokeWidth, (int) (f * 18.0f)));
        this.mIntervals.add(view);
    }

    public int checkColorToStyle(int i) {
        if (this.isDiy) {
            return 1;
        }
        int i2 = (i >> 16) & 255;
        int i3 = (i >> 8) & 255;
        int i4 = i & 255;
        if (i2 <= 235 || i3 <= 235 || i4 <= 235) {
            if (this.mStyle == 1) {
                this.mStyle = 2;
                setRoundColor(Color.parseColor("#1a000000"), Color.parseColor("#4de5e5e5"), 1);
                updateIntervalColor();
            }
        } else if (this.mStyle == 2) {
            this.mStyle = 1;
            setRoundColor(Color.parseColor("#ffffffff"), Color.parseColor("#ffe5e5e5"), 1);
            updateIntervalColor();
        }
        return this.mStyle;
    }

    public void removeAllViews() {
        super.removeAllViews();
        this.mIntervals.clear();
    }

    public void setAngle(float f) {
        this.mAngle = f;
        updateBackground();
    }

    public void setBackground(Drawable drawable) {
        if (drawable instanceof CapsuleDrawable) {
            super.setBackground(drawable);
        }
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        updateBackground();
    }

    public void setBackgroundResource(int i) {
    }

    public void setButtonSelectColor(View view, ButtonType buttonType, int i) {
        if (view != null && view.getParent() != null) {
            this.mSelectColor = i;
            initButtonBackground((View) view.getParent(), buttonType);
        }
    }

    public void setRoundColor(int i, int i2, int i3) {
        this.mBackgroundColor = i;
        this.mStrokeColor = i2;
        this.mStrokeWidth = i3;
        updateBackground();
    }

    public void setRoundColor(int i) {
        this.mStrokeColor = i;
        updateBackground();
    }
}
