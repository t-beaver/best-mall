package io.dcloud.e.c;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import io.dcloud.common.DHInterface.DCI;
import io.dcloud.common.DHInterface.IConfusionMgr;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.PdrUtil;
import java.util.Map;
import org.json.JSONObject;

public class b implements IConfusionMgr {
    private static String a;
    private static String b;
    private static String c;
    private static String d;
    private static String e;
    private static String f;
    private static IConfusionMgr g;
    private int h = 3;
    private DCI i;
    final String j = "##";
    private boolean k = false;

    private b() {
        Object invokeMethod = PlatformUtil.invokeMethod(decodeString(b(), true, this.h), "getInstance", (Object) null);
        if (invokeMethod != null && (invokeMethod instanceof DCI)) {
            this.i = (DCI) invokeMethod;
        }
    }

    private String a() {
        return "amwtZ2BvbHZnLWVmYnd2cWYtYGUtYEVmYnd2cWZKbnNvKjZhM2Q4OGZhLTRiYTAtNDc5Zi05NDIyLWU1YWFiZTE1ODk3YjY3";
    }

    private String a(String str) throws Exception {
        byte[] bytes = str.getBytes("GBK");
        for (int i2 = 0; i2 < bytes.length; i2++) {
            bytes[i2] = (byte) (bytes[i2] ^ 8);
        }
        return new String(bytes, "GBK");
    }

    private String b() {
        return "amwtZ2BvbHZnLWBsbm5sbS1gcC1HTyo2YTNkODhmYS00YmEwLTQ3OWYtOTQyMi1lNWFhYmUxNTg5N2I2Nw==";
    }

    public static IConfusionMgr c() {
        if (g == null) {
            g = new b();
        }
        return g;
    }

