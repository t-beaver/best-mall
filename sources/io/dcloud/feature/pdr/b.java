package io.dcloud.feature.pdr;

import android.util.Log;
import io.dcloud.common.adapter.util.Logger;
import java.io.File;
import java.io.IOException;

public class b extends Logger {
    private static String a;
    private static File b;
    private static Boolean c = Boolean.TRUE;

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002f A[SYNTHETIC, Splitter:B:19:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x003a A[SYNTHETIC, Splitter:B:24:0x003a] */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void WriteLogToSDcard(java.lang.String r2, java.lang.String r3, java.lang.String r4) {
        /*
            java.lang.String r2 = io.dcloud.common.adapter.util.Logger.generateLog(r2, r3, r4)
            java.io.File r3 = b
            if (r3 == 0) goto L_0x0043
            if (r2 == 0) goto L_0x0043
            r3 = 0
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0029 }
            java.io.File r0 = b     // Catch:{ Exception -> 0x0029 }
            r1 = 1
            r4.<init>(r0, r1)     // Catch:{ Exception -> 0x0029 }
            byte[] r2 = r2.getBytes()     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.write(r2)     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.flush()     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0043
        L_0x0021:
            r2 = move-exception
            r3 = r4
            goto L_0x0038
        L_0x0024:
            r2 = move-exception
            r3 = r4
            goto L_0x002a
        L_0x0027:
            r2 = move-exception
            goto L_0x0038
        L_0x0029:
            r2 = move-exception
        L_0x002a:
            r2.printStackTrace()     // Catch:{ all -> 0x0027 }
            if (r3 == 0) goto L_0x0043
            r3.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0043
        L_0x0033:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0043
        L_0x0038:
            if (r3 == 0) goto L_0x0042
            r3.close()     // Catch:{ IOException -> 0x003e }
            goto L_0x0042
        L_0x003e:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0042:
            throw r2
        L_0x0043:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.pdr.b.WriteLogToSDcard(java.lang.String, java.lang.String, java.lang.String):void");
    }

    public static void a(String str) {
        if (c.booleanValue()) {
            a = str;
            storeLogToSDcard();
            c = Boolean.FALSE;
        }
    }

    public static void d(String str, String str2) {
        Log.d(str, str2);
        WriteLogToSDcard(Logger.D, str, str2);
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
        WriteLogToSDcard(Logger.E, str, str2);
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
        WriteLogToSDcard(Logger.I, str, str2);
    }

    public static void storeLogToSDcard() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(a);
        stringBuffer.append(File.separatorChar);
        stringBuffer.append(Logger.generateTimeStamp(Boolean.FALSE));
        stringBuffer.append(".log");
        File file = new File(a);
        b = new File(stringBuffer.toString());
        if (!file.exists()) {
            file.mkdirs();
        } else {
            Logger.deleteOldLog(file);
        }
        if (!b.exists()) {
            try {
                b.createNewFile();
            } catch (IOException e) {
                b = null;
                e.printStackTrace();
            }
        }
    }

    public static void a(String str, String str2) {
        Log.i(str, str2);
        WriteLogToSDcard(Logger.W, str, str2);
    }
}
