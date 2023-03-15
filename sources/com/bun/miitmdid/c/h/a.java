package com.bun.miitmdid.c.h;

import android.content.Context;
import android.net.Uri;

public class a {
    private static Uri a = Uri.parse("content://cn.nubia.identity/identity");

    public static native String a(Context context);

    public static native String a(Context context, String str);

    public static native String b(Context context, String str);

    public static native boolean b(Context context);
}
