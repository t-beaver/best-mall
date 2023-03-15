package io.dcloud.feature.internal.reflect;

import android.content.Context;
import android.content.Intent;

public interface BroadcastReceiver {
    void onReceive(Context context, Intent intent);
}
