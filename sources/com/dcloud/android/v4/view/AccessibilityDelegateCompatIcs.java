package com.dcloud.android.v4.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

class AccessibilityDelegateCompatIcs {

    public interface AccessibilityDelegateBridge {
        boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent);

        void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent);

        void onInitializeAccessibilityNodeInfo(View view, Object obj);

        void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent);

        boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent);

        void sendAccessibilityEvent(View view, int i);

        void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent);
    }

    AccessibilityDelegateCompatIcs() {
    }

    public static boolean dispatchPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        return ((View.AccessibilityDelegate) obj).dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public static Object newAccessibilityDelegateBridge(final AccessibilityDelegateBridge accessibilityDelegateBridge) {
        return new View.AccessibilityDelegate() {
            public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                return AccessibilityDelegateBridge.this.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
            }

            public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                AccessibilityDelegateBridge.this.onInitializeAccessibilityEvent(view, accessibilityEvent);
            }

            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                AccessibilityDelegateBridge.this.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            }

            public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                AccessibilityDelegateBridge.this.onPopulateAccessibilityEvent(view, accessibilityEvent);
            }

            public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                return AccessibilityDelegateBridge.this.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }

            public void sendAccessibilityEvent(View view, int i) {
                AccessibilityDelegateBridge.this.sendAccessibilityEvent(view, i);
            }

            public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
                AccessibilityDelegateBridge.this.sendAccessibilityEventUnchecked(view, accessibilityEvent);
            }
        };
    }

    public static Object newAccessibilityDelegateDefaultImpl() {
        return new View.AccessibilityDelegate();
    }

    public static void onInitializeAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        ((View.AccessibilityDelegate) obj).onInitializeAccessibilityEvent(view, accessibilityEvent);
    }

    public static void onInitializeAccessibilityNodeInfo(Object obj, View view, Object obj2) {
        ((View.AccessibilityDelegate) obj).onInitializeAccessibilityNodeInfo(view, (AccessibilityNodeInfo) obj2);
    }

    public static void onPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        ((View.AccessibilityDelegate) obj).onPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    public static boolean onRequestSendAccessibilityEvent(Object obj, ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return ((View.AccessibilityDelegate) obj).onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }

    public static void sendAccessibilityEvent(Object obj, View view, int i) {
        ((View.AccessibilityDelegate) obj).sendAccessibilityEvent(view, i);
    }

    public static void sendAccessibilityEventUnchecked(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        ((View.AccessibilityDelegate) obj).sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }
}
