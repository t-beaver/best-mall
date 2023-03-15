package io.dcloud.feature.uniapp.utils;

public interface AbsLogLevel {
    int compare(AbsLogLevel absLogLevel);

    String getName();

    int getPriority();

    int getValue();
}
