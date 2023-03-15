package io.dcloud.common.util;

import io.dcloud.common.adapter.util.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static final int BUFF_SIZE = 1048576;

    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0049 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String inflaterString(byte[] r4) {
        /*
            java.util.zip.InflaterInputStream r0 = new java.util.zip.InflaterInputStream
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream
            r1.<init>(r4)
            java.util.zip.Inflater r4 = new java.util.zip.Inflater
            r2 = 1
            r4.<init>(r2)
            r0.<init>(r1, r4)
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream
            r4.<init>()
            r1 = 1024(0x400, float:1.435E-42)
            byte[] r1 = new byte[r1]     // Catch:{ Exception -> 0x0049, all -> 0x0035 }
        L_0x0019:
            int r2 = r0.read(r1)     // Catch:{ Exception -> 0x0049, all -> 0x0035 }
            r3 = -1
            if (r2 == r3) goto L_0x0025
            r3 = 0
            r4.write(r1, r3, r2)     // Catch:{ Exception -> 0x0049, all -> 0x0035 }
            goto L_0x0019
        L_0x0025:
            java.lang.String r1 = new java.lang.String     // Catch:{ Exception -> 0x005c }
            byte[] r2 = r4.toByteArray()     // Catch:{ Exception -> 0x005c }
            r1.<init>(r2)     // Catch:{ Exception -> 0x005c }
            r0.close()     // Catch:{ Exception -> 0x005e }
            r4.close()     // Catch:{ Exception -> 0x005e }
            goto L_0x0058
        L_0x0035:
            r1 = move-exception
            java.lang.String r2 = new java.lang.String     // Catch:{ Exception -> 0x0048 }
            byte[] r3 = r4.toByteArray()     // Catch:{ Exception -> 0x0048 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0048 }
            r0.close()     // Catch:{ Exception -> 0x0048 }
            r4.close()     // Catch:{ Exception -> 0x0048 }
            r4.flush()     // Catch:{ Exception -> 0x0048 }
        L_0x0048:
            throw r1
        L_0x0049:
            java.lang.String r1 = new java.lang.String     // Catch:{ Exception -> 0x005c }
            byte[] r2 = r4.toByteArray()     // Catch:{ Exception -> 0x005c }
            r1.<init>(r2)     // Catch:{ Exception -> 0x005c }
            r0.close()     // Catch:{ Exception -> 0x005e }
            r4.close()     // Catch:{ Exception -> 0x005e }
        L_0x0058:
            r4.flush()     // Catch:{ Exception -> 0x005e }
            goto L_0x005e
        L_0x005c:
            java.lang.String r1 = ""
        L_0x005e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.ZipUtils.inflaterString(byte[]):java.lang.String");
    }

    public static void upZipFile(File file, String str) throws ZipException, IOException {
        if (!str.endsWith("/")) {
            str = str + File.separatorChar;
        }
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (zipEntry.getName().contains("../")) {
                Logger.d("ZIP", "util.ZipUtils Path traversal attack prevented path=" + zipEntry.getName());
            } else {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                String replace = new String((str + zipEntry.getName()).getBytes(), "UTF-8").replace("\\", "/");
                File file3 = new File(replace);
                File parentFile = file3.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!replace.endsWith("/")) {
                    if (file3.exists()) {
                        file3.delete();
                    }
                    file3.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    byte[] bArr = new byte[1048576];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                }
            }
        }
        zipFile.close();
    }

    private static void zipFile(File file, ZipOutputStream zipOutputStream, String str) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str.trim().length() == 0 ? "" : File.separator);
        sb.append(file.getName());
        String str2 = new String(sb.toString().getBytes("UTF-8"), "UTF-8");
        if (file.isDirectory()) {
            for (File zipFile : file.listFiles()) {
                zipFile(zipFile, zipOutputStream, str2);
            }
            return;
        }
        byte[] bArr = new byte[1048576];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1048576);
        zipOutputStream.putNextEntry(new ZipEntry(str2));
        while (true) {
            int read = bufferedInputStream.read(bArr);
            if (read != -1) {
                zipOutputStream.write(bArr, 0, read);
            } else {
                bufferedInputStream.close();
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
                return;
            }
        }
    }

    public static void zipFiles(File[] fileArr, File file) throws IOException {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file), 1048576));
        for (File zipFile : fileArr) {
            zipFile(zipFile, zipOutputStream, "");
        }
        zipOutputStream.close();
    }

    public static byte[] zipString(String str) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes());
            gZIPOutputStream.flush();
            gZIPOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }
}
