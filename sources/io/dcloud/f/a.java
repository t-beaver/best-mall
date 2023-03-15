package io.dcloud.f;

import android.content.Context;
import io.dcloud.common.DHInterface.IConfusionMgr;
import io.dcloud.common.DHInterface.INativeAppInfo;

public class a {
    private static IConfusionMgr a;

    public static void a(INativeAppInfo iNativeAppInfo) {
        if (iNativeAppInfo != null) {
            a = iNativeAppInfo.getCofusionMgr();
        }
    }

    public static String b(String str, boolean z, int i) {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.encodeString(str, z, i);
        }
        return null;
    }

    public static String c() {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.getSK();
        }
        return null;
    }

    public static String d() {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.getSPK();
        }
        return null;
    }

    public static String e() {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.getSQK();
        }
        return null;
    }

    public static String a(String str, boolean z, int i) {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.decodeString(str, z, i);
        }
        return null;
    }

    public static String b(String str) {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.decryptStr(str);
        }
        return null;
    }

    public static String a(Context context, byte[] bArr) {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.handleEncryption(context, bArr);
        }
        return null;
    }

    public static String b() {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.getSIV();
        }
        return null;
    }

    public static String a(String str) {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.decodeString(str);
        }
        return null;
    }

    public static String a() {
        IConfusionMgr iConfusionMgr = a;
        if (iConfusionMgr != null) {
            return iConfusionMgr.getS5DS();
        }
        return null;
    }
}
