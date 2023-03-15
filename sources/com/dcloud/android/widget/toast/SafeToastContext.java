package com.dcloud.android.widget.toast;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

final class SafeToastContext extends ContextWrapper {

    private final class ApplicationContextWrapper extends ContextWrapper {
        public Object getSystemService(String str) {
            if ("window".equals(str)) {
                return new WindowManagerWrapper((WindowManager) getBaseContext().getSystemService(str));
            }
            return super.getSystemService(str);
        }

        private ApplicationContextWrapper(Context context) {
            super(context);
        }
    }

    private final class WindowManagerWrapper implements WindowManager {
        private static final String TAG = "WindowManagerWrapper";
        private final WindowManager base;

        public void addView(View view, ViewGroup.LayoutParams layoutParams) {
            try {
                this.base.addView(view, layoutParams);
            } catch (WindowManager.BadTokenException e) {
                Log.i(TAG, e.getMessage());
            } catch (Throwable th) {
                Log.e(TAG, "[addView]", th);
            }
        }

        public Display getDefaultDisplay() {
            return this.base.getDefaultDisplay();
        }

        public void removeView(View view) {
            this.base.removeView(view);
        }

        public void removeViewImmediate(View view) {
            this.base.removeViewImmediate(view);
        }

        public void updateViewLayout(View view, ViewGroup.LayoutParams layoutParams) {
            this.base.updateViewLayout(view, layoutParams);
        }

        private WindowManagerWrapper(WindowManager windowManager) {
            this.base = windowManager;
        }
    }

    SafeToastContext(Context context) {
        super(context);
    }

    public Context getApplicationContext() {
        return new ApplicationContextWrapper(getBaseContext().getApplicationContext());
    }
}
