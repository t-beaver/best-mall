package io.dcloud.e.f;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.Md5Utils;
import io.dcloud.e.f.a;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class b {
    private static b a;
    Context b;
    private a.c c;
    String d;

    private b(Context context, String str) {
        this.b = context;
        this.d = str;
        if (this.c == null) {
            try {
                String a2 = a(context);
                SharedPreferences orCreateBundle = SP.getOrCreateBundle(context, str);
                String string = orCreateBundle.getString(a2, (String) null);
                if (string == null) {
                    this.c = a.c();
                    orCreateBundle.edit().putString(a2, this.c.toString()).commit();
                    return;
                }
                this.c = a.a(string);
            } catch (GeneralSecurityException unused) {
            }
        }
    }

    public static b a(Context context, String str) {
        if (a == null) {
            synchronized (b.class) {
                if (a == null) {
                    a = new b(context, str);
                }
            }
        }
        return a;
    }

    private String c(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return a.a(str, this.c).toString();
        } catch (UnsupportedEncodingException | GeneralSecurityException unused) {
            return null;
        }
    }

    public static String d(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            instance.update(bytes, 0, bytes.length);
            return Base64.encodeToString(instance.digest(), 2);
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|(1:3)|4|(2:6|7)|8|9|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0028 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void b(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            android.content.Context r0 = r3.b     // Catch:{ Exception -> 0x0046 }
            java.lang.String r1 = r3.d     // Catch:{ Exception -> 0x0046 }
            java.lang.String r1 = io.dcloud.common.util.Md5Utils.md5((java.lang.String) r1)     // Catch:{ Exception -> 0x0046 }
            r2 = 0
            java.io.File r0 = r0.getDir(r1, r2)     // Catch:{ Exception -> 0x0046 }
            boolean r1 = r0.exists()     // Catch:{ Exception -> 0x0046 }
            if (r1 != 0) goto L_0x0016
            r0.mkdirs()     // Catch:{ Exception -> 0x0046 }
        L_0x0016:
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0046 }
            java.lang.String r4 = io.dcloud.common.util.Md5Utils.md5((java.lang.String) r4)     // Catch:{ Exception -> 0x0046 }
            r1.<init>(r0, r4)     // Catch:{ Exception -> 0x0046 }
            boolean r4 = r1.exists()     // Catch:{ Exception -> 0x0046 }
            if (r4 != 0) goto L_0x0028
            r0.createNewFile()     // Catch:{ IOException -> 0x0028 }
        L_0x0028:
            java.io.FileWriter r4 = new java.io.FileWriter     // Catch:{ IOException -> 0x004a }
            r4.<init>(r1)     // Catch:{ IOException -> 0x004a }
            java.io.BufferedWriter r0 = new java.io.BufferedWriter     // Catch:{ IOException -> 0x004a }
            r0.<init>(r4)     // Catch:{ IOException -> 0x004a }
            java.lang.String r5 = r3.c(r5)     // Catch:{ IOException -> 0x004a }
            r0.write(r5)     // Catch:{ IOException -> 0x004a }
            r0.flush()     // Catch:{ IOException -> 0x004a }
            r0.close()     // Catch:{ IOException -> 0x004a }
            r4.flush()     // Catch:{ IOException -> 0x004a }
            r4.close()     // Catch:{ IOException -> 0x004a }
            goto L_0x004a
        L_0x0046:
            r4 = move-exception
            r4.printStackTrace()
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.f.b.b(java.lang.String, java.lang.String):void");
    }

    private String a(Context context) throws GeneralSecurityException {
        return d(a.a(context.getPackageName(), context.getPackageName().getBytes(), 1000).toString());
    }

    public boolean a(String str) {
        return new File(this.b.getDir(Md5Utils.md5(this.d), 0), Md5Utils.md5(str)).exists();
    }

    public String a(String str, String str2) {
        File dir = this.b.getDir(Md5Utils.md5(this.d), 0);
        File file = new File(dir.getAbsolutePath() + "/" + Md5Utils.md5(str));
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                    } else {
                        bufferedReader.close();
                        fileReader.close();
                        return b(sb.toString());
                    }
                }
            } catch (IOException unused) {
            }
        }
        return str2;
    }

    private String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return a.b(new a.C0035a(str), this.c);
        } catch (UnsupportedEncodingException | GeneralSecurityException unused) {
            return null;
        }
    }
}
