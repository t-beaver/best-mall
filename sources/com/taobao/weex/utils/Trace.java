package com.taobao.weex.utils;

import android.util.Log;

public class Trace {
    private static final String TAG = "Weex_Trace";
    private static final boolean sEnabled = false;
    private static final AbstractTrace sTrace;

    private static abstract class AbstractTrace {
        /* access modifiers changed from: package-private */
        public abstract void beginSection(String str);

        /* access modifiers changed from: package-private */
        public abstract void endSection();

        private AbstractTrace() {
        }
    }

    static {
        if (0 != 1 || !OsVersion.isAtLeastJB_MR2()) {
            sTrace = new TraceDummy();
        } else {
            sTrace = new TraceJBMR2();
        }
    }

    public static final boolean getTraceEnabled() {
        return sEnabled;
    }

    public static void beginSection(String str) {
        Log.i(TAG, "beginSection() " + str);
        sTrace.beginSection(str);
    }

    public static void endSection() {
        sTrace.endSection();
        Log.i(TAG, "endSection()");
    }

    private static final class TraceJBMR2 extends AbstractTrace {
        private TraceJBMR2() {
            super();
        }

        /* access modifiers changed from: package-private */
        public void beginSection(String str) {
            android.os.Trace.beginSection(str);
        }

        /* access modifiers changed from: package-private */
        public void endSection() {
            android.os.Trace.endSection();
        }
    }

    private static final class TraceDummy extends AbstractTrace {
        /* access modifiers changed from: package-private */
        public void beginSection(String str) {
        }

        /* access modifiers changed from: package-private */
        public void endSection() {
        }

        private TraceDummy() {
            super();
        }
    }
}
