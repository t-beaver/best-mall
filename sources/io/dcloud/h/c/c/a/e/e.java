package io.dcloud.h.c.c.a.e;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.sdk.base.dcloud.h;
import io.dcloud.sdk.poly.base.utils.c;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class e {
    private static String a = "";
    private static String b;
    private static String c;

    private static boolean a(String str) {
        return TextUtils.isEmpty(str) || str.contains("Unknown") || str.contains("00000000");
    }

    private static StringBuilder b(InputStream inputStream) throws IOException {
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

    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0105, code lost:
        if (android.text.TextUtils.isEmpty(r1) != false) goto L_0x011b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0114, code lost:
        if (android.text.TextUtils.isEmpty(r1) != false) goto L_0x011b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0117, code lost:
        r5 = r1.replace("\n", r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x011b, code lost:
        r3.append(r5);
        r3.append("|");
     */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0158 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0159  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.content.Context r11, boolean r12, boolean r13) {
        /*
            java.lang.String r0 = "\n"
            r1 = 0
            r2 = 2
            java.lang.String r3 = "io.dcloud.common.util.TelephonyUtil"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ Exception -> 0x0041 }
            java.lang.String r4 = "getSBBS"
            r5 = 4
            java.lang.Class[] r6 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x0041 }
            java.lang.Class<android.content.Context> r7 = android.content.Context.class
            r8 = 0
            r6[r8] = r7     // Catch:{ Exception -> 0x0041 }
            java.lang.Class r7 = java.lang.Boolean.TYPE     // Catch:{ Exception -> 0x0041 }
            r9 = 1
            r6[r9] = r7     // Catch:{ Exception -> 0x0041 }
            r6[r2] = r7     // Catch:{ Exception -> 0x0041 }
            r10 = 3
            r6[r10] = r7     // Catch:{ Exception -> 0x0041 }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r6)     // Catch:{ Exception -> 0x0041 }
            java.lang.Object[] r4 = new java.lang.Object[r5]     // Catch:{ Exception -> 0x0041 }
            r4[r8] = r11     // Catch:{ Exception -> 0x0041 }
            java.lang.Boolean r5 = java.lang.Boolean.TRUE     // Catch:{ Exception -> 0x0041 }
            r4[r9] = r5     // Catch:{ Exception -> 0x0041 }
            r4[r2] = r5     // Catch:{ Exception -> 0x0041 }
            io.dcloud.h.c.c.a.a r5 = io.dcloud.h.c.c.a.a.a()     // Catch:{ Exception -> 0x0041 }
            boolean r5 = r5.a((android.content.Context) r11)     // Catch:{ Exception -> 0x0041 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ Exception -> 0x0041 }
            r4[r10] = r5     // Catch:{ Exception -> 0x0041 }
            java.lang.Object r3 = r3.invoke(r1, r4)     // Catch:{ Exception -> 0x0041 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0041 }
            return r3
        L_0x0041:
            r3 = move-exception
            boolean r4 = io.dcloud.sdk.poly.base.utils.b.a
            if (r4 == 0) goto L_0x0049
            r3.printStackTrace()
        L_0x0049:
            java.lang.StringBuffer r3 = new java.lang.StringBuffer
            r3.<init>()
            java.lang.String r4 = "|"
            if (r12 == 0) goto L_0x0056
            r3.append(r4)
        L_0x0056:
            java.lang.String r5 = ""
            if (r11 != 0) goto L_0x0060
            if (r12 == 0) goto L_0x005f
            java.lang.String r5 = "|||||"
        L_0x005f:
            return r5
        L_0x0060:
            if (r13 == 0) goto L_0x007c
            if (r12 == 0) goto L_0x006f
            java.lang.String r6 = c
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x006f
            java.lang.String r11 = c
            return r11
        L_0x006f:
            java.lang.String r6 = a
            boolean r6 = a((java.lang.String) r6)
            if (r6 != 0) goto L_0x0096
            if (r12 != 0) goto L_0x0096
            java.lang.String r11 = a
            return r11
        L_0x007c:
            if (r12 == 0) goto L_0x0089
            java.lang.String r6 = b
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 != 0) goto L_0x0089
            java.lang.String r11 = b
            return r11
        L_0x0089:
            java.lang.String r6 = a
            boolean r6 = a((java.lang.String) r6)
            if (r6 != 0) goto L_0x0096
            if (r12 != 0) goto L_0x0096
            java.lang.String r11 = a
            return r11
        L_0x0096:
            java.lang.String[] r6 = io.dcloud.h.a.d.b.c.f(r11)
            java.lang.String r7 = a
            boolean r7 = a((java.lang.String) r7)
            if (r7 != 0) goto L_0x00a8
            if (r12 == 0) goto L_0x00a5
            goto L_0x00a8
        L_0x00a5:
            java.lang.String r11 = a
            return r11
        L_0x00a8:
            if (r6 == 0) goto L_0x00b3
            java.lang.String r7 = ","
            java.lang.String r6 = android.text.TextUtils.join(r7, r6)
            a = r6
            goto L_0x00b5
        L_0x00b3:
            a = r5
        L_0x00b5:
            if (r12 == 0) goto L_0x00bf
            java.lang.String r6 = a
            r3.append(r6)
            r3.append(r4)
        L_0x00bf:
            if (r12 == 0) goto L_0x00c4
            r3.append(r4)
        L_0x00c4:
            java.lang.String r6 = a
            boolean r6 = a((java.lang.String) r6)
            if (r6 != 0) goto L_0x00d2
            if (r12 == 0) goto L_0x00cf
            goto L_0x00d2
        L_0x00cf:
            java.lang.String r11 = a
            return r11
        L_0x00d2:
            java.lang.String r7 = io.dcloud.h.a.d.b.c.a((android.content.Context) r11)
            if (r6 == 0) goto L_0x00da
            a = r7
        L_0x00da:
            if (r12 == 0) goto L_0x00e9
            boolean r6 = android.text.TextUtils.isEmpty(r7)
            if (r6 == 0) goto L_0x00e3
            r7 = r5
        L_0x00e3:
            r3.append(r7)
            r3.append(r4)
        L_0x00e9:
            java.lang.String r6 = a
            boolean r6 = a((java.lang.String) r6)
            if (r6 != 0) goto L_0x00f7
            if (r12 == 0) goto L_0x00f4
            goto L_0x00f7
        L_0x00f4:
            java.lang.String r11 = a
            return r11
        L_0x00f7:
            java.lang.String r1 = a((android.content.Context) r11)     // Catch:{ Exception -> 0x010a }
            if (r6 == 0) goto L_0x00ff
            a = r1     // Catch:{ Exception -> 0x010a }
        L_0x00ff:
            if (r12 == 0) goto L_0x0121
            boolean r11 = android.text.TextUtils.isEmpty(r1)
            if (r11 == 0) goto L_0x0117
            goto L_0x011b
        L_0x0108:
            r11 = move-exception
            goto L_0x015c
        L_0x010a:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ all -> 0x0108 }
            if (r12 == 0) goto L_0x0121
            boolean r11 = android.text.TextUtils.isEmpty(r1)
            if (r11 == 0) goto L_0x0117
            goto L_0x011b
        L_0x0117:
            java.lang.String r5 = r1.replace(r0, r5)
        L_0x011b:
            r3.append(r5)
            r3.append(r4)
        L_0x0121:
            java.lang.String r11 = r3.toString()
            byte[] r11 = r11.getBytes()
            java.lang.String r12 = io.dcloud.h.c.c.a.e.b.b()
            java.lang.String r0 = io.dcloud.h.c.c.a.e.b.a()
            byte[] r11 = io.dcloud.h.a.d.b.a.b(r11, r12, r0)
            java.lang.String r11 = android.util.Base64.encodeToString(r11, r2)
            java.lang.String r11 = java.net.URLEncoder.encode(r11)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r11)
            java.lang.String r11 = "&ie=1"
            r12.append(r11)
            java.lang.String r11 = r12.toString()
            b = r11
            java.lang.String r11 = r3.toString()
            c = r11
            if (r13 == 0) goto L_0x0159
            return r11
        L_0x0159:
            java.lang.String r11 = b
            return r11
        L_0x015c:
            if (r12 == 0) goto L_0x016f
            boolean r12 = android.text.TextUtils.isEmpty(r1)
            if (r12 == 0) goto L_0x0165
            goto L_0x0169
        L_0x0165:
            java.lang.String r5 = r1.replace(r0, r5)
        L_0x0169:
            r3.append(r5)
            r3.append(r4)
        L_0x016f:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.c.c.a.e.e.a(android.content.Context, boolean, boolean):java.lang.String");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:(3:26|27|(6:29|31|(3:33|(1:35)|36)|37|38|(1:40)(1:49)))|30|31|(0)|37|38|(0)(0)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:37:0x00ed */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00c7 A[Catch:{ Exception -> 0x00ed }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00f3 A[Catch:{ Exception -> 0x0100 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.content.Context r11) {
        /*
            java.lang.String r0 = ".imei.txt"
            r1 = 0
            boolean r2 = io.dcloud.sdk.poly.base.utils.c.a(r11)     // Catch:{ Exception -> 0x0100 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0100 }
            r3.<init>()     // Catch:{ Exception -> 0x0100 }
            java.io.File r4 = r11.getFilesDir()     // Catch:{ Exception -> 0x0100 }
            r3.append(r4)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r4 = java.io.File.separator     // Catch:{ Exception -> 0x0100 }
            r3.append(r4)     // Catch:{ Exception -> 0x0100 }
            r3.append(r0)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0100 }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0100 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x0100 }
            boolean r6 = r5.exists()     // Catch:{ Exception -> 0x0100 }
            java.lang.String r7 = ".DC4278477faeb9.txt"
            if (r6 != 0) goto L_0x0047
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0100 }
            r3.<init>()     // Catch:{ Exception -> 0x0100 }
            java.io.File r5 = r11.getFilesDir()     // Catch:{ Exception -> 0x0100 }
            r3.append(r5)     // Catch:{ Exception -> 0x0100 }
            r3.append(r4)     // Catch:{ Exception -> 0x0100 }
            r3.append(r7)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0100 }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0100 }
            r5.<init>(r3)     // Catch:{ Exception -> 0x0100 }
        L_0x0047:
            java.lang.String r6 = "mounted"
            java.lang.String r8 = android.os.Environment.getExternalStorageState()     // Catch:{ Exception -> 0x0100 }
            boolean r6 = r6.equalsIgnoreCase(r8)     // Catch:{ Exception -> 0x0100 }
            if (r6 == 0) goto L_0x0092
            if (r2 != 0) goto L_0x0092
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0100 }
            r6.<init>()     // Catch:{ Exception -> 0x0100 }
            java.io.File r8 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x0100 }
            r6.append(r8)     // Catch:{ Exception -> 0x0100 }
            r6.append(r4)     // Catch:{ Exception -> 0x0100 }
            r6.append(r0)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r0 = r6.toString()     // Catch:{ Exception -> 0x0100 }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x0100 }
            r6.<init>(r0)     // Catch:{ Exception -> 0x0100 }
            boolean r8 = r6.exists()     // Catch:{ Exception -> 0x0100 }
            if (r8 != 0) goto L_0x0094
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0100 }
            r0.<init>()     // Catch:{ Exception -> 0x0100 }
            java.io.File r6 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x0100 }
            r0.append(r6)     // Catch:{ Exception -> 0x0100 }
            r0.append(r4)     // Catch:{ Exception -> 0x0100 }
            r0.append(r7)     // Catch:{ Exception -> 0x0100 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0100 }
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x0100 }
            r6.<init>(r0)     // Catch:{ Exception -> 0x0100 }
            goto L_0x0094
        L_0x0092:
            r0 = r1
            r6 = r0
        L_0x0094:
            boolean r4 = r5.isDirectory()     // Catch:{ Exception -> 0x0100 }
            if (r4 == 0) goto L_0x009d
            r5.delete()     // Catch:{ Exception -> 0x0100 }
        L_0x009d:
            if (r6 == 0) goto L_0x00f8
            boolean r4 = r6.exists()     // Catch:{ Exception -> 0x0100 }
            if (r4 == 0) goto L_0x00f8
            long r7 = r6.length()     // Catch:{ Exception -> 0x0100 }
            r9 = 0
            int r4 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r4 <= 0) goto L_0x00f8
            if (r2 == 0) goto L_0x00b8
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x00ed }
            r4 = 29
            if (r2 < r4) goto L_0x00b8
            goto L_0x00c1
        L_0x00b8:
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00ed }
            r2.<init>(r6)     // Catch:{ Exception -> 0x00ed }
            java.lang.String r1 = a((java.io.InputStream) r2)     // Catch:{ Exception -> 0x00ed }
        L_0x00c1:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x00ed }
            if (r2 != 0) goto L_0x00ed
            java.io.File r2 = r5.getParentFile()     // Catch:{ Exception -> 0x00ed }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x00ed }
            if (r2 != 0) goto L_0x00db
            java.io.File r2 = r5.getParentFile()     // Catch:{ Exception -> 0x00ed }
            r2.mkdirs()     // Catch:{ Exception -> 0x00ed }
            r5.createNewFile()     // Catch:{ Exception -> 0x00ed }
        L_0x00db:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00ed }
            r2.<init>(r5)     // Catch:{ Exception -> 0x00ed }
            byte[] r4 = r1.getBytes()     // Catch:{ Exception -> 0x00ed }
            r2.write(r4)     // Catch:{ Exception -> 0x00ed }
            r2.flush()     // Catch:{ Exception -> 0x00ed }
            r2.close()     // Catch:{ Exception -> 0x00ed }
        L_0x00ed:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x0100 }
            if (r2 == 0) goto L_0x0104
            java.lang.String r11 = a(r5, r6, r3, r0, r11)     // Catch:{ Exception -> 0x0100 }
            goto L_0x00fc
        L_0x00f8:
            java.lang.String r11 = a(r5, r6, r3, r0, r11)     // Catch:{ Exception -> 0x0100 }
        L_0x00fc:
            r1 = r11
            goto L_0x0104
        L_0x00fe:
            r11 = move-exception
            goto L_0x0105
        L_0x0100:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ all -> 0x00fe }
        L_0x0104:
            return r1
        L_0x0105:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.c.c.a.e.e.a(android.content.Context):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String a(java.io.File r6, java.io.File r7, java.lang.String r8, java.lang.String r9, android.content.Context r10) throws java.io.IOException {
        /*
            boolean r0 = r6.exists()
            java.lang.String r1 = ".DC4278477faeb9.txt"
            if (r0 == 0) goto L_0x0047
            long r2 = r6.length()
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0047
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x003e }
            r0.<init>(r6)     // Catch:{ Exception -> 0x003e }
            java.lang.String r0 = a((java.io.InputStream) r0)     // Catch:{ Exception -> 0x003e }
            if (r7 == 0) goto L_0x004b
            boolean r2 = io.dcloud.sdk.poly.base.utils.c.a(r10)     // Catch:{ Exception -> 0x003c }
            if (r2 == 0) goto L_0x0024
            goto L_0x004b
        L_0x0024:
            java.io.File r2 = r7.getParentFile()     // Catch:{ Exception -> 0x003c }
            boolean r2 = r2.exists()     // Catch:{ Exception -> 0x003c }
            if (r2 != 0) goto L_0x0038
            java.io.File r2 = r7.getParentFile()     // Catch:{ Exception -> 0x003c }
            r2.mkdirs()     // Catch:{ Exception -> 0x003c }
            r7.createNewFile()     // Catch:{ Exception -> 0x003c }
        L_0x0038:
            io.dcloud.sdk.base.dcloud.h.a((java.lang.String) r8, (java.lang.String) r9)     // Catch:{ Exception -> 0x003c }
            goto L_0x004b
        L_0x003c:
            goto L_0x0040
        L_0x003e:
            r8 = 0
            r0 = r8
        L_0x0040:
            if (r0 != 0) goto L_0x004b
            java.lang.String r0 = a(r10, r6, r7, r1)
            goto L_0x004b
        L_0x0047:
            java.lang.String r0 = a(r10, r6, r7, r1)
        L_0x004b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.c.c.a.e.e.a(java.io.File, java.io.File, java.lang.String, java.lang.String, android.content.Context):java.lang.String");
    }

    private static String a(Context context, File file, File file2, String str) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        String replace = UUID.randomUUID().toString().replaceAll(Operators.SUB, "").replace("\n", "");
        byte[] bytes = replace.getBytes();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            if (!c.a(context)) {
                h.a(file.getPath(), file2.getPath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return replace;
    }

    public static String a(InputStream inputStream) throws IOException {
        return inputStream == null ? "" : b(inputStream).toString();
    }
}
