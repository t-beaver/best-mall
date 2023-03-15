package io.dcloud.common.util;

import android.content.Context;
import com.sample.breakpad.BreakpadInit;
import java.io.File;

public class NativeCrashManager {
    public static void initNativeCrash(Context context) {
        File file = new File(context.getExternalCacheDir(), "dcCrashDump");
        if (!file.exists()) {
            file.mkdirs();
        }
        BreakpadInit.initBreakpad(file.getAbsolutePath());
    }
}
