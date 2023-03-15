package io.dcloud.common.DHInterface.message;

import io.dcloud.common.DHInterface.message.action.AppOnConfigChangedAction;
import io.dcloud.common.DHInterface.message.action.AppOnCreateAction;
import io.dcloud.common.DHInterface.message.action.AppOnTrimMemoryAction;
import io.dcloud.common.DHInterface.message.action.BadgeSyncAction;
import io.dcloud.common.DHInterface.message.action.IAction;
import java.util.HashMap;
import java.util.HashSet;

public class ActionBus {
    private static ActionBus instance = new ActionBus();
    HashMap<EnumUniqueID, AbsActionObserver> observers = new HashMap<>();
    HashSet<Class<? extends IAction>> supportMessageType = new HashSet<>();

    private ActionBus() {
        this.supportMessageType.add(BadgeSyncAction.class);
        this.supportMessageType.add(AppOnTrimMemoryAction.class);
        this.supportMessageType.add(AppOnConfigChangedAction.class);
        this.supportMessageType.add(AppOnCreateAction.class);
    }

    public static ActionBus getInstance() {
        return instance;
    }

    public boolean observeAction(AbsActionObserver absActionObserver) {
        if (absActionObserver == null || this.observers.containsKey(absActionObserver.getObserverUniqueID())) {
            return false;
        }
        this.observers.put(absActionObserver.getObserverUniqueID(), absActionObserver);
        return true;
    }

    public boolean sendToBus(IAction iAction) {
        if (iAction == null || !this.supportMessageType.contains(iAction.getClass())) {
            return false;
        }
        for (AbsActionObserver next : this.observers.values()) {
            if (next != null) {
                next.handleMessage(iAction);
            }
        }
        return true;
    }

    public void stopObserve(EnumUniqueID enumUniqueID) {
        this.observers.remove(enumUniqueID);
    }
}
