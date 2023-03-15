package io.dcloud.e.c.h;

import android.content.Context;
import io.dcloud.common.util.Base64;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.LoadAppUtils;
import io.dcloud.common.util.Md5Utils;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.e.c.g;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;
import javax.crypto.Cipher;
import org.json.JSONArray;
import org.json.JSONObject;

public class a {
    private static boolean a(Context context, String str) {
        if (g.b() && !PdrUtil.isEmpty(g.a())) {
            try {
                JSONArray jSONArray = new JSONObject(new String(a(Base64.decode2bytes(g.a()), a(io.dcloud.f.a.d())))).getJSONArray("appkeys");
                String b = b(context, str);
                for (int i = 0; i < jSONArray.length(); i++) {
                    String string = jSONArray.getString(i);
                    if (!PdrUtil.isEmpty(string) && PdrUtil.isEquals(string, b)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static String b(Context context, String str) {
        if (PdrUtil.isEmpty(str)) {
            str = a();
        }
        return Md5Utils.md5((str + a(context)) + b(context) + io.dcloud.f.a.a(io.dcloud.f.a.a(), true, 60));
    }

    public static boolean c(Context context) {
        return !g.b() || !a(context, (String) null);
    }

    public static boolean d(Context context, String str) {
        return !g.b() || !a(context, str);
    }

    public static boolean c(Context context, String str) {
        return a(context, str);
    }

    private static String b(Context context) {
        return LoadAppUtils.getAppSignatureSHA1(context);
    }

    private static String a(Context context) {
        return context.getPackageName().toLowerCase(Locale.ENGLISH);
    }

    private static String a() {
        return BaseInfo.sDefaultBootApp;
    }

    private static Key a(String str) {
        try {
            return KeyFactory.getInstance(io.dcloud.f.a.a("WltJ")).generatePublic(new X509EncodedKeySpec(Base64.decode2bytes(str)));
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] a(byte[] bArr, Key key) {
        try {
            Cipher instance = Cipher.getInstance(io.dcloud.f.a.a("WltJJ01LSidYQ0tbOVhpbGxhZm8="));
            instance.init(2, key);
            int blockSize = instance.getBlockSize();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
            int i = 0;
            while (true) {
                int i2 = i * blockSize;
                if (bArr.length - i2 <= 0) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(instance.doFinal(bArr, i2, blockSize));
                i++;
            }
        } catch (Exception unused) {
            return null;
        }
    }
}
