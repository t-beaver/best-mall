package com.bun.lib;

import android.content.Context;
import android.content.pm.PackageInfo;

public class sysParamters {
    private static volatile sysParamters d;
    private String a;
    private String b;
    private String c;
    private String sdk_version = "10012";
    private String sdk_vname = "1.0.12";

    private sysParamters() {
    }

    private static native PackageInfo a(Context context, String str);

    public static native String a(Context context);

    public static native String a(String str, String str2);

    public static native String e();

    public static native sysParamters f();

    public static native String g();

    private static native String h();

    public native String a();

    public native String b();

    public native String c();

    public native String d();
}
