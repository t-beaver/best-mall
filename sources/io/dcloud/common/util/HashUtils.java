package io.dcloud.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String getHash(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(Md5Utils.ALGORITHM);
            instance.update(str.getBytes());
            return toHexString(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String toHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            char[] cArr = HEX_DIGITS;
            sb.append(cArr[(bArr[i] & 240) >>> 4]);
            sb.append(cArr[bArr[i] & 15]);
        }
        return sb.toString();
    }
}
