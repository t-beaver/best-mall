package com.taobao.weex.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class InstanceOnFireEventInterceptor {
    private List<String> listenEvents = new ArrayList();

    public abstract void onFireEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2);

    public void addInterceptEvent(String str) {
        if (!this.listenEvents.contains(str)) {
            this.listenEvents.add(str);
        }
    }

    public List<String> getListenEvents() {
        return this.listenEvents;
    }

    public void onInterceptFireEvent(String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        if (map != null && this.listenEvents.contains(str3)) {
            onFireEvent(str, str2, str3, map, map2);
        }
    }
}
