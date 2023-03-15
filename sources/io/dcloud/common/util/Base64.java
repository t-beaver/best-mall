package io.dcloud.common.util;

import io.dcloud.f.a;
import java.io.UnsupportedEncodingException;

public final class Base64 {
    private static final char[] BASE64CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final String CRLF = "\r\n";
    private static final char PAD = '=';

    public static String decode2String(String str) {
        try {
            return new String(decode2bytes(str), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (RuntimeException e2) {
            e2.printStackTrace();
            return "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x00af */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] decode2bytes(java.lang.String r11) {
        /*
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            r1 = 0
            byte[] r11 = r11.getBytes()     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
        L_0x000e:
            int r6 = r11.length     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            if (r3 >= r6) goto L_0x0098
            r6 = 4
            if (r4 >= r6) goto L_0x0077
            byte r7 = r11[r3]     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            int r7 = decodeInt(r7)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r8 = -1
            if (r7 != r8) goto L_0x001f
            goto L_0x0094
        L_0x001f:
            r8 = 3
            r9 = 2
            r10 = -2
            if (r7 != r10) goto L_0x0031
            if (r4 == r9) goto L_0x0031
            if (r4 == r8) goto L_0x0031
            r0.close()     // Catch:{ IOException -> 0x002c }
            goto L_0x0030
        L_0x002c:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0030:
            return r1
        L_0x0031:
            if (r7 != r10) goto L_0x004b
            if (r4 != r9) goto L_0x004b
            int r11 = r5 >> 4
            int r11 = eightbit(r11)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.write(r11)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            byte[] r11 = r0.toByteArray()     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x004a
        L_0x0046:
            r0 = move-exception
            r0.printStackTrace()
        L_0x004a:
            return r11
        L_0x004b:
            if (r7 != r10) goto L_0x006e
            if (r4 != r8) goto L_0x006e
            int r11 = r5 >> 10
            int r11 = eightbit(r11)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.write(r11)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            int r11 = r5 >> 2
            int r11 = eightbit(r11)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.write(r11)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            byte[] r11 = r0.toByteArray()     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.close()     // Catch:{ IOException -> 0x0069 }
            goto L_0x006d
        L_0x0069:
            r0 = move-exception
            r0.printStackTrace()
        L_0x006d:
            return r11
        L_0x006e:
            int r5 = r5 << 6
            int r7 = sixbit(r7)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r5 = r5 | r7
            int r4 = r4 + 1
        L_0x0077:
            if (r4 != r6) goto L_0x0094
            int r4 = r5 >> 16
            int r4 = eightbit(r4)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.write(r4)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            int r4 = r5 >> 8
            int r4 = eightbit(r4)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.write(r4)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            int r4 = eightbit(r5)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.write(r4)     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r4 = 0
            r5 = 0
        L_0x0094:
            int r3 = r3 + 1
            goto L_0x000e
        L_0x0098:
            byte[] r11 = r0.toByteArray()     // Catch:{ Exception -> 0x00af, all -> 0x00a5 }
            r0.close()     // Catch:{ IOException -> 0x00a0 }
            goto L_0x00a4
        L_0x00a0:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00a4:
            return r11
        L_0x00a5:
            r11 = move-exception
            r0.close()     // Catch:{ IOException -> 0x00aa }
            goto L_0x00ae
        L_0x00aa:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00ae:
            throw r11
        L_0x00af:
            r0.close()     // Catch:{ IOException -> 0x00b3 }
            goto L_0x00b7
        L_0x00b3:
            r11 = move-exception
            r11.printStackTrace()
        L_0x00b7:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.Base64.decode2bytes(java.lang.String):byte[]");
    }

    private static int decodeInt(int i) {
        if (i >= 65 && i <= 90) {
            return i - 65;
        }
        if (i >= 97 && i <= 122) {
            return (i - 97) + 26;
        }
        if (i >= 48 && i <= 57) {
            return (i - 48) + 52;
        }
        if (i == 43) {
            return 62;
        }
        if (i == 47) {
            return 63;
        }
        return i == 61 ? -2 : -1;
    }

    public static String decodeString(String str, boolean z, int i) {
        return a.a(str, z, i);
    }

    private static int eightbit(int i) {
        return i & 255;
    }

    public static String encode(String str) {
        try {
            return encode(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String encodeString(String str, boolean z, int i) {
        return a.b(str, z, i);
    }

    private static int sixbit(int i) {
        return i & 63;
    }

    public static String encode(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i += 3) {
            if (i % 57 == 0 && i != 0) {
                stringBuffer.append(CRLF);
            }
            int i2 = i + 1;
            if (bArr.length <= i2) {
                int eightbit = eightbit(bArr[i]) << 16;
                char[] cArr = BASE64CHARS;
                stringBuffer.append(cArr[sixbit(eightbit >> 18)]);
                stringBuffer.append(cArr[sixbit(eightbit >> 12)]);
                stringBuffer.append(PAD);
                stringBuffer.append(PAD);
            } else {
                int i3 = i + 2;
                if (bArr.length <= i3) {
                    int eightbit2 = (eightbit(bArr[i]) << 16) | (eightbit(bArr[i2]) << 8);
                    char[] cArr2 = BASE64CHARS;
                    stringBuffer.append(cArr2[sixbit(eightbit2 >> 18)]);
                    stringBuffer.append(cArr2[sixbit(eightbit2 >> 12)]);
                    stringBuffer.append(cArr2[sixbit(eightbit2 >> 6)]);
                    stringBuffer.append(PAD);
                } else {
                    int eightbit3 = (eightbit(bArr[i]) << 16) | (eightbit(bArr[i2]) << 8) | eightbit(bArr[i3]);
                    char[] cArr3 = BASE64CHARS;
                    stringBuffer.append(cArr3[sixbit(eightbit3 >> 18)]);
                    stringBuffer.append(cArr3[sixbit(eightbit3 >> 12)]);
                    stringBuffer.append(cArr3[sixbit(eightbit3 >> 6)]);
                    stringBuffer.append(cArr3[sixbit(eightbit3)]);
                }
            }
        }
        return stringBuffer.toString();
    }
}
