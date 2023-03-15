package com.huawei.android.hms.pps;

import android.content.Context;

public class AdvertisingIdClient {

    public static final class Info {
        private final String advertisingId;
        private final boolean limitAdTrackingEnabled;

        Info(String str, boolean z) {
            this.advertisingId = str;
            this.limitAdTrackingEnabled = z;
        }

        public final native String getId();

        public final native boolean isLimitAdTrackingEnabled();
    }

    public static native Info getAdvertisingIdInfo(Context context);

    private static native String getTag();
}
