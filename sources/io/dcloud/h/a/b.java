package io.dcloud.h.a;

import android.content.Context;
import io.dcloud.h.a.c.a;
import io.dcloud.h.a.d.a;
import java.io.File;

public class b {
    private static boolean a = false;

    public static a a(a.c cVar, Context context, String str, String str2, String str3, String str4) {
        a(context);
        return new io.dcloud.h.a.d.a(cVar, context, str, str2, str3, str4);
    }

    private static void a(Context context) {
        File[] listFiles;
        if (!a) {
            a = true;
            if (context != null) {
                try {
                    long currentTimeMillis = System.currentTimeMillis() - 604800000;
                    File file = new File(context.getCacheDir().getAbsolutePath() + "/dcloud_ad/img/");
                    if (file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                        for (File file2 : listFiles) {
                            if (file2.lastModified() < currentTimeMillis) {
                                file2.delete();
                            }
                        }
                    }
                } catch (Exception unused) {
                }
            }
        }
    }
}