    public String decodeString(String str) {
        try {
            return a(new String(Base64.decode(str, 2)));
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public String decryptStr(String str) {
        try {
            return a(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public String encodeString(String str, boolean z, int i2) {
        if (!z) {
            return str;
        }
        try {
            if (PdrUtil.isEmpty(str)) {
                return str;
            }
            byte[] bytes = str.getBytes("utf-8");
            for (int i3 = 0; i3 < bytes.length; i3++) {
                bytes[i3] = (byte) (bytes[i3] ^ i2);
            }
            String str2 = Base64.encodeToString(bytes, 2) + "*6a3d88fa-4ba0-479f-9422-e5aabe15897b" + (i2 + 64);
            return "##" + Base64.encodeToString(str2.getBytes(), 2);
        } catch (Exception unused) {
            return "";
        }
    }

    public String getCSJClassName() {
        if (TextUtils.isEmpty(e)) {
            try {
                e = a("kge&jq|mlifkm&{lc&gxmfil{lc&\\\\Il[lc");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return e;
    }

    public Map<String, String> getData(String str) {
        Object invokeMethod = PlatformUtil.invokeMethod(decodeString(a(), true, this.h), "getData", (Object) null, new Class[]{String.class}, new Object[]{str});
        if (invokeMethod == null || !(invokeMethod instanceof Map)) {
            return null;
        }
        return (Map) invokeMethod;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001f, code lost:
        r0 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(decodeString(a(), true, r8.h), decodeString("b218TWZrenF4fGFnZkFmeH18W3x6bWll"), (java.lang.Object) null, new java.lang.Class[]{java.lang.String.class, io.dcloud.common.DHInterface.IApp.class}, new java.lang.Object[]{r9, r10});
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.InputStream getEncryptionInputStream(java.lang.String r9, io.dcloud.common.DHInterface.IApp r10) {
        /*
            r8 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            r1 = 0
            if (r0 != 0) goto L_0x00c2
            if (r10 == 0) goto L_0x00c2
            boolean r0 = android.webkit.URLUtil.isNetworkUrl(r9)
            if (r0 == 0) goto L_0x0011
            goto L_0x00c2
        L_0x0011:
            java.lang.String r0 = "use_v3_encryption"
            java.lang.String r0 = r10.obtainConfigProperty(r0)
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
            r2 = 0
            r3 = 1
            if (r0 != 0) goto L_0x004d
            java.lang.String r0 = r8.a()
            int r4 = r8.h
            java.lang.String r0 = r8.decodeString(r0, r3, r4)
            java.lang.String r4 = "b218TWZrenF4fGFnZkFmeH18W3x6bWll"
            java.lang.String r4 = r8.decodeString(r4)
            r5 = 2
            java.lang.Class[] r6 = new java.lang.Class[r5]
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            r6[r2] = r7
            java.lang.Class<io.dcloud.common.DHInterface.IApp> r7 = io.dcloud.common.DHInterface.IApp.class
            r6[r3] = r7
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r5[r2] = r9
            r5[r3] = r10
            java.lang.Object r0 = io.dcloud.common.adapter.util.PlatformUtil.invokeMethod(r0, r4, r1, r6, r5)
            if (r0 == 0) goto L_0x004d
            boolean r4 = r0 instanceof java.io.InputStream
            if (r4 == 0) goto L_0x004d
            java.io.InputStream r0 = (java.io.InputStream) r0
            goto L_0x004e
        L_0x004d:
            r0 = r1
        L_0x004e:
            if (r0 != 0) goto L_0x00c1
            byte r4 = r10.obtainRunningAppMode()
            if (r4 != r3) goto L_0x0057
            goto L_0x0058
        L_0x0057:
            r3 = 0
        L_0x0058:
            java.lang.String r4 = "file://"
            boolean r4 = r9.startsWith(r4)
            if (r4 == 0) goto L_0x0073
            android.net.Uri r4 = android.net.Uri.parse(r9)     // Catch:{ Exception -> 0x0071 }
            if (r4 == 0) goto L_0x0078
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0071 }
            java.lang.String r4 = r4.getPath()     // Catch:{ Exception -> 0x0071 }
            r5.<init>(r4)     // Catch:{ Exception -> 0x0071 }
            r1 = r5
            goto L_0x0078
        L_0x0071:
            goto L_0x0078
        L_0x0073:
            java.io.File r1 = new java.io.File
            r1.<init>(r9)
        L_0x0078:
            if (r1 != 0) goto L_0x007c
            if (r3 == 0) goto L_0x00c1
        L_0x007c:
            if (r3 == 0) goto L_0x0085
            java.io.InputStream r0 = io.dcloud.common.adapter.util.PlatformUtil.getInputStream(r9, r2)     // Catch:{ Exception -> 0x0083 }
            goto L_0x0095
        L_0x0083:
            r9 = move-exception
            goto L_0x00be
        L_0x0085:
            if (r1 == 0) goto L_0x0095
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0083 }
            if (r2 == 0) goto L_0x0095
            android.app.Activity r2 = r10.getActivity()     // Catch:{ Exception -> 0x0083 }
            java.io.InputStream r0 = io.dcloud.common.util.FileUtil.getFileInputStream((android.content.Context) r2, (java.io.File) r1)     // Catch:{ Exception -> 0x0083 }
        L_0x0095:
            if (r0 == 0) goto L_0x00c1
            boolean r1 = r8.isV3Encryption()     // Catch:{ Exception -> 0x0083 }
            if (r1 == 0) goto L_0x00c1
            io.dcloud.common.DHInterface.DCI r1 = r8.i     // Catch:{ Exception -> 0x0083 }
            java.lang.String r2 = r10.obtainAppId()     // Catch:{ Exception -> 0x0083 }
            boolean r1 = r1.cf(r2, r9)     // Catch:{ Exception -> 0x0083 }
            if (r1 == 0) goto L_0x00c1
            io.dcloud.common.DHInterface.DCI r1 = r8.i     // Catch:{ Exception -> 0x0083 }
            java.lang.String r10 = r10.obtainAppId()     // Catch:{ Exception -> 0x0083 }
            byte[] r2 = io.dcloud.common.util.IOUtil.getBytes(r0)     // Catch:{ Exception -> 0x0083 }
            byte[] r9 = r1.lf(r10, r9, r2)     // Catch:{ Exception -> 0x0083 }
            if (r9 == 0) goto L_0x00c1
            java.io.InputStream r0 = io.dcloud.common.util.IOUtil.byte2InputStream(r9)     // Catch:{ Exception -> 0x0083 }
            goto L_0x00c1
        L_0x00be:
            r9.printStackTrace()
        L_0x00c1:
            return r0
        L_0x00c2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.b.getEncryptionInputStream(java.lang.String, io.dcloud.common.DHInterface.IApp):java.io.InputStream");
    }

    public String getGDTClassName() {
        if (TextUtils.isEmpty(d)) {
            try {
                d = a("kge&yy&m&il{&{xdi{`&[xdi{`IL");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return d;
    }

    public String getKSClassName() {
        if (TextUtils.isEmpty(f)) {
            try {
                f = a("kge&cil&{lc&ixa&C{Il[LC");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return f;
    }

    public String getS5DS() {
        return "UWV/BnpHVVhMahB0EU1XA15hAEFOAWlGVHBkcgluSF0HFhlQZx15Yhhjb3xCHgRfWxV+cQhPS1ICFxRzdkUfeyo2YTNkODhmYS00YmEwLTQ3OWYtOTQyMi1lNWFhYmUxNTg5N2IxMjQ=";
    }

    public String getSIV() {
        if (TextUtils.isEmpty(b)) {
            try {
                b = a("@\\ED=XD][Z]F\\AEM");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return b;
    }

    public String getSK() {
        if (TextUtils.isEmpty(a)) {
            try {
                a = a("LKdg}l.:\"8V9+>88");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return a;
    }

    public String getSPK() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw4a5Rcq2ZWsKUogf5rc6Q+RqEZiFS6hq6FmNd5q6ZVwIRedk7HV5B6c7WCvLcEYhEe+dnF4XqhCiZe31nvp4FpmEvIDJrzh20qEwHAGcEUijF+0iXOWOskLpDqtXuk/anskpyJ/KstPbreHKVSE4DHqxgGf0ZUn7Z4BZynIGfAK/zizsIcRQwFccBHIsgi0AT+HBdXnxGWBK9LbeSnCzotqLTPEBrV9LhZsUGcY4B+HB1qTOS1PF2sv+/UDvmgWtM9PX3FrzuB8uy8gR+vf0XYJadaL6x0NlRRcIpE5R84oWuaarSfoNr3prFbh+EkYctODHmoiVIvqdfUspqRdZaQIDAQAB";
    }

    public String getSQK() {
        if (TextUtils.isEmpty(c)) {
            try {
                c = a("Y.:]\"8MV9[+>88\\?");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return c;
    }

    public String handleEncryption(Context context, byte[] bArr) {
        Object invokeMethod = PlatformUtil.invokeMethod(decodeString(a(), true, this.h), decodeString("YGlmbGRtTWZrenF4fGFnZg=="), (Object) null, new Class[]{Context.class, byte[].class}, new Object[]{context, bArr});
        if (invokeMethod != null) {
            return String.valueOf(invokeMethod);
        }
        return null;
    }

    public boolean isV3Encryption() {
        return this.k;
    }

    public void recordEncryptionResources(String str, JSONObject jSONObject) {
        PlatformUtil.invokeMethod(decodeString(a(), true, this.h), decodeString("em1rZ3psTWZrenF4fGFnZlpte2d9emttew=="), (Object) null, new Class[]{String.class, JSONObject.class}, new Object[]{str, jSONObject});
    }

    public boolean recordEncryptionV3Resources(String str, String str2) {
        if (this.i != null && !PdrUtil.isEmpty(str2)) {
            this.k = this.i.dt(str, str2.getBytes());
        }
        return this.k;
    }

    public void removeData(String str) {
        PlatformUtil.invokeMethod(decodeString(a(), true, this.h), "removeData", (Object) null, new Class[]{String.class}, new Object[]{str});
    }

    public String decryptStr(String str, byte b2) {
        try {
            return a(str, b2);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private String a(String str, byte b2) throws Exception {
        byte[] bytes = str.getBytes("GBK");
        for (int i2 = 0; i2 < bytes.length; i2++) {
            bytes[i2] = (byte) (bytes[i2] ^ b2);
        }
        return new String(bytes, "GBK");
    }

    public String decodeString(String str, boolean z, int i2) {
        boolean z2;
        if (PdrUtil.isEmpty(str)) {
            return str;
        }
        if (str.startsWith("##")) {
            str = str.substring(2);
            z2 = true;
        } else {
            z2 = false;
        }
        byte[] decode = Base64.decode(str, 2);
        if (z) {
            try {
                String str2 = new String(decode);
                if (str2.contains("*6a3d88fa-4ba0-479f-9422-e5aabe15897b")) {
                    int indexOf = str2.indexOf("*6a3d88fa-4ba0-479f-9422-e5aabe15897b");
                    String substring = str2.substring(0, indexOf);
                    if (Integer.valueOf(str2.substring(indexOf + 37)).intValue() - 64 == i2) {
                        if (z2) {
                            decode = Base64.decode(substring, 2);
                        } else {
                            decode = substring.getBytes("utf-8");
                        }
                        for (int i3 = 0; i3 < decode.length; i3++) {
                            decode[i3] = (byte) (decode[i3] ^ i2);
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
        return new String(decode, "utf-8");
    }
}
