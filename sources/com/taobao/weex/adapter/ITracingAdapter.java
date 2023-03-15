package com.taobao.weex.adapter;

import com.taobao.weex.tracing.WXTracing;

public interface ITracingAdapter {
    void disable();

    void enable();

    void submitTracingEvent(WXTracing.TraceEvent traceEvent);
}
