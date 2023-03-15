package io.dcloud.sdk.poly.base.utils;

import android.os.Handler;
import android.os.HandlerThread;

public class d {
    private static volatile HandlerThread a = new HandlerThread("dcloud_thread", -19);
    private static volatile Handler b = new Handler(a.getLooper());

    static {
        a.start();
    }

    private d() {
    }

    public static Handler a() {
        if (a == null || !a.isAlive()) {
            synchronized (d.class) {
                if (a == null || !a.isAlive()) {
                    a = new HandlerThread("dcloud_thread", -19);
                    a.start();
                    b = new Handler(a.getLooper());
                }
            }
        }
        return b;
    }
}
