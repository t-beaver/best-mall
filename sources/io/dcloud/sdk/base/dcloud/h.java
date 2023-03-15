package io.dcloud.sdk.base.dcloud;

import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.sdk.poly.base.utils.e;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class h {
    public static boolean a(Object obj) {
        boolean z;
        if (obj == null) {
            return false;
        }
        try {
            File c = c(obj);
            if (!c.exists()) {
                return false;
            }
            if (c.isFile()) {
                return c.delete();
            }
            File[] listFiles = c.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (int i = 0; i < listFiles.length; i++) {
                    e.a("delete:" + listFiles[i].getPath());
                    if (listFiles[i].isDirectory()) {
                        z = a((Object) c.getPath() + "/" + listFiles[i].getName());
                    } else {
                        z = listFiles[i].delete();
                        Thread.sleep(2);
                    }
                    if (!z) {
                        return false;
                    }
                }
            }
            boolean delete = c.delete();
            e.a("delete " + obj + ":" + String.valueOf(delete));
            return delete;
        } catch (Exception e) {
            e.d("DHFile.delete" + e);
            return false;
        }
    }

    public static boolean b(Object obj) {
        boolean z;
        if (obj instanceof String) {
            try {
                String str = (String) obj;
                if (str.endsWith("/")) {
                    str = str.substring(0, str.length() - 1);
                }
                z = new File(str).exists();
            } catch (Exception unused) {
                return false;
            }
        } else if (!(obj instanceof File)) {
            return false;
        } else {
            z = ((File) obj).exists();
        }
        return z;
    }

    private static File c(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.endsWith("/")) {
                str = str.substring(0, str.length() - 1);
            }
            return new File(str);
        } else if (obj instanceof File) {
            return (File) obj;
        } else {
            return null;
        }
    }

    public static InputStream d(Object obj) throws IOException {
        File file;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str = str.substring(7);
            }
            file = new File(str);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException unused) {
            e.b("uniAD", "DHFile getInputStream not found file: " + file.getPath());
            return null;
        } catch (SecurityException e) {
            e.d("getInputStream2" + e);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0042 A[SYNTHETIC, Splitter:B:26:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005e A[SYNTHETIC, Splitter:B:33:0x005e] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x007e A[SYNTHETIC, Splitter:B:40:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0088 A[SYNTHETIC, Splitter:B:45:0x0088] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:37:0x0064=Splitter:B:37:0x0064, B:30:0x0048=Splitter:B:30:0x0048, B:23:0x002c=Splitter:B:23:0x002c} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] e(java.lang.Object r5) {
        /*
            r0 = 0
            java.io.InputStream r5 = d(r5)     // Catch:{ FileNotFoundException -> 0x0062, SecurityException -> 0x0046, IOException -> 0x002a, all -> 0x0025 }
            if (r5 == 0) goto L_0x001d
            byte[] r0 = a((java.io.InputStream) r5)     // Catch:{ FileNotFoundException -> 0x001b, SecurityException -> 0x0019, IOException -> 0x0017 }
            r5.close()     // Catch:{ IOException -> 0x000f }
            goto L_0x0013
        L_0x000f:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0013:
            return r0
        L_0x0014:
            r0 = move-exception
            goto L_0x0086
        L_0x0017:
            r1 = move-exception
            goto L_0x002c
        L_0x0019:
            r1 = move-exception
            goto L_0x0048
        L_0x001b:
            r1 = move-exception
            goto L_0x0064
        L_0x001d:
            if (r5 == 0) goto L_0x0085
            r5.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0085
        L_0x0023:
            r5 = move-exception
            goto L_0x0082
        L_0x0025:
            r5 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
            goto L_0x0086
        L_0x002a:
            r1 = move-exception
            r5 = r0
        L_0x002c:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0014 }
            r2.<init>()     // Catch:{ all -> 0x0014 }
            java.lang.String r3 = "readAll 2:"
            r2.append(r3)     // Catch:{ all -> 0x0014 }
            r2.append(r1)     // Catch:{ all -> 0x0014 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0014 }
            io.dcloud.sdk.poly.base.utils.e.d(r1)     // Catch:{ all -> 0x0014 }
            if (r5 == 0) goto L_0x0085
            r5.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0085
        L_0x0046:
            r1 = move-exception
            r5 = r0
        L_0x0048:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0014 }
            r2.<init>()     // Catch:{ all -> 0x0014 }
            java.lang.String r3 = "readAll 1:"
            r2.append(r3)     // Catch:{ all -> 0x0014 }
            r2.append(r1)     // Catch:{ all -> 0x0014 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0014 }
            io.dcloud.sdk.poly.base.utils.e.d(r1)     // Catch:{ all -> 0x0014 }
            if (r5 == 0) goto L_0x0085
            r5.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0085
        L_0x0062:
            r1 = move-exception
            r5 = r0
        L_0x0064:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0014 }
            r2.<init>()     // Catch:{ all -> 0x0014 }
            java.lang.String r3 = "readAll 0:"
            r2.append(r3)     // Catch:{ all -> 0x0014 }
            java.lang.String r1 = r1.getLocalizedMessage()     // Catch:{ all -> 0x0014 }
            r2.append(r1)     // Catch:{ all -> 0x0014 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0014 }
            io.dcloud.sdk.poly.base.utils.e.a(r1)     // Catch:{ all -> 0x0014 }
            if (r5 == 0) goto L_0x0085
            r5.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0085
        L_0x0082:
            r5.printStackTrace()
        L_0x0085:
            return r0
        L_0x0086:
            if (r5 == 0) goto L_0x0090
            r5.close()     // Catch:{ IOException -> 0x008c }
            goto L_0x0090
        L_0x008c:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0090:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.sdk.base.dcloud.h.e(java.lang.Object):byte[]");
    }

    public static byte[] a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[DHFile.BUF_SIZE];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                a((OutputStream) byteArrayOutputStream);
                return byteArray;
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public static void a(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean a(String str) throws IOException {
        return b(c(str));
    }

    public static void a(byte[] bArr, int i, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            e.a(str + "cannot create!");
        } else if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                randomAccessFile.setLength((long) (bArr.length + i));
                randomAccessFile.seek((long) i);
                randomAccessFile.write(bArr);
                randomAccessFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            }
            if (fileOutputStream != null) {
                if (bArr != null) {
                    try {
                        fileOutputStream.write(bArr, 0, bArr.length);
                        fileOutputStream.flush();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                        try {
                            fileOutputStream.close();
                            return;
                        } catch (IOException e6) {
                            e6.printStackTrace();
                            return;
                        }
                    } catch (Throwable th) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                        throw th;
                    }
                }
                fileOutputStream.close();
            }
        }
    }

    public static void a(InputStream inputStream, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            e.a(str + "cannot create!");
        } else if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                randomAccessFile.seek(file.length());
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = inputStream.read(bArr, 0, 8192);
                    if (read != -1) {
                        randomAccessFile.write(bArr, 0, read);
                    } else {
                        randomAccessFile.close();
                        return;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            }
            if (fileOutputStream != null) {
                try {
                    byte[] bArr2 = new byte[8192];
                    while (true) {
                        int read2 = inputStream.read(bArr2, 0, 8192);
                        if (read2 != -1) {
                            fileOutputStream.write(bArr2, 0, read2);
                        } else {
                            try {
                                fileOutputStream.close();
                                return;
                            } catch (IOException e5) {
                                e5.printStackTrace();
                                return;
                            }
                        }
                    }
                } catch (IOException e6) {
                    e6.printStackTrace();
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                    throw th;
                }
            }
        }
    }

    public static boolean a(String str, String str2) {
        try {
            File file = new File(str);
            if (!file.exists() || !file.isFile() || !file.canRead()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (-1 != read) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
