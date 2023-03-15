package io.dcloud.common.adapter.io;

import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class DHFile {
    public static final int BUF_SIZE = 204800;
    public static final byte FS_JAR = 0;
    public static final byte FS_NATIVE = 2;
    public static final byte FS_RMS = 1;
    public static final int READ = 1;
    public static final int READ_WRITE = 3;
    private static final String ROOTPATH = "/";
    public static final int WRITE = 2;

    public static void addFile(String str, byte[] bArr) throws IOException {
        Object createFileHandler = createFileHandler(str);
        createNewFile(createFileHandler);
        OutputStream outputStream = getOutputStream(createFileHandler, false);
        if (outputStream != null) {
            try {
                outputStream.write(bArr, 0, bArr.length);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean canRead(String str) throws IOException {
        return getFile(getRealPath(str)).canRead();
    }

    private static boolean checkIsNeedReload(String str) {
        return str.endsWith(".png") || str.endsWith(".jpg") || str.endsWith(".xml") || str.endsWith(".bmp");
    }

    public static boolean copyAssetsFile(String str, String str2) {
        boolean z = false;
        InputStream inputStream = null;
        try {
            inputStream = PlatformUtil.getResInputStream(str);
            if (inputStream != null) {
                z = writeFile(inputStream, str2);
            } else if (checkIsNeedReload(str)) {
                Logger.d("PlatFU copyAssetsFile fail ！！！！  is = null < " + str + " > to < " + str2 + " >");
            }
            Logger.d("PlatFU copyAssetsFile < " + str + " > to < " + str2 + " >");
        } catch (Exception e) {
            Logger.d("PlatFU copyAssetsFile " + str2 + " error!!!  is it a dir ?");
            StringBuilder sb = new StringBuilder();
            sb.append("PlatFU copyAssetsFile ");
            sb.append(e);
            Logger.d(sb.toString());
            if (checkIsNeedReload(str)) {
                Logger.d("PlatFU copyAssetsFile fail ！！！！ Exception< " + str + " > to < " + str2 + " >");
            }
        } finally {
            IOUtil.close(inputStream);
        }
        return z;
    }

    public static void copyDir(String str, String str2) {
        if (str != null && str2 != null) {
            try {
                if (str.charAt(0) == '/') {
                    str = str.substring(1, str.length());
                }
                if (str.charAt(str.length() - 1) == '/') {
                    str = str.substring(0, str.length() - 1);
                }
                String[] listResFiles = PlatformUtil.listResFiles(str);
                if (createNewFile(str2) != -1) {
                    for (String str3 : listResFiles) {
                        StringBuffer stringBuffer = new StringBuffer();
                        stringBuffer.append(str);
                        stringBuffer.append('/');
                        stringBuffer.append(str3);
                        String stringBuffer2 = stringBuffer.toString();
                        StringBuffer stringBuffer3 = new StringBuffer();
                        stringBuffer3.append(str2);
                        stringBuffer3.append(str3);
                        String stringBuffer4 = stringBuffer3.toString();
                        if (!copyAssetsFile(stringBuffer2, stringBuffer4)) {
                            if (!checkIsNeedReload(str3)) {
                                copyDir(stringBuffer2, stringBuffer4 + "/");
                            } else if (!copyAssetsFile(stringBuffer2, stringBuffer4) && !copyAssetsFile(stringBuffer2, stringBuffer4)) {
                                Logger.d("PlatFU copyDir fail 3 times!!!!" + stringBuffer2);
                                copyDir(stringBuffer2, stringBuffer4 + "/");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r5v1, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r5v2, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r5v4, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r5v5 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01a2 A[Catch:{ all -> 0x01c4 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int copyFile(java.lang.String r17, java.lang.String r18, boolean r19, boolean r20, io.dcloud.common.DHInterface.ICallBack r21) {
        /*
            r0 = r19
            r1 = r20
            r2 = r21
            java.lang.String r3 = getRealPath(r17)
            java.lang.String r4 = getRealPath(r18)
            r5 = 0
            r6 = -1
            r7 = 10
            io.dcloud.application.DCLoudApplicationImpl r9 = io.dcloud.application.DCLoudApplicationImpl.self()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            android.content.Context r9 = r9.getContext()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r10 = 1
            java.lang.String[] r11 = new java.lang.String[r10]     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r12 = 0
            r11[r12] = r4     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            boolean r11 = io.dcloud.common.util.FileUtil.checkPathAccord(r9, r11)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r13 = -3
            r14 = 28
            if (r11 != 0) goto L_0x0037
            int r11 = io.dcloud.common.adapter.util.AndroidResources.sAppTargetSdkVersion     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r11 <= r14) goto L_0x0037
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x0030 }
        L_0x0030:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r5)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            return r13
        L_0x0037:
            boolean r11 = isExist((java.lang.String) r4)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r11 == 0) goto L_0x005a
            if (r1 == 0) goto L_0x004a
            r0 = -2
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x0043 }
        L_0x0043:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r5)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            return r0
        L_0x004a:
            if (r0 == 0) goto L_0x005a
            java.io.File r11 = new java.io.File     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.<init>(r4)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            boolean r11 = isDirectory(r11)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r11 != 0) goto L_0x005a
            deleteFile(r4)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
        L_0x005a:
            java.io.File r11 = new java.io.File     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.<init>(r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            boolean r15 = r11.exists()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r15 != 0) goto L_0x0085
            if (r2 == 0) goto L_0x007b
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.<init>()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r1 = "not file ="
            r0.append(r1)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.append(r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r2.onCallBack(r6, r0)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
        L_0x007b:
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x007e }
        L_0x007e:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r5)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            return r6
        L_0x0085:
            boolean r15 = r11.isDirectory()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r15 == 0) goto L_0x0125
            java.lang.String[] r9 = list(r11)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.<init>()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.append(r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r13 = java.io.File.separator     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            boolean r3 = r3.endsWith(r13)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r14 = ""
            if (r3 == 0) goto L_0x00a3
            r3 = r14
            goto L_0x00a4
        L_0x00a3:
            r3 = r13
        L_0x00a4:
            r11.append(r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r3 = r11.toString()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.<init>()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.append(r4)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            boolean r4 = r4.endsWith(r13)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r4 == 0) goto L_0x00ba
            r13 = r14
        L_0x00ba:
            r11.append(r13)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r4 = r11.toString()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11 = -1
        L_0x00c2:
            int r13 = r9.length     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r12 >= r13) goto L_0x0122
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.<init>()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.append(r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r13 = r9[r12]     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r11.append(r13)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r13.<init>()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r13.append(r4)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r14 = r9[r12]     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r13.append(r14)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            int r11 = copyFile(r11, r13, r0, r1)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r11 == r10) goto L_0x011f
            if (r2 == 0) goto L_0x0115
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.<init>()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r1 = "copyFile error src="
            r0.append(r1)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.append(r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r1 = r9[r12]     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.append(r1)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r1 = " dest="
            r0.append(r1)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.append(r4)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r1 = r9[r12]     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.append(r1)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r2.onCallBack(r11, r0)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
        L_0x0115:
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x0118 }
        L_0x0118:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r5)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            return r11
        L_0x011f:
            int r12 = r12 + 1
            goto L_0x00c2
        L_0x0122:
            r1 = r5
            r6 = r11
            goto L_0x0174
        L_0x0125:
            java.lang.String[] r0 = new java.lang.String[r10]     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0[r12] = r3     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            boolean r0 = io.dcloud.common.util.FileUtil.checkPathAccord(r9, r0)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r0 != 0) goto L_0x0148
            int r0 = io.dcloud.common.adapter.util.AndroidResources.sAppTargetSdkVersion     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r0 <= r14) goto L_0x0148
            boolean r0 = io.dcloud.common.util.FileUtil.isFilePathForPublic(r9, r3)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            if (r0 == 0) goto L_0x013e
            java.io.InputStream r0 = io.dcloud.common.util.FileUtil.getFileInputStream((android.content.Context) r9, (java.io.File) r11)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            goto L_0x014d
        L_0x013e:
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x0141 }
        L_0x0141:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r5)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            return r13
        L_0x0148:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
            r0.<init>(r11)     // Catch:{ Exception -> 0x018a, all -> 0x0187 }
        L_0x014d:
            r1 = r0
            java.lang.Object r0 = createFileHandler(r4)     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            boolean r3 = isExist((java.lang.Object) r0)     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            if (r3 != 0) goto L_0x015b
            createNewFile(r0)     // Catch:{ Exception -> 0x0180, all -> 0x017e }
        L_0x015b:
            java.io.OutputStream r5 = getOutputStream(r0)     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            r0 = 204800(0x32000, float:2.86986E-40)
            byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            if (r1 == 0) goto L_0x0174
        L_0x0166:
            int r3 = r1.read(r0)     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            if (r3 <= 0) goto L_0x0173
            r5.write(r0, r12, r3)     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            r5.flush()     // Catch:{ Exception -> 0x0180, all -> 0x017e }
            goto L_0x0166
        L_0x0173:
            r6 = 1
        L_0x0174:
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x0177 }
        L_0x0177:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            goto L_0x01c3
        L_0x017e:
            r0 = move-exception
            goto L_0x01ca
        L_0x0180:
            r0 = move-exception
            r16 = r5
            r5 = r1
            r1 = r16
            goto L_0x018c
        L_0x0187:
            r0 = move-exception
            r1 = r5
            goto L_0x01ca
        L_0x018a:
            r0 = move-exception
            r1 = r5
        L_0x018c:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c4 }
            r3.<init>()     // Catch:{ all -> 0x01c4 }
            java.lang.String r4 = "copyFile:"
            r3.append(r4)     // Catch:{ all -> 0x01c4 }
            r3.append(r0)     // Catch:{ all -> 0x01c4 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x01c4 }
            io.dcloud.common.adapter.util.Logger.i(r3)     // Catch:{ all -> 0x01c4 }
            if (r2 == 0) goto L_0x01ba
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x01c4 }
            r3.<init>()     // Catch:{ all -> 0x01c4 }
            java.lang.String r4 = "copyFile error Message="
            r3.append(r4)     // Catch:{ all -> 0x01c4 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x01c4 }
            r3.append(r0)     // Catch:{ all -> 0x01c4 }
            java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x01c4 }
            r2.onCallBack(r6, r0)     // Catch:{ all -> 0x01c4 }
        L_0x01ba:
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x01bd }
        L_0x01bd:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r5)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r1)
        L_0x01c3:
            return r6
        L_0x01c4:
            r0 = move-exception
            r16 = r5
            r5 = r1
            r1 = r16
        L_0x01ca:
            java.lang.Thread.sleep(r7)     // Catch:{ Exception -> 0x01cd }
        L_0x01cd:
            io.dcloud.common.util.IOUtil.close((java.io.InputStream) r1)
            io.dcloud.common.util.IOUtil.close((java.io.OutputStream) r5)
            goto L_0x01d5
        L_0x01d4:
            throw r0
        L_0x01d5:
            goto L_0x01d4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.io.DHFile.copyFile(java.lang.String, java.lang.String, boolean, boolean, io.dcloud.common.DHInterface.ICallBack):int");
    }

    public static Object createFileHandler(String str) {
        return str.replace('\\', '/');
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0061 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0063  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte createNewFile(java.lang.Object r7) {
        /*
            r0 = -1
            if (r7 != 0) goto L_0x0004
            return r0
        L_0x0004:
            boolean r1 = r7 instanceof java.lang.String
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0031
            java.lang.String r7 = (java.lang.String) r7
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "createNewFile 0:"
            r1.append(r4)
            r1.append(r7)
            java.lang.String r1 = r1.toString()
            io.dcloud.common.adapter.util.Logger.d(r1)
            java.io.File r1 = new java.io.File
            r1.<init>(r7)
            java.lang.String r4 = "/"
            boolean r7 = r7.endsWith(r4)
            if (r7 == 0) goto L_0x002f
            r7 = 1
            goto L_0x0039
        L_0x002f:
            r7 = 0
            goto L_0x0039
        L_0x0031:
            boolean r1 = r7 instanceof java.io.File
            if (r1 == 0) goto L_0x0078
            r1 = r7
            java.io.File r1 = (java.io.File) r1
            goto L_0x002f
        L_0x0039:
            java.io.File r4 = r1.getParentFile()
            boolean r5 = r4.exists()
            if (r5 != 0) goto L_0x005b
            boolean r4 = r4.mkdirs()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "createNewFile: parentPath mkdirs "
            r5.append(r6)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            io.dcloud.common.adapter.util.Logger.d(r4)
        L_0x005b:
            boolean r4 = r1.exists()
            if (r4 == 0) goto L_0x0063
            r7 = -2
            return r7
        L_0x0063:
            if (r7 == 0) goto L_0x006a
            boolean r3 = r1.mkdirs()
            goto L_0x0075
        L_0x006a:
            boolean r3 = r1.createNewFile()     // Catch:{ IOException -> 0x006f }
            goto L_0x0075
        L_0x006f:
            r7 = move-exception
            java.lang.String r1 = "createNewFile:"
            io.dcloud.common.adapter.util.Logger.w(r1, r7)
        L_0x0075:
            if (r3 == 0) goto L_0x0078
            r0 = 1
        L_0x0078:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.io.DHFile.createNewFile(java.lang.Object):byte");
    }

    public static boolean delete(Object obj) {
        boolean z;
        if (obj == null) {
            return false;
        }
        try {
            File file = getFile(obj);
            if (!file.exists()) {
                return false;
            }
            if (file.isFile()) {
                return file.delete();
            }
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (int i = 0; i < listFiles.length; i++) {
                    Logger.d("delete:" + listFiles[i].getPath());
                    if (listFiles[i].isDirectory()) {
                        z = delete(file.getPath() + "/" + listFiles[i].getName());
                    } else {
                        z = listFiles[i].delete();
                        Thread.sleep(2);
                    }
                    if (!z) {
                        return false;
                    }
                }
            }
            boolean delete = file.delete();
            Logger.i("delete " + obj + ":" + String.valueOf(delete));
            return delete;
        } catch (Exception e) {
            Logger.w("DHFile.delete", e);
            return false;
        }
    }

    public static void deleteAsync(final Object obj, final IAsyncCallback iAsyncCallback) {
        if (iAsyncCallback != null) {
            if (obj == null) {
                iAsyncCallback.done(false);
                return;
            }
            try {
                ThreadPool.self().addSingleThreadTask(new Runnable() {
                    public void run() {
                        boolean z;
                        try {
                            File access$000 = DHFile.getFile(obj);
                            if (!access$000.exists()) {
                                iAsyncCallback.done(false);
                            } else if (access$000.isFile()) {
                                iAsyncCallback.done(access$000.delete());
                            } else {
                                File[] listFiles = access$000.listFiles();
                                if (listFiles != null && listFiles.length > 0) {
                                    for (int i = 0; i < listFiles.length; i++) {
                                        if (listFiles[i].isDirectory()) {
                                            z = DHFile.delete(access$000.getPath() + "/" + listFiles[i].getName());
                                        } else {
                                            z = listFiles[i].delete();
                                        }
                                        if (!z) {
                                            iAsyncCallback.done(false);
                                            return;
                                        }
                                    }
                                }
                                iAsyncCallback.done(access$000.delete());
                            }
                        } catch (Exception e) {
                            Logger.w("DHFile.delete", e);
                            iAsyncCallback.done(false);
                        }
                    }
                });
            } catch (Exception unused) {
                iAsyncCallback.done(false);
            }
        }
    }

    public static int deleteFile(String str) throws IOException {
        return delete(new File(getRealPath(str))) ? 1 : -1;
    }

    public static boolean exists(Object obj) {
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

    /* access modifiers changed from: private */
    public static File getFile(Object obj) {
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

    public static String getFileName(Object obj) {
        return getName(obj);
    }

    public static String getFilePath(Object obj) {
        return getPath(obj);
    }

    public static long getFileSize(File file) {
        long j = 0;
        if (!file.isDirectory()) {
            return 0 + file.length();
        }
        for (File fileSize : file.listFiles()) {
            j += getFileSize(fileSize);
        }
        return j;
    }

    public static String getFileUrl(Object obj) {
        return getPath(obj);
    }

    public static InputStream getInputStream(Object obj) throws IOException {
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
            Logger.e("DHFile getInputStream not found file: " + file.getPath());
            return null;
        } catch (SecurityException e) {
            Logger.w("getInputStream2", e);
            return null;
        }
    }

    public static long getLastModify(String str) throws IOException {
        File file = new File(getRealPath(str));
        if (file.exists()) {
            return file.lastModified();
        }
        return 0;
    }

    public static String getName(Object obj) {
        if (!(obj instanceof String)) {
            return ((File) obj).getName();
        }
        String str = (String) obj;
        if (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str.substring(str.lastIndexOf(47) + 1);
    }

    public static OutputStream getOutputStream(Object obj) throws IOException {
        File file;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null) {
            return null;
        }
        if (file.canWrite()) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Logger.w("getOutputStream:", e);
                return null;
            }
        } else {
            Logger.i("getOutputStream:can not write");
            return null;
        }
    }

    public static Object getParent(Object obj) throws IOException {
        StringBuffer stringBuffer = new StringBuffer(getPath(obj));
        if (((File) obj).isDirectory()) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.delete(stringBuffer.toString().lastIndexOf(47), stringBuffer.length());
        return createFileHandler(stringBuffer.toString());
    }

    public static String getPath(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return str.substring(0, str.lastIndexOf(47) + 1);
        } else if (obj instanceof File) {
            return ((File) obj).getPath();
        } else {
            return null;
        }
    }

    private static String getRealPath(String str) {
        String str2 = DeviceInfo.sBaseFsRootPath;
        StringBuffer stringBuffer = new StringBuffer();
        if ("".equals(str)) {
            return str2;
        }
        try {
            char[] charArray = str.toCharArray();
            int i = 0;
            while (i < charArray.length) {
                int i2 = 3;
                if ((charArray[0] == 'C' || charArray[0] == 'c') && i == 0) {
                    stringBuffer.append(str2);
                    i = 3;
                }
                if ((charArray[0] == 'D' || charArray[0] == 'd') && i == 0) {
                    stringBuffer.append(str2);
                } else {
                    i2 = i;
                }
                if (charArray[i2] == '\\') {
                    stringBuffer.append('/');
                } else {
                    stringBuffer.append(charArray[i2]);
                }
                i = i2 + 1;
            }
            return stringBuffer.toString();
        } catch (ArrayIndexOutOfBoundsException unused) {
            return str;
        }
    }

    public static boolean hasFile() {
        try {
            return new File("/sdcard/.system/45a3c43f-5991-4a65-a420-0a8a71874f72").exists();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isDirectory(Object obj) throws IOException {
        return ((File) obj).isDirectory();
    }

    public static boolean isExist(String str) throws IOException {
        return exists(getFile(getRealPath(str)));
    }

    public static boolean isHidden(Object obj) throws IOException {
        File file = getFile(obj);
        if (file == null) {
            return false;
        }
        return file.isHidden();
    }

    public static long length(Object obj) {
        try {
            return ((File) obj).length();
        } catch (Exception e) {
            Logger.w("length:", e);
            return -1;
        }
    }

    public static String[] list(Object obj) throws IOException {
        Object[] listFiles = listFiles(obj);
        if (listFiles == null) {
            return null;
        }
        String[] strArr = new String[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            File file = (File) listFiles[i];
            if (file.isDirectory()) {
                strArr[i] = file.getName() + "/";
            } else {
                strArr[i] = file.getName();
            }
        }
        return strArr;
    }

    public static String[] listDir(Object obj) throws IOException {
        Object[] listFiles = listFiles(obj);
        if (listFiles == null) {
            return null;
        }
        String[] strArr = new String[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            File file = (File) listFiles[i];
            if (file.isDirectory()) {
                strArr[i] = file.getName() + "/";
            }
        }
        return strArr;
    }

    public static Object[] listFiles(Object obj) throws IOException {
        File file;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null) {
            return null;
        }
        file.isDirectory();
        try {
            return file.listFiles();
        } catch (Exception e) {
            Logger.w("listFiles:", e);
            return null;
        }
    }

    public static List<String> listFilesWithSuffix(Object obj, final String str) {
        File[] listFiles;
        ArrayList arrayList = new ArrayList();
        if (str == null) {
            return arrayList;
        }
        File file = null;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else if (obj instanceof File) {
            file = (File) obj;
        }
        if (file == null || !file.exists() || !file.isDirectory() || (listFiles = file.listFiles(new FilenameFilter() {
            public boolean accept(File file, String str) {
                return new File(file, str).isDirectory() || str.toLowerCase().endsWith(str);
            }
        })) == null) {
            return arrayList;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                arrayList.addAll(listFilesWithSuffix(file2, str));
            } else {
                arrayList.add(file2.getPath());
            }
        }
        return arrayList;
    }

    public static String[] listRoot() throws IOException {
        return new File("/").list();
    }

    protected static Object openFile(String str, int i, boolean z) throws IOException {
        return createFileHandler(getRealPath(str));
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0032 A[SYNTHETIC, Splitter:B:27:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x003f A[SYNTHETIC, Splitter:B:35:0x003f] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x005f A[SYNTHETIC, Splitter:B:42:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0069 A[SYNTHETIC, Splitter:B:47:0x0069] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readAll(java.lang.Object r5) {
        /*
            r0 = 0
            java.io.InputStream r5 = getInputStream(r5)     // Catch:{ FileNotFoundException -> 0x0043, SecurityException -> 0x0036, IOException -> 0x0029, all -> 0x0024 }
            if (r5 == 0) goto L_0x001c
            byte[] r0 = io.dcloud.common.util.IOUtil.getBytes(r5)     // Catch:{ FileNotFoundException -> 0x001a, SecurityException -> 0x0018, IOException -> 0x0016 }
            r5.close()     // Catch:{ IOException -> 0x000f }
            goto L_0x0013
        L_0x000f:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0013:
            return r0
        L_0x0014:
            r0 = move-exception
            goto L_0x0067
        L_0x0016:
            r1 = move-exception
            goto L_0x002b
        L_0x0018:
            r1 = move-exception
            goto L_0x0038
        L_0x001a:
            r1 = move-exception
            goto L_0x0045
        L_0x001c:
            if (r5 == 0) goto L_0x0066
            r5.close()     // Catch:{ IOException -> 0x0022 }
            goto L_0x0066
        L_0x0022:
            r5 = move-exception
            goto L_0x0063
        L_0x0024:
            r5 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
            goto L_0x0067
        L_0x0029:
            r1 = move-exception
            r5 = r0
        L_0x002b:
            java.lang.String r2 = "readAll 2:"
            io.dcloud.common.adapter.util.Logger.w(r2, r1)     // Catch:{ all -> 0x0014 }
            if (r5 == 0) goto L_0x0066
            r5.close()     // Catch:{ IOException -> 0x0022 }
            goto L_0x0066
        L_0x0036:
            r1 = move-exception
            r5 = r0
        L_0x0038:
            java.lang.String r2 = "readAll 1:"
            io.dcloud.common.adapter.util.Logger.w(r2, r1)     // Catch:{ all -> 0x0014 }
            if (r5 == 0) goto L_0x0066
            r5.close()     // Catch:{ IOException -> 0x0022 }
            goto L_0x0066
        L_0x0043:
            r1 = move-exception
            r5 = r0
        L_0x0045:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0014 }
            r2.<init>()     // Catch:{ all -> 0x0014 }
            java.lang.String r3 = "readAll 0:"
            r2.append(r3)     // Catch:{ all -> 0x0014 }
            java.lang.String r1 = r1.getLocalizedMessage()     // Catch:{ all -> 0x0014 }
            r2.append(r1)     // Catch:{ all -> 0x0014 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0014 }
            io.dcloud.common.adapter.util.Logger.i(r1)     // Catch:{ all -> 0x0014 }
            if (r5 == 0) goto L_0x0066
            r5.close()     // Catch:{ IOException -> 0x0022 }
            goto L_0x0066
        L_0x0063:
            r5.printStackTrace()
        L_0x0066:
            return r0
        L_0x0067:
            if (r5 == 0) goto L_0x0071
            r5.close()     // Catch:{ IOException -> 0x006d }
            goto L_0x0071
        L_0x006d:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0071:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.io.DHFile.readAll(java.lang.Object):byte[]");
    }

    public static int rename(String str, String str2) throws IOException {
        String str3;
        String realPath = getRealPath(str);
        if (realPath.endsWith("/")) {
            if (!str2.endsWith("/")) {
                str2 = str2 + "/";
            }
            str3 = realPath.substring(0, realPath.length() - 1);
        } else {
            str3 = null;
        }
        if (str3 == null) {
            return -1;
        }
        if (!PdrUtil.isDeviceRootDir(str2)) {
            str2 = str3.substring(0, str3.lastIndexOf("/") + 1) + str2;
        }
        String realPath2 = getRealPath(str2);
        File file = new File(realPath);
        if (file.exists()) {
            File file2 = new File(realPath2);
            if (!file2.exists() && file.renameTo(file2)) {
                return 1;
            }
        }
        return -1;
    }

    public static boolean writeFile(InputStream inputStream, String str) throws IOException {
        FileOutputStream fileOutputStream;
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        FileOutputStream fileOutputStream2 = null;
        boolean z = false;
        try {
            fileOutputStream = new FileOutputStream(file);
            if (inputStream != null) {
                try {
                    byte[] bArr = new byte[BUF_SIZE];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    z = true;
                } catch (Exception e) {
                    e = e;
                    fileOutputStream2 = fileOutputStream;
                    try {
                        e.printStackTrace();
                        fileOutputStream = fileOutputStream2;
                        IOUtil.close((OutputStream) fileOutputStream);
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        IOUtil.close((OutputStream) fileOutputStream2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream2 = fileOutputStream;
                    IOUtil.close((OutputStream) fileOutputStream2);
                    throw th;
                }
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            fileOutputStream = fileOutputStream2;
            IOUtil.close((OutputStream) fileOutputStream);
            return z;
        }
        IOUtil.close((OutputStream) fileOutputStream);
        return z;
    }

    public static Object openFile(String str, int i) throws IOException {
        return openFile(str, i, false);
    }

    public static boolean isExist(Object obj) throws IOException {
        File file = getFile(obj);
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    public static boolean isHidden(String str) throws IOException {
        File file = new File(getRealPath(str));
        if (file.exists()) {
            return isHidden((Object) file);
        }
        return false;
    }

    public static OutputStream getOutputStream(Object obj, boolean z) throws IOException {
        File file;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null) {
            return null;
        }
        if (file.canWrite()) {
            try {
                return new FileOutputStream(file, z);
            } catch (FileNotFoundException e) {
                Logger.w("getOutputStream:", e);
                return null;
            }
        } else {
            Logger.i("getOutputStream:can not write");
            return null;
        }
    }

    public static void writeFile(byte[] bArr, int i, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            Logger.i(str + "cannot create!");
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

    public static void writeFile(InputStream inputStream, int i, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            Logger.i(str + "cannot create!");
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

    public static int copyFile(String str, String str2) {
        return copyFile(str, str2, false, false, (ICallBack) null);
    }

    public static int copyFile(String str, String str2, ICallBack iCallBack) {
        return copyFile(str, str2, false, false, iCallBack);
    }

    public static int copyFile(String str, String str2, boolean z, boolean z2) {
        return copyFile(str, str2, z, z2, (ICallBack) null);
    }
}
