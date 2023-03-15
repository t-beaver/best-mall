package io.dcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

class d extends BroadcastReceiver {
    io.dcloud.feature.internal.reflect.BroadcastReceiver a = null;
    IntentFilter b = null;

    d(io.dcloud.feature.internal.reflect.BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        this.a = broadcastReceiver;
        this.b = intentFilter;
    }

    public void onReceive(Context context, Intent intent) {
        IntentFilter intentFilter;
        if (this.a != null && (intentFilter = this.b) != null && intentFilter.hasAction(intent.getAction())) {
            this.a.onReceive(context, intent);
        }
    }
}
