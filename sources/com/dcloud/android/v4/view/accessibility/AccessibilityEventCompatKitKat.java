package com.dcloud.android.v4.view.accessibility;

import android.view.accessibility.AccessibilityEvent;

class AccessibilityEventCompatKitKat {
    AccessibilityEventCompatKitKat() {
    }

    public static int getContentChangeTypes(AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getContentChangeTypes();
    }

    public static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int i) {
        accessibilityEvent.setContentChangeTypes(i);
    }
}
