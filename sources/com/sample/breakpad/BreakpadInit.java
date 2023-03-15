package com.sample.breakpad;

public class BreakpadInit {
    private static native void initBreakpadNative(String str);

    static {
        System.loadLibrary("breakpad-core");
    }

    public static void initBreakpad(String str) {
        initBreakpadNative(str);
    }
}
