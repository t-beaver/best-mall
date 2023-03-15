package io.dcloud.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class IOUtil {
    static final int BUF_SIZE = 20480;

    public static final InputStream byte2InputStream(byte[] bArr) {
        return new ByteArrayInputStream(bArr);
    }

    public static void close(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[BUF_SIZE];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                close((OutputStream) byteArrayOutputStream);
                return byteArray;
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x001e A[SYNTHETIC, Splitter:B:16:0x001e] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0026 A[SYNTHETIC, Splitter:B:23:0x0026] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readStringFile(java.lang.String r3) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0017, all -> 0x0015 }
            r1.<init>(r3)     // Catch:{ IOException -> 0x0017, all -> 0x0015 }
            byte[] r3 = getBytes(r1)     // Catch:{ IOException -> 0x0013 }
            java.lang.String r2 = new java.lang.String     // Catch:{ IOException -> 0x0013 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0013 }
            r1.close()     // Catch:{ IOException -> 0x0012 }
        L_0x0012:
            return r2
        L_0x0013:
            r3 = move-exception
            goto L_0x0019
        L_0x0015:
            r3 = move-exception
            goto L_0x0024
        L_0x0017:
            r3 = move-exception
            r1 = r0
        L_0x0019:
            r3.printStackTrace()     // Catch:{ all -> 0x0022 }
            if (r1 == 0) goto L_0x0021
            r1.close()     // Catch:{ IOException -> 0x0021 }
        L_0x0021:
            return r0
        L_0x0022:
            r3 = move-exception
            r0 = r1
        L_0x0024:
            if (r0 == 0) goto L_0x0029
            r0.close()     // Catch:{ IOException -> 0x0029 }
        L_0x0029:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.IOUtil.readStringFile(java.lang.String):java.lang.String");
    }

    public static String toString(InputStream inputStream) throws IOException {
        return inputStream == null ? "" : toStringBuffer(inputStream).toString();
    }

    private static StringBuilder toStringBuffer(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine != null) {
                sb.append(readLine);
                sb.append("\n");
            } else {
                inputStream.close();
                return sb;
            }
        }
    }

    public static boolean writeStringFile(String str, String str2) {
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(str);
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream2, "utf-8");
                outputStreamWriter.write(str2, 0, str2.length());
                outputStreamWriter.flush();
                outputStreamWriter.close();
                close((OutputStream) fileOutputStream2);
                return true;
            } catch (IOException e) {
                e = e;
                fileOutputStream = fileOutputStream2;
                try {
                    e.printStackTrace();
                    close((OutputStream) fileOutputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    close((OutputStream) fileOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = fileOutputStream2;
                close((OutputStream) fileOutputStream);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            close((OutputStream) fileOutputStream);
            return false;
        }
    }

    public static void close(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
