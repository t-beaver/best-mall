package com.bun.miitmdid.c.j.b;

import android.database.ContentObserver;
import android.os.Handler;

public class c extends ContentObserver {
    private String a;
    private int b;
    private b c;

    public c(b bVar, int i, String str) {
        super((Handler) null);
        this.c = bVar;
        this.b = i;
        this.a = str;
    }

    public native void onChange(boolean z);
}
