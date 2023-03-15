package io.dcloud.e;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class a {

    /* renamed from: io.dcloud.e.a$a  reason: collision with other inner class name */
    public static class C0025a {
        public long a;
        public long b;
        public long c;
        public long d;
    }

    public static long a() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static long b() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize());
    }

    public static C0025a a(Context context) {
        C0025a aVar = new C0025a();
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
            aVar.b = memoryInfo.availMem;
            aVar.a = memoryInfo.totalMem;
            aVar.c = a();
            aVar.d = b();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return aVar;
    }
}
