package io.dcloud.h.c.c.a.e;

import android.text.TextUtils;
import android.util.Base64;
import java.nio.charset.StandardCharsets;

public class d {
    public static byte[] a(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ 8);
        }
        return bytes;
    }

    public static String b(String str) {
        return a(f(str));
    }

    public static String c(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return a(Base64.decode(str, 2));
    }

    public static String d(String str) {
        try {
            byte[] bytes = str.getBytes("GBK");
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (bytes[i] ^ 8);
            }
            return new String(bytes, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String e(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return Base64.encodeToString(a(str), 2);
    }

    public static byte[] f(String str) {
        if (str == null || str.length() < 2) {
            return new byte[0];
        }
        String lowerCase = str.toLowerCase();
        int length = lowerCase.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (Integer.parseInt(lowerCase.substring(i2, i2 + 2), 16) & 255);
        }
        return bArr;
    }

    public static String a(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ 8);
        }
        return new String(bArr, StandardCharsets.UTF_8);
    }
}
