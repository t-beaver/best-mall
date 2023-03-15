package io.dcloud.h.c.c.a.e;

import io.dcloud.common.util.Md5Utils;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class c {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(Md5Utils.ALGORITHM);
            instance.update(str.getBytes("UTF-8"));
            return a(instance.digest());
        } catch (UnsupportedEncodingException | Exception | NoSuchAlgorithmException unused) {
            return str;
        }
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            char[] cArr = a;
            sb.append(cArr[(b & 240) >> 4]);
            sb.append(cArr[b & 15]);
        }
        return sb.toString();
    }
}
