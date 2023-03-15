package com.taobao.weex.dom;

import androidx.collection.ArrayMap;
import io.dcloud.feature.uniapp.dom.AbsEvent;
import java.util.List;

public class WXEvent extends AbsEvent {
    public WXEvent clone() {
        WXEvent wXEvent = new WXEvent();
        wXEvent.addAll(this);
        if (getEventBindingArgs() != null) {
            wXEvent.setEventBindingArgs(getEventBindingArgs());
        }
        wXEvent.setEventBindingArgsValues((ArrayMap<String, List<Object>>) null);
        return wXEvent;
    }
}
