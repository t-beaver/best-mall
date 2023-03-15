package com.taobao.weex.ui;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class WXRenderHandler extends Handler {
    public WXRenderHandler() {
        super(Looper.getMainLooper());
    }

    public final boolean post(String str, Runnable runnable) {
        Message obtain = Message.obtain(this, runnable);
        obtain.what = str.hashCode();
        return sendMessageDelayed(obtain, 0);
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
    }
}
