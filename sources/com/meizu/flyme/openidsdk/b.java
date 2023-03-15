package com.meizu.flyme.openidsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.Cursor;

public class b {
    public static volatile b g;
    public static boolean h;
    public OpenId a = new OpenId("udid");
    public OpenId b = new OpenId("oaid");
    public OpenId c = new OpenId("aaid");
    public OpenId d = new OpenId("vaid");
    public Boolean e;
    public BroadcastReceiver f;

    public static native ValueData a(Cursor cursor);

    public static final native b a();

    public static native void b(String str);

    public native OpenId a(String str);

    public final native String a(Context context, OpenId openId);

    public final native synchronized void a(Context context);

    public final native boolean a(Context context, boolean z);
}
