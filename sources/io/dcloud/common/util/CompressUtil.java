package io.dcloud.common.util;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CompressUtil {
    /* JADX WARNING: Can't wrap try/catch for region: R(2:62|63) */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:50|51|52|53|54|55|56|57|58) */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x012f, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
        throw r0;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:54:0x0128 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:56:0x012b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:62:0x0133 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:64:0x0134 */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x012f A[ExcHandler: all (r0v3 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:30:0x00d5] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:62:0x0133=Splitter:B:62:0x0133, B:56:0x012b=Splitter:B:56:0x012b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String compressImage(java.lang.String r19, java.lang.String r20, boolean r21, android.content.Context r22) {
        /*
            r0 = r20
            java.lang.String r1 = "Orientation"
            java.lang.String r2 = ""
            r3 = r19
            r4 = r22
            byte[] r3 = inputStream2ByteArr(r3, r4)     // Catch:{ Exception -> 0x0138 }
            android.graphics.BitmapFactory$Options r4 = new android.graphics.BitmapFactory$Options     // Catch:{ Exception -> 0x0138 }
            r4.<init>()     // Catch:{ Exception -> 0x0138 }
            r5 = 1
            r4.inJustDecodeBounds = r5     // Catch:{ Exception -> 0x0138 }
            int r5 = r3.length     // Catch:{ Exception -> 0x0138 }
            r6 = 0
            android.graphics.BitmapFactory.decodeByteArray(r3, r6, r5, r4)     // Catch:{ Exception -> 0x0138 }
            r4.inJustDecodeBounds = r6     // Catch:{ Exception -> 0x0138 }
            android.graphics.Bitmap$Config r5 = android.graphics.Bitmap.Config.RGB_565     // Catch:{ Exception -> 0x0138 }
            r4.inPreferredConfig = r5     // Catch:{ Exception -> 0x0138 }
            if (r21 == 0) goto L_0x0026
            android.graphics.Bitmap$CompressFormat r5 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Exception -> 0x0138 }
            goto L_0x0028
        L_0x0026:
            android.graphics.Bitmap$CompressFormat r5 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x0138 }
        L_0x0028:
            int r7 = r4.outWidth     // Catch:{ Exception -> 0x0138 }
            r8 = 75
            r9 = 1080(0x438, float:1.513E-42)
            if (r7 >= r9) goto L_0x0043
            int r10 = r4.outHeight     // Catch:{ Exception -> 0x0138 }
            if (r10 >= r9) goto L_0x0043
            int r7 = r3.length     // Catch:{ Exception -> 0x0138 }
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeByteArray(r3, r6, r7, r4)     // Catch:{ Exception -> 0x0138 }
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0138 }
            r6.<init>()     // Catch:{ Exception -> 0x0138 }
            r4.compress(r5, r8, r6)     // Catch:{ Exception -> 0x0138 }
            goto L_0x00d5
        L_0x0043:
            int r10 = r4.outHeight     // Catch:{ Exception -> 0x0138 }
            r11 = 1149698048(0x44870000, float:1080.0)
            if (r10 < r9) goto L_0x007f
            if (r7 < r9) goto L_0x007f
            android.graphics.Matrix r7 = new android.graphics.Matrix     // Catch:{ Exception -> 0x0138 }
            r7.<init>()     // Catch:{ Exception -> 0x0138 }
            int r8 = r4.outWidth     // Catch:{ Exception -> 0x0138 }
            int r9 = r4.outHeight     // Catch:{ Exception -> 0x0138 }
            if (r8 <= r9) goto L_0x0058
            float r8 = (float) r9     // Catch:{ Exception -> 0x0138 }
            goto L_0x0059
        L_0x0058:
            float r8 = (float) r8     // Catch:{ Exception -> 0x0138 }
        L_0x0059:
            float r11 = r11 / r8
            r7.setScale(r11, r11)     // Catch:{ Exception -> 0x0138 }
            int r8 = r3.length     // Catch:{ Exception -> 0x0138 }
            android.graphics.Bitmap r12 = android.graphics.BitmapFactory.decodeByteArray(r3, r6, r8, r4)     // Catch:{ Exception -> 0x0138 }
            r13 = 0
            r14 = 0
            int r15 = r12.getWidth()     // Catch:{ Exception -> 0x0138 }
            int r16 = r12.getHeight()     // Catch:{ Exception -> 0x0138 }
            r18 = 1
            r17 = r7
            android.graphics.Bitmap r4 = android.graphics.Bitmap.createBitmap(r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x0138 }
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0138 }
            r6.<init>()     // Catch:{ Exception -> 0x0138 }
            r7 = 70
            r4.compress(r5, r7, r6)     // Catch:{ Exception -> 0x0138 }
            goto L_0x00d5
        L_0x007f:
            float r7 = (float) r7     // Catch:{ Exception -> 0x0138 }
            r9 = 1065353216(0x3f800000, float:1.0)
            float r10 = (float) r10     // Catch:{ Exception -> 0x0138 }
            float r10 = r10 * r9
            float r7 = r7 / r10
            r9 = 1073741824(0x40000000, float:2.0)
            int r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r9 > 0) goto L_0x00c6
            double r9 = (double) r7     // Catch:{ Exception -> 0x0138 }
            r12 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            int r7 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1))
            if (r7 >= 0) goto L_0x0094
            goto L_0x00c6
        L_0x0094:
            android.graphics.Matrix r7 = new android.graphics.Matrix     // Catch:{ Exception -> 0x0138 }
            r7.<init>()     // Catch:{ Exception -> 0x0138 }
            int r9 = r4.outWidth     // Catch:{ Exception -> 0x0138 }
            int r10 = r4.outHeight     // Catch:{ Exception -> 0x0138 }
            if (r9 <= r10) goto L_0x00a1
            float r9 = (float) r9     // Catch:{ Exception -> 0x0138 }
            goto L_0x00a2
        L_0x00a1:
            float r9 = (float) r10     // Catch:{ Exception -> 0x0138 }
        L_0x00a2:
            float r11 = r11 / r9
            r7.setScale(r11, r11)     // Catch:{ Exception -> 0x0138 }
            int r9 = r3.length     // Catch:{ Exception -> 0x0138 }
            android.graphics.Bitmap r12 = android.graphics.BitmapFactory.decodeByteArray(r3, r6, r9, r4)     // Catch:{ Exception -> 0x0138 }
            r13 = 0
            r14 = 0
            int r15 = r12.getWidth()     // Catch:{ Exception -> 0x0138 }
            int r16 = r12.getHeight()     // Catch:{ Exception -> 0x0138 }
            r18 = 1
            r17 = r7
            android.graphics.Bitmap r4 = android.graphics.Bitmap.createBitmap(r12, r13, r14, r15, r16, r17, r18)     // Catch:{ Exception -> 0x0138 }
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0138 }
            r6.<init>()     // Catch:{ Exception -> 0x0138 }
            r4.compress(r5, r8, r6)     // Catch:{ Exception -> 0x0138 }
            goto L_0x00d5
        L_0x00c6:
            int r7 = r3.length     // Catch:{ Exception -> 0x0138 }
            android.graphics.Bitmap r4 = android.graphics.BitmapFactory.decodeByteArray(r3, r6, r7, r4)     // Catch:{ Exception -> 0x0138 }
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0138 }
            r6.<init>()     // Catch:{ Exception -> 0x0138 }
            r7 = 73
            r4.compress(r5, r7, r6)     // Catch:{ Exception -> 0x0138 }
        L_0x00d5:
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            r5.<init>(r0)     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            java.io.File r7 = r5.getParentFile()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            if (r7 == 0) goto L_0x00f0
            boolean r8 = r7.exists()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            if (r8 != 0) goto L_0x00f0
            boolean r7 = r7.mkdirs()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            if (r7 != 0) goto L_0x00f0
            r6.close()     // Catch:{ Exception -> 0x00ef }
        L_0x00ef:
            return r2
        L_0x00f0:
            boolean r7 = r5.exists()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            if (r7 != 0) goto L_0x0100
            boolean r7 = r5.createNewFile()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            if (r7 != 0) goto L_0x0100
            r6.close()     // Catch:{ Exception -> 0x00ff }
        L_0x00ff:
            return r2
        L_0x0100:
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            r7.<init>(r5)     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            byte[] r8 = r6.toByteArray()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            r7.write(r8)     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            r7.close()     // Catch:{ Exception -> 0x0134, all -> 0x012f }
            io.dcloud.common.util.ExifInterface r2 = new io.dcloud.common.util.ExifInterface     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            java.io.ByteArrayInputStream r7 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            r7.<init>(r3)     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            r2.<init>((java.io.InputStream) r7)     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            io.dcloud.common.util.ExifInterface r3 = new io.dcloud.common.util.ExifInterface     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            r3.<init>((java.io.File) r5)     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            java.lang.String r2 = r2.getAttribute(r1)     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            r3.setAttribute(r1, r2)     // Catch:{ Exception -> 0x0128, all -> 0x012f }
            r3.saveAttributes()     // Catch:{ Exception -> 0x0128, all -> 0x012f }
        L_0x0128:
            r6.close()     // Catch:{ Exception -> 0x012b }
        L_0x012b:
            r4.recycle()     // Catch:{ Exception -> 0x0138 }
            return r0
        L_0x012f:
            r0 = move-exception
            r6.close()     // Catch:{ Exception -> 0x0133 }
        L_0x0133:
            throw r0     // Catch:{ Exception -> 0x0138 }
        L_0x0134:
            r6.close()     // Catch:{ Exception -> 0x0137 }
        L_0x0137:
            return r2
        L_0x0138:
            r0 = move-exception
            r0.printStackTrace()
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.CompressUtil.compressImage(java.lang.String, java.lang.String, boolean, android.content.Context):java.lang.String");
    }

    private static byte[] inputStream2ByteArr(String str, Context context) throws IOException {
        InputStream inputStream;
        File file = new File(str);
        if (!file.exists()) {
            return null;
        }
        try {
            if (FileUtil.checkPrivatePath(context, str)) {
                inputStream = new FileInputStream(file);
            } else {
                inputStream = FileUtil.getImageFileStream(context, file);
            }
            if (inputStream == null) {
                inputStream = new FileInputStream(file);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    inputStream.close();
                    byteArrayOutputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (Exception unused) {
            return null;
        }
    }
}
