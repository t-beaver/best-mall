package io.dcloud.h.a.e;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class f {
    private static f a;
    ThreadPoolExecutor b = new ThreadPoolExecutor(3, 6, 1, TimeUnit.SECONDS, new LinkedBlockingQueue());

    private f() {
    }

    public static f a() {
        if (a == null) {
            synchronized (f.class) {
                if (a == null) {
                    a = new f();
                }
            }
        }
        return a;
    }

    public void a(Runnable runnable) {
        this.b.execute(runnable);
    }
}
