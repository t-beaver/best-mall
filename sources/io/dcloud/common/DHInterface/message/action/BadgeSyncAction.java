package io.dcloud.common.DHInterface.message.action;

public class BadgeSyncAction implements IAction {
    private ENUM_ACTION_TYPE mActionType;
    public int syncNumVal;

    public enum ENUM_ACTION_TYPE {
        SYNC_NUM
    }

    private BadgeSyncAction(ENUM_ACTION_TYPE enum_action_type) {
        this.mActionType = enum_action_type;
    }

    public static BadgeSyncAction obtain(ENUM_ACTION_TYPE enum_action_type) {
        return new BadgeSyncAction(enum_action_type);
    }

    public ENUM_ACTION_TYPE getActionType() {
        return this.mActionType;
    }

    public int getSyncNumVal() {
        return this.syncNumVal;
    }

    public BadgeSyncAction setSyncNum(int i) {
        this.syncNumVal = i;
        return this;
    }
}
