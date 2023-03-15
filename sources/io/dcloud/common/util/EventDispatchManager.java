package io.dcloud.common.util;

import io.dcloud.common.DHInterface.ISysEventListener;
import java.util.ArrayList;
import java.util.List;

public class EventDispatchManager {
    private static EventDispatchManager instance;
    private List<ActivityEventDispatchListener> dispatchListeners = new ArrayList();

    public interface ActivityEventDispatchListener {
        boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj);
    }

    private EventDispatchManager() {
    }

    public static EventDispatchManager getInstance() {
        if (instance == null) {
            synchronized (EventDispatchManager.class) {
                if (instance == null) {
                    instance = new EventDispatchManager();
                }
            }
        }
        return instance;
    }

    public void addListener(ActivityEventDispatchListener activityEventDispatchListener) {
        if (activityEventDispatchListener != null) {
            this.dispatchListeners.add(activityEventDispatchListener);
        }
    }

    public boolean dispatchEvent(ISysEventListener.SysEventType sysEventType, Object obj) {
        boolean z = false;
        for (int size = this.dispatchListeners.size() - 1; size >= 0; size--) {
            z = this.dispatchListeners.get(size).onExecute(sysEventType, obj);
            if (z) {
                break;
            }
        }
        return z;
    }

    public void removeListener(ActivityEventDispatchListener activityEventDispatchListener) {
        if (activityEventDispatchListener != null) {
            this.dispatchListeners.remove(activityEventDispatchListener);
        }
    }
}
