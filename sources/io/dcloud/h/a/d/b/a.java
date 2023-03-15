package io.dcloud.h.a.d.b;

import com.taobao.weex.performance.WXInstanceApm;
import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class a {
    private static IvParameterSpec a(String str) {
        byte[] bArr;
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bArr = null;
        }
        return new IvParameterSpec(bArr);
    }

    public static byte[] b(byte[] bArr, String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(1, secretKeySpec, a(str2));
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] a(byte[] bArr, String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, secretKeySpec, a(str2));
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
