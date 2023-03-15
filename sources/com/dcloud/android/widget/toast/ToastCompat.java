package com.dcloud.android.widget.toast;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.Toast;
import java.lang.reflect.Field;

public final class ToastCompat extends Toast {
    private final Toast toast;

    public ToastCompat(Context context) {
        super(context);
        this.toast = this;
    }

    public static ToastCompat makeText(Context context, CharSequence charSequence, int i) {
        Toast makeText = Toast.makeText(context, charSequence, i);
        setContextCompat(makeText.getView(), new SafeToastContext(context));
        return new ToastCompat(context, makeText);
    }

    private static void setContextCompat(View view, Context context) {
        if (Build.VERSION.SDK_INT == 25) {
            try {
                Field declaredField = View.class.getDeclaredField("mContext");
                declaredField.setAccessible(true);
                declaredField.set(view, context);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public Toast getBaseToast() {
        return this.toast;
    }

    public int getDuration() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getDuration();
        }
        return toast2.getDuration();
    }

    public int getGravity() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getGravity();
        }
        return toast2.getGravity();
    }

    public float getHorizontalMargin() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getHorizontalMargin();
        }
        return toast2.getHorizontalMargin();
    }

    public float getVerticalMargin() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getVerticalMargin();
        }
        return toast2.getVerticalMargin();
    }

    public View getView() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getView();
        }
        return toast2.getView();
    }

    public int getXOffset() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getXOffset();
        }
        return toast2.getXOffset();
    }

    public int getYOffset() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            return super.getYOffset();
        }
        return toast2.getYOffset();
    }

    public void setDuration(int i) {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.setDuration(i);
        } else {
            toast2.setDuration(i);
        }
    }

    public void setGravity(int i, int i2, int i3) {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.setGravity(i, i2, i3);
        } else {
            toast2.setGravity(i, i2, i3);
        }
    }

    public void setMargin(float f, float f2) {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.setMargin(f, f2);
        } else {
            toast2.setMargin(f, f2);
        }
    }

    public void setText(int i) {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.setText(i);
        } else {
            toast2.setText(i);
        }
    }

    public void setView(View view) {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.setView(view);
        } else {
            toast2.setView(view);
        }
        setContextCompat(view, new SafeToastContext(view.getContext()));
    }

    public void show() {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.show();
        } else {
            toast2.show();
        }
    }

    private ToastCompat(Context context, Toast toast2) {
        super(context);
        this.toast = toast2;
    }

    public static ToastCompat makeText(Context context, int i, int i2) throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(i), i2);
    }

    public void setText(CharSequence charSequence) {
        Toast toast2 = this.toast;
        if (toast2 instanceof ToastCompat) {
            super.setText(charSequence);
        } else {
            toast2.setText(charSequence);
        }
    }
}
