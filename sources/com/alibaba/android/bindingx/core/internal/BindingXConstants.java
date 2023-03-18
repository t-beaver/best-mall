package com.alibaba.android.bindingx.core.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BindingXConstants {
    public static final String KEY_ANCHOR = "anchor";
    public static final String KEY_CONFIG = "config";
    public static final String KEY_ELEMENT = "element";
    public static final String KEY_EVENT_TYPE = "eventType";
    public static final String KEY_EXIT_EXPRESSION = "exitExpression";
    public static final String KEY_EXPRESSION = "expression";
    public static final String KEY_INSTANCE_ID = "instanceId";
    public static final String KEY_INTERCEPTORS = "interceptors";
    public static final String KEY_OPTIONS = "options";
    public static final String KEY_ORIGIN = "origin";
    public static final String KEY_PROPERTY = "property";
    public static final String KEY_RUNTIME_PROPS = "props";
    public static final String KEY_SCENE_TYPE = "sceneType";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_TRANSFORMED = "transformed";
    public static final String STATE_CANCEL = "cancel";
    public static final String STATE_END = "end";
    public static final String STATE_EXIT = "exit";
    public static final String STATE_INTERCEPTOR = "interceptor";
    public static final String STATE_READY = "ready";
    public static final String STATE_START = "start";
    public static final String STATE_TURNING = "turn";
    public static final String TAG = "BindingX";

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
}
