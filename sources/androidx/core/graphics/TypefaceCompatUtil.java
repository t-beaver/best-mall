package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    public static File getTempFile(Context context) {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        String str = CACHE_FILE_PREFIX + Process.myPid() + Operators.SUB + Process.myTid() + Operators.SUB;
        int i = 0;
        while (i < 100) {
            File file = new File(cacheDir, str + i);
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException unused) {
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001b, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (r7 != null) goto L_0x001e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.nio.ByteBuffer mmap(java.io.File r7) {
        /*
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x002b }
            r0.<init>(r7)     // Catch:{ IOException -> 0x002b }
            java.nio.channels.FileChannel r1 = r0.getChannel()     // Catch:{ all -> 0x0019 }
            long r5 = r1.size()     // Catch:{ all -> 0x0019 }
            java.nio.channels.FileChannel$MapMode r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ all -> 0x0019 }
            r3 = 0
            java.nio.MappedByteBuffer r7 = r1.map(r2, r3, r5)     // Catch:{ all -> 0x0019 }
            r0.close()     // Catch:{ IOException -> 0x002b }
            return r7
        L_0x0019:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x001b }
        L_0x001b:
            r1 = move-exception
            if (r7 == 0) goto L_0x0027
            r0.close()     // Catch:{ all -> 0x0022 }
            goto L_0x002a
        L_0x0022:
            r0 = move-exception
            r7.addSuppressed(r0)     // Catch:{ IOException -> 0x002b }
            goto L_0x002a
        L_0x0027:
            r0.close()     // Catch:{ IOException -> 0x002b }
        L_0x002a:
            throw r1     // Catch:{ IOException -> 0x002b }
        L_0x002b:
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatUtil.mmap(java.io.File):java.nio.ByteBuffer");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0037, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0038, code lost:
        if (r9 != null) goto L_0x003a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r9.addSuppressed(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0043, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0046, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0049, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004a, code lost:
        if (r7 != null) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x004c, code lost:
        if (r8 != null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0057, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x005a, code lost:
        throw r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.nio.ByteBuffer mmap(android.content.Context r7, android.os.CancellationSignal r8, android.net.Uri r9) {
        /*
            android.content.ContentResolver r7 = r7.getContentResolver()
            r0 = 0
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r7 = r7.openFileDescriptor(r9, r1, r8)     // Catch:{ IOException -> 0x005b }
            if (r7 != 0) goto L_0x0013
            if (r7 == 0) goto L_0x0012
            r7.close()     // Catch:{ IOException -> 0x005b }
        L_0x0012:
            return r0
        L_0x0013:
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ all -> 0x0047 }
            java.io.FileDescriptor r9 = r7.getFileDescriptor()     // Catch:{ all -> 0x0047 }
            r8.<init>(r9)     // Catch:{ all -> 0x0047 }
            java.nio.channels.FileChannel r1 = r8.getChannel()     // Catch:{ all -> 0x0035 }
            long r5 = r1.size()     // Catch:{ all -> 0x0035 }
            java.nio.channels.FileChannel$MapMode r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ all -> 0x0035 }
            r3 = 0
            java.nio.MappedByteBuffer r9 = r1.map(r2, r3, r5)     // Catch:{ all -> 0x0035 }
            r8.close()     // Catch:{ all -> 0x0047 }
            if (r7 == 0) goto L_0x0034
            r7.close()     // Catch:{ IOException -> 0x005b }
        L_0x0034:
            return r9
        L_0x0035:
            r9 = move-exception
            throw r9     // Catch:{ all -> 0x0037 }
        L_0x0037:
            r1 = move-exception
            if (r9 == 0) goto L_0x0043
            r8.close()     // Catch:{ all -> 0x003e }
            goto L_0x0046
        L_0x003e:
            r8 = move-exception
            r9.addSuppressed(r8)     // Catch:{ all -> 0x0047 }
            goto L_0x0046
        L_0x0043:
            r8.close()     // Catch:{ all -> 0x0047 }
        L_0x0046:
            throw r1     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r8 = move-exception
            throw r8     // Catch:{ all -> 0x0049 }
        L_0x0049:
            r9 = move-exception
            if (r7 == 0) goto L_0x005a
            if (r8 == 0) goto L_0x0057
            r7.close()     // Catch:{ all -> 0x0052 }
            goto L_0x005a
        L_0x0052:
            r7 = move-exception
            r8.addSuppressed(r7)     // Catch:{ IOException -> 0x005b }
            goto L_0x005a
        L_0x0057:
            r7.close()     // Catch:{ IOException -> 0x005b }
        L_0x005a:
            throw r9     // Catch:{ IOException -> 0x005b }
        L_0x005b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    public static ByteBuffer copyToDirectBuffer(Context context, Resources resources, int i) {
        File tempFile = getTempFile(context);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!copyToFile(tempFile, resources, i)) {
                return null;
            }
            ByteBuffer mmap = mmap(tempFile);
            tempFile.delete();
            return mmap;
        } finally {
            tempFile.delete();
        }
    }

    public static boolean copyToFile(File file, InputStream inputStream) {
        StrictMode.ThreadPolicy allowThreadDiskWrites = StrictMode.allowThreadDiskWrites();
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream2.write(bArr, 0, read);
                    } else {
                        closeQuietly(fileOutputStream2);
                        StrictMode.setThreadPolicy(allowThreadDiskWrites);
                        return true;
                    }
                }
            } catch (IOException e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                try {
                    Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
                    closeQuietly(fileOutputStream);
                    StrictMode.setThreadPolicy(allowThreadDiskWrites);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(fileOutputStream);
                    StrictMode.setThreadPolicy(allowThreadDiskWrites);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                closeQuietly(fileOutputStream);
                StrictMode.setThreadPolicy(allowThreadDiskWrites);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
            closeQuietly(fileOutputStream);
            StrictMode.setThreadPolicy(allowThreadDiskWrites);
            return false;
        }
    }

    public static boolean copyToFile(File file, Resources resources, int i) {
        InputStream inputStream;
        try {
            inputStream = resources.openRawResource(i);
            try {
                boolean copyToFile = copyToFile(file, inputStream);
                closeQuietly(inputStream);
                return copyToFile;
            } catch (Throwable th) {
                th = th;
                closeQuietly(inputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            closeQuietly(inputStream);
            throw th;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
