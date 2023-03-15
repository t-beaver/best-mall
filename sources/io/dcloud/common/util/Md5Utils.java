package io.dcloud.common.util;

import android.text.TextUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
    public static final String ALGORITHM = "MD5";
    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_LOWER_CASE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(ALGORITHM);
            instance.update(str.getBytes("UTF-8"));
            return toHex(instance.digest());
        } catch (UnsupportedEncodingException | Exception | NoSuchAlgorithmException unused) {
            return str;
        }
    }

    public static String md5LowerCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance(ALGORITHM);
            instance.update(str.getBytes("UTF-8"));
            return toLowerCaseHex(instance.digest());
        } catch (UnsupportedEncodingException | Exception | NoSuchAlgorithmException unused) {
            return str;
        }
    }

    public static String md5LowerCase32Bit(String str) {
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance(ALGORITHM);
            instance.update(bytes);
            char[] cArr = new char[(r0 * 2)];
            int i = 0;
            for (byte b : instance.digest()) {
                int i2 = i + 1;
                char[] cArr2 = HEX;
                cArr[i] = cArr2[(b >>> 4) & 15];
                i = i2 + 1;
                cArr[i2] = cArr2[b & 15];
            }
            return new String(cArr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String toHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            char[] cArr = HEX;
            sb.append(cArr[(b & 240) >> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }

    private static String toLowerCaseHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            char[] cArr = HEX_LOWER_CASE;
            sb.append(cArr[(b & 240) >> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = md5(new java.io.File(r1));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean verifyFileMd5(java.lang.String r1, java.lang.String r2) {
        /*
            int r0 = r1.length()
            if (r0 <= 0) goto L_0x001b
            java.io.File r0 = new java.io.File
            r0.<init>(r1)
            java.lang.String r1 = md5((java.io.File) r0)
            if (r1 == 0) goto L_0x001b
            if (r2 == 0) goto L_0x001b
            int r1 = r1.compareToIgnoreCase(r2)
            if (r1 != 0) goto L_0x001b
            r1 = 1
            return r1
        L_0x001b:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.Md5Utils.verifyFileMd5(java.lang.String, java.lang.String):boolean");
    }

    public static String md5(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance(ALGORITHM);
            instance.update(bArr);
            return toHex(instance.digest());
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    public static String md5(File file) {
        return md5(file, ALGORITHM);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0054 A[SYNTHETIC, Splitter:B:24:0x0054] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0061 A[SYNTHETIC, Splitter:B:32:0x0061] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String md5(java.io.File r4, java.lang.String r5) {
        /*
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]
            r1 = 0
            io.dcloud.application.DCLoudApplicationImpl r2 = io.dcloud.application.DCLoudApplicationImpl.self()     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            android.content.Context r2 = r2.getContext()     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            java.lang.String r3 = r4.getPath()     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            boolean r2 = io.dcloud.common.util.FileUtil.checkPrivatePath(r2, r3)     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            if (r2 == 0) goto L_0x001d
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            r2.<init>(r4)     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            goto L_0x0029
        L_0x001d:
            io.dcloud.application.DCLoudApplicationImpl r2 = io.dcloud.application.DCLoudApplicationImpl.self()     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            android.content.Context r2 = r2.getContext()     // Catch:{ Exception -> 0x004d, all -> 0x004b }
            java.io.InputStream r2 = io.dcloud.common.util.FileUtil.getFileInputStream((android.content.Context) r2, (java.io.File) r4)     // Catch:{ Exception -> 0x004d, all -> 0x004b }
        L_0x0029:
            java.security.MessageDigest r4 = java.security.MessageDigest.getInstance(r5)     // Catch:{ Exception -> 0x0049 }
        L_0x002d:
            int r5 = r2.read(r0)     // Catch:{ Exception -> 0x0049 }
            if (r5 <= 0) goto L_0x0038
            r3 = 0
            r4.update(r0, r3, r5)     // Catch:{ Exception -> 0x0049 }
            goto L_0x002d
        L_0x0038:
            byte[] r4 = r4.digest()     // Catch:{ Exception -> 0x0049 }
            java.lang.String r4 = toHex(r4)     // Catch:{ Exception -> 0x0049 }
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0048
        L_0x0044:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0048:
            return r4
        L_0x0049:
            r4 = move-exception
            goto L_0x004f
        L_0x004b:
            r4 = move-exception
            goto L_0x005f
        L_0x004d:
            r4 = move-exception
            r2 = r1
        L_0x004f:
            r4.printStackTrace()     // Catch:{ all -> 0x005d }
            if (r2 == 0) goto L_0x005c
            r2.close()     // Catch:{ IOException -> 0x0058 }
            goto L_0x005c
        L_0x0058:
            r4 = move-exception
            r4.printStackTrace()
        L_0x005c:
            return r1
        L_0x005d:
            r4 = move-exception
            r1 = r2
        L_0x005f:
            if (r1 == 0) goto L_0x0069
            r1.close()     // Catch:{ IOException -> 0x0065 }
            goto L_0x0069
        L_0x0065:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0069:
            goto L_0x006b
        L_0x006a:
            throw r4
        L_0x006b:
            goto L_0x006a
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.Md5Utils.md5(java.io.File, java.lang.String):java.lang.String");
    }
}
