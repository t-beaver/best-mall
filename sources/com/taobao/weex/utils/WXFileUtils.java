package com.taobao.weex.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import io.dcloud.common.util.Md5Utils;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.weex.DCFileUtils;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class WXFileUtils {
    public static String loadFileOrAsset(String str, Context context) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        File file = new File(str);
        if (!file.exists()) {
            return loadAsset(str, context);
        }
        try {
            return readStreamToString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String loadAsset(String str, Context context) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            String assetPath = DCFileUtils.getAssetPath(str);
            InputStream loadWeexAsset = DCFileUtils.loadWeexAsset(assetPath, context);
            if (loadWeexAsset == null) {
                loadWeexAsset = context.getAssets().open(assetPath);
            }
            return readStreamToString(loadWeexAsset);
        } catch (IOException unused) {
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x004c A[SYNTHETIC, Splitter:B:27:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0056 A[SYNTHETIC, Splitter:B:32:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0063 A[SYNTHETIC, Splitter:B:40:0x0063] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x006d A[SYNTHETIC, Splitter:B:45:0x006d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String readStreamToString(java.io.InputStream r7) {
        /*
            java.lang.String r0 = ""
            java.lang.String r1 = "WXFileUtils loadAsset: "
            r2 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            int r4 = r7.available()     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            int r4 = r4 + 10
            r3.<init>(r4)     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            r5.<init>(r7)     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            r4.<init>(r5)     // Catch:{ IOException -> 0x0044, all -> 0x0042 }
            r2 = 4096(0x1000, float:5.74E-42)
            char[] r2 = new char[r2]     // Catch:{ IOException -> 0x0040 }
        L_0x001e:
            int r5 = r4.read(r2)     // Catch:{ IOException -> 0x0040 }
            if (r5 <= 0) goto L_0x0029
            r6 = 0
            r3.append(r2, r6, r5)     // Catch:{ IOException -> 0x0040 }
            goto L_0x001e
        L_0x0029:
            java.lang.String r0 = r3.toString()     // Catch:{ IOException -> 0x0040 }
            r4.close()     // Catch:{ IOException -> 0x0031 }
            goto L_0x0035
        L_0x0031:
            r2 = move-exception
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r2)
        L_0x0035:
            if (r7 == 0) goto L_0x003f
            r7.close()     // Catch:{ IOException -> 0x003b }
            goto L_0x003f
        L_0x003b:
            r7 = move-exception
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r7)
        L_0x003f:
            return r0
        L_0x0040:
            r2 = move-exception
            goto L_0x0047
        L_0x0042:
            r0 = move-exception
            goto L_0x0061
        L_0x0044:
            r3 = move-exception
            r4 = r2
            r2 = r3
        L_0x0047:
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r2)     // Catch:{ all -> 0x005f }
            if (r4 == 0) goto L_0x0054
            r4.close()     // Catch:{ IOException -> 0x0050 }
            goto L_0x0054
        L_0x0050:
            r2 = move-exception
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r2)
        L_0x0054:
            if (r7 == 0) goto L_0x005e
            r7.close()     // Catch:{ IOException -> 0x005a }
            goto L_0x005e
        L_0x005a:
            r7 = move-exception
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r7)
        L_0x005e:
            return r0
        L_0x005f:
            r0 = move-exception
            r2 = r4
        L_0x0061:
            if (r2 == 0) goto L_0x006b
            r2.close()     // Catch:{ IOException -> 0x0067 }
            goto L_0x006b
        L_0x0067:
            r2 = move-exception
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r2)
        L_0x006b:
            if (r7 == 0) goto L_0x0075
            r7.close()     // Catch:{ IOException -> 0x0071 }
            goto L_0x0075
        L_0x0071:
            r7 = move-exception
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r7)
        L_0x0075:
            goto L_0x0077
        L_0x0076:
            throw r0
        L_0x0077:
            goto L_0x0076
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.readStreamToString(java.io.InputStream):java.lang.String");
    }

    public static byte[] readBytesFromAssets(String str, Context context) {
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                InputStream open = context.getAssets().open(str);
                byte[] bArr = new byte[4096];
                int read = open.read(bArr);
                byte[] bArr2 = new byte[read];
                System.arraycopy(bArr, 0, bArr2, 0, read);
                return bArr2;
            } catch (IOException e) {
                WXLogUtils.e("", (Throwable) e);
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0042 A[SYNTHETIC, Splitter:B:24:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004d A[SYNTHETIC, Splitter:B:30:0x004d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveFile(java.lang.String r2, byte[] r3, android.content.Context r4) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            r1 = 0
            if (r0 != 0) goto L_0x0056
            if (r3 == 0) goto L_0x0056
            if (r4 != 0) goto L_0x000c
            goto L_0x0056
        L_0x000c:
            r4 = 0
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0027 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x0027 }
            r0.write(r3)     // Catch:{ Exception -> 0x0022, all -> 0x001f }
            r2 = 1
            r0.close()     // Catch:{ IOException -> 0x001a }
            goto L_0x001e
        L_0x001a:
            r3 = move-exception
            r3.printStackTrace()
        L_0x001e:
            return r2
        L_0x001f:
            r2 = move-exception
            r4 = r0
            goto L_0x004b
        L_0x0022:
            r2 = move-exception
            r4 = r0
            goto L_0x0028
        L_0x0025:
            r2 = move-exception
            goto L_0x004b
        L_0x0027:
            r2 = move-exception
        L_0x0028:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0025 }
            r3.<init>()     // Catch:{ all -> 0x0025 }
            java.lang.String r0 = "WXFileUtils saveFile: "
            r3.append(r0)     // Catch:{ all -> 0x0025 }
            java.lang.String r2 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r2)     // Catch:{ all -> 0x0025 }
            r3.append(r2)     // Catch:{ all -> 0x0025 }
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x0025 }
            com.taobao.weex.utils.WXLogUtils.e(r2)     // Catch:{ all -> 0x0025 }
            if (r4 == 0) goto L_0x004a
            r4.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x004a
        L_0x0046:
            r2 = move-exception
            r2.printStackTrace()
        L_0x004a:
            return r1
        L_0x004b:
            if (r4 == 0) goto L_0x0055
            r4.close()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0055
        L_0x0051:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0055:
            throw r2
        L_0x0056:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.saveFile(java.lang.String, byte[], android.content.Context):boolean");
    }

    public static String md5(String str) {
        if (str == null) {
            return "";
        }
        try {
            return md5(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }

    public static String md5(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance(Md5Utils.ALGORITHM);
            instance.update(bArr);
            return new BigInteger(1, instance.digest()).toString(16);
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    public static String base64Md5(String str) {
        if (str == null) {
            return "";
        }
        try {
            return base64Md5(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }

    public static String base64Md5(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance(Md5Utils.ALGORITHM);
            instance.update(bArr);
            return Base64.encodeToString(instance.digest(), 2);
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    private static String[] validCPUABIS() {
        String[] strArr = Build.VERSION.SDK_INT >= 21 ? Build.SUPPORTED_ABIS : null;
        if (strArr != null && strArr.length != 0) {
            return strArr;
        }
        return new String[]{Build.CPU_ABI};
    }

    private static boolean validLibPath(String str) {
        for (String contains : validCPUABIS()) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    private static boolean replaceLib(String str, String str2) {
        if (str == null || str2 == null) {
            return true;
        }
        boolean z = false;
        for (String str3 : validCPUABIS()) {
            if (str2.contains(str3) && z) {
                return true;
            }
            if (str.contains(str3)) {
                z = true;
            }
        }
        return false;
    }

    public static boolean extractSo(String str, String str2) throws IOException {
        boolean z = false;
        if (PdrUtil.isSafeEntryName(str) && PdrUtil.isSafeEntryName(str2)) {
            HashMap hashMap = new HashMap();
            ZipFile zipFile = new ZipFile(str);
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(str)));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                if (!zipEntry.isDirectory() && validLibPath(zipEntry.getName())) {
                    if (zipEntry.getName().contains("weex") || zipEntry.getName().equals("libjsc.so") || zipEntry.getName().equals("libJavaScriptCore.so")) {
                        String[] split = zipEntry.getName().split("/");
                        String str3 = split[split.length - 1];
                        File file = new File(str2 + "/" + str3);
                        if (replaceLib(zipEntry.getName(), (String) hashMap.get(str3))) {
                            hashMap.put(str3, zipEntry.getName());
                            if (file.exists()) {
                                file.delete();
                            }
                            InputStream inputStream = zipFile.getInputStream(zipEntry);
                            byte[] bArr = new byte[1024];
                            file.createNewFile();
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            while (inputStream.read(bArr) != -1) {
                                fileOutputStream.write(bArr);
                            }
                            fileOutputStream.close();
                        }
                        z = true;
                    }
                }
            }
            zipInputStream.closeEntry();
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x005b A[SYNTHETIC, Splitter:B:18:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0065 A[SYNTHETIC, Splitter:B:23:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFile(java.io.File r5, java.io.File r6) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0027 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0027 }
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0023 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0023 }
            r3.<init>(r6)     // Catch:{ Exception -> 0x0023 }
        L_0x000f:
            int r0 = r1.read(r2)     // Catch:{ Exception -> 0x0021 }
            r4 = -1
            if (r0 == r4) goto L_0x001a
            r3.write(r2)     // Catch:{ Exception -> 0x0021 }
            goto L_0x000f
        L_0x001a:
            r1.close()     // Catch:{ Exception -> 0x0021 }
            r3.close()     // Catch:{ Exception -> 0x0021 }
            goto L_0x006d
        L_0x0021:
            r0 = move-exception
            goto L_0x002b
        L_0x0023:
            r2 = move-exception
            r3 = r0
            r0 = r2
            goto L_0x002b
        L_0x0027:
            r1 = move-exception
            r3 = r0
            r0 = r1
            r1 = r3
        L_0x002b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "copyFile "
            r2.append(r4)
            java.lang.String r0 = r0.getMessage()
            r2.append(r0)
            java.lang.String r0 = ": "
            r2.append(r0)
            java.lang.String r5 = r5.getAbsolutePath()
            r2.append(r5)
            r2.append(r0)
            java.lang.String r5 = r6.getAbsolutePath()
            r2.append(r5)
            java.lang.String r5 = r2.toString()
            com.taobao.weex.utils.WXLogUtils.e(r5)
            if (r1 == 0) goto L_0x0063
            r1.close()     // Catch:{ IOException -> 0x005f }
            goto L_0x0063
        L_0x005f:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0063:
            if (r3 == 0) goto L_0x006d
            r3.close()     // Catch:{ IOException -> 0x0069 }
            goto L_0x006d
        L_0x0069:
            r5 = move-exception
            r5.printStackTrace()
        L_0x006d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.copyFile(java.io.File, java.io.File):void");
    }

    public static void copyFileWithException(File file, File file2) throws Exception {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                byte[] bArr = new byte[1024];
                fileOutputStream = new FileOutputStream(file2);
                while (fileInputStream2.read(bArr) != -1) {
                    try {
                        fileOutputStream.write(bArr);
                    } catch (Exception e) {
                        e = e;
                        fileInputStream = fileInputStream2;
                        try {
                            throw e;
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream = fileInputStream2;
                        closeIo(fileInputStream);
                        closeIo(fileOutputStream);
                        throw th;
                    }
                }
                closeIo(fileInputStream2);
                closeIo(fileOutputStream);
            } catch (Exception e2) {
                e = e2;
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                throw e;
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                closeIo(fileInputStream);
                closeIo(fileOutputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            fileOutputStream = null;
            throw e;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream = null;
            closeIo(fileInputStream);
            closeIo(fileOutputStream);
            throw th;
        }
    }

    public static void closeIo(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
