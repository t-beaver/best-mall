package com.taobao.weex.base;

import android.os.Handler;
import android.os.Message;
import java.io.Serializable;
import java.lang.reflect.Method;

public class SystemMessageHandler extends Handler implements Serializable {
    private static final int SCHEDULED_WORK = 1;
    private static final String TAG = "SystemMessageHandler";
    private Method mMessageMethodSetAsynchronous;
    private long mMessagePumpDelegateNative;

    private native void nativeRunWork(long j);

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private SystemMessageHandler(long r4, boolean r6) {
        /*
            r3 = this;
            java.lang.String r0 = "SystemMessageHandler"
            if (r6 == 0) goto L_0x0009
            android.os.Looper r6 = android.os.Looper.getMainLooper()
            goto L_0x000d
        L_0x0009:
            android.os.Looper r6 = android.os.Looper.myLooper()
        L_0x000d:
            r3.<init>(r6)
            r1 = 0
            r3.mMessagePumpDelegateNative = r1
            r3.mMessagePumpDelegateNative = r4
            java.lang.String r4 = "android.os.Message"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException -> 0x0059, NoSuchMethodException -> 0x0043, RuntimeException -> 0x002d }
            java.lang.String r5 = "setAsynchronous"
            r6 = 1
            java.lang.Class[] r6 = new java.lang.Class[r6]     // Catch:{ ClassNotFoundException -> 0x0059, NoSuchMethodException -> 0x0043, RuntimeException -> 0x002d }
            r1 = 0
            java.lang.Class r2 = java.lang.Boolean.TYPE     // Catch:{ ClassNotFoundException -> 0x0059, NoSuchMethodException -> 0x0043, RuntimeException -> 0x002d }
            r6[r1] = r2     // Catch:{ ClassNotFoundException -> 0x0059, NoSuchMethodException -> 0x0043, RuntimeException -> 0x002d }
            java.lang.reflect.Method r4 = r4.getMethod(r5, r6)     // Catch:{ ClassNotFoundException -> 0x0059, NoSuchMethodException -> 0x0043, RuntimeException -> 0x002d }
            r3.mMessageMethodSetAsynchronous = r4     // Catch:{ ClassNotFoundException -> 0x0059, NoSuchMethodException -> 0x0043, RuntimeException -> 0x002d }
            goto L_0x006e
        L_0x002d:
            r4 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Exception while loading Message.setAsynchronous method: "
            r5.append(r6)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            android.util.Log.e(r0, r4)
            goto L_0x006e
        L_0x0043:
            r4 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to load Message.setAsynchronous method:"
            r5.append(r6)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            android.util.Log.e(r0, r4)
            goto L_0x006e
        L_0x0059:
            r4 = move-exception
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to find android.os.Message class:"
            r5.append(r6)
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            android.util.Log.e(r0, r4)
        L_0x006e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.base.SystemMessageHandler.<init>(long, boolean):void");
    }

    public static SystemMessageHandler create(long j, boolean z) {
        return new SystemMessageHandler(j, z);
    }

    private void scheduleWork() {
        sendMessage(obtainAsyncMessage(1));
    }

    private void scheduleDelayedWork(long j) {
        sendMessageDelayed(obtainAsyncMessage(1), j);
    }

    private void stop() {
        removeMessages(1);
    }

    private Message obtainAsyncMessage(int i) {
        Message obtain = Message.obtain();
        obtain.what = i;
        return obtain;
    }

    public void handleMessage(Message message) {
        nativeRunWork(this.mMessagePumpDelegateNative);
    }
}
