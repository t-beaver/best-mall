package com.taobao.weex.tracing;

import android.os.Looper;
import android.util.SparseArray;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.ITracingAdapter;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WXTracing {
    private static final AtomicInteger sIdGenerator = new AtomicInteger(0);

    public static class TraceInfo {
        public long domQueueTime;
        public long domThreadNanos;
        public long domThreadStart = -1;
        public int rootEventId;
        public long uiQueueTime;
        public long uiThreadNanos;
        public long uiThreadStart = -1;
    }

    public static int nextId() {
        return sIdGenerator.getAndIncrement();
    }

    public static boolean isAvailable() {
        return WXEnvironment.isApkDebugable();
    }

    public static synchronized void submit(TraceEvent traceEvent) {
        synchronized (WXTracing.class) {
            ITracingAdapter tracingAdapter = WXSDKManager.getInstance().getTracingAdapter();
            if (tracingAdapter != null) {
                tracingAdapter.submitTracingEvent(traceEvent);
            }
        }
    }

    public static class TraceEvent {
        public String classname;
        public double duration;
        public Map<String, Object> extParams;
        public boolean firstScreenFinish;
        public String fname;
        public String iid;
        public boolean isSegment;
        public String name;
        public int parentId = -1;
        public String parentRef;
        public double parseJsonTime;
        public String payload;
        public String ph;
        public String ref;
        public SparseArray<TraceEvent> subEvents;
        private boolean submitted;
        public String tname = WXTracing.currentThreadName();
        public int traceId = WXTracing.nextId();
        public long ts = System.currentTimeMillis();

        public void submit() {
            if (!this.submitted) {
                this.submitted = true;
                WXTracing.submit(this);
                return;
            }
            WXLogUtils.w("WXTracing", "Event " + this.traceId + " has been submitted.");
        }
    }

    public static String currentThreadName() {
        String name = Thread.currentThread().getName();
        if ("WeexJSBridgeThread".equals(name)) {
            return "JSThread";
        }
        if ("WeeXDomThread".equals(name)) {
            return "DOMThread";
        }
        return Looper.getMainLooper() == Looper.myLooper() ? "UIThread" : name;
    }

    public static TraceEvent newEvent(String str, String str2, int i) {
        TraceEvent traceEvent = new TraceEvent();
        traceEvent.fname = str;
        traceEvent.iid = str2;
        traceEvent.traceId = nextId();
        traceEvent.parentId = i;
        return traceEvent;
    }
}
