package io.dcloud.feature.uniapp.utils;

public enum UniLogLevel implements AbsLogLevel {
    OFF("off", 8, 7),
    WTF("wtf", 7, 7),
    TLOG("tlog", 6, 6),
    ERROR("error", 5, 6),
    WARN("warn", 4, 5),
    INFO("info", 3, 4),
    DEBUG("debug", 2, 3),
    VERBOSE("verbose", 1, 2),
    ALL("all", 0, 2);
    
    String name;
    int priority;
    int value;

    private UniLogLevel(String str, int i, int i2) {
        this.name = str;
        this.value = i;
        this.priority = i2;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public int getPriority() {
        return this.priority;
    }

    public int compare(AbsLogLevel absLogLevel) {
        return this.value - absLogLevel.getValue();
    }
}
