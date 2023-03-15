package io.dcloud.feature.ui.nativeui;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import io.dcloud.common.DHInterface.ICallBack;
import java.util.ArrayList;

public class b extends Toast implements ICallBack {
    private static ArrayList<b> a = new ArrayList<>();
    View b = null;
    TextView c = null;
    WindowManager.LayoutParams d = null;
    WindowManager e = null;
    String f;

    class a implements Runnable {
        a() {
        }

        public void run() {
            b.this.a();
        }
    }

    public b(Activity activity, String str) {
        super(activity);
        this.f = str;
        this.e = activity.getWindowManager();
        TextView textView = new TextView(activity);
        this.c = textView;
        this.b = textView;
        textView.setPadding(20, 20, 20, 20);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2, 8, -2);
        this.d = layoutParams;
        layoutParams.gravity = 17;
    }

    public Object onCallBack(int i, Object obj) {
        if (Build.VERSION.SDK_INT > 19) {
            return null;
        }
        a();
        return null;
    }

    public void setDuration(int i) {
        if (i == 1) {
            i = 3500;
        } else if (i == 0) {
            i = 2000;
        }
        super.setDuration(i);
    }

    public void setGravity(int i, int i2, int i3) {
        WindowManager.LayoutParams layoutParams = this.d;
        layoutParams.gravity = i;
        layoutParams.x = i2;
        layoutParams.y = i3;
        super.setGravity(i, i2, i3);
    }

    public void setText(CharSequence charSequence) {
        this.c.setText(charSequence);
        super.setText(charSequence);
    }

    public synchronized void show() {
        a.add(this);
        this.e.addView(this.b, this.d);
        this.b.postDelayed(new a(), (long) getDuration());
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:4|5|6|7) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x000a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void a() {
        /*
            r2 = this;
            monitor-enter(r2)
            android.view.View r0 = r2.b     // Catch:{ all -> 0x0014 }
            if (r0 == 0) goto L_0x0012
            android.view.WindowManager r1 = r2.e     // Catch:{ Exception -> 0x000a }
            r1.removeViewImmediate(r0)     // Catch:{ Exception -> 0x000a }
        L_0x000a:
            java.util.ArrayList<io.dcloud.feature.ui.nativeui.b> r0 = a     // Catch:{ all -> 0x0014 }
            r0.remove(r2)     // Catch:{ all -> 0x0014 }
            r0 = 0
            r2.b = r0     // Catch:{ all -> 0x0014 }
        L_0x0012:
            monitor-exit(r2)
            return
        L_0x0014:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.ui.nativeui.b.a():void");
    }

    public void a(View view, TextView textView) {
        this.b = view;
        this.c = textView;
    }

    public static synchronized void a(String str) {
        synchronized (b.class) {
            if (!a.isEmpty()) {
                for (int size = a.size() - 1; size >= 0; size--) {
                    a.get(size).a();
                }
                a.clear();
            }
        }
    }
}
