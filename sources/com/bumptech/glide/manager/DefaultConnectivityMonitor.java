package com.bumptech.glide.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.util.Preconditions;
import io.dcloud.common.util.net.NetCheckReceiver;

final class DefaultConnectivityMonitor implements ConnectivityMonitor {
    private static final String TAG = "ConnectivityMonitor";
    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean z = DefaultConnectivityMonitor.this.isConnected;
            DefaultConnectivityMonitor defaultConnectivityMonitor = DefaultConnectivityMonitor.this;
            defaultConnectivityMonitor.isConnected = defaultConnectivityMonitor.isConnected(context);
            if (z != DefaultConnectivityMonitor.this.isConnected) {
                if (Log.isLoggable(DefaultConnectivityMonitor.TAG, 3)) {
                    Log.d(DefaultConnectivityMonitor.TAG, "connectivity changed, isConnected: " + DefaultConnectivityMonitor.this.isConnected);
                }
                DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
            }
        }
    };
    private final Context context;
    boolean isConnected;
    private boolean isRegistered;
    final ConnectivityMonitor.ConnectivityListener listener;

    public void onDestroy() {
    }

    DefaultConnectivityMonitor(Context context2, ConnectivityMonitor.ConnectivityListener connectivityListener) {
        this.context = context2.getApplicationContext();
        this.listener = connectivityListener;
    }

    private void register() {
        if (!this.isRegistered) {
            this.isConnected = isConnected(this.context);
            try {
                this.context.registerReceiver(this.connectivityReceiver, new IntentFilter(NetCheckReceiver.netACTION));
                this.isRegistered = true;
            } catch (SecurityException e) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Failed to register", e);
                }
            }
        }
    }

    private void unregister() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.connectivityReceiver);
            this.isRegistered = false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected(Context context2) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) Preconditions.checkNotNull((ConnectivityManager) context2.getSystemService("connectivity"))).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                return false;
            }
            return true;
        } catch (RuntimeException e) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Failed to determine connectivity status when connectivity changed", e);
            }
            return true;
        }
    }

    public void onStart() {
        register();
    }

    public void onStop() {
        unregister();
    }
}
