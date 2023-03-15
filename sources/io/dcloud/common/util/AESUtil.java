package io.dcloud.common.util;

import io.dcloud.f.a;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static Cipher createCipher(int i, byte[] bArr, String str, byte[] bArr2) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
        Cipher instance = Cipher.getInstance(str);
        if (bArr2 == null) {
            bArr2 = new byte[instance.getBlockSize()];
        }
        instance.init(i, secretKeySpec, new IvParameterSpec(bArr2));
        return instance;
    }

    public static String decrypt(String str, String str2, byte[] bArr) {
        try {
            return new String(decrypt(str.getBytes(), str2.getBytes(), bArr));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encrypt(String str, String str2, String str3) {
        return encrypt(str, str2, str3.getBytes());
    }

    public static String getDefaultTransformation() {
        return a.b("IM['KJK'XCK[=Xillafo");
    }

    public static byte[] encrypt(String str, String str2, byte[] bArr) {
        try {
            return encrypt(str.getBytes(), bArr, getDefaultTransformation(), str2 != null ? str2.getBytes() : null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encrypt(byte[] bArr, byte[] bArr2, String str, byte[] bArr3) throws Exception {
        return createCipher(1, bArr, str, bArr3).doFinal(bArr2);
    }

    private static byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        return createCipher(2, bArr, getDefaultTransformation(), bArr2).doFinal(bArr3);
    }
}
